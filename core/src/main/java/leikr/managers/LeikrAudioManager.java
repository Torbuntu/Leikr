package leikr.managers;

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
    public final void sfx(String name) {
        audioLoader.sound(name);
    }

    public final void sfx(String name, float vol, float pit, float pan) {
        audioLoader.sound(name, vol, pit, pan);
    }
    
    public final void stopSfx(){
        audioLoader.stopSound();
    }

    public final void music(String name) {
        audioLoader.music(name);
    }

    public final void music(String name, boolean loop) {
        audioLoader.music(name, loop);
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
