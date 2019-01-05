import leikr.Engine
class Groove extends Engine {

	float t = 0;
	def amnt = 500;
	float x = 0;
	float y = 0;
	float x2 = 0;
	float y2 = 0;

	void render(){
		FPS()
		t+=1
		for(float i = 0; i < amnt; i+=0.5){
			x = Math.cos(i / 32 + t / 40) * 50
		    y = Math.sin(i / 32 + t / 40) * 50

		    x2 = (240 + Math.cos(x / 13 + t / 40) * 240)
		    y2 = (240 + Math.sin(y / 13 + t / 40) * 240)
		    

		    pixel(-x2, (y2), 9)
		    pixel((x2 ), (y2 ), 9)
		    
		    pixel((x2 ), (- y2), 9)
		    pixel(- x2, (- y2 ), 9)
		    
		}
		
	}
}
