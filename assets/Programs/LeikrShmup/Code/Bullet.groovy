class Bullet{

	def x, y, speed
	boolean hit, gone
	
	Bullet(px, py){
		x = px+16
		y = py+12
		speed = 10
	}
	
	def update(delta){
		x += speed
		if(x > 240) gone = true
	}
	
	def draw(screen){
		screen.sprite(4,x,y)
//		screen.drawColor(3)
//		screen.rect(x, y, 4, 4, true)	
	}
}
