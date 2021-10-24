/*
 * Copyright 2019 See AUTHORS file.
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

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.font.MonospaceGameFont.FontParameters;
import org.mini2Dx.core.graphics.Texture;

/**
 *
 * @author tor
 *
 * Font loading class
 */
public class FontLoader {

    private final MonospaceGameFont defaultFont;

    public FontLoader(AssetManager manager) {
        manager.load("./Data/Images/Fonts/ultra_compact_6x4.png", Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters(
			texturePath: "./Data/Images/Fonts/ultra_compact_6x4.png",
			spacing: 0,
			frameWidth: 4,
			frameHeight: 6
		)
        defaultFont = new MonospaceGameFont(params);
    }

    public MonospaceGameFont getDefaultFont() {
        return defaultFont;
    }

    public MonospaceGameFont getCustomFont(AssetManager manager, String fontPath, int spacing, int width, int height) {
        manager.load(fontPath, Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters(
			texturePath: fontPath,
			spacing: spacing,
			frameWidth: width,
			frameHeight: height
		)
        return new MonospaceGameFont(params);
    }
}
