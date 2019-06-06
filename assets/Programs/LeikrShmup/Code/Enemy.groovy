class Enemy{

	def x, y, speed
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
		screen.drawColor(23)
		screen.circle(x, (int)y, 8, true)	
	}
}
