/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.loaders;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import leikr.CustomProperties;
import leikr.Engine;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class EngineLoader {

    //TODO: Add support for more than one code file?
    public static Engine getEngine(String name) {
        Engine engine = null;
        GroovyClassLoader gcl = new GroovyClassLoader();
        CustomProperties cp = new CustomProperties(name);
        try {
            Class game = gcl.parseClass(new File("./Games/" + name + "/Code/main.groovy"));//loads the game code  
            Constructor[] cnst = game.getConstructors();//gets the constructos
            engine = (Engine) cnst[0].newInstance();//instantiates based on first constructor
            engine.preCreate(cp.MAX_SPRITES, cp.MAX_SPRITE_SHEETS);//pre create here to instantiate objects
        } catch (IOException | IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException | CompilationFailedException ex) {
            System.out.println(ex.getMessage());
            return engine;
        }
        return engine;
    }

}
