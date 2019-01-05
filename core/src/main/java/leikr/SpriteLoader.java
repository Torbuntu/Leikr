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
    ArrayList<Sprite> spriteBank;
    ArrayList<Sprite> mediumSpriteBank;
    ArrayList<Sprite> largeSpriteBank;

    String sheet1;
    String sheet2;
    String sheet3;
    String sheet4;

    SpriteLoader() {
        assetManager = new AssetManager();
        spriteBank = new ArrayList<>();
        mediumSpriteBank = new ArrayList<>();
        largeSpriteBank = new ArrayList<>();
        
        sheet1 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png";
        sheet2 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png";
        sheet3 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_2.png";
        sheet4 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_3.png";

        loadSpriteSheets();

        //This section is kind of hacky.
        addSpritesToSpriteBank(sheet1);
        addSpritesToSpriteBank(sheet2);
        addSpritesToSpriteBank(sheet3);
        addSpritesToSpriteBank(sheet4);

        addMediumSpritesToSpriteBank(sheet1);
        addMediumSpritesToSpriteBank(sheet2);
        addMediumSpritesToSpriteBank(sheet3);
        addMediumSpritesToSpriteBank(sheet4);

        addLargeSpritesToSpriteBank(sheet1);
        addLargeSpritesToSpriteBank(sheet2);
        addLargeSpritesToSpriteBank(sheet3);
        addLargeSpritesToSpriteBank(sheet4);
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

    private void addMediumSpritesToSpriteBank(String sheet) {

        for (int i = 0; i < 128; i += 16) {
            for (int j = 0; j < 128; j += 16) {
                mediumSpriteBank.add(new Sprite(new TextureRegion(assetManager.get(sheet, Texture.class), j, i, 16, 16)));
            }
        }
    }

    private void addLargeSpritesToSpriteBank(String sheet) {

        for (int i = 0; i < 128; i += 64) {
            for (int j = 0; j < 128; j += 64) {
                largeSpriteBank.add(new Sprite(new TextureRegion(assetManager.get(sheet, Texture.class), j, i, 64, 64)));
            }
        }
    }

    public ArrayList getSpriteBank() {
        return spriteBank;
    }

    public ArrayList getMediumSpriteBank() {
        return mediumSpriteBank;
    }

    public ArrayList getLargeSpriteBank() {
        return largeSpriteBank;
    }

    public void clearSpriteData() {
        assetManager.dispose();
    }

}
