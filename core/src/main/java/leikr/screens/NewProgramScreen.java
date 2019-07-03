package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.graphics.viewport.FitViewport;

/**
 *
 * @author tor
 */
public class NewProgramScreen extends BasicGameScreen {

    public static int ID = 6;
    FitViewport viewport;

    boolean BACK = false;
    boolean CREATE = false;
    boolean FINISH = false;
    String newName = "";

    ControllerAdapter newProgAdapter;

    String newLocation = "New Program template generated at: /Programs/";

    public NewProgramScreen() {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    private void createControllerAdapter() {
        newProgAdapter = new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                BACK = true;
                return false;
            }
        };
    }

    @Override
    public void preTransitionOut(Transition transOut) {
        Controllers.clearListeners();
        if (Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).removeListener(newProgAdapter);
        }
    }

    @Override
    public void preTransitionIn(Transition trns) {
        newLocation = "New Program template generated at: /Programs/";
        newName = "";
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {
                if (FINISH) {
                    if (i == Input.Keys.Q || i == Input.Keys.SPACE) {
                        BACK = true;
                    }
                }
                if (i == Input.Keys.ESCAPE) {
                    BACK = true;
                }
                if (i == Input.Keys.ENTER) {
                    CREATE = true;
                }
                if (i == Input.Keys.BACKSPACE && newName.length() > 0) {
                    newName = newName.substring(0, newName.length() - 1);
                }
                return false;
            }

            @Override
            public boolean keyTyped(char c) {
                if ((int) c > 64 && (int) c < 127) {
                    newName = newName + c;
                }
                return true;
            }
        });
        try {
            Controllers.clearListeners();
            if (Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(newProgAdapter);
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on NewProgram Screen. ");
            Logger.getLogger(NewProgramScreen.class.getName()).log(Level.INFO, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (BACK) {
            BACK = false;
            FINISH = false;
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
        if (CREATE) {
            try {
                int index = 0;
                String NP = newName.length() > 0 ? newName : "NewProgram";
                for (FileHandle name : Gdx.files.local("Programs").list()) {
                    if (name.name().contains(NP)) {
                        index++;
                    }
                }
                if (index > 0) {
                    NP = NP + index;
                }
                Gdx.files.local("Programs/" + NP).mkdirs();
                for (FileHandle file : Gdx.files.local("Data/Templates/NewProgram").list()) {
                    Gdx.files.local("Data/Templates/NewProgram/" + file.name()).copyTo(Gdx.files.local("Programs/" + NP));
                }
                Gdx.files.local("Programs/" + NP + "/Code/main.groovy").moveTo(Gdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy"));
                newLocation += NP + "/";
                System.out.println(NP + " template copied to Programs directory");
                FINISH = true;
            } catch (Exception ex) {
                Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex);
                CREATE = false;
            }
            CREATE = false;
        }
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        if (!FINISH) {
            g.setColor(Color.GREEN);
            g.drawString("Enter new program name: ", 0, 0);
            g.drawString(newName, 0, 12, 232);
        } else {
            g.drawString(newLocation, 0, 0, 232);
            g.setColor(Color.BLACK);
            g.drawRect(0, 152, 240, 8);
            g.setColor(Color.GREEN);
            g.drawString(":q to quit", 0, 152);
        }
    }

    @Override
    public int getId() {
        return ID;
    }

}
