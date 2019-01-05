/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
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

    String sheet1;
    String sheet2;
    String sheet3;
    String sheet4;

    SpriteLoader() {
        assetManager = new AssetManager();
        spriteBank = new ArrayList<>();

        sheet1 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png";
        sheet2 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png";
        sheet3 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_2.png";
        sheet4 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_3.png";

        loadSpriteSheets();
        addSpritesToSpriteBank(sheet1);
        addSpritesToSpriteBank(sheet2);
        addSpritesToSpriteBank(sheet3);
        addSpritesToSpriteBank(sheet4);
    }

    private void loadSpriteSheets() {
        assetManager.load(sheet1, Texture.class);
        assetManager.load(sheet2, Texture.class);
        assetManager.load(sheet3, Texture.class);
        assetManager.load(sheet4, Texture.class);

        assetManager.finishLoading();
    }

    private void addSpritesToSpriteBank(String sheet) {

        for (int i = 0; i < 128; i += 8) {
            for (int j = 0; j < 128; j += 8) {
                spriteBank.add(new Sprite(new TextureRegion(assetManager.get(sheet, Texture.class), j, i, 8, 8)));
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
