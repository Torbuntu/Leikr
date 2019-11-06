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

import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.assets.loader.TextureLoader;
import org.mini2Dx.core.files.LocalFileHandleResolver;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;
import org.mini2Dx.core.graphics.Texture;

/**
 *
 * @author tor
 */
public class SpriteLoader {

    AssetManager assetManager;
    TextureLoader assetLoader;

    SpriteSheet spriteBank;
    SpriteSheet spriteBank16;
    SpriteSheet spriteBank32;
    SpriteSheet spriteBank64;

    String rootPath;

    private static SpriteLoader instance;

    private SpriteLoader() {
        assetManager = new AssetManager(new LocalFileHandleResolver());
    }

    public static SpriteLoader getSpriteLoader() {
        if (instance == null) {
            instance = new SpriteLoader();
        }
        if (Mdx.files.local(GameRuntime.getProgramPath() + "/Sprites/Sprites.png").exists()) {
            instance.resetSpriteLoader();
            instance.loadSpriteSheets();
            instance.addSpritesToSpriteBank();
        } else {
            System.out.println("No sprites found for: " + GameRuntime.getProgramPath());
        }

        return instance;
    }

    public void loadManualSpritesheets(String programName) {
        resetSpriteLoader();
        rootPath = "Programs/" + programName + "/Sprites/Sprites.png";
        if (!Mdx.files.local(rootPath).exists()) {
            System.out.println("No sprites found for: " + rootPath);
            return;
        }
        try {
            instance.loadSpriteSheets();
            instance.addSpritesToSpriteBank();
        } catch (Exception ex) {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Manual Sprite sheet not loadable for: " + rootPath);
        }
    }

    private void resetSpriteLoader() {
        disposeSprites();
        rootPath = GameRuntime.getProgramPath() + "/Sprites/Sprites.png";
        assetLoader = new TextureLoader();
        assetManager.setAssetLoader(Texture.class, assetLoader);
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
        assetManager.clearAssetLoaders();
    }

}
