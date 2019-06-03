class DogCat extends leikr.Engine {

	Player p = new Player()
    void create(){
        loadMap("map")
        p.makePlayerCat(screen)
    }
    
    def solid(x,y){
        float mx = (x)/32 
        float my = (y)/32
        int id = mapGet((int)mx,(int)my)
        return	( id > 81 && id <= 96)
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
    }
    void render(){	
    	bgColor(6)
		map() 
		
		p.draw(screen)
    }	
}

