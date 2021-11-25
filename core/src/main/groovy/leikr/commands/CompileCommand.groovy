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
import leikr.loaders.EngineLoader
import org.mini2Dx.core.Mdx

/**
 *
 * @author tor
 */
class CompileCommand implements Command {

	private final GameRuntime runtime
	private final EngineLoader engineLoader

	CompileCommand(GameRuntime runtime, EngineLoader engineLoader) {
		this.runtime = runtime
		this.engineLoader = engineLoader
	}

	@Override
	String execute(String[] args) {
		if (args.length == 1) {
			return "[E] Missing - required program name."
		}
		if (!Mdx.files.external(runtime.getProgramsPath() + args[1]).exists()) {
			return "[E] Program name [${args[1]}] not in [Programs] directory."
		}
		runtime.setGameName(args[1])
		engineLoader.reset(runtime.getProgramsPath() + args[1])
		// Assumes the Programs/GameTitle/ root directory, so Code is all we need to set the compiler class path
		engineLoader.compile("Code", "/Code/Compiled")
		return "[I] Compiled project [${args[1]}]"
	}

	@Override
	String help() {
		">compile [option] \nCompiles the source code of a given program into the corresponding /Code/Compiled directory"
	}

	@Override
	String getName() {
		"compile"
	}

}
