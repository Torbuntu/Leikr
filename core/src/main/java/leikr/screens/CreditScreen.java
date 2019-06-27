package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
public class CreditScreen extends BasicGameScreen {

    public static int ID = 3;
    AssetManager assetManager;
    FitViewport viewport;
    boolean MENU = false;
    int timer = 0;

    public CreditScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        loadMini2DxGraphic();
    }

    private void loadMini2DxGraphic() {
        assetManager.load("./Data/Images/mini2Dx.png", Texture.class);
        assetManager.finishLoading();
    }

    void checkInput(ScreenManager sm) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.X) || MENU) {
            MENU = false;
            if (GameRuntime.checkLaunchTitle()) {
                sm.enterGameScreen(LoadScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
            } else {
                sm.enterGameScreen(MenuScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("Good bye!");
            Gdx.app.exit();
        }
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
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
            System.out.println("No controllers active on Credit Screen. " + ex.getMessage());
        }
    }

    @Override
    public void initialise(GameContainer gc) {

    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        checkInput(sm);
        if (Gdx.input.isTouched() || timer > 300) {
            MENU = true;
        }
        timer++;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.drawString("Powered by ", 72, 40);
        g.drawTexture(assetManager.get("./Data/Images/mini2Dx.png"), 16, 64, 208, 87);
    }

    @Override
    public int getId() {
        return ID;
    }
}
