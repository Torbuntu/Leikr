import java.io.File //for bad guy code test

class Shapes extends leikr.Engine {

    int x, y, x2, y2, r, t, amt, rx, ry, cx, cy, cw, ch, cd, cr
    Box box = new Box()

    void create(){
        x = 120
        y = 80
        amt = 500
        t = 0
        rx = 120
        ry = 80
        
        cx = 25
        cy = 25
        cw = 50
        ch = 50
        cd = 1
        cr = 1
        //println new File("/").list() //bad guy code test
    }
    void update(float delta){
        FPS()
        if (cx > 180) cd = -1
        if (cx < 25) cd = 1
        if (cy > 105) cr = -1
        if (cy < 25) cr = 1
        
        cx += cd
        cy += cr
    }
    void render(){	
        t+=1
       // clip(cx, cy, cw, ch)
        drawColor(15)
        rect(0, 12, 50, 10, true)
        for(int i = 0; i < 34; i++){
            pixel(i, i+10, 18)
        }
        
        for(int i = 0; i < amt; i++){
            x = cos(i / 10 + t/40)*40
            y = sin(i / 10 + t/40)*40
            
            x2 = 40 + cos(x / 10 + t/40)*40
            y2 = 40 + sin(y / 10 + t/40)*40

            pixel((i%34), -x2+rx+5, -y2+ry+5)//top left
            pixel((i%34), -x2+rx+5, y2+ry-5)//bottom left
			//sprite(0, -x2+rx+5, -y2+ry+5)
			//sprite(0, -x2+rx+5, y2+ry-5)
            pixel((i%34), x2+rx-5, -y2+ry+5)//top right
            pixel((i%34), x2+rx-5, y2+ry-5)//bottom right
            //sprite(0, x2+rx-5, -y2+ry+5)
            //sprite(0, x2+rx-5, y2+ry-5)
        }
        //box.square(screen, 0,20,20,20)
        //sprite(0, 10, 10, 32.5f)
        //clip()

        text("Hello, World!", 0, 80, 240, 1, 32)
        
        
    }	
}
