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

import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author Torbuntu
 */
class CleanCommand implements Command {

    CleanCommand() {
    }

    @Override
    String execute(String[] args) {
        try {
            Mdx.files.external(System.getProperty("user.home") + "/Leikr/Packages/").deleteDirectory()
            Mdx.files.external(System.getProperty("user.home") + "/Leikr/Packages").mkdirs()
            return "Package directory cleaned."
        } catch (IOException ex) {
            Logger.getLogger(CleanCommand.class.getName()).log(Level.SEVERE, null, ex)
            return "Failed to clean Package directory. Please check logs."
        }
    }

    @Override
    String help() {
        ">clean \nRemoves all .lkr packages from the Packages directory."
    }

    @Override
    String getName() {
        "clean"
    }

}
