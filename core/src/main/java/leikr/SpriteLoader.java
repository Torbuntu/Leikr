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
    Texture sheet_0;
    Texture sheet_1;

    SpriteLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
        spriteBank = new ArrayList<>();
        loadSpriteSheets();
        splitSprites();
        addSpritesToSpriteBank();
    }

    private void loadSpriteSheets() {
        sheet_0 = new Texture("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_0.png");
        sheet_1 = new Texture("./Games/" + MenuScreen.GAME_NAME + "/Sprites/Sprites_1.png");
    }

    private void splitSprites() {
        regions_0 = (TextureRegion[][]) TextureRegion.split(sheet_0, 8, 8);
        regions_1 = (TextureRegion[][]) TextureRegion.split(sheet_1, 8, 8);
    }

    private void addSpritesToSpriteBank() {
        for (TextureRegion[] regionC : regions_0) {
            for (TextureRegion regionR : regionC) {
                spriteBank.add(new Sprite(regionR));                
            }
        }
        for (TextureRegion[] regionC : regions_1) {
            for (TextureRegion regionR : regionC) {
                spriteBank.add(new Sprite(regionR));                
            }
        }
    }
    
    public ArrayList getSpriteBank(){
        return spriteBank;
    }
    

}
