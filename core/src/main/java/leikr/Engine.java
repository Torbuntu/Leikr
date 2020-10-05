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

import leikr.managers.ManagerDTO;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.controls.LeikrController;
import leikr.controls.LeikrKeyboard;
import leikr.controls.LeikrMouse;
import leikr.managers.AudioManager;
import leikr.managers.DataManager;
import leikr.managers.GraphicsManager;
import leikr.managers.SystemManager;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.viewport.StretchViewport;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public abstract class Engine extends ControllerAdapter implements InputProcessor {

    //used by the Engine Screen to determine if the game should be actively running.
    private boolean active;

    /*
     * Controllers and listeners for handling custom controller input
     */
    public LeikrController lControllerA;
    public LeikrController lControllerB;
    public LeikrKeyboard lKeyboard;
    public LeikrMouse lMouse;

    /*
     * Managers
     *
     * The managers are used to load the custom assets for a game at startup.
     */
    public GraphicsManager lGraphics;
    public SystemManager lSystem;
    public AudioManager lAudio;
    public DataManager lData;

    // <editor-fold desc="Pre game loop methods" defaultstate="collapsed"> 
    /**
     * preCreate sets the audio, graphics, data, and system objects and sets up
     * the controllers if there are any connected.
     *
     * @param path the path to the game assets
     * @param maxSprites maximum allowed sprites to draw at one time
     * @param managerDTO DTO with the manager objects
     * @param viewport
     * @param framebuffer
     */
    public final void preCreate(String path, int maxSprites, ManagerDTO managerDTO, StretchViewport viewport, FrameBuffer framebuffer) {
        // set managers
        lAudio = managerDTO.getAudioManager();
        lGraphics = managerDTO.getGraphicsManager();
        lData = managerDTO.getDataManager();
        lSystem = managerDTO.getSystemManager();

        // reset the settings to apply to new game assets
        lAudio.resetAudioManager(path);
        lGraphics.resetScreenManager(path, maxSprites);
        lGraphics.preCreate(framebuffer, viewport);

        // set the input processors
        try {
            lControllerA = managerDTO.getInputManager().getControllerA();
            lControllerB = managerDTO.getInputManager().getControllerB();
            if (Controllers.getControllers().size < 1) {
                throw new RuntimeException("No controllers, continue as Keyboard + Mouse");
            }

            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(lControllerA);
            }
            if (Controllers.getControllers().size > 1) {
                Controllers.getControllers().get(1).addListener(lControllerB);
            }
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.WARNING, "Controllers not active: {0}", ex.getMessage());
        }
        lMouse = managerDTO.getInputManager().getMouse();
        lMouse.setViewport(viewport);
        lKeyboard = managerDTO.getInputManager().getKeyboard();

        try {
            Mdx.input.setInputProcessor(this);
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, "Input processor not set: {0}", ex.getMessage());
        }

        active = true;
    }

    /**
     * Run just before the Engine update method. Used to update system objects
     * behind the scenes.
     *
     * update screen objects.
     *
     * @param delta
     */
    public final void preUpdate(float delta) {
        lGraphics.preUpdate(delta);
    }

    /**
     * Run just before the Engine render method.
     *
     * Used to set up system objects before doing any Engine rendering. Applies
     * the viewports and preRenders the screen.
     *
     * @param graphics
     */
    public final void preRender(Graphics graphics) {
        lSystem.preRender(graphics);
        lGraphics.preRender(graphics);
    }
    // </editor-fold>

    // <editor-fold desc="Override functions for game code" defaultstate="collapsed">
    public void create() {
    }

    public void create(String[] args) {
    }

    public void update() {
    }

    public void update(float delta) {
    }

    public void render() {
    }

    public void onPause() {
    }

    public void onResume() {
    }
    // </editor-fold>

    // <editor-fold desc="Dispose" defaultstate="collapsed">
    public final void dispose() {
        if (lAudio != null) {
            lAudio.dispose();
        }

        if (lGraphics != null) {
            lGraphics.dispose();
        }

        //Debugging for ARM-GameShell
        try {
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).removeListener(lControllerA);
            }
            if (Controllers.getControllers().size > 1) {
                Controllers.getControllers().get(1).removeListener(lControllerB);
            }
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.WARNING, null, ex);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Helper and System api" defaultstate="collapsed">
    /**
     * Returns the FPS as an int
     *
     * @return the FPS as an int
     */
    public final int FPS() {
        return Mdx.platformUtils.getFramesPerSecond();
    }

    public int getUsedSprites() {
        return lGraphics.getUsedSprites();
    }

    public final boolean getActive() {
        return active;
    }

    public final void setActive(boolean state) {
        this.active = state;
    }

    public final void loadSpriteSheet(String progName) {
        lSystem.loadSpriteSheet(progName);
    }

    public void loadProgram(String name) {
        lSystem.loadProgram(name);
    }

    public void setFont(String path, int spacing, int width, int height) {
        lSystem.setCustomFont(path, spacing, width, height);
    }

    public String getProgramPath() {
        return lSystem.getProgramPath();
    }

    public String getDataPath() {
        return lSystem.getDataPath();
    }
    // </editor-fold>

    // <editor-fold desc="Texture api" defaultstate="collapsed"> 
    public final void loadImages() {
        lGraphics.loadImages();
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        lGraphics.drawTexture(name, x, y);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawTexture(name, x, y, w, h);
    }

    public final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        lGraphics.drawTexture(name, x, y, w, h, flipv);
    }
    // </editor-fold>

    // <editor-fold desc="Map api" defaultstate="collapsed">
    public final void loadMap(String map) {
        lGraphics.loadMap(map);
    }

    public final void drawMap() {
        lGraphics.drawMap();
    }

    public final void drawMap(BigDecimal x, BigDecimal y) {
        lGraphics.drawMap(x, y);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        lGraphics.drawMap(x, y, layer);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        lGraphics.drawMap(x, y, sx, sy, w, h);
    }

    public final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        lGraphics.drawMap(x, y, sx, sy, w, h, layer);
    }

    public final int getMapTile(BigDecimal x, BigDecimal y) {
        return lGraphics.getMapTile(x, y);
    }

    public final int getMapTile(BigDecimal x, BigDecimal y, int layer) {
        return lGraphics.getMapTile(x, y, layer);
    }

    public final void setMapTile(int id, BigDecimal x, BigDecimal y) {
        lGraphics.setMapTile(id, x, y);
    }

    public final void setMapTile(int id, BigDecimal x, BigDecimal y, int layer) {
        lGraphics.setMapTile(id, x, y, layer);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y) {
        lGraphics.removeMapTile(x, y);
    }

    public final void removeMapTile(BigDecimal x, BigDecimal y, int layer) {
        lGraphics.removeMapTile(x, y, layer);
    }

    public final int getMapHeight() {
        return lGraphics.getMapHeight();
    }

    public final int getMapWidth() {
        return lGraphics.getMapWidth();
    }
    // </editor-fold>

    // <editor-fold desc="Color api" defaultstate="collapsed">
    public final void bgColor(Color color) {
        lGraphics.bgColor(color);
    }

    public final void bgColor(int color) {
        lGraphics.bgColor(color);
    }

    public final void bgColor(String color) {
        lGraphics.bgColor(color);
    }

    public final Color getColor(int color) {
        return lGraphics.getColor(color);
    }

    public final Color getColor(String color) {
        return lGraphics.getColor(color);
    }
    // </editor-fold>

    // <editor-fold desc="Graphics helper api" defaultstate="collapsed">
    public final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.setClip(x, y, w, h);
    }

    public final void removeClip() {
        lGraphics.removeClip();
    }
    // </editor-fold>

    // <editor-fold desc="String api" defaultstate="collapsed"> 
    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y);
    }

    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width);
    }

    public final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width);
    }

    public final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width);
    }

    public final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align);
    }
    // </editor-fold>

    // <editor-fold desc="Sprite api" defaultstate="collapsed"> 
    public final void sprite(int id, BigDecimal x, BigDecimal y) {
        lGraphics.sprite(id, x, y);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr) {
        lGraphics.sprite(id, x, y, degr);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        lGraphics.sprite(id, x, y, flipX, flipY);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr) {
        lGraphics.sprite(id, x, y, flipX, flipY, degr);
    }
    //end 8x8 sprites

    //start sizable sprites
    public final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        lGraphics.sprite(id, x, y, size);
    }

    public final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y, int size) {
        lGraphics.sprite(id, degr, x, y, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        lGraphics.sprite(id, x, y, flipX, flipY, size);
    }

    public final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        lGraphics.sprite(id, x, y, flipX, flipY, degr, size);
    }
    //end sizable sprites

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph) {
        lGraphics.sprite(ids, px, py, pw, ph);
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        lGraphics.sprite(ids, px, py, pw, ph, size);
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY) {
        lGraphics.sprite(ids, px, py, pw, ph, flipX, flipY);
    }

    public final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY, int size) {
        lGraphics.sprite(ids, px, py, pw, ph, flipX, flipY, size);
    }

    //start scaled sprites
    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        lGraphics.spriteSc(id, x, y, scale);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, degr);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, degr);
    }

    //Scaled sprites with size
    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale, int size) {
        lGraphics.spriteSc(id, x, y, scale, size);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, degr, size);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, size);
    }

    public void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, degr, size);
    }
    // </editor-fold>

    // <editor-fold desc="Shape drawing api" defaultstate="collapsed"> 
    public final void drawPixel(Color color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y);
    }

    public final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y);
    }

    public final void drawPixel(String color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y);
    }

    public final void drawRect(Color color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h);
    }

    public final void drawRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h);
    }

    public final void drawRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h);
    }

    public final void fillRect(Color color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h);
    }

    public final void fillRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h);
    }

    public final void fillRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h);
    }

    public final void drawLineSegment(Color color, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1);
    }

    public final void drawLineSegment(int color, BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        lGraphics.drawLineSegment(color, x1, y1, x2, y2);
    }

    public final void drawLineSegment(String color, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1);
    }

    public final void drawCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r);
    }

    public final void drawCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r);
    }

    public final void drawCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r);
    }

    public final void fillCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r);
    }

    public final void fillCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r);
    }

    public final void fillCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r);
    }

    public final void tint(Color color) {
        lGraphics.tint(color);
    }

    public final void tint(int color) {
        lGraphics.tint(color);
    }

    public final void tint(String color) {
        lGraphics.tint(color);
    }

    public final void tint() {
        lGraphics.tint();
    }

    public Color getPixel(BigDecimal x, BigDecimal y) {
        return lGraphics.getPixel(x, y);
    }

    public Color getPixel(String name, BigDecimal x, BigDecimal y) {
        return lGraphics.getPixel(name, x, y);
    }

    public ArrayList<Color> getPixels(String name) {
        return lGraphics.getPixels(name);
    }
    // </editor-fold>

    // <editor-fold desc="Audio api" defaultstate="collapsed">
    public final Sound getSound(String fileName) {
        return lAudio.getSound(fileName);
    }

    public final void playSound(String name) {
        lAudio.playSound(name);
    }

    public final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        lAudio.playSound(name, vol, pit, pan);
    }

    public final void stopSound() {
        lAudio.stopSound();
    }

    public final void stopSound(String fileName) {
        lAudio.stopSound(fileName);
    }

    public final Music getMusic(String fileName) {
        return lAudio.getMusic(fileName);
    }

    public final void playMusic(String name) {
        lAudio.playMusic(name);
    }

    public final void playMusic(String name, boolean loop) {
        lAudio.playMusic(name, loop);
    }

    public final void stopMusic() {
        lAudio.stopMusic();
    }

    public final void stopMusic(String fileName) {
        lAudio.stopMusic(fileName);
    }

    public final void pauseAudio() {
        lAudio.pauseAudio();
    }

    public final void resumeAudio() {
        lAudio.resumeAudio();
    }
    // </editor-fold>

    // <editor-fold desc="Math api" defaultstate="collapsed">
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
    // </editor-fold>

    // <editor-fold desc="Input api" defaultstate="collapsed"> 
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
    // </editor-fold>

    // <editor-fold desc="Data api" defaultstate="collapsed"> 
    public void addData(String key, Object value) {
        lData.addData(key, value);
    }

    public void deleteData(String key) {
        lData.deleteData(key);
    }

    public void clearData() {
        lData.clearData();
    }

    public void saveData(String path) {
        lData.saveData(path);
    }

    public void saveData(String path, HashMap<String, Object> dat) {
        lData.saveData(path, dat);
    }

    public HashMap<String, Object> readData(String path) {
        return lData.readData(path);
    }
    // </editor-fold>

    // <editor-fold desc="Experimental" defaultstate="collapsed"> 
    public void pause() {
        lSystem.pause();
    }

    public void pause(boolean shouldPause) {
        lSystem.pause(shouldPause);
    }

    public boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal r1, BigDecimal x2, BigDecimal y2, BigDecimal r2) {
        return lSystem.collides(x1, y1, r1, x2, y2, r2);
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
    // </editor-fold>
}
