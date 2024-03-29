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

import groovy.util.logging.Log4j2
import org.mini2Dx.core.Mdx

/**
 *
 * @author tor
 */
@Log4j2
class ProgramProperties {

	int maxSprites = 128, players = 1
	boolean useCompiled = false, compileSource = false
	String title = "unknown", type = "Program", author = "unknown",
		   version = "0", about = "A Leikr Program.", gameTitle = ""

	ProgramProperties(String gamePath) {
		if (Mdx.files.external("$gamePath/program.properties").exists()) {
			gameTitle = gamePath.substring(gamePath.lastIndexOf("/") + 1)
			Properties prop = new Properties()
			try (InputStream stream = new FileInputStream(new File("$gamePath/program.properties"))) {
				prop.load(stream)

				maxSprites = prop.max_sprites ? prop.max_sprites as int : 128
				if (maxSprites > 128) {
					maxSprites = 128
				}
				useCompiled = Boolean.valueOf(prop.use_compiled as String) ?: false
				compileSource = Boolean.valueOf(prop.compile_source as String) ?: false

				title = prop.title ?: "unknown"
				type = prop.type ?: "Program"
				author = prop.author ?: "unknown"
				version = prop.version ?: "0.1"
				players = prop.players ? prop.players as int : 1
				about = prop.about ?: "A Leikr Program."
			} catch (IOException | NumberFormatException ex) {
				log.warn("Malformed program.properties file for $gameTitle: ${ex.getMessage()}")
			}
		}
	}

	void writeProperties(String gamePath) {

		try (FileOutputStream stream = new FileOutputStream(new File("$gamePath/program.properties"))) {
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
			log.warn("Malformed program.properties file for $gameTitle: ${ex.getMessage()}")
		}
	}

}
