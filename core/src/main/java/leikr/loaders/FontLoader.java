package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.font.MonospaceFont;
import org.mini2Dx.core.font.MonospaceFont.FontParameters;

/**
 *
 * @author tor
 *
 * Font loading class
 */
public class FontLoader {

    MonospaceFont defaultFont;

    public void initializeDefaultFont(AssetManager manager) {
        manager.load("./Data/Images/Fonts/zx_evolution_8x8.png", Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters();
        params.texturePath = "./Data/Images/Fonts/zx_evolution_8x8.png";
        params.spacing = 0;
        params.frameWidth = 8;
        params.frameHeight = 8;
        defaultFont = new MonospaceFont(params);
    }

    public MonospaceFont getDefaultFont() {
        return defaultFont;
    }

    public MonospaceFont getCustomFont(AssetManager manager, String fontPath, int spacing, int width, int height) {
        manager.load(fontPath, Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters();
        params.texturePath = fontPath;
        params.spacing = spacing;
        params.frameWidth = width;
        params.frameHeight = height;
        return new MonospaceFont(params);
    }
}
