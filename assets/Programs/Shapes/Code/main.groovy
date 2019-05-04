import leikr.Engine
import com.badlogic.gdx.math.MathUtils
import java.io.File;

class Shapes extends Engine {

    Random rand	
    int x, y, x2, y2, r, t, amt, rx, ry
    
	
    void create(){
        rand = new Random()
        x = 120
        y = 80
        amt = 300
        t = 0
        rx = 120
        ry = 80
        usePixels()
       // println new File("/").list()
    }
    void update(float delta){
        FPS()
    }
    void render(){	
        t+=1
        clpx()
        for(int i = 0; i < amt; i++){
            x = MathUtils.cos(i / 32 + t/40)*50
            y = MathUtils.sin(i / 32 + t/40)*50
            
            x2 = 40 + MathUtils.cos(x / 13 + t/40)*40
            y2 = 40 + MathUtils.sin(y / 13 + t/40)*40
            
            drawColor((i%14)+2)
            
            pixel(-x2+rx+5, -y2+ry+5)//top left
            pixel(-x2+rx+5, y2+ry-5)//bottom left

            pixel(x2+rx-5, -y2+ry+5)//top right
            pixel(x2+rx-5, y2+ry-5)//bottom right
        }
        
    }	
}

/*
function draw()
for i=0,48,0.5 do
local x = 64 + cos(i/16+t()/4)*40
local y = 64 + sin(i/16+t()/4)*40
local z = 10+i + cos(i/7+t())*3
sphere(x,y,z, 3, 8+i%8)
end
end
 */
