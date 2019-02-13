/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    AssetManager assetManager;
    FitViewport viewport;
    Controller menuController;
    ControllerAdapter controllerAdapter;

    String[] gameList;

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        gameList = new File("./Programs").list();
        loadIcons();
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
        Gdx.app.log("INFO", "Game window changed to " + width + "x" + height);
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
    public void postTransitionIn(Transition transitionIn) {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int i) {
                if (i == Keys.UP && cursor > 0) {
                    cursor--;
                }
                if (i == Keys.DOWN && cursor < gameList.length - 1) {
                    cursor++;
                }
                if (i == Keys.ENTER) {
                    System.out.println("Loading game: " + gameList[cursor]);
                    START = true;
                    LOADING = true;
                }
                if (i == Keys.ESCAPE) {
                    System.out.println("Good bye!");
                    System.exit(0);
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
                            } else if (value == -1 && cursor > 0) {
                                cursor--;
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
            GameRuntime.setGamePath("./Programs/" + gameList[cursor]);
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
            g.drawTexture(assetManager.get("./Programs/" + gameList[cursor] + "/Art/icon.png"), ID, ID);
            g.drawString("Selection: " + gameList[cursor], 0, viewport.getHeight() - 9);
        } else {
            g.drawString("No game chips detected... ", 0, viewport.getHeight() - 9);
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
