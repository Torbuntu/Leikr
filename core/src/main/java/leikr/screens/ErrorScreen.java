package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

/**
 *
 * @author tor
 */
public class ErrorScreen extends BasicGameScreen {

    public static int ID = 4;
    AssetManager assetManager;
    FitViewport viewport;
    boolean MENU = false;
    boolean RELOAD = false;
    static String errorMessage;
    ControllerAdapter errorControllerAdapter;

    public ErrorScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        errorMessage = "";
        createControllerAdapter();
    }

    /**
     * Creates a reusable controller adapter object.
     */
    private void createControllerAdapter() {
        errorControllerAdapter = new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A || buttonIndex == CustomSystemProperties.SELECT || buttonIndex == CustomSystemProperties.B) {
                    MENU = true;
                }

                if (buttonIndex == CustomSystemProperties.LEFT_BUMPER || buttonIndex == CustomSystemProperties.RIGHT_BUMPER) {
                    RELOAD = true;
                }
                return true;
            }
        };
    }

    private void addController() {
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).addListener(errorControllerAdapter);
        }
    }

    private void removeController() {
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).removeListener(errorControllerAdapter);
        }
    }

    public static void setErrorMessage(String message) {
        errorMessage = message;
    }

    void reloadEngine(ScreenManager sm) {
        sm.enterGameScreen(LoadScreen.ID, null, null);
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        addController();
    }

    @Override
    public void preTransitionOut(Transition transitionOut) {
        removeController();
    }

    @Override
    public void initialise(GameContainer gc) {
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(new ControllerAdapter() {
                    @Override
                    public boolean buttonDown(Controller controller, int buttonIndex) {
                        MENU = true;
                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Error Screen. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (MENU || Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.Q)) {
            MENU = false;
            sm.enterGameScreen(MenuScreen.ID, new FadeOutTransition(Colors.TEAL()), new FadeInTransition(Colors.FOREST()));
        }

        if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.R) || Gdx.input.isKeyPressed(Keys.F5)) {
            reloadEngine(sm);
        }
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Colors.RED());
        g.drawString("Message:  " + errorMessage, 0, 0, 232);
        g.setColor(Colors.BLACK());
        g.drawRect(0, 152, 240, 8);
        g.setColor(Colors.GREEN());
        g.drawString(":q to quit", 0, 152);
    }

    @Override
    public int getId() {
        return ID;
    }
}
