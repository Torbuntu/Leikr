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

import leikr.GameRuntime
import leikr.managers.TerminalManager
import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author Torbuntu
 */
class ToolCommand implements Command {

	private String out

	private final GameRuntime runtime
	private final TerminalManager terminalManager

	ToolCommand(GameRuntime runtime, TerminalManager terminalManager) {
		this.runtime = runtime
		this.terminalManager = terminalManager
	}

	@Override
	String execute(String[] command) {
		if (command.length == 1) {
			try {
				out = ""
				Mdx.files.external(runtime.getDataPath() + "Tools").list().each { e ->
					out += e.nameWithoutExtension() + "\n"
				}
				return out
			} catch (IOException ex) {
				Logger.getLogger(ToolCommand.class.getName()).log(Level.WARNING, null, ex)
				return "[E] Failed to list tools."
			}
		} else {
			try {
				if (!containsName(command[1])) {
					return "[E] Tool [${command[1]}] does not exist in Data/Tools/ directory."
				}
				runtime.setGameName(command[1])
				terminalManager.setToolRunning()
				return "[I] Running tool [${command[1]}]."
			} catch (Exception ex) {
				Logger.getLogger(ToolCommand.class.getName()).log(Level.WARNING, null, ex)
				return "[E] Failed to run tool with name [${command[1]}]"
			}

		}
	}

	private boolean containsName(String name) {
		try {
			def names = []
			Mdx.files.external(runtime.getDataPath() + "Tools").list().each { e ->
				names.add(e.nameWithoutExtension())
			}
			return names.contains(name)
		} catch (IOException ex) {
			Logger.getLogger(ToolCommand.class.getName()).log(Level.SEVERE, null, ex)
			return false
		}
	}

	@Override
	String help() {
		">tool [option] \nLoads and Runs a tool given a name. If no arguments given then will display all installed tools."
	}

	@Override
	String getName() {
		"tool"
	}

}
