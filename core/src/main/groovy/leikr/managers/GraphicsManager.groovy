/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ScreenUtils
import leikr.loaders.ImageLoader
import leikr.loaders.MapLoader
import leikr.loaders.SpriteLoader
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.graphics.*
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.graphics.viewport.Viewport
import org.mini2Dx.gdx.math.MathUtils

/**
 * Manages the drawing API for {@link leikr.Engine}.
 *
 * @author tor
 */
class GraphicsManager {

	/**
	 * The path to the running programs local `Art` directory.
	 *
	 * @see #getPixels(java.lang.String)
	 */
	String path

	/**
	 * Properties set by Custom Properties
	 *
	 * @see #maxSprites can be overwritten in
	 * {@link leikr.properties.ProgramProperties} more custom
	 * experience.
	 */
	int maxSprites
	int usedSprites

	/**
	 * Loaders
	 *
	 * The loaders are used to load the custom assets for a game at startup.
	 */
	final SpriteLoader spriteLoader
	final ImageLoader imageLoader
	final MapLoader mapLoader

	/**
	 * Color objects. pixels are the indexed Color pixels which are used for the
	 * drawing functions. colorPalette is a List of all the available colors to
	 * use. bgColor is what {@link #preRender} uses to clear the background.
	 *
	 */
	Color bgColor
	final PixelManager pixelManager

	Graphics g
	Viewport viewport
	FrameBuffer frameBuffer

	/**
	 *
	 * @param spriteLoader
	 * @param imageLoader
	 * @param mapLoader
	 * @param pixelManager
	 */
	GraphicsManager(SpriteLoader spriteLoader, ImageLoader imageLoader, MapLoader mapLoader, PixelManager pixelManager) {
		this.spriteLoader = spriteLoader
		this.imageLoader = imageLoader
		this.mapLoader = mapLoader
		this.pixelManager = pixelManager
	}

	/**
	 * Resets and initializes assets for a new loaded game.
	 *
	 * @param path
	 * @param maxSprites
	 */
	void resetScreenManager(String path, int maxSprites) {
		this.path = path
		this.maxSprites = maxSprites
		spriteLoader.resetSpriteLoader(path)
		imageLoader.reloadImageLoader(path)
		mapLoader.resetMapLoader(path)
		bgColor = Colors.BLACK()
	}

	// <editor-fold desc="Engine methods" defaultstate="collapsed">
	void preCreate(FrameBuffer f, StretchViewport v) {
		this.frameBuffer = f
		this.viewport = v
	}

	void preRender(Graphics g) {
		// Reset used count to 0
		usedSprites = 0
		this.g = g
		this.g.clearContext(bgColor)
	}

	void preUpdate(float delta) {
		if (null != mapLoader.getMap()) {
			mapLoader.getMap().update(delta)
		}
	}

	void dispose() {
		spriteLoader.disposeSprites()
		imageLoader.disposeImages()
		mapLoader.disposeMap()
	}
	// </editor-fold>

	//Image methods
	final void loadImages() {
		imageLoader.load()
	}

