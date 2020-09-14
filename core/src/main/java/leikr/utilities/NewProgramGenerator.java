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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author Torbuntu
 */
public class NewProgramGenerator {

    private final String NEW_LOCATION = "New Program template generated at: /Programs/";

    private String maxSprites = "2048";
    private String useCompiled = "false";
    private String compileSource = "false";
    private String title = "unknown";
    private String type = "Program";
    private String author = "unknown";
    private String version = "0.0.0";
    private String players = "1";
    private String about = "A Leikr Program.";

    public String setNewProgramFileName(String newName, String template) throws IOException {
        String newProject = newName.length() > 0 ? newName : "NewProgram";

        for (FileHandle name : Mdx.files.local("Programs").list()) {
            if (name.name().equalsIgnoreCase(newProject)) {
                return "A program with name [" + newProject + "] already exists.";
            }
        }

        String message = copyTemplate(newProject, template);
        setNewProgramClassName(newProject);
        return message;
    }

    private String copyTemplate(String newProject, String template) throws IOException {
        if (!Mdx.files.local("Data/Templates/" + template).exists()) {
            throw new IOException("Templates: [" + template + "] does not exist");
        }
        Mdx.files.local("Programs/" + newProject).mkdirs();
        for (FileHandle file : Mdx.files.local("Data/Templates/" + template).list()) {
            Mdx.files.local("Data/Templates/" + template + "/" + file.name()).copyTo(Mdx.files.local("Programs/" + newProject));
        }
        Mdx.files.local("Programs/" + newProject + "/Code/main.groovy").moveTo(Mdx.files.local("Programs/" + newProject + "/Code/" + newProject + ".groovy"));
        return NEW_LOCATION + newProject + "/";
    }

    private void setNewProgramClassName(String newProject) throws IOException {
        Path nfPath = new File(Mdx.files.local("Programs/" + newProject + "/Code/" + newProject + ".groovy").path()).toPath();
        String newFile = Files.readString(nfPath);
        String replace = newFile.replace("NewProgram", newProject);
        Files.writeString(nfPath, replace);
    }

    public void writePropertyName(String name) {
        String propPath = Mdx.files.local("Programs/" + name + "/program.properties").path();
        try ( FileOutputStream fos = new FileOutputStream(propPath)) {
            Properties props = new Properties();
            props.setProperty("title", name);
            props.setProperty("type", "Program");
            props.setProperty("author", "Unknown");
            props.setProperty("version", "0.0.0");
            props.setProperty("players", "1");
            props.setProperty("about", "Leikr project");
            props.setProperty("max_sprites", "128");
            props.setProperty("compile_source", "false");
            props.setProperty("use_compiled", "false");

            props.store(fos, "Program generated with Leikr Program Generator");

        } catch (Exception ex) {
            Logger.getLogger(NewProgramGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeProperties(String name) {
        String propPath = Mdx.files.local("Programs/" + name + "/program.properties").path();
        try ( FileOutputStream fos = new FileOutputStream(propPath)) {
            Properties props = new Properties();
            props.setProperty("title", title);
            props.setProperty("type", type);
            props.setProperty("author", author);
            props.setProperty("version", version);
            props.setProperty("players", players);
            props.setProperty("about", about);
            props.setProperty("max_sprites", maxSprites);
            props.setProperty("compile_source", compileSource);
            props.setProperty("use_compiled", useCompiled);

            props.store(fos, "Program generated with Leikr Program Generator");

        } catch (Exception ex) {
            Logger.getLogger(NewProgramGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMaxSprites() {
        return maxSprites;
    }

    public void setMaxSprites(String maxSprites) {
        this.maxSprites = maxSprites;
    }

    public String getUseCompiled() {
        return useCompiled;
    }

    public void setUseCompiled(String useCompiled) {
        this.useCompiled = useCompiled;
    }

    public String getCompileSource() {
        return compileSource;
    }

    public void setCompileSource(String compileSource) {
        this.compileSource = compileSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
