/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import org.mini2Dx.core.font.MonospaceFont;

/**
 *
 * @author tor
 * 
 * Super Temporary font loading class
 */
public class FontLoader {

    MonospaceFont font;
    public FontLoader() {
    }

    public MonospaceFont getFont(AssetManager manager){
        manager.load("./LeikrFontA.png", Texture.class);
        manager.finishLoading();
        MonospaceFont.FontParameters params = new MonospaceFont.FontParameters();
        params.texturePath = "./LeikrFontA.png";
        params.spacing = 0;
        params.frameWidth = 8;
        params.frameHeight = 8;
        font = new MonospaceFont(params);
        return font;
    }

}
