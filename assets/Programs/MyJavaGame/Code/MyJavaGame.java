public class MyJavaGame extends leikr.Engine {



	int y, sy, t, steam;
    public void create(){
    	y = 20;
    	sy = 1;
    	t = 0;
    	steam = 1;
    	loadMap("map");
    }
    public void update(float delta){
        if(y >= 30 && sy == 1) sy = -1;
        if(y <= 5 && sy == -1) sy = 1;
        y += sy;
        if(key("X")){
			loadProgram("Shapes")
		}
		if(key("Up") || key("Down") || key("Left") || key("Right")) steam++;
		if(steam > 3000) steam = 1;
		t++;
		if( t > 100 ) t = 0;
    }
    public void render(){
		map();
		pixel((t%33), 120, 80);

		drawColor(23); //only pixel takes a color, so we set red for the following
		rect(10, 100, 45, 45);
		rect(60, 100, 45, 45, true); // full rect

		drawColor(13);
		circle(45, y, 5);
		circle(60, 20, 5, true);

		drawColor(1);
		triangle(5, 5, 5, 35, 20, 5);

		drawColor(1);
		line(5, 50, 120, 55);

		sprite((steam%3)+1, 150, 84, 1);
		sprite(0, 150, 100, 1);

		text("Press X to go to Shapes Demo", 0, 150, 32);
    }
}

