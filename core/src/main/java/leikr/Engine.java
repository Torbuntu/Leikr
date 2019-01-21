/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.viewport.FitViewport;

/**
 *
 * @author tor
 */
public class Engine {

    Graphics g;
    SpriteLoader spriteLoader;
    ArrayList<Sprite> sprites;
    ArrayList<Sprite> mediumSprites;
    ArrayList<Sprite> largeSprites;

    LeikrController p1Controller;
    LeikrController p2Controller;

    MapLoader mapLoader;

    private int MAX_SPRITES;
    private int USED_SPRITES;

    public static ButtonCodes BTN; //static codes for the buttons for readability

    FPSLogger logger;
    FitViewport viewport;

    //custom prop functions
    public void setMaxSprites(int ms) {
        this.MAX_SPRITES = ms;
    }

    public int getUsedSprites() {
        return USED_SPRITES;
    }
    //end custom prop functions

    // Override functions for game scripting.
    void preCreate() {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        mapLoader = new MapLoader();

        logger = new FPSLogger();
        BTN = new ButtonCodes();
        spriteLoader = new SpriteLoader();

        sprites = spriteLoader.getSpriteBank();
        mediumSprites = spriteLoader.getMediumSpriteBank();
        largeSprites = spriteLoader.getLargeSpriteBank();

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
            System.out.println("Controllers: " + ex.getMessage());
        }

    }

    public void create() {
    }

    public void update() {
    }

    public void update(float delta) {
        mapLoader.getMap().update(delta);
    }

    void preRender(Graphics g) {
        this.g = g;
        viewport.apply(this.g);
        //set to 0 before drawing anything
        USED_SPRITES = 0;
    }

    public void render() {
    }
    // end override functions

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

    int mapCellId(float x, float y) {
        return mapLoader.getMapTileId(x, y);
    }
    void setCellId(float x, float y, int id){
        mapLoader.setMapTile(x, y, id);
    }
    
    int getMapHeight(){
        return mapLoader.getMap().getHeight();
    }
    int getMapWidth(){
        return mapLoader.getMap().getWidth();
    }
    //end Map methods

    //start color methods
    void setDrawColor(int color) {
        g.setColor(getDrawColor(color));
    }

    public Color getDrawColor(int color) {
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

    public void bgColor(int color) {
        setDrawColor(color);
        g.fillRect(-1, -1, GameRuntime.WIDTH + 1, GameRuntime.HEIGHT + 1);
    }
    //end color methods

    //start 8x8 sprites
    public void sprite(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(sprites.get(id), x, y);
        USED_SPRITES++;
    }

    public void sprite(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        sprites.get(id).rotate(degr);
        g.drawSprite(sprites.get(id), x, y);
        sprites.get(id).rotate(-degr);
        USED_SPRITES++;
    }

    public void sprite(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        sprites.get(id).rotate90(clockwise);
        g.drawSprite(sprites.get(id), x, y);
        sprites.get(id).rotate90(!clockwise);
        USED_SPRITES++;
    }

    public void sprite(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        sprites.get(id).setFlip(flipX, flipY);
        g.drawSprite(sprites.get(id), x, y);
        sprites.get(id).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 8x8 sprites

    //start 16x16 sprites
    public void sprite16(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(mediumSprites.get(id), x, y);
        USED_SPRITES++;
    }

    public void sprite16(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        mediumSprites.get(id).rotate(degr);
        g.drawSprite(mediumSprites.get(id), x, y);
        mediumSprites.get(id).rotate(-degr);
        USED_SPRITES++;
    }

    public void sprite16(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        mediumSprites.get(id).rotate90(clockwise);
        g.drawSprite(mediumSprites.get(id), x, y);
        mediumSprites.get(id).rotate90(!clockwise);
        USED_SPRITES++;
    }

    public void sprite16(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        mediumSprites.get(id).setFlip(flipX, flipY);
        g.drawSprite(mediumSprites.get(id), x, y);
        mediumSprites.get(id).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 16x16 sprites

    //start 64x64 sprites
    public void sprite64(int id, float x, float y) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        g.drawSprite(largeSprites.get(id), x, y);
        USED_SPRITES++;
    }

    public void sprite64(int id, float x, float y, float degr) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        largeSprites.get(id).rotate(degr);
        g.drawSprite(largeSprites.get(id), x, y);
        largeSprites.get(id).rotate(-degr);
        USED_SPRITES++;
    }

    public void sprite64(int id, float x, float y, boolean clockwise) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        largeSprites.get(id).rotate90(clockwise);
        g.drawSprite(largeSprites.get(id), x, y);
        largeSprites.get(id).rotate90(!clockwise);
        USED_SPRITES++;
    }

    public void sprite64(int id, float x, float y, boolean flipX, boolean flipY) {
        if (USED_SPRITES >= MAX_SPRITES) {
            return;
        }
        largeSprites.get(id).setFlip(flipX, flipY);
        g.drawSprite(largeSprites.get(id), x, y);
        largeSprites.get(id).setFlip(!flipX, !flipY);
        USED_SPRITES++;
    }
    //end 64x64 sprites

    //draws a sprite given the id, and the next sprite in the sequence on top of it.
    public void tallSprite(int id, float x, float y) {
        g.drawSprite(sprites.get(id), x, y);
        g.drawSprite(sprites.get(id + 1), x, y - 8);
    }

    public void drawText(String text, float x, float y, int color) {
        setDrawColor(color);
        g.drawString(text, x, y);
    }
    //start shape drawing methods

    public void square(float x, float y, float w, float h, int color) {
        setDrawColor(color);
        g.drawRect(x, y, w, h);
    }

    void square(float x, float y, float w, float h, int color, boolean fill) {
        setDrawColor(color);
        if (fill) {
            g.fillRect(x, y, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
    }

    void circle(float x, float y, float r, int color) {
        setDrawColor(color);
        g.drawCircle(x, y, r);
    }

    void circle(float x, float y, float r, int color, boolean fill) {
        setDrawColor(color);
        if (fill) {
            g.fillCircle(x, y, r);
        } else {
            g.drawCircle(x, y, r);
        }
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        setDrawColor(color);
        g.drawTriangle(x1, y1, x2, y2, x3, y3);
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int color, boolean fill) {
        setDrawColor(color);
        if (fill) {
            g.fillTriangle(x1, y1, x2, y2, x3, y3);
        } else {
            g.drawTriangle(y3, y1, y2, y3, y3, y3);
        }
    }

    void line(float x1, float y1, float x2, float y2, int color) {
        setDrawColor(color);
        g.drawLineSegment(x1, x2, y1, y2);
    }
    //end shape drawing methods

    //start input handling
    boolean button(int button) {
        //assume single player game, only p1Controller
        return p1Controller.button(button);
    }

    boolean button(int button, int player) {
        if (null != p1Controller && player == 1) {
            return p1Controller.button(button);
        }
        if (null != p2Controller && player == 2) {
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
