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
import com.badlogic.gdx.graphics.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
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

    String newLocation = "New Program template generated at: /Programs/";

    public NewProgramScreen() {
        viewport = new FitViewport(GameRuntime.WIDTH, GameRuntime.HEIGHT);
    }

    @Override
    public void initialise(GameContainer gc) {
    }

    @Override
    public void onResize(int width, int height) {
        Gdx.app.log("INFO", "Game window changed to " + width + "x" + height);
        viewport.onResize(width, height);
    }

    @Override
    public void preTransitionIn(Transition trns) {
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
                FileUtils.copyDirectory(new File("Data/Templates/NewProgram"), new File("Programs/NewProgram" + index));
                newLocation += "NewProgram" + index + "/";
            } else {
                FileUtils.copyDirectory(new File("Data/Templates/NewProgram"), new File("Programs/NewProgram"));
                newLocation += "NewProgram/";
            }
            System.out.println("NewProgram template copied to Programs directory");
        } catch (IOException ex) {
            Logger.getLogger(NewProgramScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int i) {
                if (i == Input.Keys.ESCAPE || i == Input.Keys.Q || i == Input.Keys.ENTER || i == Input.Keys.SPACE) {
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
                    public boolean buttonDown(Controller controller, int buttonIndex) {
                        BACK = true;
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
            BACK = false;
            sm.enterGameScreen(MenuScreen.ID, null, null);
        }
    }

    @Override
    public void interpolate(GameContainer gc, float f) {
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        viewport.apply(g);
        g.drawString(newLocation, 0, 0, 232);
        g.setColor(Color.BLACK);
        g.drawRect(0, 152, 240, 8);
        g.setColor(Color.GREEN);
        g.drawString(":q to quit", 0, 152);
    }

    @Override
    public int getId() {
        return ID;
    }

}
