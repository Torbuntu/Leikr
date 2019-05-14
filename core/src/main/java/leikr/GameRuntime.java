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
package leikr;

import leikr.customProperties.CustomSystemProperties;
import leikr.screens.EngineScreen;
import leikr.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import java.io.File;
import leikr.screens.CreditScreen;
import leikr.screens.ErrorScreen;
import leikr.screens.LoadScreen;
import leikr.screens.NewProgramScreen;
import leikr.screens.TitleScreen;
import org.mini2Dx.core.game.ScreenBasedGame;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";
    public static final String PROGRAM_PATH = "Programs/";
    private static String GAME_PATH;

    public static int WIDTH = 240;
    public static int HEIGHT = 160;
    public static boolean SINGLE_LAUNCH = false;

    AssetManager assetManager;
    CustomSystemProperties csp;

    public GameRuntime() {
        csp = new CustomSystemProperties();
        if (csp.SINGLE_LAUNCH) {
            GAME_PATH = PROGRAM_PATH + new File(PROGRAM_PATH).list()[0];//Look at this crazy hack! 
            System.out.println(GAME_PATH);
            SINGLE_LAUNCH = true;
        }
    }

    public static void setGamePath(String name) {
        if (!SINGLE_LAUNCH) {
            GAME_PATH = name;
        }
    }

    public static String getGamePath() {
        return GAME_PATH;
    }

    @Override
    public void initialise() {
        assetManager = new AssetManager();
        this.addScreen(new MenuScreen(assetManager));//0
        this.addScreen(new EngineScreen());//1
        this.addScreen(new TitleScreen(assetManager));//2
        this.addScreen(new CreditScreen(assetManager));//3
        this.addScreen(new ErrorScreen(assetManager));//4
        this.addScreen(new LoadScreen());//5
        this.addScreen(new NewProgramScreen());//6
    }

    @Override
    public int getInitialScreenId() {
        return TitleScreen.ID;//initial screen to begin on is the title screen.
    }

}
