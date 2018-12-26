/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.miniscript.core.GameScriptingEngine;
import org.mini2Dx.miniscript.core.ScriptBindings;
import org.mini2Dx.miniscript.core.exception.InsufficientCompilersException;
import org.mini2Dx.miniscript.groovy.GroovyGameScriptingEngine;
import org.mini2Dx.miniscript.kotlin.KotlinGameScriptingEngine;
import org.mini2Dx.miniscript.lua.LuaGameScriptingEngine;
import org.mini2Dx.miniscript.python.PythonGameScriptingEngine;
import org.mini2Dx.miniscript.ruby.RubyGameScriptingEngine;

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

    EngineScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        
        File test = new File("./");
        System.out.println(Arrays.toString(test.list()));

        libraryDir = new File("./Games");
        libraryList = libraryDir.list();
        if (null != libraryList) {
            for (String file : libraryList) {
                System.out.println(file);
            }

            dir = new File("./Code/main.groovy");
        }
    }

    

    GameScriptingEngine getEngine() {
        Properties prop = new Properties();
        InputStream stream;
        try {
            stream = new FileInputStream(new File("./game.properties"));
            prop.load(stream);
            switch (prop.getProperty("runtime").toLowerCase()) {
                case "kotlin":
                    return new KotlinGameScriptingEngine();
                case "lua":
                    return new LuaGameScriptingEngine();
                case "python":
                    return new PythonGameScriptingEngine();
                case "ruby":
                    return new RubyGameScriptingEngine();
                case "groovy":
                default:
                    return new GroovyGameScriptingEngine();
            }
        } catch (IOException ex) {
            Logger.getLogger(GameRuntime.class.getName()).log(Level.SEVERE, null, ex);
        }
        // if all fails, return groovy scripting engine.
        return new GroovyGameScriptingEngine();
    }

    @Override
    public void initialise(GameContainer gc) {

        scriptEngine = getEngine();
        scriptBindings = new ScriptBindings();
        engine = new Engine();// null pointer if this isn't pre-initialized...
        scriptBindings.put("game", engine);
        try {
            int scriptId = scriptEngine.compileScript(new FileInputStream(dir));
            scriptEngine.invokeCompiledScriptLocally(scriptId, scriptBindings);
            engine = (Engine) scriptBindings.get("game");
            engine.init();
        } catch (InsufficientCompilersException | IOException ex) {
            Logger.getLogger(GameRuntime.class.getName()).log(Level.SEVERE, null, ex);
        }
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
