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

import java.awt.Desktop
import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author Torbuntu
 */
class WikiCommand implements Command {

    WikiCommand() {
    }

    @Override
    String execute(String[] command) {
        String wiki = "https://github.com/Torbuntu/Leikr/wiki"
        if (command.length == 2) {
            wiki += "/${command[1]}"
        }
        try {
            Desktop.getDesktop().browse(new URI(wiki))
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(WikiCommand.class.getName()).log(Level.WARNING, null, ex)
            return "[E] Host browser inaccessible."
        }
        return "[I] Opening [$wiki] in host browser."
    }

    @Override
    String help() {
        ">wiki [option] \nOpens the Leikr wiki. Use an Option to open a specific wiki page."
    }

    @Override
    String getName() {
        "wiki"
    }

}
