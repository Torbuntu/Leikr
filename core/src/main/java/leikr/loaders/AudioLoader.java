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
package leikr.loaders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.files.LocalFileHandleResolver;

/**
 *
 * @author tor
 */
public class AudioLoader {

    private String musicRootPath;
    private String soundRootPath;

    private AssetManager assetManager;

    private Music mPlayer;
    private Sound sPlayer;

    public AudioLoader() {
    }

    public void resetAudioLoader(String path) {
        assetManager = new AssetManager(new LocalFileHandleResolver());
        musicRootPath = path + "/Audio/Music/";
        soundRootPath = path + "/Audio/Sound/";

        loadAudio();
    }

    private void loadAudio() {
        try {
            Arrays.asList(Mdx.files.local(soundRootPath).list()).stream()
                    .filter(file -> !file.isDirectory()
                    && (file.extension().equalsIgnoreCase("wav")
                    || file.extension().equalsIgnoreCase("mp3")
                    || file.extension().equalsIgnoreCase("ogg")))
                    .forEach(f -> assetManager.load(soundRootPath + f.name(), Sound.class));
            assetManager.finishLoading();
        } catch (Exception ex) {
            Logger.getLogger(AudioLoader.class.getName()).log(Level.WARNING, "Sound load error: {0}", ex.getMessage());
        }

        try {
            Arrays.asList(Mdx.files.local(musicRootPath).list()).stream()
                    .filter(file -> !file.isDirectory()
                    && (file.extension().equalsIgnoreCase("wav")
                    || file.extension().equalsIgnoreCase("mp3")
                    || file.extension().equalsIgnoreCase("ogg")))
                    .forEach(f -> assetManager.load(musicRootPath + f.name(), Music.class));
            assetManager.finishLoading();
        } catch (Exception ex) {
            Logger.getLogger(AudioLoader.class.getName()).log(Level.WARNING, "Music load error: {0}", ex.getMessage());
        }
    }

    //probably useless, but makes me feel safe. 
    public void load() {
        assetManager.finishLoading();
    }

    public void playSound(String fileName) {
        sPlayer = assetManager.get(soundRootPath + fileName, Sound.class);
        sPlayer.play();
    }

    public void playSound(String fileName, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        //vol: range [0,1]
        //pit: 0.5 and 2.0
        //pan: panning in the range -1 (full left) to 1 (full right). 0 is center position.
        sPlayer = assetManager.get(soundRootPath + fileName, Sound.class);
        sPlayer.play(vol.floatValue(), pit.floatValue(), pan.floatValue());
    }

    public void playMusic(String fileName) {
        mPlayer = assetManager.get(musicRootPath + fileName, Music.class);
        mPlayer.play();
    }

    public void playMusic(String fileName, boolean loop) {
        mPlayer = assetManager.get(musicRootPath + fileName, Music.class);
        mPlayer.setLooping(loop);
        mPlayer.play();
    }

    public void stopMusic() {
        mPlayer.stop();
    }

    public void stopSound() {
        sPlayer.stop();
    }

    public void stopMusic(String fileName) {
        mPlayer = assetManager.get(musicRootPath + fileName, Music.class);
        mPlayer.stop();
    }

    public void pauseAudio() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
        if (sPlayer != null) {
            sPlayer.pause();
        }
    }

    public void resumeAudio() {
        if (mPlayer != null) {
            mPlayer.play();
        }
    }

    public void disposeAudioLoader() {
        if (null != assetManager) {
            if (sPlayer != null) {
                sPlayer.stop();
            }
            if (mPlayer != null) {
                mPlayer.stop();
            }
            assetManager.clearAssetLoaders();
            assetManager.dispose();
        }
    }

}
