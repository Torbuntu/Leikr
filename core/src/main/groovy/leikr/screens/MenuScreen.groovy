/*
 * Copyright 2020 tor.
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
package leikr.screens

import leikr.GameRuntime
import leikr.managers.InputManager
import leikr.properties.ProgramProperties
import leikr.transitions.EnterTransition
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.Texture
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.core.util.Align
import org.mini2Dx.gdx.Input.Keys

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class MenuScreen extends BasicGameScreen {

	public static final int ID = 7
	int index = 0
	String isCompiled = ""

	List<ProgramProperties> games

	final FitViewport fitViewport
	final StretchViewport stretchViewport
	final GameRuntime runtime
	final InputManager inputManager
	Texture icon
	FrameBuffer framebuffer
	Menu currentMenu = Menu.PROGRAMS

	int popupTimer = 0
	String popupText = ""

	MenuScreen(FitViewport vp, GameRuntime runtime) {
		this.runtime = runtime
		this.inputManager = runtime.getInputManager()
		fitViewport = vp
		stretchViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT)
	}

	private void rebuildGamesList() {
		try {
			games = []
			// Enforce name size of >= 3
			Mdx.files.external(runtime.getProgramsPath()).list()
					.findAll { it.nameWithoutExtension().size() > 2 }
					.each { game ->
						games.add(new ProgramProperties(runtime.getProgramsPath() + game.nameWithoutExtension()))
					}

		} catch (IOException ex) {
			ex.printStackTrace()
			Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, ex.getMessage(), ex)
		}
		loadIcon()
	}

	@Override
	void initialise(GameContainer gc) {
		rebuildGamesList()
	}

	@Override
	void preTransitionIn(Transition transition) {
		index = 0
		rebuildGamesList()
		currentMenu = Menu.PROGRAMS
		framebuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT)
	}

	@Override
	void preTransitionOut(Transition transition) {
		framebuffer.dispose()
	}

	private void loadIcon() {
		try {
			icon = Mdx.graphics.newTexture(Mdx.files.external(runtime.getProgramsPath() + "${games.get(index).getGameTitle()}/Art/icon.png"))
			if (Mdx.files.external(runtime.getProgramsPath() + "${games.get(index).getGameTitle()}/Code/Compiled").exists()) {
				isCompiled = " *"
				if (games.get(index).getUseCompiled()) {
					isCompiled = " **"
				}
			} else {
				isCompiled = ""
			}
		} catch (Exception ignored) {
			icon = Mdx.graphics.newTexture(Mdx.files.external(runtime.getDataPath() + "Logo/logo-32x32.png"))
			Logger.getLogger(MenuScreen.class.getName()).log(Level.WARNING, "No icon file for: {0}", games.get(index).getGameTitle())
		}
	}

	@Override
	void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
		if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Mdx.platformUtils.exit(true)
		}
		if (Mdx.input.isKeyJustPressed(Keys.T)) {
			sm.enterGameScreen(TerminalScreen.ID, null, null)
		}
		if (runtime.checkFileDropped()) {
			LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
			ls.setGameName(runtime.getFileDroppedTitle())
			runtime.setGameName(runtime.getFileDroppedTitle())
			runtime.clearFileDropped()
			sm.enterGameScreen(LoadScreen.ID, new EnterTransition(runtime), null)
		}

		switch (currentMenu) {
			case Menu.SETTINGS:
				updateSettings(sm)
				break
			case Menu.PROGRAMS:
				updatePrograms(sm)
				break
		}
	}

	void updateSettings(ScreenManager sm) {
		if (inputManager.button("DOWN") || Mdx.input.isKeyJustPressed(Keys.DOWN)) {
			currentMenu = Menu.PROGRAMS
			index = 0
			return
		}
		if ((Mdx.input.isKeyJustPressed(Keys.LEFT) || inputManager.buttonPress("LEFT")) && index > 0) {
			index--
		}
		if ((Mdx.input.isKeyJustPressed(Keys.RIGHT) || inputManager.buttonPress("RIGHT")) && index < 2) {
			index++
		}
		if (Mdx.input.isKeyJustPressed(Keys.ENTER) || inputManager.buttonPress("START")) {
			switch (index) {
				case 0: sm.enterGameScreen(TerminalScreen.ID, null, null)
					break
				case 1:
					if (Mdx.input.getGamePads().size() == 0) {
						popupText = "No controllers available to map."
						popupTimer = 100
					} else {
						sm.enterGameScreen(ControllerMappingScreen.ID, null, null)
					}
					break
				case 2:
					sm.enterGameScreen(NewProgramScreen.ID, null, null)
					break
			}
		}
	}

	void updatePrograms(ScreenManager sm) {
		if (inputManager.button("UP") || Mdx.input.isKeyJustPressed(Keys.UP)) {
			currentMenu = Menu.SETTINGS
			index = 0
			return
		}
		if ((Mdx.input.isKeyJustPressed(Keys.LEFT) || inputManager.buttonPress("LEFT")) && index > 0) {
			index--
			loadIcon()
		}
		if ((Mdx.input.isKeyJustPressed(Keys.RIGHT) || inputManager.buttonPress("RIGHT")) && index < games.size() - 1) {
			index++
			loadIcon()
		}
		if (Mdx.input.isKeyJustPressed(Keys.ENTER) || inputManager.buttonPress("START")) {
			LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
			ls.setGameName(games.get(index).getGameTitle())
			runtime.setGameName(games.get(index).getGameTitle())
			sm.enterGameScreen(LoadScreen.ID, new EnterTransition(runtime), null)
		}
	}

	/**
	 * See the EngineScreen for comments on why we use both the StretchViewport and the FitViewport.
	 * @param gc
	 * @param g
	 */
	@Override
	void render(GameContainer gc, Graphics g) {
		stretchViewport.apply(g)
		framebuffer.begin()
		switch (currentMenu) {
			case Menu.SETTINGS:
				renderSettings(g)
				break
			case Menu.PROGRAMS:
				renderPrograms(g)
				break
		}
		framebuffer.end()

		fitViewport.apply(g)
		g.drawTexture(framebuffer.getTexture(), 0, 0, false)
	}

	void renderSettings(Graphics g) {
		g.with {
			clearContext()
			setColor(Colors.WHITE())

			switch (index) {
				case 0:
					drawString("Terminal", 0, 80, 240, Align.CENTER)
					break
				case 1:
					drawString("Controller Mapping", 0, 80, 240, Align.CENTER)
					break
				case 2:
					drawString("New Program Generator", 0, 80, 240, Align.CENTER)
					break
			}
			drawString("Settings Menu", 0, 153, 240, Align.CENTER)

			renderPopup(g)
			flush()
		}
	}

	void renderPrograms(Graphics g) {
		g.with {
			clearContext()
			// Icon
			drawTexture(icon, 120 - icon.width / 2, 80 - icon.height / 2)

			// Top bar
			setColor(Colors.BLACK())
			fillRect(0, 0, 240, 10)

			setColor(Colors.GREEN())
			drawLineSegment(0, 10, 240, 10)

			setColor(Colors.WHITE())

			drawString(games.get(index).getGameTitle() + isCompiled, 0, 3, 240, Align.CENTER)

			if (index > 0) {
				drawString("<-", 10, 3, 240, Align.LEFT)
			}
			if (index < games.size() - 1) {
				drawString("->", -10, 3, 240, Align.RIGHT)
			}
			if (!runtime.isSecure()) {
				setColor(Colors.RED())
				drawString("!NO SANDBOX!", 0, 16, 240, Align.CENTER)
			}

			// Bottom Bar
			setColor(Colors.BLACK())
			fillRect(0, 150, 240, 10)
			setColor(Colors.GREEN())
			drawLineSegment(0, 150, 240, 150)

			setColor(Colors.WHITE())
			drawString("[START] - Play", 0, 153, 240, Align.CENTER)

			renderPopup(g)
			flush()
		}
	}

	void renderPopup(Graphics g) {
		if (popupTimer > 0) {
			popupTimer--
			g.with {
				setColor(Colors.RED())
				drawRect(1, 140, 239, 19)
				drawString(popupText, 0, 143, 240, Align.CENTER)
			}
		}
	}

	@Override
	int getId() {
		ID
	}


	protected enum Menu {
		SETTINGS, PROGRAMS
	}

}
