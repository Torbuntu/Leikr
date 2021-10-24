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
package leikr.customProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
class CustomProgramProperties {

    int MAX_SPRITES = 128
    boolean USE_COMPILED = false
    boolean COMPILE_SOURCE = false
    String TITLE = "unknown"
    String TYPE = "Program"
    String AUTHOR = "unknown"
    String VERSION = "0"
    int PLAYERS = 1
    String ABOUT = "A Leikr Program."
    private String GAME_TITLE = ""

    CustomProgramProperties(String gamePath) {
        GAME_TITLE = gamePath.substring(gamePath.lastIndexOf("/") + 1);
        Properties prop = new Properties();
        try ( InputStream stream = new FileInputStream(new File(gamePath + "/program.properties"))) {
            prop.load(stream);

            MAX_SPRITES = Integer.parseInt(prop.getProperty("max_sprites")) ?: 128
            if (MAX_SPRITES > 128) {
                MAX_SPRITES = 128;
            }
            USE_COMPILED = Boolean.valueOf(prop.getProperty("use_compiled")) ?: false
            COMPILE_SOURCE = Boolean.valueOf(prop.getProperty("compile_source")) ?: false

            TITLE = prop.getProperty("title") ?: "unknown"
            TYPE = prop.getProperty("type") ?: "Program"
            AUTHOR = prop.getProperty("author") ?: "unknown"
            VERSION = prop.getProperty("version") ?: "0.1"
            PLAYERS = Integer.parseInt(prop.getProperty("players")) ?: 1
            ABOUT = prop.getProperty("about") ?: "A Leikr Program."
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(CustomProgramProperties.class.getName()).log(Level.SEVERE, ex.getMessage())
        }
    }

    void writeProperties(String gamePath) {
        try ( FileOutputStream stream = new FileOutputStream(new File(gamePath + "/program.properties"))) {
            Properties prop = new Properties();

            prop.setProperty("max_sprites", String.valueOf(MAX_SPRITES))
            prop.setProperty("use_compiled", String.valueOf(USE_COMPILED))
            prop.setProperty("compile_source", String.valueOf(COMPILE_SOURCE))
            prop.setProperty("title", TITLE)
            prop.setProperty("type", TYPE)
            prop.setProperty("author", AUTHOR)
            prop.setProperty("version", VERSION)
            prop.setProperty("players", String.valueOf(PLAYERS))
            prop.setProperty("about", ABOUT)

            prop.store(stream, null);

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(CustomProgramProperties.class.getName()).log(Level.SEVERE, ex.getMessage())
        }
    }

    boolean getUseCompiled() {
        return USE_COMPILED;
    }

    String getTitle() {
        return GAME_TITLE;
    }

}
