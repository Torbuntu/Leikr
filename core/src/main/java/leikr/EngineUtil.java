/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.codehaus.groovy.control.CompilationFailedException;
import org.mini2Dx.miniscript.core.GameScriptingEngine;
import org.mini2Dx.miniscript.core.ScriptBindings;
import org.mini2Dx.miniscript.core.exception.InsufficientCompilersException;
import org.mini2Dx.miniscript.groovy.GroovyGameScriptingEngine;

/**
 *
 * @author tor
 */
public class EngineUtil {

    String type;
    GameScriptingEngine scriptEngine;
    Engine engine = new Engine(); // avoid null pointer

    Engine getEngine(String name) {
        GroovyClassLoader gcl = new GroovyClassLoader();
        try {
            Class game = gcl.parseClass(new File("./Games/" + name + "/Code/main.groovy"));//loads the game code  
            Constructor[] cnst = game.getConstructors();//gets the constructos
            engine = (Engine) cnst[0].newInstance();//instantiates based on first constructor
        } catch (SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException | CompilationFailedException | IOException | IllegalAccessException ex) {
        }
//
//        scriptEngine = getLocalScriptEngine(name);
//        ScriptBindings scriptBindings = new ScriptBindings();
//        scriptBindings.put("game", engine);
//        try {
//            int scriptId = scriptEngine.compileScript(new FileInputStream(new File("./Games/" + name + "/Code/main.groovy")));
//            scriptEngine.invokeCompiledScriptLocally(scriptId, scriptBindings);
//            engine = (Engine) scriptBindings.get("game");
//
//        } catch (InsufficientCompilersException | IOException ex) {
//            System.out.println(ex.getMessage());
//        }
        return engine;
    }

    private GameScriptingEngine getLocalScriptEngine(String name) {
        Properties prop = new Properties();
        try (InputStream stream = new FileInputStream(new File("./Games/" + name + "/game.properties"))) {
            prop.load(stream);
            switch (prop.getProperty("runtime").toLowerCase()) {
                case "groovy":
                default:
                    type = "groovy";
                    return new GroovyGameScriptingEngine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // if all fails, return groovy scripting engine.
        return new GroovyGameScriptingEngine();
    }

    GameScriptingEngine getScriptEngine() {
        return scriptEngine;
    }

}
