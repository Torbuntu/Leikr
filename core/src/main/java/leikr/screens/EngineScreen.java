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

import com.badlogic.gdx.Gdx;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.ManagerDTO;
import leikr.loaders.EngineLoader;
import leikr.managers.SystemManager;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.StretchViewport;
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

    public String errorMessage;
    private boolean CONFIRM = false;
    private String path;

    private Engine engine;
    private FrameBuffer frameBuffer;
    private final EngineLoader engineLoader;
    private final FitViewport mainViewport;
    private final GameRuntime runtime;
    private final SystemManager systemManager;
    private final StretchViewport fboViewport;
    private final ManagerDTO managerDTO;

    private EngineState engineState;

    protected enum EngineState {
        RUNNING,
        BACK,
        ERROR,
        PAUSE
    }

    public EngineScreen(FitViewport vp, ManagerDTO managerDTO, EngineLoader engineLoader, GameRuntime runtime) {
        fboViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT);
        mainViewport = vp;
        this.managerDTO = managerDTO;
        this.engineLoader = engineLoader;
        this.runtime = runtime;
        this.systemManager = managerDTO.getSystemManager();
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void postTransitionOut(Transition transition) {
        if (null != engine) {
            // release all Engine objects for gc
            engine.setActive(false);
            engine.dispose();
            engine = null;
        }
        frameBuffer.dispose();
        // Reset args to an empty array.
        Logger.getLogger(EngineScreen.class.getName()).log(Level.INFO, "Engine classes disposed.");
    }

    @Override
    public void preTransitionIn(Transition trans) {
        engineState = EngineState.RUNNING;
        frameBuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT);
    }

    @Override
    public void postTransitionIn(Transition transition) {
        if (engineState.equals(EngineState.ERROR)) {
            return;
        }
        try {
            systemManager.setRunning(true);
            engine.preCreate(path, engineLoader.getMaxSprite(), managerDTO, fboViewport, frameBuffer);
            engine.create(engineLoader.getEngineArgs());
            engine.create();
        } catch (Exception ex) {
            engineState = EngineState.ERROR;
            errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void preTransitionOut(Transition transition) {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (Mdx.input.isKeyJustPressed(Keys.F5) || Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyJustPressed(Keys.R) || Mdx.input.isKeyJustPressed(Keys.HOME)) {
            reloadEngine(sm);
        }

        // Checking for pause. We pause on keyboard ESCAPE, or if a pause was triggered in the game's code.
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE) || systemManager.checkShouldPause()) {
            systemManager.pause(false);
            if (engineState.equals(EngineState.PAUSE)) {
                resume();
            } else {
                pause();
            }
        }

        // File drop listener. If a file is dropped into the screen, we try to load the new game.
        if (runtime.checkFileDropped()) {
            LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
            ls.setGameName(runtime.getFileDroppedTitle());
            runtime.setGameName(runtime.getFileDroppedTitle());
            runtime.clearFileDropped();
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }

        // Main update loop for the game.
        switch (engineState) {
            case BACK -> {
                systemManager.resetFont();
                enterMenuScreen(sm);
            }
            case ERROR -> {
                systemManager.resetFont();
                enterErrorScreen(sm);
            }
            case RUNNING -> {
                try {
                    engine.preUpdate(delta);
                    engine.update(delta);
                    engine.update();
                } catch (Exception ex) {
                    engineState = EngineState.ERROR;
                    errorMessage = "Error in program `update` method. " + ex.getLocalizedMessage();
                    Logger.getLogger(EngineScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case PAUSE -> {
                if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT)) {
                    if (Mdx.input.isKeyJustPressed(Keys.NUM_1)) {
                        Gdx.graphics.setWindowedMode(240, 160);
                    }
                    if (Mdx.input.isKeyJustPressed(Keys.NUM_2)) {
                        Gdx.graphics.setWindowedMode(240 * 2, 160 * 2);
                    }
                    if (Mdx.input.isKeyJustPressed(Keys.NUM_3)) {
                        Gdx.graphics.setWindowedMode(240 * 3, 160 * 3);
                    }
                    if (Mdx.input.isKeyJustPressed(Keys.NUM_4)) {
                        Gdx.graphics.setWindowedMode(240 * 4, 160 * 4);
                    }
                    break;
                }
                if (Mdx.input.isKeyJustPressed(Keys.LEFT)) {
                    CONFIRM = true;
                }
                if (Mdx.input.isKeyJustPressed(Keys.RIGHT)) {
                    CONFIRM = false;
                }
                if (Mdx.input.isKeyJustPressed(Keys.ENTER) || Mdx.input.isKeyJustPressed(Keys.K)) {
                    resume();
                }
            }
        }

        if (!systemManager.update(sm)) {
            systemManager.resetFont();
            Logger.getLogger(EngineScreen.class.getName()).log(Level.WARNING, "Transition initiated from running program.");
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (null != engine && !engine.getActive()) {
            return;
        }

        //Apply a StretchViewport so that all FBO operations are scaled correctly to the fbo.
        fboViewport.apply(g);

        // Main preRender methods
        frameBuffer.begin();
        switch (engineState) {
            case RUNNING -> {
                Gdx.input.setCursorCatched(true);
                renderRunning(g);
            }
            case PAUSE -> {
                Gdx.input.setCursorCatched(false);
                renderPause(g);
            }
        }
        g.flush();
        frameBuffer.end();

        //Apply a FitViewport so that the 240x160 and aspect ratio is correct.
        mainViewport.apply(g);
        g.drawTexture(frameBuffer.getTexture(), 0, 0, false);
    }

    @Override
    public int getId() {
        return ID;
    }

    private void renderRunning(Graphics g) {
        try {
            engine.preRender(g);
            engine.render();
        } catch (Exception ex) {
            engineState = EngineState.ERROR;
            errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void renderPause(Graphics g) {
        g.clearContext(Colors.BLACK());

        //Call shape rendering first, to ensure that the shape renderer draws and ends clean.
        if (CONFIRM) {
            g.setColor(Colors.GREEN());
            g.drawRect(78, 86, 36, 16);
        } else {
            g.setColor(Colors.RED());
            g.drawRect(130, 86, 36, 16);
        }
        g.setColor(Colors.WHITE());
        g.drawString("-- Paused --", 0, 60, runtime.WIDTH, 1);
        g.drawString("Exit running program?", 0, 74, runtime.WIDTH, 1);
        g.drawString("Yes           No", 0, 90, runtime.WIDTH, 1);
    }

    public void errorEngine(String message) {
        errorMessage = message;
        engineState = EngineState.ERROR;
    }

    public void pauseEngine() {
        engineState = EngineState.PAUSE;
    }

    private void pause() {
        engineState = EngineState.PAUSE;
        engine.lAudio.pauseAudio();
        engine.onPause();
    }

    private void resume() {
        if (CONFIRM) {
            engineState = EngineState.BACK;
        } else {
            engineState = EngineState.RUNNING;
            //engine.lAudio.resumeAllAudio();
            engine.onResume();
        }
    }

    void enterMenuScreen(ScreenManager sm) {
        if (null != engine) {
            engine.setActive(false);
        }
        if (runtime.checkDirectLaunch()) {
            Mdx.platformUtils.exit(false);
        } else {
            sm.enterGameScreen(TerminalScreen.ID, null, null);
        }
    }

    void enterErrorScreen(ScreenManager sm) {
        if (null != engine) {
            engine.setActive(false);
        }
        if (runtime.checkDirectLaunch()) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            ErrorScreen es = (ErrorScreen) sm.getGameScreen(ErrorScreen.ID);
            es.setErrorMessage(errorMessage);
            sm.enterGameScreen(ErrorScreen.ID, null, null);
        }
    }

    void reloadEngine(ScreenManager sm) {
        sm.enterGameScreen(LoadScreen.ID, null, null);
    }

    public void setEngine(Engine engine, String path) {
        this.engine = engine;
        this.path = path;
        systemManager.reset();
    }
}
