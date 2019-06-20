class Enemy{

	def x, y, speed, width = 16, height = 16
	boolean hit, gone
	def type
	Random rand = new Random()
	Enemy(type){
		x = 240
		y = rand.nextInt(130)+10
		speed = 4
	}
	
	def update(delta){
		x -= speed
		if(x > 240) gone = true
	}
	
	def draw(screen){
		screen.sprite(16,x,y,1)
//		screen.drawColor(23)
//		screen.circle(x, (int)y, 8, true)	
	}
}
