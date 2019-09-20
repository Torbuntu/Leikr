package leikr.loaders;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.customProperties.CustomProgramProperties;
import leikr.Engine;
import leikr.GameRuntime;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.Compiler;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

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
            GameRuntime.GAME_NAME = GameRuntime.LAUNCH_TITLE;
        }
        if(GameRuntime.PACKAGED_ONLY){
            return getPackagedEngine();
        }
        if (cp.COMPILE_SOURCE) {
            compileEngine();
        }
        if (cp.USE_COMPILED) {
            return getCompiledEngine();
        }
        return getSourceEngine();
    }

    public Engine getPackagedEngine() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        gcl.addClasspath(Mdx.files.local("Programs/"+GameRuntime.GAME_NAME+".lkr").path());
        return (Engine) gcl.loadClass(GameRuntime.GAME_NAME).getDeclaredConstructors()[0].newInstance();
    }

    private Engine getSourceEngine() throws MalformedURLException, CompilationFailedException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        gcl.clearCache();
        gcl.addClasspath(rootPath);
        return (Engine) gcl.parseClass(new File(Mdx.files.local(rootPath + GameRuntime.GAME_NAME + ".groovy").path())).getDeclaredConstructors()[0].newInstance();//loads the game code  
    }

    private Engine getCompiledEngine() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        String COMPILED = rootPath + "Compiled/";
        gcl.addClasspath(COMPILED);
        return (Engine) gcl.loadClass(GameRuntime.GAME_NAME).getConstructors()[0].newInstance();
    }

    private void compileEngine() throws IOException {
        String COMPILED = rootPath + "Compiled/";
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(rootPath);
        if (!Mdx.files.local(COMPILED).exists()) {
            Mdx.files.local(COMPILED).mkdirs();
        }

        cc.setTargetDirectory(COMPILED);
        Compiler compiler = new Compiler(cc);

        FileHandle[] list = Mdx.files.local(rootPath).list(".groovy");
        ArrayList<String> files = new ArrayList<>();
        for (FileHandle f : list) {
            files.add(f.path());
        }
        String[] out = new String[files.size()];
        out = files.toArray(out);

        compiler.compile(out);
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
            String url = path.substring(0, path.lastIndexOf("/"));
            gcl.addClasspath(Mdx.files.local(GameRuntime.getProgramPath() + "/" + url).path());
            return gcl.parseClass(new File(Mdx.files.local(GameRuntime.getProgramPath() + "/" + path + ".groovy").path())).getDeclaredConstructors()[0].newInstance();
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
        String COMPILED = Mdx.files.local(output).toString();
        gcl.addClasspath(Mdx.files.local(COMPILED).path());

        String codePath = GameRuntime.getProgramPath() + "/" + path;

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(codePath);
        try {
            if (!Mdx.files.local(COMPILED).exists()) {
                Mdx.files.local(COMPILED).mkdirs();
            }

            cc.setTargetDirectory(COMPILED);
            Compiler compiler = new Compiler(cc);

            FileHandle[] list = Mdx.files.local(rootPath).list(".groovy");
            ArrayList<String> files = new ArrayList<>();
            for (FileHandle f : list) {
                files.add(f.path());
            }
            String[] fileNames = new String[files.size()];
            fileNames = files.toArray(fileNames);

            compiler.compile(fileNames);
        } catch (IOException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Adds a URL to the groovy class loader for the @newInstance(String name)
     * method to work
     *
     * @param path
     */
    public void loadLib(String path) {
        String COMPILED = GameRuntime.getProgramPath() + "/" + path + "/";
        gcl.addClasspath(Mdx.files.local(COMPILED).path());
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
     * Evaluates groovy source code
     *
     * @param code
     * @return Object result of the evaluation
     */
    public Object eval(String code) {
        try {
            return sh.evaluate(code);
        } catch (CompilationFailedException ex) {
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public Object parse(String code) {
        try {
            return sh.parse(code);
        } catch (CompilationFailedException ex) {
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
