/*
 * Copyright 2021 See AUTHORS file.
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
package leikr.transitions;

import leikr.GameRuntime;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.ExternalFileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.Transition;

/**
 * Trial class for implementing a custom Transition.
 * 
 * This is subject to change or removal in future releases pending any feedback.
 * I liked the idea of a graphical transition to selecting a game and into the 
 * loading screen. 
 *
 * @author tor
 */
public class EnterTransition implements Transition {

    float box;
    private final AssetManager assetManager;
    private final GameRuntime runtime;

    public EnterTransition(GameRuntime runtime) {
        super();
        this.runtime = runtime;
        this.assetManager = new AssetManager(new ExternalFileHandleResolver());
        box = 0;
        assetManager.load(runtime.getDataPath() + "Images/EnterCart.png", Texture.class);
        assetManager.finishLoading();
    }

    @Override
    public void initialise(GameScreen gs, GameScreen gs1) {
    }

    @Override
    public void update(GameContainer gc, float f) {
        box+=2;
    }

    @Override
    public void preRender(GameContainer gc, Graphics grphcs) {
    }

    @Override
    public void postRender(GameContainer gc, Graphics grphcs) {
        grphcs.drawTexture(assetManager.get(runtime.getDataPath()+"Images/EnterCart.png", Texture.class), 100, box);
    }

    @Override
    public boolean isFinished() {
        return box > 80;
    }

}
