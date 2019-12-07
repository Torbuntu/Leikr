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
package leikr.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import java.math.BigDecimal;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;
import org.mini2Dx.gdx.math.MathUtils;

/**
 * This class is used to manage the drawing API for the Engine. It also provides
 * a screen instance which can be passed to non Engine extending classes in the
 * game code.
 *
 * @author tor
 */
public class LeikrScreenManager {

    /*
     Loaders
     
     The loaders are used to load the custom assets for a game at startup.
     */
    SpriteLoader spriteLoader;
    ImageLoader imageLoader;
    MapLoader mapLoader;

    /*
    Color objects.
    pixels are the indexed Color pixels which are used for the drawing functions.
    colorPalette is List of all the available colors to use.
    bgColor is what preRender uses to clear the backrgound.
    
     */
    Color bgColor;
    PixelManager pixelManager;

    private static LeikrScreenManager instance;

    /*
     * Properties set by Custom Properties
     *
     * These can be overwritten for a more custom experience.
     */
    private int MAX_SPRITES;
    private int USED_SPRITES;
    private Graphics g;
    private Viewport v;
    private FrameBuffer frameBuffer;

    /**
     * LeikrScreenManager constructor
     *
     * @param mSprites
     */
    private LeikrScreenManager() {
    }

    public static LeikrScreenManager getLeikrScreenManager(int mSprites) {
        if (instance == null) {
            instance = new LeikrScreenManager();
        }
        instance.resetLeikrScreenManager(mSprites);
        return instance;
    }

    private void resetLeikrScreenManager(int mSprites) {
        MAX_SPRITES = mSprites;
        spriteLoader = SpriteLoader.getSpriteLoader();
        imageLoader = ImageLoader.getImageLoader();
        mapLoader = MapLoader.getMapLoader();
        pixelManager = PixelManager.getPixelManager();
        bgColor = Colors.BLACK();
    }

    //Engine methods
    public void preCreate(FrameBuffer f, FitViewport v) {
        this.frameBuffer = f;
        this.v = v;
    }

    public void preRender(Graphics g) {
        //set to 0 before drawing anything
        USED_SPRITES = 0;
        this.g = g;
        this.g.clearContext(bgColor);
    }

    public void preUpdate(float delta) {
        if (null != mapLoader.getMap()) {
            mapLoader.getMap().update(delta);
        }
    }

    public void dispose() {
        spriteLoader.disposeSprites();
        imageLoader.disposeImages();
        mapLoader.disposeMap();
    }
    //End Engine methods

    //Helper methods
    public int getUsedSprites() {
        return USED_SPRITES;
    }
    //End helper methods

