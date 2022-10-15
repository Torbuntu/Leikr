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
		return switch (args[2].toLowerCase()) {
			case "author" -> "author - ${props.author}"
			case "use_compiled" -> "use_compiled - ${String.valueOf(props.useCompiled)}"
			case "about" -> "about - ${props.about}"
			case "ver", "version" -> "version - ${props.version}"
			case "compile_source" -> "compile_source - ${String.valueOf(props.compileSource)}"
			case "players" -> "players - ${String.valueOf(props.players)}"
			case "type" -> "type - ${props.type}"
			default -> "[W] Property [${args[2]}] not found in Program [${args[1]}]"
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
