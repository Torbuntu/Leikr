import leikr.Engine;
class Groove extends Engine {

	def t;
	int color;
	
	int x = 145;
	int y = 145;
	void create(){
		t = 0;
		color = 0;
	}

	void update(){}

	void render(){
		t += 0.1;
		int count = 20;
		
		for(int i = 0; i < count; i++){
			float angle = i/count+t;
			float dis = 70;
			float x = Math.cos(angle)*dis+100;
			float y = Math.sin(angle)*dis+100;			
			circle(x,y,2,color);
			circle(y,x,2,color);
			square(x,60,5,60,1);
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
		sprite(0, x, y);
	}
}
game = new Groove();
