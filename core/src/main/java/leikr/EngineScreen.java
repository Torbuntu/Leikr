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

    public void setEngines(Engine engine) {
        this.engine = engine;
    }

    EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void postTransitionIn(Transition transition) {
        engine.preCreate();
        engine.create();
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (back) {
            sm.enterGameScreen(MenuScreen.ID, null, null);
            Gdx.input.setInputProcessor((MenuScreen) sm.getGameScreen(MenuScreen.ID));
            back = false;
        }
        engine.update();
        engine.update(delta);
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        engine.preRender(g);
        engine.render();
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
