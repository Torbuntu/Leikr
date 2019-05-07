/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import leikr.customProperties.CustomSystemProperties;
import static leikr.screens.MenuScreen.getGameName;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.apache.commons.io.FileUtils;
import org.mini2Dx.core.graphics.viewport.FitViewport;

/**
 *
 * @author tor
 */
public class NewProgramScreen extends BasicGameScreen {

    public static int ID = 6;
    FitViewport viewport;

    boolean BACK = false;

    public NewProgramScreen() {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void preTransitionIn(Transition trns) {
        BACK = false;
        try {
            String[] programs = new File("Programs").list();
            int index = 0;
            String NP = "NewProgram";
            for (String name : programs) {
                if (name.contains(NP)) {
                    index++;
                }
            }
            if (index > 0) {
                FileUtils.copyDirectory(new File("Data/Templates/NewProgram"), new File("Programs/NewProgram"+index));

            } else {
                FileUtils.copyDirectory(new File("Data/Templates/NewProgram"), new File("Programs/NewProgram"));

            }
            System.out.println("NewProgram template copied to Programs directory");
        } catch (IOException ex) {
            Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {

                if (i == Input.Keys.ESCAPE) {
                    BACK = true;
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

                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            System.out.println("No controllers active on NewProgram Screen. ");
            Logger.getLogger(NewProgramScreen.class.getName()).log(Level.INFO, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float f) {
        if (BACK) {
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) {
        viewport.apply(grphcs);
        grphcs.drawString("New Game", 0, 0);
    }

    @Override
    public int getId() {
        return ID;
    }

}
