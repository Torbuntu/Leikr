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
package leikr.properties

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class ProgramProperties {

	int maxSprites = 128, players = 1
	boolean useCompiled = false, compileSource = false
	String title = "unknown", type = "Program", author = "unknown",
			version = "0", about = "A Leikr Program.", gameTitle = ""

	ProgramProperties(String gamePath) {
		gameTitle = gamePath.substring(gamePath.lastIndexOf("/") + 1)
		Properties prop = new Properties()
		try (InputStream stream = new FileInputStream(new File("$gamePath/program.properties"))) {
			prop.load(stream)

			maxSprites = Integer.parseInt(prop.getProperty("max_sprites")) ?: 128
			if (maxSprites > 128) {
				maxSprites = 128
			}
			useCompiled = Boolean.valueOf(prop.getProperty("use_compiled")) ?: false
			compileSource = Boolean.valueOf(prop.getProperty("compile_source")) ?: false

			title = prop.getProperty("title") ?: "unknown"
			type = prop.getProperty("type") ?: "Program"
			author = prop.getProperty("author") ?: "unknown"
			version = prop.getProperty("version") ?: "0.1"
			players = Integer.parseInt(prop?.getProperty("players"))
			about = prop.getProperty("about") ?: "A Leikr Program."
		} catch (IOException | NumberFormatException ex) {
			Logger.getLogger(ProgramProperties.class.getName()).log(Level.WARNING, "Malformed program.properties file for $gameTitle: ${ex.getCause()}")
		}
	}

	void writeProperties(String gamePath) {
		try (FileOutputStream stream = new FileOutputStream(new File( "$gamePath/program.properties"))) {
			Properties prop = new Properties()
			prop.with {
				setProperty("max_sprites", String.valueOf(this.maxSprites))
				setProperty("use_compiled", String.valueOf(this.useCompiled))
				setProperty("compile_source", String.valueOf(this.compileSource))
				setProperty("title", this.title)
				setProperty("type", this.type)
				setProperty("author", this.author)
				setProperty("version", this.version)
				setProperty("players", String.valueOf(this.players))
				setProperty("about", this.about)

				store(stream, null)
			}
		} catch (IOException | NumberFormatException ex) {
			Logger.getLogger(ProgramProperties.class.getName()).log(Level.WARNING, "Malformed program.properties file for $gameTitle: ${ex.getCause()}")
		}
	}

}
