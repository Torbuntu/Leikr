import leikr.Engine
class SimplePlatformer extends Engine {
	
	def p = [:]
	def i = [:]
	def bc = [:]
	def sc = [:]
	
	int height = 15
	int offX = 0
	int offY = 0
	def anim = [32,4,5,33]
	def animIndex = 0
	def time = 0
	
	def attackSprite = 35
	def swdAnim = [35,36,37,38]
	def swdIndex = 0
	def swdTime = 0
	
	def gunAnim = [39,40,41,42]
	def gunIndex = 0
	def gunTime = 0
	
	def bullets = []
		
	Random rand
	void move(){
		def play = false		
		p.vx = 0
		p.sid = 4
		if(key("Left") && !solid(p.x+1, p.y)){		
			p.vx =-1
			i.x += 0.5
			p.f = 0
			p.w = true
		}
		if(key("Right") && !solid(p.x+7,p.y)){
			p.vx = 1
			i.x -= 0.5
			p.f = 1
			p.w = true
		}
		if(p.vx == 0){
			p.w = false
		}
		if( solid(p.x,p.y+8+p.vy) || solid(p.x+7,p.y+8+p.vy) ){
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
		
		if( p.vy<0 && (solid(p.x+p.vx,p.y+p.vy) || solid(p.x+7+p.vx,p.y+p.vy)) ){
		    p.vy=0
		    p.g = true
		    play = false
		}   
		
		if(play) sfx("jump")
		if(!p.g){
			p.sid = 5
		}
		if(p.g && !p.w){
			p.sid = 4
		}
		
		if(p.g && p.w){
			p.sid = anim[animIndex]
			if(time>5){
				animIndex++
				time = 0
			}
		}
		if(animIndex > 3){
			animIndex = 0
		}
		
		attack()		
		
		p.x=p.x+p.vx
		p.y=p.y+p.vy
		if(p.x < 0){
			offX = 0
			p.x = 232
		}		
		if(p.x > 240){
			offX = 30
			p.x = 0
		}
	}
	
	void attack(){
		if(key("X") && !p.attack){
			p.attack = true
			if(p.f == 1){
				p.vx = 1
			}else{
				p.vx = -1
			}
			p.vy = 0
			p.w = false	
			p.swd = true
			p.gun = false
		}
		if(key("Z") && !p.attack){
			p.attack = true
			if(p.f == 1){
				p.vx = -1
			}else{
				p.vx = 1
			}
			p.vy = 0
			p.w = false
			p.swd = false
			p.gun = true
		}
		
		if(p.attack){
			p.sid = 34
			if(p.swd){
				attackSprite = swdAnim[swdIndex]
				if(swdTime > 2){
					swdIndex++
					swdTime = 0
				}
				if(swdIndex > 3){
					swdIndex = 0
					p.attack = false
					attackSprite = swdAnim[swdIndex]
				}
			}else{
				attackSprite = gunAnim[gunIndex]
				if(gunTime > 2){
					gunIndex++
					gunTime = 0
				}
				if(gunIndex > 3){
					gunIndex = 0
					p.attack = false
					attackSprite = gunAnim[gunIndex]
					int bspeed = -2
					int fx = -15
					if(p.f == 1){
						bspeed = 2
						fx = 15
					}
					
					def bullet = [x: (p.x+fx).toFloat(), y: (p.y).toFloat(), vx: bspeed, hit: false]
					bullets.add(bullet)
				}
			}
		}
	}
	
	def solid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		int cellid = mapCellId(mx,my)
		if(	cellid > 0 && cellid != 4){
			return true
		}
		return false
	}
	
	def getSolid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		return mapCellId(mx,my)
	}
	
	void setSolid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		setCellId(mx,my, 16)
	}
	
	
	void moveClouds(){
		bc.x -= 0.2
		sc.x -= 0.2
		if(bc.x < -8) {
			bc = [x: 240, y:(rand.nextInt(height)*10).toFloat()]
		}
		if(sc.x < -8){
			sc = [x: 240, y: (rand.nextInt(height)*10).toFloat()]
		}
	}
	
	void updateTime(){
		time++
		swdTime++
		gunTime++
		if(time > 20) time = 0
		if(swdTime > 20) swdTime = 0
		if(gunTime > 20) gunTime = 0
	}
	
	void drawBullets(){
		
		if(p.f == 1){
			bulletHitWall(-2)
			bullets.each{		
				sprite(43, (it.x).toFloat(), it.y, false, false)
				it.x += it.vx
			}
		}else{
			bulletHitWall(8)
			bullets.each{
				sprite(43, (it.x).toFloat(), it.y, true, false)
				it.x += it.vx				
			}
		}
	}
	
	void bulletHitWall(front){
		for(def b in bullets){
			if(getSolid(b.x+front, b.y) == 4){
				b.hit = true
				setSolid(b.x+front, b.y)//kill 3
			}
		
			if(solid(b.x+front,b.y)){
				b.hit = true
			}
		}
		bullets.removeAll{it.hit == true}
	}
	
	
	
	void create(){		
		loadMap("map")
		loadImages()
		
		rand = new Random()
		p = [sid: 4, x: 100, y:120, vx: 0, vy: 0, f: 1, g: true, w: false, attack: false, swd: false, gun:false]
		i = [x:40, y:0]	
		bc = [x:30, y:(rand.nextInt(height)*10).toFloat()]
		sc = [x:180, y:(rand.nextInt(height)*10).toFloat()]
	}

	void update(float delta){
		moveClouds()
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
				sprite(attackSprite, p.x+7, p.y, false, false)
			}
			sprite(p.sid, p.x, p.y, false, false)
		}else{
			if(p.attack){
				sprite(attackSprite, p.x-7, p.y, true, false)
			}
			sprite(p.sid, p.x, p.y, true, false)
		}
				
		sprite16(3, bc.x, bc.y)
		sprite16(4, sc.x, sc.y)	
		drawBullets()	
	}
}
