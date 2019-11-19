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
import leikr.managers.LeikrAudioManager;
import leikr.managers.LeikrScreenManager;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public abstract class Engine extends BaseGamePadListener implements InputProcessor {

    //Mini2DX specific classes for managing the Screen state and drawing.
    FitViewport viewport;

    //used by the Engine Screen to determine if the game should be actively running.
    boolean active;

    /*
     * Controllers and listeners for handling custom controller input
     */
    public LeikrController lControllerA;
    public LeikrController lControllerB;

    public LeikrMouse lMouse;
    public LeikrKeyboard lKeyboard;

    /*
     * Loaders
     *
     * The loaders are used to load the custom assets for a game at startup.
     */
    public LeikrScreenManager lScreen;
    public LeikrSystemManager lSystem;
    public LeikrAudioManager lAudio;

    /**
     * preCreate gets the audio, screen and system singletons.sets up the
     * controllers if there are any connected.
     *
     * @param mSprites maximum allowed sprites to draw at one time
     * @param sys object used to interact with the Leikr lSystem at runtime
     * @param viewport
     */
    public final void preCreate(int mSprites, LeikrSystemManager sys, FitViewport viewport) {
        this.viewport = viewport;
        lAudio = LeikrAudioManager.getLeikrAudioManager();
        lScreen = LeikrScreenManager.getLeikrScreenManager(mSprites);
        lSystem = sys;
        active = true;
        try {
            if (Mdx.input.getGamePads().size > 0) {
                lControllerA = LeikrController.getLeikrControllerListenerA();
                Mdx.input.getGamePads().get(0).addListener(lControllerA);
                Mdx.input.getGamePads().get(0).addListener(this);
            }
            if (Mdx.input.getGamePads().size > 1) {
                lControllerB = LeikrController.getLeikrControllerListenerB();
                Mdx.input.getGamePads().get(1).addListener(lControllerB);
                Mdx.input.getGamePads().get(1).addListener(this);
            }
        } catch (Exception ex) {
            System.out.println("Controllers not active: " + ex.getMessage());
        }
        // input processors
        lMouse = LeikrMouse.getLeikrMouse(viewport);
        lKeyboard = LeikrKeyboard.getLeikrKeyboard();
        Mdx.input.setInputProcessor(this);
    }

    /**
     * Run just before the Engine update method.Used to update system objects
     * behind the scenes. lMouse positions updates
     *
     *
     * update screen objects
     *
     * @param delta
     */
    public final void preUpdate(float delta) {
        lMouse.updateMouse();
        lScreen.preUpdate(delta);
    }

    /**
     * preRender
     *
     * run just before the Engine render method.Used to set up system objects
 before doing any Engine rendering.Applies viewport and preRenders the screen.
     *
     * @param g
     * @param v
     */
    public final void preRender(Graphics g, Viewport v) {
        lScreen.preRender(g, v);
    }

    /*
     * Override functions for game scripting. 
     */
    public void create() {
    }

    public void update(float delta) {
    }

    public void render() {
    }

    // end override functions
    // Optional override methods
    public void onPause() {
    }

    public void onResume() {
    }
    // END Optional override methods.

    //disposes the game objects on exit
    public final void dispose() {
        if (lAudio != null) {
            lAudio.dispose();
        }

        if (lScreen != null) {
            lScreen.dispose();
        }

        if (Mdx.input.getGamePads().size > 0) {
            Mdx.input.getGamePads().get(0).removeListener(this);
            Mdx.input.getGamePads().get(0).removeListener(lControllerA);
        }
        if (Mdx.input.getGamePads().size > 1) {
            Mdx.input.getGamePads().get(1).removeListener(this);
            Mdx.input.getGamePads().get(1).removeListener(lControllerB);
        }
        //  Controllers.clearListeners();
    }
    //dispose

    //Start Helper methods
    //custom prop functions
    public int getUsedSprites() {
        return lScreen.getUsedSprites();
    }
    //end custom prop functions

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

    public final boolean getActive() {
        return active;
    }

    public final void setActive(boolean state) {
        this.active = state;
    }

    public final void pause() {
        lSystem.pause();
    }

    public final void loadSpriteSheet(String progName) {
        lSystem.loadSpriteSheet(progName);
    }
    //End helper methods.

    //Image methods
    public final void loadImages() {
        lScreen.loadImages();
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        lScreen.drawTexture(name, x, y);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lScreen.drawTexture(name, x, y, w, h);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        lScreen.drawTexture(name, x, y, w, h, flipv);
    }

    //end Image methods
    //Map methods
    public final void loadMap(String map) {
        lScreen.loadMap(map);
    }

    public final void drawMap() {
        lScreen.drawMap();
    }

    public final void drawMap(BigDecimal x, BigDecimal y) {
        lScreen.drawMap(x, y);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        lScreen.drawMap(x, y, layer);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        lScreen.drawMap(x, y, sx, sy, w, h);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        lScreen.drawMap(x, y, sx, sy, w, h, layer);
    }

    public final int getMapTileId(BigDecimal x, BigDecimal y) {
        return lScreen.getMapTileId(x, y);
    }

    public final void setMapTile(BigDecimal x, BigDecimal y, int id) {
        lScreen.setMapTile(x, y, id);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y) {
        lScreen.removeMapTile(x, y);
    }

    public final int getMapHeight() {
        return lScreen.getMapHeight();
    }

    public final int getMapWidth() {
        return lScreen.getMapWidth();
    }
    //end Map methods

    //start color methods
    public final void bgColor(int color) {
        lScreen.bgColor(color);
    }

    public final void bgColor(String color) {
        lScreen.bgColor(color);
    }
    //end color methods

    //Helper methods
    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lScreen.setClip(x, y, w, h);
    }

    public final void removeClip() {
        lScreen.removeClip();
    }

    //end helper methods
    //text methods
    public final void drawString(int color, String text, BigDecimal x, BigDecimal y) {
        lScreen.drawString(color, text, x, y);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lScreen.drawString(color, text, x, y, width);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lScreen.drawString(color, text, x, y, width, align);
    }
    
    public final void drawString(String color, String text, BigDecimal x, BigDecimal y) {
        lScreen.drawString(color, text, x, y);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lScreen.drawString(color, text, x, y, width);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lScreen.drawString(color, text, x, y, width, align);
    }
    //end drawString methods

    //start 8x8 sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        lScreen.sprite(id, x, y);
    }

    public final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y) {
        lScreen.sprite(id, degr, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        lScreen.sprite(id, x, y, flipX, flipY);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        lScreen.sprite(id, x, y, size);
    }

    public final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y, int size) {
        lScreen.sprite(id, degr, x, y, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        lScreen.sprite(id, x, y, flipX, flipY, size);
    }
    //end sizable sprites

    //start scaled sprites
    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        lScreen.spriteSc(id, x, y, scale);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        lScreen.spriteSc(id, x, y, scaleX, scaleY);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        lScreen.spriteSc(id, x, y, scaleX, scaleY, degr);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY) {
        lScreen.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY);
    }
    //end scaled sprites

    //start shape drawing methods
    public final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        lScreen.drawPixel(color, x, y);
    }

    public final void drawRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lScreen.drawRect(color, x, y, w, h);
    }

    public final void fillRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lScreen.fillRect(color, x, y, w, h);
    }

    public final void drawLineSegment(int color, BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        lScreen.drawLineSegment(color, x1, y1, x2, y2);
    }
    //end shape drawing methods

    //start Audio handling
    public final void playSound(String name) {
        lAudio.playSound(name);
    }

    public final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        lAudio.playSound(name, vol, pit, pan);
    }

    public final void stopSound() {
        lAudio.stopSound();
    }

    public final void playMusic(String name) {
        lAudio.playMusic(name);
    }

    public final void playMusic(String name, boolean loop) {
        lAudio.playMusic(name, loop);
    }

    public final void stopAllMusic() {
        lAudio.stopAllMusic();
    }

    public final void stopMusic(String fileName) {
        lAudio.stopMusic(fileName);
    }

    public final void pauseAudio() {
        lAudio.pauseAllAudio();
    }
    //end Audio handling

    //START Math utils
    public float cos(BigDecimal radians) {
        return lSystem.cos(radians);
    }

    public float cosDeg(BigDecimal deg) {
        return lSystem.cosDeg(deg);
    }

    public float sin(BigDecimal radians) {
        return lSystem.sin(radians);
    }

    public float sinDeg(BigDecimal deg) {
        return lSystem.sinDeg(deg);
    }

    public int ceil(BigDecimal value) {
        return lSystem.ceil(value);
    }

    public int floor(BigDecimal value) {
        return lSystem.floor(value);
    }

    public int randInt(int range) {
        return lSystem.randInt(range);
    }

    public int randInt(int start, int end) {
        return lSystem.randInt(start, end);
    }

    public float randFloat(BigDecimal range) {
        return lSystem.randFloat(range);
    }

    public float randFloat(BigDecimal start, BigDecimal end) {
        return lSystem.randFloat(start, end);
    }

    public int round(BigDecimal number) {
        return lSystem.round(number);
    }
    //END Math utils

    //START input handling
    public final boolean button(String button) {
        return (null != lControllerA) ? lControllerA.button(button) : false;
    }

    public final boolean button(String button, int player) {
        if (null != lControllerA && player == 0) {
            return lControllerA.button(button);
        }
        if (null != lControllerB && player == 1) {
            return lControllerB.button(button);
        }
        //default search is false, in case there are no controllers.
        return false;
    }

    //detect Keyboard key presses (polling continuously)
    public final boolean key(String key) {
        return lKeyboard.key(key);
    }

    public final boolean keyUp(String key) {
        return lKeyboard.keyUp(key);
    }

    //detect single key press.
    public final boolean keyPress(String key) {
        return lKeyboard.keyPress(key);
    }

    /**
     * Detects a Mouse click event.
     *
     * @return
     */
    public final boolean mouseClick() {
        return lMouse.mouseClick();
    }

    public final float mouseX() {
        return lMouse.mouseX();
    }

    public final float mouseY() {
        return lMouse.mouseY();
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
        lSystem.loadProgram(name);
    }

    public void setFont(String path, int spacing, int width, int height) {
        lSystem.setCustomFont(path, spacing, width, height);
    }
    //END System API

    //Experimental API methods
    public void tint(int color) {
        lScreen.tint(color);
    }

    public void tint() {
        lScreen.tint();
    }
    
    public int getPixel(BigDecimal x, BigDecimal y){
        return lScreen.getPixel(x, y);
    }
    
    public void drawPixel(String color, BigDecimal x, BigDecimal y){
        lScreen.drawPixel(color, x, y);
    }
    
    public void drawRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h){
        lScreen.drawRect(color, x, y, w, h);
    }
    
    public void drawLineSegment(String color, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1){
        lScreen.drawLineSegment(color, x0, y0, x1, y1);
    }

    public boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal w1, BigDecimal h1, BigDecimal x2, BigDecimal y2, BigDecimal w2, BigDecimal h2) {
        return lSystem.collides(x1, y1, w1, h1, x2, y2, w2, h2);
    }

    public boolean collides(BigDecimal[] a, BigDecimal[] b) {
        return lSystem.collides(a, b);
    }

    public boolean point(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2, BigDecimal w, BigDecimal h) {
        return lSystem.point(x, y, x2, y2, w, h);
    }

    public Object compile(String path) {
        return lSystem.compile(path);
    }

    public void compile(String path, String out) {
        lSystem.compile(path, out);
    }

    public Object eval(String code) {
        return lSystem.eval(code);
    }

    public Object parse(String code) {
        return lSystem.parse(code);
    }

    public void loadLib(String path) {
        lSystem.loadLib(path);
    }

    public Object newInstance(String name) {
        return lSystem.newInstance(name);
    }
    //END Experimental
}
