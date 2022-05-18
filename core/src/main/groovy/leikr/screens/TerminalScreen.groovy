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
import leikr.managers.TerminalManager
import leikr.managers.TerminalManager.TerminalState
import leikr.utilities.ExportTool
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.gdx.Input.Keys
/**
 *
 * @author Torbuntu
 */
class TerminalScreen extends BasicGameScreen {

    public static final int ID = 6

    int blink = 0

    final TerminalManager terminalManager
    final FitViewport viewport
    final GameRuntime runtime
    final ExportTool exportTool

    TerminalScreen(FitViewport vp, TerminalManager terminalManager, GameRuntime runtime) {
        this.runtime = runtime
        this.terminalManager = terminalManager
        viewport = vp
        exportTool = new ExportTool(runtime)
    }

    @Override
    void initialise(GameContainer gc) {
    }

    @Override
    void preTransitionIn(Transition transition) {
        terminalManager.init()
        Mdx.input.setInputProcessor(terminalManager)
    }

    @Override
    void update(GameContainer gc, ScreenManager sm, float delta) {
        terminalManager.update()
        if (runtime.checkFileDropped()) {
            if (runtime.getFileDroppedTitle().endsWith(".lkr")) {
                terminalManager.setState(TerminalState.INSTALLING)
            } else {
                LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
                ls.setGameName(runtime.getFileDroppedTitle())
                runtime.setGameName(runtime.getFileDroppedTitle())
                runtime.clearFileDropped()
                sm.enterGameScreen(LoadScreen.ID, null, null)
            }
        }

        switch (terminalManager.getState()) {
            case TerminalState.INSTALLING:
                if (Mdx.input.isKeyJustPressed(Keys.Y)) {
                    String title = runtime.getFileDroppedTitle().substring(0, runtime.getFileDroppedTitle().lastIndexOf('.'))
                    String success = exportTool.importProject(title, runtime.getProgramsPath())
                    if (!success.startsWith("[E]")) {

                        LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
                        ls.setGameName(title)
                        runtime.setGameName(title)
                        runtime.clearFileDropped()
                        sm.enterGameScreen(LoadScreen.ID, null, null)
                    }
                }
                if (Mdx.input.isKeyJustPressed(Keys.N)) {
                    runtime.clearFileDropped()
                    terminalManager.setState(TerminalState.PROCESSING)
                }
				break
            case TerminalState.RUN_PROGRAM:
                LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
                ls.setGameName(runtime.getGameName())
                sm.enterGameScreen(LoadScreen.ID, null, null)
				break
            case TerminalState.NEW_PROGRAM:
                sm.enterGameScreen(NewProgramScreen.ID, null, null)
				break
        }

        blink++
        if (blink > 60) {
            blink = 0
        }
    }

    @Override
    void render(GameContainer gc, Graphics g) {
        viewport.apply(g)
        g.setColor(Colors.GREEN())

        if(terminalManager.getState()==TerminalState.INSTALLING){
			g.drawString("Install and run program [${runtime.getFileDroppedTitle()}]? [Y/N]", 0, 60, runtime.WIDTH, 1)
		}else{
			g.with{
				drawString(terminalManager.getHistoryText(), 0, 0, 240)
				setColor(Colors.BLACK())
				fillRect(0, 152, runtime.WIDTH, runtime.HEIGHT)
				setColor(Colors.GREEN())
				drawString(">" + terminalManager.getPrompt() + ((blink > 30) ? "|" : ""), 0, 152, runtime.WIDTH)

				if (Mdx.input.isKeyDown(Keys.CONTROL_LEFT)) {
					setColor(Colors.RED())
					drawString("Ctrl", 0, 146, runtime.WIDTH, 1)
				}
			}
        }

    }

    @Override
    int getId() {
        ID
    }

}
