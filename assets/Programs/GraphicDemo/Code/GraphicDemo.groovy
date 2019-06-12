class GraphicDemo extends leikr.Engine {

    float x, xs, deg
    void create(){
    	x = 70
    	deg = 0
    	xs = 1f
    }
    void update(float delta){
        FPS()
        if(x < 70) xs = 1f
        if(x > 160) xs = -1f
        x += xs
        if(deg > 179) deg = 0
        deg++
    }
    void render(){	
        clip((float)(x-20f), 30f, 50f, 60f)  
        
        spriteSc(0, x, 50, 3f, 5f, deg)
        text("Hello, World!", 0, 80, 240, 1, 32)
       
        pixel( (int)(x%12)+2, 80, 80)
        clip()//clears clipping
        
        sprite(0, 30, 30, 32.5f, 3)
        
        
        
    }	
}
