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
import leikr.screens.EngineScreen;
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

    }

    public static SpriteLoader getSpriteLoader() {
        if (instance == null) {
            instance = new SpriteLoader();
        }
        if (Mdx.files.local(GameRuntime.getProgramPath() + "/Sprites/Sprites.png").exists()) {
            instance.disposeSprites();

            instance.resetSpriteLoader();
            instance.loadSpriteSheets();
            instance.addSpritesToSpriteBank();
        } else {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.WARNING, "No sprites found for: {0}", GameRuntime.getProgramPath());
        }

        return instance;
    }

    private void resetSpriteLoader() {
        rootPath = GameRuntime.getProgramPath() + "/Sprites/Sprites.png";
        assetLoader = new TextureLoader();
        assetManager = new AssetManager(new LocalFileHandleResolver());
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

    /**
     * Get a sprite from one of the 4 banks.
     *
     * If the id is outside of the index range of a bank, the error screen will
     * be invoked with the message in the catch.
     *
     * If a localized message exists, it will append it.
     *
     * The first sprite in default bank is returned to prevent followup errors
     * in the EngineScreen render method.
     *
     * @param id The id of the sprite to return
     * @param size Which bank size to index
     * @return The sprite given the ID and size
     */
    public Sprite getSprite(int id, int size) {
        try {
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
        } catch (Exception ex) {
            EngineScreen.errorEngine("Error in program `render` method. Sprite index out of bounds. " + (ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ""));
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, null, ex);
            return spriteBank.getSprite(0);
        }
    }

    public void loadManualSpritesheets(String programName) {
        resetSpriteLoader();
        rootPath = "Programs/" + programName + "/Sprites/Sprites.png";
        if (!Mdx.files.local(rootPath).exists()) {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.WARNING, "No sprites found for: {0}", rootPath);
            return;
        }
        try {
            instance.loadSpriteSheets();
            instance.addSpritesToSpriteBank();
        } catch (Exception ex) {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, "Manual Sprite sheet not loadable for: {0}", rootPath);
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disposeSprites() {
        if (null != assetManager) {
            assetManager.clearAssetLoaders();
            assetManager.dispose();
        }
    }

}
