package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
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
    static String errorMessage;

    public ErrorScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        errorMessage = "";
    }

    public static void setErrorMessage(String message) {
        errorMessage = message;
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {
                if (i == Input.Keys.ESCAPE || i == Input.Keys.ENTER || i == Input.Keys.SPACE || i == Input.Keys.Q) {
                    MENU = true;
                }
                return true;
            }
        });
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
        if (MENU) {
            MENU = false;
            sm.enterGameScreen(MenuScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
        }
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Color.RED);
        g.drawString("Message:  " + errorMessage, 0, 0, 232);
        g.setColor(Color.BLACK);
        g.drawRect(0, 152, 240, 8);
        g.setColor(Color.GREEN);
        g.drawString(":q to quit", 0, 152);
    }

    @Override
    public int getId() {
        return ID;
    }
}
