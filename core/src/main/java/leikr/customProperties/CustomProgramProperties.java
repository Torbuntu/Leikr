/*
 * Copyright 2019 torbuntu.
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

        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
