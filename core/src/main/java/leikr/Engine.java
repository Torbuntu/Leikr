/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.graphics.Graphics;

/**
 *
 * @author tor
 */
public class Engine {

    Graphics g;

    public void init() {
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

    void square(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    void line(int x1, int y1, int x2, int y2) {
        g.drawLineSegment(x1, x2, y1, y2);
    }

    boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }
}
