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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 *
 */
public class MenuScreen extends BasicGameScreen {

    /* TODO: Make a graphical menu list to display games. This will take in a 
        This will take in a few items to use from the game's properties file.
        Such as:
            1. a custom icon which will be displayed in a selection box. 
            2. a Title
            3. a small one or two line description of the game.
            4?. a genre?
     */
    public static int ID = 0;
    static boolean LOADING = false;
    boolean START = false;
    int cursor;
    public static String GAME_NAME;
    private static final String ICON_PATH = "/Art/icon.png";

    AssetManager assetManager;
    FitViewport viewport;
    String[] gameList;

    ExecutorService service;
    Future engineGetter;

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        gameList = new File(GameRuntime.PROGRAM_PATH).list();
        setGameName(gameList[0]);
        loadIcons();
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    private void setGameName(String GAME_NAME) {
        MenuScreen.GAME_NAME = GAME_NAME;
    }

    public static void finishLoading() {
        LOADING = false;
    }

    private void loadIcons() {
        for (String game : gameList) {
            assetManager.load(GameRuntime.PROGRAM_PATH + game + ICON_PATH, Texture.class);
        }
        assetManager.finishLoading();
    }

    private void loadProgram() {
        service = Executors.newFixedThreadPool(5);
        GameRuntime.setGamePath("Programs/" + getGameName());
        engineGetter = service.submit(new EngineLoader());
    }

    @Override
    public void onResize(int width, int height) {
        viewport.onResize(width, height);
    }

    @Override
    public void initialise(GameContainer gc) {
        cursor = 0;
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            START = true;
            LOADING = true;
        }
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            return;
        }
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int i) {
                if (i == Keys.UP && cursor > 0) {
                    cursor--;
                    setGameName(gameList[cursor]);
                }
                if (i == Keys.DOWN && cursor < gameList.length - 1) {
                    cursor++;
                    setGameName(gameList[cursor]);
                }
                if (i == Keys.ENTER) {
                    System.out.println("Loading program: " + getGameName());
                    loadProgram();
                    LOADING = true;
                }
                if (i == Keys.ESCAPE) {
                    System.out.println("Good bye!");
                    Gdx.app.exit();
                }
                return false;
            }
        });
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(new ControllerAdapter() {
                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {
                        if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A) {
                            loadProgram();
                            LOADING = true;
                        }
                        return false;
                    }

                    @Override
                    public boolean axisMoved(Controller controller, int axisCode, float value) {
                        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
                            if (value == 1 && cursor < gameList.length - 1) {
                                cursor++;
                                setGameName(gameList[cursor]);
                            } else if (value == -1 && cursor > 0) {
                                cursor--;
                                setGameName(gameList[cursor]);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Menu Screen. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (START) {
            START = false;
            EngineScreen scrn = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
            try {
                scrn.setEngine((Engine) engineGetter.get());
                sm.enterGameScreen(EngineScreen.ID, null, null);

            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.WHITE);
        viewport.apply(g);
        if (LOADING) {
            if (engineGetter.isDone()) {
                g.drawString("Finished!", 0, viewport.getHeight() - 9);
                START = true;
            } else {
                g.setColor(Color.MAGENTA);
                g.drawCircle(120, 80, 15);
                g.drawString("Loading... ", 0, viewport.getHeight() - 9);
            }
        } else if (null != gameList) {
            g.drawTexture(assetManager.get(GameRuntime.PROGRAM_PATH + getGameName() + ICON_PATH, Texture.class), ID, ID);
            g.drawString("Selection: " + getGameName(), 0, viewport.getHeight() - 9);
        } else {
            g.drawString("No programs detected... ", 0, viewport.getHeight() - 9);
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
