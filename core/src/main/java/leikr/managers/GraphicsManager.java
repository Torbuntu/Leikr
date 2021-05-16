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
import java.util.ArrayList;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.viewport.StretchViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;
import org.mini2Dx.gdx.math.MathUtils;

/**
 * Manages the drawing API for {@link leikr.Engine }. 
 *
 * @author tor
 */
public class GraphicsManager {

    /**
     * The path to the running programs local `Art` directory.
     *
     * @see #getPixel(java.lang.String, java.math.BigDecimal,
     * java.math.BigDecimal)
     * @see #getPixels(java.lang.String)
     */
    private String path;

    /**
     * Properties set by Custom Properties
     *
     * @see #maxSprites can be overwritten in
     * {@link leikr.customProperties.CustomProgramProperties} more custom
     * experience.
     */
    private int maxSprites;
    private int usedSprites;

    /**
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    private final SpriteLoader spriteLoader;
    private final ImageLoader imageLoader;
    private final MapLoader mapLoader;

    /**
     * Color objects. pixels are the indexed Color pixels which are used for the
     * drawing functions. colorPalette is a List of all the available colors to
     * use. bgColor is what {@link #preRender} uses to clear the background.
     *
     */
    private Color bgColor;
    private final PixelManager pixelManager;

    private Graphics g;
    private Viewport viewport;
    private FrameBuffer frameBuffer;

    /**
     *
     * @param spriteLoader
     * @param imageLoader
     * @param mapLoader
     * @param pixelManager
     */
    public GraphicsManager(SpriteLoader spriteLoader, ImageLoader imageLoader, MapLoader mapLoader, PixelManager pixelManager) {
        this.spriteLoader = spriteLoader;
        this.imageLoader = imageLoader;
        this.mapLoader = mapLoader;
        this.pixelManager = pixelManager;
    }

    /**
     * Resets and initializes assets for a new loaded game.
     *
     * @param path
     * @param maxSprites
     */
    public void resetScreenManager(String path, int maxSprites) {
        this.path = path;
        this.maxSprites = maxSprites;
        spriteLoader.resetSpriteLoader(path);
        imageLoader.reloadImageLoader(path);
        mapLoader.resetMapLoader(path);
        bgColor = Colors.BLACK();
    }

    // <editor-fold desc="Engine methods" defaultstate="collapsed">
    public void preCreate(FrameBuffer f, StretchViewport v) {
        this.frameBuffer = f;
        this.viewport = v;
    }

