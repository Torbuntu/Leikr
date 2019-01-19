import leikr.Engine
class Groove extends Engine {

	void create(){
	}

	void update(){}

	void render(){
		if(button(BTN.UP,1)){
			drawText("up", 1, 10, 1);
		}
		if(button(11,1)){
			drawText("right", 1, 10, 1);
		}
		if(button(12,1)){
			drawText("down", 1, 10, 1);
		}
		if(button(13,1)){
			drawText("left", 1, 10, 1);
		}
		if(button(10,2)){
			drawText("up", 1, 20, 1);
		}
		if(button(11,2)){
			drawText("right", 1, 20, 1);
		}
		if(button(12,2)){
			drawText("down", 1, 20, 1);
		}
		if(button(13,2)){
			drawText("left", 1, 20, 1);
		}
		
		
	}
}
