import leikr.Engine
import com.badlogic.gdx.math.MathUtils

class Shapes extends Engine {

    Random rand	
    int x, y, r, a, i, tx, ty
    
	
    void create(){
        rand = new Random()
        x = 120
        y = 80
        r = 10
        i = 0
        usePixels()
    }
    void update(float delta){
        //        if (i < 360) {
        //            a = (i * Math.PI / 360) 
        //            i++
        //        }else{
        //            i = 0
        //        }
        //        tx = x + r * MathUtils.cos(i/16+a/4)*40
        //        ty = y + r * MathUtils.sin(i/16+a/4)*40
        FPS()
    }
    void render(){	
        //        line(x, y, tx, ty)
        def seed = rand.nextInt(100);
        for (int x = 0; x<240; x++) {
            for (int y = 0; y<160; y++) {
                drawColor((x*y)%16);
                //pixel(x,y);
                //                pixCircle(x, y, x)
                //                pixRect(x, y, seed, seed)
            }
        }
        for(int x = 0; x < 240; x++){
            drawColor(x%16)

            rect(seed,seed,x,x)
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