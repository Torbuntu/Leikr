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
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import leikr.GameRuntime;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;

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

    SpriteSheet pixelSheet;
    Graphics g;

    /**
     * Properties set by Custom Properties
     *
     * These can be overwritten for a more custom experience.
     */
    private final int MAX_SPRITES;
    private int USED_SPRITES;

    /**
     * LeikrScreenManager constructor
     *
     * @param mSprites
     */
    public LeikrScreenManager(int mSprites) {
        MAX_SPRITES = mSprites;
        spriteLoader = new SpriteLoader();
        imageLoader = new ImageLoader();
        mapLoader = new MapLoader();
        constructPixelSheet();
    }

    /**
     * constructPixelSheet
     *
     * Constructs the sprite sheet of colors available for pixel drawing
     */
    private void constructPixelSheet() {
        Pixmap colorMap = new Pixmap(34, 1, Format.RGBA8888);
        for (int i = 0; i < 34; i++) {
            colorMap.setColor(getDrawColor(i));
            colorMap.drawPixel(i, 0);
        }
        pixelSheet = new SpriteSheet(new Texture(colorMap), 1, 1);
        colorMap.dispose();
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

    public void preUpdate(float delta) {
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
    public final Color getDrawColor(int color) {
        switch (color) {
            case 0:
                return (Color.BLACK);
            case 1:
                return (Color.BLUE);
            case 2:
                return (Color.BROWN);
            case 3:
                return (Color.CHARTREUSE);
            case 4:
                return (Color.CLEAR);
            case 5:
                return (Color.CORAL);
            case 6:
                return (Color.CYAN);
            case 7:
                return (Color.DARK_GRAY);
            case 8:
                return (Color.FIREBRICK);
            case 9:
                return (Color.FOREST);
            case 10:
                return (Color.GOLD);
            case 11:
                return (Color.GOLDENROD);
            case 12:
                return (Color.GRAY);
            case 13:
                return (Color.GREEN);
            case 14:
                return (Color.LIGHT_GRAY);
            case 15:
                return (Color.LIME);
            case 16:
                return (Color.MAGENTA);
            case 17:
                return (Color.MAROON);
            case 18:
                return (Color.NAVY);
            case 19:
                return (Color.OLIVE);
            case 20:
                return (Color.ORANGE);
            case 21:
                return (Color.PINK);
            case 22:
                return (Color.PURPLE);
            case 23:
                return (Color.RED);
            case 24:
                return (Color.ROYAL);
            case 25:
                return (Color.SALMON);
            case 26:
                return (Color.SCARLET);
            case 27:
                return (Color.SKY);
            case 28:
                return (Color.SLATE);
            case 29:
                return (Color.TAN);
            case 30:
                return (Color.TEAL);
            case 31:
                return (Color.VIOLET);
            case 32:
                return (Color.WHITE);
            case 33:
                return (Color.YELLOW);
            default:
                return Color.BLACK;
        }
    }

    public final void drawColor(int color) {
        g.setColor(getDrawColor(color));
    }

    public final void drawColor(float r, float gr, float b) {
        g.setColor(new Color(r, gr, b, 1f));
    }

    public final void drawColor(float[] c) {
        g.setColor(new Color(c[0], c[1], c[2], 1f));
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

    public final void text(String text, float x, float y, float width, int align, int color) {
        drawColor(color);
        g.drawString(text, x, y, width, align);
    }

    public final void text(String text, float x, float y, float width, int align, float[] color) {
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

    //START special sprite mode
    public final void spriteSc(int id, float x, float y, float scale) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.scale(scale);
        g.drawSprite(t, x, y);
        t.scale(-scale);
        USED_SPRITES++;
    }

    public final void spriteSc(int id, float x, float y, float scaleX, float scaleY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX, scaleY);
        g.drawSprite(t, x, y);
        t.setScale(-scaleX, -scaleY);
        USED_SPRITES++;
    }

    public final void spriteSc(int id, float x, float y, float scaleX, float scaleY, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX, scaleY);
        t.rotate(degr);
        g.drawSprite(t, x, y);
        t.rotate(-degr);
        t.setScale(-scaleX, -scaleY);
        USED_SPRITES++;
    }

    public final void spriteSc(int id, float x, float y, float scaleX, float scaleY, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX, scaleY);
        t.flip(flipX, flipY);
        g.drawSprite(t, x, y);
        t.flip(!flipX, !flipY);
        t.setScale(-scaleX, -scaleY);
        USED_SPRITES++;
    }
    //END special sprite mode

    //START animated sprites
    public Animation makeAnimSprite(int[] ids, float[] length) {
        Animation tmp = new Animation();
        for (int i = 0; i < ids.length; i++) {
            tmp.addFrame((Sprite) spriteLoader.getSprite(ids[i], 0), length[i]);
        }
        return tmp;
    }

    public Animation makeAnimSprite(int[] ids, float[] length, boolean loop) {
        Animation tmp = new Animation();
        for (int i = 0; i < ids.length; i++) {
            tmp.addFrame((Sprite) spriteLoader.getSprite(ids[i], 0), length[i]);
        }
        tmp.setLooping(true);
        return tmp;
    }

    public Animation makeAnimSprite(int[] ids, float[] length, int size) {
        Animation tmp = new Animation();
        for (int i = 0; i < ids.length; i++) {
            tmp.addFrame((Sprite) spriteLoader.getSprite(ids[i], size), length[i]);
        }
        return tmp;
    }

    public Animation makeAnimSprite(int[] ids, float[] length, int size, boolean loop) {
        Animation tmp = new Animation();
        for (int i = 0; i < ids.length; i++) {
            tmp.addFrame((Sprite) spriteLoader.getSprite(ids[i], size), length[i]);
        }
        tmp.setLooping(loop);
        return tmp;
    }

    public void spriteAnim(Animation sprite, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        sprite.draw(g, x, y);
    }
    //END animated sprites

    //start shape drawing methods
    public void pixel(int color, int x, int y) {
        g.drawSprite(pixelSheet.getSprite(color), x, y);
    }

    public final void rect(int x, int y, int w, int h) {
        g.drawRect(x, y, w, h);
    }

    public final void rect(int x, int y, int w, int h, boolean fill) {
        if (fill) {
            g.fillRect(x, y, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
    }

    public final void circle(int x, int y, int r) {
        g.drawCircle(x, y, r);
    }

    public final void circle(int x, int y, int r, boolean fill) {
        if (fill) {
            g.fillCircle(x, y, r);
        } else {
            g.drawCircle(x, y, r);
        }
    }

    public final void triangle(int x, int y, int x2, int y2, int x3, int y3) {
        g.drawTriangle(x, y, x2, y2, x3, y3);
    }

    public final void triangle(int x, int y, int x2, int y2, int x3, int y3, boolean fill) {
        if (fill) {
            g.fillTriangle(x, y, x2, y2, x3, y3);
        } else {
            g.drawTriangle(x, y, x2, y2, x3, y3);
        }
    }

    public final void line(int x, int y, int x2, int y2) {
        g.drawLineSegment(x, y, x2, y2);
    }
    //end shape drawing methods

    //EXPERIMENTAL METHODS
    public final void clip(float x, float y, float w, float h) {
        g.setClip(x, y, w, h);
    }

    public final void clip() {
        g.removeClip();
    }

    //END EXPERIMENTAL
}
