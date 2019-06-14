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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import leikr.ChipData;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.font.MonospaceFont;
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

    public static String GAME_NAME;
    
    private final static String MENU_BG = "Data/Images/menu_bg.png";

    boolean START = false;
    boolean NEW_PROGRAM = false;
    boolean page = true;
    int cursor;
    int offset;

    MonospaceFont font;

    AssetManager assetManager;
    FitViewport viewport;
    ArrayList<String> gameList;
    ArrayList<ChipData> programs;

    float time = 0;// Create a lock on the mouse click for the up/down selection of programs.

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.load(MENU_BG, Texture.class);
        font = GameRuntime.primaryFontLoader.getDefaultFont();
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        initMenuList();
    }

    private void initMenuList() {

        setCursor();

        gameList = new ArrayList<>(Arrays.asList(new File("Programs/").list()));
        programs = new ArrayList<>();

        cursor = 0;
        offset = 0;

        if (gameList != null && gameList.size() > 0) {
            Collections.sort(gameList);
            GAME_NAME = gameList.get(0);
            loadPrograms();
            gameList.add("Start new...");
        } else {
            gameList.add("Start new...");
            programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager));
        }
    }

    private void setCursor() {
        Pixmap map = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        map.setColor(Color.RED);
        map.fillCircle(0, 0, 8);
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(map, 0, 0));
        map.dispose();
    }

    private void loadPrograms() {
        gameList.forEach((game) -> {
            programs.add(new ChipData("Programs/" + game, assetManager));
        });
        programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager));
        assetManager.finishLoading();
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        if (GameRuntime.checkLaunchTitle()) {
            GameRuntime.setProgramPath("Programs/" + GAME_NAME);
            START = true;
        }
        initMenuList();
    }

    @Override
    public void postTransitionIn(Transition transitionIn) {
        if (GameRuntime.checkLaunchTitle()) {
            return;
        }
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {
                if (i == Keys.UP && cursor > 0) {
                    cursor--;
                    GAME_NAME = gameList.get(cursor);
                }
                if (i == Keys.DOWN && cursor < gameList.size() - 1) {
                    cursor++;
                    GAME_NAME = gameList.get(cursor);
                }
                if (i == Keys.ENTER) {
                    if (cursor == gameList.size() - 1) {
                        System.out.println("initializing new game...");
                        NEW_PROGRAM = true;
                    } else {
                        System.out.println("Loading program: " + GAME_NAME);
                        GameRuntime.setProgramPath("Programs/" + GAME_NAME);
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
                    public boolean buttonDown(Controller controller, int buttonIndex) {
                        if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A) {
                            if (cursor == gameList.size() - 1) {
                                NEW_PROGRAM = true;
                            } else {
                                GameRuntime.setProgramPath("Programs/" + GAME_NAME);
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
                                GAME_NAME = gameList.get(cursor);
                            } else if (value == -1 && cursor > 0) {
                                cursor--;
                                GAME_NAME = gameList.get(cursor);
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
        font.load(assetManager);
        time += delta;
        if (Gdx.input.getY() > 145 && cursor < gameList.size() - 1 && Gdx.input.isTouched() && time > 0.2) {
            cursor++;
            GAME_NAME = gameList.get(cursor);
            time = 0;
        }
        if (Gdx.input.getY() < 116 && cursor > 0 && Gdx.input.isTouched() && time > 0.2) {
            cursor--;
            GAME_NAME = gameList.get(cursor);
            time = 0;
        }
        if (Gdx.input.getY() > 117 && Gdx.input.getY() < 145 && Gdx.input.isTouched()) {
            if (cursor == gameList.size() - 1) {
                NEW_PROGRAM = true;
                System.out.println("initializing new game...");
            } else {
                START = true;
                System.out.println("Loading program: " + GAME_NAME);
                GameRuntime.setProgramPath("Programs/" + GAME_NAME);
            }
        }
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
        g.setFont(font);
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
                g.drawString(programs.get(cursor).getTitle(), 8, 104);
                g.drawString(programs.get(cursor).getAuthor(), 8, 114);
                g.drawString(programs.get(cursor).getType(), 8, 124);
                g.drawString(programs.get(cursor).getPlayers(), 8, 134);
                g.drawString(programs.get(cursor).getVersion(), 8, 144);
            } else {
                g.drawString(programs.get(cursor).getAbout(), 8, 104, 224);
            }

            g.drawTexture(programs.get(cursor).getIcon(), 120, 8, 112, 80);

        } else {
            g.drawString("X", 8, 8);
            g.drawString("X", 120, 8);

            g.drawString("No programs detected... ", 8, 104);
        }
        //Menu tops everything else.
        g.drawTexture(assetManager.get(MENU_BG, Texture.class), 0, 0);

    }

    @Override
    public int getId() {
        return ID;
    }
}
