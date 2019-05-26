/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.managers;

import leikr.GameRuntime;
import leikr.screens.LoadScreen;
import org.mini2Dx.core.screen.ScreenManager;

/**
 *
 * @author tor
 */
public class LeikrSystemManager {

    private boolean LOAD_PROGRAM = false;
    private boolean RUNNING = true;

    public void loadProgram(String name) {
        GameRuntime.setProgramPath("Programs/" + name);
        LOAD_PROGRAM = true;
    }

    public boolean update(ScreenManager sm) {
        if (LOAD_PROGRAM) {
            LOAD_PROGRAM = false;
            RUNNING = false;
            sm.enterGameScreen(LoadScreen.ID, null, null);

        }
        return RUNNING;
    }

    public void setRunning(boolean run) {
        RUNNING = run;
    }
}
