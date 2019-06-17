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

import com.badlogic.gdx.Gdx;
import leikr.customProperties.CustomSystemProperties;
import leikr.screens.EngineScreen;
import leikr.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;
import leikr.loaders.FontLoader;
import leikr.screens.CreditScreen;
import leikr.screens.ErrorScreen;
import leikr.screens.LoadScreen;
import leikr.screens.NewProgramScreen;
import leikr.screens.TitleScreen;
import org.mini2Dx.core.game.ScreenBasedGame;

public class GameRuntime extends ScreenBasedGame {

    public static final String GAME_IDENTIFIER = "torbuntu.leikr";

    //Start out in the Programs/ directory. Append either the Launch Title, or in MenuScreen append program titles including the directory
    public static String PROGRAM_PATH = "Programs/";
    public static String LAUNCH_TITLE;

    public static int WIDTH = 240;
    public static int HEIGHT = 160;

    AssetManager assetManager;
    CustomSystemProperties csp;

    public static FontLoader primaryFontLoader = new FontLoader();

    public GameRuntime(String[] arg) {
        
        csp = new CustomSystemProperties();
        if (csp.LAUNCH_TITLE.length() > 3) {
            LAUNCH_TITLE = csp.LAUNCH_TITLE;
            PROGRAM_PATH = "Programs/" + csp.LAUNCH_TITLE;
            System.out.println(PROGRAM_PATH);
        }
    }

    /**
     * setProgramPath Pass in a path to a program: EG "Programs/MyProgramTitle"
     * The directory "Programs" is required, as seen on MenuScreen
     *
     * @param name
     */
    public static void setProgramPath(String name) {
        PROGRAM_PATH = name;
    }

    /**
     * getProgramPath Returns the variable PROGRAM_PATH which should contain
     * "Programs/" as the root directory.
     *
     * @return
     */
    public static String getProgramPath() {
        return PROGRAM_PATH;
    }

    public static boolean checkLaunchTitle() {
        return (null != LAUNCH_TITLE && LAUNCH_TITLE.length() > 3);
    }

    @Override
    public void initialise() {
        assetManager = new AssetManager();
        primaryFontLoader.initializeDefaultFont(assetManager);

        this.addScreen(new MenuScreen(assetManager));//0
        this.addScreen(new EngineScreen(assetManager));//1
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
