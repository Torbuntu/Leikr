package leikr.loaders;

import com.badlogic.gdx.Gdx;
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

    String rootPath;

    private static MapLoader instance;

    private MapLoader() {
    }

    public void loadMap(String name) {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
        tiledMap = new TiledMap(Gdx.files.local(rootPath + name + ".tmx"));
    }

    public static MapLoader getMapLoader() {
        if (instance == null) {
            instance = new MapLoader();
        }
        instance.resetMapLoader();
        return instance;
    }

    private void resetMapLoader() {
        rootPath = GameRuntime.getProgramPath() + "/Maps/";
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
    public int getMapTileId(int x, int y) {
        try {
            return tiledMap.getTile(x, y, 0).getTileId(1);
        } catch (Exception ex) {
            //System.out.println(ex);
            return -1;
        }
    }

    public void setMapTile(int x, int y, int id) {
        tiledMap.getTileLayer(0).setTileId(x, y, id);
    }

    public void removeMapTile(int x, int y) {
        tiledMap.getTileLayer(0).setTileId(x, y, -1);
    }

    public void disposeMap() {
        if (null != tiledMap) {
            tiledMap.dispose();
        }
    }

}
