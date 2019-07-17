class ColorTester extends leikr.Engine {

    float r,g,b
    
    //yt = y top, yb = y bottom
    int rx=49, yt = 32,yb = 112
    int gx=113
    int bx=177
    int w=16,h=16
    
    int exitX = 231, exitY = 2, exitS = 7
    
    int mx, my
    
   	//def thing
    
    void create(){
       r = 0f
       g = 0f
       b = 0f
       loadImages()
       mx = mouseX() as int
       my = mouseY() as int
       //thing = compile("ColorTester/help")//compile continuous
       //println thing.getClass()
    }
    boolean buttonColorChange(color, torb, cx){
    	return mx >= cx && mx <= cx+w && my >= torb && my <= torb+h
    }
   
    void update(float delta){
    	mx = mouseX() as int
    	my = mouseY() as int
    	if(r > 1.0f) r = 1.0f
    	if(g > 1.0f) g = 1.0f
    	if(b > 1.0f) b = 1.0f
    	
    	if(r < 0.0f) r = 0.0f
    	if(g < 0.0f) g = 0.0f
    	if(b < 0.0f) b = 0.0f
    	
		if(mouseClick(0)){
			if(buttonColorChange(r, yt, rx)) r += 0.01f
			if(buttonColorChange(r, yb, rx)) r -= 0.01f
			
			if(buttonColorChange(g, yt, gx)) g += 0.01f
			if(buttonColorChange(g, yb, gx)) g -= 0.01f
			
			if(buttonColorChange(b, yt, bx)) b += 0.01f
			if(buttonColorChange(b, yb, bx)) b -= 0.01f			
			
			if(mx >= exitX && mx <= exitX+exitS && my >= exitY && my <= exitY+exitS) pause()
		}
		
		
		//thing = compile("ColorTester/help")//compile continuous
		//thing.update()
    }
    void render(){	
    	image("BG",0,0)
    	drawColor(r,g,b)
    	rect(48,64,144,32, true)
    	
    	//rgb text values
    	r = (double)r.round(2)
    	g = (double)g.round(2)
    	b = (double)b.round(2)
    	text("Red:\n $r", 16, 16, 34)
    	text("Green:\n $g", 64, 16, 34)
    	text("Blue:\n $b", 132, 16, 34)
    	
    	
    	//mouse
    	drawColor(5)
    	line(mx, my, (mx+3) as int, (my+4) as int)
    	
    	//thing test
    	//if(thing != null){
    		//drawColor(thing.c)
    		//rect(thing.x, thing.y, thing.w, thing.h, true)
    	//}
    }
        
}	
