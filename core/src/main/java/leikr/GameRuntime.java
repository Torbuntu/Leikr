package leikr;

import leikr.screens.EngineScreen;
import leikr.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import leikr.screens.TitleScreen;
import org.mini2Dx.core.game.ScreenBasedGame;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";

    public static int WIDTH = 240;
    public static int HEIGHT = 160;

    AssetManager assetManager;
    
    public static String GAME_NAME;
    public static void setGameName(String name){
        GAME_NAME = name;
    }
    
    public static String getGamePath(){
        return "./Games/"+GAME_NAME;
    }

    @Override
    public void initialise() {
        assetManager = new AssetManager();
        this.addScreen(new MenuScreen(assetManager));
        this.addScreen(new EngineScreen(assetManager));
        this.addScreen(new TitleScreen(assetManager));
    }

    @Override
    public int getInitialScreenId() {
        return TitleScreen.ID;
    }

}
