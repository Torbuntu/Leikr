/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        config.width = 720;
        config.height = 480;
        config.fullscreen = false;
        new DesktopMini2DxGame(new GameRuntime(arg), config);
    }
}
