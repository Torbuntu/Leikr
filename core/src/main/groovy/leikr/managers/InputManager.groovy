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

import leikr.controls.LeikrKeyboard
import leikr.controls.LeikrMouse
import leikr.customProperties.CustomSystemProperties
import org.mini2Dx.core.graphics.viewport.StretchViewport

/**
 *
 * @author tor
 */
class InputManager {

    private final LeikrKeyboard keyboard
    private final LeikrMouse mouse

    InputManager(CustomSystemProperties customSystemProperties) {
        keyboard = new LeikrKeyboard()
        mouse = new LeikrMouse()
    }

    void setMouseViewport(StretchViewport viewport) {
        mouse.setViewport(viewport)
    }

    LeikrKeyboard getKeyboard() {
        return keyboard
    }

    LeikrMouse getMouse() {
        return mouse
    }

}
