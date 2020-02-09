/*
 * Copyright 2020 See AUTHORS.
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
package leikr.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.serialization.annotation.Field;

/**
 *
 * @author tor
 */
public class LeikrDataManager {

    @Field
    public Map<String, Object> data;

    public LeikrDataManager() {
        data = new HashMap<>();
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public void deleteData(String key) {
        data.remove(key);
    }

    public void clearData() {
        data.clear();
    }

    public void saveData(String path) {
        try {
            Mdx.playerData.writeJson(this, GameRuntime.getProgramPath() + path);
        } catch (Exception ex) {
            Logger.getLogger(LeikrDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
