package leikr.controls;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;
import leikr.customProperties.CustomSystemProperties;

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
    
    public String btnName(int id){
        return btnCodes.get(id).toString();
    }

    public int btnCode(String button){
        return (int) btnLookup.get(button.toUpperCase());
    } 
    
    public void setController(ControllerAdapter adap){
        Controllers.clearListeners();
        Controllers.addListener(adap);
    }
    
    public int horizontalAxis(){
        return CustomSystemProperties.HORIZONTAL_AXIS;
    }
    public int verticalAxis(){
        return CustomSystemProperties.VERTICAL_AXIS;
    }
    
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getName() + " : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), true);
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getName() + " : " + buttonCode);
        }
        buttons.replace(btnCodes.get(buttonCode), false);
        return false;
    }

    //Keypad
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        //Legacy codes: axis 0 = x axis -1 = left 1 = right
        //Legacy codes: axis 1 = y axis -1 = up 1 = down
        if (CustomSystemProperties.DEBUG) {
            System.out.println(controller.getName() + " : " + axisCode + " | " + value);
        }
        if ((int) value == 0) {
            buttons.replace(UP, false);
            buttons.replace(DOWN, false);
            buttons.replace(LEFT, false);
            buttons.replace(RIGHT, false);
        }

        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
            if (value == CustomSystemProperties.DOWN) {
                buttons.replace(DOWN, true);
            }
            if (value == CustomSystemProperties.UP) {
                buttons.replace(UP, true);
            }
        }
        if (axisCode == CustomSystemProperties.HORIZONTAL_AXIS) {
            if (value == CustomSystemProperties.RIGHT) {
                buttons.replace(RIGHT, true);
            }
            if (value == CustomSystemProperties.LEFT) {
                buttons.replace(LEFT, true);
            }
        }

        return false;
    }

    //Unused methods
    @Override
    public void connected(Controller controller) {
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println("Controller lost...");
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
