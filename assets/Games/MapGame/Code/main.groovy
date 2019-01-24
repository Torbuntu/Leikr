import leikr.Engine
class MapGame extends Engine {
	
	float mapX = 120;
	float mapY = 80;
	
	void create(){		
		loadMap("map")
	}

	void update(float delta){
	}
	
	void render(){
		//FPS()
		bgColor(0)
		map(0,0)
		cam(mapX, mapY)	
				
		if(key("Right") ){
			mapX-=1
		}
		if(key("Left") ){
			mapX+=1
		}
		if(key("Up") ){
			mapY+=1
		}
		if(key("Down")){
			mapY-=1		
		}
		
		
		sprite(4, mapX, mapY)
	}
	
	
}
