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
package leikr;

import leikr.customProperties.CustomSystemProperties;
import leikr.loaders.FontLoader;
import leikr.screens.EngineScreen;
import leikr.screens.ErrorScreen;
import leikr.screens.LoadScreen;
import leikr.screens.MenuScreen;
import leikr.screens.NewProgramScreen;
import leikr.screens.TerminalScreen;
import leikr.screens.TitleScreen;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.LocalFileHandleResolver;
import org.mini2Dx.core.game.ScreenBasedGame;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.viewport.FitViewport;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";

    public static String GAME_NAME;

    public static String getGamePath() {
        return "Programs/" + getGameName();
    }

    public static String getToolPath() {
        return "Data/Tools/" + getGameName();
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    public static void setGameName(String GAME_NAME) {
        GameRuntime.GAME_NAME = GAME_NAME;
    }
    private static boolean DIRECT_LAUNCH;

    public static int WIDTH = 240;
    public static int HEIGHT = 160;
    FitViewport viewport;

    AssetManager assetManager;

    public static FontLoader primaryFontLoader = new FontLoader();

    /**
     * Creates CustomSystemProperties for detecting launch title.
     *
     * @param arg
     */
    public GameRuntime(String[] arg) {
        DIRECT_LAUNCH = false;
        GAME_NAME = "";
        viewport = new FitViewport(WIDTH, HEIGHT);
        CustomSystemProperties.init();

        if (arg.length > 0 && arg[0].length() > 3 && !arg[0].equalsIgnoreCase("insecure")) {
            GAME_NAME = arg[0];
            DIRECT_LAUNCH = true;
        } else if (CustomSystemProperties.LAUNCH_TITLE.length() > 3) {
            DIRECT_LAUNCH = true;
            GAME_NAME = CustomSystemProperties.LAUNCH_TITLE;
        }
    }

    public static boolean checkDirectLaunch() {
        return DIRECT_LAUNCH;
    }

    @Override
    public void initialise() {
        assetManager = new AssetManager(new LocalFileHandleResolver());
        primaryFontLoader.initializeDefaultFont(assetManager);
        primaryFontLoader.getDefaultFont().load(assetManager);
        Mdx.graphicsContext.setFont(primaryFontLoader.getDefaultFont());

        //Transparent image to hide host system cursor.
        Pixmap tmp = Mdx.graphics.newPixmap(Mdx.files.local("Internal/Cursor.png"));
        Mdx.graphics.newCustomCursor(tmp, tmp, 0, 0).setVisible(false);
        tmp.dispose();

        this.addScreen(new EngineScreen(viewport));//1
        this.addScreen(new TitleScreen(assetManager, viewport));//2
        this.addScreen(new ErrorScreen(assetManager, viewport));//3
        this.addScreen(new LoadScreen(assetManager, viewport));//4
        this.addScreen(new NewProgramScreen(viewport));//5
        this.addScreen(new TerminalScreen(viewport));//6
        this.addScreen(new MenuScreen(viewport));//7
    }

    @Override
    public int getInitialScreenId() {
        if (DIRECT_LAUNCH) {
            return LoadScreen.ID;
        }
        return MenuScreen.ID;//initial screen to begin on is the menu screen.
    }

}
