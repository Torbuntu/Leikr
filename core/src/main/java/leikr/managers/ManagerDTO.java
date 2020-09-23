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
package leikr.managers;

import leikr.managers.AudioManager;
import leikr.managers.DataManager;
import leikr.managers.GraphicsManager;
import leikr.managers.InputManager;
import leikr.managers.PixelManager;
import leikr.managers.SystemManager;

/**
 *
 * @author tor
 */
public class ManagerDTO {

    private final AudioManager audioManager;
    private final DataManager dataManager;
    private final PixelManager pixelManager;
    private final GraphicsManager graphicsManager;
    private final SystemManager systemManager;
    private final InputManager inputManager;

    public ManagerDTO(AudioManager audioManager, DataManager dataManager, PixelManager pixelManager, GraphicsManager graphicsManager, InputManager inputManager, SystemManager systemManager) {
        this.audioManager = audioManager;
        this.dataManager = dataManager;
        this.pixelManager = pixelManager;
        this.graphicsManager = graphicsManager;
        this.inputManager = inputManager;
        this.systemManager = systemManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public PixelManager getPixelManager() {
        return pixelManager;
    }

    public GraphicsManager getGraphicsManager() {
        return graphicsManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public SystemManager getSystemManager() {
        return systemManager;
    }
}
