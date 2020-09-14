/*
 * Copyright 2020 tor.
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

package leikr.managers

import leikr.loaders.AudioLoader
import org.mini2Dx.core.Files
import org.mini2Dx.core.Mdx
import org.mini2Dx.libgdx.LibgdxFiles
import spock.lang.Specification

/**
 *
 * @author tor
 */
class AudioManagerSpec extends Specification{
    def loader
    def setup(){
        Mdx.files = Spy(LibgdxFiles)
        loader = Spy(AudioLoader)
    }
    
    def "audioManager resets audioLoader" () {
        given:
        AudioManager manager = new AudioManager(loader)
        
        when:
        manager.resetAudioManager("Programs/AudioDemo")
        
        then:
        1 * loader.resetAudioLoader("Programs/AudioDemo")
        1 * Mdx.files.local("Programs/AudioDemo/Audio/Sound/")
        1 * Mdx.files.local("Programs/AudioDemo/Audio/Music/")
        
        0 * _
    }
    
}

