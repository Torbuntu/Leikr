class DogCat extends leikr.Engine {

	Player p = new Player()
	Menu menu = new Menu()
	
	int mapX = 0, mapY = 0
	
	// Engine methods
    void create(){
        loadMap("map")
        p.makePlayerCat(screen)
        music("Map", true)
    }  
    
    void update(float delta){
    	if(menu.activeMenu){
    		if(key("Enter")) menu.activeMenu = false
    		return
    	}
        p.setState(0)
		if(key("Left") && !solid(p.x+10, p.y+16)){
			if(p.x+12 < 0){
				p.x = 220
				mapX = 0
			}
			p.setFace(true)
			if(key("L-Shift")) {
				p.setState(2)
			}else{
				p.setState(1)
			}		
		}
		if(key("Right") && !solid(p.x+20, p.y+16)){
			if(p.x+20 > 240) {
				p.x = -12
				mapX = 8
			}
			p.setFace(false)
			if(key("L-Shift")){
				p.setState(2)//2 running
			}else{
				p.setState(1)//1 walking
			}
		}
		if(key("Up") && p.vy == 0 && !p.jumping){
			sfx("jump")
			p.setState(3)//3 jumping
			p.vy = -0.5f
			p.jumping = true
		}
		if(solid(p.x+12,p.y+32) || solid(p.x+20, p.y+32)){
			p.onGround()
		}else{
			p.falling()
		}
		p.update(delta)
		
		
		
    }
    
    void render(){	
    	if(menu.activeMenu) {
    		menu.drawMenu(screen)
    		return
    	}
    	bgColor(6)
		map(0, 0, mapX, mapY, 240, 160) 
		
		p.draw(screen)
		drawColor(8)
		line((int)(p.x+12), (int)(p.y+32), (int)(p.x+20), (int)(p.y+32))
    }	
    //End engine methods
    
    //Start Helper methods 
    def solid(x,y){
        float mx = (x)/32 + mapX
        float my = (y)/32 + mapY
        int id = mapGet((int)mx,(int)my)
        return	( id > 81 && id <= 97)
    }	
}

