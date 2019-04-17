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

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovySystem;
import groovy.util.GroovyScriptEngine;
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
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.Compiler;

/**
 *
 * @author tor
 */
public class EngineLoader implements Callable{

    static GroovyClassLoader gcl = new GroovyClassLoader(ClassLoader.getSystemClassLoader());
    public static CustomProgramProperties cp;
    String rootPath;
    
    public EngineLoader(){
        rootPath = GameRuntime.getGamePath() + "/Code/";
    }

    //Returns either a pre-compiled game Engine, an Engine compiled from sources, or null. Returning Null helps the EngineScreen return to the MenuScreen.
    public Engine getEngine() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, ResourceException, ScriptException {
        cp = new CustomProgramProperties(GameRuntime.getGamePath());
        if (cp.USE_SCRIPT) {
            return getScriptedEngine();
        }
        if (cp.COMPILE_SOURCE) {
            compileEngine();
        }
        if (cp.USE_COMPILED) {
            return getCompiledEngine();
        }
        return getSourceEngine();
    }

    private Engine getSourceEngine() throws CompilationFailedException, IOException, InstantiationException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Arrays.asList(new File(rootPath).list()).stream()
                .filter(x -> !x.equals("main.groovy") && !x.equals("Compiled"))
                .forEach(path -> {
                    try {
                        gcl.parseClass(new File(rootPath + path));
                    } catch (CompilationFailedException | IOException ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
        return (Engine) gcl.parseClass(new File(rootPath+"main.groovy")).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getCompiledEngine() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        String COMPILED = rootPath + "Compiled/";
        gcl.addURL(new File(COMPILED).toURI().toURL());
        Arrays.asList(new File(COMPILED).list()).stream()
                .filter(x -> !x.equals(MenuScreen.getGameName() + ".class"))
                .forEach(classFile -> {
                    try {
                        gcl.loadClass(classFile.replace(".class", ""), false, true);
                    } catch (ClassNotFoundException | CompilationFailedException ex) {
                        Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
        return (Engine) gcl.loadClass(MenuScreen.getGameName()).getConstructors()[0].newInstance();
    }

    private void compileEngine() {
        String COMPILED = rootPath + "Compiled/";
        CompilerConfiguration cc = new CompilerConfiguration();
        if (!(new File(COMPILED).exists())) {
            new File(COMPILED).mkdir();
        }
        cc.setTargetDirectory(COMPILED);
        Compiler compiler = new Compiler(cc);

        Arrays.asList(new File(rootPath).list()).stream()
                .filter(x -> !x.equals("Compiled"))
                .forEach(path -> compiler.compile(new File(rootPath + path)));
    }

    public void destroy() {
        gcl.clearCache();
        for (Class<?> c : gcl.getLoadedClasses()) {
            GroovySystem.getMetaClassRegistry().removeMetaClass(c);
        }
    }

    //TODO: these are for testing
    private Engine getScriptedEngine() throws IOException, ResourceException, ScriptException {
        String[] paths = {rootPath};
        Binding bd = new Binding();
        GroovyScriptEngine gse = new GroovyScriptEngine(paths);
        return (Engine) gse.run("main.groovy", bd);
    }

    @Override
    public Object call() throws Exception {
        return getEngine();
    }
    
}
