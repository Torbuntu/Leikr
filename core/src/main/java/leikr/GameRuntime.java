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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.managers.ManagerDTO;
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
import leikr.managers.InputManager;
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
import org.mini2Dx.core.files.ExternalFileHandleResolver;
import org.mini2Dx.core.game.ScreenBasedGame;
import org.mini2Dx.core.graphics.CustomCursor;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.viewport.FitViewport;

public class GameRuntime extends ScreenBasedGame {

    public final String GAME_IDENTIFIER = "torbuntu.leikr";
    public String fileDroppedTitle;

    public final int WIDTH = 240;
    public final int HEIGHT = 160;
    private boolean directLaunch;
    private String gameName;

    private final String programsPath;
    private final String basePath;
    private final String dataPath;
    private final String deployPath;

    private final FitViewport viewport;
    private AssetManager assetManager;

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
    private GraphicsManager graphicsManager;
    private InputManager inputManager;
    private SystemManager systemManager;

    private ManagerDTO managerDTO;

    private CustomCursor cursor;

    private final CustomSystemProperties customSystemProperties;

    /**
     * Creates CustomSystemProperties for detecting launch title.
     *
     * @param args
     */
    public GameRuntime(String[] args) {
        basePath = System.getProperty("user.home") + "/.config/Leikr/";
        programsPath = System.getProperty("user.home") + "/.config/Leikr/Programs/";
        dataPath = System.getProperty("user.home") + "/.config/Leikr/Data/";
        deployPath = System.getProperty("user.home") + "/.config/Leikr/Deploy/";
        System.out.println(programsPath + "\n" + dataPath);
        directLaunch = false;
        gameName = "";
        viewport = new FitViewport(WIDTH, HEIGHT);
        customSystemProperties = new CustomSystemProperties();

        if (args.length > 0 && args[0].length() > 3 && !args[0].equalsIgnoreCase("insecure")) {
            gameName = args[0];
            directLaunch = true;
        } else if (customSystemProperties.getLaunchTitle().length() > 3) {
            directLaunch = true;
            gameName = customSystemProperties.getLaunchTitle();
            System.out.println("Game Title: " + gameName);
        }
    }

    void checkFileSystem() throws IOException {
        String leikrConfig = System.getProperty("user.home") + "/.config/Leikr/";
        if (!Mdx.files.external(leikrConfig).exists()) {
            Mdx.files.external(leikrConfig).mkdirs();
            Mdx.files.external(programsPath).mkdirs();
            Mdx.files.external(dataPath).mkdirs();
            Mdx.files.local("Data").copyTo(Mdx.files.external(leikrConfig));
            Mdx.files.local("Programs").copyTo(Mdx.files.external(leikrConfig));
        }
    }

    @Override
    public void initialise() {
        try {
            checkFileSystem();
        } catch (IOException ex) {
            Logger.getLogger(GameRuntime.class.getName()).log(Level.SEVERE, null, ex);
        }

        assetManager = new AssetManager(new ExternalFileHandleResolver());

        // Loaders
        initializeLoaders();

        //Managers
        initializeManagers();

        // Initialize screens
        initializeScreens();

        //Transparent image to hide host system cursor.
        Pixmap tmp = Mdx.graphics.newPixmap(Mdx.files.local("Internal/Cursor.png"));
        cursor = Mdx.graphics.newCustomCursor(tmp, tmp, 0, 0);
        tmp.dispose();
        cursor.setVisible(false);
    }

    @Override
    public int getInitialScreenId() {
        if (directLaunch) {
            return LoadScreen.ID;
        }
        return TitleScreen.ID;//initial screen to begin on is the menu screen.
    }

    private void initializeLoaders() {
        primaryFontLoader = new FontLoader(assetManager);
        primaryFontLoader.getDefaultFont().load(assetManager);
        Mdx.graphicsContext.setFont(primaryFontLoader.getDefaultFont());

        audioLoader = new AudioLoader();
        engineLoader = new EngineLoader(this);
        imageLoader = new ImageLoader();
        mapLoader = new MapLoader(customSystemProperties);
        spriteLoader = new SpriteLoader();
    }

    private void initializeManagers() {
        audioManager = new AudioManager(audioLoader);
        dataManager = new DataManager();
        pixelManager = new PixelManager();
        graphicsManager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager);
        inputManager = new InputManager(customSystemProperties);
        systemManager = new SystemManager(engineLoader, primaryFontLoader, spriteLoader, this);
        terminalManager = new TerminalManager(this, engineLoader);

        // DTO for passing managers to lower systems
        managerDTO = new ManagerDTO(audioManager, dataManager, pixelManager, graphicsManager, inputManager, systemManager);
    }

    private void initializeScreens() {
        this.addScreen(new EngineScreen(viewport, managerDTO, engineLoader, this));//1
        this.addScreen(new TitleScreen(assetManager, viewport, pixelManager, this));//2
        this.addScreen(new ErrorScreen(viewport, this));//3
        this.addScreen(new LoadScreen(this, assetManager, viewport, engineLoader, gameName));//4
        this.addScreen(new NewProgramScreen(viewport, this));//5
        this.addScreen(new TerminalScreen(viewport, terminalManager, engineLoader, this));//6
        this.addScreen(new MenuScreen(customSystemProperties, viewport, this));//7
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

    public void clearFileDropped() {
        fileDroppedTitle = "";
    }

    public CustomCursor getCursor() {
        return cursor;
    }

    /**
     * Returns the full path to Leikr's Programs directory. NOTE: This includes a
     * trailing `/`
     *
     * @return programsPath
     */
    public String getProgramsPath() {
        return programsPath;
    }

    /**
     * Returns the full path to Leikr's Data directory. NOTE: This includes a
     * trailing `/`
     *
     * @return dataPath
     */
    public String getDataPath() {
        return dataPath;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public String getBasePath() {
        return basePath;
    }

}
