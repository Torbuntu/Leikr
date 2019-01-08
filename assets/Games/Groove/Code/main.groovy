import leikr.Engine
class Groove extends Engine {

	float t = 0
	float x = 0
	float y = 0	
	int len = 50
	
	float angle = 0.0
	float step = 0.1

	void update(float delta){
		t+=delta
	}
	void render(){
		FPS()
		
		float amnt = 2*Math.PI
		angle = 0.0
		while(angle < amnt){
			x = len * Math.cos(angle+t)
			y = len * Math.sin(angle+t)
			
			x = Math.cos(x/10 * angle/t) * 120
			y = Math.sin(y/10 * angle/t) * 80
			
		    sprite(0,(x+120).toFloat(),(y+80).toFloat())
		    
		    
		    angle += step
		}
		drawText(x.toString(), 0, 20, 5) 
		drawText(t.toString(), 0, 30, 5) 
		if(t>20){
			t = 0
		}
	}
}
