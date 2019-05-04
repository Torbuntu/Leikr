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
package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.font.MonospaceFont;

/**
 *
 * @author tor
 * 
 * Super Temporary font loading class
 */
public class FontLoader {
    public MonospaceFont getFont(AssetManager manager){
        manager.load("./Data/Images/LeikrFontA.png", Texture.class);
        manager.finishLoading();
        MonospaceFont.FontParameters params = new MonospaceFont.FontParameters();
        params.texturePath = "./Data/Images/LeikrFontA.png";
        params.spacing = 0;
        params.frameWidth = 8;
        params.frameHeight = 8;
        return new MonospaceFont(params);
    }
}
