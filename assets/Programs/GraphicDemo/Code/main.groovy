class GraphicDemo extends leikr.Engine {

    float x, xs, deg
    void create(){
    	x = 30
    	deg = 0
    	xs = 1f
        usePixels()
    }
    void update(float delta){
        FPS()
        if(x < 30) xs = 1f
        if(x > 130) xs = -1f
        x += xs
        if(deg > 179) deg = 0
        deg++
    }
    void render(){	
        clpx()
        sprite(0, 30, 30, 32.5f, 3)
        spriteSc(0, x, 50, 3f, 5f, deg)
        text("Hello, World!", 0, 80, 240, 1, 1)
    }	
}
