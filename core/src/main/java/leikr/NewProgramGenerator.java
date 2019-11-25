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
package leikr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author Torbuntu
 */
public class NewProgramGenerator {

    private static final String newLocation = "New Program template generated at: /Programs/";

    public static String setNewProgramFileName(String newName) throws IOException {
        int index = 0;
        String NP = newName.length() > 0 ? newName : "NewProgram";
        for (FileHandle name : Mdx.files.local("Programs").list()) {
            if (name.name().contains(NP)) {
                index++;
            }
        }
        if (index > 0) {
            NP = NP + index;
        }
        String message = copyTemplate(NP);
        setNewProgramClassName(NP);
        return message;
    }

    private static String copyTemplate(String NP) throws IOException {
        Mdx.files.local("Programs/" + NP).mkdirs();
        for (FileHandle file : Mdx.files.local("Data/Templates/NewProgram").list()) {
            Mdx.files.local("Data/Templates/NewProgram/" + file.name()).copyTo(Mdx.files.local("Programs/" + NP));
        }
        Mdx.files.local("Programs/" + NP + "/Code/main.groovy").moveTo(Mdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy"));
        return newLocation + NP + "/";
    }

    private static void setNewProgramClassName(String NP) throws IOException {
        Path nfPath = new File(Mdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy").path()).toPath();
        String newFile = Files.readString(nfPath);
        String replace = newFile.replace("NewProgram", NP);
        Files.writeString(nfPath, replace);
    }
}
