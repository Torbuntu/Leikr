package leikr.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;
import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import leikr.GameRuntime;

public class DesktopLauncher {

    public static void main(String[] arg) {
        DesktopMini2DxConfig config = new DesktopMini2DxConfig(GameRuntime.GAME_IDENTIFIER);
        config.vSyncEnabled = true;
        config.title = "Leikr";
        config.width = 320;
        config.height = 240;
        new DesktopMini2DxGame(new GameRuntime(), config);
    }
}
