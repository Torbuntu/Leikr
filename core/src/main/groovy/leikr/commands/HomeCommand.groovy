/*
 * Copyright 2020 tor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import java.util.logging.Level
import java.util.logging.Logger
import leikr.GameRuntime

/**
 *
 * @author tor
 */
class HomeCommand implements Command {

    private final GameRuntime runtime
    
    HomeCommand(GameRuntime runtime){
        this.runtime = runtime
    }

    @Override
    String execute(String[] args) {
        try {
            System.setProperty("leikr.home", args[1])
            runtime.setLeikrHome(System.getProperty("leikr.home"))
        } catch (Exception ex) {
            Logger.getLogger(HomeCommand.class.getName()).log(Level.SEVERE, null, ex)
            return "[E] Unable to set LEIKR_HOME: "
        }
        return "[I] Set LEIKR_HOME to [${args[1]}]"
    }

    @Override
    String help() {
        "home [path] \nSets the LEIKR_HOME property telling the host where Leikr's home directory should be."
    }

    @Override
    String getName() {
        "home"
    }

}
