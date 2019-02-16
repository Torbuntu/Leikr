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
import com.badlogic.gdx.assets.AssetManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.codehaus.groovy.control.CompilationFailedException;
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
    AssetManager assetManager;

    Engine engine;
    public static boolean back = false;

    public EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    void switchScreen(ScreenManager sm) {
        back = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.SINGLE_LAUNCH) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void onResize(int width, int height) {
    }

    @Override
    public void preTransitionOut(Transition transition) {
        if (null != engine) {
            engine.dispose();
        }
        System.out.println("Engine classes disposed.");
    }

    @Override
    public void preTransitionIn(Transition transition) {
        try {
            engine = EngineLoader.getEngine();//calls engine.preCreate()
        } catch (IOException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | CompilationFailedException ex) {
            back = true;
            System.out.println("Error parsing program code. " + ex.getMessage());
        }
    }

    @Override
    public void postTransitionIn(Transition transition) {
        MenuScreen.finishLoading();
        try {
            engine.create();
            Gdx.input.setInputProcessor(engine);
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in program `create` method. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {

        if (back) {
            switchScreen(sm);
        }
        try {
            engine.preUpdate(delta);
            engine.update(delta);
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in program `update` method. " + ex.getMessage());
        }

    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (null != engine && !engine.getActive()) {
            return;
        }
        try {
            engine.preRender(g);
            engine.render();
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in program `render` method. " + ex.getMessage());
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
