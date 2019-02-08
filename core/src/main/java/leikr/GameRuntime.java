package leikr;

import leikr.screens.EngineScreen;
import leikr.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import org.mini2Dx.core.game.ScreenBasedGame;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";

    public static int WIDTH = 240;
    public static int HEIGHT = 160;

    AssetManager assetManager;

    @Override
    public void initialise() {
        assetManager = new AssetManager();
        this.addScreen(new MenuScreen(assetManager));
        this.addScreen(new EngineScreen(assetManager));
    }

    @Override
    public int getInitialScreenId() {
        return MenuScreen.ID;
    }

}
