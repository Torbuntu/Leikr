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

import com.badlogic.gdx.Gdx;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovySystem;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.customProperties.CustomProgramProperties;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.screens.MenuScreen;
import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.Compiler;

/**
 *
 * @author tor
 */
public class EngineLoader implements Callable<Engine> {

    public GroovyClassLoader gcl;
    public CustomProgramProperties cp;
    String rootPath;

    private static EngineLoader instance;

    public static EngineLoader getEngineLoader() {
        if (instance == null) {
            instance = new EngineLoader();
        }
        instance.reset();
        return instance;
    }

    private void reset() {
        destroy();
        rootPath = GameRuntime.getProgramPath() + "/Code/";
        cp = new CustomProgramProperties(GameRuntime.getProgramPath());
        gcl = new GroovyClassLoader(ClassLoader.getSystemClassLoader());
    }

    //Returns either a pre-compiled game Engine, an Engine compiled from sources, or null. Returning Null helps the EngineScreen return to the MenuScreen.
    public Engine getEngine() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, ResourceException, ScriptException {
        if(GameRuntime.checkLaunchTitle()){
            MenuScreen.GAME_NAME = GameRuntime.LAUNCH_TITLE;
        }
        if (cp.COMPILE_SOURCE) {
            compileEngine();
        }
        if (cp.USE_COMPILED) {
            return getCompiledEngine();
        }
        if (cp.JAVA_ENGINE) {
            return getJavaSourceEngine();
        }

        return getSourceEngine();
    }

    private Engine getSourceEngine() throws MalformedURLException, CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        gcl.clearCache();
        gcl.addURL(new File(rootPath).toURI().toURL());
        return (Engine) gcl.parseClass(Gdx.files.internal(rootPath + MenuScreen.GAME_NAME + ".groovy").file()).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getJavaSourceEngine() throws MalformedURLException, CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        gcl.clearCache();
        gcl.addURL(new File(rootPath).toURI().toURL());
        return (Engine) gcl.parseClass(Gdx.files.internal(rootPath + MenuScreen.GAME_NAME + ".java").file()).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getCompiledEngine() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        String COMPILED = rootPath + "Compiled/";
        gcl.addURL(new File(COMPILED).toURI().toURL());
        Arrays.asList(Gdx.files.local(COMPILED).list()).stream()
                .filter(x -> !x.name().equals(MenuScreen.GAME_NAME + ".class"))
                .forEach(classFile -> {
                    try {
                        gcl.loadClass(classFile.name().replace(".class", ""), false, true);
                    } catch (ClassNotFoundException | CompilationFailedException ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
        return (Engine) gcl.loadClass(MenuScreen.GAME_NAME).getConstructors()[0].newInstance();
    }

    private void compileEngine() {
        String COMPILED = rootPath + "Compiled/";
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(rootPath);
        if (!(new File(COMPILED).exists())) {
            new File(COMPILED).mkdir();
        }
        cc.setTargetDirectory(COMPILED);
        Compiler compiler = new Compiler(cc);

        File[] files = ArrayUtils.removeElement(new File(rootPath).listFiles(), new File(rootPath + "Compiled"));
        compiler.compile(files);
    }

    /**
     * destroy
     *
     * Clears the class loader cache and attempts to clear the
     * metaClassRegistry. This is a testing method and may be removed.
     */
    public void destroy() {
        if (null != gcl) {
            gcl.clearCache();
            for (Class<?> c : gcl.getLoadedClasses()) {
                GroovySystem.getMetaClassRegistry().removeMetaClass(c);
            }
        }
    }

    /**
     * call()
     * 
     * This method is used by the ExecutorService to spawn a new thread to load
     * the Engine object async. 
     * 
     * @return The Engine object for running the loaded Leikr program
     * @throws Exception 
     */
    @Override
    public Engine call() throws Exception {
        return getEngine();
    }
}
