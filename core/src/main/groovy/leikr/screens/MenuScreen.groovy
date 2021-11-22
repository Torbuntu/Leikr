/*
 * Copyright 2020 tor.
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
import leikr.properties.ProgramProperties
import leikr.transitions.EnterTransition
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.Colors
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.Texture
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.core.util.Align
import org.mini2Dx.gdx.Input.Keys

import java.util.logging.Level
import java.util.logging.Logger
/**
 *
 * @author tor
 */
class MenuScreen extends BasicGameScreen {

    public static int ID = 7
    private int index = 0
    private String isCompiled = ""

    private List<ProgramProperties> games

    private final FitViewport fitViewport
    private final StretchViewport stretchViewport
    private final GameRuntime runtime
    private Texture icon
    private FrameBuffer framebuffer

    MenuScreen(FitViewport vp, GameRuntime runtime) {
        this.runtime = runtime
        fitViewport = vp
        stretchViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT)
    }
    
    private void rebuildGamesList(){
        try {
            games = []

            Arrays.asList(Mdx.files.external(runtime.getProgramsPath()).list()).stream().forEach(game -> {
                games.add(new ProgramProperties(runtime.getProgramsPath() + game.nameWithoutExtension()))
            })

        } catch (IOException ex) {
            Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex)
        }
        loadIcon()
    }

    @Override
    void initialise(GameContainer gc) {
        rebuildGamesList()
    }

    @Override
    void preTransitionIn(Transition transition) {
        rebuildGamesList()
        framebuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT)
        if(Mdx.input.getGamePads().size() > 0){
            println "Free listeners"
            Mdx.input.getGamePads().each {
                it.removeListener(runtime.getInputManager().getControllerA())
                it.removeListener(runtime.getInputManager().getControllerB())
            }
            println "Adding Controller A"
            Mdx.input.getGamePads().get(0).addListener(runtime.getInputManager().getControllerA())
            if(Mdx.input.getGamePads().size() > 1){
                println "Adding Controller B"
                Mdx.input.getGamePads().get(1).addListener(runtime.getInputManager().getControllerB())
            }
        }
    }

    @Override
    void preTransitionOut(Transition transition) {
        framebuffer.dispose()
    }

    private void loadIcon() {
        try {
            icon = Mdx.graphics.newTexture(Mdx.files.external(runtime.getProgramsPath() + "${games.get(index).getTitle()}/Art/icon.png"))
            if (Mdx.files.external(runtime.getProgramsPath() + "${games.get(index).getTitle()}/Code/Compiled").exists()) {
                isCompiled = " *"
                if (games.get(index).getUseCompiled()) {
                    isCompiled = " **"
                }
            } else {
                isCompiled = ""
            }
        } catch (Exception ignored) {
            icon = Mdx.graphics.newTexture(Mdx.files.external(runtime.getDataPath() + "Logo/logo-32x32.png"))
            Logger.getLogger(MenuScreen.class.getName()).log(Level.WARNING, "No icon file for: {0}", games.get(index).getTitle())
        }
    }

    @Override
    void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Mdx.platformUtils.exit(true)
        }
        if (Mdx.input.isKeyJustPressed(Keys.T)) {
            sm.enterGameScreen(TerminalScreen.ID, null, null)
        }

        if (Mdx.input.isKeyJustPressed(Keys.LEFT) && index > 0) {
            index--
            loadIcon()
        }
        if (Mdx.input.isKeyJustPressed(Keys.RIGHT) && index < games.size() - 1) {
            index++
            loadIcon()
        }
        if (Mdx.input.isKeyJustPressed(Keys.ENTER)) {
            LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
            ls.setGameName(games.get(index).getTitle())
            runtime.setGameName(games.get(index).getTitle())
            sm.enterGameScreen(LoadScreen.ID, new EnterTransition(runtime), null)
        }

        if (runtime.checkFileDropped()) {
            LoadScreen ls = (LoadScreen) sm.getGameScreen(LoadScreen.ID)
            ls.setGameName(runtime.getFileDroppedTitle())
            runtime.setGameName(runtime.getFileDroppedTitle())
            runtime.clearFileDropped()
            sm.enterGameScreen(LoadScreen.ID, new EnterTransition(runtime), null)
        }
    }

    /**
     * See the EngineScreen for comments on why we use both the StretchViewport and the FitViewport.
     * @param gc
     * @param g
     */
    @Override
    void render(GameContainer gc, Graphics g) {
        stretchViewport.apply(g)
        framebuffer.begin()
        g.with{
			clearContext()
            // Icon
            drawTexture(icon, 120-icon.width/2, 80-icon.height/2)

            // Top bar
            setColor(Colors.BLACK())
            fillRect(0, 0, 240, 10)

            setColor(Colors.GREEN())
            drawLineSegment(0, 10, 240, 10)

            setColor(Colors.WHITE())

            drawString(games.get(index).getTitle() + isCompiled, 0, 3, 240, Align.CENTER)

            if (index > 0) {
				drawString("<-", 10, 3, 240, Align.LEFT)
            }
			if (index < games.size() - 1) {
				drawString("->", -10, 3, 240, Align.RIGHT)
            }
			if(!runtime.isSecure()){
				setColor(Colors.RED())
                drawString("!NO SANDBOX!", 0, 16, 240, Align.CENTER)
            }

            // Bottom Bar
            setColor(Colors.BLACK())
            fillRect(0, 150, 240, 10)
            setColor(Colors.GREEN())
            drawLineSegment(0, 150, 240, 150)

			setColor(Colors.YELLOW())
            drawString("[ENTER/START] - Play", 0, 154, 240, Align.LEFT)
            drawString("[T] - Enter Terminal", 0, 154, 240, Align.RIGHT)
            flush()
        }
        framebuffer.end()

        fitViewport.apply(g)
        g.drawTexture(framebuffer.getTexture(), 0, 0, false)
    }

    @Override
    int getId() {
        ID
    }

}
