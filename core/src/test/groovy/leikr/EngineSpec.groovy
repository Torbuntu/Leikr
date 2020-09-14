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

package leikr

import leikr.controls.LeikrMouse
import leikr.managers.AudioManager
import leikr.managers.DataManager
import leikr.managers.GraphicsManager
import leikr.managers.InputManager
import leikr.managers.PixelManager
import leikr.managers.SystemManager
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.viewport.StretchViewport
import spock.lang.Specification

/**
 *
 * @author tor
 */
class EngineSpec extends Specification {
    
    AudioManager audioManager = Mock()
    DataManager dataManager = Mock()
    PixelManager pixelManager = Mock()
    GraphicsManager graphicsManager = Mock()
    SystemManager systemManager = Mock()
    InputManager inputManager = Mock()
    
    
    
    def "preCreate sets up the proper instances" () {
        given:
        def engine = (Engine)new TestEngine()
        def testPath = "test/game"
        def maxSprites = 100
        ManagerDTO managerDTO = Mock()
        def stretchViewport = new StretchViewport(240, 160)
        def fbo = Mock(FrameBuffer)
        
        def mouse = Mock(LeikrMouse)
        
        when:
        engine.preCreate(testPath, maxSprites, managerDTO, stretchViewport, fbo)
        
        then:
        1 * managerDTO.getAudioManager() >> audioManager
        1 * managerDTO.getGraphicsManager() >> graphicsManager
        1 * managerDTO.getDataManager() >> dataManager
        1 * managerDTO.getSystemManager() >> systemManager
        
        // mouse, keyboard, 2 controllers
        4 * managerDTO.getInputManager() >> inputManager
        
        1 * audioManager.resetAudioManager(testPath)
        1 * graphicsManager.resetScreenManager(testPath, maxSprites)
        1 * graphicsManager.preCreate(fbo, stretchViewport)
        with(inputManager){
            1 * getControllerA() >> _
            1 * getControllerB() >> _
            1 * getKeyboard() >> _
            1 * getMouse() >> mouse
        }
        
        1 * mouse.setViewport(stretchViewport)

        
        0 * _
        
        engine.getActive()
    }
}

class TestEngine extends Engine{
    
}