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
import com.badlogic.gdx.utils.Array;
import java.io.File;
import leikr.GameRuntime;
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

    AssetManager assetManager;
    FitViewport viewport;
    Controller menuController;
    ControllerAdapter controllerAdapter;

    String[] gameList;

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        gameList = new File(Gdx.files.getLocalStoragePath()+"Programs").list();
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
            assetManager.load("./Programs/" + game + "/Art/icon.png", Texture.class);
        }
        assetManager.finishLoading();
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
    public void preTransitionOut(Transition transitionOut) {
        if (null != menuController) {
            menuController.removeListener(controllerAdapter);
        }
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
                    START = true;
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
            Array<Controller> nmc = Controllers.getControllers();
            if (null != nmc.get(0)) {
                menuController = nmc.get(0);
                controllerAdapter = new ControllerAdapter() {
                    //10 up
                    //12 down
                    //1 A
                    //9 start
                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {
                        switch (buttonIndex) {
                            case 1:
                            case 9:
                                START = true;
                                LOADING = true;
                                break;
                        }
                        return false;
                    }

                    @Override
                    public boolean axisMoved(Controller controller, int axisCode, float value) {
                        if (axisCode == 1) {
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
                };
                menuController.addListener(controllerAdapter);

            }
        } catch (Exception ex) {
            System.out.println("No controllers active. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (START) {
            START = false;
            GameRuntime.setGamePath("Programs/" + getGameName());
            sm.enterGameScreen(EngineScreen.ID, null, null);
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
            g.setColor(Color.MAGENTA);
            g.drawCircle(120, 80, 15);
            g.drawString("Loading... ", 0, viewport.getHeight() - 9);
        } else if (null != gameList) {
            g.drawTexture(assetManager.get("./Programs/" + getGameName() + "/Art/icon.png"), ID, ID);
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
