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
package leikr.Commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.managers.TerminalManager;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author tor
 */
public class PrintDirectory extends Command {
    public String out;
    public FileHandle[] programs;
    
    public PrintDirectory(){
        super.name = "ls";
    }

    @Override
    public String execute(String[] param) {
        if (param.length > 1) {
            return runLs(param[1]);
        } else {
            return runLs("Programs");
        }
    }

    @Override
    public String help() {
        return ">ls [option] \nDisplays the contents of a given directory or the default directory Programs.";
    }

    private void refreshProgramList(String dir) throws IOException {
        out = "";
        programs = Mdx.files.local(dir).list();
        Arrays.asList(programs).stream().forEach(e -> out += e.nameWithoutExtension() + "\n");
    }

    public String runLs(String dir) {
        try {
            refreshProgramList(dir);
            return out;
        } catch (IOException ex) {
            Logger.getLogger(TerminalManager.class.getName()).log(Level.WARNING, null, ex);
            return "Failed to execute command [ ls ]";
        }
    }

}
