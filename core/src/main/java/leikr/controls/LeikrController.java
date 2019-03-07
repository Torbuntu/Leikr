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
import java.util.HashMap;
import java.util.Map;
import leikr.Engine;
import leikr.customProperties.CustomSystemProperties;

/**
 *
 * @author tor
 */
public class LeikrController implements ControllerListener {

    Map buttons = new HashMap();

    public LeikrController() {
        buttons.put(Engine.BTN.A, false);
        buttons.put(Engine.BTN.B, false);
        buttons.put(Engine.BTN.X, false);
        buttons.put(Engine.BTN.Y, false);
        buttons.put(Engine.BTN.LEFT_BUMPER, false);
        buttons.put(Engine.BTN.RIGHT_BUMPER, false);
        buttons.put(Engine.BTN.SELECT, false);
        buttons.put(Engine.BTN.START, false);
        
        buttons.put(Engine.BTN.LEFT, false);
        buttons.put(Engine.BTN.RIGHT, false);
        buttons.put(Engine.BTN.UP, false);
        buttons.put(Engine.BTN.DOWN, false);
        
    }

    //engine api for returning boolean status of button presses on snes style controller
    public boolean button(Engine.BTN button) {
        return (boolean) buttons.get(button);
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        if(buttonCode == CustomSystemProperties.X){
            buttons.replace(Engine.BTN.X, true);
        }
        if(buttonCode == CustomSystemProperties.A){
            buttons.replace(Engine.BTN.A, true);
        }
        if(buttonCode == CustomSystemProperties.B){
            buttons.replace(Engine.BTN.B, true);
        }
        if(buttonCode == CustomSystemProperties.Y){
            buttons.replace(Engine.BTN.Y, true);
        }
        if(buttonCode == CustomSystemProperties.LEFT_BUMPER){
            buttons.replace(Engine.BTN.LEFT_BUMPER, true);
        }
        if(buttonCode == CustomSystemProperties.RIGHT_BUMPER){
            buttons.replace(Engine.BTN.RIGHT_BUMPER, true);
        }
        if(buttonCode == CustomSystemProperties.SELECT){
            buttons.replace(Engine.BTN.SELECT, true);
        }
        if(buttonCode == CustomSystemProperties.START){
            buttons.replace(Engine.BTN.START, true);
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        System.out.println(controller.getName() + " : " + buttonCode);
        if(buttonCode == CustomSystemProperties.X){
            buttons.replace(Engine.BTN.X, false);
        }
        if(buttonCode == CustomSystemProperties.A){
            buttons.replace(Engine.BTN.A, false);
        }
        if(buttonCode == CustomSystemProperties.B){
            buttons.replace(Engine.BTN.B, false);
        }
        if(buttonCode == CustomSystemProperties.Y){
            buttons.replace(Engine.BTN.Y, false);
        }
        if(buttonCode == CustomSystemProperties.LEFT_BUMPER){
            buttons.replace(Engine.BTN.LEFT_BUMPER, false);
        }
        if(buttonCode == CustomSystemProperties.RIGHT_BUMPER){
            buttons.replace(Engine.BTN.RIGHT_BUMPER, false);
        }
        if(buttonCode == CustomSystemProperties.SELECT){
            buttons.replace(Engine.BTN.SELECT, false);
        }
        if(buttonCode == CustomSystemProperties.START){
            buttons.replace(Engine.BTN.START, false);
        }
        
        return false;
    }

    //Keypad
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        //axis 0 = x axis -1 = left 1 = right
        //axis 1 = y axis -1 = up 1 = down
        if ((int) value == 0) {
            buttons.replace(Engine.BTN.UP, false);
            buttons.replace(Engine.BTN.DOWN, false);
            buttons.replace(Engine.BTN.LEFT, false);
            buttons.replace(Engine.BTN.RIGHT, false);
        }

        if (axisCode == CustomSystemProperties.VERTICAL_AXIS) {
            if (value == CustomSystemProperties.DOWN) {
                buttons.replace(Engine.BTN.DOWN, true);
                System.out.println("DOWN is pressed");
            } else if (value == CustomSystemProperties.UP) {
                buttons.replace(Engine.BTN.UP, true);
                System.out.println("UP is pressed");
            }
        } else {
            if (value == CustomSystemProperties.RIGHT) {
                buttons.replace(Engine.BTN.RIGHT, true);
            } else if (value == CustomSystemProperties.LEFT) {
                buttons.replace(Engine.BTN.LEFT, true);
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
