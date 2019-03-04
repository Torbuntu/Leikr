/*
 * Copyright 2019 torbuntu.
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

/**
 *
 * @author tor
 */
public class LeikrController implements ControllerListener {

    //controller buttons
    boolean buttonAisPressed = false;
    boolean buttonBisPressed = false;
    boolean buttonXisPressed = false;
    boolean buttonYisPressed = false;
    boolean bumperLeftPressed = false;
    boolean bumperRightPressed = false;
    boolean buttonSelect = false;
    boolean buttonStart = false;

    //d-pad buttons
    boolean leftButtonPressed = false;
    boolean rightButtonPressed = false;
    boolean upButtonPressed = false;
    boolean downButtonPressed = false;

    //engine api for returning boolean status of button presses on snes style controller
    public boolean button(int button) {
        switch (button) {
            case 0:
                return buttonXisPressed;
            case 1:
                return buttonAisPressed;
            case 2:
                return buttonBisPressed;
            case 3:
                return buttonYisPressed;
            case 4:
                return bumperLeftPressed;
            case 5:
                return bumperRightPressed;
            case 8:
                return buttonSelect;
            case 9:
                return buttonStart;
            case 10:
                return upButtonPressed;
            case 11:
                return rightButtonPressed;
            case 12:
                return downButtonPressed;
            case 13:
                return leftButtonPressed;
            default:
                return false;
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = true;
                return true;
            case 1:
                buttonAisPressed = true;
                return true;
            case 2:
                buttonBisPressed = true;
                return true;
            case 3:
                buttonYisPressed = true;
                return true;
            case 4:
                bumperLeftPressed = true;
                return true;
            case 5:
                bumperRightPressed = true;
                return true;
            case 8:
                buttonSelect = true;
                return true;
            case 9:
                buttonStart = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        switch (buttonCode) {
            case 0:
                buttonXisPressed = false;
                return true;
            case 1:
                buttonAisPressed = false;
                return true;
            case 2:
                buttonBisPressed = false;
                return true;
            case 3:
                buttonYisPressed = false;
                return true;
            case 4:
                bumperLeftPressed = false;
                return true;
            case 5:
                bumperRightPressed = false;
                return true;
            case 8:
                buttonSelect = false;
                return true;
            case 9:
                buttonStart = false;
                return true;
        }
        return false;
    }

    //Keypad
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        //axis 0 = x axis -1 = left 1 = right
        //axis 1 = y axis -1 = up 1 = down
        System.out.println("Axis moved: " + axisCode + " : " + (int) value);

        if ((int) value == 0) {
            leftButtonPressed = false;
            rightButtonPressed = false;
            upButtonPressed = false;
            downButtonPressed = false;
        }
        if (axisCode == 1) {
            if (value == 1) {
                downButtonPressed = true;
            } else if (value == -1) {
                upButtonPressed = true;
            }
            return true;
        } else {
            if (value == 1) {
                rightButtonPressed = true;
            } else if (value == -1) {
                leftButtonPressed = true;
            }
            return true;
        }
    }

    //Unused methods
    @Override
    public void connected(Controller controller) {
    }

    @Override
    public void disconnected(Controller controller) {
    }

    @Override
    public boolean povMoved(Controller cntrlr, int i, PovDirection pd) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller cntrlr, int i, boolean bln) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller cntrlr, int i, boolean bln) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller cntrlr, int i, Vector3 vctr) {
        return false;
    }

}
