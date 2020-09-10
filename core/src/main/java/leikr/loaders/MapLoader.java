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
package leikr.loaders;

import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.customProperties.CustomSystemProperties;
import leikr.exceptions.RenderException;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.tiled.TiledMap;

/**
 *
 * @author tor
 */
public class MapLoader {
    //TODO: Add handlers for object layers and collisions.  

    TiledMap tiledMap;

    String rootPath;

    public MapLoader() {
    }

    public void loadMap(String name) {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
        tiledMap = new TiledMap(Mdx.files.local(rootPath + name + ".tmx"));
        tiledMap.loadTilesetTextures();
    }

    public void resetMapLoader(String path) {
        disposeMap();
        rootPath = path + "/Maps/";
    }

    public TiledMap getMap() {
        return tiledMap;
    }

    private boolean checkMap() {
        if (tiledMap == null) {
            throw new RenderException("Error in program `render` method. `drawMap()` called with null map. Load a map with `loadMap(String name)`");
        }
        if (tiledMap.isTilesetTexturesLoaded(true)) {
            return true;
        } else {
            throw new RenderException("Error in program `render` method. `drawMap()` called before tileset loaded.");
        }
    }

    public void drawMap(Graphics g) {
        drawMap(g, 0, 0);
    }

    public void drawMap(Graphics g, int x, int y) {
        if (checkMap()) {
            tiledMap.draw(g, x, y);
        }
    }

    public void drawMap(Graphics g, int x, int y, int layer) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, layer);
        }
    }

    public void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, sx, sy, w, h);
        }
    }

    public void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h, int layer) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, sx, sy, w, h, layer);
        }
    }

    // Gets the tileId of the cell located at x and y. 
    public int getMapTile(int x, int y) {
        return getMapTile(x, y, 0);
    }

    public int getMapTile(int x, int y, int layer) {
        try {
            return tiledMap.getTile(x, y, layer).getTileId(1);
        } catch (Exception ex) {
            if (CustomSystemProperties.DEBUG) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex);
            }
            return -1;
        }
    }

    public void setMapTile(int id, int x, int y) {
        setMapTile(id, x, y, 0);
    }

    public void setMapTile(int id, int x, int y, int layer) {
        try {
            tiledMap.getTileLayer(layer).setTileId(x, y, id);
        } catch (Exception ex) {
            if (CustomSystemProperties.DEBUG) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex);
            }
        }
    }

    public void removeMapTile(int x, int y) {
        removeMapTile(x, y, 0);
    }

    public void removeMapTile(int x, int y, int layer) {
        try {
            tiledMap.getTileLayer(layer).setTileId(x, y, -1);
        } catch (Exception ex) {
            if (CustomSystemProperties.DEBUG) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex);
            }
        }
    }

    public void disposeMap() {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
    }

}
