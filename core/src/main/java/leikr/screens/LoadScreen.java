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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 */
public class LoadScreen extends BasicGameScreen {

    public static int ID = 4;

    FitViewport viewport;
    ExecutorService service;
    Future engineGetter;

    int loadCircleCircumf = 15;
    int loadCircleDir = 1;
    String loadPhrase = "Loading ";

    public LoadScreen() {
        service = Executors.newFixedThreadPool(1);
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void preTransitionIn(Transition transition) {
        engineGetter = service.submit(EngineLoader.getEngineLoader(true));
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (engineGetter.isDone()) {
            try {
                EngineScreen scrn = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
                scrn.setEngine((Engine) engineGetter.get());
                sm.enterGameScreen(EngineScreen.ID, null, null);
            } catch (InterruptedException | ExecutionException ex) {
                ErrorScreen.setErrorMessage("Error loading engine: " + ex.getMessage());
                sm.enterGameScreen(ErrorScreen.ID, null, null);
                Logger.getLogger(LoadScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (loadCircleCircumf >= 20 && loadCircleDir > 0) {
            loadCircleDir = -1;
        }
        if (loadCircleCircumf <= 1 && loadCircleDir < 0) {
            loadCircleDir = 1;
        }

        if (loadCircleCircumf <= 5) {
            loadPhrase = "Loading " + GameRuntime.GAME_NAME;
        } else if (loadCircleCircumf <= 10) {
            loadPhrase = "Loading " + GameRuntime.GAME_NAME + ".";
        } else if (loadCircleCircumf <= 15) {
            loadPhrase = "Loading " + GameRuntime.GAME_NAME + "..";
        } else if (loadCircleCircumf <= 20) {
            loadPhrase = "Loading " + GameRuntime.GAME_NAME + "...";
        }

        loadCircleCircumf += loadCircleDir;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        if (!engineGetter.isDone()) {
            g.setColor(Colors.GREEN());
            g.fillCircle(120, 80, loadCircleCircumf);
            g.drawString(loadPhrase, 0, viewport.getHeight() - 9);
        }
    }

    @Override
    public int getId() {
        return ID;
    }

}
