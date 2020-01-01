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
import leikr.ExportTool;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class PackageCommand extends Command {

    @Override
    public String execute(String[] args) {
        String name = args[1];
        File outputDir = new File(Mdx.files.local("Deploy/").path());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try {
            Mdx.files.local("Deploy/" + name + "/Programs/").mkdirs();
            Mdx.files.local("Programs/" + name).copyTo(Mdx.files.local("Deploy/" + name + "/Programs/"));
            Mdx.files.local("Data").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("Sys").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("Internal").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("Leikr").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("Leikr.bat").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("gamecontrollerdb.txt").copyTo(Mdx.files.local("Deploy/" + name));
            Mdx.files.local("Leikr.jar").copyTo(Mdx.files.local("Deploy/" + name));
        } catch (Exception ex) {
            Logger.getLogger(PackageCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed to package and deploy project [" + name + "]";
        }
        Properties outProp = new Properties();
        outProp.setProperty("launch_title", name);

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
        try (FileOutputStream stream = new FileOutputStream(new File("Deploy/" + name + "/Data/system.properties"))) {
            outProp.store(stream, "Packaged from Leikr.");
        } catch (Exception ex) {
            Logger.getLogger(PackageCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed to package and deploy project [" + name + "]";
        }

        ExportTool.deployPackage(name);
        return "Successfully packaged [" + name + "]. Check the Deploy directory.";
    }

    @Override
    public String help() {
        return ">package [project] \nPackages a project given the name and deploys it as a single launch project in the Deploy folder.";
    }

}
