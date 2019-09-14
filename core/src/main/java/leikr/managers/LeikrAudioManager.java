package leikr.managers;

import java.math.BigDecimal;
import leikr.loaders.AudioLoader;

/**
 *
 * @author tor
 */
public class LeikrAudioManager {

    private AudioLoader audioLoader;

    private static LeikrAudioManager instance;

    private LeikrAudioManager() {
        audioLoader = AudioLoader.getAudioLoader();
    }

    public static LeikrAudioManager getLeikrAudioManager() {
        if (instance == null) {
            instance = new LeikrAudioManager();
        }
        instance.resetLeikrAudioManager();
        return instance;
    }

    private void resetLeikrAudioManager() {
        audioLoader = AudioLoader.getAudioLoader();
    }

    //START loaded audio methods
    public final void playSound(String name) {
        audioLoader.playSound(name);
    }

    public final void playSound(String name, BigDecimal vol, BigDecimal pit, BigDecimal pan) {
        audioLoader.playSound(name, vol, pit, pan);
    }
    
    public final void stopSfx(){
        audioLoader.stopSound();
    }

    public final void playMusic(String name) {
        audioLoader.playMusic(name);
    }

    public final void playMusic(String name, boolean loop) {
        audioLoader.playMusic(name, loop);
    }

    public final void stopAllMusic() {
        audioLoader.stopMusic();
    }

    public final void stopMusic(String fileName) {
        audioLoader.stopMusic(fileName);
    }
    
    public void pauseAllAudio(){
        audioLoader.pauseAllAudio();
    }
    
    public void resumeAllAudio(){
        audioLoader.resumeAllAudio();
    }
    //END loaded audio methods

    //Engine methods
    public void dispose() {
        audioLoader.disposeAudioLoader();
    }

}
