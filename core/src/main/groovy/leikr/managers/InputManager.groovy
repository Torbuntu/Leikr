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
package leikr.managers

import leikr.controls.LeikrController
import leikr.controls.LeikrKeyboard
import leikr.controls.LeikrMouse
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.input.GamePad
import org.mini2Dx.core.input.GamePadConnectionListener

/**
 *
 * @author tor
 */
// TODO: work out the hot-plugging to be a bit more robust
class InputManager implements GamePadConnectionListener {

    private final LeikrKeyboard keyboard
    private final LeikrMouse mouse
    private LeikrController controllerA
    private LeikrController controllerB

    InputManager() {
        keyboard = new LeikrKeyboard()
        mouse = new LeikrMouse()
        Mdx.input.setGamePadConnectionListener(this, true)
    }

    void createControllers() {
        controllerA = new LeikrController(0)
        controllerB = new LeikrController(1)
    }

    void setMouseViewport(StretchViewport viewport) {
        mouse.setViewport(viewport)
    }

    LeikrKeyboard getKeyboard() {
        keyboard
    }

    LeikrMouse getMouse() {
        mouse
    }

    LeikrController getControllerA() {
        controllerA
    }

    LeikrController getControllerB() {
        controllerB
    }

    boolean button(String key) {
        controllerA.button(key)
    }

    boolean button(String key, int playerId) {
        playerId == 1 ? controllerA.button(key) : controllerB.button(key)
    }

    @Override
    void onConnect(GamePad gamePad) {
        println gamePad.getModelInfo()
        if (Mdx.input.getGamePads().size() > 0 && controllerA?.getInstanceId() == 0) {
            controllerA = new LeikrController(0, gamePad.getModelInfo())
            Mdx.input.getGamePads().get(0).addListener(controllerA)
            controllerA.setInstanceId(gamePad.getInstanceId())
        }

        if (Mdx.input.getGamePads().size() > 1 && controllerB?.getInstanceId() == 0) {
            controllerB = new LeikrController(1, gamePad.getModelInfo())
            Mdx.input.getGamePads().get(1).addListener(controllerB)
            controllerB.setInstanceId(gamePad.getInstanceId())
        }
    }

    @Override
    void onDisconnect(GamePad gamePad) {
        println "InstanceId: ${gamePad.getInstanceId()}"
        if (controllerA.getInstanceId() != 0 && gamePad.getInstanceId() == controllerA.getInstanceId()) {
            controllerA.setInstanceId(0)
        }
        if (controllerB.getInstanceId() != 0 && gamePad.getInstanceId() == controllerB.getInstanceId()) {
            controllerB.setInstanceId(0)
        }
    }
}
