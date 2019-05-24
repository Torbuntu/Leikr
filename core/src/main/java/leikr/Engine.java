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
package leikr;

import leikr.controls.LeikrControllerListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.MathUtils;
import leikr.loaders.AudioLoader;
import leikr.managers.LeikrScreenManager;
import leikr.screens.EngineScreen;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;

/**
 *
 * @author tor
 */
public abstract class Engine implements InputProcessor {

    //Mini2DX specific classes for managing the screen state and drawing.
    Graphics g;
    FitViewport viewport;
    FPSLogger logger;

    //used by the Engine Screen to determine if the game should be actively running.
    boolean active;

    public static enum BTN {
        X,
        A,
        B,
        Y,
        LEFT_BUMPER,
        RIGHT_BUMPER,
        SELECT,
        START,
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    /**
     * Controllers and listeners for handling custom controller input
     */
    LeikrControllerListener p1ControllerListener;
    LeikrControllerListener p2ControllerListener;
    Controller playerOneController;
    Controller playerTwoController;

    /**
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    private AudioLoader audioLoader;
    public LeikrScreenManager screen;

    //custom prop functions
    int getUsedSprites() {
        return screen.getUsedSprites();
    }
    //end custom prop functions

    public final void preCreate(int mSprites) {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        logger = new FPSLogger();
        audioLoader = new AudioLoader();
        screen = new LeikrScreenManager(mSprites);
        active = true;
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                p1ControllerListener = new LeikrControllerListener();
                Controllers.getControllers().get(0).addListener(p1ControllerListener);

            }
            if (Controllers.getControllers().size > 1) {
                p2ControllerListener = new LeikrControllerListener();
                Controllers.getControllers().get(1).addListener(p2ControllerListener);
            }
        } catch (Exception ex) {
            System.out.println("Controllers not active: " + ex.getMessage());
        }
    }

    public final void preUpdate(float delta) {
        screen.preUpdate(delta);
    }

    public final void preRender(Graphics g) {
        this.g = g;
        viewport.apply(this.g);
        screen.preRender(this.g);
    }

    public final void postRender() {
        screen.postRender();
    }

    public void usePixels() {
        screen.usePixels();
    }

    /**
     * Override functions for game scripting. These are abstract and are
     * required to be implemented in the game code.
     */
    abstract public void create();

    abstract public void update(float delta);

    abstract public void render();
    // end override functions

    //disposes the game objects on exit
    public final void dispose() {
        audioLoader.disposeAudioLoader();
        screen.dispose();
        if (null != playerOneController) {
            playerOneController.removeListener(p1ControllerListener);
        }
        if (null != playerTwoController) {
            playerTwoController.removeListener(p2ControllerListener);
        }
    }
    //dispose

    //Start Helper methods
    //Not a very helpful method, but I like to see how things perform.
    public final void FPS() {
        logger.log();
    }

    public final boolean getActive() {
        return active;
    }

    public final void setActive(boolean state) {
        this.active = state;
    }
    //End helper methods.

    //Image methods
    final void loadImages() {
        screen.loadImages();
    }

    final void image(String name, float x, float y) {
        screen.image(name, x, y);
    }

    final void image(String name, float x, float y, float w, float h) {
        screen.image(name, x, y, w, h);
    }

    final void image(String name, float x, float y, float w, float h, boolean flipv) {
        screen.image(name, x, y, w, h, flipv);
    }
    //end Image methods

    //Map methods
    final void loadMap(String map) {
        screen.loadMap(map);
    }

    final void map() {
        screen.map();
    }

    final void map(int x, int y) {
        screen.map(x, y);
    }

    final void map(int x, int y, int layer) {
        screen.map(x, y, layer);
    }

    final void map(int x, int y, int sx, int sy, int w, int h) {
        screen.map(x, y, sx, sy, w, h);
    }

    final void map(int x, int y, int sx, int sy, int w, int h, int layer) {
        screen.map(x, y, sx, sy, w, h, layer);
    }

    final int mapGet(int x, int y) {
        return screen.mapGet(x, y);
    }

    final void mapSet(int x, int y, int id) {
        screen.mapSet(x, y, id);
    }

    final void mapRemove(int x, int y) {
        screen.mapRemove(x, y);
    }

    final int getMapHeight() {
        return screen.getMapHeight();
    }

    final int getMapWidth() {
        return screen.getMapWidth();
    }
    //end Map methods

    //start color methods
    public final void drawColor(int color) {
        screen.drawColor(color);
    }

    public final void drawColor(float r, float gr, float b) {
        screen.drawColor(r, gr, b);
    }

    public final Color getDrawColor(int color) {
        return screen.getDrawColor(color);
    }

    public final void bgColor(int color) {
        screen.bgColor(color);
    }

    public final void bgColor(float r, float gr, float b) {
        screen.bgColor(r, gr, b);
    }

    public final void bgColor(float[] color) {
        screen.bgColor(color);
    }
    //end color methods

