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
package leikr.Commands;

import leikr.ExportTool;

/**
 *
 * @author Torbuntu
 */
public class InstallCommand extends Command {

    public InstallCommand() {
        super.name = "install";
    }

    @Override
    public String execute(String[] command) {
        if (command.length <= 1) {
            return "Missing - required name argument.";
        }
        if (command.length == 3) {
            return ExportTool.importProject(command[1], command[2]);
        } else {
            return ExportTool.importProject(command[1], "Programs");
        }
    }

    @Override
    public String help() {
        return ">install [name] [option]\nInstalls a .lkr package from the Packages directory into the Programs directory. Can optionally direct where to install a project given a path.";
    }

}
