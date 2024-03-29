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
package leikr.screens

import groovy.util.logging.Log4j2
import leikr.Engine
import leikr.GameRuntime
import leikr.loaders.EngineLoader
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.Texture
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition

import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 *
 * @author tor, pixelbath
 */
@Log4j2
class LoadScreen extends BasicGameScreen {

	public static final int ID = 4

	int frame = 0
	String loadPhrase = "Loading "
	String gameName
	final ArrayList<Integer> barItems

	final FitViewport viewport
	final EngineLoader engineLoader
	ExecutorService service
	Future<Engine> engineGetter

	final AssetManager assetManager
	final GameRuntime runtime

	LoadScreen(GameRuntime runtime, AssetManager assetManager, FitViewport vp, EngineLoader engineLoader, String gameName) {
		this.runtime = runtime
		this.assetManager = assetManager
		this.engineLoader = engineLoader
		this.gameName = gameName
		viewport = vp

		assetManager.load(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class)
		assetManager.finishLoading()

		barItems = [12, -56, 60]
	}

	void setGameName(String name) {
		gameName = name
	}

	@Override
	void preTransitionIn(Transition transition) {
		service = Executors.newFixedThreadPool(1)
		engineLoader.reset(runtime.getProgramsPath() + gameName)
		engineGetter = service.submit(engineLoader)
	}

	@Override
	void initialise(GameContainer gc) {
		service = Executors.newFixedThreadPool(1)
	}

	@Override
	void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
		if (engineGetter.isDone()) {
			try {
				EngineScreen es = (EngineScreen) sm.getGameScreen(EngineScreen.ID)
				es.setEngine(engineGetter.get(), runtime.getProgramsPath() + gameName)
				service.shutdown()
				sm.enterGameScreen(EngineScreen.ID, null, null)
			} catch (InterruptedException | ExecutionException ex) {
				service.shutdownNow()
				log.error("Failed to get Engine", ex)

				ErrorScreen es = (ErrorScreen) sm.getGameScreen(ErrorScreen.ID)
				String error = "Error loading engine: " + ex.getMessage()

				es.setErrorMessage(error)
				sm.enterGameScreen(ErrorScreen.ID, null, null)
			}
		}

		if (frame % 25 <= 5) {
			loadPhrase = ""
		} else if (frame % 25 <= 10) {
			loadPhrase = "."
		} else if (frame % 25 <= 15) {
			loadPhrase = ".."
		} else if (frame % 25 <= 20) {
			loadPhrase = "..."
		}
		translateArrayPositions()
		++frame
	}

	@Override
	void render(GameContainer gc, Graphics g) {
		viewport.apply(g)
		if (!engineGetter.isDone()) {
			// logo and loading
			if (assetManager.isLoaded(runtime.getDataPath() + "Images/leikr-logo.png")) {
				g.drawTexture(assetManager.get(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class), 80, 64, 48, 16)
			} else {
				assetManager.load(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class)
				assetManager.finishLoading()
			}

			g.with {
				setColor(Colors.WHITE())
				drawString("Loading", 96, 73)

				// draw game name
				setColor(Colors.rgbToColor(0 + "," + (155 + (frame * 2) % 100) + "," + 0))
				drawString(gameName + loadPhrase, 128, 73)

				// loading bar
				setColor(Colors.RED())
				fillRect(82 + Math.abs(barItems.get(0)), 80, 6, 4)
				setColor(Colors.GREEN())
				fillRect(82 + Math.abs(barItems.get(1)), 80, 6, 4)
				setColor(Colors.BLUE())
				fillRect(82 + Math.abs(barItems.get(2)), 80, 6, 4)

				setColor(Colors.WHITE())
				drawRect(82, 80, 80, 4)
			}
		}
	}

	@Override
	int getId() {
		ID
	}

	private void translateArrayPositions() {
		for (int i = 0; i < barItems.size(); i++) {
			Integer num = barItems.get(i)
			// separate the sign
			int sign = (num != 0) ? num / Math.abs(num) : 1
			num = Math.abs(num)

			if (sign == 1) {
				num++
			} else {
				num--
			}
			if (num <= 0 || num >= 74) {
				sign = -sign
			}
			barItems.set(i, num * sign)
		}
	}

}