    //Image methods
    public final void loadImages() {
        imageLoader.load();
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue());
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue(), flipv);
    }
    //end Image methods

    //Map methods
    public final void loadMap(String map) {
        mapLoader.loadMap(map);
    }

    public final void drawMap() {
        mapLoader.drawMap(g);
    }

    public final void drawMap(BigDecimal x, BigDecimal y) {
        mapLoader.drawMap(g, x.intValue(), y.intValue());
    }

    public final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        mapLoader.drawMap(g, x.intValue(), y.intValue(), layer);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        mapLoader.drawMap(g, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue());
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        mapLoader.drawMap(g, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue(), layer);
    }

    public final int getMapTileId(BigDecimal x, BigDecimal y) {
        return mapLoader.getMapTileId(x.intValue(), y.intValue());
    }
    
    public final int getMapTileId(BigDecimal x, BigDecimal y, int layer) {
        return mapLoader.getMapTileId(x.intValue(), y.intValue(), layer);
    }

    public final void setMapTile(BigDecimal x, BigDecimal y, int id) {
        mapLoader.setMapTile(x.intValue(), y.intValue(), id);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y) {
        mapLoader.removeMapTile(x.intValue(), y.intValue());
    }

    public final int getMapHeight() {
        return mapLoader.getMap().getHeight();
    }

    public final int getMapWidth() {
        return mapLoader.getMap().getWidth();
    }
    //end Map methods

    //start color methods
    private void setDrawColor(int color) {
        g.setColor(pixelManager.getDrawColor(color));
    }

    public final void bgColor(int color) {
        bgColor = pixelManager.getDrawColor(color);
    }

    public final void bgColor(String c) {
        bgColor = Colors.rgbToColor(c);
    }

    //end color methods
    //text methods
    public final void drawString(int color, String text, BigDecimal x, BigDecimal y) {
        setDrawColor(color);
        g.drawString(text, x.floatValue(), y.floatValue());
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        setDrawColor(color);
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue());
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        setDrawColor(color);
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y) {
        g.setColor(Colors.rgbToColor(color));
        g.drawString(text, x.floatValue(), y.floatValue());
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        g.setColor(Colors.rgbToColor(color));
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue());
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        g.setColor(Colors.rgbToColor(color));
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align);
    }

    //end drawString methods
    //sprite helper methods.
    private void drawSpriteRotate(int id, int size, BigDecimal degr, BigDecimal x, BigDecimal y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate(degr.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.rotate(-degr.floatValue());
        USED_SPRITES++;
    }

    private void drawSpriteFlip(int id, BigDecimal x, BigDecimal y, int size, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        USED_SPRITES++;
    }

    //start 8x8 sprites. Default size 0 (8x8)
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 0), x.floatValue(), y.floatValue());
        USED_SPRITES++;
    }

    public final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y) {
        drawSpriteRotate(id, 0, degr, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        drawSpriteFlip(id, x, y, 0, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, size), x.floatValue(), y.floatValue());
        USED_SPRITES++;
    }

    public final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y, int size) {
        drawSpriteRotate(id, size, degr, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        drawSpriteFlip(id, x, y, size, flipX, flipY);
    }
    //end sizable sprites

    //START special sprite mode
    /**
     * Draws a sprite with given ID value at the given coordinates with a given
     * Scale.
     *
     * The 'sc' portion of the method name refers to 'scale'
     *
     * @param id
     * @param x
     * @param y
     * @param scale
     */
    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.scale(scale.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.scale(-scale.floatValue());
        USED_SPRITES++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setScale(-scaleX.floatValue(), -scaleY.floatValue());
        USED_SPRITES++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        t.rotate(degr.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.rotate(-degr.floatValue());
        t.setScale(-scaleX.floatValue(), -scaleY.floatValue());
        USED_SPRITES++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        t.flip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.flip(!flipX, !flipY);
        t.setScale(-scaleX.floatValue(), -scaleY.floatValue());
        USED_SPRITES++;
    }
    //END special sprite mode

    //start shape drawing methods
    /**
     * Basic drawPixel taking Integer colord ID and a BigDecimal x,y coordinate
     * value.
     *
     * @param color
     * @param x
     * @param y
     */
    public final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        g.drawSprite(pixelManager.getSprite(color), x.intValue(), y.intValue());
    }

    /**
     * Internal drawPixel with Integer color ID .
     *
     * @param color
     * @param x
     * @param y
     */
    private void drawPixel(int color, int x, int y) {
        g.drawSprite(pixelManager.getSprite(color), x, y);
    }

    /**
     * Internal drawPixel with String color .
     *
     * @param color
     * @param x
     * @param y
     */
    private void drawPixel(String color, int x, int y) {
        g.setTint(Colors.rgbToColor(color));
        g.drawSprite(pixelManager.getSprite(1), x, y);
        g.removeTint();
    }

    /**
     * Uses the white pixel with a colored tint to produce a pixel of any
     * desired RGB color String, ex: "255,255,255"
     *
     * @param color
     * @param x
     * @param y
     */
    public final void drawPixel(String color, BigDecimal x, BigDecimal y) {
        drawPixel(color, x.intValue(), y.intValue());
    }

    public final void drawRect(int c, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        for (int i = x.intValue(); i < x.intValue() + w.intValue(); i++) {
            drawPixel(c, i, y.intValue());
            drawPixel(c, i, y.intValue() + h.intValue() - 1);
        }
        for (int i = y.intValue(); i < y.intValue() + h.intValue(); i++) {
            drawPixel(c, x.intValue(), i);
            drawPixel(c, x.intValue() + w.intValue() - 1, i);
        }
    }

    public final void drawRect(String c, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        for (int i = x.intValue(); i < x.intValue() + w.intValue(); i++) {
            drawPixel(c, i, y.intValue());
            drawPixel(c, i, y.intValue() + h.intValue() - 1);
        }
        for (int i = y.intValue(); i < y.intValue() + h.intValue(); i++) {
            drawPixel(c, x.intValue(), i);
            drawPixel(c, x.intValue() + w.intValue() - 1, i);
        }
    }

    public final void fillRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        setDrawColor(color);
        g.fillRect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
    }

    public final void fillRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        g.setColor(Colors.rgbToColor(color));
        g.fillRect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
    }

    private void drawHLine(int c, int x0, int x1, int y) {
        for (int i = x0; i < x1; i++) {
            drawPixel(c, i, y);
        }
    }

    private void drawVLine(int c, int x, int y0, int y1) {
        for (int i = y0; i < y1; i++) {
            drawPixel(c, x, i);
        }
    }

    private void drawHLine(String c, int x0, int x1, int y) {
        for (int i = x0; i < x1; i++) {
            drawPixel(c, i, y);
        }
    }

    private void drawVLine(String c, int x, int y0, int y1) {
        for (int i = y0; i < y1; i++) {
            drawPixel(c, x, i);
        }
    }

    public final void drawLineSegment(int c, BigDecimal p0, BigDecimal v0, BigDecimal p1, BigDecimal v1) {
        int x0 = p0.intValue();
        int x1 = p1.intValue();
        int y0 = v0.intValue();
        int y1 = v1.intValue();

        if (y0 == y1) {
            if (x0 < x1) {
                drawHLine(c, x0, x1, y0);
            } else {
                drawHLine(c, x1, x0, y0);
            }
            return;
        }
        if (x0 == x1) {
            if (y0 < y1) {
                drawVLine(c, x0, y0, y1);
            } else {
                drawVLine(c, x0, y1, y0);
            }
            return;
        }

        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;

        int err = dx + dy;
        while (true) {
            drawPixel(c, x0, y0);
            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public final void drawLineSegment(String c, BigDecimal p0, BigDecimal v0, BigDecimal p1, BigDecimal v1) {
        int x0 = p0.intValue();
        int x1 = p1.intValue();
        int y0 = v0.intValue();
        int y1 = v1.intValue();

        if (y0 == y1) {
            if (x0 < x1) {
                drawHLine(c, x0, x1, y0);
            } else {
                drawHLine(c, x1, x0, y0);
            }
            return;
        }
        if (x0 == x1) {
            if (y0 < y1) {
                drawVLine(c, x0, y0, y1);
            } else {
                drawVLine(c, x0, y1, y0);
            }
            return;
        }

        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;

        int err = dx + dy;
        while (true) {
            drawPixel(c, x0, y0);
            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
    
    public final void drawCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r){
        g.setColor(pixelManager.getDrawColor(color));
        g.drawCircle(x.intValue(), y.intValue(), r.intValue());
    }
    
    public final void fillCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r){
        g.setColor(pixelManager.getDrawColor(color));
        g.fillCircle(x.intValue(), y.intValue(), r.intValue());
    }
    
    public final void drawCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r){
        g.setColor(Colors.rgbToColor(color));
        g.drawCircle(x.intValue(), y.intValue(), r.intValue());
    }
    
    public final void fillCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r){
        g.setColor(Colors.rgbToColor(color));
        g.fillCircle(x.intValue(), y.intValue(), r.intValue());
    }
//end shape drawing methods

    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        g.setClip(x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
    }

    public final void removeClip() {
        g.removeClip();
    }

    //EXPERIMENTAL METHODS
    public void tint(int color) {
        g.setTint(pixelManager.getDrawColor(color));
    }
    public void tint(String color) {
        g.setTint(Colors.rgbToColor(color));
    }

    public void tint() {
        g.removeTint();
    }

    public String getPixel(BigDecimal x, BigDecimal y) {
        int gx = MathUtils.ceil((v.getX() * v.getScaleX()) + (x.floatValue() * v.getScaleX()));
        int gy = MathUtils.ceil((v.getY() * v.getScaleY()) + (y.floatValue() * v.getScaleY()));
        gy = Gdx.graphics.getHeight() - gy;
        frameBuffer.end();

        byte[] b = ScreenUtils.getFrameBufferPixels(gx, gy, 1, 1, false);
        frameBuffer.begin();

        int re = 0xFF & b[0];
        int gr = 0xFF & b[1];
        int bl = 0xFF & b[2];
        return (re + "," + gr + "," + bl);
    }

//END EXPERIMENTAL
}
