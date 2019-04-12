package leikr.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;
import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;

public class DesktopLauncher {

    public static void main(String[] arg) {
        DesktopMini2DxConfig config = new DesktopMini2DxConfig(GameRuntime.GAME_IDENTIFIER);
        config.vSyncEnabled = false;
        config.title = "Leikr";
        config.width = 720;
        config.height = 480;
        config.fullscreen = false;
        System.out.println("OS: " + Mdx.os.name());
        new DesktopMini2DxGame(new GameRuntime(), config);
    }
}
