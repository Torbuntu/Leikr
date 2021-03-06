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
package leikr.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.utilities.ExportTool;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class DeployCommand implements Command {

    private final ExportTool exportTool;
    private final GameRuntime runtime;

    public DeployCommand(GameRuntime runtime, ExportTool exportTool) {
        this.runtime = runtime;
        this.exportTool = exportTool;
    }

    @Override
    public String execute(String[] args) {
        String programName = args[1];
        File outputDir = new File(Mdx.files.external(runtime.getDeployPath()).path());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try {
            Mdx.files.external(runtime.getDeployPath() + programName + "/Programs/").mkdirs();
            Mdx.files.external(runtime.getProgramsPath() + programName).copyTo(Mdx.files.external(runtime.getDeployPath() + programName + "/Programs/"));
            Mdx.files.local("Data").copyTo(Mdx.files.external(runtime.getDeployPath() + programName));
            Mdx.files.local("Sys").copyTo(Mdx.files.external(runtime.getDeployPath() + programName));
            Mdx.files.local("Leikr").copyTo(Mdx.files.external(runtime.getDeployPath()+ programName));
            Mdx.files.local("Leikr.bat").copyTo(Mdx.files.external(runtime.getDeployPath() + programName));
            Mdx.files.local("Leikr.jar").copyTo(Mdx.files.external(runtime.getDeployPath()+ programName));
        } catch (Exception ex) {
            Logger.getLogger(DeployCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "[E] Failed to package and deploy project [" + programName + "]";
        }
        Properties outProp = new Properties();
        outProp.setProperty("launch_title", programName);
        outProp.setProperty("dev_mode", "false");

        outProp.setProperty("btn_x", "3");
        outProp.setProperty("btn_a", "1");
        outProp.setProperty("btn_b", "0");
        outProp.getProperty("btn_y", "2");

        outProp.setProperty("btn_lbumper", "9");
        outProp.setProperty("btn_rbumper", "10");

        outProp.setProperty("btn_select", "4");
        outProp.setProperty("btn_start", "6");

        outProp.setProperty("btn_up", "-1");
        outProp.setProperty("btn_down", "1");
        outProp.setProperty("btn_left", "-1");
        outProp.setProperty("btn_right", "1");
        outProp.setProperty("axis_horizontal", "0");
        outProp.setProperty("axis_vertical", "1");
        try ( FileOutputStream stream = new FileOutputStream(new File(runtime.getDeployPath() + programName + "/Data/system.properties"))) {
            outProp.store(stream, "Packaged from Leikr.");
        } catch (Exception ex) {
            Logger.getLogger(DeployCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "[E] Failed to package and deploy project [" + programName + "]";
        }

        exportTool.deployPackage(programName);
        return "[I] Successfully packaged [" + programName + "]. Check the Deploy directory.";
    }

    @Override
    public String help() {
        return ">deploy [project] \nPackages a project given the name and deploys it as a single launch project in the Deploy folder.";
    }

    @Override
    public String getName() {
        return "deploy";
    }

}
