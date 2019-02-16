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

import leikr.screens.EngineScreen;
import leikr.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import java.io.File;
import leikr.screens.TitleScreen;
import org.mini2Dx.core.game.ScreenBasedGame;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";

    public static int WIDTH = 240;
    public static int HEIGHT = 160;
    public static boolean SINGLE_LAUNCH = false;

    AssetManager assetManager;

    public GameRuntime() {
        CustomSystemProperties csp = new CustomSystemProperties();
        if (csp.SINGLE_LAUNCH) {
            GAME_PATH = "./Programs/" + new File("./Programs/").list()[0];//Look at this crazy hack! 
            System.out.println(GAME_PATH);
            SINGLE_LAUNCH = true;
        }
    }

    private static String GAME_PATH;

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
        this.addScreen(new TitleScreen(assetManager));
        this.addScreen(new MenuScreen(assetManager));
        this.addScreen(new EngineScreen(assetManager));
    }

    @Override
    public int getInitialScreenId() {
        return TitleScreen.ID;//initial screen to begin on is the title screen.
    }

}
