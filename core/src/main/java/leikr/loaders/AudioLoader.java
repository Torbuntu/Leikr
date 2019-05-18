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
package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import leikr.GameRuntime;

/**
 *
 * @author tor
 */
public class AudioLoader {

    AssetManager soundManager;
    AssetManager musicManager;

    String musicRootPath = GameRuntime.getProgramPath() + "/Audio/Music/";
    String soundRootPath = GameRuntime.getProgramPath() + "/Audio/Sound/";
    
    Music mPlayer;
    Sound sPlayer;

    public AudioLoader() {
        soundManager = new AssetManager();
        musicManager = new AssetManager();
        loadAudio();
    }

    private void loadAudio() {
        try {
            for (String path : new File(soundRootPath).list()) {
                soundManager.load(soundRootPath + path, Sound.class);
            }
            soundManager.finishLoading();
        } catch (Exception ex) {
            System.out.println("Sound load err: " + ex.getMessage());
        }

        try {
            for (String path : new File(musicRootPath).list()) {
                musicManager.load(musicRootPath + path, Music.class);
            }
            musicManager.finishLoading();
        } catch (Exception ex) {
            System.out.println("Music load err: " + ex.getMessage());
        }
    }

    //probably useless, but makes me feel safe. 
    public void load() {
        soundManager.finishLoading();
        musicManager.finishLoading();
    }

    public void sound(String fileName) {
        sPlayer = soundManager.get(soundRootPath + fileName + ".wav", Sound.class);
        sPlayer.play();
    }

    public void sound(String fileName, float vol, float pit, float pan) {
        //vol: range [0,1]
        //pit: 0.5 and 2.0
        //pan: panning in the range -1 (full left) to 1 (full right). 0 is center position.
        sPlayer = soundManager.get(soundRootPath + fileName + ".wav", Sound.class);
        sPlayer.play(vol, pit, pan);
    }

    public void music(String fileName) {
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
        mPlayer.play();
    }

    public void music(String fileName, boolean loop) {
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
        mPlayer.setLooping(loop);
        mPlayer.play();
    }

    public void stopMusic() {
        Array<Music> allMusic = new Array<>();
        for (Music m : musicManager.getAll(Music.class, allMusic)) {
            m.stop();
        }
    }

    public void stopMusic(String fileName) {
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
        mPlayer.stop();
    }

    public void disposeAudioLoader() {
        musicManager.dispose();
        soundManager.dispose();
    }

}
