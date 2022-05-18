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
package leikr.loaders

import org.mini2Dx.core.Mdx
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.files.ExternalFileHandleResolver
import org.mini2Dx.core.graphics.Texture

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class ImageLoader {

	String rootPath

	AssetManager assetManager

	void reloadImageLoader(String path) {
		disposeImages()
		assetManager = new AssetManager(new ExternalFileHandleResolver())
		rootPath = "$path/Art/"
		try {
			Mdx.files.external(rootPath).list()
					.findAll(file -> !file.isDirectory() && (file.extension().toLowerCase() in ["png", "jpg", "bmp"]))
					.each(p -> {
						assetManager.load(rootPath + p.name(), Texture.class)
					})
			assetManager.finishLoading()
		} catch (IOException ex) {
			Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex)
		}
	}

	void load() {
		//just to make sure we are done before calling getImage
		assetManager.finishLoading()
	}

	Texture getImage(String fileName) {
		assetManager.get(rootPath + fileName, Texture.class)
	}

	void disposeImages() {
		if (assetManager) {
			assetManager.clearAssetLoaders()
			assetManager.dispose()
		}
	}

}
