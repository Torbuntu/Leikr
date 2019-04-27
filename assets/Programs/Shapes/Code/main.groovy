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
    }
    void update(float delta){
        if (i < 360) {
            a = (i * Math.PI / 360) 
            i++
        }else{
            i = 0
        }
        tx = x + r * MathUtils.cos(i/16+a/4)*40
        ty = y + r * MathUtils.sin(i/16+a/4)*40
    }
    void render(){	
        line(x, y, tx, ty)
        
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