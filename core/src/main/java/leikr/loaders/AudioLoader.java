package leikr.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import leikr.GameRuntime;

/**
 *
 * @author tor
 */
public class AudioLoader {

    AssetManager soundManager;
    AssetManager musicManager;

    String musicRootPath;
    String soundRootPath;

    Music mPlayer;
    Sound sPlayer;

    private static AudioLoader instance;

    private AudioLoader() {
        soundManager = new AssetManager();
        musicManager = new AssetManager();
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
        soundManager.clear();
        musicManager.clear();
        musicRootPath = GameRuntime.getProgramPath() + "/Audio/Music/";
        soundRootPath = GameRuntime.getProgramPath() + "/Audio/Sound/";
    }

    private void loadAudio() {
        try {
            for (FileHandle path : Gdx.files.local(soundRootPath).list()) {
                soundManager.load(soundRootPath + path.name(), Sound.class);
            }
            soundManager.finishLoading();
        } catch (Exception ex) {
            System.out.println("Sound load err: " + ex.getMessage());
        }

        try {
            for (FileHandle path : Gdx.files.local(musicRootPath).list()) {
                musicManager.load(musicRootPath + path.name(), Music.class);
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
        if (mPlayer != null) {
            mPlayer.dispose();
        }
        if (sPlayer != null) {
            sPlayer.dispose();
        }
        musicManager.clear();
        soundManager.clear();
    }

}
