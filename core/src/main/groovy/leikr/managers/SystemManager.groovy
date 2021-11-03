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
    float abs(BigDecimal value) {
        Math.abs(value.floatValue())
    }

    float cos(BigDecimal radians) {
        MathUtils.cos(radians.floatValue())
    }

    float cosDeg(BigDecimal deg) {
        MathUtils.cosDeg(deg.floatValue())
    }

    float sin(BigDecimal radians) {
        MathUtils.sin(radians.floatValue())
    }

    float sinDeg(BigDecimal deg) {
        MathUtils.sinDeg(deg.floatValue())
    }

    int ceil(BigDecimal value) {
        MathUtils.ceil(value.floatValue())
    }

    int floor(BigDecimal value) {
        MathUtils.floor(value.floatValue())
    }

    int randInt(BigDecimal range) {
        MathUtils.random(range.intValue())
    }

    int randInt(BigDecimal start, BigDecimal end) {
        MathUtils.random(start.intValue(), end.intValue())
    }

    float randFloat(BigDecimal range) {
        MathUtils.random(range.floatValue())
    }

    float randFloat(BigDecimal start, BigDecimal end) {
        MathUtils.random(start.floatValue(), end.floatValue())
    }

    int round(BigDecimal number) {
        MathUtils.round(number.floatValue())
    }
    // </editor-fold>

    // <editor-fold desc="Built in collisions" defaultstate="collapsed"> 
    boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal r1, BigDecimal x2, BigDecimal y2, BigDecimal r2) {
        float vx = x1.floatValue() - x2.floatValue()
        float vy = y1.floatValue() - y2.floatValue()
        float vr = r1.floatValue() + r2.floatValue()
        return Math.abs(vx * vx + vy * vy) < vr * vr
    }

    boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal w1, BigDecimal h1, BigDecimal x2, BigDecimal y2, BigDecimal w2, BigDecimal h2) {
        x1.floatValue() + w1.floatValue() >= x2.floatValue() && x2.floatValue() + w2.floatValue() >= x1.floatValue() && y1.floatValue() + h1.floatValue() >= y2.floatValue() && y2.floatValue() + h2.floatValue() >= y1.floatValue()
    }

    boolean collides(BigDecimal[] a, BigDecimal[] b) {
        return a[0].floatValue() + a[2].floatValue() >= b[0].floatValue() && b[0].floatValue() + b[2].floatValue() >= a[0].floatValue() && a[1].floatValue() + a[3].floatValue() >= b[1].floatValue() && b[1].floatValue() + b[3].floatValue() >= a[1].floatValue()
    }

    boolean point(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2, BigDecimal w, BigDecimal h) {
        return x.floatValue() >= x2.floatValue() && x.floatValue() <= x2.floatValue() + w.floatValue() && y.floatValue() >= y2.floatValue() && y.floatValue() <= y2.floatValue() + h.floatValue()
    }
    // </editor-fold>

    // <editor-fold desc="Engine loader api" defaultstate="collapsed"> 
    Object compile(String path) {
        return engineLoader.compile(path)
    }

    void compile(String path, String out) {
        engineLoader.compile(path, out)
    }

    Object eval(String code) {
        return engineLoader.eval(code)
    }

    Object parse(String code) {
        return engineLoader.parse(code)
    }

    void loadLib(String path) {
        engineLoader.loadLib(path)
    }

    Object newInstance(String name) {
        return engineLoader.newInstance(name)
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
        return pause
    }

    final void preRender(Graphics g) {
        g.setFont(font)
    }

    void setRunning(boolean run) {
        running = run
    }
    // </editor-fold>
}
