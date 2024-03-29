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

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 *
 * @author tor
 */
@Log4j2
class ExportTool {

	private final GameRuntime runtime

	ExportTool(GameRuntime runtime) {
		this.runtime = runtime
	}

	String exportAll() {
		try {
			Mdx.files.external(runtime.getProgramsPath()).list().each { file ->
				zip(file.name())
			}
			return "Projects exported."
		} catch (IOException ex) {
			log.error(ex)
		}
		return "[E] Failed to export all projects."
	}

	String export(String project) {
		try {
			zip(project)
			return "Package [$project] exported successfully. Check Packages directory."
		} catch (Exception ex) {
			log.error(ex)
		}
		return "Failure to export Package. Please check logs."
	}

	String importProject(String project, String location) {
		try {
			unzip(project, location)
			return "Package [$project] installed successfully. Check [$location]."
		} catch (Exception ex) {
			log.error(ex)
		}
		return "[E] Failure to install Package. Please check logs."
	}

	void zip(String path) {
		File exportDir = new File(Mdx.files.external(runtime.getPackagePath()).path())
		if (!exportDir.exists()) {
			exportDir.mkdirs()
		}
		zipFolder(new File(Mdx.files.external(runtime.getProgramsPath() + path).path()).toPath(),
				new File(Mdx.files.external(runtime.getPackagePath() + path).path() + ".lkr").toPath())
	}

	void deployPackage(String name) {
		zipFolder(new File(Mdx.files.external(runtime.getDeployPath() + name).path()).toPath(),
				new File(Mdx.files.external(runtime.getDeployPath() + name).path() + ".zip").toPath())
	}

	//https://www.quickprogrammingtips.com/java/how-to-zip-a-folder-in-java.html
	private void zipFolder(Path sourceFolderPath, Path zipPath) {
		try (ZipOutputStream zipOutStream = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
			Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
				@Override
				FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String path = sourceFolderPath.relativize(file).toString().replace('\\', '/')
					zipOutStream.putNextEntry(new ZipEntry(path))
					Files.copy(file, zipOutStream)
					zipOutStream.closeEntry()
					return FileVisitResult.CONTINUE
				}
			})
		} catch (Exception ex) {
			log.error(ex)
		}
	}

	void unzip(String zipName, String location) {
		File outputDir = new File(Mdx.files.external(location + "/" + zipName).path())

		if (!outputDir.exists()) {
			outputDir.mkdirs()
		}

		byte[] buffer = new byte[1024]
		int len
		File lkrPackage = new File(Mdx.files.external(runtime.getPackagePath() + zipName).path() + ".lkr")

		try (FileInputStream fileInStream = new FileInputStream(lkrPackage); ZipInputStream zipInStream = new ZipInputStream(fileInStream)) {
			ZipEntry zipEntry = zipInStream.getNextEntry()
			while (zipEntry != null) {
				String fileName = zipEntry.getName()

				File newFile = new File("$outputDir${File.separator}$fileName")

				new File(newFile.getParent()).mkdirs()

				try (FileOutputStream fileOutStream = new FileOutputStream(newFile)) {
					while ((len = zipInStream.read(buffer)) > 0) {
						fileOutStream.write(buffer, 0, len)
					}
				}
				zipInStream.closeEntry()
				zipEntry = zipInStream.getNextEntry()
			}
			zipInStream.closeEntry()
			fileInStream.close()

		} catch (IOException ex) {
			log.error(ex)
		}
	}

}
