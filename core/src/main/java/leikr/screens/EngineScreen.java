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
package leikr.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.anuke.gif.GifRecorder;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;

/**
 *
 * @author tor
 */
public class EngineScreen extends BasicGameScreen {

    public static int ID = 1;

    Engine engine;
    LeikrSystemManager system;
    String errorMessage;
    FitViewport viewport;
    FrameBuffer frameBuffer;

    SpriteBatch gifBatch;
    GifRecorder recorder;

    private static String[] engineArgs;

    private static boolean CONFIRM = false;

    public static EngineState engineState;

    protected enum EngineState {
        RUNNING,
        BACK,
        ERROR,
        PAUSE
    }

    public EngineScreen(FitViewport vp) {
        viewport = vp;
        gifBatch = new SpriteBatch();
        recorder = new GifRecorder(gifBatch);
        recorder.setOpenKey(Keys.F2);
        recorder.setRecordKey(Keys.F3);
    }

    public static void pauseEngine() {
        engineState = EngineState.PAUSE;
    }

    public static void setEngineArgs(String[] args) {
        engineArgs = args;
    }

    private void pause() {
        engineState = EngineState.PAUSE;
        engine.lAudio.pauseAllAudio();
        engine.onPause();
    }

    private void resume() {
        if (CONFIRM) {
            engineState = EngineState.BACK;
        } else {
            engineState = EngineState.RUNNING;
            engine.lAudio.resumeAllAudio();
            engine.onResume();
        }
    }

    void enterMenuScreen(ScreenManager sm) {
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.checkLaunchTitle()) {
            Mdx.platformUtils.exit(false);
        } else {
            sm.enterGameScreen(TerminalScreen.ID, null, null);
        }
    }

    void enterErrorScreen(ScreenManager sm) {
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.checkLaunchTitle()) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            ErrorScreen.setErrorMessage(errorMessage);
            sm.enterGameScreen(ErrorScreen.ID, null, null);
        }
    }

    void reloadEngine(ScreenManager sm) {
        sm.enterGameScreen(LoadScreen.ID, null, null);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialise(GameContainer gc) {
        system = LeikrSystemManager.getLeikrSystemManager();
    }

    @Override
    public void postTransitionOut(Transition transition) {
        if (null != engine) {
            engine.setActive(false);
            engine.dispose();
            engine = null; // release all Engine objects for gc
        }
        frameBuffer.dispose();
        engineArgs = new String[0];//set args to an empty array.
        System.out.println("Engine classes disposed.");
    }

    @Override
    public void preTransitionIn(Transition trans) {
        engineState = EngineState.RUNNING;
        frameBuffer = Mdx.graphics.newFrameBuffer(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void postTransitionIn(Transition transition) {
        if (engineState.equals(EngineState.ERROR)) {
            return;
        }
        try {
            system.setRunning(true);
            engine.preCreate(EngineLoader.getEngineLoader(false).cp.MAX_SPRITES, system, viewport, frameBuffer);
            engine.create(engineArgs);
            engine.create();
        } catch (Exception ex) {
            engineState = EngineState.ERROR;
            errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (Mdx.input.isKeyJustPressed(Keys.F5) || Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyJustPressed(Keys.R) || Mdx.input.isKeyJustPressed(Keys.HOME)) {
            reloadEngine(sm);
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (engineState.equals(EngineState.PAUSE)) {
                resume();
            } else {
                pause();
            }
        }
        switch (engineState) {
            case BACK:
                system.resetFont();
                enterMenuScreen(sm);
                break;
            case ERROR:
                system.resetFont();
                enterErrorScreen(sm);
                break;
            case RUNNING:
                try {
                engine.preUpdate(delta);
                engine.update(delta);
                engine.update();
            } catch (Exception ex) {
                engine.lAudio.pauseAllAudio();
                engineState = EngineState.ERROR;
                errorMessage = "Error in program `update` method. " + ex.getLocalizedMessage();
                Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case PAUSE:
                if (Mdx.input.isKeyJustPressed(Keys.LEFT)) {
                    CONFIRM = true;
                }
                if (Mdx.input.isKeyJustPressed(Keys.RIGHT)) {
                    CONFIRM = false;
                }
                if (Mdx.input.isKeyJustPressed(Keys.ENTER) || Mdx.input.isKeyJustPressed(Keys.K)) {
                    resume();
                }
                break;
        }

        if (!system.update(sm)) {
            system.resetFont();
            System.out.println("Transition initiated from running program.");
        }
    }

    /*
    postRender() and preRender() are not recommended to be used, but for the FrameBuffer object to work
    inside of the viewport it was necessary to cut off two separate rendering sessions.
    
    preRender(240,160) sets the game windowWidth and windowHeight to the required size for proper 
    rendering. Couldn't find any other way to make this happen.
     */
    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        if (null != engine && !engine.getActive()) {
            return;
        }
        g.drawTexture(frameBuffer.getTexture(), 0, 0, false);
        g.postRender();

        frameBuffer.begin();
        g.preRender(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        switch (engineState) {
            case RUNNING:
                renderRunning(g);
                break;
            case PAUSE:
                renderPause(g);
                break;
            case ERROR:
            default:
        }
        g.flush();
        g.postRender();
        frameBuffer.end();
        recorder.update();
    }

    void renderRunning(Graphics g) {
        try {
            engine.preRender(g);
            engine.render();
        } catch (Exception ex) {
            engine.lAudio.pauseAllAudio();
            engineState = EngineState.ERROR;
            errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void renderPause(Graphics g) {
        g.setColor(Colors.WHITE());
        g.drawString("-- Paused --", 0, 60, GameRuntime.WIDTH, 1);
        g.drawString("Exit running program?", 0, 74, GameRuntime.WIDTH, 1);
        g.drawString("Yes           No", 0, 90, GameRuntime.WIDTH, 1);

        if (CONFIRM) {
            g.setColor(Colors.GREEN());
            g.drawRect(78, 86, 36, 16);
        } else {
            g.setColor(Colors.RED());
            g.drawRect(130, 86, 36, 16);
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
