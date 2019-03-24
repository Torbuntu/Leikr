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
import leikr.GameRuntime;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;

/**
 *
 * @author tor
 */
public class SpriteLoader {

    AssetManager assetManager;

    SpriteSheet spriteBank;
    SpriteSheet spriteBank16;
    SpriteSheet spriteBank32;
    SpriteSheet spriteBank64;

    String rootPath;

    public SpriteLoader() {
        assetManager = new AssetManager();

        rootPath = GameRuntime.getGamePath() + "/Sprites/Sprites.png";

        loadSpriteSheets();
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
        assetManager.load(rootPath, Texture.class);
        assetManager.finishLoading();
    }

    private void addSpritesToSpriteBank() {
        spriteBank = new SpriteSheet(assetManager.get(rootPath, Texture.class), 8, 8);
        spriteBank16 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 16, 16);
        spriteBank32 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 32, 32);
        spriteBank64 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 64, 64);
    }

    public Sprite getSprite(int id, int size) {
        switch (size) {
            case 0:
            default:
                return spriteBank.getSprite(id);
            case 1:
                return spriteBank16.getSprite(id);
            case 2:
                return spriteBank32.getSprite(id);
            case 3:
                return spriteBank64.getSprite(id);
        }
    }

    public void disposeSprites() {
        assetManager.dispose();
    }

}
