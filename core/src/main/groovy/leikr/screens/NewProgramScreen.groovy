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
package leikr.screens

import leikr.GameRuntime
import leikr.utilities.NewProgramGenerator
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.files.FileHandle
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.gdx.Input.Keys
import org.mini2Dx.gdx.InputProcessor

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author tor
 */
class NewProgramScreen extends BasicGameScreen {

    static int ID = 5
    private static GeneratorStep generatorStep
    String prompt, name, template, errorMessage
    private String newLocation
    private final FitViewport viewport
    private final NewProgramGenerator generator

    NewProgramScreen(FitViewport vp, GameRuntime runtime) {
        viewport = vp
        generator = new NewProgramGenerator(runtime)
    }

    @Override
    void initialise(GameContainer gc) {
    }

    @Override
    void preTransitionOut(Transition transOut) {

    }

    @Override
    void preTransitionIn(Transition trns) {
        prompt = ""
        name = ""
        template = ""
        generatorStep = GeneratorStep.TEMPLATE
        Mdx.input.setInputProcessor(new InputProcessor() {
            @Override
            boolean keyDown(int i) {
                if (generatorStep == GeneratorStep.FINISHED) {
                    if (i == Keys.Q || i == Keys.SPACE) {
                        generatorStep = GeneratorStep.BACK
                    }
                }
                if (i == Keys.ESCAPE) {
                    generatorStep = GeneratorStep.BACK
                }
                if (i == Keys.ENTER) {
                    progressStep()
                }
                if (i == Keys.BACKSPACE && prompt.length() > 0) {
                    prompt = prompt.substring(0, prompt.length() - 1)
                }
                return false
            }

            @Override
            boolean keyTyped(char c) {
                if (generatorStep != GeneratorStep.FINISHED) {
                    if ((int) c >= 32 && (int) c < 127) {
                        prompt = prompt + c
                    }
                }
                return true
            }

            @Override
            boolean keyUp(int keycode) {
                return false
            }

            @Override
            boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false
            }

            @Override
            boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false
            }

            @Override
            boolean touchDragged(int screenX, int screenY, int pointer) {
                return false
            }

            @Override
            boolean mouseMoved(int screenX, int screenY) {
                return false
            }

            @Override
            boolean scrolled(float amount, float i) {
                return false
            }
        })

    }

    @Override
    void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        switch (generatorStep) {
            case GeneratorStep.BACK:
				sm.enterGameScreen(TerminalScreen.ID, null, null)
				break
            case GeneratorStep.CREATE:
                try {
                    newLocation = generator.setNewProgramFileName(name, template)
                    generator.writeProperties(name)
                    generatorStep = GeneratorStep.FINISHED
                } catch (IOException ex) {
                    Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex)
                    ErrorScreen es = (ErrorScreen) sm.getGameScreen(ErrorScreen.ID)
                    es.setErrorMessage(ex.getMessage())
                    sm.enterGameScreen(ErrorScreen.ID, null, null)
                }
				break
            case GeneratorStep.ERROR:
                ErrorScreen es = (ErrorScreen) sm.getGameScreen(ErrorScreen.ID)
                es.setErrorMessage(errorMessage)
                sm.enterGameScreen(ErrorScreen.ID, null, null)
				break
        }
    }

    @Override
    void interpolate(GameContainer gc, float f) {
    }

    @Override
    void render(GameContainer gc, Graphics g) {
        viewport.apply(g)
        renderSteps(g)
    }

    void renderSteps(Graphics g) {
        g.setColor(Colors.GREEN())
        switch (generatorStep) {
            case GeneratorStep.TEMPLATE:
                g.drawString("Enter Template (Default): ", 0, 0)
				break
            case GeneratorStep.NAME:
                g.drawString("Enter New Program Name: ", 0, 0)
				break
            case GeneratorStep.TITLE:
                g.drawString("Enter Title (unknown): ", 0, 0)
				break
            case GeneratorStep.TYPE:
                g.drawString("Enter Type (Program): ", 0, 0)
				break
            case GeneratorStep.AUTHOR:
                g.drawString("Enter Author (unknown): ", 0, 0)
				break
            case GeneratorStep.VERSION:
                g.drawString("Enter Version (0.1): ", 0, 0)
				break
            case GeneratorStep.PLAYERS:
                g.drawString("Enter Players (1): ", 0, 0)
				break
            case GeneratorStep.ABOUT:
                g.drawString("Enter About (A Leikr Program.): ", 0, 0)
				break
            case GeneratorStep.MAX_SPRITES:
                g.drawString("Enter Max Sprites (128): ", 0, 0)
				break
            case GeneratorStep.COMPILE_SOURCE:
                g.drawString("Enter Compile Source (false): ", 0, 0)
				break
            case GeneratorStep.USE_COMPILED:
                g.drawString("Enter Use Compiled (false): ", 0, 0)
				break
            case GeneratorStep.FINISHED:
				g.with{
					drawString(newLocation, 0, 0, 232)
					setColor(Colors.BLACK())
					drawRect(0, 152, 240, 8)
					setColor(Colors.GREEN())
					drawString(":q to quit", 0, 152)
				}
				break
        }
        g.drawString(prompt, 0, 12, 232)
    }

    void progressStep() {
        switch (generatorStep) {
            case GeneratorStep.TEMPLATE: 
                template = prompt.length() == 0 ? "Default" : prompt
                generatorStep = GeneratorStep.NAME
				break
            case GeneratorStep.NAME: 
                if (prompt.length() > 0) {
					try {
						for (FileHandle pn : Mdx.files.local("Programs").list()) {
							if (pn.name().equalsIgnoreCase(prompt)) {
								errorMessage = "A program with name [" + prompt + "] already exists."
								generatorStep = GeneratorStep.ERROR
								return
							}
						}
					} catch (IOException ex) {
						Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex)
						errorMessage = ex.getMessage()
						generatorStep = GeneratorStep.ERROR
					}
					name = prompt
					generatorStep = GeneratorStep.TITLE
				}
            
				break
            case GeneratorStep.TITLE: 
                generator.setTitle(prompt.length() == 0 ? "unknown" : prompt)
                generatorStep = GeneratorStep.TYPE
				 break
            case GeneratorStep.TYPE: 
                generator.setType(prompt.length() == 0 ? "Program" : prompt)
                generatorStep = GeneratorStep.AUTHOR
				break
            case GeneratorStep.AUTHOR: 
                generator.setAuthor(prompt.length() == 0 ? "unknown" : prompt)
                generatorStep = GeneratorStep.VERSION
				break
            case GeneratorStep.VERSION: 
                generator.setVersion(prompt.length() == 0 ? "0.1" : prompt)
                generatorStep = GeneratorStep.PLAYERS
				break
            case GeneratorStep.PLAYERS: 
                generator.setPlayers(prompt.length() == 0 ? "1" : prompt)
                generatorStep = GeneratorStep.ABOUT
				break
            case GeneratorStep.ABOUT: 
                generator.setAbout(prompt.length() == 0 ? "A Leikr Program." : prompt)
                generatorStep = GeneratorStep.MAX_SPRITES
				break
            case GeneratorStep.MAX_SPRITES: 
                generator.setMaxSprites(prompt.length() == 0 ? "128" : prompt)
                generatorStep = GeneratorStep.COMPILE_SOURCE
				break
            case GeneratorStep.COMPILE_SOURCE:
                generator.setCompileSource(prompt.length() == 0 ? "false" : prompt)
                generatorStep = GeneratorStep.USE_COMPILED
				break
            case GeneratorStep.USE_COMPILED: 
                generator.setUseCompiled(prompt.length() == 0 ? "false" : prompt)
                generatorStep = GeneratorStep.CREATE
				break
        }
        prompt = ""

    }

    @Override
    int getId() {
        ID
    }
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

}
