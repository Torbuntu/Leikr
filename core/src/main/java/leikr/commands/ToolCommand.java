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
package leikr.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.managers.TerminalManager;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author Torbuntu
 */
public class ToolCommand extends Command {

    private String out;

    private final GameRuntime runtime;
    private final TerminalManager terminalManager;

    public ToolCommand(GameRuntime runtime, TerminalManager terminalManager) {
        super.name = "tool";
        this.runtime = runtime;
        this.terminalManager = terminalManager;
    }

    @Override
    public String execute(String[] command) {
        if (command.length == 1) {
            try {
                out = "";
                Arrays.asList(Mdx.files.external(runtime.getDataPath() + "Tools").list()).stream().forEach(e -> out += e.nameWithoutExtension() + "\n");
                return out;
            } catch (IOException ex) {
                Logger.getLogger(ToolCommand.class.getName()).log(Level.WARNING, null, ex);
                return "Failed to list tools.";
            }
        } else {
            try {
                if (!containsName(command[1])) {
                    return "Tool [" + command[1] + "] does not exist in Data/Tools/ directory.";
                }
                runtime.setGameName(command[1]);
                terminalManager.setToolRunning();
                return "Running tool [" + command[1] + "].";
            } catch (Exception ex) {
                Logger.getLogger(ToolCommand.class.getName()).log(Level.WARNING, null, ex);
                return "Failed to run tool with name [ " + command[1] + " ]";
            }

        }
    }

    private boolean containsName(String name) {
        try {
            ArrayList<String> names = new ArrayList<>();
            Arrays.asList(Mdx.files.external(runtime.getDataPath() + "Tools").list()).stream().forEach(e -> names.add(e.nameWithoutExtension()));
            return names.contains(name);
        } catch (IOException ex) {
            Logger.getLogger(ToolCommand.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public String help() {
        return ">tool [option] \nLoads and Runs a tool given a name. If no arguments given then will display all installed tools.";
    }

}
