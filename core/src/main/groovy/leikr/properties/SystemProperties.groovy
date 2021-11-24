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
package leikr.properties

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class SystemProperties {

    private static String launchTitle

    private boolean debug

    private boolean devMode

    SystemProperties() {
        Properties prop = new Properties()
        try (InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream)
            launchTitle = prop.getProperty("launch_title") ?: ""
            debug = Boolean.valueOf(prop.getProperty("debug_mode")) ?: false
            devMode = Boolean.valueOf(prop.getProperty("dev_mode")) ?: false
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(SystemProperties.class.getName()).log(Level.WARNING, null, ex)
        }
    }

    static String getLaunchTitle() {
        launchTitle
    }


    boolean isDebug() {
        debug
    }

    boolean isDevMode() {
        devMode
    }

}
