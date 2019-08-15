package leikr.controls;

import java.util.HashMap;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.input.BaseGamePadListener;
import org.mini2Dx.core.input.GamePad;

/**
 *
 * @author tor
 */

//TODO: This whole input system needs to be fixed. Mapping is currently broken with the libgdx backends.
public class LeikrController extends BaseGamePadListener {

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

    public String btnName(int id) {
        return btnCodes.get(id).toString();
    }

    public int btnCode(String button) {
        return (int) btnLookup.get(button.toUpperCase());
    }

    public void setController(BaseGamePadListener adap) {
//        Mdx.input.getGamePads().clearListeners();
//        Controllers.addListener(adap);
    }

    public int horizontalAxis() {
        return CustomSystemProperties.HORIZONTAL_AXIS;
    }

    public int verticalAxis() {
        return CustomSystemProperties.VERTICAL_AXIS;
    }

    @Override
    public void onButtonDown(GamePad controller, int buttonCode) {
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getGamePadType().toFriendlyString() + " : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), true);
    }

    @Override
    public void onButtonUp(GamePad controller, int buttonCode) {
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getGamePadType().toFriendlyString() + " : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), false);
    }

    //Keypad
    @Override
    public void onAxisChanged(GamePad controller, int axisCode, float value) {
        //Legacy codes: axis 0 = x axis -1 = left 1 = right
        //Legacy codes: axis 1 = y axis -1 = up 1 = down
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getGamePadType().toFriendlyString() + " : " + axisCode + " | " + value);
        }
        if ((int) value == 0) {
            buttons.replace(UP, false);
            buttons.replace(DOWN, false);
            buttons.replace(LEFT, false);
            buttons.replace(RIGHT, false);
        }

        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
            if ((int)value == CustomSystemProperties.DOWN) {
                buttons.replace(DOWN, true);
            }
            if ((int)value == CustomSystemProperties.UP) {
                buttons.replace(UP, true);
            }
        }
        if (axisCode == CustomSystemProperties.HORIZONTAL_AXIS) {
            if ((int)value == CustomSystemProperties.RIGHT) {
                buttons.replace(RIGHT, true);
            }
            if ((int)value == CustomSystemProperties.LEFT) {
                buttons.replace(LEFT, true);
            }
        }
    }
}
