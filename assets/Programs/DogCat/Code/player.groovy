class Player{
	float x = 20, y = 20, vx, vy
	
	def idleAnimation
	def walkAnimation
	
	def makePlayerCat(screen){
		idleAnimation = screen.makeAnimSprite(0..9 as int[], [100f,100f,100f,100f,100f,100f,100f,100f,100f,100f] as float[], 2, true)
		walkAnimation = screen.makeAnimSprite(10..19 as int[], [100f,100f,100f,100f,100f,100f,100f,100f,100f,100f] as float[], 2, true)
	}
	
	def update(delta){
		idleAnimation.update(delta)
		walkAnimation.update(delta)
	}	

	def draw(screen){
		
		screen.spriteAnim(walkAnimation, x, y)	
	}
}
