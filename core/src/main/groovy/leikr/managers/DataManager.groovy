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

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.mini2Dx.core.Mdx

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author tor
 */
class DataManager {

    private String gamePath
    private final JsonSlurper jsonSlurper

    public Map data

    DataManager() {
        data = [:]
        jsonSlurper = new JsonSlurper()
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

    void writeData(String path){
        try {
            Mdx.files.with{
                String dir = "${gamePath}/${path}"
                if (external(dir).exists()) {
                    external(dir).delete()
                }
                external(dir).writeString(JsonOutput.toJson(data), false)
            }
        } catch (Exception ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.WARNING, "Failed saving data to: $gamePath/$path", ex)
        }
    }

    void writeData(String path, Map data){
        this.data = data
        writeData(path)
    }

    Map readData(String path) {
        try {
            String json = Mdx.files.external( "$gamePath/$path").readString()
            data = jsonSlurper.parseText(json) as Map
        } catch (Exception ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.WARNING, "Problem trying to read data from: $path", ex)
        }
        return data
    }

}
