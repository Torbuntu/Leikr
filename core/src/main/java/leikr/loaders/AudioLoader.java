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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.assets.loader.MusicLoader;
import org.mini2Dx.core.assets.loader.SoundLoader;
import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.files.LocalFileHandleResolver;

/**
 *
 * @author tor
 */
public class AudioLoader {

    AssetManager soundManager;
    AssetManager musicManager;

    SoundLoader soundLoader;
    MusicLoader musicLoader;

    String musicRootPath;
    String soundRootPath;

    Music mPlayer;
    Sound sPlayer;

    private static AudioLoader instance;

    private AudioLoader() {
    }

    public static AudioLoader getAudioLoader() {
        if (instance == null) {
            instance = new AudioLoader();
        }
        instance.disposeAudioLoader();
        instance.resetAudioLoader();
        instance.loadAudio();
        return instance;
    }

    private void resetAudioLoader() {
        soundLoader = new SoundLoader();
        soundManager = new AssetManager(new LocalFileHandleResolver());
        soundManager.setAssetLoader(Sound.class, soundLoader);

        musicLoader = new MusicLoader();
        musicManager = new AssetManager(new LocalFileHandleResolver());
        musicManager.setAssetLoader(Music.class, musicLoader);
        musicRootPath = GameRuntime.getProgramPath() + "/Audio/Music/";
        soundRootPath = GameRuntime.getProgramPath() + "/Audio/Sound/";
    }

    private void loadAudio() {
        try {
            Arrays.asList(Mdx.files.local(soundRootPath).list()).stream()
                    .filter(file -> !file.isDirectory() && file.extension().equalsIgnoreCase("wav"))
                    .forEach(f -> soundManager.load(soundRootPath + f.name(), Sound.class));
            soundManager.finishLoading();
        } catch (IOException ex) {
            System.out.println("Sound load err: " + ex.getMessage());
        }

        try {
            Arrays.asList(Mdx.files.local(musicRootPath).list()).stream()
                    .filter(file -> !file.isDirectory() && file.extension().equalsIgnoreCase("wav"))
                    .forEach(f -> musicManager.load(musicRootPath + f.name(), Music.class));
            musicManager.finishLoading();
        } catch (IOException ex) {
            System.out.println("Music load err: " + ex.getMessage());
        }
    }

    //probably useless, but makes me feel safe. 
    public void load() {
        soundManager.finishLoading();
        musicManager.finishLoading();
    }

    public void playSound(String fileName) {
        sPlayer = soundManager.get(soundRootPath + fileName + ".wav", Sound.class);
        sPlayer.play();
    }

    public void playSound(String fileName, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        //vol: range [0,1]
        //pit: 0.5 and 2.0
        //pan: panning in the range -1 (full left) to 1 (full right). 0 is center position.
        sPlayer = soundManager.get(soundRootPath + fileName + ".wav", Sound.class);
        sPlayer.play(vol.floatValue(), pit.floatValue(), pan.floatValue());
    }

    public void playMusic(String fileName) {
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
        mPlayer.play();
    }

    public void playMusic(String fileName, boolean loop) {
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
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
        mPlayer = musicManager.get(musicRootPath + fileName + ".wav", Music.class);
        mPlayer.stop();
    }

    public void pauseAllAudio() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
        if (sPlayer != null) {
            sPlayer.pause();
        }
    }

    public void resumeAllAudio() {
        if (mPlayer != null) {
            mPlayer.play();
        }
    }

    public void disposeAudioLoader() {
        if (null != soundManager) {
            sPlayer.stop();
            soundManager.clearAssetLoaders();
            soundManager.dispose();
        }
        if (null != musicManager) {
            mPlayer.stop();
            musicManager.clearAssetLoaders();
            musicManager.dispose();
        }
    }

}
