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
import java.util.Arrays;
import leikr.Engine;
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
public class EngineScreen extends BasicGameScreen {

    public static int ID = 1;
    AssetManager assetManager;

    Engine engine;
    public static boolean back = false;

    public EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    void switchScreen(ScreenManager sm) {
        back = false;
        engine.setActive(false);
        sm.enterGameScreen(MenuScreen.ID, null, null);
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
        Gdx.input.setInputProcessor(engine);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        
        if (back) {
            switchScreen(sm);
        }
        try {
            engine.preUpdate(delta);
            engine.update(delta);
        } catch (Exception ex) {
            back = true;
            System.out.println("Error in game `update` method. " + Arrays.toString(ex.getStackTrace()));
        }

    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (!engine.getActive()) {
            return;
        }
        try {
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
}
