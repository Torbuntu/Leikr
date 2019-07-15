package leikr.loaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import leikr.GameRuntime;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.SpriteSheet;

/**
 *
 * @author tor
 */
public class SpriteLoader {

    AssetManager assetManager;

    SpriteSheet spriteBank;
    SpriteSheet spriteBank16;
    SpriteSheet spriteBank32;
    SpriteSheet spriteBank64;

    String rootPath;

    private static SpriteLoader instance;

    private SpriteLoader() {
        assetManager = new AssetManager();
    }

    public static SpriteLoader getSpriteLoader() {
        if (instance == null) {
            instance = new SpriteLoader();
        }
        instance.resetSpriteLoader();
        instance.loadSpriteSheets();
        instance.addSpritesToSpriteBank();

        return instance;
    }

    public void loadManualSpritesheets(String programName) {
        try {
            assetManager.clear();
            rootPath = "Programs/" + programName + "/Sprites/Sprites.png";
            instance.loadSpriteSheets();
            instance.addSpritesToSpriteBank();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Manual Sprite sheet not loadable.");
        }
    }

    private void resetSpriteLoader() {
        assetManager.clear();
        rootPath = GameRuntime.getProgramPath() + "/Sprites/Sprites.png";
    }

    private void loadSpriteSheets() {
        assetManager.load(rootPath, Texture.class);
        assetManager.finishLoading();

    }

    private void addSpritesToSpriteBank() {
        spriteBank = new SpriteSheet(assetManager.get(rootPath, Texture.class), 8, 8);
        spriteBank16 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 16, 16);
        spriteBank32 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 32, 32);
        spriteBank64 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 64, 64);
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

    public void disposeSprites() {
        assetManager.clear();
    }

}
