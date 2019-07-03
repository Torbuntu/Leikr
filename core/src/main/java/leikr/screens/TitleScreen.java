package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import leikr.GameRuntime;
import org.mini2Dx.core.font.MonospaceFont;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.tiled.TiledMap;

/**
 *
 * @author tor
 */
public class TitleScreen extends BasicGameScreen {

    public static int ID = 2;
    AssetManager assetManager;
    MonospaceFont font;
    FitViewport viewport;
    boolean CREDITS = false;

    ControllerAdapter titleAdapter;

    TiledMap logo;
    int timer = 0;

    public TitleScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        logo = new TiledMap(Gdx.files.local("./Data/Logo/Logo.tmx"));
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    void checkInput(ScreenManager sm) {
        if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER) || CREDITS) {
            CREDITS = false;
            sm.enterGameScreen(CreditScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            System.out.println("Good bye!");
            Gdx.app.exit();
        }
    }

    @Override
    public void preTransitionOut(Transition out) {
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).removeListener(titleAdapter);
        }
    }

    @Override
    public void initialise(GameContainer gc) {
        font = GameRuntime.primaryFontLoader.getDefaultFont();

        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                titleAdapter = new ControllerAdapter() {
                    @Override
                    public boolean buttonDown(Controller controller, int buttonIndex) {
                        CREDITS = true;
                        return true;
                    }
                };
                Controllers.getControllers().get(0).addListener(titleAdapter);
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Title Screen. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        font.load(assetManager);
        logo.update(f);

        checkInput(sm);
        if (Gdx.input.isTouched() || timer > 300) {
            CREDITS = true;
        }
        timer++;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setFont(font);
        logo.draw(g, 36, 64);
        g.drawString("Leikr Game System", 56, 88);
    }

    @Override
    public int getId() {
        return ID;
    }
}
