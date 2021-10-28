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
package leikr.screens

import leikr.GameRuntime
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.gdx.Input.Keys
/**
 *
 * @author tor
 */
class ErrorScreen extends BasicGameScreen {

    public static int ID = 3
    private boolean MENU = false
    private String errorMessage
    private final FitViewport viewport
    private final GameRuntime runtime

    ErrorScreen(FitViewport vp, GameRuntime runtime) {
        viewport = vp
        errorMessage = ""
        this.runtime = runtime
    }

    void setErrorMessage(String message) {
        errorMessage = message
    }

    static void reloadEngine(ScreenManager sm) {
        sm.enterGameScreen(LoadScreen.ID, null, null)
    }

    @Override
    void preTransitionIn(Transition transitionIn) {
    }

    @Override
    void preTransitionOut(Transition transitionOut) {
    }

    @Override
    void initialise(GameContainer gc) {
    }

    @Override
    void update(GameContainer gc, ScreenManager sm, float f) {
        if (MENU || Mdx.input.isKeyJustPressed(Keys.ESCAPE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.Q)) {
            MENU = false
            if (runtime.isDevMode()) {
                sm.enterGameScreen(TerminalScreen.ID, null, null)
            } else {
                sm.enterGameScreen(MenuScreen.ID, null, null)
            }
        }

        if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyJustPressed(Keys.R) || Mdx.input.isKeyJustPressed(Keys.F5)) {
            reloadEngine(sm)
        }

    }

    @Override
    void interpolate(GameContainer gc, float f) {
    }

    @Override
    void render(GameContainer gc, Graphics g) {
        viewport.apply(g)
        g.with{
			setColor(Colors.RED())
            drawString("Message:  " + errorMessage, 0, 0, 232)
            setColor(Colors.BLACK())
            drawRect(0, 152, runtime.WIDTH, 8)
            setColor(Colors.GREEN())
            drawString(":q to quit", 0, 152)
        }
    }

    @Override
    int getId() {
        ID
    }
}
