import leikr.Engine
class Megadude extends Engine {
	
	def p = [:]
	def i = [:]
	
	int height = 15
	int offX = 0
	int offY = 0
	def anim = [1,2,3]
	def animIndex = 0
	def time = 0
	
		
	Random rand
	void move(){
		def play = false		
		p.vx = 0
		p.sid = 0
		if(key("Left") && !solid(p.x+12, p.y)){		
			p.vx =-1
			i.x += 0.5
			p.f = 0
			p.w = true
		}
		if(key("Right") && !solid(p.x+20,p.y)){
			p.vx = 1
			i.x -= 0.5
			p.f = 1
			p.w = true
		}
		if(p.vx == 0){
			p.w = false
		}
		if( solid(p.x,p.y+24+p.vy) || solid(p.x+20,p.y+24+p.vy) ){
        	p.vy=0
        	p.g = true
		}else{
		    p.vy = p.vy+0.2
		    p.g = false
		}
		
		if((key("Up") || key("Space")) && p.vy==0){
			p.vy=-3.5
			play = true
			p.g = false
		}	
		
		if( p.vy<0 && (solid(p.x+p.vx,p.y+p.vy) || solid(p.x+20+p.vx,p.y+p.vy)) ){
		    p.vy=0
		    p.g = true
		    play = false
		}   
		
		if(play) sfx("jump")
		if(!p.g){
			p.sid = 5
		}
		if(p.g && !p.w){
			p.sid = 0
		}
		
		if(p.g && p.w){
			p.sid = anim[animIndex]
			if(time>5){
				animIndex++
				time = 0
			}
		}
		if(animIndex > 2){
			animIndex = 0
		}		
		
		p.x=p.x+p.vx
		p.y=p.y+p.vy

	}
	
	
	
	def solid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		int cellid = mapGet(mx,my)
		if(	cellid > 200 && cellid != 4){
			return true
		}
		return false
	}
	
	def getSolid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		return mapGet(mx,my)
	}
	
	void setSolid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		mapSet(mx,my, 16)
	}
	

	
	void updateTime(){
		time++
		if(time > 20) time = 0
	}

	void create(){		
		loadMap("map")
		loadImages()
		
		rand = new Random()
		p = [sid: 0, x: 10, y:12, vx: 0, vy: 0, f: 1, g: true, w: false, attack: false, swd: false, gun:false]
		i = [x:40, y:0]	
	}

	void update(float delta){
		move()
		updateTime()
	}
	
	void render(){
		bgColor(0.1f,0.1f,0.1f)
		//FPS()
		image("moon", i.x,i.y)
		
		map(0,0, offX, offY, 240, 160)
		
		if(p.f == 1) {
			if(p.attack){
				sprite32(attackSprite, p.x+7, p.y, false, false)
			}
			sprite32(p.sid, p.x.toFloat(), p.y.toFloat(), false, false)
		}else{
			if(p.attack){
				sprite32(attackSprite, p.x-7, p.y, true, false)
			}
			sprite32(p.sid, p.x.toFloat(), p.y.toFloat(), true, false)
		}	
	}
}
