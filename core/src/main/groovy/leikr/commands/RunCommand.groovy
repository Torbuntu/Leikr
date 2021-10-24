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
package leikr.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.managers.TerminalManager;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class RunCommand implements Command {

    private final GameRuntime runtime;
    private final TerminalManager terminalManager;
    private final EngineLoader engineLoader;

    public RunCommand(GameRuntime runtime, TerminalManager terminalManager, EngineLoader engineLoader) {
        this.runtime = runtime;
        this.terminalManager = terminalManager;
        this.engineLoader = engineLoader;
    }

    @Override
    public String execute(String[] command) {
        if (command.length == 1) {
            return "[E] Missing - required program title.";
        }
        try {
            System.out.println(runtime.getProgramsPath());
            ArrayList<String> names = new ArrayList<>();
            Arrays.asList(Mdx.files.external(runtime.getProgramsPath()).list()).stream().forEach(e -> names.add(e.nameWithoutExtension()));
            if (!names.contains(command[1])) {
                return "[E] Program [" + command[1] + "] does not exist in Programs directory.";
            }
            runtime.setGameName(command[1]);
            if (command.length > 2) {
                String[] args = Arrays.copyOfRange(command, 2, command.length);
                engineLoader.setEngineArgs(args);
            }
            terminalManager.setProgramRunning();
            return "[I] Loading...";
        } catch (IOException ex) {
            Logger.getLogger(RunCommand.class.getName()).log(Level.WARNING, null, ex);
            return "[E] Failed to run program with name [ " + command[1] + " ]";
        }
    }

    @Override
    public String help() {
        return ">run [option] [args...] \nLoads and Runs a program given a title. Optional args can be passed.";
    }

    @Override
    public String getName() {
        return "run";
    }

}
