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

    private static boolean CONFIRM = false;

    public static EngineState engineState;

    protected enum EngineState {
        RUNNING,
        BACK,
        ERROR,
        PAUSE
    }

    public EngineScreen() {
    }
    
    public static void pauseEngine(){
        engineState = EngineState.PAUSE;
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
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void postTransitionOut(Transition transition) {
        if (null != engine) {
            engine.setActive(false);
            engine.dispose();
            engine = null; // release all objects for gc
        }
        System.out.println("Engine classes disposed.");
    }

    @Override
    public void preTransitionIn(Transition trans) {
        engineState = EngineState.RUNNING;
    }

    @Override
    public void postTransitionIn(Transition transition) {
        if (engineState.equals(EngineState.ERROR)) {
            return;
        }
        try {
            system.setRunning(true);
            engine.preCreate(EngineLoader.getEngineLoader(false).cp.MAX_SPRITES, system, viewport);
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
                    if (engine.preUpdate(delta)) {
                        pause();
                    }
                    engine.update(delta);
                } catch (Exception ex) {
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

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(Mdx.graphicsContext);
        if (null != engine && !engine.getActive()) {
            return;
        }
        switch (engineState) {
            case RUNNING:
                try {
                    system.render();
                    engine.preRender();
                    engine.render();
                } catch (Exception ex) {
                    engineState = EngineState.ERROR;
                    errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
                    Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case PAUSE:
                g.setColor(Colors.WHITE());
                g.drawString("-- Paused --", 0, 60, 240, 1);
                g.drawString("Exit running program?", 0, 74, 240, 1);
                g.drawString("Yes    No", 0, 90, 240, 1);

                if (CONFIRM) {
                    g.setColor(Colors.GREEN());
                    g.drawRect(78, 86, 36, 16);
                } else {
                    g.setColor(Colors.RED());
                    g.drawRect(130, 86, 36, 16);
                }
                break;
            case ERROR:
            default:
        }

    }

    @Override
    public int getId() {
        return ID;
    }
}
