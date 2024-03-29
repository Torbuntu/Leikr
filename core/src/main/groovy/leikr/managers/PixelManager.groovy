/*
 * Copyright 2019 See AUTHORS.
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
package leikr.managers

import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.graphics.*

/**
 *
 * @author tor
 */
class PixelManager {

	private SpriteSheet pixels
	private List<Color> colorPalette

	PixelManager() {
		createPalette()
		createPixmap()
	}

	Color getDrawColor(int color) {
		if (color > colorPalette.size() || color < 0) {
			return Colors.BLACK()
		}
		return colorPalette.get(color)
	}

	Sprite getSprite(int id) {
		return pixels.getSprite(id)
	}

	private void createPalette() {
		colorPalette = [Colors.CLEAR(), Colors.WHITE(), Colors.WHITE_M1(), Colors.LIGHT_GRAY(), Colors.GRAY(),
						Colors.DARK_GRAY(), Colors.BLACK_P1(), Colors.BLACK(), Colors.RED(), Colors.GREEN(), Colors.BLUE(), Colors.MAROON(),
						Colors.CORAL(), Colors.SALMON(), Colors.PINK(), Colors.LIME(), Colors.FOREST(), Colors.OLIVE(), Colors.NAVY(), Colors.ROYAL(), Colors.SKY(),
						Colors.CYAN(), Colors.TEAL(), Colors.YELLOW(), Colors.GOLD(), Colors.GOLDENROD(), Colors.ORANGE(),
						Colors.BROWN(), Colors.TAN(), Colors.FIREBRICK(), Colors.PURPLE(), Colors.VIOLET(), Colors.MAGENTA()]
	}

	private void createPixmap() {
		Pixmap pm = Mdx.graphics.newPixmap(colorPalette.size(), 1, PixmapFormat.RGBA8888)
		colorPalette.size().times { i ->
			pm.setColor(getDrawColor(i))
			pm.drawPixel(i, 0)
		}
		pixels = new SpriteSheet(Mdx.graphics.newTexture(pm), 1, 1)
		pm.dispose()
	}

	void drawPixel(Graphics g, int id, int x, int y) {
		g.drawSprite(getSprite(id), x, y)
	}
}