    //text methods
    final void text(String text, float x, float y, int color) {
        screen.text(text, x, y, color);
    }

    final void text(String text, float x, float y, float[] color) {
        screen.text(text, x, y, color);
    }

    final void text(String text, float x, float y, float width, int color) {
        screen.text(text, x, y, width, color);
    }

    final void text(String text, float x, float y, float width, float[] color) {
        screen.text(text, x, y, width, color);
    }

    final void text(String text, float x, float y, float width, int align, int color) {
        screen.text(text, x, y, width, align, color);
    }

    final void text(String text, float x, float y, float width, int align, float[] color) {
        screen.text(text, x, y, width, align, color);
    }
    //end text methods

    //start 8x8 sprites
    final void sprite(int id, float x, float y) {
        screen.sprite(id, x, y);
    }

    final void sprite(int id, float x, float y, float degr) {
        screen.sprite(id, x, y, degr);
    }

    final void sprite(int id, float x, float y, boolean clockwise) {
        screen.sprite(id, x, y, clockwise);
    }

    final void sprite(int id, float x, float y, boolean flipX, boolean flipY) {
        screen.sprite(id, x, y, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    final void sprite(int id, float x, float y, int size) {
        screen.sprite(id, x, y, size);
    }

    final void sprite(int id, float x, float y, float degr, int size) {
        screen.sprite(id, x, y, degr, size);
    }

    final void sprite(int id, float x, float y, boolean clockwise, int size) {
        screen.sprite(id, x, y, clockwise, size);
    }

    final void sprite(int id, float x, float y, boolean flipX, boolean flipY, int size) {
        screen.sprite(id, x, y, flipX, flipY, size);
    }
    //end sizable sprites

    //start shape drawing methods
    public void clpx() {
        screen.clpx();
    }

    public void pixel(int x, int y) {
        screen.pixel(x, y);
    }

    public void pixel(int x, int y, int color) {
        screen.pixel(x, y, color);
    }

    public void pixel(int x, int y, float[] c) {
        screen.pixel(x, y, c);
    }

    public final void rect(int x, int y, int w, int h) {
        screen.rect(x, y, w, h);
    }

    public final void rect(int x, int y, int w, int h, boolean fill) {
        screen.rect(x, y, w, h, fill);
    }

    public final void circle(int x, int y, int r) {
        screen.circle(x, y, r);
    }

    public final void circle(int x, int y, int r, boolean fill) {
        screen.circle(x, y, r, fill);
    }

    public final void triangle(int x, int y, int x2, int y2, int x3, int y3) {
        screen.triangle(x, y, x2, y2, x3, y3);
    }

    public final void line(int x1, int y1, int x2, int y2) {
        screen.line(x1, y1, x2, y2);
    }
    //end shape drawing methods

    //start Audio handling
    final void sfx(String name) {
        audioLoader.sound(name);
    }

    final void sfx(String name, float vol, float pit, float pan) {
        audioLoader.sound(name, vol, pit, pan);
    }

    final void music(String name) {
        audioLoader.music(name);
    }

    final void music(String name, boolean loop) {
        audioLoader.music(name, loop);
    }

    final void stopAllMusic() {
        audioLoader.stopMusic();
    }

    final void stopMusic(String fileName) {
        audioLoader.stopMusic(fileName);
    }
    //end audio handling

    //START Math utils
    public float cos(float radians) {
        return MathUtils.cos(radians);
    }

    public float cosDeg(float deg) {
        return MathUtils.cosDeg(deg);
    }

    public float sin(float radians) {
        return MathUtils.sin(radians);
    }

    public float sinDeg(float deg) {
        return MathUtils.sinDeg(deg);
    }

    public float ceil(float value) {
        return MathUtils.ceil(value);
    }

    public float floor(float value) {
        return MathUtils.floor(value);
    }

    public int randInt(int range) {
        return MathUtils.random(range);
    }

    public int randInt(int start, int end) {
        return MathUtils.random(start, end);
    }

    public float randFloat(float range) {
        return MathUtils.random(range);
    }

    public float randFloat(float start, float end) {
        return MathUtils.random(start, end);
    }

    //END Math utils
    //start input handling
    final boolean button(BTN button) {
        return (null != p1ControllerListener) ? p1ControllerListener.button(button) : false;
    }

    final boolean button(BTN button, int player) {
        if (null != p1ControllerListener && player == 0) {
            return p1ControllerListener.button(button);
        }
        if (null != p2ControllerListener && player == 1) {
            return p2ControllerListener.button(button);
        }
        //default search is false, in case there are no controllers.
        return false;
    }

    final boolean getButton(int code) {
        return playerOneController.getButton(code);
    }

    //detect keyboard key presses
    final boolean key(String key) {
        return Gdx.input.isKeyPressed(Keys.valueOf(key));
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            EngineScreen.setBack(true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    //end input handling
    //Experimental API methods
    public void tint(int color) {
        g.setTint(getDrawColor(color));
    }
    //END Experimental
}
