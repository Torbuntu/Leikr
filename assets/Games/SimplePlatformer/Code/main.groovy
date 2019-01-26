import leikr.Engine
class SimplePlatformer extends Engine {
	
	def p = [:]
	
	int mid = 0
		
	void move(){
		p.vx = 0
		if(key("Left")) p.vx =-1
		if(key("Right")) p.vx = 1
		
		if( solid(p.x,p.y+8+p.vy) || solid(p.x+7,p.y+8+p.vy) ){
        	p.vy=0
		}else{
		    p.vy = p.vy+0.2
		}
		
		if(key("Up") && p.vy==0){
			p.vy=-2.5
			sfx("jump")
		}	
		
		if( p.vy<0 && (solid(p.x+p.vx,p.y+p.vy) || solid(p.x+7+p.vx,p.y+p.vy)) ){
		    p.vy=0
		}   

		p.x=p.x+p.vx
		p.y=p.y+p.vy
	}
	
	def solid(x,y){
		float mx = (x)/8 
		float my = (y)/8
		int id = mapCellId(mx,my)	
		mid = id	
		if(id > 0){
			return true
		}
		return false
	}
	
	void create(){		
		loadMap("map")
		p.sid = 4
		p.x = 100
		p.y = 120
		p.vx = 0
		p.vy = 0
	}

	void update(float delta){
		move()
	}
	
	void render(){
		bgColor(0)
		//FPS()
		
		map(0,0)
					
		sprite(p.sid, p.x, p.y)		
		text("ID: "+mid, 0, 20, 13)
	}
	
	
}
