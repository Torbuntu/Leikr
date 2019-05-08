import leikr.Engine
import java.io.File //for bad guy code test

class Shapes extends Engine {

    int x, y, x2, y2, r, t, amt, rx, ry
    
	
    void create(){
        x = 120
        y = 80
        amt = 300
        t = 0
        rx = 120
        ry = 80
        usePixels()
        //println new File("/").list() //bad guy code test
    }
    void update(float delta){
        FPS()
    }
    void render(){	
        t+=1
        clpx()
        drawColor(2)
        for(int i = 0; i < amt; i++){
            x = cos(i / 32 + t/40)*50
            y = sin(i / 32 + t/40)*50
            
            x2 = 40 + cos(x / 13 + t/40)*40
            y2 = 40 + sin(y / 13 + t/40)*40
            
            //drawColor((i%14)+2)
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
