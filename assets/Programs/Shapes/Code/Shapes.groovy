import java.io.File //for bad guy code test

class Shapes extends leikr.Engine {

    int x, y, x2, y2, r, t, amt, rx, ry, i, j
    
    void create(){
        x = 120
        y = 80
        amt = 500
        t = 0
        rx = 120
        ry = 80
        
        //println new File("/").list() //bad guy code test
    }
    void update(float delta){
        FPS()
       
    }
    void render(){	
        t+=1
        // clip(cx, cy, cw, ch)
        drawColor(15)
        rect(0, 12, 40, 10, true)
        for(j = 0; j < 34; j++){
            pixel(j, j+3, 18)
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
        text("Hello, World!", 0, 80, 240, 1, 32)

    }
        
}	
