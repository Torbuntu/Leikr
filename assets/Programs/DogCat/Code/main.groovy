class DogCat extends leikr.Engine {

	Player p = new Player()
    void create(){
        loadMap("map")
        p.makePlayerCat(screen)
    }
    
    def solid(x,y){
        float mx = (x)/32 
        float my = (y)/32
        println mapGet((int)mx,(int)my) 
        return	(mapGet((int)mx,(int)my) >= 81)
    }	  
    
    void update(float delta){
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
		if(key("Up") && p.vy <= 0){
			p.setState(3)//3 jumping
			p.y -= 40
		}
		if(solid(p.x,p.y+32) || solid(p.x+32, p.y+32)){
			p.onGround()
		}else{
			p.falling()
		}
		p.update(delta)
    }
    void render(){	
    	bgColor(6)
		map() 
		
		p.draw(screen)
		drawColor(23)
		line((int)p.x, (int)p.y+32, (int)(p.x+32), (int)p.y+32)
    }	
}

