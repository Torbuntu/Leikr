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

import java.awt.Desktop
import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author Torbuntu
 */
class PrintWorkspaceCommand implements Command {

    private final GameRuntime runtime

    PrintWorkspaceCommand(GameRuntime runtime) {
        this.runtime = runtime
    }

    @Override
    String execute(String[] args) {
        try {
            File f = new File(runtime.getProgramsPath())
            Desktop.getDesktop().open(f)
            return f.getAbsolutePath()
        } catch (IOException ex) {
            Logger.getLogger(PrintWorkspaceCommand.class.getName()).log(Level.SEVERE, null, ex)
            return "[E] Could not find workspace directory."
        }
    }

    @Override
    String help() {
        ">pwd \nPrints the location fo the Programs directory. Attempts to open the directory in the host file manager."
    }

    @Override
    String getName() {
        "pwd"
    }

}