    public void preRender(Graphics g) {
        // Reset used count to 0 
        usedSprites = 0;
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
    // </editor-fold>

    //Helper methods
    public int getUsedSprites() {
        return usedSprites;
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

    // <editor-fold desc="Map methods" defaultstate="collapsed"> 
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

    public final int getMapTile(BigDecimal x, BigDecimal y) {
        return mapLoader.getMapTile(x.intValue(), y.intValue());
    }

    public final int getMapTile(BigDecimal x, BigDecimal y, int layer) {
        return mapLoader.getMapTile(x.intValue(), y.intValue(), layer);
    }

    public final void setMapTile(int id, BigDecimal x, BigDecimal y) {
        mapLoader.setMapTile(id, x.intValue(), y.intValue());
    }

    public final void setMapTile(int id, BigDecimal x, BigDecimal y, int layer) {
        mapLoader.setMapTile(id, x.intValue(), y.intValue(), layer);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y) {
        mapLoader.removeMapTile(x.intValue(), y.intValue());
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y, int layer) {
        mapLoader.removeMapTile(x.intValue(), y.intValue(), layer);
    }

    public final int getMapHeight() {
        return mapLoader.getMap().getHeight();
    }

    public final int getMapWidth() {
        return mapLoader.getMap().getWidth();
    }
    // </editor-fold>

    //start color methods
    public final void bgColor(Color color) {
        bgColor = color;
    }

    public final void bgColor(int color) {
        bgColor = pixelManager.getDrawColor(color);
    }

    public final void bgColor(String c) {
        bgColor = Colors.rgbToColor(c);
    }

    public final Color getColor(int color) {
        return pixelManager.getDrawColor(color);
    }

    public final Color getColor(String color) {
        return Colors.rgbToColor(color);
    }
    //end color methods

    // <editor-fold desc="String drawing methods" defaultstate="collapsed"> 
    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y) {
        g.setColor(color);
        g.drawString(text, x.floatValue(), y.floatValue());
    }

    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        g.setColor(color);
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue());
    }

    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        g.setColor(color);
        g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y) {
        drawString(pixelManager.getDrawColor(color), text, x, y);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        drawString(pixelManager.getDrawColor(color), text, x, y, width);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        drawString(pixelManager.getDrawColor(color), text, x, y, width, align);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y) {
        drawString(Colors.rgbToColor(color), text, x, y);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        drawString(Colors.rgbToColor(color), text, x, y, width);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        drawString(Colors.rgbToColor(color), text, x, y, width, align);
    }
    // </editor-fold>

    // <editor-fold desc="Sprite methods" defaultstate="collapsed"> 
    private void drawSpriteRotate(int id, BigDecimal x, BigDecimal y, BigDecimal degr, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate(degr.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.rotate(-degr.floatValue());
        usedSprites++;
    }

    private void drawSpriteFlip(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        usedSprites++;
    }

    //start 8x8 sprites. Default size 0 (8x8)
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        if (usedSprites >= maxSprites) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 0), x.floatValue(), y.floatValue());
        usedSprites++;
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr) {
        sprite(id, x, y, degr, 0);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        sprite(id, x, y, flipX, flipY, 0);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr) {
        sprite(id, x, y, flipX, flipY, degr, 0);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, size), x.floatValue(), y.floatValue());
        usedSprites++;
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr, int size) {
        drawSpriteRotate(id, x, y, degr, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        drawSpriteFlip(id, x, y, flipX, flipY, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate(degr.floatValue());
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        t.rotate(-degr.floatValue());
        usedSprites++;
    }
    //end sizable sprites

    //START special sprite mode
    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph) {
        sprite(ids, px, py, pw, ph, 0);
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        int x = px.intValue();
        int y = py.intValue();
        int inc = (size + 1) * 8;

        int i = 0;
        for (int h = 0; h < ph.intValue(); h++) {
            for (int w = 0; w < pw.intValue(); w++) {
                if (i < ids.size()) {
                    g.drawSprite(spriteLoader.getSprite(ids.get(i), size), x, y);
                    usedSprites++;
                }
                i++;
                x += inc;
            }
            x = px.intValue();
            y += inc;
        }
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY) {
        sprite(ids, px, py, pw, ph, flipX, flipY, 0);
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        if (!flipX && !flipY) {
            sprite(ids, px, py, pw, ph, size);
        } else {
            if (flipX && !flipY) {
                spriteArrayFlipX(ids, px, py, pw, ph, size);
            } else if (!flipX && flipY) {
                spriteArrayFlipY(ids, px, py, pw, ph, size);
            } else {
                spriteArrayFlipBoth(ids, px, py, pw, ph, size);
            }
        }
    }

    private void spriteArrayFlipBoth(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        int inc = (size + 1) * 8;
        int startX = px.intValue() + (pw.intValue() * inc) - inc;
        int y = py.intValue() + (ph.intValue() * inc) - inc;
        int x = startX;

        int i = 0;
        for (int h = ph.intValue(); h > 0; h--) {
            for (int w = pw.intValue(); w > 0; w--) {
                if (i < ids.size()) {
                    Sprite t = spriteLoader.getSprite(ids.get(i), size);
                    t.setFlip(true, true);
                    g.drawSprite(t, x, y);
                    t.setFlip(false, false);
                    usedSprites++;
                }
                i++;
                x -= inc;
            }
            x = startX;
            y -= inc;
        }
    }

    private void spriteArrayFlipY(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        int inc = (size + 1) * 8;
        int y = py.intValue() + (ph.intValue() * inc) - inc;
        int x = px.intValue();

        int i = 0;
        for (int h = ph.intValue(); h > 0; h--) {
            for (int w = 0; w < pw.intValue(); w++) {
                if (i < ids.size()) {
                    Sprite t = spriteLoader.getSprite(ids.get(i), size);
                    t.setFlip(false, true);
                    g.drawSprite(t, x, y);
                    t.setFlip(false, false);
                    usedSprites++;
                }
                i++;
                x += inc;
            }
            x = px.intValue();
            y -= inc;
        }
    }

    private void spriteArrayFlipX(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        int inc = (size + 1) * 8;
        int start = px.intValue() + (pw.intValue() * inc) - inc;
        int y = py.intValue();
        int x = start;

        int i = 0;
        for (int h = 0; h < ph.intValue(); h++) {
            for (int w = pw.intValue(); w > 0; w--) {
                if (i < ids.size()) {
                    Sprite t = spriteLoader.getSprite(ids.get(i), size);
                    t.setFlip(true, false);
                    g.drawSprite(t, x, y);
                    t.setFlip(false, false);
                    usedSprites++;
                }
                i++;
                x -= inc;
            }
            x = start;
            y += inc;
        }
    }

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
        spriteSc(id, x, y, scale, 0);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        spriteSc(id, x, y, scaleX, scaleY, 0);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        spriteSc(id, x, y, scaleX, scaleY, degr, 0);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY) {
        spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, 0);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr) {
        spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, 0);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale, int size) {
        spriteSc(id, x, y, scale, scale, size);
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setScale(1);
        usedSprites++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        t.rotate(degr.floatValue());
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.rotate(-degr.floatValue());
        t.setScale(1);
        usedSprites++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        t.setScale(1);
        usedSprites++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        if (usedSprites >= maxSprites) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        t.rotate(degr.floatValue());
        t.setFlip(flipX, flipY);
        g.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        t.rotate(-degr.floatValue());
        t.setScale(1);
        usedSprites++;
    }
    // </editor-fold>

    // <editor-fold desc="Shape methods" defaultstate="collapsed">
    /**
     * Internal drawPixel with color .
     *
     * @param color
     * @param x
     * @param y
     */
    private void drawPixel(Color color, int x, int y) {
        tint(color);
        g.drawSprite(pixelManager.getSprite(1), x, y);
        g.removeTint();
    }

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
     * Uses the white pixel with a colored tint to produce a pixel of any
     * desired RGB color String, ex: "255,255,255"
     *
     * @param color
     * @param x
     * @param y
     */
    public final void drawPixel(String color, BigDecimal x, BigDecimal y) {
        drawPixel(Colors.rgbToColor(color), x.intValue(), y.intValue());
    }

    /**
     * Uses the white pixel with a colored tint to produce a pixel of any
     * desired RGB color String, ex: "255,255,255"
     *
     * @param color
     * @param x
     * @param y
     */
    public final void drawPixel(Color color, BigDecimal x, BigDecimal y) {
        tint(color);
        g.drawSprite(pixelManager.getSprite(1), x.intValue(), y.intValue());
        g.removeTint();
    }

    public final void drawRect(Color c, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        for (int i = x.intValue(); i < x.intValue() + w.intValue(); i++) {
            drawPixel(c, i, y.intValue());
            drawPixel(c, i, y.intValue() + h.intValue() - 1);
        }
        for (int i = y.intValue(); i < y.intValue() + h.intValue(); i++) {
            drawPixel(c, x.intValue(), i);
            drawPixel(c, x.intValue() + w.intValue() - 1, i);
        }
    }

    public final void drawRect(int c, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        drawRect(pixelManager.getDrawColor(c), x, y, w, h);
    }

    public final void drawRect(String c, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        drawRect(Colors.rgbToColor(c), x, y, w, h);
    }

    public final void fillRect(Color color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        g.setColor(color);
        g.fillRect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
    }

    public final void fillRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        fillRect(pixelManager.getDrawColor(color), x, y, w, h);
    }

    public final void fillRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        fillRect(Colors.rgbToColor(color), x, y, w, h);
    }

    private void drawHLine(Color c, int x0, int x1, int y) {
        for (int i = x0; i < x1; i++) {
            drawPixel(c, i, y);
        }
    }

    private void drawVLine(Color c, int x, int y0, int y1) {
        for (int i = y0; i < y1; i++) {
            drawPixel(c, x, i);
        }
    }

    public final void drawLineSegment(int c, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        drawLineSegment(pixelManager.getDrawColor(c), x0, y0, x1, y1);
    }

    public final void drawLineSegment(String c, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        drawLineSegment(Colors.rgbToColor(c), x0, y0, x1, y1);
    }

    public final void drawLineSegment(Color c, BigDecimal p0, BigDecimal v0, BigDecimal p1, BigDecimal v1) {
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

    public final void drawLineSegment(Color c, int p0, int v0, int p1, int v1) {
        int x0 = p0;
        int x1 = p1;
        int y0 = v0;
        int y1 = v1;

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

    //https://www.geeksforgeeks.org/bresenhams-circle-drawing-algorithm/
    public final void drawCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        g.setColor(color);
        //g.drawCircle(x.intValue(), y.intValue(), r.intValue());
        int cx = x.intValue();
        int cy = y.intValue();
        int cr = r.intValue();
        int d = 3 - (2 * cr);
        int nx = 0;
        int ny = cr;
        while (nx <= ny) {
            drawPixel(color, cx + nx, cy + ny);
            drawPixel(color, cx + nx, cy - ny);
            drawPixel(color, cx - nx, cy + ny);
            drawPixel(color, cx - nx, cy - ny);

            drawPixel(color, cx + ny, cy + nx);
            drawPixel(color, cx + ny, cy - nx);
            drawPixel(color, cx - ny, cy + nx);
            drawPixel(color, cx - ny, cy - nx);

            if (d <= 0) {
                d += 4 * nx + 6;
            } else {
                ny--;
                d += 4 * (nx - ny) + 10;
            }
            nx++;
        }
    }

    public final void drawFillCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        g.setColor(color);
        //g.drawCircle(x.intValue(), y.intValue(), r.intValue());
        int cx = x.intValue();
        int cy = y.intValue();
        int cr = r.intValue();
        int d = 3 - (2 * cr);
        int nx = 0;
        int ny = cr;

        while (ny >= nx) {

            drawLineSegment(color, cx - nx, cy + ny, cx + nx + 1, cy + ny);
            drawLineSegment(color, cx - ny, cy + nx, cx + ny + 1, cy + nx);
            drawLineSegment(color, cx - nx, cy - ny, cx + nx + 1, cy - ny);
            drawLineSegment(color, cx - ny, cy - nx, cx + ny + 1, cy - nx);

            nx++;
            if (d > 0) {
                ny--;
                d += 4 * (nx - ny) + 10;
            } else {
                d += 4 * nx + 6;
            }
        }

    }

    public final void fillCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        g.setColor(color);
