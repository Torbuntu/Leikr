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
package leikr.managers

import com.badlogic.gdx.Gdx
import leikr.GameRuntime
import leikr.commands.*
import leikr.loaders.EngineLoader
import leikr.utilities.ExportTool
import org.mini2Dx.core.Mdx
import org.mini2Dx.gdx.Input.Keys
import org.mini2Dx.gdx.InputProcessor

import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors
/**
 *
 * @author tor
 */
class TerminalManager implements InputProcessor {

    private String prompt = ""
    private String historyText = ""
	
	private Map<String, Command> commandList
    private ArrayList<String> history
	private ArrayList<String> programList

    private int index
    private int programIndex

    private TerminalState terminalState

    private final GameRuntime runtime
    private final ExportTool exportTool

    /**
     * The list of available commands.displayed when "help" without params is
     * run.
     *
     * @param runtime
     * @param engineLoader
     */
    TerminalManager(GameRuntime runtime, EngineLoader engineLoader) {
        history = []
        programList = []
        commandList = [:]
        terminalState = TerminalState.PROCESSING
        exportTool = new ExportTool(runtime)

        try {
            Arrays.stream(Mdx.files.local("Programs/").list())
                    .filter(e -> e.isDirectory())
                    .forEach(game -> programList.add(game.nameWithoutExtension()))
            if (programList.size() > 0) {
                programIndex = programList.size()
            }
        } catch (Exception ex) {
            Logger.getLogger(TerminalManager.class.getName()).log(Level.SEVERE, null, ex)
        }
        commandList["about"] = new AboutCommand(runtime)
        commandList["cat"] = new PrintCommand(runtime)
        commandList["ls"] = new PrintDirectoryCommand(runtime)
        commandList["new"] = new NewProgramCommand(runtime, this)
        commandList["exit"] = new ExitCommand()
        commandList["run"] = new RunCommand(runtime, this, engineLoader)
        commandList["find"] = new FindCommand(runtime)
        commandList["clean"] = new CleanCommand()
        commandList["pwd"] = new PrintWorkspaceCommand(runtime)
        commandList["wiki"] = new WikiCommand()
        commandList["package"] = new PackageCommand(exportTool)
        commandList["install"] = new InstallCommand(runtime, exportTool)
        commandList["tool"] = new ToolCommand(runtime, this)
        commandList["uninstall"] = new RemoveCommand(runtime)
        commandList["deploy"] = new DeployCommand(runtime, exportTool)
        commandList["compile"] = new CompileCommand(runtime, engineLoader)
        commandList["get"] = new GetCommand(runtime)
        commandList["set"] = new SetCommand(runtime)
        commandList["home"] = new HomeCommand(runtime)
        commandList["version"] = new VersionCommand()

        this.runtime = runtime
    }

    String getPrompt() {
        prompt
    }

    String getHistoryText() {
        historyText
    }

    void setToolRunning() {
        terminalState = TerminalState.RUN_UTILITY
    }

    void setNewProgramRunning() {
        terminalState = TerminalState.NEW_PROGRAM
    }

    void setProgramRunning() {
        terminalState = TerminalState.RUN_PROGRAM
    }

    void setState(TerminalState state) {
        terminalState = state
    }

    TerminalState getState() {
        terminalState
    }

    void init() {
        terminalState = TerminalState.PROCESSING
        prompt = ""
        index = history.size() - 1
        if (runtime.getGameName().length() < 2) {
            historyText = "No program loaded."
        } else {
            historyText = "Closed program: [${runtime.getGameName()}]"
        }
    }

    void update() {
        if (programList.size() <= 0) {
            return
        }
        if (Mdx.input.isKeyJustPressed(Keys.PAGE_UP)) {
            if (programIndex > 0) {
                programIndex--
            } else {
                programIndex = programList.size() - 1
            }
            prompt = "run ${programList.get(programIndex)}"
        }
        if (Mdx.input.isKeyJustPressed(Keys.PAGE_DOWN)) {
            if (programIndex < programList.size() - 1) {
                programIndex++
            } else {
                programIndex = 0
            }
            prompt = "run ${programList.get(programIndex)}"
        }

    }

    String getAllHelp() {
        ArrayList<String> output = new ArrayList<>()
        commandList.keySet().forEach((h) -> {
            output.add(commandList.get(h).getName())
        })
        output.sort(String::compareToIgnoreCase)
        return output.stream().collect(Collectors.joining(", "))
    }

    String getSpecificHelp(String name) {
        if (!commandList.containsKey(name)) {
            return "No help for unknown command: [ ${name} ]"
        }
        return commandList.get(name).help()
    }

    String processCommand() {
        history.add(prompt)
        if (history.size() > 20) {
            history.remove(0)
        } else {
            index = history.size() - 1
        }
        String[] command = prompt.split(" ")
        if (command[0].equalsIgnoreCase("help")) {
            if (command.length > 1) {
                return getSpecificHelp(command[1])
            } else {
                return getAllHelp()
            }
        }
        if (!commandList.containsKey(command[0])) {
            return "Unknown command [${command[0]}]"
        }
        try {
            Command c = commandList.get(command[0])
            return c.execute(command)
        } catch (Exception ex) {
            Logger.getLogger(TerminalManager.class.getName()).log(Level.SEVERE, null, ex)
            return "[E] ${command[0]} failed with exception: ${ex.getMessage()}"
        }
    }

    @Override
    boolean keyDown(int keycode) {
        if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT)) {
            switch (keycode) {
                case Keys.NUM_1: Gdx.graphics.setWindowedMode(240, 160); break
                case Keys.NUM_2: Gdx.graphics.setWindowedMode(240 * 2, 160 * 2); break
                case Keys.NUM_3: Gdx.graphics.setWindowedMode(240 * 3, 160 * 3); break
                case Keys.NUM_4: Gdx.graphics.setWindowedMode(240 * 4, 160 * 4); break
            }

            return true
        }
        if (keycode == Keys.ESCAPE) {
            Mdx.platformUtils.exit(true)
        }
        if (keycode == Keys.ENTER) {
            historyText = processCommand() + "\n\n"
            prompt = ""
            return true
        }
        if (keycode == Keys.UP) {
            if (history.size() > 0) {
                prompt = history.get(index)
                if (index > 0) {
                    index--
                }
            }
        }
        if (keycode == Keys.DOWN) {
            if (history.size() > 0) {
                if (index < history.size() - 1) {
                    index++
                    prompt = history.get(index)
                } else {
                    prompt = ""
                }
            }
        }
        return false
    }

    @Override
    boolean keyUp(int keycode) {
        return false
    }

    @Override
    boolean keyTyped(char c) {
        if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT)) {
            return true
        }
        if ((int) c >= 32 && (int) c <= 126) {
            prompt += c
            return true
        }
        if ((int) c == 8 && prompt.length() > 0) {
            prompt = prompt.substring(0, prompt.length() - 1)
        }
        return false
    }

    @Override
    boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchDragged(int screenX, int screenY, int pointer) {
        return false
    }

    @Override
    boolean mouseMoved(int screenX, int screenY) {
        return false
    }

    @Override
    boolean scrolled(float amount, float i) {
        return false
    }

    enum TerminalState {
        INSTALLING,
        PROCESSING,
        RUN_PROGRAM,
        NEW_PROGRAM,
        RUN_UTILITY
    }
}
