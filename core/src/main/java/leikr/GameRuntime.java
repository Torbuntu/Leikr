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
import leikr.loaders.AudioLoader;
import leikr.loaders.EngineLoader;
import leikr.loaders.FontLoader;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import leikr.managers.AudioManager;
import leikr.managers.DataManager;
import leikr.managers.PixelManager;
import leikr.managers.GraphicsManager;
import leikr.managers.SystemManager;
import leikr.managers.TerminalManager;
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
    public static String fileDroppedTitle;
    public final int WIDTH = 240;
    public final int HEIGHT = 160;
    private boolean directLaunch;
    private String gameName;

    FitViewport viewport;
    AssetManager assetManager;

    // Loaders
    private FontLoader primaryFontLoader;
    private AudioLoader audioLoader;
    private EngineLoader engineLoader;
    private ImageLoader imageLoader;
    private MapLoader mapLoader;
    private SpriteLoader spriteLoader;
    private TerminalManager terminalManager;

    //Managers
    private AudioManager audioManager;
    private DataManager dataManager;
    private PixelManager pixelManager;
    private GraphicsManager screenManager;
    private SystemManager systemManager;

    /**
     * Creates CustomSystemProperties for detecting launch title.
     *
     * @param arg
     */
    public GameRuntime(String[] arg) {
        directLaunch = false;
        gameName = "";
        viewport = new FitViewport(WIDTH, HEIGHT);
        CustomSystemProperties.init();

        if (arg.length > 0 && arg[0].length() > 3 && !arg[0].equalsIgnoreCase("insecure")) {
            gameName = arg[0];
            directLaunch = true;
        } else if (CustomSystemProperties.LAUNCH_TITLE.length() > 3) {
            directLaunch = true;
            gameName = CustomSystemProperties.LAUNCH_TITLE;
        }
    }

    @Override
    public void initialise() {
        assetManager = new AssetManager(new LocalFileHandleResolver());

        // Loaders
        primaryFontLoader = new FontLoader();
        primaryFontLoader.initializeDefaultFont(assetManager);
        primaryFontLoader.getDefaultFont().load(assetManager);
        Mdx.graphicsContext.setFont(primaryFontLoader.getDefaultFont());

        audioLoader = new AudioLoader();
        engineLoader = new EngineLoader(this);
        imageLoader = new ImageLoader();
        mapLoader = new MapLoader();
        spriteLoader = new SpriteLoader();
        terminalManager = new TerminalManager(this, engineLoader);

        //Managers
        audioManager = new AudioManager(audioLoader);
        dataManager = new DataManager();
        pixelManager = new PixelManager();
        screenManager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager);
        systemManager = new SystemManager(engineLoader, primaryFontLoader, spriteLoader, this);

        //Transparent image to hide host system cursor.
        Pixmap tmp = Mdx.graphics.newPixmap(Mdx.files.local("Internal/Cursor.png"));
        Mdx.graphics.newCustomCursor(tmp, tmp, 0, 0).setVisible(false);
        tmp.dispose();

        this.addScreen(new EngineScreen(viewport, audioManager, dataManager, screenManager, systemManager, engineLoader, this));//1
        this.addScreen(new TitleScreen(assetManager, viewport, pixelManager, this));//2
        this.addScreen(new ErrorScreen(assetManager, viewport, this));//3
        this.addScreen(new LoadScreen(assetManager, viewport, engineLoader));//4
        this.addScreen(new NewProgramScreen(viewport));//5
        this.addScreen(new TerminalScreen(viewport, terminalManager, this));//6
        this.addScreen(new MenuScreen(viewport, this));//7
    }

    @Override
    public int getInitialScreenId() {
        if (directLaunch) {
            return LoadScreen.ID;
        }
        return TitleScreen.ID;//initial screen to begin on is the menu screen.
    }

    public boolean checkDirectLaunch() {
        return directLaunch;
    }

    public String getGamePath() {
        return "Programs/" + getGameName();
    }

    public String getToolPath() {
        return "Data/Tools/" + getGameName();
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String GAME_NAME) {
        this.gameName = GAME_NAME;
    }

    public boolean checkFileDropped() {
        return (null != fileDroppedTitle && fileDroppedTitle.length() > 2);
    }

    public String getFileDroppedTitle() {
        if (checkFileDropped()) {
            return fileDroppedTitle;
        }
        return "";

    }

}
