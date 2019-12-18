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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import leikr.Commands.AboutCommand;
import leikr.Commands.CleanCommand;
import leikr.Commands.Command;
import leikr.Commands.ExitCommand;
import leikr.Commands.ExportCommand;
import leikr.Commands.FindCommand;
import leikr.Commands.InstallCommand;
import leikr.Commands.NewProgramCommand;
import leikr.Commands.PrintDirectoryCommand;
import leikr.Commands.PrintWorkspaceCommand;
import leikr.Commands.RemoveCommand;
import leikr.Commands.RunCommand;
import leikr.Commands.ToolCommand;
import leikr.Commands.WikiCommand;
import leikr.GameRuntime;
import leikr.screens.TerminalScreen;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public class TerminalManager implements InputProcessor {

    public String prompt = "";
    public String historyText = "";

    Map<String, Command> commandList;

    public static enum TerminalState {
        PROCESSING,
        RUN_PROGRAM,
        NEW_PROGRAM,
        RUN_UTILITY
    }

    public static TerminalState terminalState;

    /**
     * The list of available commands. displayed when "help" with no params is
     * run.
     */
    public TerminalManager() {
        terminalState = TerminalState.PROCESSING;
        commandList = new HashMap<>();
        commandList.put("about", new AboutCommand());
        commandList.put("ls", new PrintDirectoryCommand());
        commandList.put("new", new NewProgramCommand());
        commandList.put("exit", new ExitCommand());
        commandList.put("run", new RunCommand());
        commandList.put("find", new FindCommand());
        commandList.put("clean", new CleanCommand());
        commandList.put("pwd", new PrintWorkspaceCommand());
        commandList.put("wiki", new WikiCommand());
        commandList.put("export", new ExportCommand());
        commandList.put("install", new InstallCommand());
        commandList.put("tool", new ToolCommand());
        commandList.put("uninstall", new RemoveCommand());

    }

    public static void setState(TerminalState state) {
        terminalState = state;
    }

    public TerminalState getState() {
        return terminalState;
    }

    public void init() {
        terminalState = TerminalState.PROCESSING;
        prompt = "";
        if (GameRuntime.GAME_NAME.length() < 2) {
            historyText = "No program loaded.";
        } else {
            historyText = "Closed program: [" + GameRuntime.GAME_NAME + "]";
        }
    }

    String getAllHelp() {
        ArrayList<String> output = new ArrayList<>();
        commandList.keySet().forEach((h) -> {
            output.add(commandList.get(h).name);
        });
        output.sort(String::compareToIgnoreCase);
        return output.stream().collect(Collectors.joining(", "));
    }

    String getSpecificHelp(String name) {
        if (!commandList.containsKey(name)) {
            return "No help for unknown command: [ " + name + " ]";
        }
        return commandList.get(name).help();
    }

    public String processCommand() {
        String[] command = prompt.split(" ");
        if (command[0].equalsIgnoreCase("help")) {
            if (command.length > 1) {
                return getSpecificHelp(command[1]);
            } else {
                return getAllHelp();
            }
        }
        if (!commandList.containsKey(command[0])) {
            return "Unknown command [" + command[0] + "]";
        }
        try {
            Command c = commandList.get(command[0]);
            return c.execute(command);
        } catch (Exception ex) {
            Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Unknown command [" + command[0] + "]";
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
            //TODO - Command history
        }
        if (keycode == Input.Keys.DOWN) {
            //TODO - Command history
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
