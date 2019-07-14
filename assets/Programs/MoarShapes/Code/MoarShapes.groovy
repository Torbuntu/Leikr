class MoarShapes extends leikr.Engine {
	
	int amount = 16
	int t
	
    void create(){
        
    }
    void update(float delta){
        
    }
    void render(){	
		t = getFrame() as int
		for(int i = 0; i < amount; i++){
			int distance = cos(t / 60+i / 2) * 16 + 32
			int angle = i / amount + t / 120
			int x = cos(angle)*distance+64
			int y = sin(angle)*distance+64
  			int size = cos(t / 40 + i / 4) * 3 + 4
  			
			drawColor((i%32+8)as int)
			circle(x, y, size as int, true)
			drawColor(34)
			circle(x, y, (size / 2) as int, true)
		}
    }	
}

