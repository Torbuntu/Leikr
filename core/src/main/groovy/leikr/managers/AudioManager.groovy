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
package leikr.managers

import leikr.loaders.AudioLoader
import org.mini2Dx.core.audio.Music
import org.mini2Dx.core.audio.Sound
/**
 *
 * @author tor
 */
class AudioManager {

    private final AudioLoader audioLoader

    AudioManager(AudioLoader audioLoader) {
        this.audioLoader = audioLoader
    }

    void resetAudioManager(String path) {
        audioLoader.resetAudioLoader(path)
    }

    //START loaded audio methods
    final Sound getSound(String fileName) {
        return audioLoader.getSound(fileName)
    }

    final void playSound(String name) {
        audioLoader.playSound(name)
    }

    final void playSound(String name, Number vol, Number pit, Number pan) {
        audioLoader.playSound(name, vol, pit, pan)
    }

    final void stopSound() {
        audioLoader.stopSound()
    }

    final void stopSound(String fileName) {
        audioLoader.stopSound(fileName)
    }

    final Music getMusic(String fileName) {
        return audioLoader.getMusic(fileName)
    }

    final void playMusic(String name) {
        audioLoader.playMusic(name)
    }

    final void playMusic(String name, boolean loop) {
        audioLoader.playMusic(name, loop)
    }

    final void stopMusic() {
        audioLoader.stopMusic()
    }

    final void stopMusic(String fileName) {
        audioLoader.stopMusic(fileName)
    }

    void pauseAudio() {
        audioLoader.pauseAudio()
    }

    void resumeAudio() {
        audioLoader.resumeAudio()
    }
    //END loaded audio methods

    //Experimental
    def listSounds(){
        audioLoader.listSounds()
    }
    def listMusic(){
        audioLoader.listMusic()
    }

    //Engine methods
    void dispose() {
        audioLoader.disposeAudioLoader()
    }

}
