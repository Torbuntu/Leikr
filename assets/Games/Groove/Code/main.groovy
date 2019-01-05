import leikr.Engine;
class Groove extends Engine {

	def t;
	int col;
	
	int x = 145;
	int y = 145;
	void create(){
		
	}

	void update(){}

	void render(){
		
		
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
		tallSprite(0,x,y, true);
		
	}
}
//game = new Groove();
