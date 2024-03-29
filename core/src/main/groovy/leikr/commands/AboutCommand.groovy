/*
 * Copyright 2019 See AUTHORS.
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

import groovy.util.logging.Log4j2
import leikr.GameRuntime
import leikr.properties.ProgramProperties
import org.mini2Dx.core.Mdx

/**
 *
 * @author tor
 */
@Log4j2
class AboutCommand implements Command {

	private final GameRuntime runtime

	AboutCommand(GameRuntime runtime) {
		this.runtime = runtime
	}

	@Override
	String execute(String[] command) {
		if (command.length <= 1) {
			return "Pass a program name to get the program's about info."
		}
		if (!containsName(command[1])) {
			return "Program [${command[1]}] does not exist in Programs directory."
		}
		try {
			ProgramProperties cpp = new ProgramProperties(runtime.getProgramsPath() + command[1])
			return "Title: ${cpp.title} \nType: ${cpp.type} \nPlayers: ${cpp.players} \nAuthor: ${cpp.author} \nAbout: ${cpp.about}"
		} catch (Exception ex) {
			log.error(ex)
			return "Failed to load property file for [${command[1]}]."
		}
	}

	boolean containsName(String name) {
		try {
			def names = []
			Mdx.files.external(runtime.getProgramsPath()).list().each(e -> names.add(e.nameWithoutExtension()))
			return names.contains(name)
		} catch (IOException ex) {
			log.error(ex)
			return false
		}
	}

	@Override
	String help() {
		">about [name]\nReads the property file of the given program name."
	}

	@Override
	String getName() {
		"about"
	}

}
