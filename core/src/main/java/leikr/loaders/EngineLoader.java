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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import leikr.CustomProgramProperties;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.screens.MenuScreen;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.Compiler;

/**
 *
 * @author tor
 */
public class EngineLoader {

    //TODO: Add support for more than one code file?
    public static Engine getEngine() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        GroovyClassLoader gcl = new GroovyClassLoader();
        CustomProgramProperties cp = new CustomProgramProperties(GameRuntime.getGamePath());
        String rootPath = GameRuntime.getGamePath() + "/Code/";

        if (cp.COMPILE_SOURCE) {
            compileEngine(rootPath);
        }
        if (cp.USE_COMPILED) {
            return getCompiledEngine(rootPath, gcl, cp);
        }
        return getSourceEngine(rootPath, gcl, cp);
    }

    private static Engine getSourceEngine(String rootPath, GroovyClassLoader gcl, CustomProgramProperties cp) {
        try {
            String[] codes = new File(rootPath).list();
            if (codes.length > 1) {
                for (String path : codes) {
                    if (!path.equals("main.groovy") && !path.equals("Compiled")) {
                        gcl.parseClass(new File(rootPath + path));
                    }
                }
            }
            Engine engine = (Engine) gcl.parseClass(new File(rootPath + "main.groovy")).getConstructors()[0].newInstance();//loads the game code  
            engine.preCreate(cp.MAX_SPRITES);//pre create here to instantiate objects
            return engine;
        } catch (Exception ex) {
            System.out.println("Failed to parse and load program sources.");
            ex.printStackTrace();
            return null;
        }
    }

    private static Engine getCompiledEngine(String rootPath, GroovyClassLoader gcl, CustomProgramProperties cp) {
        try {
            gcl.addURL(new File(rootPath.substring(2, rootPath.length()) + "Compiled/").toURI().toURL());
            for (String classFile : new File(rootPath + "Compiled/").list()) {
                if (!classFile.equals(MenuScreen.getGAME_NAME() + ".class")) {
                    gcl.loadClass(classFile.replace(".class", ""));
                }
            }
            Engine engine = (Engine) gcl.loadClass(MenuScreen.getGAME_NAME()).getConstructors()[0].newInstance();
            engine.preCreate(cp.MAX_SPRITES);
            return engine;
        } catch (Exception ex) {
            System.out.println("Failed to collect compiled sources.");
            ex.printStackTrace();
            return null;

        }
    }

    private static void compileEngine(String rootPath) {
        try {
            CompilerConfiguration cc = new CompilerConfiguration();
            if (!(new File(rootPath + "Compiled/").exists())) {
                new File(rootPath + "Compiled/").mkdir();
            }
            cc.setTargetDirectory(rootPath + "Compiled/");
            Compiler cp = new Compiler(cc);
            for (String path : new File(rootPath).list()) {
                if (!path.equals("Compiled")) {
                    cp.compile(new File(rootPath + path));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
