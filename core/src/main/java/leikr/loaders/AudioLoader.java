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
import leikr.MenuScreen;

/**
 *
 * @author tor
 */
public class AudioLoader {

    AssetManager soundManager;
    AssetManager musicManager;

    String musicRootPath = "./Games/" + MenuScreen.GAME_NAME + "/Audio/Music/";
    String soundRootPath = "./Games/" + MenuScreen.GAME_NAME + "/Audio/Sound/";

    public AudioLoader() {
        soundManager = new AssetManager();
        musicManager = new AssetManager();
        loadAudio();
    }

    private void loadAudio() {
        try {
            String[] sounds = new File(soundRootPath).list();
            for (String path : sounds) {
                soundManager.load(soundRootPath + path, Sound.class);
            }
            soundManager.finishLoading();
        } catch (Exception ex) {
            System.out.println("Sound load err: " + ex.getMessage());
        }

        try {
            String[] musics = new File(musicRootPath).list();
            for (String path : musics) {
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
        Sound tmp = soundManager.get(soundRootPath + fileName + ".wav");
        tmp.play();
    }

    public void sound(String fileName, float vol, float pit, float pan) {
        //vol: range [0,1]
        //pit: 0.5 and 2.0
        //pan: panning in the range -1 (full left) to 1 (full right). 0 is center position.
        Sound tmp = soundManager.get(soundRootPath + fileName + ".wav");
        tmp.play(vol, pit, pan);
    }

    public void music(String fileName) {
        Music tmp = musicManager.get(musicRootPath + fileName + ".wav");
        tmp.play();
    }

    public void music(String fileName, boolean loop) {
        Music tmp = musicManager.get(musicRootPath + fileName + ".wav");
        tmp.setLooping(loop);
        tmp.play();
    }

    public void stopMusic() {
        Array<Music> allMusic = new Array<>();
        for (Music m : musicManager.getAll(Music.class, allMusic)) {
            m.stop();
        }
    }

    public void stopMusic(String fileName) {
        Music m = musicManager.get(musicRootPath + fileName + ".wav");
        m.stop();
    }

    public void disposeAudioLoader() {
        musicManager.dispose();
        soundManager.dispose();
    }

}
