/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package leikr

import java.math.BigDecimal
import java.util.ArrayList
import java.util.HashMap
import java.util.logging.Level
import java.util.logging.Logger
import leikr.controls.*
import leikr.managers.AudioManager
import leikr.managers.DataManager
import leikr.managers.GraphicsManager
import leikr.managers.ManagerDTO
import leikr.managers.SystemManager
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.audio.Music
import org.mini2Dx.core.audio.Sound
import org.mini2Dx.core.graphics.Color
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.gdx.InputProcessor

/**
 *
 * @author tor
 */
abstract class Engine implements InputProcessor {

    /**
     * Used by {@link leikr.screens.EngineScreen} to determine if the game
     * should be actively running.
     */
    private boolean active

    private LeikrKeyboard lKeyboard
    private LeikrMouse lMouse

    /**
     * Managers are used to load the custom assets for a game at startup.
     */
    private GraphicsManager lGraphics
    private SystemManager lSystem
    private AudioManager lAudio
    private DataManager lData

    // <editor-fold desc="Field getters" defaultstate="collapsed">

    /**
     *
     * @return lKeyboard
     */
    LeikrKeyboard getKeyboard() {
        return lKeyboard
    }

    /**
     * @return lMouse
     */
    LeikrMouse getMouse() {
        return lMouse
    }

    /**
     *
     * @return lGraphics
     */
    GraphicsManager getGraphics() {
        return lGraphics
    }

    /**
     *
     * @return lSystem
     */
    SystemManager getSystem() {
        return lSystem
    }

    /**
     *
     * @return lAudio
     */
    AudioManager getAudio() {
        return lAudio
    }

    /**
     *
     * @return lData
     */
    DataManager getData() {
        return lData
    }
    // </editor-fold>

    // <editor-fold desc="Pre game loop methods" defaultstate="collapsed"> 
    /**
     * preCreate sets the audio, graphics, data, and system objects
     *
     * @param path the path to the game assets
     * @param maxSprites maximum allowed sprites to draw at one time
     * @param managerDTO DTO with the manager objects
     * @param viewport
     * @param framebuffer
     */
    final void preCreate(String path, int maxSprites, ManagerDTO managerDTO, StretchViewport viewport, FrameBuffer framebuffer) {
        // set managers
        lAudio = managerDTO.audioManager
        lGraphics = managerDTO.graphicsManager
        lData = managerDTO.dataManager
        lSystem = managerDTO.systemManager

        // reset the settings to apply to new game assets
        lAudio.resetAudioManager(path)
        lData.resetData(path)
        lGraphics.resetScreenManager(path, maxSprites)
        lGraphics.preCreate(framebuffer, viewport)

        lMouse = managerDTO.inputManager.getMouse()
        lMouse.setViewport(viewport)
        lKeyboard = managerDTO.inputManager.getKeyboard()

        try {
            Mdx.input.setInputProcessor(this)
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, "Input processor not set: {0}", ex.getMessage())
        }

