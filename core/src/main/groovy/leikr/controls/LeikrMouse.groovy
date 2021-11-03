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
package leikr.controls

import org.mini2Dx.core.Mdx
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.gdx.math.Vector2

/**
 * The class to handle mouse interactions within Leikr programs
 *
 * @author Torbuntu
 */
class LeikrMouse {

    private StretchViewport viewport

    private final Vector2 realMouse
    private final Vector2 leikrMouse

    LeikrMouse() {
        realMouse = new Vector2()
        leikrMouse = new Vector2()
    }

    /**
     * Updates the Game world coordinates of the real mouse pointer using the
     * Game Screen's StretchViewport
     *
     */
    private void updateMouse() {
        realMouse.x = Mdx.input.getX()
        realMouse.y = Mdx.input.getY()
        viewport.toWorldCoordinates(leikrMouse, realMouse.x, realMouse.y)
    }

    void setViewport(StretchViewport view) {
        viewport = view
    }

    /**
     * Detects a mouse click event.
     * 
     * Note: this does not check which button was pressed. Assume Leikr only
     * has one mouse button.
     *
     * @return if the mouse was just clicked. 
     */
    static boolean mouseClick() {
        Mdx.input.justTouched()
    }
    
    /**
     * Gets the mouse X coordinate after updating position relative to game world
     * @return the mouse X coordinate
     */
    float mouseX() {
        updateMouse()
        return leikrMouse.x
    }

    /**
     * Gets the mouse Y coordinate after updating position relative to game world
     * @return the mouse Y coordinate
     */
    float mouseY() {
        updateMouse()
        return leikrMouse.y
    }

}
