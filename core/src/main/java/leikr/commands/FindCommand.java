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
package leikr.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author Torbuntu
 */
public class FindCommand implements Command {

    private final GameRuntime runtime;

    public FindCommand(GameRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public String execute(String[] command) {
        if (command.length <= 1) {
            return "[E] Missing - required program name.";
        }
        if (!containsName(command[1])) {
            return "[E] Program [" + command[1] + "] does not exist in Programs directory.";
        }
        try {
            File f = new File(runtime.getProgramsPath() + command[1]);
            Desktop.getDesktop().open(f);
            return f.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(FindCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "[E] Could not find program directory for [" + command[1] + "].";
        }
    }

    private boolean containsName(String name) {
        try {
            ArrayList<String> names = new ArrayList<>();
            Arrays.asList(Mdx.files.external(runtime.getProgramsPath()).list()).stream().forEach(e -> names.add(e.nameWithoutExtension()));
            return names.contains(name);
        } catch (IOException ex) {
            Logger.getLogger(FindCommand.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public String help() {
        return ">find [option] \nPrints the location of the given program name. Attempts to open the directory in the host file manager.";
    }

    @Override
    public String getName() {
        return "find";
    }

}
