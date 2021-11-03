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
package leikr.commands

import leikr.GameRuntime
import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author tor
 */
class PrintDirectoryCommand implements Command {

    private String out
    private final GameRuntime runtime

    PrintDirectoryCommand(GameRuntime runtime) {
        this.runtime = runtime
    }

    @Override
    String execute(String[] param) {
        return param.length > 1 ? runLs(param[1]) : runLsPrograms()
    }

    @Override
    String help() {
        ">ls [Directory] \nDisplays the contents of a given directory or the default directory Programs."
    }

    private String runLsPrograms() {
        try {
            out = ""
            List<String> titles = new ArrayList<>()

            Arrays.asList(Mdx.files.external(runtime.getProgramsPath()).list()).forEach(e -> titles.add(e.name()))

            titles.stream().sorted().forEach(e -> {
                out = Mdx.files.external(runtime.getProgramsPath() + "$e/Code/Compiled").exists()
                        ? e + " *\n" : e + "\n"
            })
            return out
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex)
            return "[E] Failed to execute command [ ls ]"
        }
    }

    private String runLs(String dir) {
        try {
            out = ""
            def titles = []
            Arrays.asList(Mdx.files.external(runtime.getProgramsPath() + dir).list()).forEach(e -> titles.add(e.name()))

            titles.stream().sorted().forEach(e -> out += e + "\n")
            return out
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex)
            return "[E] Failed to execute command [ls]"
        }
    }

    @Override
    String getName() {
        "ls"
    }

}
