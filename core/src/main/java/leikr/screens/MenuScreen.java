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
package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.ArrayList;
import leikr.ChipData;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 *
 * @author tor
 *
 */
public class MenuScreen extends BasicGameScreen {

    /* TODO: Make a graphical menu list to display games. This will take in a 
        This will take in a few items to use from the game's properties file.
        Such as:
            1. a custom icon which will be displayed in a selection box. 
            2. a Title
            3. a small one or two line description of the game.
            4?. a genre?
     */
    public static int ID = 0;
    boolean START = false;
    boolean page = true;
    int cursor;
    int offset;
    public static String GAME_NAME;
    private String menuBg;
    ArrayList<ChipData> programs;

    AssetManager assetManager;
    FitViewport viewport;
    String[] gameList;

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        menuBg = "Data/menu_bg.png";

        this.assetManager.load(menuBg, Texture.class);

        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        gameList = new File(GameRuntime.PROGRAM_PATH).list();
        programs = new ArrayList<>();

        if (gameList != null) {
            setGameName(gameList[0]);
            loadPrograms();
        }
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    private void setGameName(String GAME_NAME) {
        MenuScreen.GAME_NAME = GAME_NAME;
    }

    private void loadPrograms() {
        for (String game : gameList) {
            programs.add(new ChipData(game, assetManager));
        }
        assetManager.finishLoading();
    }

    @Override
    public void onResize(int width, int height) {
        viewport.onResize(width, height);
    }

    @Override
    public void initialise(GameContainer gc) {
        cursor = 0;
        offset = 0;
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            GameRuntime.setGamePath("Programs/" + getGameName());
            START = true;
        }
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            return;
        }
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int i) {
                if (i == Keys.UP && cursor > 0) {
                    cursor--;
                    setGameName(gameList[cursor]);
                }
                if (i == Keys.DOWN && cursor < gameList.length - 1) {
                    cursor++;
                    setGameName(gameList[cursor]);
                }
                if (i == Keys.ENTER) {
                    System.out.println("Loading program: " + getGameName());
                    GameRuntime.setGamePath("Programs/" + getGameName());
                    START = true;
                }
                if (i == Keys.ESCAPE) {
                    System.out.println("Good bye!");
                    Gdx.app.exit();
                }
                if(i == Keys.LEFT || i == Keys.RIGHT){
                    page = !page;
                }
                return false;
            }
        });
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(new ControllerAdapter() {
                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {
                        if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A) {
                            GameRuntime.setGamePath("Programs/" + getGameName());
                            START = true;
                        }
                        return false;
                    }

                    @Override
                    public boolean axisMoved(Controller controller, int axisCode, float value) {
                        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
                            if (value == 1 && cursor < gameList.length - 1) {
                                cursor++;
                                setGameName(gameList[cursor]);
                            } else if (value == -1 && cursor > 0) {
                                cursor--;
                                setGameName(gameList[cursor]);
                            }
                            return true;
                        }else{
                            page = !page;
                        }
                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Menu Screen. " + ex.getMessage());
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if (START) {
            START = false;
            sm.enterGameScreen(LoadScreen.ID, null, null);
        }
        offset = cursor * 8;
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setColor(Color.WHITE);
        if (null != gameList) {
            for (int i = 0; i < gameList.length; i++) {
                g.drawString(gameList[i], 8, (8 * i) + 40 - offset);
            }
            g.setColor(Color.BLACK);
            g.fillRect(118, 6, 120, 90);
            g.fillRect(6, 100, 240, 150);

            g.setColor(Color.WHITE);

            if (page) {
                g.drawString("Title  : " + programs.get(cursor).getTitle(), 8, 104);
                g.drawString("Author : " + programs.get(cursor).getAuthor(), 8, 114);
                g.drawString("Type   : " + programs.get(cursor).getType(), 8, 124);
                g.drawString("Players: " + programs.get(cursor).getPlayers(), 8, 134);
                g.drawString("Engine : " + programs.get(cursor).getEngine(), 8, 144);
            } else {
                g.drawString("About: " + programs.get(cursor).getAbout(), 8, 104, 224);
            }

            g.drawTexture(programs.get(cursor).getIcon(), 120, 8, 112, 80);

        } else {
            g.drawString("X", 8, 8);
            g.drawString("X", 120, 8);

            g.drawString("No programs detected... ", 8, 104);
        }
        //Menu tops everything else.
        g.drawTexture(assetManager.get(menuBg, Texture.class), 0, 0);

        g.setColor(Color.RED);
//        g.drawString("offset: " + offset, 0, 0);

    }

    @Override
    public int getId() {
        return ID;
    }
}
