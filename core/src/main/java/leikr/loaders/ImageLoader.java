/*
 * Copyright 2019 torbuntu.
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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import leikr.MenuScreen;

/**
 *
 * @author tor
 */
public class ImageLoader {

    AssetManager assetManager;
    String[] images;
    String rootPath = "./Games/" + MenuScreen.GAME_NAME + "/Art/";

    public ImageLoader() {
        assetManager = new AssetManager();
        images = new File(rootPath).list();
        for (String path : images) {
            assetManager.load(rootPath + path, Texture.class);
        }
        assetManager.finishLoading();
    }

    public void load() {
        assetManager.finishLoading();//just to make sure we are done before calling getImage
    }

    public Texture getImage(String fileName) {
        return assetManager.get(rootPath+fileName+".png");
    }

    public void disposeImages() {
        assetManager.dispose();
    }

}
