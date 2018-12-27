import leikr.Engine;
class Groove extends Engine {

	def x;
	def y;
	void create(){
		x = 10;
		y = 100;
	}

	void update(){

	}

	void render(){
		if(key("Left")){
			x= x - 10;
		}
		if(key("Right")){
			x = x + 10;
		}
		square(x, y, 25, 25);
	}
}
game = new Groove();
