import leikr.Engine;
class Groove extends Engine {

	def t;
	int col;
	
	int x = 145;
	int y = 145;
	void create(){
		t = 0;
	}

	void update(){}

	void render(){
	
		bgColor(6);
		t += 0.1;
		int count = 15;
		
		for(int i = 0; i < count; i++){
			float angle = i/count+t;
			float dis = 70;
			float x = Math.cos(angle)*dis+150;
			float y = Math.sin(angle)*dis+100;		
		
			circle(x,y,12, i);
			circle(y,x,12,i);
		}		
		
		if(key("Left")){
			x -= 8;
		}
		if(key("Right")){
			x += 8;
		}
		if(key("Up")){
			y -= 8;
		}
		if(key("Down")){
			y += 8;
		}
		if(key("Space")){
			println(col);
		}
		
		drawText("Hello, World!", 330, 30, 2);
	}
}
//game = new Groove();
