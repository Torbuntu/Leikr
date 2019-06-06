class LeikrShmup extends leikr.Engine {
	
	Player p = new Player()
	def bullets = []
    void create(){
        
    }
    void update(float delta){
    	FPS()
        if(key("Space")){
        	bullets.add(new Bullet(p.x, p.y))	
        }
        
        if(key("Left")) p.speedX = -3
        if(key("Right")) p.speedX = 3
        if(key("Up")) p.speedY = -3
        if(key("Down")) p.speedY = 3
        p.update(delta)
        
        bullets.each{
        	it.update(delta)
        }
    }
    void render(){	
		p.draw(screen)
		bullets.each{
			it.draw(screen)
		}
    }	
}

