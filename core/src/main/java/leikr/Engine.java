/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import leikr.controls.LeikrController;
import leikr.controls.ButtonCodes;
import leikr.loaders.SpriteLoader;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import leikr.loaders.AudioLoader;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;

/**
 *
 * @author tor
 */
public class Engine {

    Graphics g;
    FitViewport viewport;

    //used by the Engine Screen to determine if the game is actively running.
    boolean active;

    LeikrController p1Controller;
    LeikrController p2Controller;

    MapLoader mapLoader;
    ImageLoader imageLoader;
    SpriteLoader spriteLoader;
    AudioLoader audioLoader;

    FPSLogger logger;
    private int MAX_SPRITES;
    private int USED_SPRITES;
    public ButtonCodes BTN; //codes for the buttons for readability

    //custom prop functions
    public int getUsedSprites() {
        return USED_SPRITES;
    }
    //end custom prop functions

    // Override functions for game scripting.
    public void preCreate(int mSprites, int mSpriteSheets) {
        MAX_SPRITES = mSprites;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        logger = new FPSLogger();
        BTN = new ButtonCodes();
        spriteLoader = new SpriteLoader(mSpriteSheets);
        imageLoader = new ImageLoader();
        mapLoader = new MapLoader();
        audioLoader = new AudioLoader();
        active = true;

        try {
            Array<Controller> nmc = Controllers.getControllers();
            if (null != nmc.get(0)) {
                Controller p1c = nmc.get(0);
                p1Controller = new LeikrController();
                p1c.addListener(p1Controller);
            }
            if (null != nmc.get(1)) {
                Controller p2c = nmc.get(1);
                p2Controller = new LeikrController();
                p2c.addListener(p2Controller);
            }
        } catch (Exception ex) {
            System.out.println("Controllers not active: " + ex.getMessage());
        }

    }

    public void create() {
    }

    public void update(float delta) {
        mapLoader.getMap().update(delta);
    }

    void preRender(Graphics g) {
        this.g = g;
        //set to 0 before drawing anything
        USED_SPRITES = 0;
    }

    public void render() {
    }
    // end override functions

    //dispose
    public void dispose() {
        audioLoader.disposeAudioLoader();
        mapLoader.disposeMap();
        spriteLoader.disposeSprites();
        imageLoader.disposeImages();
    }
    //dispose

    //Start Helper methods
    //Not a very helpful method, but I like to see how things perform.
    void FPS() {
        logger.log();
    }

    int getScreenWidth() {
        return GameRuntime.WIDTH;
    }

    int getScreenHeight() {
        return GameRuntime.HEIGHT;
    }
    //End helper methods.

    //Image methods
    void loadImages() {
        imageLoader.load();
    }

    void image(String name, float x, float y) {
        g.drawTexture(imageLoader.getImage(name), x, y);
    }

    void image(String name, float x, float y, float w, float h) {
        g.drawTexture(imageLoader.getImage(name), x, y, w, h);
    }

    void image(String name, float x, float y, float w, float h, boolean flipv) {
        g.drawTexture(imageLoader.getImage(name), x, y, w, h, flipv);
    }
    //end Image methods

    //Map methods
    void loadMap(String map) {
        mapLoader.loadMap(map);
    }

    void map() {
        mapLoader.drawMap(g);
    }

    void map(float x, float y) {
        mapLoader.drawMap(g, Math.round(x), Math.round(y));
    }

    void map(float x, float y, int layer) {
        mapLoader.drawMap(g, Math.round(x), Math.round(y), layer);
    }

    void map(float x, float y, float sx, float sy, float w, float h) {
        mapLoader.drawMap(g, Math.round(x), Math.round(y), Math.round(sx), Math.round(sy), Math.round(w), Math.round(h));
    }

    void map(float x, float y, float sx, float sy, float w, float h, int layer) {
        mapLoader.drawMap(g, Math.round(x), Math.round(y), Math.round(sx), Math.round(sy), Math.round(w), Math.round(h), layer);
    }

    int mapGet(float x, float y) {
        return mapLoader.getMapTileId(x, y);
    }

    void mapSet(float x, float y, int id) {
        mapLoader.setMapTile(x, y, id);
    }

    int getMapHeight() {
        return mapLoader.getMap().getHeight();
    }

    int getMapWidth() {
        return mapLoader.getMap().getWidth();
    }
    //end Map methods

    //start color methods
    void drawColor(int color) {
        g.setColor(getDrawColor(color));
    }

    void drawColor(float r, float gr, float b) {
        Color tmp = new Color(r, gr, b, 1f);
        g.setColor(tmp);
    }

    Color getDrawColor(int color) {
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

    void bgColor(int color) {
        drawColor(color);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    void bgColor(float r, float gr, float b) {
        drawColor(r, gr, b);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }

    void bgColor(float[] color) {
        drawColor(color[0], color[1], color[2]);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }
    //end color methods

    //text methods
    void text(String text, float x, float y, int color) {
        drawColor(color);
        g.drawString(text, x, y);
    }

    void text(String text, float x, float y, float[] color) {
        drawColor(color[0], color[1], color[2]);
        g.drawString(text, x, y);
    }
    //end text methods
    
    //start 8x8 sprites
    void sprite(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        USED_SPRITES++;
    }

    void sprite(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 0).rotate(degr);
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        spriteLoader.getSprite(id, 0).rotate(-degr);
        USED_SPRITES++;
    }

    void sprite(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 0).rotate90(clockwise);
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        spriteLoader.getSprite(id, 0).rotate90(!clockwise);
        USED_SPRITES++;
    }

