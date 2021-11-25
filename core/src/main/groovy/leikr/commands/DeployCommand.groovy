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

import leikr.GameRuntime
import leikr.utilities.ExportTool
import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class DeployCommand implements Command {

	private final ExportTool exportTool
	private final GameRuntime runtime

	DeployCommand(GameRuntime runtime, ExportTool exportTool) {
		this.runtime = runtime
		this.exportTool = exportTool
	}

	@Override
	String execute(String[] args) {
		String programName = args[1]
		File outputDir = new File(Mdx.files.external(runtime.getDeployPath()).path())
		if (!outputDir.exists()) {
			outputDir.mkdirs()
		}
		try {
			Mdx.files.with {
				external(runtime.getDeployPath() + "$programName/Programs/").mkdirs()
				external(runtime.getProgramsPath() + programName).copyTo(external(runtime.getDeployPath() + "$programName/Programs/"))
				local("Data").copyTo(external(runtime.getDeployPath() + programName))
				local("Leikr").copyTo(external(runtime.getDeployPath() + programName))
				local("Leikr.bat").copyTo(external(runtime.getDeployPath() + programName))
				local("Leikr.jar").copyTo(external(runtime.getDeployPath() + programName))

				def setExec = new File(external(runtime.getDeployPath() + "$programName/Leikr").path()).setExecutable(true, false)
				def setBatExec = new File(external(runtime.getDeployPath() + "$programName/Leikr.bat").path()).setExecutable(true, false)
				println("[I] Set executable success: $setExec")
				println("[I] Set bat executable success: $setBatExec")
			}
		} catch (Exception ex) {
			Logger.getLogger(DeployCommand.class.getName()).log(Level.SEVERE, null, ex)
			return "[E] Failed to package and deploy project [$programName]"
		}
		Properties outProp = new Properties()
		outProp.with {
			setProperty("launch_title", programName)
			setProperty("dev_mode", "false")

			setProperty("btn_x", "3")
			setProperty("btn_a", "1")
			setProperty("btn_b", "0")
			setProperty("btn_y", "2")

			setProperty("btn_lbumper", "9")
			setProperty("btn_rbumper", "10")

			setProperty("btn_select", "4")
			setProperty("btn_start", "6")

			setProperty("btn_up", "-1")
			setProperty("btn_down", "1")
			setProperty("btn_left", "-1")
			setProperty("btn_right", "1")
			setProperty("axis_horizontal", "0")
			setProperty("axis_vertical", "1")
		}

		try (FileOutputStream stream = new FileOutputStream(new File(runtime.getDeployPath() + "$programName/Data/system.properties"))) {
			outProp.store(stream, "Packaged from Leikr.")
		} catch (Exception ex) {
			Logger.getLogger(DeployCommand.class.getName()).log(Level.SEVERE, null, ex)
			return "[E] Failed to package and deploy project [$programName]"
		}

		exportTool.deployPackage(programName)
		return "[I] Successfully packaged [$programName]. Check the Deploy directory."
	}

	@Override
	String help() {
		">deploy [project] \nPackages a project given the name and deploys it as a single launch project in the Deploy folder."
	}

	@Override
	String getName() {
		"deploy"
	}

}
