/*
 * Copyright 2020 tor.
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

import java.math.BigDecimal;
import leikr.loaders.ImageLoader;
import leikr.loaders.MapLoader;
import leikr.loaders.SpriteLoader;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.Sprite
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.tiled.TiledMap;

import org.mini2Dx.core.GraphicsUtils;

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author tor
 */
class GraphicsManagerSpec extends Specification {
    def graphics 
    def spriteLoader
    def imageLoader
    def mapLoader
    def pixelManager
        
    GraphicsManager manager
    
    def setup(){
        Mdx.graphics = Mock(GraphicsUtils)
        
        graphics = Mock(Graphics)
        spriteLoader = Mock(SpriteLoader)
        imageLoader = Mock(ImageLoader)
        mapLoader = Mock(MapLoader)
        pixelManager = Mock(PixelManager)
        
        
        manager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager)
        manager.g = graphics
    }
    
    @Unroll
    def "drawCircle completes when #desc" () {
        given:
        BigDecimal val = new BigDecimal(3)

        
        when:
        manager.drawCircle(color, val, val, val)
        
        then:
        if(color == 0){
            1 * pixelManager.getDrawColor(0)
        }else{
            1 * Mdx.graphics.newColor(_, _, _, _)
        }
        1 * graphics.setColor(_)

        // 3 * 8 calls = 24
        24 * pixelManager.getSprite(1)
        24 * graphics.setTint(_)
        24 * graphics.drawSprite(_, _, _)
        24 * graphics.removeTint()
        0 * _
        
        where:
        color   | desc
        0       | "color id"
        "0,0,0" | "color rgb String"
    }
    
    @Unroll
    def "drawLineSegment completes when #desc" () {

        when:
        manager.drawLineSegment(color, values[0], values[1], values[2], values[3])
        
        then:
        if(color == 0){
            1 * pixelManager.getDrawColor(0)
        }else{
            1 * Mdx.graphics.newColor(_, _, _, _)
        }
        
        expected * graphics.setTint(_)
        expected * pixelManager.getSprite(1)
        expected * graphics.drawSprite(_, _, _)
        expected * graphics.removeTint()
        
        0 * _
        
        where:
        color   | values                                                                         | expected  || desc
        0       | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(10), new BigDecimal(10)] | 11        || "down right"
        0       | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(10), new BigDecimal(100)]| 101       || "down right long"
        "0,0,0" | [new BigDecimal(10), new BigDecimal(10), new BigDecimal(20), new BigDecimal(0)]| 11        || "up right"
        0       | [new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(10)] | 11        || "down left"
        0       | [new BigDecimal(100), new BigDecimal(0), new BigDecimal(0), new BigDecimal(10)]| 101       || "down left long"
        "0,0,0" | [new BigDecimal(10), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0)] | 11        || "up left"
        0       | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(10)]  | 10        || "vertical line forwards"
        "0,0,0" | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(10), new BigDecimal(0)]  | 10        || "horizontal line forwards"
        0       | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(-10)] | 10        || "vertical line backwards"
        "0,0,0" | [new BigDecimal(0), new BigDecimal(0), new BigDecimal(-10), new BigDecimal(0)] | 10        || "horizontal line backwards"
    }
    
    @Unroll
    def "successfully runs game loop and updates map when #desc" () {
        given:
        FrameBuffer fbo = Mock(FrameBuffer)
        StretchViewport svp = Mock(StretchViewport)
        
        when:
        manager.preCreate(fbo, svp)
        manager.preUpdate(0.0f)
        manager.preRender(graphics)
        manager.dispose()
        
        then:
        
        // preUpdate checks map and updates if not null
        expected * mapLoader.getMap() >> map
        //1 * map.update(0.0f)
        
        // preRender clears the graphics context using the bgColor, in this case not set so null
        1 * graphics.clearContext(null)
        
        // Dispose clears the assets
        1 * spriteLoader.disposeSprites();
        1 * imageLoader.disposeImages();
        1 * mapLoader.disposeMap();
        
        0 * _
        
        // preRender sets sprites to 0
        manager.getUsedSprites() == 0
        
        where:
        map             | expected  || desc
        Mock(TiledMap)  | 2         || "Map is not null"
        null            | 1         || "Map is null"
    }
    
    def "sprite with flip x and y are true succeeds" () {
        given:
        Sprite sprite = Mock(Sprite)
        def x = new BigDecimal(0)
        def y = new BigDecimal(0)
        def w = new BigDecimal(2)
        def h = new BigDecimal(2)
        ArrayList<Integer> ids = [0, 1, 2, 3]
        def path = "Programs/game"
        
        when:
        // reset manager and pre render to set the variables
        manager.resetScreenManager(path, 32)
        manager.preRender(graphics)
        manager.sprite(ids, x, y, w, h, true, true, size)
        
        then:
        1 * spriteLoader.resetSpriteLoader(path);
        1 * imageLoader.reloadImageLoader(path);
        1 * mapLoader.resetMapLoader(path);
        2 * Mdx.graphics.newReadOnlyColor(_, _, _, _)
        
        1 * graphics.clearContext(Colors.BLACK())
        
        4 * spriteLoader.getSprite(_, _) >> sprite
        4 * sprite.setFlip(true, true)
        4 * graphics.drawSprite(_, _, _)
        4 * sprite.setFlip(false, false)
        
        0 * _
        
        where:
        size    || desc
        0       || "original size"
        1       || "16x16 size"
    }
    
    def "sprite with only flip x true succeeds" () {
        given:
        Sprite sprite = Mock(Sprite)
        def x = new BigDecimal(0)
        def y = new BigDecimal(0)
        def w = new BigDecimal(2)
        def h = new BigDecimal(2)
        ArrayList<Integer> ids = [0, 1, 2, 3]
        def path = "Programs/game"
        
        when:
        // reset manager and pre render to set the variables
        manager.resetScreenManager(path, 32)
        manager.preRender(graphics)
        manager.sprite(ids, x, y, w, h, true, false, size)
        
        then:
        1 * spriteLoader.resetSpriteLoader(path);
        1 * imageLoader.reloadImageLoader(path);
        1 * mapLoader.resetMapLoader(path);
        2 * Mdx.graphics.newReadOnlyColor(_, _, _, _)
        
        1 * graphics.clearContext(Colors.BLACK())
        
        4 * spriteLoader.getSprite(_, _) >> sprite
        4 * sprite.setFlip(true, false)
        4 * graphics.drawSprite(_, _, _)
        4 * sprite.setFlip(false, false)
        
        0 * _
        
        where:
        size    || desc
        0       || "original size"
        1       || "16x16 size"
    }
    
    def "sprite with only flip y true succeeds" () {
        given:
        Sprite sprite = Mock(Sprite)
        def x = new BigDecimal(0)
        def y = new BigDecimal(0)
        def w = new BigDecimal(2)
        def h = new BigDecimal(2)
        ArrayList<Integer> ids = [0, 1, 2, 3]
        def path = "Programs/game"
        
        when:
        // reset manager and pre render to set the variables
        manager.resetScreenManager(path, 32)
        manager.preRender(graphics)
        manager.sprite(ids, x, y, w, h, false, true, size)
        
        then:
        1 * spriteLoader.resetSpriteLoader(path);
        1 * imageLoader.reloadImageLoader(path);
        1 * mapLoader.resetMapLoader(path);
        2 * Mdx.graphics.newReadOnlyColor(_, _, _, _)
        
        1 * graphics.clearContext(Colors.BLACK())
        
        4 * spriteLoader.getSprite(_, _) >> sprite
        4 * sprite.setFlip(false, true)
        4 * graphics.drawSprite(_, _, _)
        4 * sprite.setFlip(false, false)
        
        0 * _
        
        where:
        size    || desc
        0       || "original size"
        1       || "16x16 size"
    }
    
}

