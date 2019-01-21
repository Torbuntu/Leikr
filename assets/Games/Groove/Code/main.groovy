import leikr.Engine
class Groove extends Engine {

	float t = 0
	float x = 0
	float y = 0	
	int len = 50
	
	float angle = 0.0
	float step = 0.1
	
	float xp = 0;
	float yp = 0;

	void update(float delta){
		t+=delta
	}
	void render(){
		FPS()
		bgColor(0)
		map(xp,yp)
		
		float amnt = 2000
		angle = 0.0
		int spnm = 0
		while(angle < amnt){
			x = len * Math.cos(angle+t)
			y = len * Math.sin(angle+t)
			
			x = Math.cos(x/10 * angle/t) * 120
			y = Math.sin(y/10 * angle/t) * 80
			
		    sprite(0,(x+120).toFloat(),(y+80).toFloat())
		    sprite(0,(-x+120).toFloat(),(-y+80).toFloat())
		    
		    angle += step
		    spnm++
		}
		drawText(x.toString(), 0, 20, 5) 
		drawText(t.toString(), 0, 30, 5) 
		drawText(getUsedSprites().toString(), 0, 40, 5)
		if(t>20){
			t = 0
		}
		
		if(key("Right")){
			xp+=5
		}
		if(key("Left")){
			xp-=5
		}
		if(key("Up")){
			yp-=5
		}
		if(key("Down")){
			yp+=5
		}
		sprite(1, xp, yp)
	}
}
