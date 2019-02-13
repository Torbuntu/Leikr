/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.font.MonospaceFont;

/**
 *
 * @author tor
 * 
 * Super Temporary font loading class
 */
public class FontLoader {
    public MonospaceFont getFont(AssetManager manager){
        manager.load("./Data/LeikrFontA.png", Texture.class);
        manager.finishLoading();
        MonospaceFont.FontParameters params = new MonospaceFont.FontParameters();
        params.texturePath = "./Data/LeikrFontA.png";
        params.spacing = 0;
        params.frameWidth = 8;
        params.frameHeight = 8;
        return new MonospaceFont(params);
    }
}
