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
package leikr.managers

import org.mini2Dx.core.Mdx
import org.mini2Dx.core.exception.SerializationException
import org.mini2Dx.core.serialization.annotation.Field

import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors
/**
 *
 * @author tor
 */
class DataManager {

    private String gamePath

    @Field
    public def data

    DataManager() {
        data = [:]
    }

    void resetData(String path) {
        setGamePath(path)
    }

    private void setGamePath(String path) {
        gamePath = path
    }

    void addData(String key, Object value) {
        data.put(key, value)
    }

    void deleteData(String key) {
        data.remove(key)
    }

    void clearData() {
        data.clear()
    }

    void saveData(String path) {
        try {
			Mdx.files.with{
				String dir = "${gamePath}/${path}"
				if (!external(dir).exists()) {
					external(dir).delete()
                }
				external(dir).writeString(Mdx.json.toJson(data), false)
            }
        } catch (IOException | SerializationException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, String.format("Failed saving data to %s", "$gamePath/$path"), ex)
        }
    }

    void saveData(String path, Map<String, Object> data) {
        this.data = data
        saveData(path)
    }

    Map<String, Object> readData(String path) {
        try {
            String json = Mdx.files.external( "$gamePath/$path").readString()
            json = json.replaceAll("[{\"}]", "")
            data = json.split(",")
                    .collect(s -> s.split(":"))
                    .collectEntries(e -> [e[0], (Object) e[1]])
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex)
        }
        return data
    }

}
