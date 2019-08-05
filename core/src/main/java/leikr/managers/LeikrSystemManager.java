package leikr.managers;

import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.loaders.SpriteLoader;
import leikr.screens.EngineScreen;
import leikr.screens.LoadScreen;
import leikr.screens.MenuScreen;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.LocalFileHandleResolver;
import org.mini2Dx.core.screen.ScreenManager;

/**
 *
 * @author tor
 */
public class LeikrSystemManager {

    private boolean LOAD_PROGRAM = false;
    private boolean RUNNING = true;
    AssetManager manager;
    MonospaceGameFont font;
    
    private static LeikrSystemManager instance;

    public LeikrSystemManager() {
        this.manager = new AssetManager(new LocalFileHandleResolver());
        font = GameRuntime.primaryFontLoader.getDefaultFont();
    }

    public static LeikrSystemManager getLeikrSystemManager() {
        if (instance == null) {
            instance = new LeikrSystemManager();
        }
        instance.reset();
        return instance;
    }
    
    private void reset(){
        manager.clearAssetLoaders();
    }

    //START API
    /**
     * resetFont
     *
     * after a program is finished running, this should be called to reset the
     * font to the default loaded system font.
     */
    public void resetFont() {
        font = GameRuntime.primaryFontLoader.getDefaultFont();
    }

    public void setCustomFont(String fontPath, int spacing, int width, int height) {
        font = GameRuntime.primaryFontLoader.getCustomFont(manager, fontPath, spacing, width, height);
    }

    public void loadProgram(String name) {
        GameRuntime.setProgramPath("Programs/" + name);
        MenuScreen.GAME_NAME = name;
        LOAD_PROGRAM = true;
    }
    
    /**
     * A kind of hacky entry to the EngineScreen PAUSE boolean.
     */
    public void pause(){
        EngineScreen.PAUSE = true;
    }
    
    //START EngineLoader API
    public void loadSpriteSheet(String sheetName){
        SpriteLoader sl = SpriteLoader.getSpriteLoader();
        sl.loadManualSpritesheets(sheetName);        
    }
    
    public Object compile(String path){
        return EngineLoader.getEngineLoader(false).compile(path);
    }
    public void compile(String path, String out){
        EngineLoader.getEngineLoader(false).compile(path, out);
    }
    public Object eval(String code, int opt){
        return EngineLoader.getEngineLoader(false).eval(code, opt);
    }
    public Object eval(String code){
        return EngineLoader.getEngineLoader(false).eval(code);
    }
    public void loadLib(String path){
        EngineLoader.getEngineLoader(false).loadLib(path);
    }
    public Object newInstance(String name){
        return EngineLoader.getEngineLoader(false).newInstance(name);
    }
    //END EngineLoader API
    
    //END API

    //START game loop methods on EngineScreen
    public boolean update(ScreenManager sm) {
        if (LOAD_PROGRAM) {
            LOAD_PROGRAM = false;
            RUNNING = false;
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }
        font.load(manager);
        return RUNNING;
    }

    public void render(Graphics g) {
        g.setFont(font);
    }

    public void setRunning(boolean run) {
        RUNNING = run;
    }
    //END game loop methods on EngineScreen
}
