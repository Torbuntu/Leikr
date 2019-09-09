package leikr.screens;

import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;

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
    FitViewport viewport;

    public static boolean PAUSE = false;
    private static boolean CONFIRM = false;

    public EngineScreen() {
    }

    private void pause() {
        PAUSE = true;
        engine.audio.pauseAllAudio();
        engine.onPause();
    }

    private void resume() {
        if (CONFIRM) {
            BACK = true;
        } else {
            PAUSE = false;
            engine.audio.resumeAllAudio();
            engine.onResume();
        }
    }

    void enterMenuScreen(ScreenManager sm) {
        BACK = false;
        if (null != engine) {
            engine.setActive(false);
        }
        if (GameRuntime.checkLaunchTitle()) {
            sm.enterGameScreen(TitleScreen.ID, null, null);
        } else {
            sm.enterGameScreen(TerminalScreen.ID, null, null);
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

    void reloadEngine(ScreenManager sm) {
        sm.enterGameScreen(LoadScreen.ID, null, null);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialise(GameContainer gc) {
        system = LeikrSystemManager.getLeikrSystemManager();
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
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
    public void preTransitionIn(Transition trans) {
        PAUSE = false;
    }

    @Override
    public void postTransitionIn(Transition transition) {
        if (ERROR) {
            return;
        }
        try {
            system.setRunning(true);
            engine.preCreate(EngineLoader.getEngineLoader(false).cp.MAX_SPRITES, system);
            engine.create();
        } catch (Exception ex) {
            ERROR = true;
            errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage();
            Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (Mdx.input.isKeyJustPressed(Keys.F5) || Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyJustPressed(Keys.R)) {
            reloadEngine(sm);
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (PAUSE) {
                resume();
            } else {
                pause();
            }
        }
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

        if (!PAUSE) {
            try {
                if (engine.preUpdate(delta)) {
                    pause();
                }
                engine.update(delta);
            } catch (Exception ex) {
                ERROR = true;
                errorMessage = "Error in program `update` method. " + ex.getLocalizedMessage();
                Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (Mdx.input.isKeyJustPressed(Keys.LEFT)) {
                CONFIRM = true;
            }
            if (Mdx.input.isKeyJustPressed(Keys.RIGHT)) {
                CONFIRM = false;
            }
            if (Mdx.input.isKeyJustPressed(Keys.ENTER)) {
                resume();
            }
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

        if (!PAUSE) {
            try {
                system.render();
                engine.preRender();
                engine.render();
            } catch (Exception ex) {
                ERROR = true;
                errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
                Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            viewport.apply(g);
            g.setColor(Colors.WHITE());
            g.drawString("-- Paused --", 0, 60, 240, 1);
            g.drawString("Exit to main menu?", 0, 74, 240, 1);
            g.drawString("Yes    No", 0, 90, 240, 1);

            if (CONFIRM) {
                g.setColor(Colors.GREEN());
                g.drawRect(78, 86, 36, 16);
            } else {
                g.setColor(Colors.RED());
                g.drawRect(130, 86, 36, 16);
            }

        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
