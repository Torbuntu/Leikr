/*
 * Copyright 2019 torbuntu.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import groovy.lang.GroovyShell;
import leikr.GameRuntime;
import leikr.loaders.FontLoader;
import leikr.terminal.TerminalCommands;
import org.mini2Dx.core.font.MonospaceFont;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 */
public class TerminalScreen extends BasicGameScreen {

    public static int ID = 7;
    FitViewport viewport;

    StringBuilder input;
    StringBuilder history;
    private final AssetManager assetManager;
    FontLoader fontLoader;
    MonospaceFont font;
    GroovyShell shell;
    TerminalCommands tCommands;

    public TerminalScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
        shell = new GroovyShell();
        fontLoader = new FontLoader();
        font = fontLoader.getFont(assetManager);
        tCommands = new TerminalCommands();
    }

    @Override
    public void onResize(int width, int height) {
        Gdx.app.log("INFO", "Game window changed to " + width + "x" + height);
        viewport.onResize(width, height);
    }

    @Override
    public void postTransitionIn(Transition in) {
        input = new StringBuilder();
        history = new StringBuilder();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    history.append(input);
                    try {
                        String value = shell.evaluate(input.toString()).toString();
                        input.setLength(0);
                        input.append(value);
                        history.append(tCommands.processCommand(input.toString()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (keycode == Input.Keys.ESCAPE) {
                    System.out.println("Goodbye!");
                    Gdx.app.exit();
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                System.out.println(Integer.valueOf(character));
                if (character == 0 || character == 13) {
                    return true;
                }
                if(character == 8){
                    if(input.length() > 0) input.deleteCharAt(input.length()-1);
                    return true;
                }
                input.append(character);
                return true;
            }
        });
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        font.load(assetManager);
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setFont(font);
        viewport.apply(g);
        g.drawString(input.toString(), 0, 0, 232);

    }

    @Override
    public int getId() {
        return ID;
    }

}
