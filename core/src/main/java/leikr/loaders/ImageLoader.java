package leikr.loaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.GameRuntime;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.assets.loader.TextureLoader;
import org.mini2Dx.core.files.LocalFileHandleResolver;
import org.mini2Dx.core.graphics.Texture;

/**
 *
 * @author tor
 */
public class ImageLoader {

    AssetManager assetManager;
    TextureLoader assetLoader;
    String rootPath;

    private static ImageLoader instance;

    private ImageLoader() {
        assetLoader = new TextureLoader();
        assetManager = new AssetManager(new LocalFileHandleResolver());
        assetManager.setAssetLoader(Texture.class, assetLoader);
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
        try {
            Arrays.asList(Mdx.files.local(rootPath).list()).stream()
                    .forEach(path -> assetManager.load(rootPath + path.name(), Texture.class));
            assetManager.finishLoading();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        assetManager.finishLoading();//just to make sure we are done before calling getImage
    }

    public Texture getImage(String fileName) {
        return assetManager.get(rootPath + fileName + ".png", Texture.class);
    }

    public void disposeImages() {
//        assetManager.clear();
    }

}
