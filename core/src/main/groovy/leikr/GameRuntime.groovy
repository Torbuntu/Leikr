/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package leikr

import leikr.customProperties.CustomSystemProperties
import leikr.loaders.*
import leikr.managers.*
import leikr.screens.*
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.files.ExternalFileHandleResolver
import org.mini2Dx.core.game.ScreenBasedGame
import org.mini2Dx.core.graphics.CustomCursor
import org.mini2Dx.core.graphics.Pixmap
import org.mini2Dx.core.graphics.viewport.FitViewport

import java.util.logging.Level
import java.util.logging.Logger 

class GameRuntime extends ScreenBasedGame {

    String GAME_IDENTIFIER = "torbuntu.leikr"
    String fileDroppedTitle

    static int WIDTH = 240
    static int HEIGHT = 160
    private boolean directLaunch
    private String gameName

    private String programsPath
    private String basePath
    private String dataPath
    private String deployPath
    private String packagePath

    private FitViewport viewport
    private AssetManager assetManager

    // Loaders
    private FontLoader primaryFontLoader
    private AudioLoader audioLoader
    private EngineLoader engineLoader
    private ImageLoader imageLoader
    private MapLoader mapLoader
    private SpriteLoader spriteLoader
    private TerminalManager terminalManager

    // Managers
    private AudioManager audioManager
    private DataManager dataManager
    private PixelManager pixelManager
    private GraphicsManager graphicsManager
    private InputManager inputManager
    private SystemManager systemManager

    /**
     * DTO for passing managers to lower systems
     */
    private ManagerDTO managerDTO

    private CustomCursor cursor

    private CustomSystemProperties customSystemProperties
    private boolean secure

    /**
     * Creates CustomSystemProperties for detecting launch title.
     *
     * @param args
     * @param secure
     */
    GameRuntime(String[] args, boolean secure) {
        this.secure = secure
        if (System.getenv("LEIKR_HOME") != null) {
            customPathVariables()
        } else {
            defaultPathVariables()
        }

        directLaunch = false
        gameName = ""
        viewport = new FitViewport(WIDTH, HEIGHT)
        customSystemProperties = new CustomSystemProperties()

        if (args.length > 0 && args[0].length() > 3 && !args[0].equalsIgnoreCase("insecure")) {
            gameName = args[0]
            directLaunch = true
        } else if (customSystemProperties.getLaunchTitle().length() > 3) {
            directLaunch = true
            gameName = customSystemProperties.getLaunchTitle()
            System.out.println("Game Title: " + gameName)
        }
    }

    void setLeikrHome(String leikrHome) {
        try {
            basePath = "$leikrHome/Leikr/"
            programsPath = "$leikrHome/Leikr/Programs/"
            dataPath = "$leikrHome/Leikr/Data/"
            deployPath = "$leikrHome/Leikr/Deploy/"
            packagePath = "$leikrHome/Leikr/Packages/"

            customSystemProperties = new CustomSystemProperties()
            checkFileSystem()
            Logger.getLogger(GameRuntime.class.getName()).log(Level.INFO, "Using custom Leikr home at: {0}", basePath)

        } catch (IOException ignored) {
            Logger.getLogger(GameRuntime.class.getName()).log(Level.WARNING, "Unable to use custom Leikr home: {0}", basePath)
        }
    }

    private void customPathVariables() {
        String leikrHome = System.getenv("LEIKR_HOME")
        basePath = "$leikrHome/Leikr/"
        programsPath = "$leikrHome/Leikr/Programs/"
        dataPath = "$leikrHome/Leikr/Data/"
        deployPath = "$leikrHome/Leikr/Deploy/"
        packagePath = "$leikrHome/Leikr/Packages/"
        Logger.getLogger(GameRuntime.class.getName()).log(Level.INFO, "Using custom Leikr home at: {0}", basePath)
    }

    private void defaultPathVariables() {
        String userHome = System.getProperty("user.home")
        basePath = "$userHome/Leikr/"
        programsPath = "$userHome/Leikr/Programs/"
        dataPath = "$userHome/Leikr/Data/"
        deployPath = "$userHome/Leikr/Deploy/"
        packagePath = "$userHome/Leikr/Packages/"
        Logger.getLogger(GameRuntime.class.getName()).log(Level.INFO, "Using default Leikr home at: {0}", basePath)
    }

