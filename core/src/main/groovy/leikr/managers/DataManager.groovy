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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.serialization.annotation.Field;

/**
 *
 * @author tor
 */
public class DataManager {

    private String gamePath;

    @Field
    public HashMap<String, Object> data;

    public DataManager() {
        data = new HashMap<>();
    }

    public void resetData(String path) {
        setGamePath(path);
    }

    private void setGamePath(String path) {
        gamePath = path;
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
			Mdx.files.with{
				String dir = "${gamePath}/${path}";
				if (!external(dir).exists()) {
					external(dir).delete();
				}
				external(dir).writeString(Mdx.json.toJson(data), false);
			}
        } catch (IOException | SerializationException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, String.format("Failed saving data to %s", gamePath + "/" + path), ex);
        }
    }

    public void saveData(String path, HashMap<String, Object> data) {
        this.data = data;
        saveData(path);
    }

    public HashMap<String, Object> readData(String path) {
        try {
            String json = Mdx.files.external(gamePath + "/" + path).readString();
            json = json.replaceAll("[{\"}]", "");
            data = (HashMap<String, Object>) Arrays.asList(json.split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> (Object) e[1]));
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
