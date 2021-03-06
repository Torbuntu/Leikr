/*
 * Copyright 2019 See AUTHORS file.
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
package leikr.controls;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.customProperties.CustomSystemProperties;

/**
 *
 * @author tor
 */
public class LeikrController implements ControllerListener {

    private final int ID;

    private final HashMap<Object, Boolean> buttons = new HashMap<>();
    private final HashMap<Object, Object> btnCodes = new HashMap<>();
    private final HashMap<Object, Object> btnLookup = new HashMap<>();

    private final String UP = "UP";
    private final String DOWN = "DOWN";
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    private final String A = "A";
    private final String B = "B";
    private final String X = "X";
    private final String Y = "Y";
    private final String LEFT_BUMPER = "LEFT_BUMPER";
    private final String RIGHT_BUMPER = "RIGHT_BUMPER";
    private final String SELECT = "SELECT";
    private final String START = "START";

    private final int horizontalAxis;
    private final int verticalAxis;
    private final boolean debug;
    private final CustomSystemProperties customSystemProperties;

    public LeikrController(CustomSystemProperties customSystemProperties, int id) {
        this.customSystemProperties = customSystemProperties;
        this.ID = id;

        buttons.put(A, false);
        buttons.put(B, false);
        buttons.put(X, false);
        buttons.put(Y, false);
        buttons.put(LEFT_BUMPER, false);
        buttons.put(RIGHT_BUMPER, false);
        buttons.put(SELECT, false);
        buttons.put(START, false);

        buttons.put(LEFT, false);
        buttons.put(RIGHT, false);
        buttons.put(UP, false);
        buttons.put(DOWN, false);

        btnCodes.put(customSystemProperties.getA(), A);
        btnCodes.put(customSystemProperties.getB(), B);
        btnCodes.put(customSystemProperties.getX(), X);
        btnCodes.put(customSystemProperties.getY(), Y);
        btnCodes.put(customSystemProperties.getLeftBumper(), LEFT_BUMPER);
        btnCodes.put(customSystemProperties.getRightBumper(), RIGHT_BUMPER);
        btnCodes.put(customSystemProperties.getSelect(), SELECT);
        btnCodes.put(customSystemProperties.getStart(), START);

        btnLookup.put(A, customSystemProperties.getA());
        btnLookup.put(B, customSystemProperties.getB());
        btnLookup.put(X, customSystemProperties.getX());
        btnLookup.put(Y, customSystemProperties.getY());
        btnLookup.put(LEFT_BUMPER, customSystemProperties.getLeftBumper());
        btnLookup.put(RIGHT_BUMPER, customSystemProperties.getRightBumper());
        btnLookup.put(SELECT, customSystemProperties.getSelect());
        btnLookup.put(START, customSystemProperties.getStart());

        btnLookup.put(UP, customSystemProperties.getUp());
        btnLookup.put(DOWN, customSystemProperties.getDown());
        btnLookup.put(LEFT, customSystemProperties.getLeft());
        btnLookup.put(RIGHT, customSystemProperties.getRight());

        horizontalAxis = customSystemProperties.getHorizontalAxis();
        verticalAxis = customSystemProperties.getVerticalAxis();
        debug = customSystemProperties.isDEBUG();
    }

    /**
     * Check status of button presses on snes style controller
     * @param button
     * @return if the given button is pressed
     */ 
    public boolean button(String button) {
        return (boolean) buttons.get(button.toUpperCase());
    }

    public int horizontalAxis() {
        return horizontalAxis;
    }

    public int verticalAxis() {
        return verticalAxis;
    }

    @Override
    public void connected(Controller controller) {
        Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "ID: " + ID + ", New Connection: {0}", controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "ID: " + ID + ", Disconnected: {0}", controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (debug) {
            Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "ID: " + ID + ", Controller: {0} , Code: {1}", new Object[]{controller.getName(), buttonCode});
        }
        buttons.replace(btnCodes.get(buttonCode), true);
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (debug) {
            Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "ID: " + ID + ", Controller: {0} , Code: {1}", new Object[]{controller.getName(), buttonCode});
        }
        buttons.replace(btnCodes.get(buttonCode), false);
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {

        //Legacy codes: axis 0 = x axis -1 = left 1 = right
        //Legacy codes: axis 1 = y axis -1 = up 1 = down
        if (debug) {
            Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "ID: {0}, Controller: {1} , axis: {2} , value: {3}", new Object[]{ID, controller.getName(), axisCode, value});
        }
        if ((int) value == 0) {
            buttons.replace(UP, false);
            buttons.replace(DOWN, false);
            buttons.replace(LEFT, false);
            buttons.replace(RIGHT, false);
        }

        if (axisCode == verticalAxis) {
            if ((int) value == customSystemProperties.getDown()) {
                buttons.replace(DOWN, true);
            }
            if ((int) value == customSystemProperties.getUp()) {
                buttons.replace(UP, true);
            }
            if (debug) {
                Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "Vertical Axis : {0}", value);
            }
        }
        if (axisCode == horizontalAxis) {
            if ((int) value == customSystemProperties.getRight()) {
                buttons.replace(RIGHT, true);
            }
            if ((int) value == customSystemProperties.getLeft()) {
                buttons.replace(LEFT, true);
            }
            if (debug) {
                Logger.getLogger(LeikrController.class.getName()).log(Level.INFO, "Horizontal Axis : {0}", value);
            }
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
