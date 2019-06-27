package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 */
public class EngineScreen extends BasicGameScreen {

    public static int ID = 1;

    Engine engine;
    LeikrSystemManager system;
    static boolean BACK = false;
    static boolean ERROR = false;
    String errorMessage;
    AssetManager manager;

    public EngineScreen(AssetManager manager) {
        this.manager = manager;
    }

    public static void setBack(boolean setback) {
        BACK = setback;
    }

    void enterMenuScreen(ScreenManager sm) {
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.checkLaunchTitle()) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
    }

    void enterErrorScreen(ScreenManager sm) {
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.checkLaunchTitle()) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            ErrorScreen.setErrorMessage(errorMessage);
            sm.enterGameScreen(ErrorScreen.ID, null, null);
        }
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialise(GameContainer gc) {
        system = LeikrSystemManager.getLeikrSystemManager();
    }

    @Override
    public void postTransitionOut(Transition transition) {
        ERROR = false;
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
            engine.dispose();
            engine = null; // release all objects for gc
        }
        System.out.println("Engine classes disposed.");
    }

    @Override
    public void postTransitionIn(Transition transition) {
        Gdx.input.setCursorCatched(true);//remove cursor
        if (ERROR) {
            return;
        }
        try {
            system.setRunning(true);
            engine.preCreate(EngineLoader.getEngineLoader().cp.MAX_SPRITES, system);
            engine.create();
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (BACK) {
            system.resetFont();
            enterMenuScreen(sm);
        }
        if (ERROR) {
            system.resetFont();
            enterErrorScreen(sm);
            return;
        }
        if (!system.update(sm)) {
            system.resetFont();
            System.out.println("Transition initiated from running program.");
            return;
        }

        try {
            engine.preUpdate(delta);
            engine.update(delta);
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `update` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (null != engine && !engine.getActive() || ERROR) {
            return;
        }
        try {
            system.render(g);
            engine.preRender(g);
            engine.render();
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
