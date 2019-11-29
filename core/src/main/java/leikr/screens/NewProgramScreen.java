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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.NewProgramGenerator;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.gdx.InputProcessor;

/**
 *
 * @author tor
 */
public class NewProgramScreen extends BasicGameScreen {

    public static int ID = 5;
    FitViewport viewport;

    String prompt;
    String name;
    String template;

    String newLocation;
    NewProgramGenerator generator;

    protected enum GeneratorStep {
        NAME,
        TEMPLATE,
        TITLE,
        TYPE,
        AUTHOR,
        VERSION,
        PLAYERS,
        ABOUT,
        MAX_SPRITES,
        COMPILE_SOURCE,
        USE_COMPILED,
        CREATE,
        FINISHED,
        BACK,
        ERROR
    }

    private static GeneratorStep generatorStep;

    public NewProgramScreen(FitViewport vp) {
        viewport = vp;
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionOut(Transition transOut) {

    }

    @Override
    public void preTransitionIn(Transition trns) {
        prompt = "";
        name = "";
        template = "";
        generatorStep = GeneratorStep.TEMPLATE;
        generator = new NewProgramGenerator();
        Mdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int i) {
                if (generatorStep.equals(GeneratorStep.FINISHED)) {
                    if (i == Keys.Q || i == Keys.SPACE) {
                        generatorStep = GeneratorStep.BACK;
                    }
                }
                if (i == Keys.ESCAPE) {
                    generatorStep = GeneratorStep.BACK;
                }
                if (i == Keys.ENTER) {
                    progressStep();
                }
                if (i == Keys.BACKSPACE && prompt.length() > 0) {
                    prompt = prompt.substring(0, prompt.length() - 1);
                }
                return false;
            }

            @Override
            public boolean keyTyped(char c) {
                if (generatorStep != GeneratorStep.FINISHED) {
                    if ((int) c >= 32 && (int) c < 127) {
                        prompt = prompt + c;
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });

    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        switch (generatorStep) {
            case BACK:
                sm.enterGameScreen(TerminalScreen.ID, null, null);
                break;
            case CREATE:
                try {
                    newLocation = NewProgramGenerator.setNewProgramFileName(name, template);
                    generator.writeProperties(name);
                    generatorStep = GeneratorStep.FINISHED;
                } catch (IOException ex) {
                    Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex);
                    ErrorScreen.setErrorMessage(ex.getMessage());
                    sm.enterGameScreen(ErrorScreen.ID, null, null);
                }
                break;
            case ERROR:
                sm.enterGameScreen(ErrorScreen.ID, null, null);
                break;

        }
        
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        renderSteps(g);

    }

    void renderSteps(Graphics g) {
        g.setColor(Colors.GREEN());
        switch (generatorStep) {
            case TEMPLATE:
                g.drawString("Enter Template (Default): ", 0, 0);
                break;
            case NAME:
                g.drawString("Enter New Program Name: ", 0, 0);
                break;
            case TITLE:
                g.drawString("Enter Title (unknown): ", 0, 0);
                break;
            case TYPE:
                g.drawString("Enter Type (Program): ", 0, 0);
                break;
            case AUTHOR:
                g.drawString("Enter Author (unknown): ", 0, 0);
                break;
            case VERSION:
                g.drawString("Enter Version (0.1): ", 0, 0);
                break;
            case PLAYERS:
                g.drawString("Enter Players (1): ", 0, 0);
                break;
            case ABOUT:
                g.drawString("Enter About (A Leikr Program.): ", 0, 0);
                break;
            case MAX_SPRITES:
                g.drawString("Enter Max Sprites (128): ", 0, 0);
                break;
            case COMPILE_SOURCE:
                g.drawString("Enter Compile Source (false): ", 0, 0);
                break;
            case USE_COMPILED:
                g.drawString("Enter Use Compiled (false): ", 0, 0);
                break;
            case FINISHED:
                g.drawString(newLocation, 0, 0, 232);
                g.setColor(Colors.BLACK());
                g.drawRect(0, 152, 240, 8);
                g.setColor(Colors.GREEN());
                g.drawString(":q to quit", 0, 152);
                break;
        }
        g.drawString(prompt, 0, 12, 232);
    }

    void progressStep() {
        switch (generatorStep) {
            case TEMPLATE:
                template = prompt.length() == 0 ? "Default" : prompt;
                generatorStep = GeneratorStep.NAME;
                break;
            case NAME:
                if (prompt.length() == 0) {
                    break;
                }
                try {
                    for (FileHandle pn : Mdx.files.local("Programs").list()) {
                        if (pn.name().equalsIgnoreCase(prompt)) {
                            ErrorScreen.setErrorMessage("A program with name [" + prompt + "] already exists.");
                            generatorStep = GeneratorStep.ERROR;
                            return;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex);
                    ErrorScreen.setErrorMessage(ex.getMessage());
                    generatorStep = GeneratorStep.ERROR;
                }
                name = prompt;
                generatorStep = GeneratorStep.TITLE;
                break;

            case TITLE:
                generator.TITLE = prompt.length() == 0 ? "unknown" : prompt;
                generatorStep = GeneratorStep.TYPE;
                break;
            case TYPE:
                generator.TYPE = prompt.length() == 0 ? "Program" : prompt;;
                generatorStep = GeneratorStep.AUTHOR;
                break;
            case AUTHOR:
                generator.AUTHOR = prompt.length() == 0 ? "unknown" : prompt;;
                generatorStep = GeneratorStep.VERSION;
                break;
            case VERSION:
                generator.VERSION = prompt.length() == 0 ? "0.1" : prompt;;
                generatorStep = GeneratorStep.PLAYERS;
                break;
            case PLAYERS:
                generator.PLAYERS = prompt.length() == 0 ? "1" : prompt;;
                generatorStep = GeneratorStep.ABOUT;
                break;
            case ABOUT:
                generator.ABOUT = prompt.length() == 0 ? "A Leikr Program." : prompt;;
                generatorStep = GeneratorStep.MAX_SPRITES;
                break;
            case MAX_SPRITES:
                generator.MAX_SPRITES = prompt.length() == 0 ? "128" : prompt;;
                generatorStep = GeneratorStep.COMPILE_SOURCE;
                break;
            case COMPILE_SOURCE:
                generator.COMPILE_SOURCE = prompt.length() == 0 ? "false" : prompt;;
                generatorStep = GeneratorStep.USE_COMPILED;
                break;
            case USE_COMPILED:
                generator.USE_COMPILED = prompt.length() == 0 ? "false" : prompt;;
                generatorStep = GeneratorStep.CREATE;
                break;
        }
        prompt = "";

    }

    @Override
    public int getId() {
        return ID;
    }

}
