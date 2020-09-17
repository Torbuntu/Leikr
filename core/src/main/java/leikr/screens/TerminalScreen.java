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
import leikr.loaders.EngineLoader;
import leikr.managers.TerminalManager;
import leikr.managers.TerminalManager.TerminalState;
import leikr.utilities.ExportTool;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;

/**
 *
 * @author Torbuntu
 */
public class TerminalScreen extends BasicGameScreen {

    public static final int ID = 6;

    private int blink = 0;

    private final TerminalManager terminalManager;
    private final FitViewport viewport;
    private final EngineLoader engineLoader;
    private final GameRuntime runtime;
    private final ExportTool exportTool;

    public TerminalScreen(FitViewport vp, TerminalManager terminalManager, EngineLoader engineLoader, GameRuntime runtime) {
        this.runtime = runtime;
        this.terminalManager = terminalManager;
        this.engineLoader = engineLoader;
        viewport = vp;
        exportTool = new ExportTool();
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionIn(Transition trns) {
        terminalManager.init();
        Mdx.input.setInputProcessor(terminalManager);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        terminalManager.update();
        if (runtime.checkFileDropped()) {
            if (runtime.getFileDroppedTitle().endsWith(".lkr")) {
                terminalManager.setState(TerminalState.INSTALLING);
            } else {
                LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
                ls.setGameName(runtime.getFileDroppedTitle());
                runtime.setGameName(runtime.getFileDroppedTitle());
                runtime.clearFileDropped();
                sm.enterGameScreen(LoadScreen.ID, null, null);
            }
        }

        switch (terminalManager.getState()) {
            case INSTALLING -> {
                if (Mdx.input.isKeyJustPressed(Keys.Y)) {
                    String title = runtime.getFileDroppedTitle().substring(0, runtime.getFileDroppedTitle().lastIndexOf('.'));
                    String success = exportTool.importProject(title, "Programs");
                    if (!success.startsWith("[E]")) {

                        LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
                        ls.setGameName(title);
                        runtime.setGameName(title);
                        runtime.clearFileDropped();
                        sm.enterGameScreen(LoadScreen.ID, null, null);
                    }
                }
                if (Mdx.input.isKeyJustPressed(Keys.N)) {
                    runtime.clearFileDropped();
                    terminalManager.setState(TerminalState.PROCESSING);
                }
            }
            case RUN_PROGRAM -> {
                LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID);
                ls.setGameName(runtime.getGameName());
                sm.enterGameScreen(LoadScreen.ID, null, null);
            }
            case NEW_PROGRAM ->
                sm.enterGameScreen(NewProgramScreen.ID, null, null);
            case RUN_UTILITY -> {
                engineLoader.setRunTool();
                sm.enterGameScreen(LoadScreen.ID, null, null);
            }
        }

        blink++;
        if (blink > 60) {
            blink = 0;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Colors.GREEN());

        switch (terminalManager.getState()) {
            case INSTALLING -> {
                g.drawString("Install and run program [" + runtime.getFileDroppedTitle() + "]? [Y/N]", 0, 60, runtime.WIDTH, 1);
            }
            default -> {
                g.drawString(terminalManager.getHistoryText(), 0, 0, 240);
                g.setColor(Colors.BLACK());
                g.fillRect(0, 152, runtime.WIDTH, runtime.HEIGHT);
                g.setColor(Colors.GREEN());
                g.drawString(">" + terminalManager.getPrompt() + ((blink > 30) ? (char) 131 : ""), 0, 152, runtime.WIDTH);

                if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT)) {
                    g.setColor(Colors.RED());
                    g.drawString("Ctrl", 0, 146, runtime.WIDTH, 1);
                }
            }
        }

    }

    @Override
    public int getId() {
        return ID;
    }

}
