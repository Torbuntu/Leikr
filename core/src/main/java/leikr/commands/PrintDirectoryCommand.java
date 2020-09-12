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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class PrintDirectoryCommand extends Command {

    private String out;

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
        return ">ls [Directory] \nDisplays the contents of a given directory or the default directory Programs.";
    }

    private String runLsPrograms() {
        try {
            out = "";
            List<String> titles = new ArrayList<>();

            Arrays.asList(Mdx.files.local("Programs").list()).forEach(e -> titles.add(e.nameWithoutExtension()));

            titles.stream().sorted().forEach(e -> {
                if (Mdx.files.local("Programs/" + e + "/Code/Compiled").exists()) {
                    out += e + " *\n";
                } else {
                    out += e + "\n";
                }
            });
            return out;
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex);
            return "[E] Failed to execute command [ ls ]";
        }
    }

    private String runLs(String dir) {
        try {
            out = "";
            List<String> titles = new ArrayList<>();
            Arrays.asList(Mdx.files.local(dir).list()).forEach(e -> titles.add(e.nameWithoutExtension()));

            titles.stream().sorted().forEach(e -> out += e + "\n");
            return out;
        } catch (IOException ex) {
            Logger.getLogger(PrintDirectoryCommand.class.getName()).log(Level.WARNING, null, ex);
            return "[E] Failed to execute command [ ls ]";
        }
    }

}
