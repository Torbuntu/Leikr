package leikr;

import leikr.controls.LeikrController;
import leikr.controls.LeikrKeyboard;
import leikr.controls.LeikrMouse;
import leikr.customProperties.CustomSystemProperties;
import leikr.managers.LeikrAudioManager;
import leikr.managers.LeikrScreenManager;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.math.MathUtils;

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

    public final void drawColor(String c) {
        screen.drawColor(c);
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
    public final void clip(float x, float y, float w, float h) {
        screen.clip(x, y, w, h);
    }

    public final void clip() {
        screen.clip();
    }

    //end helper methods
    //text methods
    public final void text(String text, float x, float y, int color) {
        screen.text(text, x, y, color);
    }

    public final void text(String text, float x, float y, String color) {
        screen.text(text, x, y, color);
    }

    public final void text(String text, float x, float y, float width, int color) {
        screen.text(text, x, y, width, color);
    }

    public final void text(String text, float x, float y, float width, String color) {
        screen.text(text, x, y, width, color);
    }

    public final void text(String text, float x, float y, float width, int align, int color) {
        screen.text(text, x, y, width, align, color);
    }

    public final void text(String text, float x, float y, float width, int align, String color) {
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
    public final void pixel(int color, int x, int y) {
        screen.pixel(color, x, y);
    }

    public final void rect(int x, int y, int w, int h) {
        screen.rect(x, y, w, h);
    }

    public final void rect(int x, int y, int w, int h, boolean fill) {
        screen.rect(x, y, w, h, fill);
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

    public final void stopSfx() {
        audio.stopSfx();
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

    public final void pauseAudio() {
        audio.pauseAllAudio();
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

    public int btnCode(String button) {
        return (null != controllerA) ? controllerA.btnCode(button) : -1;
    }

    public String btnName(int id) {
        return (controllerA != null) ? controllerA.btnName(id) : "Null";
    }

    public void setController(BaseGamePadListener adap) {
        if (controllerA != null) {
            controllerA.setController(adap);
        }
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

    public boolean collides(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        return x1 + w1 >= x2 && x2 + w2 >= x1 || y1 + h1 >= y2 && y2 + h2 >= y1;
    }

    public boolean collides(float[] a, float[] b) {
        return a[0] + a[2] >= b[0] && b[0] + b[2] >= a[0] || a[1] + a[3] >= b[1] && b[1] + b[3] >= a[1];
    }

    public boolean point(float x, float y, float x2, float y2, float w, float h) {
        return x >= x2 && x <= x2 + w && y >= y2 && y <= y2 + h;
    }

    public Object compile(String path) {
        return system.compile(path);
    }

    public void compile(String path, String out) {
        system.compile(path, out);
    }

    public Object eval(String code, int opt) {
        return system.eval(code, opt);
    }

    public Object eval(String code) {
        return system.eval(code);
    }

    public void loadLib(String path) {
        system.loadLib(path);
    }

    public Object newInstance(String name) {
        return system.newInstance(name);
    }
    //END Experimental
}
