/*
 * Copyright 2020 tor.
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
 * @author tor
 */
class PrintCommand implements Command {

    private final GameRuntime runtime

    PrintCommand(GameRuntime runtime) {
        this.runtime = runtime
    }

    @Override
    String execute(String[] command) {
        String adjustedPath = runtime.getProgramsPath() + command[1]
        if (command.length <= 1) {
            return "[E] Missing - required name argument."
        }
        if(!Mdx.files.external(adjustedPath).exists()){
            return "[E] File [" + command[1] + "] not found."
        }
        try {
            return Mdx.files.external(adjustedPath).readString()
        } catch (IOException ex) {
            Logger.getLogger(PrintCommand.class.getName()).log(Level.SEVERE, null, ex)
            return "[E] Failed to print contents of file [" + command[1] + "]"
        }

    }

    @Override
    String help() {
        ">cat [file] \nPrints the contents of a file to the terminal.\nTerminal does not currently scroll contents."
    }

    @Override
    String getName() {
        "cat"
    }

}
