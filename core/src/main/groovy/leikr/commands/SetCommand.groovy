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
import leikr.customProperties.CustomProgramProperties

/**
 *
 * @author tor
 */
class SetCommand implements Command {

    private final List<String> properties
    private final GameRuntime runtime

    SetCommand(GameRuntime runtime) {
        this.runtime = runtime
        properties = [
                "author",
                "use_compiled",
                "about",
                "ver",
                "version",
                "compile_source"
        ]
    }

    @Override
    String execute(String[] args) {
        if (args.length < 3) {
            return "[E] Not enough arguments."
        }
        if (!properties.contains(args[2])) {
            return "[E] Property [${args[2]}] not found in Program [${args[1]}]"
        }
        CustomProgramProperties props = new CustomProgramProperties(runtime.getProgramsPath() + args[1])
        switch (args[2].toLowerCase()) {
            case "author": props.AUTHOR = args[3]; break
            case "use_compiled": props.USE_COMPILED = Boolean.valueOf(args[3]); break
            case "about": props.ABOUT = args[3]; break
            case "ver": props.VERSION = args[3]; break
            case "version": props.VERSION = args[3]; break
            case "compile_source": props.COMPILE_SOURCE = Boolean.valueOf(args[3]); break
        }
        props.writeProperties(runtime.getProgramsPath() + args[1])
        return "[I] Set property [${args[2]}] to [${args[3]}] for program [${args[1]}]"
    }

    @Override
    String help() {
        ">set [Program] [Property] [Value] \nSets the given program's property to the given value."
    }

    @Override
    String getName() {
        "set"
    }

}
