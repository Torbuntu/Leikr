package leikr.managers;

import com.badlogic.gdx.assets.AssetManager;
import leikr.GameRuntime;
import leikr.screens.LoadScreen;
import leikr.screens.MenuScreen;
import org.mini2Dx.core.font.MonospaceFont;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.ScreenManager;

/**
 *
 * @author tor
 */
public class LeikrSystemManager {

    private boolean LOAD_PROGRAM = false;
    private boolean RUNNING = true;
    AssetManager manager;
    MonospaceFont font;
    
    private static LeikrSystemManager instance;

    public LeikrSystemManager() {
        this.manager = new AssetManager();
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
        manager.clear();
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
