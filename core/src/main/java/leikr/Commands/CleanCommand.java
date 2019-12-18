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
package leikr.Commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author Torbuntu
 */
public class CleanCommand extends Command {

    public CleanCommand() {
        super.name = "clean";
    }

    @Override
    public String execute(String[] args) {
        try {
            Mdx.files.local("Packages/").deleteDirectory();
            Mdx.files.local("Packages").mkdirs();
            return "Package directory cleaned.";
        } catch (IOException ex) {
            Logger.getLogger(CleanCommand.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed to clean package directory. Please check logs.";
        }
    }

    @Override
    public String help() {
        return ">clean \nRemoves all lkr packages from the Packages directory.";
    }

}
