
public class MyJavaGame extends leikr.Engine {
	
	int y, sy;
    public void create(){
    	y = 20;
    	sy = 1;
    }
    public void update(float delta){
        if(y >= 30 && sy == 1) sy = -1;
        if(y <= 5 && sy == -1) sy = 1;
        y += sy;
        if(key("X")){
			loadProgram("Shapes")
		}
    }
    public void render(){	
    	
		pixel(23, 120, 80);
		
		drawColor(2); //only pixel takes a color, so we set red for the following
		rect(10, 100, 45, 45);
		rect(60, 100, 45, 45, true); // full rect
		
		drawColor(9);
		circle(45, y, 5);
		circle(60, 20, 5, true);
		
		drawColor(5);
		triangle(5, 5, 5, 35, 20, 5);
		
		drawColor(1);
		line(5, 50, 120, 55);
		
		text("Press X to go to Shapes Demo", 0, 150, 1)		
    }	
}

