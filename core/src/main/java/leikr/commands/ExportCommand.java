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

import leikr.utilities.ExportTool;

/**
 *
 * @author Torbuntu
 */
public class ExportCommand extends Command {

    public ExportCommand() {
        super.name = "export";
    }

    @Override
    public String execute(String[] command) {
        if (command.length <= 1) {
            return "Missing - required name argument.";
        }
        if (command[1].equalsIgnoreCase("all")) {
            return ExportTool.exportAll();
        } else {
            return ExportTool.export(command[1]);
        }
    }

    @Override
    public String help() {
        return ">export [name] \nPackages a program by name into the Packages directory as .lkr package for sharing. Run with `all` argument to export all programs. See `install` command.";
    }

}
