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
package leikr.managers;

import java.math.BigDecimal;
import leikr.loaders.AudioLoader;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.audio.Music;

/**
 *
 * @author tor
 */
public class AudioManager {

    private final AudioLoader audioLoader;

    public AudioManager(AudioLoader audioLoader) {
        this.audioLoader = audioLoader;
    }

    public void resetAudioManager(String path) {
        audioLoader.resetAudioLoader(path);
    }

    //START loaded audio methods
    public final Sound getSound(String fileName) {
        return audioLoader.getSound(fileName);
    }

    public final void playSound(String name) {
        audioLoader.playSound(name);
    }

    public final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        audioLoader.playSound(name, vol, pit, pan);
    }

    public final void stopSound() {
        audioLoader.stopSound();
    }

    public final void stopSound(String fileName) {
        audioLoader.stopSound(fileName);
    }

    public final Music getMusic(String fileName) {
        return audioLoader.getMusic(fileName);
    }

    public final void playMusic(String name) {
        audioLoader.playMusic(name);
    }

    public final void playMusic(String name, boolean loop) {
        audioLoader.playMusic(name, loop);
    }

    public final void stopMusic() {
        audioLoader.stopMusic();
    }

    public final void stopMusic(String fileName) {
        audioLoader.stopMusic(fileName);
    }

    public void pauseAudio() {
        audioLoader.pauseAudio();
    }

    public void resumeAudio() {
        audioLoader.resumeAudio();
    }
    //END loaded audio methods

    //Engine methods
    public void dispose() {
        audioLoader.disposeAudioLoader();
    }

}
