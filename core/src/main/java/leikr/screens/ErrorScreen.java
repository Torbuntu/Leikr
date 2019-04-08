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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

/**
 *
 * @author tor
 */
public class ErrorScreen extends BasicGameScreen {

    public static int ID = 4;
    AssetManager assetManager;
    FitViewport viewport;
    boolean MENU = false;
    static String errorMessage;

    public ErrorScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(240, 160);
        errorMessage = "";
    }

    public static void setErrorMessage(String message) {
        errorMessage = message;
    }

    void checkInput(ScreenManager sm) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER) || MENU || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            sm.enterGameScreen(MenuScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
        }
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        MENU = false;
    }

    @Override
    public void initialise(GameContainer gc) {
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controller menuController = Controllers.getControllers().get(0);
                menuController.addListener(new ControllerAdapter() {
                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {
                        MENU = true;
                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Error Screen. " + ex.getMessage());
        }
    }

    @Override
    public void onResize(int width, int height) {
        viewport.onResize(width, height);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        checkInput(sm);
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Color.RED);
        g.drawString("Message:  " + errorMessage, 0, 0, 232);
    }

    @Override
    public int getId() {
        return ID;
    }
}
