package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.stream.Collectors;
import leikr.customProperties.ChipData;
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
    boolean PAGE = true;
    int cursor;
    int offset;
    int category = 0; //0: Program, 1: Game, 2: Utility, 3: Demo, 4: System
    String categoryDisplay = "Programs";

    MonospaceFont font;

    AssetManager assetManager;
    FitViewport viewport;
    ArrayList<ChipData> programs;

    ArrayList<ChipData> filteredPrograms;

    Vector2 realMouse;
    Vector2 leikrMouse;

    ControllerAdapter menuControllerAdapter;

    float time = 0;// Create a lock on the mouse click for the up/down selection of programs.

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.load(MENU_BG, Texture.class);
        font = GameRuntime.primaryFontLoader.getDefaultFont();
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        realMouse = new Vector2();
        leikrMouse = new Vector2();
        filteredPrograms = new ArrayList<>();
        initMenuList();
        createControllerAdapter();
    }

    private void shiftCategoryLeft() {
        category--;
        if (category < 0) {
            category = 4;
        }
        cursor = 0;
        updateProgramCategory();
        GAME_NAME = filteredPrograms.get(cursor).getDirectory();
    }

    private void shiftCategoryRight() {
        category++;
        if (category > 4) {
            category = 0;
        }
        cursor = 0;
        updateProgramCategory();
        GAME_NAME = filteredPrograms.get(cursor).getDirectory();
    }

    private void createControllerAdapter() {
        menuControllerAdapter = new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                if (buttonIndex == CustomSystemProperties.START || buttonIndex == CustomSystemProperties.A) {
                    if (GAME_NAME.equals("Start new...")) {
                        NEW_PROGRAM = true;
                    } else if (GAME_NAME.length() > 2) {
                        GameRuntime.setProgramPath("Programs/" + GAME_NAME);
                        START = true;
                    }
                }
                if (buttonIndex == CustomSystemProperties.LEFT_BUMPER) {
                    shiftCategoryLeft();
                    return true;
                }
                if (buttonIndex == CustomSystemProperties.RIGHT_BUMPER) {
                    shiftCategoryRight();
                    return true;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
                    if (value == 1 && cursor < filteredPrograms.size() - 1) {
                        cursor++;
                        GAME_NAME = filteredPrograms.get(cursor).getDirectory();
                    } else if (value == -1 && cursor > 0) {
                        cursor--;
                        GAME_NAME = filteredPrograms.get(cursor).getDirectory();
                    }
                    return true;
                } else {
                    if (value == CustomSystemProperties.LEFT) {
                        PAGE = true;
                    }
                    if (value == CustomSystemProperties.RIGHT) {
                        PAGE = false;
                    }
                }
                return false;
            }
        };
    }

    private void initMenuList() {
        programs = new ArrayList<>();
        for (FileHandle file : Gdx.files.local("Programs/").list()) {
            programs.add(new ChipData(file.name(), assetManager));
        }

        cursor = 0;
        offset = 0;

        programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager, "Start new..."));
        assetManager.finishLoading();
        updateProgramCategory();
        GAME_NAME = programs.get(0).getDirectory();
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        if (GameRuntime.checkLaunchTitle()) {
            GAME_NAME = GameRuntime.LAUNCH_TITLE;
            START = true;
        } else {
            initMenuList();
        }
    }

    @Override
    public void preTransitionOut(Transition transitionIn) {
        Controllers.clearListeners();
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).removeListener(menuControllerAdapter);
        }
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
                    GAME_NAME = filteredPrograms.get(cursor).getDirectory();
                }
                if (i == Keys.DOWN && cursor < filteredPrograms.size() - 1) {
                    cursor++;
                    GAME_NAME = filteredPrograms.get(cursor).getDirectory();
                }
                if (i == Keys.ENTER) {
                    if (GAME_NAME.equals("Start new...")) {
                        System.out.println("initializing new game...");
                        NEW_PROGRAM = true;
                    } else if (GAME_NAME.length() > 2) {
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
                    PAGE = !PAGE;
                }
                return false;
            }
        });
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(menuControllerAdapter);
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on Menu Screen. " + ex.getMessage());
        }
    }

    private void updateMouse() {
        realMouse.x = Gdx.input.getX();
        realMouse.y = Gdx.input.getY();
        viewport.toWorldCoordinates(leikrMouse, realMouse.x, realMouse.y);
    }

    private void updateProgramCategory() {
        switch (category) {
            case 0:
                categoryDisplay = "Program";
                break;
            case 1:
                categoryDisplay = "Game";
                break;
            case 2:
                categoryDisplay = "Utility";
                break;
            case 3:
                categoryDisplay = "Demo";
                break;
            case 4:
                categoryDisplay = "System";
                break;
            default:
                categoryDisplay = "Program";
                break;
        }

        filteredPrograms = programs.stream().filter(cd -> cd.getType().equals(categoryDisplay)).collect(Collectors
                .toCollection(ArrayList::new));

    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        font.load(assetManager);
        updateMouse();
        time += delta;
        if (leikrMouse.y > 51 && cursor < filteredPrograms.size() - 1 && Gdx.input.isTouched() && time > 0.2) {
            cursor++;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
            time = 0;
        }
        if (leikrMouse.y < 36 && cursor > 0 && Gdx.input.isTouched() && time > 0.2) {
            cursor--;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
            time = 0;
        }
        if (leikrMouse.y > 37 && leikrMouse.y < 50 && Gdx.input.isTouched()) {
            if (GAME_NAME.equals("Start new...")) {
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

        //Update offset position
        offset = cursor * 8;

        if (Gdx.input.isKeyJustPressed(Keys.W)) {
            shiftCategoryLeft();
        }
        if (Gdx.input.isKeyJustPressed(Keys.Q)) {
            shiftCategoryLeft();
        }
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            System.out.println(GAME_NAME);
        }
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.setFont(font);
        g.setColor(Color.WHITE);
        if (null != filteredPrograms && filteredPrograms.size() > 0) {
            for (int i = 0; i < filteredPrograms.size(); i++) {
                g.drawString(filteredPrograms.get(i).getTitle(), 8, (8 * i) + 40 - offset);
            }
            g.setColor(Color.BLACK);
            g.fillRect(118, 6, 120, 90);
            g.fillRect(6, 100, 240, 150);

            g.setColor(Color.WHITE);

            if (PAGE) {
                g.drawString("Title  : " + filteredPrograms.get(cursor).getTitle(), 8, 104);
                g.drawString("Author : " + filteredPrograms.get(cursor).getAuthor(), 8, 114);
                g.drawString("Type   : " + filteredPrograms.get(cursor).getType(), 8, 124);
                g.drawString("Players: " + filteredPrograms.get(cursor).getPlayers(), 8, 134);
                g.drawString("Version: " + filteredPrograms.get(cursor).getVersion(), 8, 144);
            } else {
                g.drawString("About: " + filteredPrograms.get(cursor).getAbout(), 8, 104, 224);
            }

            g.drawTexture(filteredPrograms.get(cursor).getIcon(), 120, 8, 112, 80);

        } else {
            g.drawString("X", 8, 8);
            g.drawString("X", 120, 8);

            g.drawString("No programs detected... ", 8, 104);
        }
        //Menu tops everything else.
        g.drawTexture(assetManager.get(MENU_BG, Texture.class), 0, 0);

        //Display Category
        g.setColor(Color.WHITE);
        g.drawString(categoryDisplay, 16, 8);

        //Mouse
        g.setColor(Color.RED);
        g.drawLineSegment(leikrMouse.x, leikrMouse.y, leikrMouse.x + 6, leikrMouse.y + 4);
    }

    @Override
    public int getId() {
        return ID;
    }
}
