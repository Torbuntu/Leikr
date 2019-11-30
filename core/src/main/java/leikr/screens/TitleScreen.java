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
package leikr.screens;

import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.tiled.TiledMap;

import leikr.managers.LeikrScreenManager;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 *
 * @author tor
 */
public class TitleScreen extends BasicGameScreen {

    public static int ID = 2;
    AssetManager assetManager;
    FitViewport viewport;
    boolean CREDITS = false;

    int timer = 0;
    ArrayList<SteamPixel> pixels;
    ArrayList<Integer> rgb;
    int pixCount = 75;
    int cycleLength = 25;

    public LeikrScreenManager lScreen;

    public TitleScreen(AssetManager assetManager, FitViewport vp) {
        this.assetManager = assetManager;
        assetManager.load("./Data/Images/leikr-logo.png", Texture.class);
        assetManager.finishLoading();

        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        lScreen = LeikrScreenManager.getLeikrScreenManager(30);
    }

    void checkInput(ScreenManager sm) {
        if (Mdx.input.isKeyJustPressed(Keys.SPACE) || Mdx.input.isKeyJustPressed(Keys.ENTER) || CREDITS) {
            CREDITS = false;
            sm.enterGameScreen(TerminalScreen.ID, null, null);
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            System.out.println("Goodbye!");
            Mdx.platformUtils.exit(true);
        }
    }

    @Override
    public void preTransitionOut(Transition out) {
    }

    @Override
    public void initialise(GameContainer gc) {
        rgb = new ArrayList<>();
        rgb.add(8);
        rgb.add(9);
        rgb.add(10);

        pixels = new ArrayList<>();

        for (int i=0; i<pixCount; i++) {
            int clr = rgb.get(i % 3);
            int x = (int)Math.floor(Math.random() * 8) + 91;
            pixels.add(new SteamPixel(x, 64, clr, i*5));
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        checkInput(sm);
        if (Mdx.input.justTouched() || timer > 300) {
            CREDITS = true;
        }
        timer++;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        //g.drawString("Game System", 106, 73);
        renderExtraText(lScreen, "Game System", 106, 73, (int)(timer / 2));
        g.drawTexture(assetManager.get("./Data/Images/leikr-logo.png", Texture.class), 90, 64, 48, 16);

        for (int i=0; i<pixels.size()-1; i++) {
            pixels.get(i).draw(lScreen);
            pixels.get(i).update();
        }
    }

    private void renderExtraText(LeikrScreenManager g, String text, int x, int y, int step) {
        for (int i = 0; i < text.length(); i++){
            double red   = 255*(0.5+0.5*Math.sin((step/3)+0-i));
            double green = 255*(0.5+0.5*Math.sin((step/3)+2-i));
            double blue  = 255*(0.5+0.5*Math.sin((step/3)+4-i));
            // charColor.set(red, blue, green, 255);
            //g.setColor();

            char c = text.charAt(i);
            int yPos = (int)(Math.sin(i+1+step) * (3.0/((step/2)+1)) * 3);

            if (step > cycleLength) {
                if ((step - cycleLength)/2 > i) {
                    red = 255; blue = 255; green = 255;
                }
            }
            g.drawString(red+","+blue+","+green, Character.toString(c), i*4+x, yPos+y);
        }
    }

    @Override
    public int getId() {
        return ID;
    }
}

/**
 *
 * @author pixelbath
 */
class SteamPixel {
    int x = 0;
    int y = 0;
    int color = 1;
    int step = 0;
    int height = 5;
    int delay = 0;

    SteamPixel(int xPos, int yPos, int colorIndex, int delayAmt) {
        x = xPos;
        y = yPos;
        color = colorIndex;
        delay = delayAmt;
        height = (int)Math.floor(Math.random() * 7) + 4;
    }

    public void draw(LeikrScreenManager screen) {
        switch (step - delay) {
            case 0:
                screen.drawPixel(color, x, y);
                break;
            case 1:
                screen.drawPixel(color, x, y-1);
                break;
            case 2:
                for (int i=2; i<height-1; i++) {
                    screen.drawPixel(color, x, y-i);
                }
                break;
            case 3:
                screen.drawPixel(color, x, y-height+1);
                break;
            case 4:
                screen.drawPixel(color, x, y-height);
                break;
            default:
                return;
        }
    }

    public void update() {
        step++;
    }
}