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
    //TODO: Add handlers for animation tiles and object layers for collisions.  
    
    TiledMap tiledMap;

    TiledMapTileLayer tiledMapLayer;

    MapLoader() {
    }

    void loadMap(String name) {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
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

    void drawMap(Graphics g, int x, int y, int layer) {
        tiledMap.draw(g, x, y, layer);
    }

    void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h) {
        tiledMap.draw(g, x, y, sx, sy, w, h);
    }

    void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h, int layer) {
        tiledMap.draw(g, x, y, sx, sy, w, h, layer);
    }

    // Gets the tileId of the cell located at x and y. 
    int getMapTileId(float x, float y) {
        try {
            return tiledMap.getTile((int) x, (int) y, 0).getTileId(1);
        } catch (Exception ex) {
            System.out.println(ex);
            return -1;
        }
    }

    void setMapTile(float x, float y, int id) {
        tiledMap.getTileLayer(0).setTileId((int) x, (int) y, id);
    }

}
