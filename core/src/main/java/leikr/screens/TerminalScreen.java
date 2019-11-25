/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leikr.screens;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.ExportTool;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author Torbuntu
 */
public class TerminalScreen extends BasicGameScreen implements InputProcessor {

    public static int ID = 6;

    boolean RUN_PROGRAM = false;
    boolean NEW_PROGRAM = false;

    String out;

    FitViewport viewport;

    String prompt = "";
    String historyText = "";

    int blink = 0;

    int index = -1;
    FileHandle[] programs;

    Desktop desktop;
    
    /**
     * The list of available commands.
     * displayed when "help" with no params is run.
     */
    private final String commands = "exit, find, clear, help, ls, new, pwd, run, wiki";

    public TerminalScreen(FitViewport vp) {
        viewport = vp;
        desktop = Desktop.getDesktop();
    }

    private void setProcessor() {
        Mdx.input.setInputProcessor(this);
    }

    private String runLs() {
        try {
            out = "";
            programs = Mdx.files.local("Programs").list();
            Arrays.asList(programs).stream().forEach(e -> out += e.nameWithoutExtension() + "\n");

            return out;
        } catch (IOException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
            return "Failed to execute command ( ls )";
        }
    }

    @Override
    public void initialise(GameContainer gc) {

    }

    @Override
    public void preTransitionIn(Transition trns) {
        prompt = "";
        out = runLs();
        if (GameRuntime.GAME_NAME.length() < 2) {
            historyText = "No program loaded.";
        } else {
            historyText = "Loaded program: `" + GameRuntime.GAME_NAME + "`";
        }
        setProcessor();
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (RUN_PROGRAM) {
            RUN_PROGRAM = false;
            historyText = "";
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }
        if (NEW_PROGRAM) {
            NEW_PROGRAM = false;
            sm.enterGameScreen(NewProgramScreen.ID, null, null);
        }
        blink++;
        if (blink > 60) {
            blink = 0;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Colors.GREEN());

        g.drawString(historyText, 0, 0, 240);
        g.setColor(Colors.BLACK());
        g.fillRect(0, 152, GameRuntime.WIDTH, GameRuntime.HEIGHT);
        g.setColor(Colors.GREEN());
        g.drawString(">" + prompt + ((blink > 30) ? (char) 173 : ""), 0, 152, GameRuntime.WIDTH);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE) {
            Mdx.platformUtils.exit(true);
        }
        if (keycode == Keys.ENTER) {
            historyText = processCommand() + "\n\n";
            prompt = "";
            return true;
        }
        if (keycode == Keys.UP) {
            if (programs.length <= 0) {
                return false;
            }
            index++;
            if (index > programs.length - 1) {
                index = 0;
            }
            prompt = "run " + programs[index].nameWithoutExtension();
        }
        if (keycode == Keys.DOWN) {
            if (programs.length <= 0) {
                return false;
            }
            index--;
            if (index < 0) {
                index = programs.length - 1;
            }
            prompt = "run " + programs[index].nameWithoutExtension();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        if ((int) c >= 32 && (int) c <= 126) {
            prompt = prompt + c;
            return true;
        }
        if ((int) c == 8 && prompt.length() > 0) {
            prompt = prompt.substring(0, prompt.length() - 1);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private String processCommand() {
        String[] command = prompt.split(" ");
        switch (command[0]) {
            case "clean": {
                try {
                    Mdx.files.local("Packages/").deleteDirectory();
                    return "Package directory cleaned.";
                } catch (IOException ex) {
                    Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
                    return "Failed to clean package directory. Please check logs.";
                }
            }
            case "clear":
                return "";
            case "exit":
                Mdx.platformUtils.exit(true);
                return "Goodbye";
            case "export":
                return ExportTool.export(command[1]);
            case "exportAll":
                return ExportTool.exportAll();
            case "find":
                if (command.length <= 1) {
                    return "Missing - required program name.";
                }
                if (!Arrays.asList(out.split("\n")).contains(command[1])) {
                    return "Program [" + command[1] + "] does not exist in Programs directory.";
                }
                try {
                    File f = new File("Programs/" + command[1]);
                    desktop.open(f);
                    return f.getAbsolutePath();
                } catch (IOException ex) {
                    Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
                    return "Could not find program directory for [" + command[1] + "].";
                }
            case "help":
                if (command.length > 1) {
                    switch (command[1]) {
                        case "exit":
                            return ">exit \nExits the Leikr Game system.";
                        case "find":
                            return ">find [option] \nPrints the location of the given program name. Attempts to open the directory in the host file manager.";
                        case "clear":
                            return ">clear \nClears the terminal screen text.";
                        case "help":
                            return ">help [option] \nDisplays the help options to the screen or info about a command.";
                        case "ls":
                            return ">ls \nDisplays the contents of the Programs directory.";
                        case "new":
                            return ">new \nOpens a new project builder";
                        case "pwd":
                            return ">pwd \nPrints the location fo the Programs directory. Attempts to open the directory in the host file manager.";
                        case "run":
                            return ">run [arg] \nLoads and Runs a program given a title.";
                        case "wiki":
                            return ">wiki [option] \nOpens the Leikr wiki. Use an Option to open a specific wiki page.";
                        default:
                            return "No help for unknown command: ( " + command[1] + " )";
                    }
                }
                return "Commands: "+commands+" \n \nRun help with the name of a command for more details on that command.";
            case "install":
                return ExportTool.importProject(command[1]);
            case "ls":
                return runLs();
            case "new":
                NEW_PROGRAM = true;
                return "Create a new program.";
            case "pwd":
                try {
                    File f = new File("Programs");
                    desktop.open(f);
                    return f.getAbsolutePath();
                } catch (IOException ex) {
                    Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
                    return "Could not find workspace directory.";
                }
            case "run":
                if (command.length == 1) {
                    return "Missing - required program title.";
                }
                if (!Arrays.asList(out.split("\n")).contains(command[1])) {
                    return "Program [" + command[1] + "] does not exist in Programs directory.";
                }
                try {
                    GameRuntime.GAME_NAME = command[1];
                    GameRuntime.setProgramPath("Programs/" + command[1]);
                    RUN_PROGRAM = true;
                    return "loading...";
                } catch (Exception ex) {
                    Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
                    return "Failed to run program with name ( " + command[1] + " )";
                }

            case "wiki":
                String wiki = "https://github.com/Torbuntu/Leikr/wiki";
                if(command.length == 2){
                    wiki += "/"+command[1];
                }
                try {
                    Desktop.getDesktop().browse(new URI(wiki));
                } catch (IOException | URISyntaxException ex) {
                    return "Host browser unaccessible.";
                }
                return "Opening ["+ wiki +"] in host browser.";
            default:
                return "Uknown command: ( " + prompt + " )";
        }
    }

}
