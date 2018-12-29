/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import org.mini2Dx.core.graphics.Sprite;
//import org.mini2Dx.core.graphics.TextureRegion;

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
        splitSprites();
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
        assetManager.load("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png", Texture.class);
        assetManager.load("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png", Texture.class);
        assetManager.finishLoading();
    }

    private void splitSprites() {
        regions_0 = TextureRegion.split(assetManager.get("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png", Texture.class), 8, 8);
        regions_1 = TextureRegion.split(assetManager.get("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png", Texture.class), 8, 8);
    }

    private void addSpritesToSpriteBank() {
        for (TextureRegion[] regionC : regions_0) {
            for (TextureRegion regionR : regionC) {
                spriteBank.add(new Sprite(regionR.getTexture()));                
            }
        }
        for (TextureRegion[] regionC : regions_1) {
            for (TextureRegion regionR : regionC) {
                spriteBank.add(new Sprite(regionR.getTexture()));                
            }
        }
    }
    
    public ArrayList getSpriteBank(){
        return spriteBank;
    }
    
    public void clearSpriteData(){
        assetManager.dispose();
    }
    

}
