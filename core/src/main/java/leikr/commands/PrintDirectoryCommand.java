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

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class PrintDirectoryCommand extends Command {

    public String out;

    public PrintDirectoryCommand() {
        super.name = "ls";
    }

    @Override
    public String execute(String[] param) {
        if (param.length > 1) {
            return runLs(param[1]);
        } else {
            return runLsPrograms();
        }
    }

    @Override
    public String help() {
        return ">ls [option] \nDisplays the contents of a given directory or the default directory Programs.";
    }

    private String runLsPrograms() {
        try {
            out = "";
            Arrays.asList(Mdx.files.local("Programs").list()).stream()
                    .forEach(e -> {
                        System.out.println("TITLE: " + e.nameWithoutExtension());
                        if (Mdx.files.local("Programs/" + e.nameWithoutExtension() + "/Code/Compiled").exists()) {
                            out += e.nameWithoutExtension() + " *\n";
                        } else {
                            out += e.nameWithoutExtension() + "\n";
                        }
                    });
            return out;
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex);
            return "Failed to execute command [ ls ]";
        }
    }

    private String runLs(String dir) {
        try {
            out = "";
            Arrays.asList(Mdx.files.local(dir).list()).stream().forEach(e -> out += e.nameWithoutExtension() + "\n");
            return out;
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex);
            return "Failed to execute command [ ls ]";
        }
    }

}
