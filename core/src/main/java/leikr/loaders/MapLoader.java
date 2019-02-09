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
package leikr.loaders;

import com.badlogic.gdx.files.FileHandle;
import leikr.GameRuntime;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledMap;

/**
 *
 * @author tor
 */
public class MapLoader {
    //TODO: Add handlers for animation tiles and object layers for collisions.  

    TiledMap tiledMap;

    String rootPath = GameRuntime.getGamePath() + "/Maps/";

    public void loadMap(String name) {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
        tiledMap = new TiledMap(new FileHandle(rootPath + name + ".tmx"));
    }

    public TiledMap getMap() {
        return tiledMap;
    }

    public void drawMap(Graphics g) {
        tiledMap.draw(g, 0, 0);
    }

    public void drawMap(Graphics g, int x, int y) {
        tiledMap.draw(g, x, y);
    }

    public void drawMap(Graphics g, int x, int y, int layer) {
        tiledMap.draw(g, x, y, layer);
    }

    public void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h) {
        tiledMap.draw(g, x, y, sx, sy, w, h);
    }

    public void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h, int layer) {
        tiledMap.draw(g, x, y, sx, sy, w, h, layer);
    }

    // Gets the tileId of the cell located at x and y. 
    public int getMapTileId(double x, double y) {
        try {
            return tiledMap.getTile((int) x, (int) y, 0).getTileId(1);
        } catch (Exception ex) {
            //System.out.println(ex);
            return -1;
        }
    }

    public void setMapTile(double x, double y, int id) {
        tiledMap.getTileLayer(0).setTileId((int) x, (int) y, id);
    }

    public void disposeMap() {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
    }

}
