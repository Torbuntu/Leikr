/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import leikr.MenuScreen;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;
import org.mini2Dx.core.graphics.TextureRegion;

/**
 *
 * @author tor
 */
public class SpriteLoader {

    AssetManager assetManager;
//    ArrayList<Sprite> spriteBank;
//    ArrayList<Sprite> spriteBank16;
//    ArrayList<Sprite> spriteBank32;
//    ArrayList<Sprite> spriteBank64;
    
    SpriteSheet spriteBank;
    SpriteSheet spriteBank16;
    SpriteSheet spriteBank32;
    SpriteSheet spriteBank64;

    Array<Texture> allSheets;
    
    String rootPath;
    String[] sheets;

    public SpriteLoader(int numberSpriteSheets) {
        assetManager = new AssetManager();
//        spriteBank = new ArrayList<>();
//        spriteBank16 = new ArrayList<>();
//        spriteBank32 = new ArrayList<>();
//        spriteBank64 = new ArrayList<>();
//        allSheets = new Array<>();

        rootPath = "./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites.png";
        
        loadSpriteSheets();

        //This section is kind of hacky.
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
//        sheets = new File(rootPath).list();
//        Arrays.sort(sheets);
//        for (String path : sheets) {
            assetManager.load(rootPath, Texture.class);
//        }        
        assetManager.finishLoading();
    }

    private void addSpritesToSpriteBank() {
        spriteBank = new SpriteSheet(assetManager.get(rootPath, Texture.class), 8,8);
        spriteBank16 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 16,16);
        spriteBank32 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 32,32);
        spriteBank64 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 64,64);
//        
//        allSheets.clear();
//        for (Texture tex : assetManager.getAll(Texture.class, allSheets)) {            
//            for (int i = 0; i < 128; i += 8) {
//                for (int j = 0; j < 128; j += 8) {
//                    spriteBank.add(new Sprite(new TextureRegion(tex, j, i, 8, 8)));
//                }
//            }
//            for (int i = 0; i < 128; i += 16) {
//                for (int j = 0; j < 128; j += 16) {
//                    spriteBank16.add(new Sprite(new TextureRegion(tex, j, i, 16, 16)));
//                }
//            }
//            for (int i = 0; i < 128; i += 32) {
//                for (int j = 0; j < 128; j += 32) {
//                    spriteBank32.add(new Sprite(new TextureRegion(tex, j, i, 32, 32)));
//                }
//            }
//            for (int i = 0; i < 128; i += 64) {
//                for (int j = 0; j < 128; j += 64) {
//                    spriteBank64.add(new Sprite(new TextureRegion(tex, j, i, 64, 64)));
//                }
//            }
//        }
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
    
    public void disposeSprites(){
        assetManager.dispose();
    }

}
