package leikr.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import java.util.Arrays;
import leikr.GameRuntime;

/**
 *
 * @author tor
 */
public class ImageLoader {

    AssetManager assetManager;
    String rootPath;

    private static ImageLoader instance;

    private ImageLoader() {
        assetManager = new AssetManager();
    }

    public static ImageLoader getImageLoader() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        instance.reloadImageLoader();
        return instance;
    }

    private void reloadImageLoader() {
        rootPath = GameRuntime.getProgramPath() + "/Art/";
        assetManager.clear();
        Arrays.asList(Gdx.files.internal(rootPath).list()).stream()
                .forEach(path -> assetManager.load(rootPath + path.name(), Texture.class));

        assetManager.finishLoading();
    }

    public void load() {
        assetManager.finishLoading();//just to make sure we are done before calling getImage
    }

    public Texture getImage(String fileName) {
        return assetManager.get(rootPath + fileName + ".png");
    }

    public void disposeImages() {
        assetManager.clear();
    }

}
