import java.io.File //for bad guy code test

class Shapes extends leikr.Engine {

    int x, y, x2, y2, r, t, amt, rx, ry
    Box box = new Box()
    	
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
            x = cos(i / 10 + t/40)*40
            y = sin(i / 10 + t/40)*40
            
            x2 = 40 + cos(x / 10 + t/40)*40
            y2 = 40 + sin(y / 10 + t/40)*40
            
            //drawColor((i%14)+2)
            pixel(-x2+rx+5, -y2+ry+5)//top left
            pixel(-x2+rx+5, y2+ry-5)//bottom left

            pixel(x2+rx-5, -y2+ry+5)//top right
            pixel(x2+rx-5, y2+ry-5)//bottom right
        }
        box.square(screen, 0,20,20,20)
        
        text("Hello, World!", 0, 80, 240, 1, 1)
    }	
}
