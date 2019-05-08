/*
 * Copyright 2019 torbuntu.
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
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 */
public class EngineScreen extends BasicGameScreen {

    public static int ID = 1;

    Engine engine;
    static boolean BACK = false;
    static boolean ERROR = false;
    String errorMessage;

    public static void setBack(boolean setback) {
        BACK = setback;
    }

    void enterMenuScreen(ScreenManager sm) {
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.SINGLE_LAUNCH) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
    }

    void enterErrorScreen(ScreenManager sm) {
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.SINGLE_LAUNCH) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            ErrorScreen.setErrorMessage(errorMessage);
            sm.enterGameScreen(ErrorScreen.ID, null, null);
        }
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void postTransitionOut(Transition transition) {
        ERROR = false;
        BACK = false;
        if (null != engine) {
            engine.dispose();
            engine = null; // release all objects for gc
        }
        System.out.println("Engine classes disposed.");
    }

    @Override
    public void postTransitionIn(Transition transition) {
        if (ERROR) {
            return;
        }
        try {
            engine.preCreate(EngineLoader.cp.MAX_SPRITES);
            engine.create();
            Gdx.input.setInputProcessor(engine);
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage();
            ex.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (BACK) {
            enterMenuScreen(sm);
        }
        if (ERROR) {
            enterErrorScreen(sm);
            return;
        }
        try {
            engine.preUpdate(delta);
            engine.update(delta);
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `update` method. " + ex.getLocalizedMessage();
            ex.printStackTrace();
        }

    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (null != engine && !engine.getActive() || ERROR) {
            return;
        }
        try {
            engine.preRender(g);
            engine.render();
            engine.postRender();
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
            ex.printStackTrace();
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
