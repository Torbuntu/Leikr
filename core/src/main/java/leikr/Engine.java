/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

/**
 *
 * @author tor
 */
public class Engine {

    Graphics g;
    SpriteLoader spriteLoader;
    ArrayList<Sprite> sprites;
    FontLoader fontLoader;
    BitmapFont font;
    LeikrController p1Controller;
    LeikrController p2Controller;

    enum BTN {
        X, A, B, Y, LEFT_BUMPER, RIGHT_BUMPER, nil6, nil7, SELECT, START, UP, RIGHT, DOWN, LEFT;
    }

    // Override functions for game scripting.
    void preCreate() {
        spriteLoader = new SpriteLoader();
        sprites = spriteLoader.getSpriteBank();
        fontLoader = new FontLoader();
        font = fontLoader.getFont();
        try {
            int nmc = Controllers.getControllers().size;
            if (nmc > 0) {
                p1Controller = new LeikrController(0);            
                if (nmc > 1) {
                    p2Controller = new LeikrController(1);
                }
            }
        } catch (Exception ex) {
            System.out.println("No controllers active.");
        }
        
    }

    public void create() {
    }

    public void update() {
    }

    public void update(float delta) {
    }

    public void preRender(Graphics g) {
        this.g = g;
        this.g.setFont(font);
    }

    public void render() {
    }
    // end override functions

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
        g.setBackgroundColor(getDrawColor(color));
    }

    public void drawText(String text, float x, float y, int color) {
        setDrawColor(color);
        g.drawString(text, x, y);
    }

    public void sprite(int id, float x, float y) {
        g.drawSprite(sprites.get(id), x, y);
    }

    public void sprite(int id, float x, float y, boolean clockwise) {
        sprites.get(id).rotate90(clockwise);
        g.drawSprite(sprites.get(id), x, y);
        sprites.get(id).rotate90(!clockwise);
    }

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

    boolean button(int button, int player) {
         if (null != p1Controller) {
            if (player == 1) {
                return p1Controller.button(button);
            }
        }
        if (null != p2Controller) {
            if (player == 2) {
                return p2Controller.button(button);
            }
        }
        //default search is false
        return false;
    }

    boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }
}
