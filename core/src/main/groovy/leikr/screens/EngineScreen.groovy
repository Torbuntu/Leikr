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

import com.badlogic.gdx.Gdx
import groovy.util.logging.Log4j2

import leikr.Engine
import leikr.GameRuntime
import leikr.managers.ManagerDTO
import leikr.loaders.EngineLoader
import leikr.managers.SystemManager
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.gdx.Input.Keys

/**
 *
 * @author tor
 */
@Log4j2
class EngineScreen extends BasicGameScreen {

	public static final int ID = 1

	String errorMessage
	boolean CONFIRM = false
	String path

	Engine engine
	FrameBuffer frameBuffer
	final EngineLoader engineLoader
	final FitViewport mainViewport
	final GameRuntime runtime
	final SystemManager systemManager
	final StretchViewport fboViewport
	final ManagerDTO managerDTO

	EngineState engineState

	EngineScreen(FitViewport vp, ManagerDTO managerDTO, EngineLoader engineLoader, GameRuntime runtime) {
		fboViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT)
		mainViewport = vp
		this.managerDTO = managerDTO
		this.engineLoader = engineLoader
		this.runtime = runtime
		this.systemManager = managerDTO.systemManager
	}

	@Override
	void initialise(GameContainer gc) {
	}

	@Override
	void postTransitionOut(Transition transition) {
		if (null != engine) {
			// release all Engine objects for gc
			engine.with {
				setActive(false)
				dispose()
			}
			engine = null
		}
		frameBuffer.dispose()
		// Reset args to an empty array.
		log.info("Engine classes disposed.")
	}

	@Override
	void preTransitionIn(Transition trans) {
		engineState = EngineState.RUNNING
		Gdx.input.setCursorCatched(true)
		frameBuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT)
	}

	@Override
	void postTransitionIn(Transition transition) {
		if (engineState == EngineState.ERROR) {
			return
		}
		try {
			systemManager.setRunning(true)
			engine.with {
				preCreate(path, engineLoader.getMaxSprite(), managerDTO, fboViewport, frameBuffer)
				create(engineLoader.getEngineArgs())
				create()
			}
		} catch (Exception ex) {
			engineState = EngineState.ERROR
			errorMessage = "Error in program `create` method. " + ex.getLocalizedMessage()
			log.error(ex)
		}
	}

	@Override
	void preTransitionOut(Transition transition) {
		Gdx.input.setCursorCatched(false)
		//runtime.getCursor().setVisible(false)
	}

	@Override
	void update(GameContainer gc, ScreenManager sm, float delta) {
		if (Mdx.input.isKeyJustPressed(Keys.F5) || Mdx.input.isKeyDown(Keys.CONTROL_LEFT) && Mdx.input.isKeyJustPressed(Keys.R) || Mdx.input.isKeyJustPressed(Keys.HOME)) {
			reloadEngine(sm)
		}

		// Checking for pause. We pause on keyboard ESCAPE, or if a pause was triggered in the game's code.
		if (Mdx.input.isKeyJustPressed(Keys.ESCAPE) || systemManager.checkShouldPause()) {
			systemManager.pause(false)
			if (engineState == EngineState.PAUSE) {
				resume()
			} else {
				pause()
			}
		}

		// Main update loop for the game.
		switch (engineState) {
			case EngineState.BACK:
				systemManager.resetFont()
				enterMenuScreen(sm)
				break
			case EngineState.ERROR:
				systemManager.resetFont()
				enterErrorScreen(sm)
				break
			case EngineState.RUNNING:
				try {
					engine.with {
						preUpdate(delta)
						update(delta)
						update()
					}
				} catch (Exception ex) {
					engineState = EngineState.ERROR
					errorMessage = "Error in program `update` method. ${ex.getLocalizedMessage()}"
					log.error(ex)
				}
				break
			case EngineState.PAUSE:
				Mdx.input.with {
					if (isKeyDown(Keys.CONTROL_LEFT)) {
						if (isKeyJustPressed(Keys.NUM_1)) {
							Gdx.graphics.setWindowedMode(240, 160)
						}
						if (isKeyJustPressed(Keys.NUM_2)) {
							Gdx.graphics.setWindowedMode(240 * 2, 160 * 2)
						}
						if (isKeyJustPressed(Keys.NUM_3)) {
							Gdx.graphics.setWindowedMode(240 * 3, 160 * 3)
						}
						if (isKeyJustPressed(Keys.NUM_4)) {
							Gdx.graphics.setWindowedMode(240 * 4, 160 * 4)
						}
					}
					if (isKeyJustPressed(Keys.LEFT) || runtime.getInputManager().buttonPress("LEFT")) {
						CONFIRM = true
					}
					if (isKeyJustPressed(Keys.RIGHT) || runtime.getInputManager().buttonPress("RIGHT")) {
						CONFIRM = false
					}
					if (isKeyJustPressed(Keys.ENTER) || isKeyJustPressed(Keys.K) || runtime.getInputManager().buttonPress("START")) {
						resume()
					}
				}
				break
		}

		if (!systemManager.update(sm)) {
			systemManager.resetFont()
			log.warn("Transition initiated from running program.")
		}
	}

	/**
	 * Rendering process:
	 * 1. Apply StretchViewport for the framebuffer to render on
	 * 2. Begin the frame buffer
	 * 3. Flush graphics object to frame buffer
	 * 4. End the frame buffer ops
	 * 5. Apply the FitViewport to the graphics object
	 * 6. Render the frame buffer's texture
	 * @param gc
	 * @param g
	 */
	@Override
	void render(GameContainer gc, Graphics g) {
		if (!engine?.getActive()) {
			return
		}

		//Apply a StretchViewport so that all FBO operations are scaled correctly to the fbo.
		fboViewport.apply(g)

		// Main preRender methods
		frameBuffer.begin()
		switch (engineState) {
			case EngineState.RUNNING: renderRunning(g); break
			case EngineState.PAUSE: renderPause(g); break
		}

		// We have to flush to the framebuffer
		g.flush()
		frameBuffer.end()

		//Apply a FitViewport so that the 240x160 and aspect ratio is correct.
		mainViewport.apply(g)
		g.drawTexture(frameBuffer.getTexture(), 0, 0, false)
	}

	@Override
	int getId() {
		ID
	}

	private void renderRunning(Graphics g) {
		try {
			engine.preRender(g)
			engine.render()
		} catch (Exception ex) {
			engineState = EngineState.ERROR
			errorMessage = "Error in program `render` method. ${ex.getLocalizedMessage()}"
			log.error(ex)
		}
	}

	private void renderPause(Graphics g) {
		g.with {
			clearContext(Colors.BLACK())

			//Call shape rendering first, to ensure that the shape renderer draws and ends clean.
			if (CONFIRM) {
				setColor(Colors.GREEN())
				drawRect(78, 86, 36, 16)
			} else {
				setColor(Colors.RED())
				drawRect(130, 86, 36, 16)
			}
			setColor(Colors.WHITE())
			drawString("-- Paused --", 0, 60, runtime.WIDTH, 1)
			drawString("Exit running program?", 0, 74, runtime.WIDTH, 1)
			drawString("Yes           No", 0, 90, runtime.WIDTH, 1)
		}
	}

	private void pause() {
		Gdx.input.setCursorCatched(false)
		//runtime.getCursor().setVisible(false)
		engineState = EngineState.PAUSE
		engine.pauseAudio()
		engine.onPause()
	}

	private void resume() {
		if (CONFIRM) {
			engineState = EngineState.BACK
		} else {
			Gdx.input.setCursorCatched(true)
			engineState = EngineState.RUNNING
			//engine.lAudio.resumeAllAudio()
			engine.onResume()
		}
	}

	void enterMenuScreen(ScreenManager sm) {
		if (engine) {
			engine.setActive(false)
		}
		if (runtime.checkDirectLaunch()) {
			Mdx.platformUtils.exit(false)
		} else {
			sm.enterGameScreen(MenuScreen.ID, null, null)
		}
	}

	void enterErrorScreen(ScreenManager sm) {
		if (engine) {
			engine.setActive(false)
		}
		if (runtime.checkDirectLaunch()) {
			sm.enterGameScreen(TitleScreen.ID, null, null)
		} else {
			ErrorScreen es = (ErrorScreen) sm.getGameScreen(ErrorScreen.ID)
			es.setErrorMessage(errorMessage)
			sm.enterGameScreen(ErrorScreen.ID, null, null)
		}
	}

	static void reloadEngine(ScreenManager sm) {
		sm.enterGameScreen(LoadScreen.ID, null, null)
	}

	void setEngine(Engine engine, String path) {
		this.engine = engine
		this.path = path
		systemManager.reset()
	}


	protected enum EngineState {
		RUNNING,
		BACK,
		ERROR,
		PAUSE
	}
}
