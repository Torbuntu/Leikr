/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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

    // Override functions for game scripting.
    void preCreate() {
        spriteLoader = new SpriteLoader();
        sprites = spriteLoader.getSpriteBank();
    }

    public void create() {
    }

    public void update() {
    }

    public void update(float delta) {
    }

    public void preRender(Graphics g) {
        this.g = g;
        this.g.setColor(Color.GREEN);
    }

    public void render() {
    }
    // end override functions

    void setColor(String color) {
        switch (color.toLowerCase()) {
            case "red":
                g.setColor(Color.RED);
                break;
            case "green":
                g.setColor(Color.GREEN);
                break;
            case "blue":
                g.setColor(Color.BLUE);
                break;
            case "yellow":
                g.setColor(Color.YELLOW);
                break;
            case "black":
                g.setColor(Color.BLACK);
                break;
            case "white":
                g.setColor(Color.WHITE);
                break;
        }
    }

    void setColor(int color) {
        switch (color) {
            case 0:
                g.setColor(Color.RED);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
            case 2:
                g.setColor(Color.BLUE);
                break;
            case 3:
                g.setColor(Color.YELLOW);
                break;
            case 4:
                g.setColor(Color.BLACK);
                break;
            case 5:
                g.setColor(Color.WHITE);
                break;
        }
    }
    
    public void sprite(int id, float x, float y){
        g.drawSprite(sprites.get(id), x, y);
    }

    public void square(float x, float y, float w, float h) {
        g.drawRect(x, y, w, h);
    }

    void square(float x, float y, float w, float h, String fill) {
        setColor(fill);
        g.fillRect(x, y, w, h);
    }

    void square(float x, float y, float w, float h, int fill) {
        setColor(fill);
        g.fillRect(x, y, w, h);
    }

    void circle(float x, float y, float r) {
        g.drawCircle(x, y, r);
    }

    void circle(float x, float y, float r, String fill) {
        setColor(fill);
        g.fillCircle(x, y, r);
    }

    void circle(float x, float y, float r, int fill) {
        setColor(fill);
        g.fillCircle(x, y, r);
    }

    void triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        g.drawTriangle(x1, y1, x2, y2, x3, y3);
    }

    void triangle(int x1, int y1, int x2, int y2, int x3, int y3, String fill) {
        setColor(fill);
        g.fillTriangle(x1, y1, x2, y2, x3, y3);
    }

    void line(int x1, int y1, int x2, int y2) {
        g.drawLineSegment(x1, x2, y1, y2);
    }

    boolean button(String button) {
        return Gdx.input.isButtonPressed(0);
    }

    boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }
}
