/*
 * Copyright 2019 torbuntu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    public static Engine getEngine() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        GroovyClassLoader gcl = new GroovyClassLoader();
        CustomProperties cp = new CustomProperties(GameRuntime.getGamePath());
        String rootPath = GameRuntime.getGamePath() + "/Code/";
        String[] codes = new File(rootPath).list();
        for (String path : codes) {
            if (!path.equals("main.groovy")) {
                gcl.parseClass(new File(rootPath + path));
            }
        }
        Class game = gcl.parseClass(new File(rootPath + "main.groovy"));//loads the game code  
        Constructor[] cnst = game.getConstructors();//gets the constructos
        Engine engine = (Engine) cnst[0].newInstance();//instantiates based on first constructor
        engine.preCreate(cp.MAX_SPRITES);//pre create here to instantiate objects
        return engine;
    }

}
