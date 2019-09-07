class Player{

	def x, y, speedX, speedY, width=32, height = 32
	
	Player(){
		x = 10
		y = 10
		speedX = 5	
		speedY = 5
	}
	
	def update(delta){
	
		if(x + speedX > 0 && x+16 + speedX < 240){
			x += speedX
		}
		if(y + speedY > 0 && y+16 + speedY < 160){
			y += speedY
		}
		speedX = 0
		speedY = 0
	}
	
	def draw(screen){
		screen.sprite(0,x,y,2)
//		screen.drawColor(1)
//		screen.rect(x, y, 16, 16, true)	
//		screen.rect((int)(x+16), (int)(y+6), 6, 4, true)	
	}
}
