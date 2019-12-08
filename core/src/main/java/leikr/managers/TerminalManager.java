/*
 * Copyright 2019 See AUTHORS.
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
package leikr.managers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.ExportTool;
import leikr.GameRuntime;
import leikr.NewProgramGenerator;
import leikr.loaders.EngineLoader;
import leikr.screens.EngineScreen;
import leikr.screens.TerminalScreen;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public class TerminalManager implements InputProcessor {

    FileHandle[] programs;
    int index = -1;

    Desktop desktop;

    public String prompt = "";
    public String historyText = "";
    String out;

    public enum TerminalState {
        PROCESSING,
        RUN_PROGRAM,
        NEW_PROGRAM,
        RUN_UTILITY
    }

    TerminalState terminalState;

    /**
     * The list of available commands. displayed when "help" with no params is
     * run.
     */
    private final String commands = "about, clear, exit, find, help, ls, new, pwd, run, tool, tools, wiki";

    public TerminalManager() {
        terminalState = TerminalState.PROCESSING;
        desktop = Desktop.getDesktop();
    }

    public void setState(TerminalState state) {
        terminalState = state;
    }

    public TerminalState getState() {
        return terminalState;
    }

    public void init() {
        terminalState = TerminalState.PROCESSING;
        prompt = "";
        out = runLs("Programs");
        if (GameRuntime.GAME_NAME.length() < 2) {
            historyText = "No program loaded.";
        } else {
            historyText = "Closed program: [" + GameRuntime.GAME_NAME + "]";
        }
    }

    public String processCommand() {
        String[] command = prompt.split(" ");
        if (command.length > 0) {

            switch (command[0].toLowerCase()) {
                case "about":
                    if (command.length <= 1) {
                        return "Pass a program name to get the program's about info.";
                    }
                    if (!Arrays.asList(out.split("\n")).contains(command[1])) {
                        return "Program [" + command[1] + "] does not exist in Programs directory.";
                    }
                    try (InputStream stream = new FileInputStream(new File("Programs/" + command[1] + "/program.properties"))) {
                        Properties prop = new Properties();
                        prop.load(stream);
                        return ((prop.getProperty("about") != null) ? prop.getProperty("about") : "A Leikr Program.");
                    } catch (Exception ex) {
                        Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
                        return "Failed to clean package directory. Please check logs.";
                    }
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
                            case "about":
                                return ">about [option]\nReads the about property of the given program name.";
                            case "exit":
                                return ">exit \nExits the Leikr Game system.";
                            case "find":
                                return ">find [option] \nPrints the location of the given program name. Attempts to open the directory in the host file manager.";
                            case "clear":
                                return ">clear \nClears the terminal screen text.";
                            case "help":
                                return ">help [option] \nDisplays the help options to the screen or info about a command.";
                            case "ls":
                                return ">ls [option] \nDisplays the contents of a given directory or the default directory Programs.";
                            case "new":
                                return ">new [option]\nOpens a new project builder.\nIf run with option, will attempt to generate a project with the given name.";
                            case "pwd":
                                return ">pwd \nPrints the location fo the Programs directory. Attempts to open the directory in the host file manager.";
                            case "run":
                                return ">run [option] [args...] \nLoads and Runs a program given a title. Optional args can be passed.";
                            case "tool":
                                return ">tool [option] \nLoads and Runs a tool given a title.";
                            case "tools":
                                return ">tools \nDisplays the contents of the Data/Tools directory.";
                            case "wiki":
                                return ">wiki [option] \nOpens the Leikr wiki. Use an Option to open a specific wiki page.";
                            default:
                                return "No help for unknown command: [ " + command[1] + " ]";
                        }
                    }
                    return "Commands: " + commands + " \n \nRun help with the name of a command for more details on that command.";
                case "install":
                    return ExportTool.importProject(command[1]);
                case "ls":
                    if (command.length > 1) {
                        return runLs(command[1]);
                    } else {
                        return runLs("Programs");

                    }
                case "new":
                    if (command.length == 2) {
                        try {
                            return NewProgramGenerator.setNewProgramFileName(command[1], "Default");
                        } catch (IOException ex) {
                            Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
                            return "New program with name [" + command[1] + "] failed to generate.";
                        }
                    }
                    setState(TerminalState.NEW_PROGRAM);
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
                case "rn":
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
                        if (command.length > 2) {
                            String[] args = Arrays.copyOfRange(command, 2, command.length);
                            EngineScreen.setEngineArgs(args);
                        }
                        setState(TerminalState.RUN_PROGRAM);
                        return "loading...";
                    } catch (Exception ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
                        return "Failed to run program with name [ " + command[1] + " ]";
                    }
                case "tool":
                    if (command.length == 1) {
                        return "Missing - required tool title.";
                    }
                    try {
                        runLs("Data/Tools");
                        if (!Arrays.asList(out.split("\n")).contains(command[1])) {
                            return "Tool [" + command[1] + "] does not exist in Data/Tools/ directory.";
                        }
                        GameRuntime.GAME_NAME = command[1];
                        GameRuntime.setProgramPath("Data/Tools/" + command[1]);
                        setState(TerminalState.RUN_UTILITY);
                    } catch (Exception ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
                        return "Failed to run tool with name [ " + command[1] + " ]";
                    }

                    return "Running [" + command[1] + "] tool.";

                case "tools":
                    return runLs("Data/Tools");
                case "wiki":
                    String wiki = "https://github.com/Torbuntu/Leikr/wiki";
                    if (command.length == 2) {
                        wiki += "/" + command[1];
                    }
                    try {
                        Desktop.getDesktop().browse(new URI(wiki));
                    } catch (IOException | URISyntaxException ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
                        return "Host browser unaccessible.";
                    }
                    return "Opening [" + wiki + "] in host browser.";
                default:
                    return "Uknown command: ( " + prompt + " )";
            }
        } else {
            return "";
        }
    }

    public String runLs(String dir) {
        try {
            out = "";
            programs = Mdx.files.local(dir).list();
            Arrays.asList(programs).stream().forEach(e -> out += e.nameWithoutExtension() + "\n");
            return out;
        } catch (IOException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.WARNING, null, ex);
            return "Failed to execute command [ ls ]";
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Mdx.platformUtils.exit(true);
        }
        if (keycode == Input.Keys.ENTER) {
            historyText = processCommand() + "\n\n";
            prompt = "";
            return true;
        }
        if (keycode == Input.Keys.UP) {
            if (programs.length <= 0) {
                return false;
            }
            index++;
            if (index > programs.length - 1) {
                index = 0;
            }
            prompt = "run " + programs[index].nameWithoutExtension();
        }
        if (keycode == Input.Keys.DOWN) {
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
}
