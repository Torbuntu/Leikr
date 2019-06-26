package leikr.customProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author tor
 */
public class CustomProgramProperties {

    public int MAX_SPRITES;
    public boolean USE_COMPILED;
    public boolean COMPILE_SOURCE;
    public String TITLE;
    public String TYPE;
    public String AUTHOR;
    public String VERSION;
    public String PLAYERS;
    public String ABOUT;
    public boolean JAVA_ENGINE;

    public CustomProgramProperties(String gamePath) {

        Properties prop = new Properties();
        try (InputStream stream = new FileInputStream(new File(gamePath + "/program.properties"))) {
            prop.load(stream);

            MAX_SPRITES = (prop.getProperty("max_sprites") != null) ? Integer.parseInt(prop.getProperty("max_sprites")) : 120;
            if (MAX_SPRITES > 2048) {
                MAX_SPRITES = 2048;
            }
            USE_COMPILED = (prop.getProperty("use_compiled") != null) ? Boolean.valueOf(prop.getProperty("use_compiled")) : false;
            COMPILE_SOURCE = (prop.getProperty("compile_source") != null) ? Boolean.valueOf(prop.getProperty("compile_source")) : false;
            JAVA_ENGINE = (prop.getProperty("java_engine") != null) ? Boolean.valueOf(prop.getProperty("java_engine")) : false;

            TITLE = "Title  : " + ((prop.getProperty("title") != null) ? prop.getProperty("title") : "unknown");
            TYPE = "Type   : " + ((prop.getProperty("type") != null) ? prop.getProperty("type") : "Program");
            AUTHOR = "Author : " + ((prop.getProperty("author") != null) ? prop.getProperty("author") : "unknown");
            VERSION = "Version: " + ((prop.getProperty("version") != null) ? prop.getProperty("version") : "0.1");
            PLAYERS = "Players: " + ((prop.getProperty("players") != null) ? Integer.parseInt(prop.getProperty("players")) : 1);
            ABOUT = "About: " + ((prop.getProperty("about") != null) ? prop.getProperty("about") : "A Leikr Program.");
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public CustomProgramProperties(String title, String type, String author, String version, int players, String about) {
        TITLE = "Title  : " + title;
        TYPE = "Type   : " + type;
        AUTHOR = "Author : " + author;
        VERSION = "Version: " + version;
        PLAYERS = "Players: " + players;
        ABOUT = "About: " + about;
    }
}
