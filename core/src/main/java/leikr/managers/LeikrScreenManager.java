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
package leikr.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import leikr.GameRuntime;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

/**
 *
 * @author tor
 *
 * This class is used to manage the drawing API for the Engine. It also provides
 * a screen instance which can be passed to non Engine extending classes in the
 * game code.
 */
public class LeikrScreenManager {

    /**
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    SpriteLoader spriteLoader;
    ImageLoader imageLoader;
    MapLoader mapLoader;

    Graphics g;

    Pixmap pixMap;
    Texture pixTex;
    boolean usePix;

    /**
     * Properties set by Custom Properties
     *
     * These can be overwritten for a more custom experience.
     */
    private final int MAX_SPRITES;
    private int USED_SPRITES;

    public LeikrScreenManager(int mSprites) {
        MAX_SPRITES = mSprites;
        spriteLoader = new SpriteLoader();
        imageLoader = new ImageLoader();
        mapLoader = new MapLoader();
        pixMap = new Pixmap(GameRuntime.WIDTH, GameRuntime.HEIGHT, Pixmap.Format.RGBA8888);
        pixTex = new Texture(pixMap);
        usePix = false;

    }

    //Helper methods
    public int getUsedSprites() {
        return USED_SPRITES;
    }
    //End helper methods

    //Engine methods
    public void preRender(Graphics g) {
        this.g = g;
        //set to 0 before drawing anything
        USED_SPRITES = 0;
    }

    public void postRender() {
        if (usePix) {
            g.drawTexture(pixTex, 0, 0);
        }
    }

    public void preUpdate(float delta) {
        if (usePix) {
            pixTex.dispose();//Must dispose, else memory leak occurs.
            pixTex = new Texture(pixMap);
        }
        if (null == mapLoader.getMap()) {
            return;//don't update the map if it is null
        }
        mapLoader.getMap().update(delta);
    }

    public void dispose() {
        spriteLoader.disposeSprites();
        imageLoader.disposeImages();
        mapLoader.disposeMap();
    }

    //End Engine methods
    public void usePixels() {
        usePix = true;
    }

    //Image methods
    public final void loadImages() {
        imageLoader.load();
    }

    public final void image(String name, float x, float y) {
        g.drawTexture(imageLoader.getImage(name), x, y);
    }

    public final void image(String name, float x, float y, float w, float h) {
        g.drawTexture(imageLoader.getImage(name), x, y, w, h);
    }

    public final void image(String name, float x, float y, float w, float h, boolean flipv) {
        g.drawTexture(imageLoader.getImage(name), x, y, w, h, flipv);
    }
    //end Image methods

    //Map methods
    public final void loadMap(String map) {
        mapLoader.loadMap(map);
    }

    public final void map() {
        mapLoader.drawMap(g);
    }

    public final void map(int x, int y) {
        mapLoader.drawMap(g, x, y);
    }

    public final void map(int x, int y, int layer) {
        mapLoader.drawMap(g, x, y, layer);
    }

    public final void map(int x, int y, int sx, int sy, int w, int h) {
        mapLoader.drawMap(g, x, y, sx, sy, w, h);
    }

    public final void map(int x, int y, int sx, int sy, int w, int h, int layer) {
        mapLoader.drawMap(g, x, y, sx, sy, w, h, layer);
    }

    public final int mapGet(int x, int y) {
        return mapLoader.getMapTileId(x, y);
    }

    public final void mapSet(int x, int y, int id) {
        mapLoader.setMapTile(x, y, id);
    }

    public final void mapRemove(int x, int y) {
        mapLoader.removeMapTile(x, y);
    }

    public final int getMapHeight() {
        return mapLoader.getMap().getHeight();
    }

    public final int getMapWidth() {
        return mapLoader.getMap().getWidth();
    }
    //end Map methods

    //start color methods
    public final void drawColor(int color) {
        g.setColor(getDrawColor(color));
        pixMap.setColor(getDrawColor(color));
    }

    public final void drawColor(float r, float gr, float b) {
        Color tmp = new Color(r, gr, b, 1f);
        g.setColor(tmp);
        pixMap.setColor(tmp);
    }

