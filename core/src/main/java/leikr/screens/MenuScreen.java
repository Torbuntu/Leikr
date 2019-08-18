package leikr.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import leikr.customProperties.ChipData;
import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.gdx.math.Vector2;

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

    int category = 0; //0: Program, 1: Game, 2: Utility, 3: Demo, 4: System, 5: Tool
    String categoryDisplay = "Programs";

    MonospaceGameFont font;

    AssetManager assetManager;
    FitViewport viewport;

    ArrayList<ChipData> programs;
    ArrayList<ChipData> filteredPrograms;

    Vector2 realMouse;
    Vector2 leikrMouse;

    float time = 0;// Create a lock on the mouse click for the up/down selection of programs.

    public MenuScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.load(MENU_BG, Texture.class);
        font = GameRuntime.primaryFontLoader.getDefaultFont();
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
        realMouse = new Vector2();
        leikrMouse = new Vector2();
        filteredPrograms = new ArrayList<>();
        cursor = 0;
        offset = 0;
        initMenuList();
    }

    /**
     * Initializes the program list and sets the initial program to GAME_NAME
     */
    private void initMenuList() {
        try {
            programs = new ArrayList<>();
            programs.add(new ChipData("New Game", "System", "Template", "1.0", 0, "Initializes a new program template", assetManager, "Start new..."));

            Arrays.asList(Mdx.files.local("Programs/").list())
                    .forEach(file -> {
                        programs.add(new ChipData(file.name(), assetManager));
                    });

            assetManager.finishLoading();
            updateProgramCategory();
            GAME_NAME = filteredPrograms.size() > 0 ? filteredPrograms.get(cursor).getDirectory() : programs.get(cursor).getDirectory();
        } catch (IOException ex) {
            Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * update the display category and list.
     */
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
            case 5:
                categoryDisplay = "Tool";
                break;
            default:
                categoryDisplay = "Program";
                break;
        }

        filteredPrograms = programs.stream().filter(cd -> cd.getType().equals(categoryDisplay)).collect(Collectors
                .toCollection(ArrayList::new));
    }

    /**
     * Selects the category to the left, updating the filtered list of programs.
     * updates the GAME_NAME selection.
     */
    private void shiftCategoryLeft() {
        category--;
        if (category < 0) {
            category = 5;
        }
        cursor = 0;
        updateProgramCategory();
        if (filteredPrograms.size() > 0) {
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
        } else {
            GAME_NAME = "";
        }
    }

    /**
     * Selects the category to the right, updating the filtered list of
     * programs. updates the GAME_NAME selection.
     */
    private void shiftCategoryRight() {
        category++;
        if (category > 5) {
            category = 0;
        }
        cursor = 0;
        updateProgramCategory();
        if (filteredPrograms.size() > 0) {
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
        } else {
            GAME_NAME = "";
        }
    }

    /**
     * translate the mouse coordinates to viewport
     */
    private void updateMouse() {
        realMouse.x = Mdx.input.getX();
        realMouse.y = Mdx.input.getY();
        viewport.toWorldCoordinates(leikrMouse, realMouse.x, realMouse.y);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionOut(Transition transitionIn) {

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
    public void postTransitionIn(Transition transitionIn) {

    }

    private void checkInput() {
        if ((Mdx.input.isKeyJustPressed(Keys.UP) || Mdx.input.isKeyJustPressed(Keys.W)) && cursor > 0) {
            cursor--;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
        }
        if ((Mdx.input.isKeyJustPressed(Keys.DOWN) || Mdx.input.isKeyJustPressed(Keys.S)) && cursor < filteredPrograms.size() - 1) {
            cursor++;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
        }
        if (Mdx.input.isKeyJustPressed(Keys.ENTER)) {
            if (GAME_NAME.equals("Start new...")) {
                System.out.println("initializing new game...");
                NEW_PROGRAM = true;
            } else if (GAME_NAME.length() > 2) {
                System.out.println("Loading program: " + GAME_NAME);
                GameRuntime.setProgramPath("Programs/" + GAME_NAME);
                START = true;
            }
        }
        if (Mdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            System.out.println("Goodbye!");
            Mdx.platformUtils.exit(true);
        }
        if (Mdx.input.isKeyJustPressed(Keys.Q) || Mdx.input.isKeyJustPressed(Keys.E)) {
            PAGE = !PAGE;
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        font.load(assetManager);
        updateMouse();
        checkInput();
        time += delta;
        if (leikrMouse.y >= 52 && leikrMouse.y <= 56 && leikrMouse.x >= 108 && leikrMouse.x <= 115 && cursor < filteredPrograms.size() - 1 && Mdx.input.justTouched() && time > 0.2) {
            cursor++;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
            time = 0;
        }
        if (leikrMouse.y <= 36 && leikrMouse.y >= 32 && leikrMouse.x >= 108 && leikrMouse.x <= 115 && cursor > 0 && Mdx.input.justTouched() && time > 0.2) {
            cursor--;
            GAME_NAME = filteredPrograms.get(cursor).getDirectory();
            time = 0;
        }
        if (leikrMouse.y >= 42 && leikrMouse.y <= 46 && leikrMouse.x >= 108 && leikrMouse.x <= 115 && Mdx.input.justTouched()) {
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

        //Category shifting        
        if (Mdx.input.isKeyJustPressed(Keys.A) || Mdx.input.isKeyJustPressed(Keys.LEFT) || (leikrMouse.y <= 16 && leikrMouse.y >= 9 && leikrMouse.x > 107 && leikrMouse.x < 111 && Mdx.input.justTouched() && time > 0.2)) {
            shiftCategoryRight();
            time = 0;
        }
        if (Mdx.input.isKeyJustPressed(Keys.D) || Mdx.input.isKeyJustPressed(Keys.RIGHT) || (leikrMouse.y <= 16 && leikrMouse.y >= 9 && leikrMouse.x > 112 && leikrMouse.x < 116 && Mdx.input.justTouched() && time > 0.2)) {
            shiftCategoryLeft();
            time = 0;
        }
        if (Mdx.input.isKeyJustPressed(Keys.P)) {
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
        g.setColor(Colors.WHITE());
        if (null != filteredPrograms && filteredPrograms.size() > 0) {
            for (int i = 0; i < filteredPrograms.size(); i++) {
                g.drawString(filteredPrograms.get(i).getTitle(), 8, (8 * i) + 40 - offset);
            }
            g.setColor(Colors.BLACK());
            g.fillRect(118, 6, 120, 90);
            g.fillRect(6, 100, 240, 150);

            g.setColor(Colors.WHITE());

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
        g.setColor(Colors.WHITE());
        g.drawString(categoryDisplay, 16, 8);

        //Mouse
        g.setColor(Colors.RED());
        g.drawLineSegment(leikrMouse.x, leikrMouse.y, leikrMouse.x + 6, leikrMouse.y + 4);
    }

    @Override
    public int getId() {
        return ID;
    }
}
