package leikr.loaders;

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.font.MonospaceGameFont.FontParameters;
import org.mini2Dx.core.graphics.Texture;

/**
 *
 * @author tor
 *
 * Font loading class
 */
public class FontLoader {

    MonospaceGameFont defaultFont;

    public void initializeDefaultFont(AssetManager manager) {
        manager.load("./Data/Images/Fonts/zx_evolution_8x8.png", Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters();
        params.texturePath = "./Data/Images/Fonts/zx_evolution_8x8.png";
        params.spacing = 0;
        params.frameWidth = 8;
        params.frameHeight = 8;
        defaultFont = new MonospaceGameFont(params);
    }

    public MonospaceGameFont getDefaultFont() {
        return defaultFont;
    }

    public MonospaceGameFont getCustomFont(AssetManager manager, String fontPath, int spacing, int width, int height) {
        manager.load(fontPath, Texture.class);
        manager.finishLoading();
        FontParameters params = new FontParameters();
        params.texturePath = fontPath;
        params.spacing = spacing;
        params.frameWidth = width;
        params.frameHeight = height;
        return new MonospaceGameFont(params);
    }
}
