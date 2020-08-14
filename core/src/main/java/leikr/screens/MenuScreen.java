/*
 * Copyright 2020 tor.
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

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.gdx.Input.Keys;

/**
 *
 * @author tor
 */
public class MenuScreen extends BasicGameScreen {

    public static int ID = 7;
    FitViewport viewport;
    FileHandle[] games;
    Texture icon;
    int index = 0;

    public MenuScreen(FitViewport vp) {
        viewport = vp;
    }

    @Override
    public void initialise(GameContainer gc) {
        try {
            games = Mdx.files.local("/Programs").list();

            icon = Mdx.graphics.newTexture(Mdx.files.internal("Data/Logo/logo-16x16.png"));
        } catch (IOException ex) {
            Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (Mdx.input.isKeyDown(Keys.ALT_LEFT) && Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyDown(Keys.T)) {
            sm.enterGameScreen(TerminalScreen.ID, null, null);
        }

        if (Mdx.input.isKeyJustPressed(Keys.LEFT) && index > 0) {
            index--;
        }
        if (Mdx.input.isKeyJustPressed(Keys.RIGHT) && index < games.length - 1) {
            index++;
        }
        if (Mdx.input.isKeyJustPressed(Keys.ENTER)) {
            GameRuntime.setGameName(games[index].nameWithoutExtension());
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }

        Controllers.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {

                if (buttonIndex == CustomSystemProperties.START) {
                    System.out.println(buttonIndex);

                    GameRuntime.setGameName(games[index].nameWithoutExtension());
                    Controllers.clearListeners();
                    sm.enterGameScreen(LoadScreen.ID, null, null);
                    return true;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisIndex, float value) {
                if (axisIndex == CustomSystemProperties.HORIZONTAL_AXIS) {
                    if ((int) value == CustomSystemProperties.RIGHT && index < games.length - 1) {
                        index++;
                        return true;
                    }
                    if ((int) value == CustomSystemProperties.LEFT && index > 0) {
                        index--;
                        return true;
                    }
                }
                return false;
            }

        });

    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setLineHeight(2);
        
        g.setColor(Colors.BLACK());
        g.fillRect(50, 50, 140, 60);

        g.setColor(Colors.GREEN());
        g.drawRect(50, 50, 140, 60);

        g.setColor(Colors.WHITE());
        g.drawTexture(icon, GameRuntime.WIDTH / 2 - 50, GameRuntime.HEIGHT / 2 - 8);
        g.drawString(games[index].nameWithoutExtension(), GameRuntime.WIDTH / 2, GameRuntime.HEIGHT / 2);

        g.setColor(Colors.YELLOW());
        g.drawString("> [INFO] This menu is a work in progress.", 0, 152);
    }

    @Override
    public int getId() {
        return ID;
    }

}
