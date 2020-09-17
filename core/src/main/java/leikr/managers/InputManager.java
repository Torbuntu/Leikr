/*
 * Copyright 2020 tor.
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
package leikr.managers;

import leikr.controls.LeikrController;
import leikr.controls.LeikrKeyboard;
import leikr.controls.LeikrMouse;
import leikr.customProperties.CustomSystemProperties;
import org.mini2Dx.core.graphics.viewport.StretchViewport;

/**
 *
 * @author tor
 */
public class InputManager {

    private final LeikrController controllerA;
    private final LeikrController controllerB;
    private final LeikrKeyboard keyboard;
    private final LeikrMouse mouse;

    public InputManager(CustomSystemProperties customSystemProperties) {
        controllerA = new LeikrController(customSystemProperties);
        controllerB = new LeikrController(customSystemProperties);
        keyboard = new LeikrKeyboard();
        mouse = new LeikrMouse();
    }

    public void setMouseViewport(StretchViewport viewport) {
        mouse.setViewport(viewport);
    }

    public LeikrController getControllerA() {
        return controllerA;
    }

    public LeikrController getControllerB() {
        return controllerB;
    }

    public LeikrKeyboard getKeyboard() {
        return keyboard;
    }

    public LeikrMouse getMouse() {
        return mouse;
    }

}
