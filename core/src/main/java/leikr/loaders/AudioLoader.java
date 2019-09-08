package leikr.loaders;

import java.io.IOException;
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
        soundManager = new AssetManager(new LocalFileHandleResolver());
        soundLoader = new SoundLoader();
        soundManager.setAssetLoader(Sound.class, soundLoader);

        musicManager = new AssetManager(new LocalFileHandleResolver());
        musicLoader = new MusicLoader();
        musicManager.setAssetLoader(Music.class, musicLoader);
    }

    public static AudioLoader getAudioLoader() {
        if (instance == null) {
            instance = new AudioLoader();
        }
        instance.resetAudioLoader();
        instance.loadAudio();
        return instance;
    }

    private void resetAudioLoader() {
        if (soundManager != null) {
            soundManager.clearAssetLoaders();
        }

        if (musicManager != null) {
            musicManager.clearAssetLoaders();
        }

        soundLoader = new SoundLoader();
        soundManager.setAssetLoader(Sound.class, soundLoader);

        musicLoader = new MusicLoader();
        musicManager.setAssetLoader(Music.class, musicLoader);
        musicRootPath = GameRuntime.getProgramPath() + "/Audio/Music/";
        soundRootPath = GameRuntime.getProgramPath() + "/Audio/Sound/";
    }

    private void loadAudio() {
        try {
            Arrays.asList(Mdx.files.local(soundRootPath).list()).stream()
                    .filter(file -> !file.isDirectory())
                    .forEach(f -> soundManager.load(soundRootPath + f.name(), Sound.class));
            soundManager.finishLoading();
        } catch (IOException ex) {
            System.out.println("Sound load err: " + ex.getMessage());
        }

        try {
            Arrays.asList(Mdx.files.local(musicRootPath).list()).stream()
                    .filter(file -> !file.isDirectory())
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
        if (sPlayer != null) {
            sPlayer.play();
        }
    }

    public void disposeAudioLoader() {
        if (mPlayer != null) {
            mPlayer.dispose();
        }
        if (sPlayer != null) {
            sPlayer.dispose();
        }
        musicManager.clearAssetLoaders();
        soundManager.clearAssetLoaders();
    }

}
