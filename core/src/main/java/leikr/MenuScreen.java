/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import leikr.loaders.FontLoader;
import leikr.loaders.EngineLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;

/**
 *
 * @author tor
 *
 */
public class MenuScreen extends BasicGameScreen implements InputProcessor {

    /* TODO: Make a graphical menu list to display games. This will take in a 
        This will take in a few items to use from the game's properties file.
        Such as:
            1. a custom icon which will be displayed in a selection box. 
            2. a Title
            3. a small one or two line description of the game.
            4?. a genre?
     */
    public static int ID = 0;
    public static String GAME_NAME;

    AssetManager assetManager;
    FontLoader fontLoader;
    BitmapFont font;


    boolean start = false;
    String[] gameList;
    int cursor;
    FitViewport viewport;

    MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(240, 160);
        gameList = new File("./Games").list();
        loadIcons();
    }

    private void loadIcons() {
        for (String game : gameList) {
            assetManager.load("./Games/" + game + "/Art/icon.png", Texture.class);
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
        fontLoader = new FontLoader();
        font = fontLoader.getFont();

        try {
            Controller menuController;
            Array<Controller> nmc = Controllers.getControllers();
            if (null != nmc.get(0)) {
                menuController = nmc.get(0);
                menuController.addListener(new ControllerAdapter() {
                    //10 up
                    //12 down
                    //1 A
                    //9 start
                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {
                        switch (buttonIndex) {
                            case 1:
                            case 9:
                                start = true;
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
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active. " + ex.getMessage());
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (start) {
            GAME_NAME = gameList[cursor];
            EngineScreen screen = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
            Engine engine = EngineLoader.getEngine();
            if (null != engine) {
                start = false;
                screen.setEngine(engine);
                sm.enterGameScreen(EngineScreen.ID, null, null);
                Gdx.input.setInputProcessor(screen);
            }
        }
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setFont(font);

        viewport.apply(g);
        g.setColor(Color.WHITE);
        if (null != gameList) {
            g.drawTexture(assetManager.get("./Games/" + gameList[cursor] + "/Art/icon.png"), ID, ID);
            int y = 14;
            int selec = 0;
            for (String file : gameList) {
                g.drawString(selec + ": " + file, 0, y);
                if (selec == cursor) {
                    g.drawString(" <=", 180, y);
                }
                selec++;
                y += 12;
            }
        }
        g.drawString("Selection: " + cursor, 0, viewport.getHeight() - 10);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean keyDown(int i) {
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        if (i == Keys.UP && cursor > 0) {
            cursor--;
        }
        if (i == Keys.DOWN && cursor < gameList.length - 1) {
            cursor++;
        }
        if (i == Keys.ENTER) {
            start = true;
        }
        if (i == Keys.ESCAPE) {
            System.out.println("Good bye!");
            System.exit(0);
        }
        System.out.println(gameList.length + " : " + (cursor + 1));
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return true;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return true;
    }

    @Override
    public boolean scrolled(int i) {
        return true;
    }
}
