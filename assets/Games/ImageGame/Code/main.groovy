import leikr.Engine
class ImageGame extends Engine {
	
	
	void create(){
		loadImages()
	}

	void update(float delta){
	
	}
	
	void render(){
		//FPS()
		bgColor(0)
	
		drawImage("moon", 0,0)
		sprite(4, 20,20)
	}
	
	
}
