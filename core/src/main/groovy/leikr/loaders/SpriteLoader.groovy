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

import groovy.util.logging.Log4j2
import leikr.GameRuntime
import leikr.exceptions.RenderException
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.assets.AssetManager
import org.mini2Dx.core.assets.loader.TextureLoader
import org.mini2Dx.core.files.ExternalFileHandleResolver
import org.mini2Dx.core.graphics.Sprite
import org.mini2Dx.core.graphics.SpriteSheet
import org.mini2Dx.core.graphics.Texture

/**
 *
 * @author tor
 */
@Log4j2
class SpriteLoader {

	String rootPath

	AssetManager assetManager
	TextureLoader assetLoader

	SpriteSheet spriteBank
	SpriteSheet spriteBank16
	SpriteSheet spriteBank32
	SpriteSheet spriteBank64
	final GameRuntime runtime

	SpriteLoader(GameRuntime runtime) {
		this.runtime = runtime
	}

	void resetSpriteLoader(String path) {
		rootPath = "${path}/Sprites/Sprites.png"
		disposeSprites()

		assetLoader = new TextureLoader()
		assetManager = new AssetManager(new ExternalFileHandleResolver())
		assetManager.setAssetLoader(Texture.class, assetLoader)

		if (Mdx.files.external("${path}/Sprites/Sprites.png").exists()) {
			loadSpriteSheets()
			addSpritesToSpriteBank()
		} else {
			log.warn( "No sprites found for: {}", path)
		}

	}

	private void loadSpriteSheets() {
		assetManager.load(rootPath, Texture.class)
		assetManager.finishLoading()
	}

	private void addSpritesToSpriteBank() {
		spriteBank = new SpriteSheet(assetManager.get(rootPath, Texture.class), 8, 8)
		spriteBank16 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 16, 16)
		spriteBank32 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 32, 32)
		spriteBank64 = new SpriteSheet(assetManager.get(rootPath, Texture.class), 64, 64)
	}

	/**
	 * Get a sprite from one of the 4 banks.
	 *
	 * If the id is outside of the index range of a bank, the error screen will
	 * be invoked with the message in the catch.
	 *
	 * If a localized message exists, it will append it.
	 *
	 * The first sprite in default bank is returned to prevent followup errors
	 * in the EngineScreen render method.
	 *
	 * @param id The id of the sprite to return
	 * @param size Which bank size to index
	 * @return The sprite given the ID and size
	 */
	Sprite getSprite(int id, int size) {
		try {
			return [spriteBank, spriteBank16, spriteBank32, spriteBank64][size].getSprite(id)
		} catch (Exception ex) {
			log.error(ex)
			throw new RenderException("Error in program `render` method. Sprite index out of bounds. " + (ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ""))
		}
	}

	void loadManualSpriteSheets(String programName) {
//        resetSpriteLoader();
		rootPath = runtime.getProgramsPath() + "${programName}/Sprites/Sprites.png"
		if (!Mdx.files.external(rootPath).exists()) {
			log.warn( "No sprites found for: {}", rootPath)
			return
		}
		try {
			loadSpriteSheets()
			addSpritesToSpriteBank()
		} catch (Exception ex) {
			log.warn( "Manual Sprite sheet not loadable for: {}", rootPath)
			log.warn( "Sprite sheet load failure: ", ex)
		}
	}

	void disposeSprites() {
		if (assetManager) {
			assetManager.clearAssetLoaders()
			assetManager.dispose()
		}
	}

}
