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
import leikr.utilities.ExportTool

/**
 *
 * @author Torbuntu
 */
class InstallCommand implements Command {

    private final ExportTool exportTool
    private final GameRuntime runtime

    InstallCommand(GameRuntime runtime, ExportTool exportTool) {
        this.runtime = runtime
        this.exportTool = exportTool
    }

    @Override
    String execute(String[] command) {
        if (command.length <= 1) {
            return "[E] Missing - required name argument."
        }
        if (command.length == 3) {
            return exportTool.importProject(command[1], command[2])
        } else {
            return exportTool.importProject(command[1], runtime.getProgramsPath())
        }
    }

    @Override
    String help() {
        ">install [name] [option]\nInstalls a .lkr package from the Packages directory into the Programs directory. Can optionally direct where to install a project given a path."
    }

    @Override
    String getName() {
        "install"
    }

}
