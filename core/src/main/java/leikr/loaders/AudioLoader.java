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
        } catch (Exception ex) {
            Logger.getLogger(AudioLoader.class.getName()).log(Level.WARNING, "Music load error: {0}", ex.getMessage());
        }
        load();
    }

    private void load() {
        assetManager.finishLoading();
    }

    /**
     * Returns the Sound object by name.
     *
     * Getting the Sound object itself is useful for having more control over
     * the Sound object in the game code.
     *
     * @param fileName the name of the Sound file to get
     * @return the Sound object
     */
    public Sound getSound(String fileName) {
        return assetManager.get(soundRootPath + fileName, Sound.class);
    }

    public void playSound(String fileName) {
        assetManager.get(soundRootPath + fileName, Sound.class).play();
    }

    /**
     * Plays a sound with the given parameters applied.
     *
     * @param fileName the name of the sound file to play, including the
     * extension (wav, ogg, mp3)
     * @param volume the volume to play this sound at, range 0 to 1
     * @param pitch the pitch shift value, between 0.5 and 2.0
     * @param pan the left/right pan value, between -1.0 and 1.0
     */
    public void playSound(String fileName, BigDecimal volume, BigDecimal pitch, BigDecimal pan) {
        assetManager.get(soundRootPath + fileName, Sound.class).play(volume.floatValue(), pitch.floatValue(), pan.floatValue());
    }

    /**
     * Stops all Sounds from playing.
     */
    public void stopSound() {
        for (Sound s : assetManager.getAll(Sound.class).values()) {
            s.stop();
        }
    }

    public void stopSound(String fileName) {
        assetManager.get(soundRootPath + fileName, Sound.class).stop();
    }

    /**
     * Returns the Music object by name.
     *
     * Useful for having more manual control of the Music object in game code.
     *
     * @param fileName the name of the Music file.
     * @return the Music object
     */
    public Music getMusic(String fileName) {
        return assetManager.get(musicRootPath + fileName, Music.class);
    }

    public void playMusic(String fileName) {
        assetManager.get(musicRootPath + fileName, Music.class).play();
    }

    public void playMusic(String fileName, boolean loop) {
        Music mPlayer = assetManager.get(musicRootPath + fileName, Music.class);
        mPlayer.setLooping(loop);
        mPlayer.play();
    }

    public void stopMusic() {
        for (Music m : assetManager.getAll(Music.class).values()) {
            m.stop();
        }
    }

    public void stopMusic(String fileName) {
        assetManager.get(musicRootPath + fileName, Music.class).stop();
    }

    /**
     * Pauses all audio from playing.
     *
     */
    public void pauseAudio() {
        for (Music m : assetManager.getAll(Music.class).values()) {
            m.pause();
        }
        for (Sound s : assetManager.getAll(Sound.class).values()) {
            s.pause();
        }
    }

    /**
     * Resumes the last playing song.
     *
     * Sounds are not currently resumed.
     */
    public void resumeAudio() {
        for (Music m : assetManager.getAll(Music.class).values()) {
            m.play();
        }
        for (Sound s : assetManager.getAll(Sound.class).values()) {
            s.resume();
        }
    }

    /**
     * Stops all audio from playing and disposes the assets.
     */
    public void disposeAudioLoader() {
        if (null != assetManager) {
            for (Sound s : assetManager.getAll(Sound.class).values()) {
                s.stop();
            }
            for (Music m : assetManager.getAll(Music.class).values()) {
                m.stop();
            }
            assetManager.clearAssetLoaders();
            assetManager.dispose();
        }
    }

}
