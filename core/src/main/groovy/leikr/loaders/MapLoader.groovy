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
package leikr.loaders

import java.util.logging.Level
import java.util.logging.Logger
import leikr.properties.SystemProperties
import leikr.exceptions.RenderException
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.tiled.TiledMap

/**
 *
 * @author tor
 */
class MapLoader {

    //TODO: Add handlers for object layers and collisions.  
    private String rootPath

    private TiledMap tiledMap

    private final SystemProperties systemProperties

    MapLoader(SystemProperties systemProperties) {
        this.systemProperties = systemProperties
    }

    void loadMap(String name) {
        if (null != tiledMap) {
            tiledMap.dispose()
        }
        tiledMap = new TiledMap(Mdx.files.external(rootPath + name + ".tmx"))
        tiledMap.loadTilesetTextures()
    }

    void resetMapLoader(String path) {
        disposeMap()
        rootPath = "$path/Maps/"
    }

    TiledMap getMap() {
        return tiledMap
    }

    private boolean checkMap() {
        if (tiledMap == null) {
            throw new RenderException("Error in program `render` method. `drawMap()` called with null map. Load a map with `loadMap(String name)`")
        }
        if (tiledMap.isTilesetTexturesLoaded(true)) {
            return true
        } else {
            throw new RenderException("Error in program `render` method. `drawMap()` called before tileset loaded.")
        }
    }

    void drawMap(Graphics g, int x = 0, int y = 0) {
        if (checkMap()) {
            tiledMap.draw(g, x, y)
        }
    }

    void drawMap(Graphics g, int x, int y, int layer) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, layer)
        }
    }

    void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, sx, sy, w, h)
        }
    }

    void drawMap(Graphics g, int x, int y, int sx, int sy, int w, int h, int layer) {
        if (checkMap()) {
            tiledMap.draw(g, x, y, sx, sy, w, h, layer)
        }
    }

    /**
     * Gets the tileId of the cell located at x and y on the default layer 0.
     *
     * @param x
     * @param y
     * @return the tileId of the tile at x,y
     */
    int getMapTile(int x, int y, int layer = 0) {
        try {
            return tiledMap.getTile(x, y, layer).getTileId(1)
        } catch (Exception ex) {
            if (systemProperties.isDebug()) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex)
            }
            return -1
        }
    }

    void setMapTile(int id, int x, int y) {
        setMapTile(id, x, y, 0)
    }

    void setMapTile(int id, int x, int y, int layer) {
        try {
            tiledMap.getTileLayer(layer).setTileId(x, y, id)
        } catch (Exception ex) {
            if (systemProperties.isDebug()) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex)
            }
        }
    }

    void removeMapTile(int x, int y, int layer = 0) {
        try {
            tiledMap.getTileLayer(layer).setTileId(x, y, -1)
        } catch (Exception ex) {
            if (systemProperties.isDebug()) {
                Logger.getLogger(MapLoader.class.getName()).log(Level.INFO, null, ex)
            }
        }
    }

    void disposeMap() {
        if (tiledMap) {
            tiledMap.dispose()
        }
    }

}
