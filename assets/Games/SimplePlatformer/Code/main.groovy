import leikr.Engine
class SimplePlatformer extends Engine {
	
	def p = [:]
	def i = [:]
	def bc = [:]
	def sc = [:]
	
	int height = 15
	int offX = 30
	int offY = 0
	def anim = [32,4,5,33]
	def animIndex = 0
	def time = 0
		
	Random rand
	void move(){
		def play = false		
		p.vx = 0
		p.sid = 4
		if(key("Left")){		
			p.vx =-1
			i.x += 0.5
			p.f = 0
			p.w = true
		}
		if(key("Right")){
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
		
		if(key("Up") && p.vy==0){
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
	
	def solid(x,y){
		float mx = (x)/8+offX 
		float my = (y)/8+offY
		if(mapCellId(mx,my)	 > 0){
			return true
		}
		return false
	}
	
	void create(){		
		loadMap("map")
		loadImages()
		
		rand = new Random()
		p = [sid: 4, x: 100, y:120, vx: 0, vy: 0, f: 1, g: true, w: false]
		i = [x:40, y:0]	
		bc = [x:30, y:(rand.nextInt(height)*10).toFloat()]
		sc = [x:180, y:(rand.nextInt(height)*10).toFloat()]
		
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

	void update(float delta){
		moveClouds()
		move()
		time++
	}
	
	void render(){
		bgColor(0.1f,0.1f,0.1f)
		//FPS()
		image("moon", i.x,i.y)
		map(0,0, offX, offY, 240, 160)
			
		if(p.f == 1) {
			sprite(p.sid, p.x, p.y, false, false)
		}else{
			sprite(p.sid, p.x, p.y, true, false)
		}
						
		sprite16(3, bc.x, bc.y)
		sprite16(4, sc.x, sc.y)		
	}
}
