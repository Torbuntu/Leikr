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

import leikr.properties.SystemProperties
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

	String gameIdentifier = "torbuntu.leikr"
	String fileDroppedTitle

	static int WIDTH = 240
	static int HEIGHT = 160
	boolean directLaunch
	String gameName

	String programsPath
	String basePath
	String dataPath
	String deployPath
	String packagePath

	FitViewport viewport
	AssetManager assetManager

	// Loaders
	FontLoader primaryFontLoader
	AudioLoader audioLoader
	EngineLoader engineLoader
	ImageLoader imageLoader
	MapLoader mapLoader
	SpriteLoader spriteLoader
	TerminalManager terminalManager

	// Managers
	AudioManager audioManager
	DataManager dataManager
	PixelManager pixelManager
	GraphicsManager graphicsManager
	InputManager inputManager
	SystemManager systemManager

	/**
	 * DTO for passing managers to lower systems
	 */
	ManagerDTO managerDTO

	CustomCursor cursor

	SystemProperties systemProperties
	boolean secure

	final private def logger = Logger.getLogger(GameRuntime.class.getName())

	/**
	 * Creates SystemProperties for detecting launch title.
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
		systemProperties = new SystemProperties()

		if (args.length > 0 && args[0].length() > 3 && !args[0].equalsIgnoreCase("insecure")) {
			gameName = args[0]
			directLaunch = true
		} else if (systemProperties.getLaunchTitle().length() > 3) {
			directLaunch = true
			gameName = systemProperties.getLaunchTitle()
			println "Game Title: " + gameName
		}
	}

	void setLeikrHome(String leikrHome) {
		try {
			basePath = "$leikrHome/Leikr/"
			programsPath = "$leikrHome/Leikr/Programs/"
			dataPath = "$leikrHome/Leikr/Data/"
			deployPath = "$leikrHome/Leikr/Deploy/"
			packagePath = "$leikrHome/Leikr/Packages/"

			systemProperties = new SystemProperties()
			checkFileSystem()
			logger.log(Level.INFO, "Using custom Leikr home at: {0}", basePath)

		} catch (IOException ignored) {
			logger.log(Level.WARNING, "Unable to use custom Leikr home: {0}", basePath)
		}
	}

	private void customPathVariables() {
		String leikrHome = System.getenv("LEIKR_HOME")
		basePath = "$leikrHome/Leikr/"
		programsPath = "$leikrHome/Leikr/Programs/"
		dataPath = "$leikrHome/Leikr/Data/"
		deployPath = "$leikrHome/Leikr/Deploy/"
		packagePath = "$leikrHome/Leikr/Packages/"
		logger.log(Level.INFO, "Using custom Leikr home at: {0}", basePath)
	}

	private void defaultPathVariables() {
		String userHome = System.getProperty("user.home")
		basePath = "$userHome/Leikr/"
		programsPath = "$userHome/Leikr/Programs/"
		dataPath = "$userHome/Leikr/Data/"
		deployPath = "$userHome/Leikr/Deploy/"
		packagePath = "$userHome/Leikr/Packages/"
		logger.log(Level.INFO, "Using default Leikr home at: {0}", basePath)
	}

	private void checkFileSystem() throws IOException {
		Mdx.files.with {
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
			logger.log(Level.WARNING, ex.getMessage(), ex)
		}

		assetManager = new AssetManager(new ExternalFileHandleResolver())

		// Loaders
		initializeLoaders()

		//Managers
		initializeManagers()

		// Initialize screens
		initializeScreens()
	}

	@Override
	int getInitialScreenId() {
		(directLaunch) ? LoadScreen.ID : TitleScreen.ID
	}

	private void initializeLoaders() {
		primaryFontLoader = new FontLoader(assetManager)
		primaryFontLoader.getDefaultFont().load(assetManager)
		Mdx.graphicsContext.setFont(primaryFontLoader.getDefaultFont())

		audioLoader = new AudioLoader()
		engineLoader = new EngineLoader(this)
		imageLoader = new ImageLoader()
		mapLoader = new MapLoader(systemProperties)
		spriteLoader = new SpriteLoader(this)
	}

	private void initializeManagers() {
		audioManager = new AudioManager(audioLoader)
		dataManager = new DataManager()
		pixelManager = new PixelManager()
		graphicsManager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager)
		inputManager = new InputManager()
		systemManager = new SystemManager(engineLoader, primaryFontLoader, spriteLoader, this)
		terminalManager = new TerminalManager(this, engineLoader)

		managerDTO = new ManagerDTO(audioManager: audioManager, dataManager: dataManager, pixelManager: pixelManager,
				graphicsManager: graphicsManager, inputManager: inputManager, systemManager: systemManager)
	}

	private void initializeScreens() {
		this.with {
			addScreen(new EngineScreen(viewport, managerDTO, engineLoader, this))//1
			addScreen(new TitleScreen(assetManager, viewport, pixelManager, this))//2
			addScreen(new ErrorScreen(viewport, this))//3
			addScreen(new LoadScreen(this, assetManager, viewport, engineLoader, gameName))//4
			addScreen(new NewProgramScreen(viewport, this))//5
			addScreen(new TerminalScreen(viewport, terminalManager, this))//6
			addScreen(new MenuScreen(viewport, this))//7
			addScreen(new ControllerMappingScreen(managerDTO, viewport, this))//8
		}

	}

	boolean isDevMode() {
		systemProperties.isDevMode()
	}

	boolean checkDirectLaunch() {
		directLaunch
	}

	boolean isSecure() {
		secure
	}

	String getGamePath() {
		programsPath + getGameName()
	}

	boolean checkFileDropped() {
		(null != fileDroppedTitle && fileDroppedTitle.length() > 2)
	}

	String getFileDroppedTitle() {
		checkFileDropped() ? fileDroppedTitle : ""
	}

	void clearFileDropped() {
		fileDroppedTitle = ""
	}

}