import java.io.File //for bad guy code test
import com.badlogic.gdx.controllers.*;

class Shapes extends leikr.Engine {

    int x, y, x2, y2, r, t, amt, rx, ry, i, j

    void create(){
        amt = 32             
        //println new File("/").list() //bad guy code test
        x = 120
        y = 80
        t = 0
        rx = 120
        ry = 80
    }
    void update(float delta){

    }
    void render(){	

        t+=1
        
        for(j = 0; j < 32; j++){
            pixel(j, j, 1)
        }
 
 		 for(i = 0; i < amt; i++){
            x = cos(i / 10 + t/40)*40
            y = sin(i / 10 + t/40)*40
            
            x2 = 40 + cos(x / 10 + t/40)*40
            y2 = 40 + sin(y / 10 + t/40)*40

            pixel((i%33)+1, -x2+rx+5, -y2+ry+5)//top left
            pixel((i%33)+1, -x2+rx+5, y2+ry-5)//bottom left
			
            pixel((i%33)+1, x2+rx-5, -y2+ry+5)//top right
            pixel((i%33)+1, x2+rx-5, y2+ry-5)//bottom rigt
        }

    }
        
}	