	final void drawTexture(String name, Number x, Number y) {
		g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue())
	}

	final void drawTexture(String name, Number x, Number y, Number w, Number h) {
		g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue())
	}

	final void drawTexture(String name, Number x, Number y, Number w, Number h, boolean flipVertical) {
		g.drawTexture(imageLoader.getImage(name), x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue(), flipVertical)
	}
	//end Image methods

	// <editor-fold desc="Map methods" defaultstate="collapsed">
	final void loadMap(String map) {
		mapLoader.loadMap(map)
	}

	final void drawMap(Number x = 0, Number y = 0) {
		mapLoader.drawMap(g, x.intValue(), y.intValue())
	}

	final void drawMap(Number x, Number y, int layer) {
		mapLoader.drawMap(g, x.intValue(), y.intValue(), layer)
	}

	final void drawMap(Number x, Number y, Number sx, Number sy, Number w, Number h) {
		mapLoader.drawMap(g, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue())
	}

	final void drawMap(Number x, Number y, Number sx, Number sy, Number w, Number h, int layer) {
		mapLoader.drawMap(g, x.intValue(), y.intValue(), sx.intValue(), sy.intValue(), w.intValue(), h.intValue(), layer)
	}

	final int getMapTile(Number x, Number y, int layer = 0) {
		return mapLoader.getMapTile(x.intValue(), y.intValue(), layer)
	}

	final void setMapTile(int id, Number x, Number y, int layer = 0) {
		mapLoader.setMapTile(id, x.intValue(), y.intValue(), layer)
	}

	final void removeMapTile(Number x, Number y, int layer = 0) {
		mapLoader.removeMapTile(x.intValue(), y.intValue(), layer)
	}

	final int getMapHeight() {
		return mapLoader.getMap().getHeight()
	}

	final int getMapWidth() {
		return mapLoader.getMap().getWidth()
	}
	// </editor-fold>

	//start color methods
	final void bgColor(Color color) {
		bgColor = color
	}

	final void bgColor(int color) {
		bgColor = pixelManager.getDrawColor(color)
	}

	final void bgColor(String c) {
		bgColor = Colors.rgbToColor(c)
	}

	final Color getColor(int color) {
		return pixelManager.getDrawColor(color)
	}

	static final Color getColor(String color) {
		return Colors.rgbToColor(color)
	}
	//end color methods

	// <editor-fold desc="String drawing methods" defaultstate="collapsed">
	final void drawString(Color color, String text, Number x, Number y) {
		g.setColor(color)
		g.drawString(text, x.floatValue(), y.floatValue())
	}

	final void drawString(Color color, String text, Number x, Number y, Number width) {
		g.setColor(color)
		g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue())
	}

	final void drawString(Color color, String text, Number x, Number y, Number width, int align) {
		g.setColor(color)
		g.drawString(text, x.floatValue(), y.floatValue(), width.floatValue(), align)
	}

	final void drawString(int color, String text, Number x, Number y) {
		drawString(pixelManager.getDrawColor(color), text, x, y)
	}

	final void drawString(int color, String text, Number x, Number y, Number width) {
		drawString(pixelManager.getDrawColor(color), text, x, y, width)
	}

	final void drawString(int color, String text, Number x, Number y, Number width, int align) {
		drawString(pixelManager.getDrawColor(color), text, x, y, width, align)
	}

	final void drawString(String color, String text, Number x, Number y) {
		drawString(Colors.rgbToColor(color), text, x, y)
	}

	final void drawString(String color, String text, Number x, Number y, Number width) {
		drawString(Colors.rgbToColor(color), text, x, y, width)
	}

	final void drawString(String color, String text, Number x, Number y, Number width, int align) {
		drawString(Colors.rgbToColor(color), text, x, y, width, align)
	}
	// </editor-fold>

	// <editor-fold desc="Sprite methods" defaultstate="collapsed">
	private void drawSpriteRotate(int id, Number x, Number y, int size, Number degrees) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.rotate(degrees.floatValue())
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.rotate(-degrees.floatValue())
		usedSprites++
	}

	private void drawSpriteFlip(int id, Number x, Number y, int size, boolean flipX, boolean flipY) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.setFlip(flipX, flipY)
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.setFlip(false, false)
		usedSprites++
	}

	final void sprite(int id, Number x, Number y, int size = 0) {
		if (usedSprites >= maxSprites) {
			return
		}
		g.drawSprite(spriteLoader.getSprite(id, size), x.floatValue(), y.floatValue())
		usedSprites++
	}

	final void sprite(int id, Number x, Number y, int size, Number degrees) {
		drawSpriteRotate(id, x, y, size, degrees)
	}

	final void sprite(int id, Number x, Number y, boolean flipX, boolean flipY) {
		sprite(id, x, y, 0, flipX, flipY)
	}

	final void sprite(int id, Number x, Number y, int size, boolean flipX, boolean flipY) {
		drawSpriteFlip(id, x, y, size, flipX, flipY)
	}

	final void sprite(int id, Number x, Number y, int size, Number degrees, boolean flipX, boolean flipY) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.rotate(degrees.floatValue())
		t.setFlip(flipX, flipY)
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.setFlip(false, false)
		t.rotate(-degrees.floatValue())
		usedSprites++
	}
	//end sizable sprites

	//START special sprite mode
	final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph) {
		sprite(ids, px, py, pw, ph, 0)
	}

	final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size) {
		if (usedSprites >= maxSprites) {
			return
		}
		int x = px.intValue()
		int y = py.intValue()
		int inc = (size + 1) * 8

		int i = 0
		for (int h = 0; h < ph.intValue(); h++) {
			for (int w = 0; w < pw.intValue(); w++) {
				if (i < ids.size()) {
					g.drawSprite(spriteLoader.getSprite(ids.get(i), size), x, y)
					usedSprites++
				}
				i++
				x += inc
			}
			x = px.intValue()
			y += inc
		}
	}

	final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, boolean flipX, boolean flipY) {
		sprite(ids, px, py, pw, ph, 0, flipX, flipY)
	}

	final void sprite(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size, boolean flipX, boolean flipY) {
		if (usedSprites >= maxSprites) {
			return
		}
		if (!flipX && !flipY) {
			sprite(ids, px, py, pw, ph, size)
		} else {
			if (flipX && !flipY) {
				spriteArrayFlipX(ids, px, py, pw, ph, size)
			} else if (!flipX && flipY) {
				spriteArrayFlipY(ids, px, py, pw, ph, size)
			} else {
				spriteArrayFlipBoth(ids, px, py, pw, ph, size)
			}
		}
	}

	private void spriteArrayFlipBoth(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size) {
		int inc = (size + 1) * 8
		int startX = px.intValue() + (pw.intValue() * inc) - inc
		int y = py.intValue() + (ph.intValue() * inc) - inc
		int x = startX

		int i = 0
		for (int h = ph.intValue(); h > 0; h--) {
			for (int w = pw.intValue(); w > 0; w--) {
				if (i < ids.size()) {
					Sprite t = spriteLoader.getSprite(ids.get(i), size)
					t.setFlip(true, true)
					g.drawSprite(t, x, y)
					t.setFlip(false, false)
					usedSprites++
				}
				i++
				x -= inc
			}
			x = startX
			y -= inc
		}
	}

	private void spriteArrayFlipY(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size) {
		int inc = (size + 1) * 8
		int y = py.intValue() + (ph.intValue() * inc) - inc
		int x = px.intValue()

		int i = 0
		for (int h = ph.intValue(); h > 0; h--) {
			for (int w = 0; w < pw.intValue(); w++) {
				if (i < ids.size()) {
					Sprite t = spriteLoader.getSprite(ids.get(i), size)
					t.setFlip(false, true)
					g.drawSprite(t, x, y)
					t.setFlip(false, false)
					usedSprites++
				}
				i++
				x += inc
			}
			x = px.intValue()
			y -= inc
		}
	}

	private void spriteArrayFlipX(ArrayList<Integer> ids, Number px, Number py, Number pw, Number ph, int size) {
		int inc = (size + 1) * 8
		int start = px.intValue() + (pw.intValue() * inc) - inc
		int y = py.intValue()
		int x = start

		int i = 0
		for (int h = 0; h < ph.intValue(); h++) {
			for (int w = pw.intValue(); w > 0; w--) {
				if (i < ids.size()) {
					Sprite t = spriteLoader.getSprite(ids.get(i), size)
					t.setFlip(true, false)
					g.drawSprite(t, x, y)
					t.setFlip(false, false)
					usedSprites++
				}
				i++
				x -= inc
			}
			x = start
			y += inc
		}
	}

	/**
	 * Draws a sprite with given ID value at the given coordinates with a given
	 * Scale.
	 *
	 * The 'sc' portion of the method name refers to 'scale'
	 *
	 * @param id
	 * @param x
	 * @param y
	 * @param scale
	 */
	final void spriteSc(int id, Number x, Number y, Number scale, int size = 0) {
		spriteSc(id, x, y, scale, scale, size)
	}

	final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size = 0) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.setScale(scaleX.floatValue(), scaleY.floatValue())
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.setScale(1)
		usedSprites++
	}

	final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, Number degrees) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.setScale(scaleX.floatValue(), scaleY.floatValue())
		t.rotate(degrees.floatValue())
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.rotate(-degrees.floatValue())
		t.setScale(1)
		usedSprites++
	}

	final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, boolean flipX, boolean flipY) {
		spriteSc(id, x, y, scaleX, scaleY, 0, flipX, flipY)
	}

	final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, boolean flipX, boolean flipY) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.setScale(scaleX.floatValue(), scaleY.floatValue())
		t.setFlip(flipX, flipY)
		g.drawSprite(t, x.floatValue(), y.floatValue())
		t.setFlip(false, false)
		t.setScale(1)
		usedSprites++
	}

	final void spriteSc(int id, Number x, Number y, Number scaleX, Number scaleY, int size, Number degrees, boolean flipX, boolean flipY) {
		if (usedSprites >= maxSprites) {
			return
		}
		Sprite t = spriteLoader.getSprite(id, size)
		t.with {
			setScale(scaleX.floatValue(), scaleY.floatValue())
			rotate(degrees.floatValue())
			setFlip(flipX, flipY)
			g.drawSprite(t, x.floatValue(), y.floatValue())
			setFlip(false, false)
			rotate(-degrees.floatValue())
			setScale(1)
		}
		usedSprites++
	}

	// </editor-fold>

	// <editor-fold desc="Shape methods" defaultstate="collapsed">
	/**
	 * Internal drawPixel with color .
	 *
	 * @param color
	 * @param x
	 * @param y
	 */
	private final void drawPixelInternal(Color color, int x, int y) {
		tint(color)
		g.drawSprite(pixelManager.getSprite(1), x, y)
		g.removeTint()
	}

	/**
	 * Basic drawPixel taking Integer colord ID and a Number x,y coordinate
	 * value.
	 *
	 * @param color
	 * @param x
	 * @param y
	 */
	final void drawPixel(int color, Number x, Number y) {
		g.drawSprite(pixelManager.getSprite(color), x.intValue(), y.intValue())
	}

	/**
	 * Uses the white pixel with a colored tint to produce a pixel of any
	 * desired RGB color String, ex: "255,255,255"
	 *
	 * @param color
	 * @param x
	 * @param y
	 */
	final void drawPixel(String color, Number x, Number y) {
		drawPixelInternal(Colors.rgbToColor(color), x.intValue(), y.intValue())
	}

	/**
	 * Uses the white pixel with a colored tint to produce a pixel of any
	 * desired RGB color String, ex: "255,255,255"
	 *
	 * @param color
	 * @param x
	 * @param y
	 */
	final void drawPixel(Color color, Number x, Number y) {
		tint(color)
		g.drawSprite(pixelManager.getSprite(1), x.intValue(), y.intValue())
		g.removeTint()
	}

	final void drawRect(Color c, Number x, Number y, Number w, Number h) {
		for (int i = x.intValue(); i < x.intValue() + w.intValue(); i++) {
			drawPixelInternal(c, i, y.intValue())
			drawPixelInternal(c, i, y.intValue() + h.intValue() - 1)
		}
		for (int i = y.intValue(); i < y.intValue() + h.intValue(); i++) {
			drawPixelInternal(c, x.intValue(), i)
			drawPixelInternal(c, x.intValue() + w.intValue() - 1, i)
		}
	}

	final void drawRect(int c, Number x, Number y, Number w, Number h) {
		drawRect(pixelManager.getDrawColor(c), x, y, w, h)
	}

	final void drawRect(String c, Number x, Number y, Number w, Number h) {
		drawRect(Colors.rgbToColor(c), x, y, w, h)
	}

	final void fillRect(Color color, Number x, Number y, Number w, Number h) {
		g.setColor(color)
		g.fillRect(x.intValue(), y.intValue(), w.intValue(), h.intValue())
	}

	final void fillRect(int color, Number x, Number y, Number w, Number h) {
		fillRect(pixelManager.getDrawColor(color), x, y, w, h)
	}

	final void fillRect(String color, Number x, Number y, Number w, Number h) {
		fillRect(Colors.rgbToColor(color), x, y, w, h)
	}

	private void drawHLine(Color c, int x0, int x1, int y) {
		for (int i = x0; i < x1; i++) {
			drawPixelInternal(c, i, y)
		}
	}

	private void drawVLine(Color c, int x, int y0, int y1) {
		for (int i = y0; i < y1; i++) {
			drawPixelInternal(c, x, i)
		}
	}

	final void drawLineSegment(int c, Number x0, Number y0, Number x1, Number y1) {
		drawLineSegment(pixelManager.getDrawColor(c), x0, y0, x1, y1)
	}

	final void drawLineSegment(String c, Number x0, Number y0, Number x1, Number y1) {
		drawLineSegment(Colors.rgbToColor(c), x0, y0, x1, y1)
	}

	final void drawLineSegment(Color c, Number p0, Number v0, Number p1, Number v1) {
		int x0 = p0.intValue()
		int x1 = p1.intValue()
		int y0 = v0.intValue()
		int y1 = v1.intValue()

		if (y0 == y1) {
			if (x0 < x1) {
				drawHLine(c, x0, x1, y0)
			} else {
				drawHLine(c, x1, x0, y0)
			}
			return
		}
		if (x0 == x1) {
			if (y0 < y1) {
				drawVLine(c, x0, y0, y1)
			} else {
				drawVLine(c, x0, y1, y0)
			}
			return
		}

		int dx = Math.abs(x1 - x0)
		int sx = x0 < x1 ? 1 : -1
		int dy = -Math.abs(y1 - y0)
		int sy = y0 < y1 ? 1 : -1

		int err = dx + dy
		while (true) {
			drawPixelInternal(c, x0, y0)
			if (x0 == x1 && y0 == y1) {
				break
			}

			int e2 = 2 * err
			if (e2 >= dy) {
				err += dy
				x0 += sx
			}
			if (e2 <= dx) {
				err += dx
				y0 += sy
			}
		}
	}

	final void drawLineSegment(Color c, int p0, int v0, int p1, int v1) {
		int x0 = p0
		int x1 = p1
		int y0 = v0
		int y1 = v1

		if (y0 == y1) {
			if (x0 < x1) {
				drawHLine(c, x0, x1, y0)
			} else {
				drawHLine(c, x1, x0, y0)
			}
			return
		}
		if (x0 == x1) {
			if (y0 < y1) {
				drawVLine(c, x0, y0, y1)
			} else {
				drawVLine(c, x0, y1, y0)
			}
			return
		}

		int dx = Math.abs(x1 - x0)
		int sx = x0 < x1 ? 1 : -1
		int dy = -Math.abs(y1 - y0)
		int sy = y0 < y1 ? 1 : -1

		int err = dx + dy
		while (true) {
			drawPixelInternal(c, x0, y0)
			if (x0 == x1 && y0 == y1) {
				break
			}

			int e2 = 2 * err
			if (e2 >= dy) {
				err += dy
				x0 += sx
			}
			if (e2 <= dx) {
				err += dx
				y0 += sy
			}
		}
	}

	//https://www.geeksforgeeks.org/bresenhams-circle-drawing-algorithm/
	final void drawCircle(Color color, Number x, Number y, Number r) {
		g.setColor(color)
		//g.drawCircle(x.intValue(), y.intValue(), r.intValue())
		int cx = x.intValue()
		int cy = y.intValue()
		int cr = r.intValue()
		int d = 3 - (2 * cr)
		int nx = 0
		int ny = cr
		while (nx <= ny) {
			drawPixelInternal(color, cx + nx, cy + ny)
			drawPixelInternal(color, cx + nx, cy - ny)
			drawPixelInternal(color, cx - nx, cy + ny)
			drawPixelInternal(color, cx - nx, cy - ny)

			drawPixelInternal(color, cx + ny, cy + nx)
			drawPixelInternal(color, cx + ny, cy - nx)
			drawPixelInternal(color, cx - ny, cy + nx)
			drawPixelInternal(color, cx - ny, cy - nx)

			if (d <= 0) {
				d += 4 * nx + 6
			} else {
				ny--
				d += 4 * (nx - ny) + 10
			}
			nx++
		}
	}

	final void drawFillCircle(Color color, Number x, Number y, Number r) {
		g.setColor(color)
		//g.drawCircle(x.intValue(), y.intValue(), r.intValue())
		int cx = x.intValue()
		int cy = y.intValue()
		int cr = r.intValue()
		int d = 3 - (2 * cr)
		int nx = 0
		int ny = cr

		while (ny >= nx) {

			drawLineSegment(color, cx - nx, cy + ny, cx + nx + 1, cy + ny)
			drawLineSegment(color, cx - ny, cy + nx, cx + ny + 1, cy + nx)
			drawLineSegment(color, cx - nx, cy - ny, cx + nx + 1, cy - ny)
			drawLineSegment(color, cx - ny, cy - nx, cx + ny + 1, cy - nx)

			nx++
			if (d > 0) {
				ny--
				d += 4 * (nx - ny) + 10
			} else {
				d += 4 * nx + 6
			}
		}

	}

	final void fillCircle(Color color, Number x, Number y, Number r) {
		g.setColor(color)
//        g.fillCircle(x.intValue(), y.intValue(), r.intValue())
		drawFillCircle(color, x, y, r)
	}

	final void drawCircle(int color, Number x, Number y, Number r) {
		drawCircle(pixelManager.getDrawColor(color), x, y, r)
	}

	final void fillCircle(int color, Number x, Number y, Number r) {
		fillCircle(pixelManager.getDrawColor(color), x, y, r)
	}

	final void drawCircle(String color, Number x, Number y, Number r) {
		drawCircle(Colors.rgbToColor(color), x, y, r)
	}

	final void fillCircle(String color, Number x, Number y, Number r) {
		fillCircle(Colors.rgbToColor(color), x, y, r)
	}
	// </editor-fold>

	final void setClip(Number x, Number y, Number w, Number h) {
		g.setClip(x.intValue(), y.intValue(), w.intValue(), h.intValue())
	}

	final void removeClip() {
		g.removeClip()
	}

	// <editor-fold desc="Experimental methods" defaultstate="collapsed">
	void tint(Color color) {
		g.setTint(color)
	}

	void tint(int color) {
		tint(pixelManager.getDrawColor(color))
	}

	void tint(String color) {
		tint(Colors.rgbToColor(color))
	}

	/**
	 * Removes the applied tint color from the previous tint methods:
	 * {@link #tint(org.mini2Dx.core.graphics.Color)}
	 * {@link #tint(int)}
	 * {@link #tint(java.lang.String)}
	 *
	 * This isn't very obvious.
	 */
	void tint() {
		g.removeTint()
	}

	/**
	 * Convenience method to retrieve a color at a specified pixel coordinate of
	 * the frame buffer.
	 *
	 * Notes:
	 * <ul>
	 *     <li>The y coordinate parameter must be -1 in order to get the
	 * correct pixel.</li>
	 * 	<li>This must be ran from the {@link leikr.Engine#update()} or
	 * {@link leikr.Engine#update(float)} blocks to work.</li>
	 * </ul>
	 *
	 *
	 * @param x
	 * @param y
	 * @return The Color at the specified coordinate of the {@link #frameBuffer}
	 */
	Color getPixel(Number x, Number y) {
		int gx = MathUtils.ceil((viewport.getX() * viewport.getScaleX()) + (x.floatValue() * viewport.getScaleX()) as float)
		int gy = MathUtils.ceil((viewport.getY() * viewport.getScaleY()) + (y.floatValue() * viewport.getScaleY()) as float)
		gy = Gdx.graphics.getHeight() - gy
		frameBuffer.end()

		byte[] b = ScreenUtils.getFrameBufferPixels(gx, gy - 1, 1, 1, false)
		frameBuffer.begin()

		return Colors.rgbToColor("${0xFF & b[0]},${0xFF & b[1]},${0xFF & b[2]}")
	}

	/**
	 * Collects the pixel color from a given texture in the running program's Art directory
	 *
	 * @param name - The texture to collect the pixel color from
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @return - the Color of the pixel of the given texture, x, y
	 */
	Color getPixel(String name, Number x, Number y) {
		Pixmap pm = Mdx.graphics.newPixmap(Mdx.files.external("${path}/Art/${name}"))
		Color c = Mdx.graphics.newColor(pm.getPixel(x.intValue(), y.intValue()))
		pm.dispose()
		return c
	}

	/**
	 * Collects every color of a texture from the running Program's Art directory and
	 * returns them as a list of Color
	 * @param name - the Texture to load from the Art directory
	 * @return - the list of colors from top left to bottom right
	 */
	ArrayList<Color> getPixels(String name) {
		Pixmap pm = Mdx.graphics.newPixmap(Mdx.files.local("${path}/Art/${name}"))
		ArrayList<Color> colors = new ArrayList<>()
		for (int y = 0; y < pm.getHeight(); y++) {
			for (int x = 0; x < pm.getWidth(); x++) {
				colors.add(Mdx.graphics.newColor(pm.getPixel(x, y)))
			}
		}
		pm.dispose()
		return colors
	}
	// </editor-fold>

}
