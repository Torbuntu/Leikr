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

import leikr.GameRuntime
import leikr.loaders.EngineLoader
import leikr.managers.TerminalManager
import leikr.managers.TerminalManager.TerminalState

import org.mini2Dx.core.Files
import org.mini2Dx.core.Mdx
import org.mini2Dx.libgdx.LibgdxFiles

import spock.lang.Specification

/**
 *
 * @author tor
 */
class TerminalManagerSpec extends Specification {

    def setup(){
        Mdx.files = Spy(LibgdxFiles)
    }
    
    def "terminal manager initializes properly" () {
        given:
        def runtime = Mock(GameRuntime)
        def loader = Mock(EngineLoader)
        TerminalManager manager = new TerminalManager(runtime, loader)
        
        when:
        manager.init()
        
        then:
        2 * runtime.getGameName() >> "game"
        0 * _
        
        manager.getState() == TerminalState.PROCESSING
    }
}