        active = true
    }

    /**
     * Run just before the Engine update method. Used to update system objects
     * behind the scenes.
     *
     * update screen objects.
     *
     * @param delta
     */
    final void preUpdate(float delta) {
        lGraphics.preUpdate(delta)
    }

    /**
     * Run just before the Engine render method.
     *
     * Used to set up system objects before doing any Engine rendering. Applies
     * the viewports and preRenders the screen.
     *
     * @param graphics
     */
    final void preRender(Graphics graphics) {
        lSystem.preRender(graphics)
        lGraphics.preRender(graphics)
    }
    // </editor-fold>

    // <editor-fold desc="Override functions for game code" defaultstate="collapsed">
    /**
     * Default methods for game loop handling. Purposely NOT abstract so that
     * they are optional overrides.
     */
    void create() {
    }

    void create(String[] args) {
    }

    void update() {
    }

    void update(float delta) {
    }

    void render() {
    }

    void onPause() {
    }

    void onResume() {
    }
    // </editor-fold>

    // <editor-fold desc="Dispose" defaultstate="collapsed">
    final void dispose() {
        if (lAudio != null) {
            lAudio.dispose()
        }

        if (lGraphics != null) {
            lGraphics.dispose()
        }
    }
    // </editor-fold>

    // <editor-fold desc="Helper and System api" defaultstate="collapsed">
    /**
     * Returns the FPS as an int.
     *
     * @return the FPS as an int
     */
    final int FPS() {
        return Mdx.platformUtils.getFramesPerSecond()
    }

    int getUsedSprites() {
        return lGraphics.getUsedSprites()
    }

    final boolean getActive() {
        return active
    }

    final void setActive(boolean state) {
        this.active = state
    }

    final void loadSpriteSheet(String progName) {
        lSystem.loadSpriteSheet(progName)
    }

    void loadProgram(String name) {
        lSystem.loadProgram(name)
    }

    void setFont(String path, int spacing, int width, int height) {
        lSystem.setCustomFont(path, spacing, width, height)
    }

    String getProgramPath() {
        return lSystem.getProgramPath()
    }

    String getDataPath() {
        return lSystem.getDataPath()
    }

    /**
     * Pauses the running program.
     */
    void pause() {
        lSystem.pause()
    }

    /**
     * Uses a variable to determine if Leikr should pause.
     * 
     * @param shouldPause if we should pause Leikr
     */
    void pause(boolean shouldPause) {
        lSystem.pause(shouldPause)
    }
    // </editor-fold>

    // <editor-fold desc="Texture api" defaultstate="collapsed"> 
    final void loadImages() {
        lGraphics.loadImages()
    }

    final void drawTexture(String name, BigDecimal x, BigDecimal y) {
        lGraphics.drawTexture(name, x, y)
    }

    final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawTexture(name, x, y, w, h)
    }

    final void drawTexture(String name, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h, boolean flipv) {
        lGraphics.drawTexture(name, x, y, w, h, flipv)
    }
    // </editor-fold>

    // <editor-fold desc="Map api" defaultstate="collapsed">
    final void loadMap(String map) {
        lGraphics.loadMap(map)
    }

    final void drawMap() {
        lGraphics.drawMap()
    }

    final void drawMap(BigDecimal x, BigDecimal y) {
        lGraphics.drawMap(x, y)
    }

    final void drawMap(BigDecimal x, BigDecimal y, int layer) {
        lGraphics.drawMap(x, y, layer)
    }

    final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h) {
        lGraphics.drawMap(x, y, sx, sy, w, h)
    }

    final void drawMap(BigDecimal x, BigDecimal y, BigDecimal sx, BigDecimal sy, BigDecimal w, BigDecimal h, int layer) {
        lGraphics.drawMap(x, y, sx, sy, w, h, layer)
    }

    final int getMapTile(BigDecimal x, BigDecimal y) {
        return lGraphics.getMapTile(x, y)
    }

    final int getMapTile(BigDecimal x, BigDecimal y, int layer) {
        return lGraphics.getMapTile(x, y, layer)
    }

    final void setMapTile(int id, BigDecimal x, BigDecimal y) {
        lGraphics.setMapTile(id, x, y)
    }

    final void setMapTile(int id, BigDecimal x, BigDecimal y, int layer) {
        lGraphics.setMapTile(id, x, y, layer)
    }

    final void removeMapTile(BigDecimal x, BigDecimal y) {
        lGraphics.removeMapTile(x, y)
    }

    final void removeMapTile(BigDecimal x, BigDecimal y, int layer) {
        lGraphics.removeMapTile(x, y, layer)
    }

    final int getMapHeight() {
        return lGraphics.getMapHeight()
    }

    final int getMapWidth() {
        return lGraphics.getMapWidth()
    }
    // </editor-fold>

    // <editor-fold desc="Color api" defaultstate="collapsed">
    final void bgColor(Color color) {
        lGraphics.bgColor(color)
    }

    final void bgColor(int color) {
        lGraphics.bgColor(color)
    }

    final void bgColor(String color) {
        lGraphics.bgColor(color)
    }

    final Color getColor(int color) {
        return lGraphics.getColor(color)
    }

    final Color getColor(String color) {
        return lGraphics.getColor(color)
    }
    // </editor-fold>

    // <editor-fold desc="Graphics helper api" defaultstate="collapsed">
    final void setClip(BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.setClip(x, y, w, h)
    }

    final void removeClip() {
        lGraphics.removeClip()
    }
    // </editor-fold>

    // <editor-fold desc="String api" defaultstate="collapsed"> 
    final void drawString(Color color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(Color color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }

    final void drawString(int color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(int color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }

    final void drawString(String color, String text, BigDecimal x, BigDecimal y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(String color, String text, BigDecimal x, BigDecimal y, BigDecimal width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }
    // </editor-fold>

    // <editor-fold desc="Sprite api" defaultstate="collapsed"> 
    final void sprite(int id, BigDecimal x, BigDecimal y) {
        lGraphics.sprite(id, x, y)
    }

    final void sprite(int id, BigDecimal x, BigDecimal y, BigDecimal degr) {
        lGraphics.sprite(id, x, y, degr)
    }

    final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY) {
        lGraphics.sprite(id, x, y, flipX, flipY)
    }

    final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr) {
        lGraphics.sprite(id, x, y, flipX, flipY, degr)
    }
    //end 8x8 sprites

    //start sizable sprites
    final void sprite(int id, BigDecimal x, BigDecimal y, int size) {
        lGraphics.sprite(id, x, y, size)
    }

    final void sprite(int id, BigDecimal degr, BigDecimal x, BigDecimal y, int size) {
        lGraphics.sprite(id, degr, x, y, size)
    }

    final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, int size) {
        lGraphics.sprite(id, x, y, flipX, flipY, size)
    }

    final void sprite(int id, BigDecimal x, BigDecimal y, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        lGraphics.sprite(id, x, y, flipX, flipY, degr, size)
    }
    //end sizable sprites

    final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph) {
        lGraphics.sprite(ids, px, py, pw, ph)
    }

    final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, int size) {
        lGraphics.sprite(ids, px, py, pw, ph, size)
    }

    final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY) {
        lGraphics.sprite(ids, px, py, pw, ph, flipX, flipY)
    }

    final void sprite(ArrayList<Integer> ids, BigDecimal px, BigDecimal py, BigDecimal pw, BigDecimal ph, boolean flipX, boolean flipY, int size) {
        lGraphics.sprite(ids, px, py, pw, ph, flipX, flipY, size)
    }

    //start scaled sprites
    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale) {
        lGraphics.spriteSc(id, x, y, scale)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, degr)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, degr)
    }

    //Scaled sprites with size
    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scale, int size) {
        lGraphics.spriteSc(id, x, y, scale, size)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, BigDecimal degr, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, degr, size)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, size)
    }

    void spriteSc(int id, BigDecimal x, BigDecimal y, BigDecimal scaleX, BigDecimal scaleY, boolean flipX, boolean flipY, BigDecimal degr, int size) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY, degr, size)
    }
    // </editor-fold>

    // <editor-fold desc="Shape drawing api" defaultstate="collapsed"> 
    final void drawPixel(Color color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawPixel(int color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawPixel(String color, BigDecimal x, BigDecimal y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawRect(Color color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void drawRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void drawRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void fillRect(Color color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void fillRect(int color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void fillRect(String color, BigDecimal x, BigDecimal y, BigDecimal w, BigDecimal h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void drawLineSegment(Color color, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1)
    }

    final void drawLineSegment(int color, BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        lGraphics.drawLineSegment(color, x1, y1, x2, y2)
    }

    final void drawLineSegment(String color, BigDecimal x0, BigDecimal y0, BigDecimal x1, BigDecimal y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1)
    }

    final void drawCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void drawCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void drawCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void fillCircle(Color color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r)
    }

    final void fillCircle(int color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r)
    }

    final void fillCircle(String color, BigDecimal x, BigDecimal y, BigDecimal r) {
        lGraphics.fillCircle(color, x, y, r)
    }

    final void tint(Color color) {
        lGraphics.tint(color)
    }

    final void tint(int color) {
        lGraphics.tint(color)
    }

    final void tint(String color) {
        lGraphics.tint(color)
    }

    final void tint() {
        lGraphics.tint()
    }

    Color getPixel(BigDecimal x, BigDecimal y) {
        return lGraphics.getPixel(x, y)
    }

    Color getPixel(String name, BigDecimal x, BigDecimal y) {
        return lGraphics.getPixel(name, x, y)
    }

    ArrayList<Color> getPixels(String name) {
        return lGraphics.getPixels(name)
    }
    // </editor-fold>

    // <editor-fold desc="Audio api" defaultstate="collapsed">
    final Sound getSound(String fileName) {
        return lAudio.getSound(fileName)
    }

    final void playSound(String name) {
        lAudio.playSound(name)
    }

    final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        lAudio.playSound(name, vol, pit, pan)
    }

    final void stopSound() {
        lAudio.stopSound()
    }

    final void stopSound(String fileName) {
        lAudio.stopSound(fileName)
    }

    final Music getMusic(String fileName) {
        return lAudio.getMusic(fileName)
    }

    final void playMusic(String name) {
        lAudio.playMusic(name)
    }

    final void playMusic(String name, boolean loop) {
        lAudio.playMusic(name, loop)
    }

    final void stopMusic() {
        lAudio.stopMusic()
    }

    final void stopMusic(String fileName) {
        lAudio.stopMusic(fileName)
    }

    final void pauseAudio() {
        lAudio.pauseAudio()
    }

    final void resumeAudio() {
        lAudio.resumeAudio()
    }
    // </editor-fold>

    // <editor-fold desc="Math api" defaultstate="collapsed">
    float abs(BigDecimal value){
        return lSystem.abs(value)
    }
    float cos(BigDecimal radians) {
        return lSystem.cos(radians)
    }

    float cosDeg(BigDecimal deg) {
        return lSystem.cosDeg(deg)
    }

    float sin(BigDecimal radians) {
        return lSystem.sin(radians)
    }

    float sinDeg(BigDecimal deg) {
        return lSystem.sinDeg(deg)
    }

    int ceil(BigDecimal value) {
        return lSystem.ceil(value)
    }

    int floor(BigDecimal value) {
        return lSystem.floor(value)
    }

    int randInt(BigDecimal range) {
        return lSystem.randInt(range)
    }

    int randInt(BigDecimal start, BigDecimal end) {
        return lSystem.randInt(start, end)
    }

    float randFloat(BigDecimal range) {
        return lSystem.randFloat(range)
    }

    float randFloat(BigDecimal start, BigDecimal end) {
        return lSystem.randFloat(start, end)
    }

    int round(BigDecimal number) {
        return lSystem.round(number)
    }
    // </editor-fold>

    // <editor-fold desc="Input api" defaultstate="collapsed"> 
   
    //detect Keyboard key presses (polling continuously)
    final boolean key(String key) {
        return lKeyboard.key(key)
    }

    final boolean keyUp(String key) {
        return lKeyboard.keyUp(key)
    }

    //detect single key press.
    final boolean keyPress(String key) {
        return lKeyboard.keyPress(key)
    }

    /**
     * Detects a Mouse click event.
     *
     * @return
     */
    final boolean mouseClick() {
        return lMouse.mouseClick()
    }

    final float mouseX() {
        return lMouse.mouseX()
    }

    final float mouseY() {
        return lMouse.mouseY()
    }

    @Override
    boolean keyDown(int keycode) {
        return false
    }

    @Override
    boolean keyUp(int keycode) {
        return false
    }

    @Override
    boolean keyTyped(char character) {
        return false
    }

    @Override
    boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchDragged(int screenX, int screenY, int pointer) {
        return false
    }

    @Override
    boolean mouseMoved(int screenX, int screenY) {
        return false
    }

    @Override
    boolean scrolled(float amount, float i) {
        return false
    }
    // </editor-fold>

    // <editor-fold desc="Data api" defaultstate="collapsed"> 
    void addData(String key, Object value) {
        lData.addData(key, value)
    }

    void deleteData(String key) {
        lData.deleteData(key)
    }

    void clearData() {
        lData.clearData()
    }

    void saveData(String path) {
        lData.saveData(path)
    }

    void saveData(String path, HashMap<String, Object> dat) {
        lData.saveData(path, dat)
    }

    HashMap<String, Object> readData(String path) {
        return lData.readData(path)
    }
    // </editor-fold>

    // <editor-fold desc="Experimental" defaultstate="collapsed"> 
    boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal r1, BigDecimal x2, BigDecimal y2, BigDecimal r2) {
        return lSystem.collides(x1, y1, r1, x2, y2, r2)
    }

    boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal w1, BigDecimal h1, BigDecimal x2, BigDecimal y2, BigDecimal w2, BigDecimal h2) {
        return lSystem.collides(x1, y1, w1, h1, x2, y2, w2, h2)
    }

    boolean collides(BigDecimal[] a, BigDecimal[] b) {
        return lSystem.collides(a, b)
    }

    boolean point(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2, BigDecimal w, BigDecimal h) {
        return lSystem.point(x, y, x2, y2, w, h)
    }

    Object compile(String path) {
        return lSystem.compile(path)
    }

    void compile(String path, String out) {
        lSystem.compile(path, out)
    }

    Object eval(String code) {
        return lSystem.eval(code)
    }

    Object parse(String code) {
        return lSystem.parse(code)
    }

    void loadLib(String path) {
        lSystem.loadLib(path)
    }

    Object newInstance(String name) {
        return lSystem.newInstance(name)
    }
    // </editor-fold>
}
