/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import leikr.loaders.EngineLoader;
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
public class EngineScreen extends BasicGameScreen implements InputProcessor {

    public static int ID = 1;
    AssetManager assetManager;

    Engine engine;
    boolean back = false;

    EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void onResize(int width, int height) {
    }

    @Override
    public void preTransitionOut(Transition transition) {
        engine.dispose();
        System.out.println("Game engine classes disposed.");
    }

    @Override
    public void preTransitionIn(Transition transition) {
        try {
            engine = EngineLoader.getEngine();
            setEngine(engine);
        } catch (Exception ex) {
            back = true;
            System.out.println("Error parsing game class. " + ex.getMessage());
        }
    }

    @Override
    public void postTransitionIn(Transition transition) {
        MenuScreen.finishLoading();
        try {
            engine.create();
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in game `create` method. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (back) {
            back = false;
            engine.active = false;
            sm.enterGameScreen(MenuScreen.ID, null, null);
            Gdx.input.setInputProcessor((MenuScreen) sm.getGameScreen(MenuScreen.ID));
            return;
        }
        try {
            engine.preUpdate(delta);
            engine.update(delta);
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in game `update` method. " + ex.getMessage());
        }

    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (!engine.active) {
            return;
        }
        try {
            engine.viewport.apply(g);
            engine.preRender(g);
            engine.render();
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in game `render` method. " + ex.getMessage());
        }
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
        if (i == Keys.ESCAPE) {
            back = true;
        }
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
