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
	
	void create(){
		loadMap("map")
	}

	void update(float delta){
		t+=delta
	}
	void render(){
		//FPS()
		bgColor(0)
		map(xp,yp)		
		
		float amnt = 1
		angle = 0.0
		int spnm = 0
		while(angle < amnt){
			x = len * Math.cos(angle+t)
			y = len * Math.sin(angle+t)
						
		    sprite(0,(x+120).toFloat(),(y+80).toFloat())
		    sprite(0,(-x+120).toFloat(),(-y+80).toFloat())
		    
		    angle += step
		    spnm++
		}
		if(t>20){
			t = 0
		}
		
		if(key("Right")){
			xp-=5
		}
		if(key("Left")){
			xp+=5
		}
		if(key("Up")){
			yp+=5
		}
		if(key("Down")){
			yp-=5			
			println mapCellId(((xp+120-xp)/8).toFloat(), ((yp+140-yp)/8).toFloat())
		}
		if(key("Space")){		
			sprite16(0,((xp+120-xp)).toFloat(), ((yp+140-yp)).toFloat()) 
		}
		
		sprite(4, 120, 140)
	}
}
