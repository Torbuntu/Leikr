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

import leikr.managers.InputManager

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
abstract class Engine implements InputProcessor{

    /**
     * Used by {@link leikr.screens.EngineScreen} to determine if the game
     * should be actively running.
     */
    private boolean active

    LeikrKeyboard lKeyboard
    LeikrMouse lMouse
    LeikrController lControllerA
    LeikrController lControllerB

    /**
     * Managers are used to load the custom assets for a game at startup.
     */
    GraphicsManager lGraphics
    SystemManager lSystem
    AudioManager lAudio
    DataManager lData
    InputManager lInput

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
        lInput = managerDTO.getInputManager()

        // reset the settings to apply to new game assets
        lAudio.resetAudioManager(path)
        lData.resetData(path)
        lGraphics.resetScreenManager(path, maxSprites)
        lGraphics.preCreate(framebuffer, viewport)

        lMouse = managerDTO.inputManager.getMouse()
        lMouse.setViewport(viewport)
        lKeyboard = managerDTO.inputManager.getKeyboard()
        lControllerA = managerDTO.inputManager.getControllerA()
        lControllerB = managerDTO.inputManager.getControllerB()

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
        if (lAudio) {
            lAudio.dispose()
        }

        if (lGraphics) {
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
    static final int FPS() {
        return Mdx.platformUtils.getFramesPerSecond()
    }

    int getUsedSprites() {
        lGraphics.getUsedSprites()
    }

    final boolean getActive() {
        active
    }

    final void setActive(boolean state) {
        this.active = state
    }

    final void loadSpriteSheet(String programName) {
        lSystem.loadSpriteSheet(programName)
    }

    void loadProgram(String name) {
        lSystem.loadProgram(name)
    }

    void setFont(String path, int spacing, int width, int height) {
        lSystem.setCustomFont(path, spacing, width, height)
    }

    String getProgramPath() {
        lSystem.getProgramPath()
    }

    String getDataPath() {
        lSystem.getDataPath()
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

    final void drawTexture(String name, Number x, Number y) {
        lGraphics.drawTexture(name, x, y)
    }

    final void drawTexture(String name, Number x, Number y, Number w, Number h) {
        lGraphics.drawTexture(name, x, y, w, h)
    }

    final void drawTexture(String name, Number x, Number y, Number w, Number h, boolean flipVertical) {
        lGraphics.drawTexture(name, x, y, w, h, flipVertical)
    }
    // </editor-fold>

    // <editor-fold desc="Map api" defaultstate="collapsed">
    final void loadMap(String map) {
        lGraphics.loadMap(map)
    }

    final void drawMap() {
        lGraphics.drawMap()
    }

    final void drawMap(Number x, Number y, int layer = 0) {
        lGraphics.drawMap(x, y, layer)
    }

    final void drawMap(Number x, Number y, Number sx, Number sy, Number w, Number h) {
        lGraphics.drawMap(x, y, sx, sy, w, h)
    }

    final void drawMap(Number x, Number y, Number sx, Number sy, Number w, Number h, int layer) {
        lGraphics.drawMap(x, y, sx, sy, w, h, layer)
    }

    final int getMapTile(Number x, Number y, int layer = 0) {
        return lGraphics.getMapTile(x, y, layer)
    }

    final void setMapTile(int id, Number x, Number y, int layer = 0) {
        lGraphics.setMapTile(id, x, y, layer)
    }

    final void removeMapTile(Number x, Number y, int layer = 0) {
        lGraphics.removeMapTile(x, y, layer)
    }

    final int getMapHeight() {
        lGraphics.getMapHeight()
    }

    final int getMapWidth() {
        lGraphics.getMapWidth()
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
    final void setClip(Number x, Number y, Number w, Number h) {
        lGraphics.setClip(x, y, w, h)
    }

    final void removeClip() {
        lGraphics.removeClip()
    }
    // </editor-fold>

    // <editor-fold desc="String api" defaultstate="collapsed">
    final void drawString(Color color, String text, Number x, Number y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(Color color, String text, Number x, Number y, Number width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(Color color, String text, Number x, Number y, Number width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }

    final void drawString(int color, String text, Number x, Number y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(int color, String text, Number x, Number y, Number width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(int color, String text, Number x, Number y, Number width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }

    final void drawString(String color, String text, Number x, Number y) {
        lGraphics.drawString(color, text, x, y)
    }

    final void drawString(String color, String text, Number x, Number y, Number width) {
        lGraphics.drawString(color, text, x, y, width)
    }

    final void drawString(String color, String text, Number x, Number y, Number width, int align) {
        lGraphics.drawString(color, text, x, y, width, align)
    }
    // </editor-fold>

    // <editor-fold desc="Sprite api" defaultstate="collapsed">
    final void sprite(int id, Number x, Number y, int size = 0) {
        lGraphics.sprite(id, x, y, size)
    }

    final void sprite(int id, Number x, Number y, int size, Number degrees) {
        lGraphics.sprite(id, x, y, size, degrees)
    }

    final void sprite(int id, Number x, Number y, boolean flipX, boolean flipY) {
        lGraphics.sprite(id, x, y, flipX, flipY)
    }

    final void sprite(int id, Number x, Number y, int size, boolean flipX, boolean flipY) {
        lGraphics.sprite(id, x, y, size, flipX, flipY)
    }

    final void sprite(int id, Number x, Number y, int size, Number degrees, boolean flipX, boolean flipY) {
        lGraphics.sprite(id, x, y, size, degrees, flipX, flipY)
    }

    final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size = 0) {
        lGraphics.sprite(ids, px, py, pw, ph, size)
    }

    final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, boolean flipX, boolean flipY) {
        lGraphics.sprite(ids, px, py, pw, ph, flipX, flipY)
    }

    final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size, boolean flipX, boolean flipY) {
        lGraphics.sprite(ids, px, py, pw, ph, size, flipX, flipY)
    }

    //start scaled sprites
    final void spriteSc(int id, Number x, Number y, Number scale, int size = 0) {
        lGraphics.spriteSc(id, x, y, scale, size)
    }

    final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size = 0) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size)
    }

    final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, Number degrees) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size, degrees)
    }

    final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, boolean flipX, boolean flipY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, flipX, flipY)
    }

    final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, boolean flipX, boolean flipY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size, flipX, flipY)
    }

    final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, Number degrees, boolean flipX, boolean flipY) {
        lGraphics.spriteSc(id, x, y, scaleX, scaleY, size, degrees, flipX, flipY)
    }
    // </editor-fold>

    // <editor-fold desc="Shape drawing api" defaultstate="collapsed">
    final void drawPixel(Color color, Number x, Number y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawPixel(int color, Number x, Number y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawPixel(String color, Number x, Number y) {
        lGraphics.drawPixel(color, x, y)
    }

    final void drawRect(Color color, Number x, Number y, Number w, Number h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void drawRect(int color, Number x, Number y, Number w, Number h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void drawRect(String color, Number x, Number y, Number w, Number h) {
        lGraphics.drawRect(color, x, y, w, h)
    }

    final void fillRect(Color color, Number x, Number y, Number w, Number h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void fillRect(int color, Number x, Number y, Number w, Number h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void fillRect(String color, Number x, Number y, Number w, Number h) {
        lGraphics.fillRect(color, x, y, w, h)
    }

    final void drawLineSegment(Color color, Number x0, Number y0, Number x1, Number y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1)
    }

    final void drawLineSegment(int color, Number x1, Number y1, Number x2, Number y2) {
        lGraphics.drawLineSegment(color, x1, y1, x2, y2)
    }

    final void drawLineSegment(String color, Number x0, Number y0, Number x1, Number y1) {
        lGraphics.drawLineSegment(color, x0, y0, x1, y1)
    }

    final void drawCircle(Color color, Number x, Number y, Number r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void drawCircle(int color, Number x, Number y, Number r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void drawCircle(String color, Number x, Number y, Number r) {
        lGraphics.drawCircle(color, x, y, r)
    }

    final void fillCircle(Color color, Number x, Number y, Number r) {
        lGraphics.fillCircle(color, x, y, r)
    }

    final void fillCircle(int color, Number x, Number y, Number r) {
        lGraphics.fillCircle(color, x, y, r)
    }

    final void fillCircle(String color, Number x, Number y, Number r) {
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

    Color getPixel(Number x, Number y) {
        lGraphics.getPixel(x, y)
    }

    Color getPixel(String name, Number x, Number y) {
        lGraphics.getPixel(name, x, y)
    }

    ArrayList<Color> getPixels(String name) {
        lGraphics.getPixels(name)
    }
    // </editor-fold>

    // <editor-fold desc="Audio api" defaultstate="collapsed">
    final Sound getSound(String fileName) {
        lAudio.getSound(fileName)
    }

    final void playSound(String name) {
        lAudio.playSound(name)
    }

    final void playSound(String name, Number vol, Number pit, Number pan) {
        lAudio.playSound(name, vol, pit, pan)
    }

    final void stopSound() {
        lAudio.stopSound()
    }

    final void stopSound(String fileName) {
        lAudio.stopSound(fileName)
    }

    final Music getMusic(String fileName) {
        lAudio.getMusic(fileName)
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
    // Experimental
    def listSounds(){
        lAudio.listSounds()
    }
    def listMusic(){
        lAudio.listMusic()
    }

    // </editor-fold>

    // <editor-fold desc="Math api" defaultstate="collapsed">
    float abs(Number value){
        lSystem.abs(value)
    }
    float cos(Number radians) {
        lSystem.cos(radians)
    }

    float cosDeg(Number deg) {
        lSystem.cosDeg(deg)
    }

    float sin(Number radians) {
        lSystem.sin(radians)
    }

    float sinDeg(Number deg) {
        lSystem.sinDeg(deg)
    }

    int ceil(Number value) {
        lSystem.ceil(value)
    }

    int floor(Number value) {
        lSystem.floor(value)
    }

    int randInt(Number range) {
        lSystem.randInt(range)
    }

    int randInt(Number start, Number end) {
        lSystem.randInt(start, end)
    }

    float randFloat(Number range) {
        lSystem.randFloat(range)
    }

    float randFloat(Number start, Number end) {
        lSystem.randFloat(start, end)
    }

    int round(Number number) {
        lSystem.round(number)
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

    // Default to controller A if no index given
    final boolean button(String key){
        lInput.button(key)
    }

    final boolean button(String key, int playerId){
        lInput.button(key, playerId)
    }

    final boolean buttonAny(){
        lInput.buttonAny()
    }

    final boolean buttonAny(int playerId){
        lInput.buttonAny(playerId)
    }

    final boolean buttonPress(String key){
        lInput.buttonPress(key)
    }

    final boolean buttonPress(String key, int playerId){
        lInput.buttonPress(key, playerId)
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

    void saveData(String path, Map<String, Object> dat) {
        lData.saveData(path, dat)
    }

    Map<String, Object> readData(String path) {
        lData.readData(path)
    }
    // </editor-fold>

    // <editor-fold desc="Experimental" defaultstate="collapsed">
    Object compile(String path) {
        lSystem.compile(path)
    }

    void compile(String path, String out) {
        lSystem.compile(path, out)
    }

    Object eval(String code) {
        lSystem.eval(code)
    }

    Object parse(String code) {
        lSystem.parse(code)
    }

    void loadLib(String path) {
        lSystem.loadLib(path)
    }

    Object newInstance(String name) {
        lSystem.newInstance(name)
    }
    // </editor-fold>
}
