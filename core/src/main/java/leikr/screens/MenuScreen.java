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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.customProperties.CustomProgramProperties;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.Texture;
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
public class MenuScreen extends BasicGameScreen {

    public static int ID = 7;
    private int index = 0;
    private String isCompiled = "";

    private List<CustomProgramProperties> games;

    private final FitViewport fitViewport;
    private final StretchViewport stretchViewport;
    private final GameRuntime runtime;
    private Texture icon;
    private FrameBuffer framebuffer;
    private final CustomSystemProperties customSystemProperties;

    public MenuScreen(CustomSystemProperties customSystemProperties, FitViewport vp, GameRuntime runtime) {
        this.customSystemProperties = customSystemProperties;
        this.runtime = runtime;
        fitViewport = vp;
        stretchViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
        try {
            games = new ArrayList<>();

            Arrays.asList(Mdx.files.local("/Programs").list()).stream().forEach(game -> {
                games.add(new CustomProgramProperties("Programs/" + game.nameWithoutExtension()));
            });

        } catch (IOException ex) {
            Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadIcon();
    }

    @Override
    public void preTransitionIn(Transition transition) {
        framebuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT);
    }

    @Override
    public void preTransitionOut(Transition transition) {
        framebuffer.dispose();
    }

    private void loadIcon() {
        try {
            icon = Mdx.graphics.newTexture(Mdx.files.internal("Programs/" + games.get(index).getTitle() + "/Art/icon.png"));
            if (Mdx.files.local("Programs/" + games.get(index).getTitle() + "/Code/Compiled").exists()) {
                isCompiled = " *";
                if (games.get(index).getUseCompiled()) {
                    isCompiled = " **";
                }
            } else {
                isCompiled = "";
            }
        } catch (Exception ex) {
            icon = Mdx.graphics.newTexture(Mdx.files.internal("Data/Logo/logo-32x32.png"));
            Logger.getLogger(MenuScreen.class.getName()).log(Level.WARNING, "No icon file for: {0}", games.get(index).getTitle());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (Mdx.input.isKeyDown(Keys.ALT_LEFT) && Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyDown(Keys.T)) {
            sm.enterGameScreen(TerminalScreen.ID, null, null);
        }

        if (Mdx.input.isKeyJustPressed(Keys.LEFT) && index > 0) {
            index--;
            loadIcon();
        }
        if (Mdx.input.isKeyJustPressed(Keys.RIGHT) && index < games.size() - 1) {
            index++;
            loadIcon();
        }
        if (Mdx.input.isKeyJustPressed(Keys.ENTER)) {
            LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
            ls.setGameName(games.get(index).getTitle());
            runtime.setGameName(games.get(index).getTitle());
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }

        if (runtime.checkFileDropped()) {
            LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
            ls.setGameName(runtime.getFileDroppedTitle());
            runtime.setGameName(runtime.getFileDroppedTitle());
            runtime.clearFileDropped();
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }

        Controllers.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {

                if (buttonIndex == customSystemProperties.getStart()) {
                    System.out.println(buttonIndex);

                    LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
                    ls.setGameName(games.get(index).getTitle());
                    runtime.setGameName(games.get(index).getTitle());
                    Controllers.clearListeners();
                    sm.enterGameScreen(LoadScreen.ID, null, null);
                    return true;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisIndex, float value) {
                if (axisIndex == customSystemProperties.getHorizontalAxis()) {
                    if ((int) value == customSystemProperties.getRight() && index < games.size() - 1) {
                        index++;
                        return true;
                    }
                    if ((int) value == customSystemProperties.getLeft() && index > 0) {
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
        stretchViewport.apply(g);
        framebuffer.begin();
        g.clearContext();
        // draw texture in background
        g.drawTexture(icon, 0, 0, 240, 160);

        g.setColor(Colors.BLACK());
        g.fillRect(0, 0, 240, 10);

        g.setColor(Colors.GREEN());
        g.drawLineSegment(0, 10, 240, 10);

        g.setColor(Colors.WHITE());

        g.drawString(games.get(index).getTitle() + isCompiled, 0, 3, 240, 1);

        g.setColor(Colors.YELLOW());
        g.drawString("> [INFO] This menu is a work in progress.", 0, 152);
        g.flush();
        framebuffer.end();

        fitViewport.apply(g);
        g.drawTexture(framebuffer.getTexture(), 0, 0, false);
    }

    @Override
    public int getId() {
        return ID;
    }

}
