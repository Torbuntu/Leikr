import Bullet
import Enemy
import Player

class LeikrShmup extends leikr.Engine {
	
	Player p = new Player()
	def bullets = []
	def enemies = []
	def offX = 0, offY = 0
    void create(){
    }
    
    void update(float delta){
    	offX--
    	if(offX <= -256) offX=0
    	
    	FPS()
        if(key("Space")){
        	bullets.add(new Bullet(p.x, p.y))	
        }
        
        //test
        if(key("E")) enemies.add(new Enemy(1))
        
        if(key("Left")) {
        	p.speedX = -3
        	offX += 1	
        }
        if(key("Right")) {
        	p.speedX = 3
        	offX -= 1	
        }
        if(key("Up") && offY < 0) {
        	p.speedY = -3
        	offY += 3	
        }
        if(key("Down") && offY >= -130) {
        	p.speedY = 3
        	offY -= 3
        }
        p.update(delta)
        
        bullets.each{
        	it.update(delta)
        } 
        enemies.each{ e ->
        	e.update(delta)
        	bullets.each{b ->
        		if (e.x < b.x + 4 && e.x + 8 > b.x && e.y < b.y + 4 && e.y + 8 > b.y ) e.hit = true
        	}
        	if(e.x < 0) e.gone = true
        }
        
        //Remove enemies and bullets if they are off the screen or hit.
        enemies.removeAll{it.hit == true}
        enemies.removeAll{it.gone == true}
        bullets.removeAll{it.gone == true}
    }
    
    void render(){	
    	image("exterior-parallaxBG1", offX-256, offY)
    	image("exterior-parallaxBG1", offX,offY)
    	image("exterior-parallaxBG1", offX+256, offY)
    	
    
		p.draw(screen)
		bullets.each{
			it.draw(screen)
		}
		enemies.each{
			it.draw(screen)
		}
    }	
}

