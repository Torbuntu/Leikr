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
import leikr.properties.ProgramProperties

/**
 *
 * @author tor
 */
class GetCommand implements Command {

    private final GameRuntime runtime

    GetCommand(GameRuntime runtime) {
        this.runtime = runtime
    }

    @Override
    String execute(String[] args) {
        if (args.length < 2) {
            return "[E] Not enough arguments."
        }
        ProgramProperties props = new ProgramProperties(runtime.getProgramsPath() + args[1])
        switch (args[2].toLowerCase()) {
            case "author": return "author - ${props.AUTHOR}"
            case "use_compiled": return "use_compiled - ${String.valueOf(props.USE_COMPILED)}"
            case "about": return "about - ${props.ABOUT}"
            case "ver":
            case "version": return "version - ${props.VERSION}"
            case "compile_source": return "compile_source - ${String.valueOf(props.COMPILE_SOURCE)}"
            case "players": return "players - ${String.valueOf(props.PLAYERS)}"
            case "type": return "type - ${props.TYPE}"
            default: return "[W] Property [${args[2]}] not found in Program [${args[1]}]"
        }
    }

    @Override
    String help() {
        ">get [Program] [Property] \nGets the value of the given Program's property."
    }

    @Override
    String getName() {
        "get"
    }

}
