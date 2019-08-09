package leikr.screens;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.gdx.InputProcessor;

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

    String newLocation = "New Program template generated at: /Programs/";

    public NewProgramScreen() {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionOut(Transition transOut) {

    }

    @Override
    public void preTransitionIn(Transition trns) {
        newLocation = "New Program template generated at: /Programs/";
        newName = "";
        Mdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int i) {
                if (FINISH) {
                    if (i == Keys.Q || i == Keys.SPACE) {
                        BACK = true;
                    }
                }
                if (i == Keys.ESCAPE) {
                    BACK = true;
                }
                if (i == Keys.ENTER) {
                    CREATE = true;
                }
                if (i == Keys.BACKSPACE && newName.length() > 0) {
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

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
               return false;
            }
        });
     
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
                for (FileHandle name : Mdx.files.local("Programs").list()) {
                    if (name.name().contains(NP)) {
                        index++;
                    }
                }
                if (index > 0) {
                    NP = NP + index;
                }
                Mdx.files.local("Programs/" + NP).mkdirs();
                for (FileHandle file : Mdx.files.local("Data/Templates/NewProgram").list()) {
                    Mdx.files.local("Data/Templates/NewProgram/" + file.name()).copyTo(Mdx.files.local("Programs/" + NP));
                }
                Mdx.files.local("Programs/" + NP + "/Code/main.groovy").moveTo(Mdx.files.local("Programs/" + NP + "/Code/" + NP + ".groovy"));
                newLocation += NP + "/";
                System.out.println(NP + " template copied to Programs directory");
                FINISH = true;
            } catch (IOException ex) {
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
            g.setColor(Colors.GREEN());
            g.drawString("Enter new program name: ", 0, 0);
            g.drawString(newName, 0, 12, 232);
        } else {
            g.drawString(newLocation, 0, 0, 232);
            g.setColor(Colors.BLACK());
            g.drawRect(0, 152, 240, 8);
            g.setColor(Colors.GREEN());
            g.drawString(":q to quit", 0, 152);
        }
    }

    @Override
    public int getId() {
        return ID;
    }

}
