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
import leikr.GameRuntime;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 *
 * @author tor
 */
public class EngineLoader {

    //TODO: Add support for more than one code file?
    public static Engine getEngine() {
        Engine engine = null;
        GroovyClassLoader gcl = new GroovyClassLoader();
        CustomProperties cp = new CustomProperties(GameRuntime.GAME_NAME);
        String rootPath = GameRuntime.getGamePath() + "/Code/";
        String[] codes = new File(rootPath).list();
        try {            
            for(String path : codes){
                if(!path.equals("main.groovy")){
                    gcl.parseClass(new File(rootPath+path));                
                }
            }
            Class game = gcl.parseClass(new File(rootPath + "main.groovy"));//loads the game code  
            Constructor[] cnst = game.getConstructors();//gets the constructos
            engine = (Engine) cnst[0].newInstance();//instantiates based on first constructor
            engine.preCreate(cp.MAX_SPRITES);//pre create here to instantiate objects
        } catch (IOException | IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException | CompilationFailedException ex) {
            System.out.println(ex.getMessage());
            return engine;
        }
        return engine;
    }

}
