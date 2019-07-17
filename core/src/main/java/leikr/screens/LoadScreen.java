package leikr.screens;

import com.badlogic.gdx.graphics.Color;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.Engine;
import leikr.GameRuntime;
import leikr.loaders.EngineLoader;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

/**
 *
 * @author tor
 */
public class LoadScreen extends BasicGameScreen {

    public static int ID = 5;

    FitViewport viewport;
    ExecutorService service;
    Future engineGetter;

    int loadCircleCircumf = 15;
    int loadCircleDir = 1;
    String loadPhrase = "Loading... ";

    public LoadScreen() {
        service = Executors.newFixedThreadPool(1);
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void preTransitionIn(Transition transition) {
        engineGetter = service.submit(EngineLoader.getEngineLoader(true));
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (engineGetter.isDone()) {
            try {
                EngineScreen scrn = (EngineScreen) sm.getGameScreen(EngineScreen.ID);
                scrn.setEngine((Engine) engineGetter.get());
                sm.enterGameScreen(EngineScreen.ID, new FadeOutTransition(Color.TEAL), new FadeInTransition(Color.FOREST));
            } catch (InterruptedException | ExecutionException ex) {
                ErrorScreen.setErrorMessage("Error loading engine: " + ex.getMessage());
                sm.enterGameScreen(ErrorScreen.ID, null, null);
                Logger.getLogger(LoadScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (loadCircleCircumf >= 15 && loadCircleDir > 0) {
            loadCircleDir = -1;
        }
        if (loadCircleCircumf <= 1 && loadCircleDir < 0) {
            loadCircleDir = 1;
        }

        if (loadCircleCircumf <= 5) {
            loadPhrase = "Loading. ";
        } else if (loadCircleCircumf <= 10) {
            loadPhrase = "Loading.. ";
        } else if (loadCircleCircumf <= 15) {
            loadPhrase = "Loading... ";
        }

        loadCircleCircumf += loadCircleDir;
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        if (!engineGetter.isDone()) {
            g.setColor(Color.FOREST);
            g.drawCircle(120, 80, 16);
            g.setColor(Color.MAGENTA);
            g.fillCircle(120, 80, loadCircleCircumf);
            g.drawString(loadPhrase, 0, viewport.getHeight() - 9);
        }
    }

    @Override
    public int getId() {
        return ID;
    }

}