    public final Color getDrawColor(int color) {
        switch (color) {
            case 0:
                return (Color.BLACK);
            case 1:
                return (Color.WHITE);
            case 2:
                return (Color.RED);
            case 3:
                return (Color.GREEN);
            case 4:
                return (Color.BLUE);
            case 5:
                return (Color.YELLOW);
            case 6:
                return (Color.BROWN);
            case 7:
                return (Color.MAGENTA);
            case 8:
                return (Color.CYAN);
            case 9:
                return (Color.TEAL);
            case 10:
                return (Color.TAN);
            case 11:
                return (Color.FOREST);
            case 12:
                return (Color.PINK);
            case 13:
                return (Color.ORANGE);
            case 14:
                return (Color.PURPLE);
            case 15:
                return (Color.CORAL);
            default:
                return Color.BLACK;
        }
    }

    public final void bgColor(int color) {
        drawColor(color);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    public final void bgColor(float r, float gr, float b) {
        drawColor(r, gr, b);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    public final void bgColor(float[] color) {
        drawColor(color[0], color[1], color[2]);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }
    //end color methods

    //text methods
    public final void text(String text, float x, float y, int color) {
        drawColor(color);
        g.drawString(text, x, y);
    }

    public final void text(String text, float x, float y, float[] color) {
        drawColor(color[0], color[1], color[2]);
        g.drawString(text, x, y);
    }

    public final void text(String text, float x, float y, float width, int color) {
        drawColor(color);
        g.drawString(text, x, y, width);
    }

    public final void text(String text, float x, float y, float width, float[] color) {
        drawColor(color[0], color[1], color[2]);
        g.drawString(text, x, y, width);
    }
    
    public final void text(String text, float x, float y, float width, int align, int color){
        drawColor(color);
        g.drawString(text, x, y, width, align);
    }
    
    public final void text(String text, float x, float y, float width, int align, float[] color){
        drawColor(color[0], color[1], color[2]);
        g.drawString(text, x, y, width, align);
    }
    //end text methods

    //sprite helper methods.
    private void drawSpriteRotate(int id, int size, float degr, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate(degr);
        g.drawSprite(t, x, y);
        t.rotate(-degr);
        USED_SPRITES++;
    }

    private void drawSprite90(int id, float x, float y, int size, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate90(clockwise);
        g.drawSprite(t, x, y);
        t.rotate90(!clockwise);
        USED_SPRITES++;
    }

    private void drawSpriteFlip(int id, float x, float y, int size, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x, y);
        t.setFlip(false, false);
        USED_SPRITES++;
    }

    //start 8x8 sprites. Default size 0 (8x8)
    public final void sprite(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        USED_SPRITES++;
    }

    public final void sprite(int id, float x, float y, float degr) {
        drawSpriteRotate(id, 0, degr, x, y);
    }

    public final void sprite(int id, float x, float y, boolean clockwise) {
        drawSprite90(id, x, y, 0, clockwise);
    }

    public final void sprite(int id, float x, float y, boolean flipX, boolean flipY) {
        drawSpriteFlip(id, x, y, 0, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, float x, float y, int size) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, size), x, y);
        USED_SPRITES++;
    }

    public final void sprite(int id, float x, float y, float degr, int size) {
        drawSpriteRotate(id, size, degr, x, y);
    }

    public final void sprite(int id, float x, float y, boolean clockwise, int size) {
        drawSprite90(id, x, y, size, clockwise);
    }

    public final void sprite(int id, float x, float y, boolean flipX, boolean flipY, int size) {
        drawSpriteFlip(id, x, y, size, flipX, flipY);
    }
    //end sizable sprites

    //start shape drawing methods
    public void clpx() {
        pixMap.setColor(Color.CLEAR);
        pixMap.fill();
    }

    public void pixel(int x, int y) {
        pixMap.drawPixel(x, y);
    }

    public void pixel(int x, int y, int color) {
        drawColor(color);
        pixMap.drawPixel(x, y);
    }

    public void pixel(int x, int y, float[] c) {
        drawColor(c[0], c[1], c[2]);
        pixMap.drawPixel(x, y);
    }

    public final void rect(int x, int y, int w, int h) {
        pixMap.drawRectangle(x, y, w, h);
    }

    public final void rect(int x, int y, int w, int h, boolean fill) {
        if (fill) {
            pixMap.fillRectangle(x, y, w, h);
        } else {
            pixMap.drawRectangle(x, y, w, h);
        }
    }

    public final void circle(int x, int y, int r) {
        pixMap.drawCircle(x, y, r);
    }

    public final void circle(int x, int y, int r, boolean fill) {
        if (fill) {
            pixMap.fillCircle(x, y, r);
        } else {
            pixMap.drawCircle(x, y, r);
        }
    }

    public final void line(int x1, int y1, int x2, int y2) {
        pixMap.drawLine(x1, y1, x2, y2);
    }
    //end shape drawing methods

}
