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
import leikr.managers.LeikrAudioManager;
import leikr.managers.LeikrScreenManager;
import leikr.managers.LeikrSystemManager;
import leikr.screens.EngineScreen;
import org.mini2Dx.core.graphics.Animation;
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

    public enum BTN {
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

    /*
     * Controllers and listeners for handling custom controller input
     */
    LeikrControllerListener p1ControllerListener;
    LeikrControllerListener p2ControllerListener;
    Controller playerOneController;
    Controller playerTwoController;

    /*
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    public LeikrScreenManager screen;
    public LeikrSystemManager system;
    public LeikrAudioManager audio;

    //custom prop functions
    public int getUsedSprites() {
        return screen.getUsedSprites();
    }
    //end custom prop functions

    
    /**
     * preCreate
     * gets the audio, screen and system singletons. 
     * sets up the controllers if there are any connected.
     * 
     * @param mSprites maximum allowed sprites to draw at one time
     * @param system object used to interact with the Leikr system at runtime
     */
    public final void preCreate(int mSprites, LeikrSystemManager system) {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        logger = new FPSLogger();
        audio = LeikrAudioManager.getLeikrAudioManager();
        screen = LeikrScreenManager.getLeikrScreenManager(mSprites);
        this.system = system;
        active = true;
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                p1ControllerListener = LeikrControllerListener.getLeikrControllerListenerA();
                Controllers.getControllers().get(0).addListener(p1ControllerListener);

            }
            if (Controllers.getControllers().size > 1) {
                p2ControllerListener = LeikrControllerListener.getLeikrControllerListenerB();
                Controllers.getControllers().get(1).addListener(p2ControllerListener);
            }
        } catch (Exception ex) {
            System.out.println("Controllers not active: " + ex.getMessage());
        }
    }

    /**
     * preUpdate
     * 
     * run just before the Engine update method. Used to update system objects
     * behind the scenes.
     * 
     * @param delta 
     */
    public final void preUpdate(float delta) {
        screen.preUpdate(delta);
    }

    /**
     * preRender
     * 
     * run just before the Engine render method. Used to set up system objects
     * before doing any Engine rendering. 
     * 
     * Applies viewport and preRenders the screen.
     * 
     * @param g 
     */
    public final void preRender(Graphics g) {
        this.g = g;
        viewport.apply(this.g);
        screen.preRender(this.g);
    }

    /*
     * Override functions for game scripting. These are abstract and are
     * required to be implemented in the game code.
     */
    abstract public void create();

    abstract public void update(float delta);

    abstract public void render();
    // end override functions

    //disposes the game objects on exit
    public final void dispose() {
        audio.dispose();
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
    public final void loadImages() {
        screen.loadImages();
    }

    public final void image(String name, float x, float y) {
        screen.image(name, x, y);
    }

    public final void image(String name, float x, float y, float w, float h) {
        screen.image(name, x, y, w, h);
    }

    public final void image(String name, float x, float y, float w, float h, boolean flipv) {
        screen.image(name, x, y, w, h, flipv);
    }
    //end Image methods

    //Map methods
    public final void loadMap(String map) {
        screen.loadMap(map);
    }

    public final void map() {
        screen.map();
    }

    public final void map(int x, int y) {
        screen.map(x, y);
    }

    public final void map(int x, int y, int layer) {
        screen.map(x, y, layer);
    }

    public final void map(int x, int y, int sx, int sy, int w, int h) {
        screen.map(x, y, sx, sy, w, h);
    }

    public final void map(int x, int y, int sx, int sy, int w, int h, int layer) {
        screen.map(x, y, sx, sy, w, h, layer);
    }

    public final int mapGet(int x, int y) {
        return screen.mapGet(x, y);
    }

    public final void mapSet(int x, int y, int id) {
        screen.mapSet(x, y, id);
    }

    public final void mapRemove(int x, int y) {
        screen.mapRemove(x, y);
    }

    public final int getMapHeight() {
        return screen.getMapHeight();
    }

    public final int getMapWidth() {
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
    public final void text(String text, float x, float y, int color) {
        screen.text(text, x, y, color);
    }

    public final void text(String text, float x, float y, float[] color) {
        screen.text(text, x, y, color);
    }

    public final void text(String text, float x, float y, float width, int color) {
        screen.text(text, x, y, width, color);
    }

    public final void text(String text, float x, float y, float width, float[] color) {
        screen.text(text, x, y, width, color);
    }

    public final void text(String text, float x, float y, float width, int align, int color) {
        screen.text(text, x, y, width, align, color);
    }

    public final void text(String text, float x, float y, float width, int align, float[] color) {
        screen.text(text, x, y, width, align, color);
    }
    //end text methods

    //start 8x8 sprites
    public final void sprite(int id, float x, float y) {
        screen.sprite(id, x, y);
    }

    public final void sprite(int id, float x, float y, float degr) {
        screen.sprite(id, x, y, degr);
    }

    public final void sprite(int id, float x, float y, boolean flipX, boolean flipY) {
        screen.sprite(id, x, y, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, float x, float y, int size) {
        screen.sprite(id, x, y, size);
    }

    public final void sprite(int id, float x, float y, float degr, int size) {
        screen.sprite(id, x, y, degr, size);
    }

    public final void sprite(int id, float x, float y, boolean flipX, boolean flipY, int size) {
        screen.sprite(id, x, y, flipX, flipY, size);
    }
    //end sizable sprites

    //start scaled sprites
    public void spriteSc(int id, float x, float y, float scale) {
        screen.spriteSc(id, x, y, scale);
    }

    public void spriteSc(int id, float x, float y, float scaleX, float scaleY) {
        screen.spriteSc(id, x, y, scaleX, scaleY);
    }

    public void spriteSc(int id, float x, float y, float scaleX, float scaleY, float degr) {
        screen.spriteSc(id, x, y, scaleX, scaleY, degr);
    }
    //end scaled sprites

    //start animated sprites
    public Animation makeAnimSprite(int[] ids, float[] lengths) {
        return screen.makeAnimSprite(ids, lengths);
    }

    public Animation makeAnimSprite(int[] ids, float[] lengths, boolean loop) {
        return screen.makeAnimSprite(ids, lengths, loop);
    }

    public Animation makeAnimSprite(int[] ids, float[] lengths, int size) {
        return screen.makeAnimSprite(ids, lengths, size);
    }

    public Animation makeAnimSprite(int[] ids, float[] lengths, int size, boolean loop) {
        return screen.makeAnimSprite(ids, lengths, size, loop);
    }

    public void spriteAnim(Animation sprite, float x, float y) {
        screen.spriteAnim(sprite, x, y);
    }

    public void spriteAnim(Animation sprite, float x, float y, boolean flipH, boolean flipV) {
        screen.spriteAnim(sprite, x, y, flipH, flipV);
    }
    //END animated sprites

    //start shape drawing methods
    public void pixel(int color, int x, int y) {
        screen.pixel(color, x, y);
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
    public final void sfx(String name) {
        audio.sfx(name);
    }

    public final void sfx(String name, float vol, float pit, float pan) {
        audio.sfx(name, vol, pit, pan);
    }

    public final void music(String name) {
        audio.music(name);
    }

    public final void music(String name, boolean loop) {
        audio.music(name, loop);
    }

    public final void stopAllMusic() {
        audio.stopAllMusic();
    }

    public final void stopMusic(String fileName) {
        audio.stopMusic(fileName);
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

    public int ceil(float value) {
        return MathUtils.ceil(value);
    }

    public int floor(float value) {
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
    public final boolean button(BTN button) {
        return (null != p1ControllerListener) ? p1ControllerListener.button(button) : false;
    }

    public final boolean button(BTN button, int player) {
        if (null != p1ControllerListener && player == 0) {
            return p1ControllerListener.button(button);
        }
        if (null != p2ControllerListener && player == 1) {
            return p2ControllerListener.button(button);
        }
        //default search is false, in case there are no controllers.
        return false;
    }

    public final boolean getButton(int code) {
        return playerOneController.getButton(code);
    }

    //detect keyboard key presses
    public final boolean key(String key) {
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

    //START System api
    public void loadProgram(String name) {
        system.loadProgram(name);
    }

    public void setFont(String path, int spacing, int width, int height) {
        system.setCustomFont(path, spacing, width, height);
    }
    //END System API

    //Experimental API methods
    public void tint(int color) {
        g.setTint(getDrawColor(color));
    }

    public final void clip(float x, float y, float w, float h) {
        screen.clip(x, y, w, h);
    }

    public final void clip() {
        screen.clip();
    }

    //END Experimental
}
