package leikr.loaders;

import com.badlogic.gdx.Gdx;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
    private GroovyShell sh;
    String rootPath;

    private static EngineLoader instance;

    public static EngineLoader getEngineLoader(boolean reload) {
        if (instance == null) {
            instance = new EngineLoader();
        }
        if (reload) {
            instance.reset();
        }
        return instance;
    }

    private void reset() {
        destroy();
        rootPath = GameRuntime.getProgramPath() + "/Code/";
        cp = new CustomProgramProperties(GameRuntime.getProgramPath());
        gcl = new GroovyClassLoader(ClassLoader.getSystemClassLoader());
        sh = new GroovyShell(gcl);
    }

    /**
     * Returns either a pre-compiled game Engine, an Engine compiled from
     * sources, or null.Returning Null helps the EngineScreen return to the
     * MenuScreen.
     *
     * @return Engine object
     * @throws java.io.IOException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws groovy.util.ResourceException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.lang.ClassNotFoundException
     * @throws groovy.util.ScriptException
     */
    public Engine getEngine() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, ResourceException, ScriptException {
        if (GameRuntime.checkLaunchTitle()) {
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
        return (Engine) gcl.parseClass(Gdx.files.local(rootPath + MenuScreen.GAME_NAME + ".groovy").file()).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getJavaSourceEngine() throws MalformedURLException, CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        gcl.clearCache();
        gcl.addURL(new File(rootPath).toURI().toURL());
        return (Engine) gcl.parseClass(Gdx.files.local(rootPath + MenuScreen.GAME_NAME + ".java").file()).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getCompiledEngine() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        String COMPILED = rootPath + "Compiled/";
        gcl.addURL(new File(COMPILED).toURI().toURL());
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

    //START API
    /**
     * compiles a groovy class file and tries to return an object instance
     *
     * @param path to groovy file
     * @return either a new instance of the class in the path file, or -1 on
     * fail
     */
    public Object compile(String path) {
        try {
            return gcl.parseClass(Gdx.files.local(GameRuntime.getProgramPath() + "/" + path + ".groovy").file()).getDeclaredConstructors()[0].newInstance();
        } catch (CompilationFailedException | IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Compiles groovy sources in a the path input and puts the class files in
     * the out directory
     *
     * @param path
     * @param out
     */
    public void compile(String path, String out) {
        String output = GameRuntime.getProgramPath() + "/" + out;
        String COMPILED = Gdx.files.local(output).toString();
        try {
            gcl.addURL(new File(COMPILED).toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        String codePath = GameRuntime.getProgramPath() + "/" + path;

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(codePath);
        if (!(new File(COMPILED).exists())) {
            new File(COMPILED).mkdir();
        }
        cc.setTargetDirectory(COMPILED);
        Compiler compiler = new Compiler(cc);
        File[] files = ArrayUtils.removeElement(new File(codePath).listFiles(), new File(codePath + out));
        compiler.compile(files);
    }

    /**
     * Adds a URL to the groovy class loader for the @newInstance(String name)
     * method to work
     *
     * @param path
     */
    public void loadLib(String path) {
        String COMPILED = GameRuntime.getProgramPath() + "/" + path + "/";
        try {
            gcl.addURL(new File(COMPILED).toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Instantiates an object from loaded lib classes
     *
     * @param name
     * @return the new object instantiated
     */
    public Object newInstance(String name) {
        try {
            return gcl.loadClass(name).getDeclaredConstructors()[0].newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Evaluates a groovy source code file or source in text, returning the
     * result.
     *
     * @param code
     * @param opt
     * @return
     */
    public Object eval(String code, int opt) {
        try {
            switch (opt) {
                case 0:
                    return sh.evaluate(code);
                case 1:
                    return sh.evaluate(Gdx.files.local(GameRuntime.getProgramPath() + "/" + code + ".groovy").file());
                default:
                    return sh.evaluate(code);
            }
        } catch (CompilationFailedException | IllegalArgumentException | IOException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Unlike @eval(String code, int opt) this method only takes source code
     *
     * @param code
     * @return
     */
    public Object eval(String code) {
        try {
            return sh.evaluate(code);
        } catch (CompilationFailedException | IllegalArgumentException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //END API
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
