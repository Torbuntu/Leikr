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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor, pixelbath
 */
public class LoadScreen extends BasicGameScreen {

    public static int ID = 4;

    FitViewport viewport;
    ExecutorService service;
    Future engineGetter;

    AssetManager assetManager;
    String loadPhrase = "Loading ";
    int frame = 0;
    ArrayList<Integer> barItems;

    public LoadScreen(AssetManager assetManager, FitViewport vp) {
        this.assetManager = assetManager;
        assetManager.load("./Data/Images/leikr-logo.png", Texture.class);
        assetManager.finishLoading();
        
        service = Executors.newFixedThreadPool(1);
        viewport = vp;

        barItems = new ArrayList<>();
        barItems.add(12);
        barItems.add(-56);
        barItems.add(60);
    }

    @Override
    public void preTransitionIn(Transition transition) {
        engineGetter = service.submit(EngineLoader.getEngineLoader(true));
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (engineGetter.isDone()) {
            try {
                EngineScreen scrn = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
                scrn.setEngine((Engine) engineGetter.get());
                sm.enterGameScreen(EngineScreen.ID, null, null);
            } catch (InterruptedException | ExecutionException ex) {
                ErrorScreen.setErrorMessage("Error loading engine: " + ex.getMessage());
                sm.enterGameScreen(ErrorScreen.ID, null, null);
                Logger.getLogger(LoadScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (frame % 25 <= 5) {
            loadPhrase = "";
        } else if (frame % 25 <= 10) {
            loadPhrase = ".";
        } else if (frame % 25 <= 15) {
            loadPhrase = "..";
        } else if (frame % 25 <= 20) {
            loadPhrase = "...";
        }
        translateArrayPositions();
        ++frame;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        if (!engineGetter.isDone()) {
            // logo and loading
            g.drawTexture(assetManager.get("./Data/Images/leikr-logo.png", Texture.class), 80, 64, 48, 16);
            g.setColor(Colors.WHITE());
            g.drawString("Loading", 96, 73);

            // draw game name
            g.setColor(Colors.rgbToColor(0 + "," + (155 + (frame * 2) % 100) + "," + 0));
            g.drawString(GameRuntime.GAME_NAME + loadPhrase, 128, 73);
            
            // loading bar
            g.setColor(Colors.RED());
            g.fillRect(82+Math.abs(barItems.get(0)), 80, 6, 4);
            g.setColor(Colors.GREEN());
            g.fillRect(82+Math.abs(barItems.get(1)), 80, 6, 4);
            g.setColor(Colors.BLUE());
            g.fillRect(82+Math.abs(barItems.get(2)), 80, 6, 4);

            g.setColor(Colors.WHITE());
            g.drawRect(82, 80, 80, 4);
        }
    }

    @Override
    public int getId() {
        return ID;
    }

    private void translateArrayPositions() {
        for (int i=0; i<barItems.size(); i++) {
            Integer num = barItems.get(i);
            // separate the sign
            int sign = (num != 0) ? num / Math.abs(num) : 1;
            num = Math.abs(num);

            if (sign == 1) {
                num++;
            } else {
                num--;
            }
            if (num <= 0 || num >= 74) {
                sign = -sign;
            }
            barItems.set(i, num * sign);
        }
    }

}
