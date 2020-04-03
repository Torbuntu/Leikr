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
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.core.input.GamePad;

/**
 *
 * @author tor
 */
public class LeikrController implements ControllerListener {

    HashMap<Object, Boolean> buttons = new HashMap<>();
    HashMap<Object, Object> btnCodes = new HashMap<>();
    HashMap<Object, Object> btnLookup = new HashMap<>();

    private static LeikrController instance;
    private static LeikrController instanceTwo;

    String UP = "UP";
    String DOWN = "DOWN";
    String LEFT = "LEFT";
    String RIGHT = "RIGHT";
    String A = "A";
    String B = "B";
    String X = "X";
    String Y = "Y";
    String LEFT_BUMPER = "LEFT_BUMPER";
    String RIGHT_BUMPER = "RIGHT_BUMPER";
    String SELECT = "SELECT";
    String START = "START";

    public static LeikrController getLeikrControllerListenerA() {
        if (instance == null) {
            instance = new LeikrController();
        }
        return instance;
    }

    public static LeikrController getLeikrControllerListenerB() {
        if (instanceTwo == null) {
            instanceTwo = new LeikrController();
        }
        return instanceTwo;
    }

    public LeikrController() {
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

        btnCodes.put(CustomSystemProperties.A, A);
        btnCodes.put(CustomSystemProperties.B, B);
        btnCodes.put(CustomSystemProperties.X, X);
        btnCodes.put(CustomSystemProperties.Y, Y);
        btnCodes.put(CustomSystemProperties.LEFT_BUMPER, LEFT_BUMPER);
        btnCodes.put(CustomSystemProperties.RIGHT_BUMPER, RIGHT_BUMPER);
        btnCodes.put(CustomSystemProperties.SELECT, SELECT);
        btnCodes.put(CustomSystemProperties.START, START);

        btnLookup.put(A, CustomSystemProperties.A);
        btnLookup.put(B, CustomSystemProperties.B);
        btnLookup.put(X, CustomSystemProperties.X);
        btnLookup.put(Y, CustomSystemProperties.Y);
        btnLookup.put(LEFT_BUMPER, CustomSystemProperties.LEFT_BUMPER);
        btnLookup.put(RIGHT_BUMPER, CustomSystemProperties.RIGHT_BUMPER);
        btnLookup.put(SELECT, CustomSystemProperties.SELECT);
        btnLookup.put(START, CustomSystemProperties.START);

        btnLookup.put(UP, CustomSystemProperties.UP);
        btnLookup.put(DOWN, CustomSystemProperties.DOWN);
        btnLookup.put(LEFT, CustomSystemProperties.LEFT);
        btnLookup.put(RIGHT, CustomSystemProperties.RIGHT);
    }

    //engine api for returning boolean status of button presses on snes style controller
    public boolean button(String button) {
        return (boolean) buttons.get(button.toUpperCase());
    }

    public int horizontalAxis() {
        return CustomSystemProperties.HORIZONTAL_AXIS;
    }

    public int verticalAxis() {
        return CustomSystemProperties.VERTICAL_AXIS;
    }

//    @Override
//    public void onButtonDown(GamePad controller, int buttonCode) {
//        if (CustomSystemProperties.DEBUG) {
//            System.out.println("Button Down : " + buttonCode);
//        }
//        buttons.replace(btnCodes.get(buttonCode), true);
//    }

//    @Override
//    public void onButtonUp(GamePad controller, int buttonCode) {
//        if (CustomSystemProperties.DEBUG) {
//            System.out.println("Button Up : " + buttonCode);
//        }
//        buttons.replace(btnCodes.get(buttonCode), false);
//    }

    //Keypad
//    @Override
//    public void onAxisChanged(GamePad controller, int axisCode, float value) {
//        //Legacy codes: axis 0 = x axis -1 = left 1 = right
//        //Legacy codes: axis 1 = y axis -1 = up 1 = down
//        if (CustomSystemProperties.DEBUG) {
//            System.out.println(controller.getGamePadType().toFriendlyString() + " : " + axisCode + " | " + value);
//        }
//        if ((int) value == 0) {
//            buttons.replace(UP, false);
//            buttons.replace(DOWN, false);
//            buttons.replace(LEFT, false);
//            buttons.replace(RIGHT, false);
//        }
//
//        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
//            if ((int) value == CustomSystemProperties.DOWN) {
//                buttons.replace(DOWN, true);
//            }
//            if ((int) value == CustomSystemProperties.UP) {
//                buttons.replace(UP, true);
//            }
//            if (CustomSystemProperties.DEBUG) {
//                System.out.println("Vertical Axis : " + value);
//            }
//        }
//        if (axisCode == CustomSystemProperties.HORIZONTAL_AXIS) {
//            if ((int) value == CustomSystemProperties.RIGHT) {
//                buttons.replace(RIGHT, true);
//            }
//            if ((int) value == CustomSystemProperties.LEFT) {
//                buttons.replace(LEFT, true);
//            }
//            if (CustomSystemProperties.DEBUG) {
//                System.out.println("Horizontal Axis : " + value);
//            }
//        }
//    }
    
    
    

    @Override
    public void connected(Controller controller) {
        System.out.println("Connection: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println("Disconnection: " + controller.getName());
    }
    
    @Override
    public boolean buttonDown(Controller controller, int buttonCode){
        System.out.println("Controller: " + controller.getName() + " , Code: " + buttonCode);
        if (CustomSystemProperties.DEBUG) {
            System.out.println("Button Down : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), true);
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        System.out.println("Controller: " + controller.getName() + " , Code: " + buttonCode);
        if (CustomSystemProperties.DEBUG) {
            System.out.println("Button Up : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), false);
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        System.out.println("Controller: " + controller.getName() + " , axis: " + axisCode + " , value: " + value);
        //Legacy codes: axis 0 = x axis -1 = left 1 = right
        //Legacy codes: axis 1 = y axis -1 = up 1 = down
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getName()+ " : " + axisCode + " | " + value);
        }
        if ((int) value == 0) {
            buttons.replace(UP, false);
            buttons.replace(DOWN, false);
            buttons.replace(LEFT, false);
            buttons.replace(RIGHT, false);
        }

        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
            if ((int) value == CustomSystemProperties.DOWN) {
                buttons.replace(DOWN, true);
            }
            if ((int) value == CustomSystemProperties.UP) {
                buttons.replace(UP, true);
            }
            if (CustomSystemProperties.DEBUG) {
                System.out.println("Vertical Axis : " + value);
            }
        }
        if (axisCode == CustomSystemProperties.HORIZONTAL_AXIS) {
            if ((int) value == CustomSystemProperties.RIGHT) {
                buttons.replace(RIGHT, true);
            }
            if ((int) value == CustomSystemProperties.LEFT) {
                buttons.replace(LEFT, true);
            }
            if (CustomSystemProperties.DEBUG) {
                System.out.println("Horizontal Axis : " + value);
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
