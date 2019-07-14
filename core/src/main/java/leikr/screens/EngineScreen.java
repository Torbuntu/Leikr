package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import leikr.loaders.EngineLoader;
import leikr.managers.LeikrSystemManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
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
    FitViewport viewport;

    private static boolean PAUSE = false;
    private static boolean CONFIRM = false;

    private ControllerAdapter pauseControllerAdapter;

    public EngineScreen(AssetManager manager) {
        this.manager = manager;
        createControllerAdapter();
    }

    /**
     * Creates a reusable controller adapter object.
     */
    private void createControllerAdapter() {
        pauseControllerAdapter = new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A) {
                    resume();
                }
                if (buttonIndex == CustomSystemProperties.B) {
                    CONFIRM = false;
                    resume();
                }
                if (buttonIndex == CustomSystemProperties.LEFT_BUMPER) {
                    CONFIRM = true;
                }
                if (buttonIndex == CustomSystemProperties.RIGHT_BUMPER) {
                    CONFIRM = false;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                if (axisCode == CustomSystemProperties.HORIZONTAL_AXIS) {
                    if (CustomSystemProperties.RIGHT == value) {
                        CONFIRM = false;
                    }
                    if (CustomSystemProperties.LEFT == value) {
                        CONFIRM = true;
                    }
                }
                return false;
            }
        };
    }

    private void addController() {
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).addListener(pauseControllerAdapter);
        }
    }

    private void removeController() {
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).removeListener(pauseControllerAdapter);
        }
    }

    private void pause() {
        addController();
        PAUSE = true;
        engine.audio.pauseAllAudio();
        engine.onPause();
    }

    private void resume() {
        removeController();
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
        if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.R) || Gdx.input.isKeyPressed(Keys.F5)) {
            reloadEngine(sm);
        }
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
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
            if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
                CONFIRM = true;
            }
            if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
                CONFIRM = false;
            }
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
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
                system.render(g);
                engine.preRender(g);
                engine.render();
            } catch (Exception ex) {
                ERROR = true;
                errorMessage = "Error in program `render` method. " + ex.getLocalizedMessage();
                Logger.getLogger(EngineLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            viewport.apply(g);
            g.setColor(Color.WHITE);
            g.drawString("-- Paused --", 0, 60, 240, 1);
            g.drawString("Exit to main menu?", 0, 74, 240, 1);
            g.drawString("Yes    No", 0, 90, 240, 1);

            if (CONFIRM) {
                g.setColor(Color.GREEN);
                g.drawRect(78, 86, 36, 16);
            } else {
                g.setColor(Color.RED);
                g.drawRect(130, 86, 36, 16);
            }

        }
    }

    @Override
    public int getId() {
        return ID;
    }
}
