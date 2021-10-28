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
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.gdx.Input.Keys

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author tor
 */
class TitleScreen extends BasicGameScreen {

    public static final int ID = 2

    boolean CREDITS = false
    private double red, blue, green

    private int timer = 0
    private final int pixCount = 75
    private final int cycleLength = 25

    private ArrayList<TitleScreenPixel> pixels

    private final AssetManager assetManager
    private final FitViewport viewport
    private final PixelManager pixelManager
    private final GameRuntime runtime

    TitleScreen(AssetManager assetManager, FitViewport vp, PixelManager pixelManager, GameRuntime runtime) {
        this.assetManager = assetManager
        this.runtime = runtime
        assetManager.load(runtime.getDataPath() + "Images/leikr-logo.png", Texture.class)
        assetManager.finishLoading()

        viewport = vp
        this.pixelManager = pixelManager
    }

    void checkInput(ScreenManager sm) {
        if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || CREDITS) {
            CREDITS = false
            sm.enterGameScreen(MenuScreen.ID, null, null)
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.INFO, "Goodbye!")
            Mdx.platformUtils.exit(true)
        }
    }

    @Override
    void preTransitionOut(Transition out) {
    }

    @Override
    void initialise(GameContainer gc) {
        pixels = new ArrayList<>()

        pixCount.times{i->
            int x = (int) Math.floor(Math.random() * 8) + 91
            int height = (int) Math.floor(Math.random() * 7) + 4
            pixels.add(new TitleScreenPixel(x, 64, (i % 3) + 8, height, i * 5))
        }
    }

    @Override
    void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        checkInput(sm)
        if (Mdx.input.justTouched() || timer > 300) {
            CREDITS = true
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
                case 0: screen.drawPixel(g, pix.color, pix.x, pix.y);break
                case 1: screen.drawPixel(g, pix.color, pix.x, pix.y - 1);break
                case 2:
                    for (int i = 2; i < pix.height - 1; i++) {
                        screen.drawPixel(g, pix.color, pix.x, pix.y - i)
                    }
					break
                case 3: screen.drawPixel(g, pix.color, pix.x, pix.y - pix.height + 1);break
                case 4: screen.drawPixel(g, pix.color, pix.x, pix.y - pix.height);break
            }
        }
    }

    @Override
    int getId() {
        ID
    }
}
