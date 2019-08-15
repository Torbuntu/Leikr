package leikr.screens;

import leikr.GameRuntime;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.tiled.TiledMap;

import org.mini2Dx.core.input.GamePad;
/**
 *
 * @author tor
 */
public class TitleScreen extends BasicGameScreen {

    public static int ID = 2;
    AssetManager assetManager;
    MonospaceGameFont font;
    FitViewport viewport;
    boolean CREDITS = false;

    TiledMap logo;
    int timer = 0;
    
    GamePad gp;

    public TitleScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        logo = new TiledMap(Mdx.files.local("./Data/Logo/Logo.tmx"));
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        gp = Mdx.input.getGamePads().first();
        System.out.println("::: GamePad Debug :::");
        System.out.println(gp.getModelInfo());
        gp.addListener(new BaseGamePadListener(){
            @Override public void onButtonDown(GamePad gp, int button){
                System.out.println("::: " + button + " :::");
            }
        });
    }

    void checkInput(ScreenManager sm) {
        if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || CREDITS) {
            CREDITS = false;
            sm.enterGameScreen(CreditScreen.ID, new FadeOutTransition(Colors.TEAL()), new FadeInTransition(Colors.FOREST()));
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            System.out.println("Good bye!");
//            Gdx.app.exit(); //TODO: Implement with Mdx.platform.exit()
        }
    }

    @Override
    public void preTransitionOut(Transition out) {
    }

    @Override
    public void initialise(GameContainer gc) {
        font = GameRuntime.primaryFontLoader.getDefaultFont();
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        font.load(assetManager);
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
        g.setFont(font);
        logo.draw(g, 40, 56);
        g.drawString("Leikr Game System", 8, 80, 240, 1);
    }

    @Override
    public int getId() {
        return ID;
    }
}
