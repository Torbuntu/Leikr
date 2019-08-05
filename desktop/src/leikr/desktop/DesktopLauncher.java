package leikr.desktop;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import java.io.File;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.libgdx.desktop.DesktopMini2DxConfig;

public class DesktopLauncher {

    public static void main(String[] arg) {
        if (arg.length > 0 && arg[0].equalsIgnoreCase("insecure")) {
            Logger.getLogger(Security.class.getName()).log(Level.WARNING, "Leikr is running without security policy.");
        } else {
            Logger.getLogger(Security.class.getName()).log(Level.INFO, "Setting Leikr security policy.");
            System.setProperty("java.security.policy", new File("Sys/mysecurity.policy").getAbsolutePath());
            System.setSecurityManager(new SecurityManager());
        }
        DesktopMini2DxConfig config = new DesktopMini2DxConfig(GameRuntime.GAME_IDENTIFIER);
        config.vSyncEnabled = true;
        config.title = "Leikr";
//        config.width = 720;
//        config.height = 480;
        config.fullscreen = false;
        new DesktopMini2DxGame(new GameRuntime(arg), config);
    }
}
