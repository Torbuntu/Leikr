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
package leikr.commands

import leikr.GameRuntime
import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author Torbuntu
 */
class RemoveCommand implements Command {

    private final GameRuntime runtime

    RemoveCommand(GameRuntime runtime) {
        this.runtime = runtime
    }

    @Override
    String execute(String[] command) {
        if (command.length <= 1) {
            return "[E] Missing - required program name."
        }
        if (!containsName(command[1])) {
            return "[E] Program [" + command[1] + "] does not exist in Programs directory."
        }
        try {
            Mdx.files.external(runtime.getProgramsPath() + command[1]).deleteDirectory()
            return "[I] Program [" + command[1] + "] has been uninstalled."
        } catch (IOException ex) {
            Logger.getLogger(RemoveCommand.class.getName()).log(Level.SEVERE, null, ex)
            return "[E] Could not uninstall [" + command[1] + "]"
        }

    }

    private boolean containsName(String name) {
        try {
            ArrayList<String> names = new ArrayList<>()
            Arrays.asList(Mdx.files.external(runtime.getProgramsPath()).list()).stream().forEach(e -> names.add(e.nameWithoutExtension()))
            return names.contains(name)
        } catch (IOException ex) {
            Logger.getLogger(RemoveCommand.class.getName()).log(Level.SEVERE, null, ex)
            return false
        }
    }

    @Override
    String help() {
        ">uninstall [name] \nUninstalls a program in the Programs directory given the name."
    }

    @Override
    String getName() {
        "uninstall"
    }
}
