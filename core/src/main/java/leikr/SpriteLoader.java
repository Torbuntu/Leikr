/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureRegion;

/**
 *
 * @author tor
 */
public class SpriteLoader {

    AssetManager assetManager;
    TextureRegion[][] regions_0;
    TextureRegion[][] regions_1;
    ArrayList<Sprite> spriteBank;
    
    SpriteLoader() {
        assetManager = new AssetManager();
        spriteBank = new ArrayList<>();
        loadSpriteSheets();
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
        assetManager.load("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png", Texture.class);
        assetManager.load("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png", Texture.class);
        assetManager.finishLoading();
    }

    private void addSpritesToSpriteBank() {

        for (int i = 0; i < 128; i+=8) {
            for (int j = 0; j < 128; j+=8) {
                spriteBank.add(new Sprite(new TextureRegion(assetManager.get("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png", Texture.class), j, i, 8, 8)));
            }
        }
        for (int i = 0; i < 128; i+=8) {
            for (int j = 0; j < 128; j+=8) {
                spriteBank.add(new Sprite(new TextureRegion(assetManager.get("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png", Texture.class), j, i, 8, 8)));
            }
        }
    }

    public ArrayList getSpriteBank() {
        return spriteBank;
    }

    public void clearSpriteData() {
        assetManager.dispose();
    }

}
