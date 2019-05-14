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
import java.util.Arrays;
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

    public static int ID = 0;
    boolean START = false;
    boolean NEW_PROGRAM = false;
    boolean page = true;
    int cursor;
    int offset;
    public static String GAME_NAME;
    private final String menuBg;
    ArrayList<ChipData> programs;

    AssetManager assetManager;
    FitViewport viewport;
    ArrayList<String> gameList;

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        menuBg = "Data/Images/menu_bg.png";

        this.assetManager.load(menuBg, Texture.class);

        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        initMenuList();
    }

    private void initMenuList() {
        gameList = new ArrayList<>(Arrays.asList(new File(GameRuntime.PROGRAM_PATH).list()));
        programs = new ArrayList<>();

        cursor = 0;
        offset = 0;

        if (gameList != null && gameList.size() > 0) {
            setGameName(gameList.get(0));
            loadPrograms();
            gameList.add("Start new...");
        } else {
            gameList.add("Start new...");
            programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager));
        }
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    private void setGameName(String GAME_NAME) {
        MenuScreen.GAME_NAME = GAME_NAME;
    }

    private void loadPrograms() {
        gameList.forEach((game) -> {
            programs.add(new ChipData(game, assetManager));
        });
        programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager));
        assetManager.finishLoading();
    }

    @Override
    public void onResize(int width, int height) {
        viewport.onResize(width, height);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            GameRuntime.setGamePath("Programs/" + getGameName());
            START = true;
        }
        initMenuList();
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
        if (GameRuntime.SINGLE_LAUNCH) {
            return;
        }
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {
                if (i == Keys.UP && cursor > 0) {
                    cursor--;
                    setGameName(gameList.get(cursor));
                }
                if (i == Keys.DOWN && cursor < gameList.size() - 1) {
                    cursor++;
                    setGameName(gameList.get(cursor));
                }
                if (i == Keys.ENTER) {
                    if (cursor == gameList.size() - 1) {
                        System.out.println("initializing new game...");
                        NEW_PROGRAM = true;
                    } else {
                        System.out.println("Loading program: " + getGameName());
                        GameRuntime.setGamePath("Programs/" + getGameName());
                        START = true;
                    }
                }
                if (i == Keys.ESCAPE) {
                    System.out.println("Good bye!");
                    Gdx.app.exit();
                }
                if (i == Keys.LEFT || i == Keys.RIGHT) {
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
                            if (cursor == gameList.size() - 1) {
                                NEW_PROGRAM = true;
                            } else {
                                GameRuntime.setGamePath("Programs/" + getGameName());
                                START = true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean axisMoved(Controller controller, int axisCode, float value) {
                        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
                            if (value == 1 && cursor < gameList.size() - 1) {
                                cursor++;
                                setGameName(gameList.get(cursor));
                            } else if (value == -1 && cursor > 0) {
                                cursor--;
                                setGameName(gameList.get(cursor));
                            }
                            return true;
                        } else {
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
        if (NEW_PROGRAM) {
            NEW_PROGRAM = false;
            sm.enterGameScreen(NewProgramScreen.ID, null, null);
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
        if (null != gameList && gameList.size() > 0) {
            for (int i = 0; i < gameList.size(); i++) {
                g.drawString(gameList.get(i), 8, (8 * i) + 40 - offset);
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
                g.drawString("Version: " + programs.get(cursor).getVersion(), 8, 144);
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

    }

    @Override
    public int getId() {
        return ID;
    }
}
