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
		if(key("Left")){
			p.setFace(true)
			if(key("L-Shift")) {
				p.setState(2)
			}else{
				p.setState(1)
			}		
		}
		if(key("Right")){
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
		if(solid(p.x+8,p.y+32) || solid(p.x+16, p.y+32)){
			p.onGround()
		}else{
			p.falling()
		}
		p.update(delta)
		
		if(p.x > 240) {
			p.x = 0
			mapX = 8
		}
		if(p.x+16 < 0){
			p.x = 239
			mapX = 0
		}
    }
    
    void render(){	
    	if(menu.activeMenu) {
    		menu.drawMenu(screen)
    		return
    	}
    	bgColor(6)
		map(0, 0, mapX, mapY, 240, 160) 
		
		p.draw(screen)
    }	
    //End engine methods
    
    //Start Helper methods 
    def solid(x,y){
        float mx = (x)/32 + mapX
        float my = (y)/32 + mapY
        int id = mapGet((int)mx,(int)my)
        return	( id > 81 && id <= 96)
    }	
}

