/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        //This section is kind of hacky.
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
