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
package leikr.commands;

import leikr.customProperties.CustomProgramProperties;

/**
 *
 * @author tor
 */
public class SetCommand extends Command {

    @Override
    public String execute(String[] args) {
        if (args.length < 3) {
            return "[E] Not enough arguments.";
        }
        CustomProgramProperties props = new CustomProgramProperties("Programs/" + args[1]);
        switch (args[2].toLowerCase()) {
            case "author":
                props.AUTHOR = args[3];
                break;
            case "use_compiled":
                props.USE_COMPILED = Boolean.valueOf(args[3]);
                break;
            case "about":
                props.ABOUT = args[3];
                break;
            case "ver":
            case "version":
                props.VERSION = args[3];
                break;
            case "compile_source":
                props.COMPILE_SOURCE = Boolean.valueOf(args[3]);
                break;
            default:
                return "[W] Property [" + args[2] + "] not found in Program [" + args[1] + "]";
        }
        props.writeProperties("Programs/" + args[1]);
        return props.toString();
    }

    @Override
    public String help() {
        return ">set [Program] [Property] [Value] \nSets the given program's property to the given value.";
    }

}
