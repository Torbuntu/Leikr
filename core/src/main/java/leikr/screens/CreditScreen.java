package leikr.screens;

import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.gdx.Input.Keys;

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
        if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || Mdx.input.isKeyJustPressed(Keys.X) || MENU) {
            MENU = false;
            if (GameRuntime.checkLaunchTitle()) {
                sm.enterGameScreen(LoadScreen.ID, null, null);
            } else {
                sm.enterGameScreen(TerminalScreen.ID, null, null);
            }
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            System.out.println("Goodbye!");
            Mdx.platformUtils.exit(true);
        }
    }

    @Override
    public void initialise(GameContainer gc) {

    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        checkInput(sm);
        if (Mdx.input.justTouched() || timer > 300) {
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
        g.drawString("Powered by ", 0, 40, 240, 1);
        g.drawTexture(assetManager.get("./Data/Images/mini2Dx.png", Texture.class), 16, 64, 208, 87);
    }

    @Override
    public int getId() {
        return ID;
    }
}
