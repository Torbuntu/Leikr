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
import leikr.GameRuntime
import leikr.managers.PixelManager
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.Texture
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.core.util.Align
import org.mini2Dx.gdx.Input.Keys

/**
 *
 * @author tor
 */
@Log4j2
class TitleScreen extends BasicGameScreen {

	public static final int ID = 2

	boolean credits = false, enterMapping = false
	double red, blue, green

	int timer = 0
	final int pixCount = 75
	final int cycleLength = 25

	ArrayList<TitleScreenPixel> pixels

	final AssetManager assetManager
	final FitViewport viewport
	final PixelManager pixelManager
	final GameRuntime runtime

	TitleScreen(AssetManager assetManager, FitViewport vp, PixelManager pixelManager, GameRuntime runtime) {
		this.assetManager = assetManager
		this.runtime = runtime
		assetManager.load(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class)
		assetManager.finishLoading()
		viewport = vp
		this.pixelManager = pixelManager
	}

	void checkInput(ScreenManager sm) {
		if (enterMapping && runtime.getInputManager().buttonAny()) {
			credits = false
			sm.enterGameScreen(ControllerMappingScreen.ID, null, null)
		}
		if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || runtime.getInputManager().buttonAny() || credits) {
			credits = false
			sm.enterGameScreen(MenuScreen.ID, null, null)
		}
		if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			log.info("Goodbye!")
			Mdx.platformUtils.exit(true)
		}
	}

	@Override
	void preTransitionIn(Transition transition) {
		if (Mdx.input.getGamePads().size() > 0) {
			runtime.getInputManager().createControllers()
			if (!runtime.getInputManager().checkMappingExists()) {
				enterMapping = true
			}
		}
	}

	@Override
	void preTransitionOut(Transition out) {
	}

	@Override
	void initialise(GameContainer gc) {
		pixels = new ArrayList<>()

		pixCount.times { i ->
			int x = (int) Math.floor(Math.random() * 8) + 91
			int height = (int) Math.floor(Math.random() * 7) + 4
			pixels.add(new TitleScreenPixel(x: x, y: 64, color: (i % 3) + 8, height: height, delay: i * 5))
		}
	}

	@Override
	void update(GameContainer gc, ScreenManager sm, float f) {
		checkInput(sm)
		if (Mdx.input.justTouched() || timer > 300) {
			credits = true
		}
		timer++
	}

	@Override
	void interpolate(GameContainer gc, float f) {
	}

	@Override
	void render(GameContainer gc, Graphics g) {
		viewport.apply(g)
		renderExtraText(g, "Game System", 106, 73, (int) (timer / 2))
		g.drawTexture(assetManager.get(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class), 90, 64, 48, 16)
		drawLogoSteam(pixelManager, g)

		if (enterMapping) {
			g.drawString("Unmapped controller detected. Press any button to enter mapping", 0, 130, 240, Align.CENTER)
		}
	}

	private void renderExtraText(Graphics graphics, String text, int x, int y, int step) {
		for (int i = 0; i < text.length(); i++) {
			red = 255 * (0.5 + 0.5 * Math.sin((step / 3) + 0 - i))
			green = 255 * (0.5 + 0.5 * Math.sin((step / 3) + 2 - i))
			blue = 255 * (0.5 + 0.5 * Math.sin((step / 3) + 4 - i))

			char c = text.charAt(i)
			int yPos = (int) (Math.sin(i + 1 + step) * (3.0 / ((step / 2) + 1)) * 3)

			if (step > cycleLength) {
				if ((step - cycleLength) / 2 > i) {
					red = blue = green = 255
				}
			}

			graphics.setColor(Colors.rgbToColor("$red,$blue,$green"))
			graphics.drawString(Character.toString(c), i * 4 + x, yPos + y)
		}
	}

	private void drawLogoSteam(PixelManager screen, Graphics g) {
		for (int p = 0; p < pixCount; p++) {
			TitleScreenPixel pix = pixels.get(p)
			switch (timer - pix.delay) {
				case 0: screen.drawPixel(g, pix.color, pix.x, pix.y); break
				case 1: screen.drawPixel(g, pix.color, pix.x, pix.y - 1); break
				case 2:
					for (int i = 2; i < pix.height - 1; i++) {
						screen.drawPixel(g, pix.color, pix.x, pix.y - i)
					}
					break
				case 3: screen.drawPixel(g, pix.color, pix.x, pix.y - pix.height + 1); break
				case 4: screen.drawPixel(g, pix.color, pix.x, pix.y - pix.height); break
			}
		}
	}

	@Override
	int getId() {
		ID
	}
}
