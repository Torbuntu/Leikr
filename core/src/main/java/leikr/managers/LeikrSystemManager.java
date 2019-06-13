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
package leikr.managers;

import com.badlogic.gdx.assets.AssetManager;
import leikr.GameRuntime;
import leikr.screens.LoadScreen;
import leikr.screens.MenuScreen;
import org.mini2Dx.core.font.MonospaceFont;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.ScreenManager;

/**
 *
 * @author tor
 */
public class LeikrSystemManager {

    private boolean LOAD_PROGRAM = false;
    private boolean RUNNING = true;
    AssetManager manager;
    MonospaceFont font;
    
    private static LeikrSystemManager instance;

    public LeikrSystemManager() {
        this.manager = new AssetManager();
        font = GameRuntime.primaryFontLoader.getDefaultFont();
    }

    public static LeikrSystemManager getLeikrSystemManager() {
        if (instance == null) {
            instance = new LeikrSystemManager();
        }
        instance.reset();
        return instance;
    }
    
    private void reset(){
        manager.clear();
    }

    //START API
    /**
     * resetFont
     *
     * after a program is finished running, this should be called to reset the
     * font to the default loaded system font.
     */
    public void resetFont() {
        font = GameRuntime.primaryFontLoader.getDefaultFont();
    }

    public void setCustomFont(String fontPath, int spacing, int width, int height) {
        font = GameRuntime.primaryFontLoader.getCustomFont(manager, fontPath, spacing, width, height);
    }

    public void loadProgram(String name) {
        GameRuntime.setProgramPath("Programs/" + name);
        MenuScreen.GAME_NAME = name;
        LOAD_PROGRAM = true;
    }
    //END API

    //START game loop methods on EngineScreen
    public boolean update(ScreenManager sm) {
        if (LOAD_PROGRAM) {
            LOAD_PROGRAM = false;
            RUNNING = false;
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }
        font.load(manager);
        return RUNNING;
    }

    public void render(Graphics g) {
        g.setFont(font);
    }

    public void setRunning(boolean run) {
        RUNNING = run;
    }
    //END game loop methods on EngineScreen
}