    private void checkFileSystem() throws IOException {
		Mdx.files.with{
			if (!external(basePath).exists()) {
				external(basePath).mkdirs()
				external(programsPath).mkdirs()
				external(dataPath).mkdirs()
				local("Data").copyTo(external(basePath))
				local("Programs").copyTo(external(basePath))
			}
			if (!external(programsPath).exists()) {
				external(programsPath).mkdirs()
				local("Programs").copyTo(external(basePath))
			}
			if (!external(dataPath).exists()) {
				external(dataPath).mkdirs()
				local("Data").copyTo(external(basePath))
			}
		}

    }

    @Override
    void initialise() {
        try {
            checkFileSystem()
        } catch (IOException ex) {
            Logger.getLogger(GameRuntime.class.getName()).log(Level.SEVERE, null, ex)
        }

        assetManager = new AssetManager(new ExternalFileHandleResolver())

        // Loaders
        initializeLoaders()

        //Managers
        initializeManagers()

        // Initialize screens
        initializeScreens()

        //Transparent image to hide host system cursor.
        Pixmap tmp = Mdx.graphics.newPixmap(Mdx.files.local("Internal/Cursor.png"))
        cursor = Mdx.graphics.newCustomCursor(tmp, tmp, 0, 0)
        tmp.dispose()
        cursor.setVisible(false)
    }

    @Override
    int getInitialScreenId() {
        if (directLaunch) {
            return LoadScreen.ID
        }
        return TitleScreen.ID//initial screen to begin on is the menu screen.
    }

    private void initializeLoaders() {
        primaryFontLoader = new FontLoader(assetManager)
        primaryFontLoader.getDefaultFont().load(assetManager)
        Mdx.graphicsContext.setFont(primaryFontLoader.getDefaultFont())

        audioLoader = new AudioLoader()
        engineLoader = new EngineLoader(this)
        imageLoader = new ImageLoader()
        mapLoader = new MapLoader(customSystemProperties)
        spriteLoader = new SpriteLoader(this)
    }

    private void initializeManagers() {
        audioManager = new AudioManager(audioLoader)
        dataManager = new DataManager()
        pixelManager = new PixelManager()
        graphicsManager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager)
        inputManager = new InputManager(customSystemProperties)
        systemManager = new SystemManager(engineLoader, primaryFontLoader, spriteLoader, this)
        terminalManager = new TerminalManager(this, engineLoader)

        managerDTO = new ManagerDTO(audioManager: audioManager, dataManager: dataManager, pixelManager: pixelManager,
			graphicsManager: graphicsManager, inputManager: inputManager, systemManager: systemManager)
    }

    private void initializeScreens() {
		this.with{
			addScreen(new EngineScreen(viewport, managerDTO, engineLoader, this))//1
			addScreen(new TitleScreen(assetManager, viewport, pixelManager, this))//2
			addScreen(new ErrorScreen(viewport, this))//3
			addScreen(new LoadScreen(this, assetManager, viewport, engineLoader, gameName))//4
			addScreen(new NewProgramScreen(viewport, this))//5
			addScreen(new TerminalScreen(viewport, terminalManager, this))//6
			addScreen(new MenuScreen(viewport, this))//7
		}

    }

    boolean isDevMode() {
        return customSystemProperties.isDevMode()
    }

    boolean checkDirectLaunch() {
        return directLaunch
    }

    boolean isSecure() {
        return secure
    }

    String getGamePath() {
        return programsPath + getGameName()
    }

    String getGameName() {
        return gameName
    }

    void setGameName(String GAME_NAME) {
        this.gameName = GAME_NAME
    }

    void setFileDroppedTitle(String title) {
        this.fileDroppedTitle = title
    }

    boolean checkFileDropped() {
        return (null != fileDroppedTitle && fileDroppedTitle.length() > 2)
    }

    String getFileDroppedTitle() {
        return checkFileDropped() ? fileDroppedTitle : ""
    }

    void clearFileDropped() {
        fileDroppedTitle = ""
    }

    CustomCursor getCursor() {
        return cursor
    }

    /**
     * Returns the full path to Leikr's Programs directory. 
     * 
     * Note: This includes a trailing `/`
     *
     * @return programsPath
     */
    String getProgramsPath() {
        return programsPath
    }

    /**
     * Returns the full path to Leikr's Data directory. 
     * 
     * Note: This includes a trailing `/`
     *
     * @return dataPath
     */
    String getDataPath() {
        return dataPath
    }

    String getDeployPath() {
        return deployPath
    }

    String getBasePath() {
        return basePath
    }

    String getPackagePath() {
        return packagePath
    }

}