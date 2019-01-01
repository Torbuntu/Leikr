package leikr;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

/**
 *
 * @author tor
 */
public class LeikrController {

    Controller controller;

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

    LeikrController(int id) {
        try {
            controller = Controllers.getControllers().get(id);
            setupController(controller);
        } catch (Exception ex) {
            System.out.println(ex + " : Controller doesn't exist");
        }
    }

    private void setupController(Controller cont) {
        cont.addListener(new ControllerAdapter() {

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
        });
    }

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

}
