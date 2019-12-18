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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.customProperties.CustomProgramProperties;
import leikr.managers.TerminalManager;
import leikr.screens.TerminalScreen;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author tor
 */
public class AboutCommand extends Command {

    public AboutCommand(){
        super.name = "about";
    }
    
    @Override
    public String execute(String[] command) {
        if (command.length <= 1) {
            return "Pass a program name to get the program's about info.";
        }
        try {
            ArrayList<String> names = new ArrayList<>();
            Arrays.asList(Mdx.files.local("Programs").list()).stream().forEach(e -> names.add(e.nameWithoutExtension()));
            if (!names.contains(command[1])) {
                return "Program [" + command[1] + "] does not exist in Programs directory.";
            }
        } catch (IOException ex) {
            Logger.getLogger(TerminalManager.class.getName()).log(Level.WARNING, null, ex);
        }

        try {
            CustomProgramProperties cpp = new CustomProgramProperties("Programs/" + command[1]);

            return "Title: " + cpp.TITLE + "\nType: " + cpp.TYPE + "\nPlayers: " + cpp.PLAYERS + "\nAuthor: " + cpp.AUTHOR + "\nAbout: " + cpp.ABOUT;
        } catch (Exception ex) {
            Logger.getLogger(TerminalScreen.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed to clean package directory. Please check logs.";
        }
    }

    @Override
    public String help() {
        return ">about [option]\nReads the about property of the given program name.";
    }

}
