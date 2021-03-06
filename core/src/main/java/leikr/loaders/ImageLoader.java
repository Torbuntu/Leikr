/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leikr.loaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.ExternalFileHandleResolver;
import org.mini2Dx.core.graphics.Texture;

/**
 *
 * @author tor
 */
public class ImageLoader {

    private String rootPath;

    private AssetManager assetManager;

    public ImageLoader() {
    }

    public void reloadImageLoader(String path) {
        disposeImages();
        assetManager = new AssetManager(new ExternalFileHandleResolver());
        rootPath = path + "/Art/";
        try {
            Arrays.asList(Mdx.files.external(rootPath).list()).stream()
                    .filter(file -> !file.isDirectory()
                    && (file.extension().equalsIgnoreCase("png")
                    || file.extension().equalsIgnoreCase("jpg")
                    || file.extension().equalsIgnoreCase("bmp")))
                    .forEach(p -> {
                        assetManager.load(rootPath + p.name(), Texture.class);
                    });
            assetManager.finishLoading();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        assetManager.finishLoading();//just to make sure we are done before calling getImage
    }

    public Texture getImage(String fileName) {
        return assetManager.get(rootPath + fileName, Texture.class);

    }

    public void disposeImages() {
        if (null != assetManager) {
            assetManager.clearAssetLoaders();
            assetManager.dispose();
        }
    }

}
