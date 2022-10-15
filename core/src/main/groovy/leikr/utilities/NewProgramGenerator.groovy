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
package leikr.utilities

import groovy.util.logging.Log4j2
import leikr.GameRuntime
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.files.FileHandle

import java.nio.file.Files
import java.nio.file.Path

/**
 *
 * @author Torbuntu
 */
@Log4j2
class NewProgramGenerator {

	final String NEW_LOCATION = "New Program template generated at: /Programs/"

	String maxSprites = "2048"
	String useCompiled = "false"
	String compileSource = "false"
	String title = "unknown"
	String type = "Program"
	String author = "unknown"
	String version = "0.0.0"
	String players = "1"
	String about = "A Leikr Program."

	private final GameRuntime runtime

	NewProgramGenerator(GameRuntime runtime) {
		this.runtime = runtime
	}

	String setNewProgramFileName(String newName, String template) throws IOException {
		String newProject = newName.length() > 0 ? newName : "NewProgram"

		for (FileHandle name : Mdx.files.external(runtime.getProgramsPath()).list()) {
			if (name.name().equalsIgnoreCase(newProject)) {
				return "A program with name [$newProject] already exists."
			}
		}

		String message = copyTemplate(newProject, template)
		setNewProgramClassName(newProject)
		return message
	}

	private String copyTemplate(String newProject, String template) throws IOException {
		if (!Mdx.files.external("${runtime.getDataPath()}Templates/$template").exists()) {
			throw new IOException("Templates: [$template] does not exist")
		}
		Mdx.files.external(runtime.getProgramsPath() + newProject).mkdirs()
		Mdx.files.external("${runtime.getDataPath()}Templates/$template").list().each { FileHandle file ->
			Mdx.files.external("${runtime.getDataPath()}Templates/$template/${file.name()}")
					.copyTo(Mdx.files.external(runtime.getProgramsPath() + newProject))
		}
		Mdx.files.external(runtime.getProgramsPath() + newProject + "/Code/main.groovy")
				.moveTo(Mdx.files.external("${runtime.getProgramsPath()}$newProject/Code/${newProject}.groovy"))
		return NEW_LOCATION + newProject + "/"
	}

	private void setNewProgramClassName(String newProject) throws IOException {
		Path nfPath = new File(Mdx.files.external("${runtime.getProgramsPath()}$newProject/Code/${newProject}.groovy").path()).toPath()
		String newFile = Files.readString(nfPath)
		String replace = newFile.replace("NewProgram", newProject)
		Files.writeString(nfPath, replace)
	}

	void writePropertyName(String name) {
		String propPath = Mdx.files.external("${runtime.getProgramsPath()}$name/program.properties").path()
		try (FileOutputStream fos = new FileOutputStream(propPath)) {
			Properties props = new Properties()
			props.setProperty("title", name)
			props.setProperty("type", "Program")
			props.setProperty("author", "Unknown")
			props.setProperty("version", "0.0.0")
			props.setProperty("players", "1")
			props.setProperty("about", "Leikr project")
			props.setProperty("max_sprites", "128")
			props.setProperty("compile_source", "false")
			props.setProperty("use_compiled", "false")

			props.store(fos, "Program generated with Leikr Program Generator")

		} catch (Exception ex) {
			log.error(ex)
		}
	}

	void writeProperties(String name) {
		String propPath = Mdx.files.external("${runtime.getProgramsPath()}$name/program.properties").path()
		try (FileOutputStream fos = new FileOutputStream(propPath)) {
			Properties props = new Properties()
			props.with {
				setProperty("title", this.title)
				setProperty("type", this.type)
				setProperty("author", this.author)
				setProperty("version", this.version)
				setProperty("players", this.players)
				setProperty("about", this.about)
				setProperty("max_sprites", this.maxSprites)
				setProperty("compile_source", this.compileSource)
				setProperty("use_compiled", this.useCompiled)

				store(fos, "Program generated with Leikr Program Generator")
			}
		} catch (Exception ex) {
			log.warn("Writing properties for $name had a problem: ", ex)
		}
	}
}
