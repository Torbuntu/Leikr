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

import org.mini2Dx.core.GraphicsUtils;

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author tor
 */
class GraphicsManagerSpec extends Specification {
    
    def setup(){
        Mdx.graphics = Mock(GraphicsUtils)
    }
    
    @Unroll
    def "drawCircle completes when #desc" () {
        given:
        def graphics = Mock(Graphics)
        def spriteLoader = Mock(SpriteLoader)
        def imageLoader = Mock(ImageLoader)
        def mapLoader = Mock(MapLoader)
        def pixelManager = Mock(PixelManager)
        
        BigDecimal val = new BigDecimal(3)
        
        GraphicsManager manager = new GraphicsManager(spriteLoader, imageLoader, mapLoader, pixelManager)
        manager.g = graphics
        
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
    
}

