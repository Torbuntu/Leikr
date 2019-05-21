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
package leikr.managers;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 *
 * @author tor
 *
 * This class manages the Java Midi components for Leikr's Audio engine
 */
public class LeikrMidiManager {

    Synthesizer synth;
    MidiChannel[] channels;
    Sequencer sequencer;

    public LeikrMidiManager() {
        try {
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        initializeSynthesizer();

        //Demo code
        channels[0].programChange(synth.getLoadedInstruments()[0].getPatch().getProgram());
        playChannel(
                0,
                new int[]{0, 60, 40, 50, 60},
                new int[]{0, 100, 100, 100, 100},
                new int[]{0, 500, 500, 500, 500}
        );
        //END Demo code
    }

    private void initializeSynthesizer() {
        try {
            if (synth != null && synth.isOpen()) {
                synth.close();
            }
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());
            synth.loadAllInstruments(MidiSystem.getSoundbank(new File("Data/Audio/Famicom.sf2")));
            channels = synth.getChannels();
        } catch (Exception ex) {
            Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void initializeSynthesizer(String soundBank) {
        try {
            synth.close();
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());
            synth.loadAllInstruments(MidiSystem.getSoundbank(new File(soundBank)));
            channels = synth.getChannels();
        } catch (Exception ex) {
            Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void playChannel(int channel, int[] notes, int[] velocities, int[] durations) {
        if (synth.isOpen()) {
            for (int i = 0; i < notes.length; i++) {
                channels[channel].noteOn(notes[i], velocities[i]);
                try {
                    Thread.sleep(durations[i]);
                    channels[channel].allNotesOff();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        channels[channel].allNotesOff();
    }
}

/*
try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.unloadAllInstruments(synth.getDefaultSoundbank());
            synth.loadAllInstruments(MidiSystem.getSoundbank(new File("Data/Audio/Famicom.sf2")));
            channels = synth.getChannels();
            for (Instrument in : synth.getLoadedInstruments()) {
                System.out.println("Playing: " + in.getName() + ", ID: " + in.getPatch().getProgram());
                channels[0].programChange(0, in.getPatch().getProgram());
                channels[0].noteOn(40, 100);
                Thread.sleep(500);
                channels[0].allSoundOff();
            }
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException | InterruptedException ex) {
            Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
 */