    void sprite(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 0).setFlip(flipX, flipY);
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        spriteLoader.getSprite(id, 0).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 8x8 sprites

    //start 16x16 sprites
    void sprite16(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 1), x, y);
        USED_SPRITES++;
    }

    void sprite16(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 1).rotate(degr);
        g.drawSprite(spriteLoader.getSprite(id, 1), x, y);
        spriteLoader.getSprite(id, 1).rotate(-degr);
        USED_SPRITES++;
    }

    void sprite16(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 1).rotate90(clockwise);
        g.drawSprite(spriteLoader.getSprite(id, 1), x, y);
        spriteLoader.getSprite(id, 1).rotate90(!clockwise);
        USED_SPRITES++;
    }

    void sprite16(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 1).setFlip(flipX, flipY);
        g.drawSprite(spriteLoader.getSprite(id, 1), x, y);
        spriteLoader.getSprite(id, 1).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 16x16 sprites

    //start 32x32 sprites
    void sprite32(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 2), x, y);
        USED_SPRITES++;
    }

    void sprite32(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 2).rotate(degr);
        g.drawSprite(spriteLoader.getSprite(id, 2), x, y);
        spriteLoader.getSprite(id, 2).rotate(-degr);
        USED_SPRITES++;
    }

    void sprite32(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 2).rotate90(clockwise);
        g.drawSprite(spriteLoader.getSprite(id, 2), x, y);
        spriteLoader.getSprite(id, 2).rotate90(!clockwise);
        USED_SPRITES++;
    }

    void sprite32(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 2).setFlip(flipX, flipY);
        g.drawSprite(spriteLoader.getSprite(id, 2), x, y);
        spriteLoader.getSprite(id, 2).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 32x32 sprites

    //start 64x64 sprites
    void sprite64(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(spriteLoader.getSprite(id, 3
        ), x, y);
        USED_SPRITES++;
    }

    void sprite64(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 3).rotate(degr);
        g.drawSprite(spriteLoader.getSprite(id, 3), x, y);
        spriteLoader.getSprite(id, 3).rotate(-degr);
        USED_SPRITES++;
    }

    void sprite64(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 3).rotate90(clockwise);
        g.drawSprite(spriteLoader.getSprite(id, 3), x, y);
        spriteLoader.getSprite(id, 3).rotate90(!clockwise);
        USED_SPRITES++;
    }

    void sprite64(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        spriteLoader.getSprite(id, 3).setFlip(flipX, flipY);
        g.drawSprite(spriteLoader.getSprite(id, 3), x, y);
        spriteLoader.getSprite(id, 3).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 64x64 sprites

    //draws a sprite given the id, and the next sprite in the sequence on top of it.
    void tallSprite(int id, float x, float y) {
        g.drawSprite(spriteLoader.getSprite(id, 0), x, y);
        g.drawSprite(spriteLoader.getSprite(id + 1, 0), x, y - 8);
    }

    //start shape drawing methods
    void rect(float x, float y, float w, float h) {
        g.drawRect(x, y, w, h);
    }

    void rect(float x, float y, float w, float h, boolean fill) {
        if (fill) {
            g.fillRect(x, y, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
    }

    void circle(float x, float y, float r) {
        g.drawCircle(x, y, r);
    }

    void circle(float x, float y, float r, boolean fill) {
        if (fill) {
            g.fillCircle(x, y, r);
        } else {
            g.drawCircle(x, y, r);
        }
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        g.drawTriangle(x1, y1, x2, y2, x3, y3);
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3, boolean fill) {
        if (fill) {
            g.fillTriangle(x1, y1, x2, y2, x3, y3);
        } else {
            g.drawTriangle(x1, y1, x2, y2, x3, y3);
        }
    }

    void line(float x1, float y1, float x2, float y2) {
        g.drawLineSegment(x1, x2, y1, y2);
    }
    //end shape drawing methods

    //start Audio handling
    void sfx(String name) {
        audioLoader.sound(name);
    }
    void sfx(String name, float vol, float pit, float pan){
        audioLoader.sound(name, vol, pit, pan);
    }
    void music(String name) {
        audioLoader.music(name);
    }
    void music(String name, boolean loop){
        audioLoader.music(name, loop);
    }
    void stopAllMusic(){
        audioLoader.stopMusic();
    }
    void stopMusic(String fileName){
        audioLoader.stopMusic(fileName);
    }
    //end audio handling

    //start input handling
    boolean button(int button) {
        //assume single player game, only p1Controller
        return (null != p1Controller) ? p1Controller.button(button) : false;
    }

    boolean button(int button, int player) {
        if (null != p1Controller && player == 0) {
            return p1Controller.button(button);
        }
        if (null != p2Controller && player == 1) {
            return p2Controller.button(button);
        }
        //default search is false, in case there are no controllers.
        return false;
    }

    //detect keyboard key presses
    boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }
    //end input handling

}
