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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import leikr.GameRuntime;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.PixmapFormat;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;

/**
 * This class is used to manage the drawing API for the Engine. It also provides
 * a screen instance which can be passed to non Engine extending classes in the
 * game code.
 *
 * @author tor
 */
public class LeikrScreenManager {

    /*
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    SpriteLoader spriteLoader;
    ImageLoader imageLoader;
    MapLoader mapLoader;

    SpriteSheet pixels;

    List<Color> colorPalette;

    private static LeikrScreenManager instance;

    /*
     * Properties set by Custom Properties
     *
     * These can be overwritten for a more custom experience.
     */
    private int MAX_SPRITES;
    private int USED_SPRITES;

    /**
     * LeikrScreenManager constructor
     *
     * @param mSprites
     */
    private LeikrScreenManager() {
        createPalette();
        createPixmap();
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
    }

    private void createPalette() {
        colorPalette = Arrays.asList(Colors.CLEAR(), Colors.WHITE(), Colors.WHITE_M1(), Colors.LIGHT_GRAY(), Colors.GRAY(),
                Colors.DARK_GRAY(), Colors.BLACK_P1(), Colors.BLACK(), Colors.RED(), Colors.GREEN(), Colors.BLUE(), Colors.MAROON(),
                Colors.CORAL(), Colors.SALMON(), Colors.PINK(), Colors.LIME(), Colors.FOREST(), Colors.OLIVE(), Colors.NAVY(), Colors.ROYAL(), Colors.SKY(),
                Colors.CYAN(), Colors.TEAL(), Colors.YELLOW(), Colors.GOLD(), Colors.GOLDENROD(), Colors.ORANGE(),
                Colors.BROWN(), Colors.TAN(), Colors.FIREBRICK(), Colors.PURPLE(), Colors.VIOLET(), Colors.MAGENTA());
    }

    private void createPixmap() {
        Pixmap pm = Mdx.graphics.newPixmap(colorPalette.size(), 1, PixmapFormat.RGBA8888);
        for (int i = 0; i < colorPalette.size(); i++) {
            pm.setColor(getDrawColor(i));
            pm.drawPixel(i, 0);
        }
        pixels = new SpriteSheet(Mdx.graphics.newTexture(pm), 1, 1);
        pm.dispose();
    }

    //Helper methods
    public int getUsedSprites() {
        return USED_SPRITES;
    }
    //End helper methods

    //Engine methods
    public void preRender() {
        //set to 0 before drawing anything
        USED_SPRITES = 0;
    }

    public void preUpdate(float delta) {
        if (null == mapLoader.getMap()) {
            return;//don't update the drawMap if it is null
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

    public final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        Mdx.graphicsContext.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue());
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        Mdx.graphicsContext.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        Mdx.graphicsContext.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue(), flipv);
    }
    //end Image methods

    //Map methods
    public final void loadMap(String map) {
        mapLoader.loadMap(map);
    }

    public final void drawMap() {
        mapLoader.drawMap(Mdx.graphicsContext);
    }

    public final void drawMap(BigDecimal x, BigDecimal y) {
        mapLoader.drawMap(Mdx.graphicsContext, x.intValue(), y.intValue());
    }

    public final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        mapLoader.drawMap(Mdx.graphicsContext, x.intValue(), y.intValue(), layer);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        mapLoader.drawMap(Mdx.graphicsContext, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue());
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        mapLoader.drawMap(Mdx.graphicsContext, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue(), layer);
    }

    public final int getMapTileId(BigDecimal x, BigDecimal y) {
        return mapLoader.getMapTileId(x.intValue(), y.intValue());
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
    public final Color getDrawColor(int color) {
        if (color > colorPalette.size() || color < 0) {
            return Colors.BLACK();
        }
        return colorPalette.get(color);
    }

    public final void setColor(int color) {
        Mdx.graphicsContext.setColor(getDrawColor(color));
    }

    public final void setColor(String c) {
        Mdx.graphicsContext.setColor(Colors.rgbToColor(c));
    }

    public final void setColor(String c, boolean alpha) {
        if (alpha) {
            Mdx.graphicsContext.setColor(Colors.rgbaToColor(c));
        } else {
            Mdx.graphicsContext.setColor(Colors.rgbToColor(c));
        }
    }

    public final void bgColor(int color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    public final void bgColor(String c) {
        LeikrScreenManager.this.setColor(c);
        Mdx.graphicsContext.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    //end color methods
    //text methods
    public final void drawString(String text, BigDecimal x, BigDecimal y, int color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue());
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, String color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue());
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue(), width.floatValue());
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, String color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue(), width.floatValue());
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int align, int color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int align, String color) {
        LeikrScreenManager.this.setColor(color);
        Mdx.graphicsContext.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align);
    }
    //end drawString methods

    //sprite helper methods.
    private void drawSpriteRotate(int id, int size, BigDecimal degr, BigDecimal x, BigDecimal y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.rotate(degr.floatValue());
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
        t.rotate(-degr.floatValue());
        USED_SPRITES++;
    }

    private void drawSpriteFlip(int id, BigDecimal x, BigDecimal y, int size, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, size);
        t.setFlip(flipX, flipY);
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
        t.setFlip(false, false);
        USED_SPRITES++;
    }

    //start 8x8 sprites. Default size 0 (8x8)
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Mdx.graphicsContext.drawSprite(spriteLoader.getSprite(id, 0), x.floatValue(), y.floatValue());
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
        Mdx.graphicsContext.drawSprite(spriteLoader.getSprite(id, size), x.floatValue(), y.floatValue());
        USED_SPRITES++;
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr, int size) {
        drawSpriteRotate(id, size, degr, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        drawSpriteFlip(id, x, y, size, flipX, flipY);
    }
    //end sizable sprites

    //START special sprite mode
    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.scale(scale.floatValue());
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
        t.scale(-scale.floatValue());
        USED_SPRITES++;
    }

    public final void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        Sprite t = spriteLoader.getSprite(id, 0);
        t.setScale(scaleX.floatValue(), scaleY.floatValue());
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
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
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
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
        Mdx.graphicsContext.drawSprite(t, x.floatValue(), y.floatValue());
        t.flip(!flipX, !flipY);
        t.setScale(-scaleX.floatValue(), -scaleY.floatValue());
        USED_SPRITES++;
    }
    //END special sprite mode

    //start shape drawing methods
    public final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        Mdx.graphicsContext.drawSprite(pixels.getSprite(color), x.intValue(), y.intValue());
    }

    public final void drawRect(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        Mdx.graphicsContext.drawRect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
    }

    public final void fillRect(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        Mdx.graphicsContext.fillRect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
    }

    public final void drawLineSegment(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2) {
        Mdx.graphicsContext.drawLineSegment(x.intValue(), y.intValue(), x2.intValue(), y2.intValue());
    }
    //end shape drawing methods

    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        Mdx.graphicsContext.setClip(x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
    }

    public final void removeClip() {
        Mdx.graphicsContext.removeClip();
    }

    //EXPERIMENTAL METHODS
    public void tint(int color) {
        Mdx.graphicsContext.setTint(getDrawColor(color));
    }

    public void tint() {
        Mdx.graphicsContext.removeTint();
    }

//END EXPERIMENTAL
}
