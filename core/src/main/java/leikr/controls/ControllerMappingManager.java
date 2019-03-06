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
package leikr.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 *
 * @author tor
 */
public class ControllerMappingManager {

    public static final String LGPM = "lgpm"; //leikr gamepad mapping

    public static Preferences prefs = Gdx.app.getPreferences(LGPM);

    public static String loadControllerMappings() {
        return prefs.getString("controllerMappings", "");
    }

    public static void saveControllerMappings(String json) {
        prefs.putString("controllerMappings", json);
        prefs.flush();
    }
}
