/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 *
 * @author tor
 * 
 * Super Temporary font loading class
 */
public class FontLoader {

    BitmapFont font;
    
    public FontLoader() {
        //Generate a font object for font.ttf at size 40px
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 8;
        parameter.flip = true;

        //The following settings allow the font to scale smoothly
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.Linear;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(new FileHandle("./ProggySquareSZ.ttf"));

        font = generator.generateFont(parameter);
        font.setUseIntegerPositions(false);
    }
    
    public BitmapFont getFont(){
        return font;
    }

}
