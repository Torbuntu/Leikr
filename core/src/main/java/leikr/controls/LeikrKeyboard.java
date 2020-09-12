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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.Input.Keys;

/**
 * The class to handle keyboard interactions within Leikr programs
 *
 * @author Torbuntu
 */
public class LeikrKeyboard {

    /**
     * Returns boolean of if the value of a key is pressed. Examples: "K",
     * "Enter", "Left Shift"
     *
     * @param key
     * @return
     */
    public boolean key(String key) {
        return Mdx.input.isKeyDown(Keys.valueOf(key));
    }

    /**
     * Returns boolean of if the value of a key is NOT pressed.
     *
     * @param key
     * @return
     */
    public boolean keyUp(String key) {
        return Mdx.input.isKeyUp(Keys.valueOf(key));
    }

    /**
     * Returns boolean of if the value of a key is just pressed. Will run once
     * when key is pressed, unlike key which will poll continuously.
     *
     * @see key() Examples: "K", "Enter", "Left Shift"
     * @param key
     * @return
     */
    public boolean keyPress(String key) {
        return Mdx.input.isKeyJustPressed(Keys.valueOf(key));
    }
}
