import leikr.Engine
class MapGame extends Engine {
	
	float mapX = 0;
	float mapY = 0;

	def solid(x,y){
    	int nx = -(mapX-120-x)/8;
    	int ny = -(mapY-80-y)/8;
    	    	
		drawText(mapCellId((-(mapX-120)/8).toFloat(), (-(mapY-80)/8).toFloat()).toString(), 0, 140, 5)
    	return mapCellId(nx,ny) == 3 || mapCellId(nx,ny) == 4;
	}
	
	
	void create(){
		loadMap("map")
		mapY = getMapHeight()-160
	}

	void update(float delta){
	
	}
	
	void render(){
		//FPS()
		bgColor(0)
		map(mapX,mapY)		
				
		if(key("Right") && !solid(1,0)){
			mapX-=1
			setCellId((-(mapX-120-1)/8).toFloat(), (-(mapY-80)/8).toFloat(), 1)
		}
		if(key("Left") && !solid(-1,0)){
			mapX+=1
		}
		if(key("Up") && !solid(0,-1)){
			mapY+=1
		}
		if(key("Down") && !solid(0,1)){
			mapY-=1		
		}
		
		if(key("Space")){		
			drawText("X: " + (-(mapX-124)/8).toFloat() + " , Y:" +  (-(mapY-86)/8), 0, 120, 5)
			drawText(mapCellId((-(mapX-124)/8).toFloat(), (-(mapY-86)/8).toFloat()).toString(), 0, 140, 5)
		}
		
		sprite(4, 120, 80)
		square(120,80,8,8,6, false)
	}
	
	
}
