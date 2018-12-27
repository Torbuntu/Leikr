/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import java.io.File;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.miniscript.core.GameScriptingEngine;
import org.mini2Dx.miniscript.core.ScriptBindings;

/**
 *
 * @author tor
 */
public class EngineScreen extends BasicGameScreen {

    public static int ID = 1;
    AssetManager assetManager;
    
    GameScriptingEngine scriptEngine;
    ScriptBindings scriptBindings;
    File dir;
    Engine engine;

    File libraryDir;
    String[] libraryList;
    String gameName;

    public void setEngines(Engine engine, GameScriptingEngine gsEngine){
        this.engine = engine;
        scriptEngine = gsEngine;
    }
    
    EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        
    }

    
    @Override
    public void initialise(GameContainer gc) {

    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        scriptEngine.update(delta);
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

}
