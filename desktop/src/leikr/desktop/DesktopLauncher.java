package leikr.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;
import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.apache.commons.io.FileUtils;
import org.mini2Dx.core.Mdx;

public class DesktopLauncher {

    public static void main(String[] arg) {
        if (arg.length > 0 && arg[0].equalsIgnoreCase("insecure")) {
            Logger.getLogger(DesktopLauncher.class.getName()).log(Level.WARNING, "Leikr is running without security policy.");
        }else{
            checkForPolicy();
            System.out.println("Setting policy...");
            System.setProperty("java.security.policy", new File("Sys/mysecurity.policy").getAbsolutePath());
            System.setSecurityManager(new SecurityManager());
        }
        DesktopMini2DxConfig config = new DesktopMini2DxConfig(GameRuntime.GAME_IDENTIFIER);
        config.vSyncEnabled = true;
        config.title = "Leikr";
        config.width = 720;
        config.height = 480;
        config.fullscreen = false;
        System.out.println("OS: " + Mdx.os.name());
        new DesktopMini2DxGame(new GameRuntime(), config);
    }

    static void checkForPolicy() {
        if (!new File("./Sys/mysecurity.policy").exists()) {
            System.out.println("Policy not found. Creating...");
            try {
                FileUtils.forceMkdir(new File("./Sys"));
                copy("Sys/mysecurity.policy", "./Sys/mysecurity.policy");
            } catch (IOException ex) {
                Logger.getLogger(DesktopLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void copy(String resource, String destination) {
        try (InputStream resStreamIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                OutputStream resStreamOut = new FileOutputStream(new File(destination));) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            Logger.getLogger(DesktopLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
