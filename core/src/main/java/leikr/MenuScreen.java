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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.io.File;
import java.util.Arrays;
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

    AssetManager assetManager;
    FontLoader fontLoader;
    BitmapFont font;

    public static String GAME_NAME;

    boolean start = false;
    String[] gameList;
    int cursor;
    FitViewport viewport;

    MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(240, 160);
    }

    @Override
    public void onResize(int width, int height) {
        Gdx.app.log("INFO", "Game window changed to " + width + "x" + height);
        viewport.onResize(width, height);
    }

    @Override
    public void initialise(GameContainer gc) {
        cursor = 0;
        File test = new File("./");
        System.out.println(Arrays.toString(test.list()));

        fontLoader = new FontLoader();
        font = fontLoader.getFont();

        gameList = new File("./Games").list();
        if (null != gameList) {
            for (String file : gameList) {
                System.out.println(file);
            }
        }
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (start) {
            GAME_NAME = gameList[cursor];
            EngineScreen screen = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
            Engine engine = EngineLoader.getEngine(GAME_NAME);
            if (null != engine) {
                screen.setEngine(EngineLoader.getEngine(GAME_NAME));
                sm.enterGameScreen(EngineScreen.ID, null, null);
                Gdx.input.setInputProcessor(screen);
                start = false;
            }else{
                start = false;
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
        g.drawString("Selection: " + cursor, 0, viewport.getHeight() - 16);
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
