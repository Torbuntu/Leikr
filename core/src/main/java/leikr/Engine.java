/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

    // Override functions for game scripting.
    void preCreate() {
        spriteLoader = new SpriteLoader();
        sprites = spriteLoader.getSpriteBank();
        fontLoader = new FontLoader();
        font = fontLoader.getFont();
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
        this.g.setColor(Color.GREEN);
    }

    public void render() {
    }
    // end override functions

    void setDrawColor(int color) {
        switch (color) {
            case 0:
                g.setColor(Color.BLACK);
                break;
            case 1:
                g.setColor(Color.WHITE);
                break;
            case 2:
                g.setColor(Color.RED);
                break;
            case 3:
                g.setColor(Color.GREEN);
                break;
            case 4:
                g.setColor(Color.BLUE);
                break;
            case 5:
                g.setColor(Color.YELLOW);
                break;
            case 6:
                g.setColor(Color.BROWN);
                break;
            case 7:
                g.setColor(Color.MAGENTA);
                break;
            case 8:
                g.setColor(Color.CYAN);
                break;
            case 9:
                g.setColor(Color.TEAL);
                break;
            case 10:
                g.setColor(Color.TAN);
                break;
            case 11:
                g.setColor(Color.FOREST);
                break;
            case 12:
                g.setColor(Color.PINK);
                break;
            case 13:
                g.setColor(Color.ORANGE);
                break;
            case 14:
                g.setColor(Color.PURPLE);
                break;
            case 15:
                g.setColor(Color.CORAL);
                break;
        }
    }

    public void bgColor(int color) {
        setDrawColor(color);
        g.fillRect(0, 0, 320, 240);
    }

    public void drawText(String text, float x, float y) {
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

    public void square(float x, float y, float w, float h) {
        g.drawRect(x, y, w, h);
    }

    void square(float x, float y, float w, float h, int fill) {
        setDrawColor(fill);
        g.fillRect(x, y, w, h);
    }

    void circle(float x, float y, float r) {
        g.drawCircle(x, y, r);
    }

    void circle(float x, float y, float r, int fill) {
        setDrawColor(fill);
        g.fillCircle(x, y, r);
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        g.drawTriangle(x1, y1, x2, y2, x3, y3);
    }

    void triangle(float x1, float y1, float x2, float y2, float x3, float y3, int fill) {
        setDrawColor(fill);
        g.fillTriangle(x1, y1, x2, y2, x3, y3);
    }

    void line(float x1, float y1, float x2, float y2) {
        g.drawLineSegment(x1, x2, y1, y2);
    }
    void line(float x1, float y1, float x2, float y2, int color) {
        setDrawColor(color);
        g.drawLineSegment(x1, x2, y1, y2);
    }

    boolean button(int button) {
        return Gdx.input.isButtonPressed(button);
    }

    boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }
}
