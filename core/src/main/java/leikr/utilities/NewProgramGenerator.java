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
package leikr.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author Torbuntu
 */
public class NewProgramGenerator {

    private static final String NEW_LOCATION = "New Program template generated at: /Programs/";
    
    public String MAX_SPRITES = "2048";
    public String USE_COMPILED = "false";
    public String COMPILE_SOURCE = "false";
    public String TITLE = "unknown";
    public String TYPE = "Program";
    public String AUTHOR = "unknown";
    public String VERSION = "0.1";
    public String PLAYERS = "1";
    public String ABOUT = "A Leikr Program.";

    public static String setNewProgramFileName(String newName, String template) throws IOException {
        String NP = newName.length() > 0 ? newName : "NewProgram";
        
        for (FileHandle name : Mdx.files.local("Programs").list()) {
            if (name.name().equalsIgnoreCase(NP)) {
                return "A program with name ["+NP+"] already exists.";
            }
        }
        
        String message = copyTemplate(NP, template);
        setNewProgramClassName(NP);
        return message;
    }

    private static String copyTemplate(String NP, String template) throws IOException {
        Mdx.files.local("Programs/" + NP).mkdirs();
        for (FileHandle file : Mdx.files.local("Data/Templates/"+template).list()) {
            Mdx.files.local("Data/Templates/"+template +"/" + file.name()).copyTo(Mdx.files.local("Programs/" + NP));
        }
        Mdx.files.local("Programs/" + NP + "/Code/main.groovy").moveTo(Mdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy"));
        return NEW_LOCATION + NP + "/";
    }

    private static void setNewProgramClassName(String NP) throws IOException {
        Path nfPath = new File(Mdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy").path()).toPath();
        String newFile = Files.readString(nfPath);
        String replace = newFile.replace("NewProgram", NP);
        Files.writeString(nfPath, replace);
    }
    
    public void writeProperties(String name){
        String propPath = Mdx.files.local("Programs/" + name +"/program.properties").path();
        try(FileOutputStream fos = new FileOutputStream(propPath)){
            Properties props = new Properties();
            props.setProperty("title", TITLE);
            props.setProperty("type", TYPE);
            props.setProperty("author", AUTHOR);
            props.setProperty("version", VERSION);
            props.setProperty("players", PLAYERS);
            props.setProperty("about", ABOUT);
            props.setProperty("max_sprites", MAX_SPRITES);
            props.setProperty("compile_source", COMPILE_SOURCE);
            props.setProperty("use_compiled", USE_COMPILED);
            
            props.store(fos, "Program generated with Leikr Program Generator");
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    
    
    void setMaxSprite(String max){
        MAX_SPRITES = max;
    }
    void setTitle(String title){
        TITLE = title;
    }
}
