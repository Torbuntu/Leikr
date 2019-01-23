/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
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
    ArrayList<Sprite> bigSpriteBank;
    ArrayList<Sprite> largeSpriteBank;

    Array<Texture> allSheets;

    String sheet1;
    String sheet2;
    String sheet3;
    String sheet4;

    SpriteLoader() {
        assetManager = new AssetManager();
        spriteBank = new ArrayList<>();
        mediumSpriteBank = new ArrayList<>();
        bigSpriteBank = new ArrayList<>();
        largeSpriteBank = new ArrayList<>();
        allSheets = new Array<>();

        sheet1 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png";
        sheet2 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png";
        sheet3 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_2.png";
        sheet4 = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_3.png";
        
        loadSpriteSheets();

        //This section is kind of hacky.
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
        assetManager.load(sheet1, Texture.class);
        assetManager.load(sheet2, Texture.class);
        assetManager.load(sheet3, Texture.class);
        assetManager.load(sheet4, Texture.class);

        assetManager.finishLoading();
    }

    private void addSpritesToSpriteBank() {
        allSheets.clear();
        for (Texture tex : assetManager.getAll(Texture.class, allSheets)) {
            for (int i = 0; i < 128; i += 8) {
                for (int j = 0; j < 128; j += 8) {
                    spriteBank.add(new Sprite(new TextureRegion(tex, j, i, 8, 8)));
                }
            }
            for (int i = 0; i < 128; i += 16) {
                for (int j = 0; j < 128; j += 16) {
                    mediumSpriteBank.add(new Sprite(new TextureRegion(tex, j, i, 16, 16)));
                }
            }
            for (int i = 0; i < 128; i += 32) {
                for (int j = 0; j < 128; j += 32) {
                    bigSpriteBank.add(new Sprite(new TextureRegion(tex, j, i, 32, 32)));
                }
            }
            for (int i = 0; i < 128; i += 64) {
                for (int j = 0; j < 128; j += 64) {
                    largeSpriteBank.add(new Sprite(new TextureRegion(tex, j, i, 64, 64)));
                }
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

    Sprite getSprite(int id, int size) {
        switch (size) {
            case 0:
            default:
                return spriteBank.get(id);
            case 1:
                return mediumSpriteBank.get(id);
            case 2:
                return bigSpriteBank.get(id);
            case 3:
                return largeSpriteBank.get(id);
        }
    }

}
