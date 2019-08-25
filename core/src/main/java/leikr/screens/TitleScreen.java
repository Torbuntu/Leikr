package leikr.screens;

import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.tiled.TiledMap;


/**
 *
 * @author tor
 */
public class TitleScreen extends BasicGameScreen {

    public static int ID = 2;
    AssetManager assetManager;
    FitViewport viewport;
    boolean CREDITS = false;

    TiledMap logo;
    int timer = 0;

    public TitleScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        logo = new TiledMap(Mdx.files.local("./Data/Logo/Logo.tmx"));
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    void checkInput(ScreenManager sm) {
        if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || CREDITS) {
            CREDITS = false;
            sm.enterGameScreen(CreditScreen.ID, new FadeOutTransition(Colors.TEAL()), new FadeInTransition(Colors.FOREST()));
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            System.out.println("Goodbye!");
            Mdx.platformUtils.exit(true);
        }
    }

    @Override
    public void preTransitionOut(Transition out) {
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        logo.update(f);

        checkInput(sm);
        if (Mdx.input.justTouched() || timer > 300) {
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
        logo.draw(g, 40, 56);
        g.drawString("Leikr Game System", 8, 80, 240, 1);
    }

    @Override
    public int getId() {
        return ID;
    }
}
