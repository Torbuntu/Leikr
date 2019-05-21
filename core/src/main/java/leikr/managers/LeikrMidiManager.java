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

import com.sun.media.sound.SF2SoundbankReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

/**
 *
 * @author tor
 *
 * This class manages the Java Midi components for Leikr's Audio engine
 */
public class LeikrMidiManager {

    Synthesizer synth;

    public LeikrMidiManager() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            Soundbank sb = MidiSystem.getSoundbank(new File("Data/Audio/Famicom.sf2"));
            for(Instrument i : sb.getInstruments()){
                System.out.println(i.getName());
            }
            synth.loadAllInstruments(sb);
            MidiChannel[] chan = synth.getChannels();
            chan[0].allNotesOff();
            chan[0].noteOn(400, 10);
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException ex) {
            Logger.getLogger(LeikrMidiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
