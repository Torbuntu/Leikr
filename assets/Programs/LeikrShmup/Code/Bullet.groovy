class Bullet{

	def x, y, speed
	
	Bullet(px, py){
		x = px+16
		y = py+6
		speed = 10
	}
	
	def update(delta){
		x += speed
	}
	
	def draw(screen){
		screen.drawColor(3)
		screen.rect(x, y, 4, 4, true)	
	}
}
