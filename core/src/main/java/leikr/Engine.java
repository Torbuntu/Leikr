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
package leikr;

import java.math.BigDecimal;
import leikr.controls.LeikrController;
import leikr.controls.LeikrKeyboard;
import leikr.controls.LeikrMouse;
import leikr.customProperties.CustomSystemProperties;
import leikr.managers.LeikrAudioManager;
import leikr.managers.LeikrScreenManager;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public abstract class Engine extends BaseGamePadListener implements InputProcessor {

    //Mini2DX specific classes for managing the screen state and drawing.
    FitViewport viewport;

    //used by the Engine Screen to determine if the game should be actively running.
    boolean active;

    /*
     * Controllers and listeners for handling custom controller input
     */
    public LeikrController controllerA;
    public LeikrController controllerB;

    public LeikrMouse mouse;
    public LeikrKeyboard keyboard;

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
     * preCreate gets the audio, screen and system singletons. sets up the
     * controllers if there are any connected.
     *
     * @param mSprites maximum allowed sprites to draw at one time
     * @param sys object used to interact with the Leikr system at runtime
     */
    public final void preCreate(int mSprites, LeikrSystemManager sys) {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        audio = LeikrAudioManager.getLeikrAudioManager();
        screen = LeikrScreenManager.getLeikrScreenManager(mSprites);
        system = sys;
        active = true;
        try {
            if (Mdx.input.getGamePads().size > 0) {
                controllerA = LeikrController.getLeikrControllerListenerA();
                Mdx.input.getGamePads().get(0).addListener(controllerA);
                Mdx.input.getGamePads().get(0).addListener(this);
            }
            if (Mdx.input.getGamePads().size > 1) {
                controllerB = LeikrController.getLeikrControllerListenerB();
                Mdx.input.getGamePads().get(1).addListener(controllerB);
                Mdx.input.getGamePads().get(1).addListener(this);
            }
        } catch (Exception ex) {
            System.out.println("Controllers not active: " + ex.getMessage());
        }
        // input processors
        mouse = LeikrMouse.getLeikrMouse(viewport);
        keyboard = LeikrKeyboard.getLeikrKeyboard();
        Mdx.input.setInputProcessor(this);
    }

    /**
     * Run just before the Engine update method.Used to update system objects
     * behind the scenes. mouse positions updates
     *
     *
     * update screen objects
     *
     * @param delta
     * @return if should continue
     */
    public final boolean preUpdate(float delta) {
        mouse.updateMouse();
        screen.preUpdate(delta);
        return false;
    }

    /**
     * preRender
     *
     * run just before the Engine render method. Used to set up system objects
     * before doing any Engine rendering.
     *
     * Applies viewport and preRenders the screen.
     *
     */
    public final void preRender() {
        viewport.apply(Mdx.graphicsContext);
        screen.preRender();
    }

    /*
     * Override functions for game scripting. These are abstract and are
     * required to be implemented in the game code.
     */
    abstract public void create();

    abstract public void update(float delta);

    abstract public void render();
    // end override functions

    // Optional override methods
    public void onPause() {
    }

    public void onResume() {
    }
    // END Optional override methods.

    //disposes the game objects on exit
    public final void dispose() {
        if (audio != null) {
            audio.dispose();
        }

        if (screen != null) {
            screen.dispose();
        }

        if (Mdx.input.getGamePads().size > 0) {
            Mdx.input.getGamePads().get(0).removeListener(this);
            Mdx.input.getGamePads().get(0).removeListener(controllerA);
        }
        if (Mdx.input.getGamePads().size > 1) {
            Mdx.input.getGamePads().get(1).removeListener(this);
            Mdx.input.getGamePads().get(1).removeListener(controllerB);
        }
        //  Controllers.clearListeners();
    }
    //dispose

    //Start Helper methods
    /**
     * Prints the FPS to the console
     *
     */
    public final void FPS() {
        System.out.println("FPS: " + Mdx.platformUtils.getFramesPerSecond());
    }

    public final void HOST_INFO() {
        System.out.println("Total Mem: " + Mdx.platformUtils.getTotalMemory());
        System.out.println("Free Mem : " + Mdx.platformUtils.getAvailableMemory());
        System.out.println("Used Mem : " + Mdx.platformUtils.getUsedMemory());
        System.out.println("Avg. UPD : " + Mdx.platformUtils.getAverageUpdateDuration());
        System.out.println();
    }

    public long getFrame() {
        return Mdx.graphicsContext.getFrameId();
    }

    public final boolean getActive() {
        return active;
    }

    public final void setActive(boolean state) {
        this.active = state;
    }

    public final void pause() {
        system.pause();
    }

    public final void loadSpriteSheet(String progName) {
        system.loadSpriteSheet(progName);
    }
    //End helper methods.

    //Image methods
    public final void loadImages() {
        screen.loadImages();
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        screen.drawTexture(name, x, y);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        screen.drawTexture(name, x, y, w, h);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        screen.drawTexture(name, x, y, w, h, flipv);
    }
    //end Image methods

    //Map methods
    public final void loadMap(String map) {
        screen.loadMap(map);
    }

    public final void drawMap() {
        screen.drawMap();
    }

    public final void drawMap(BigDecimal x, BigDecimal y) {
        screen.drawMap(x, y);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        screen.drawMap(x, y, layer);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        screen.drawMap(x, y, sx, sy, w, h);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        screen.drawMap(x, y, sx, sy, w, h, layer);
    }

    public final int getMapTileId(BigDecimal x, BigDecimal y) {
        return screen.getMapTileId(x, y);
    }

    public final void setMapTile(BigDecimal x, BigDecimal y, int id) {
        screen.setMapTile(x, y, id);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y) {
        screen.removeMapTile(x, y);
    }

    public final int getMapHeight() {
        return screen.getMapHeight();
    }

    public final int getMapWidth() {
        return screen.getMapWidth();
    }
    //end Map methods

    //start color methods
    public final void setColor(int color) {
        screen.setColor(color);
    }

    public final void setColor(String c) {
        screen.setColor(c);
    }

    public final void setColor(String c, boolean a) {
        screen.setColor(c, a);
    }

    public final Color getDrawColor(int color) {
        return screen.getDrawColor(color);
    }

    public final void bgColor(int color) {
        screen.bgColor(color);
    }

    public final void bgColor(String color) {
        screen.bgColor(color);
    }
    //end color methods

    //Helper methods
    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        screen.setClip(x, y, w, h);
    }

    public final void removeClip() {
        screen.removeClip();
    }

    //end helper methods
    //text methods
    public final void drawString(String text, BigDecimal x, BigDecimal y, int color) {
        screen.drawString(text, x, y, color);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, String color) {
        screen.drawString(text, x, y, color);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int color) {
        screen.drawString(text, x, y, width, color);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, String color) {
        screen.drawString(text, x, y, width, color);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int align, int color) {
        screen.drawString(text, x, y, width, align, color);
    }

    public final void drawString(String text, BigDecimal x, BigDecimal y, BigDecimal width, int align, String color) {
        screen.drawString(text, x, y, width, align, color);
    }
    //end drawString methods

    //start 8x8 sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        screen.sprite(id, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr) {
        screen.sprite(id, x, y, degr);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        screen.sprite(id, x, y, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        screen.sprite(id, x, y, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr, int size) {
        screen.sprite(id, x, y, degr, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        screen.sprite(id, x, y, flipX, flipY, size);
    }
    //end sizable sprites

    //start scaled sprites
    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        screen.spriteSc(id, x, y, scale);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        screen.spriteSc(id, x, y, scaleX, scaleY);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        screen.spriteSc(id, x, y, scaleX, scaleY, degr);
    }
    
    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY){
        screen.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY);
    }
    //end scaled sprites

    //start shape drawing methods
    public final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        screen.drawPixel(color, x, y);
    }

    public final void drawRect(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        screen.drawRect(x, y, w, h);
    }

    public final void fillRect(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        screen.fillRect(x, y, w, h);
    }

    public final void drawLineSegment(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        screen.drawLineSegment(x1, y1, x2, y2);
    }
    //end shape drawing methods

    //start Audio handling
    public final void playSound(String name) {
        audio.playSound(name);
    }

    public final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        audio.playSound(name, vol, pit, pan);
    }

    public final void stopSound() {
        audio.stopSound();
    }

    public final void playMusic(String name) {
        audio.playMusic(name);
    }

    public final void playMusic(String name, boolean loop) {
        audio.playMusic(name, loop);
    }

    public final void stopAllMusic() {
        audio.stopAllMusic();
    }

    public final void stopMusic(String fileName) {
        audio.stopMusic(fileName);
    }

    public final void pauseAudio() {
        audio.pauseAllAudio();
    }
    //end audio handling

    //START Math utils
    public float cos(BigDecimal radians) {
        return system.cos(radians);
    }

    public float cosDeg(BigDecimal deg) {
        return system.cosDeg(deg);
    }

    public float sin(BigDecimal radians) {
        return system.sin(radians);
    }

    public float sinDeg(BigDecimal deg) {
        return system.sinDeg(deg);
    }

    public int ceil(BigDecimal value) {
        return system.ceil(value);
    }

    public int floor(BigDecimal value) {
        return system.floor(value);
    }

    public int randInt(int range) {
        return system.randInt(range);
    }

    public int randInt(int start, int end) {
        return system.randInt(start, end);
    }

    public float randFloat(BigDecimal range) {
        return system.randFloat(range);
    }

    public float randFloat(BigDecimal start, BigDecimal end) {
        return system.randFloat(start, end);
    }

    public int round(BigDecimal number) {
        return system.round(number);
    }
    //END Math utils

    //START input handling
    public final boolean button(String button) {
        return (null != controllerA) ? controllerA.button(button) : false;
    }

    public final boolean button(String button, int player) {
        if (null != controllerA && player == 0) {
            return controllerA.button(button);
        }
        if (null != controllerB && player == 1) {
            return controllerB.button(button);
        }
        //default search is false, in case there are no controllers.
        return false;
    }

    public int horizontalAxis() {
        if (controllerA != null) {
            return CustomSystemProperties.HORIZONTAL_AXIS;
        }
        return 0;
    }

    public int verticalAxis() {
        if (controllerA != null) {
            return CustomSystemProperties.VERTICAL_AXIS;
        }
        return 0;
    }

    //detect keyboard key presses (polling continuously)
    public final boolean key(String key) {
        return keyboard.key(key);
    }

    public final boolean keyUp(String key) {
        return keyboard.keyUp(key);
    }

    //detect single key press.
    public final boolean keyPress(String key) {
        return keyboard.keyPress(key);
    }

    /**
     * Detects a mouse click event.
     *
     * @return
     */
    public final boolean mouseClick() {
        return mouse.mouseClick();
    }

    public final float mouseX() {
        return mouse.mouseX();
    }

    public final float mouseY() {
        return mouse.mouseY();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
        screen.tint(color);
    }

    public void tint() {
        screen.tint();
    }

    public boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal w1, BigDecimal h1, BigDecimal x2, BigDecimal y2, BigDecimal w2, BigDecimal h2) {
        return system.collides(x1, y1, w1, h1, x2, y2, w2, h2);
    }

    public boolean collides(BigDecimal[] a, BigDecimal[] b) {
        return system.collides(a, b);
    }

    public boolean point(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2, BigDecimal w, BigDecimal h) {
        return system.point(x, y, x2, y2, w, h);
    }

    public Object compile(String path) {
        return system.compile(path);
    }

    public void compile(String path, String out) {
        system.compile(path, out);
    }

    public Object eval(String code) {
        return system.eval(code);
    }

    public Object parse(String code){
        return system.parse(code);
    }
    
    public void loadLib(String path) {
        system.loadLib(path);
    }

    public Object newInstance(String name) {
        return system.newInstance(name);
    }
    //END Experimental
}
