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
package leikr.managers


import leikr.GameRuntime
import leikr.loaders.EngineLoader
import leikr.loaders.FontLoader
import leikr.loaders.SpriteLoader
import leikr.screens.LoadScreen
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.files.ExternalFileHandleResolver
import org.mini2Dx.core.font.MonospaceGameFont
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.gdx.math.MathUtils

/**
 *
 * @author tor
 */
class SystemManager {

    private boolean loadProgram = false
    private boolean running = true
    private boolean pause = false

    private final AssetManager manager
    private final EngineLoader engineLoader
    private final FontLoader fontLoader
    private final SpriteLoader spriteLoader
    private final GameRuntime runtime
    private MonospaceGameFont font

    SystemManager(EngineLoader engineLoader, FontLoader fontLoader, SpriteLoader spriteLoader, GameRuntime runtime) {
        this.manager = new AssetManager(new ExternalFileHandleResolver())
        this.engineLoader = engineLoader
        this.fontLoader = fontLoader
        this.spriteLoader = spriteLoader
        this.runtime = runtime
        font = fontLoader.getDefaultFont()
    }

    void reset() {
        pause = false
        manager.clearAssetLoaders()
    }

    // <editor-fold desc="Helper methods" defaultstate="collapsed"> 
    String getProgramPath() {
        runtime.getProgramsPath()
    }

    String getDataPath() {
        runtime.getDataPath()
    }

    /**
     * After a program is finished running, this should be called to reset the
     * font to the default loaded system font.
     */
    void resetFont() {
        font = fontLoader.getDefaultFont()
    }

    void setCustomFont(String fontPath, int spacing, int width, int height) {
        font = fontLoader.getCustomFont(manager, fontPath, spacing, width, height)
    }

    void loadProgram(String name) {
        runtime.setFileDroppedTitle(name)
        loadProgram = true
    }

    void loadSpriteSheet(String sheetName) {
        spriteLoader.loadManualSpriteSheets(sheetName)
    }

    void pause() {
        pause = true
    }

    void pause(boolean shouldPause) {
        pause = shouldPause
    }
    // </editor-fold>

    // <editor-fold desc="Math api" defaultstate="collapsed"> 
    static float abs(Number value) {
        Math.abs(value.floatValue())
    }

    static float cos(Number radians) {
        MathUtils.cos(radians.floatValue())
    }

    static float cosDeg(Number deg) {
        MathUtils.cosDeg(deg.floatValue())
    }

    static float sin(Number radians) {
        MathUtils.sin(radians.floatValue())
    }

    static float sinDeg(Number deg) {
        MathUtils.sinDeg(deg.floatValue())
    }

    static int ceil(Number value) {
        MathUtils.ceil(value.floatValue())
    }

    static int floor(Number value) {
        MathUtils.floor(value.floatValue())
    }

    static int randInt(Number range) {
        MathUtils.random(range.intValue())
    }

    static int randInt(Number start, Number end) {
        MathUtils.random(start.intValue(), end.intValue())
    }

    static float randFloat(Number range) {
        MathUtils.random(range.floatValue())
    }

    static float randFloat(Number start, Number end) {
        MathUtils.random(start.floatValue(), end.floatValue())
    }

    static int round(Number number) {
        MathUtils.round(number.floatValue())
    }
    // </editor-fold>

    // <editor-fold desc="Engine loader api" defaultstate="collapsed"> 
    Object compile(String path) {
        engineLoader.compile(path)
    }

    void compile(String path, String out) {
        engineLoader.compile(path, out)
    }

    Object eval(String code) {
        engineLoader.eval(code)
    }

    Object parse(String code) {
        engineLoader.parse(code)
    }

    void loadLib(String path) {
        engineLoader.loadLib(path)
    }

    Object newInstance(String name) {
        engineLoader.newInstance(name)
    }
    // </editor-fold>

    // <editor-fold desc="Game loop methods ran from Engine Screen" defaultstate="collapsed"> 
    final boolean update(ScreenManager sm) {
        if (loadProgram) {
            loadProgram = false
            running = false
            sm.enterGameScreen(LoadScreen.ID, null, null)
        }
        font.load(manager)
        return running
    }

    final boolean checkShouldPause() {
        pause
    }

    final void preRender(Graphics g) {
        g.setFont(font)
    }

    void setRunning(boolean run) {
        running = run
    }
    // </editor-fold>
}
