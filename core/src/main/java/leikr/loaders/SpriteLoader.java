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
import leikr.exceptions.RenderException;
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

    private String rootPath;

    private AssetManager assetManager;
    private TextureLoader assetLoader;

    private SpriteSheet spriteBank;
    private SpriteSheet spriteBank16;
    private SpriteSheet spriteBank32;
    private SpriteSheet spriteBank64;

    public SpriteLoader() {
    }

    public void resetSpriteLoader(String path) {
        rootPath = path + "/Sprites/Sprites.png";
        disposeSprites();

        assetLoader = new TextureLoader();
        assetManager = new AssetManager(new LocalFileHandleResolver());
        assetManager.setAssetLoader(Texture.class, assetLoader);
        System.out.println("Loader set for sprites");
        if (Mdx.files.local(path + "/Sprites/Sprites.png").exists()) {
            loadSpriteSheets();
            addSpritesToSpriteBank();
        } else {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.WARNING, "No sprites found for: {0}", path);
        }

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
            return (switch (size) {
                case 1 ->
                    spriteBank16;
                case 2 ->
                    spriteBank32;
                case 3 ->
                    spriteBank64;
                default ->
                    spriteBank;
            }).getSprite(id);
        } catch (Exception ex) {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RenderException("Error in program `render` method. Sprite index out of bounds. " + (ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ""));
        }
    }

    /*
    TODO: Look into custom exceptions. 
    public Sprite getSprite(int id, int size) {
        return convertException(() -> (switch (size) {
                case 1 -> spriteBank16;
                case 2 -> spriteBank32;
                case 3 -> spriteBank64;
                default -> spriteBank;
            }).getSprite(id), RenderException::new);
    }
}
    
     */
    public void loadManualSpritesheets(String programName) {
//        resetSpriteLoader();
        rootPath = "Programs/" + programName + "/Sprites/Sprites.png";
        if (!Mdx.files.local(rootPath).exists()) {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.WARNING, "No sprites found for: {0}", rootPath);
            return;
        }
        try {
            loadSpriteSheets();
            addSpritesToSpriteBank();
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
