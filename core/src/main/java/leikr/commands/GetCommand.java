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
public class GetCommand extends Command {

    public GetCommand() {
        this.name = "get";
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 2) {
            return "[E] Not enough arguments.";
        }
        CustomProgramProperties props = new CustomProgramProperties("Programs/" + args[1]);
        return switch (args[2].toLowerCase()) {
            case "author" ->
                "author - " + props.AUTHOR;
            case "use_compiled" ->
                "use_compiled - " + String.valueOf(props.USE_COMPILED);
            case "about" ->
                "about - " + props.ABOUT;
            case "ver", "version" ->
                "version - " + props.VERSION;
            case "compile_source" ->
                "compile_source - " + String.valueOf(props.COMPILE_SOURCE);
            case "players" ->
                String.valueOf(props.PLAYERS);
            case "type" ->
                props.TYPE;
            default ->
                "[W] Property [" + args[2] + "] not found in Program [" + args[1] + "]";
        };
    }

    @Override
    public String help() {
        return ">get [Program] [Property] \nGets the value of the given Program's property.";
    }

}