//        g.fillCircle(x.intValue(), y.intValue(), r.intValue());
        drawFillCircle(color, x, y, r);
    }

    public final void drawCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        drawCircle(pixelManager.getDrawColor(color), x, y, r);
    }

    public final void fillCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        fillCircle(pixelManager.getDrawColor(color), x, y, r);
    }

    public final void drawCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        drawCircle(Colors.rgbToColor(color), x, y, r);
    }

    public final void fillCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        fillCircle(Colors.rgbToColor(color), x, y, r);
    }
    // </editor-fold>

    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        g.setClip(x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
    }

    public final void removeClip() {
        g.removeClip();
    }

    // <editor-fold desc="Experimental methods" defaultstate="collapsed">
    public void tint(Color color) {
        g.setTint(color);
    }

    public void tint(int color) {
        tint(pixelManager.getDrawColor(color));
    }

    public void tint(String color) {
        tint(Colors.rgbToColor(color));
    }

    /**
     * Removes the applied tint color from the previous tint methods:
     * {@link #tint(org.mini2Dx.core.graphics.Color) }
     * {@link #tint(int) }
     * {@link #tint(java.lang.String) }
     *
     * This isn't very obvious.
     */
    public void tint() {
        g.removeTint();
    }

    /**
     * Convenience method to retrieve a color at a specified pixel coordinate of
     * the frame buffer.
     *
     * note that the y coordinate parameter must be -1 in order to get the
     * correct pixel.
     *
     * @param x
     * @param y
     * @return The Color at the specified coordinate of the {@link #frameBuffer}
     */
    public Color getPixel(BigDecimal x, BigDecimal y) {
        int gx = MathUtils.ceil((viewport.getX() * viewport.getScaleX()) + (x.floatValue() * viewport.getScaleX()));
        int gy = MathUtils.ceil((viewport.getY() * viewport.getScaleY()) + (y.floatValue() * viewport.getScaleY()));
        gy = Gdx.graphics.getHeight() - gy;
        frameBuffer.end();

        byte[] b = ScreenUtils.getFrameBufferPixels(gx, gy - 1, 1, 1, false);
        frameBuffer.begin();

        int re = 0xFF & b[0];
        int gr = 0xFF & b[1];
        int bl = 0xFF & b[2];
        return Colors.rgbToColor(re + "," + gr + "," + bl);
    }

    public Color getPixel(String name, BigDecimal x, BigDecimal y) {
        Pixmap pm = Mdx.graphics.newPixmap(Mdx.files.external(path + "/Art/" + name));
        Color c = Mdx.graphics.newColor(pm.getPixel(x.intValue(), y.intValue()));
        pm.dispose();
        return c;
    }

    public ArrayList<Color> getPixels(String name) {
        Pixmap pm = Mdx.graphics.newPixmap(Mdx.files.local(path + "/Art/" + name));
        ArrayList<Color> colors = new ArrayList<>();
        for (int y = 0; y < pm.getHeight(); y++) {
            for (int x = 0; x < pm.getWidth(); x++) {
                colors.add(Mdx.graphics.newColor(pm.getPixel(x, y)));
            }
        }
        pm.dispose();
        return colors;
    }
    // </editor-fold>

}
