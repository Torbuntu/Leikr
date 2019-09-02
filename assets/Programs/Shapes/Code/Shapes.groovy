import java.io.File //for bad guy code test
import com.badlogic.gdx.controllers.*;
import groovy.transform.CompileStatic
@CompileStatic
class Shapes extends leikr.Engine {

    int r, t, amt, rx, ry, i, j, cx, cy, cvx, cvy 
	float x, y, x2, y2
    void create(){
        amt = 50             
        //println new File("/").list() //bad guy code test
        x = 120
        y = 80
        t = 0
        rx = 120
        ry = 80
        
        cx = 0
        cy = 0
        cvx = 1
        cvy = 1
    }
    void update(float delta){	
    	FPS()	
    }
    void render(){	
		
        t+=1
        if(cx == 190) cvx = -1
        if(cx == 0) cvx = 1
        if(cy == 130) cvy = -1
        if(cy == 0) cvy = 1
        
        cx += cvx
        cy += cvy
        
       	//clip(cx, cy, 50, 50)
        
        for(j = 0; j < 32; j++){
            pixel(j, j, 1)
        }
         
 		 for(i = 0; i < amt; i++){ 		 	
            x = (float)cos((float)(i / 10 + t/40))*80
            y = (float)sin((float)(i / 10 + t/40))*40
            
            x2 = 40 + (float)cos((float)(x / 10 + t/40))*80
            y2 = 40 + (float)sin((float)(y / 10 + t/40))*40

            pixel((i%32)+1, (int) (-x2+rx+5), (int) (-y2+ry+5))//top left
            pixel((i%32)+1, (int)(-x2+rx+5), (int) (y2+ry-5))//bottom left
			
           	pixel((i%32)+1, (int) (x2+rx-5),(int)( -y2+ry+5))//top right
            pixel((i%32)+1, (int) (x2+rx-5), (int)(y2+ry-5))//bottom rigt
        }
    }
        
}	
