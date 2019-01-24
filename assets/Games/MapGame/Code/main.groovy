import leikr.Engine
class MapGame extends Engine {
	
	
	int mapX = 0
	int mapY = 0
	def p = [:]
	
	float cx = 0
	float cy = 0
	
	void move(){
		p.sx = 0
		p.sy = 0
		
		if(key("Right") ){
			p.sx=1
			if(p.x+p.sx >= 200){
				p.sx = 0
				mapX -= 1
			}
		}
		if(key("Left") ){
			p.sx=-1
			if(p.x-p.sx <= 30){
				p.sx = 0
				mapX += 1
			}
		}
		
		
		if(key("Up") && !p.j){
			p.sy-=6
			p.j = true
		}
		
		if(key("Down")){
			p.sy+=1		
		}
		
		if(p.j){
			if(!solid(p.x+p.sx,p.y+8+p.sy)){
				p.sy+=p.g
			}else{
				p.j =false
			}
		}
		
		p.x += p.sx
		p.y += p.sy
	}
	
	def solid(x,y){
		float mx = -(mapX-x)/8 
		float my = -(mapY-y)/8
		println mx
		println my
		int id = mapCellId(mx,my)
		if(id == 1){
			true
		}
		false
	}
	
	
	
	void check(){
		float mx = -(mapX-p.x) 
		float my = -(mapY-p.y)
		
		square(mx,my,8,2,13,true)
		text("id: "+mapCellId((mx/8).toFloat(),(my/8).toFloat()), 0, 20, 2)
		text("px: "+p.x +" adj: "+(-(mapX-p.x/8)), 0, 30, 2)
		text("py: "+p.y, 0, 40, 2)
	}
	
	void create(){		
		loadMap("map")
		p.sid = 4
		p.x = 100
		p.y = 120
		p.g = 0.6
		p.sx = 0
		p.sy = 0
		p.j = false
	}

	void update(float delta){
		move()
	}
	
	void render(){
		bgColor(0)
		//FPS()
		
		map(mapX,0)
					
		sprite(p.sid, p.x, p.y)
		check()
	}
	
	
}
