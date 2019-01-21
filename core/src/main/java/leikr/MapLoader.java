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
package leikr;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledMap;

/**
 *
 * @author tor
 */
public class MapLoader {

    //TODO: Implement tile editing functions.
    TiledMap tiledMap;

    TiledMapTileLayer tiledMapLayer;

    MapLoader() {
    }

    void loadMap(String name) {
        tiledMap = new TiledMap(new FileHandle("./Games/" + MenuScreen.GAME_NAME + "/Map/" + name + ".tmx"));
    }

    TiledMap getMap() {
        return tiledMap;
    }

    void drawMap(Graphics g) {
        tiledMap.draw(g, 0, 0);
    }

    void drawMap(Graphics g, int x, int y) {
        tiledMap.draw(g, x, y);
    }

    // Gets the tileId of the cell located at x and y. 
    int getMapTileId(float x, float y) {
        try {
             return tiledMap.getTile((int)x, (int)y, 0).getTileId(1);
        } catch (Exception ex) {
            System.out.println(ex);
            return -1;
        }
    }

}
