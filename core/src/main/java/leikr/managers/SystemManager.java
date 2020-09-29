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
package leikr.managers;

import java.math.BigDecimal;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import leikr.loaders.FontLoader;
import leikr.loaders.SpriteLoader;
import leikr.screens.LoadScreen;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.ExternalFileHandleResolver;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.gdx.math.MathUtils;

/**
 *
 * @author tor
 */
public class SystemManager {

    private boolean loadProgram = false;
    private boolean running = true;
    private boolean pause = false;

    private final AssetManager manager;
    private final EngineLoader engineLoader;
    private final FontLoader fontLoader;
    private final SpriteLoader spriteLoader;
    private final GameRuntime runtime;
    private MonospaceGameFont font;

    public SystemManager(EngineLoader engineLoader, FontLoader fontLoader, SpriteLoader spriteLoader, GameRuntime runtime) {
        this.manager = new AssetManager(new ExternalFileHandleResolver());
        this.engineLoader = engineLoader;
        this.fontLoader = fontLoader;
        this.spriteLoader = spriteLoader;
        this.runtime = runtime;
        font = fontLoader.getDefaultFont();
    }

    public void reset() {
        pause = false;
        manager.clearAssetLoaders();
    }

    //START API
    /**
     * resetFont
     *
     * after a program is finished running, this should be called to reset the
     * font to the default loaded system font.
     */
    public void resetFont() {
        font = fontLoader.getDefaultFont();
    }

    public void setCustomFont(String fontPath, int spacing, int width, int height) {
        font = fontLoader.getCustomFont(manager, fontPath, spacing, width, height);
    }

    public void loadProgram(String name) {
        runtime.setGameName(name);
        loadProgram = true;
    }

    public void pause() {
        pause = true;
    }

    public void pause(boolean shouldPause) {
        pause = shouldPause;
    }

    //START Math functions
    public float cos(BigDecimal radians) {
        return MathUtils.cos(radians.floatValue());
    }

    public float cosDeg(BigDecimal deg) {
        return MathUtils.cosDeg(deg.floatValue());
    }

    public float sin(BigDecimal radians) {
        return MathUtils.sin(radians.floatValue());
    }

    public float sinDeg(BigDecimal deg) {
        return MathUtils.sinDeg(deg.floatValue());
    }

    public int ceil(BigDecimal value) {
        return MathUtils.ceil(value.floatValue());
    }

    public int floor(BigDecimal value) {
        return MathUtils.floor(value.floatValue());
    }

    public int randInt(int range) {
        return MathUtils.random(range);
    }

    public int randInt(int start, int end) {
        return MathUtils.random(start, end);
    }

    public float randFloat(BigDecimal range) {
        return MathUtils.random(range.floatValue());
    }

    public float randFloat(BigDecimal start, BigDecimal end) {
        return MathUtils.random(start.floatValue(), end.floatValue());
    }

    public int round(BigDecimal number) {
        return MathUtils.round(number.floatValue());
    }

    //END Math functions
    public boolean collides(BigDecimal x1, BigDecimal y1, BigDecimal w1, BigDecimal h1, BigDecimal x2, BigDecimal y2, BigDecimal w2, BigDecimal h2) {
        return x1.floatValue() + w1.floatValue() >= x2.floatValue() && x2.floatValue() + w2.floatValue() >= x1.floatValue() && y1.floatValue() + h1.floatValue() >= y2.floatValue() && y2.floatValue() + h2.floatValue() >= y1.floatValue();
    }

    public boolean collides(BigDecimal[] a, BigDecimal[] b) {
        return a[0].floatValue() + a[2].floatValue() >= b[0].floatValue() && b[0].floatValue() + b[2].floatValue() >= a[0].floatValue() && a[1].floatValue() + a[3].floatValue() >= b[1].floatValue() && b[1].floatValue() + b[3].floatValue() >= a[1].floatValue();
    }

    public boolean point(BigDecimal x, BigDecimal y, BigDecimal x2, BigDecimal y2, BigDecimal w, BigDecimal h) {
        return x.floatValue() >= x2.floatValue() && x.floatValue() <= x2.floatValue() + w.floatValue() && y.floatValue() >= y2.floatValue() && y.floatValue() <= y2.floatValue() + h.floatValue();
    }

    //START EngineLoader API
    public void loadSpriteSheet(String sheetName) {
        spriteLoader.loadManualSpritesheets(sheetName);
    }

    public Object compile(String path) {
        return engineLoader.compile(path);
    }

    public void compile(String path, String out) {
        engineLoader.compile(path, out);
    }

    public Object eval(String code) {
        return engineLoader.eval(code);
    }

    public Object parse(String code) {
        return engineLoader.parse(code);
    }

    public void loadLib(String path) {
        engineLoader.loadLib(path);
    }

    public Object newInstance(String name) {
        return engineLoader.newInstance(name);
    }
    //END EngineLoader API

    //END API
    //START game loop methods on EngineScreen
    public final boolean update(ScreenManager sm) {
        if (loadProgram) {
            loadProgram = false;
            running = false;
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }
        font.load(manager);
        return running;
    }

    public final boolean checkShouldPause() {
        return pause;
    }

    public final void preRender(Graphics g) {
        g.setFont(font);
    }

    public void setRunning(boolean run) {
        running = run;
    }
    //END game loop methods on EngineScreen
}
