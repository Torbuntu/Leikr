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
package leikr.managers;

import leikr.loaders.AudioLoader;

/**
 *
 * @author tor
 */
public class LeikrAudioManager {

    private final AudioLoader audioLoader;

    public LeikrAudioManager() {
        audioLoader = new AudioLoader();
    }

    //START loaded audio methods
    public final void sfx(String name) {
        audioLoader.sound(name);
    }

    public final void sfx(String name, float vol, float pit, float pan) {
        audioLoader.sound(name, vol, pit, pan);
    }

    public final void music(String name) {
        audioLoader.music(name);
    }

    public final void music(String name, boolean loop) {
        audioLoader.music(name, loop);
    }

    public final void stopAllMusic() {
        audioLoader.stopMusic();
    }

    public final void stopMusic(String fileName) {
        audioLoader.stopMusic(fileName);
    }
    //END loaded audio methods

    //Engine methods
    public void dispose() {
        audioLoader.disposeAudioLoader();
    }

}
