import leikr.Engine;
class Groove extends Engine {

	def t;
	int color;
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
	}
}
game = new Groove();
