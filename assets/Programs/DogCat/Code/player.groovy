class Player{
	float x = 40, y = 20, vx = 0, vy = 0, gv = 2
	
	def idleAnimation, walkingAnimation, runningAnimation, jumpingAnimation
	
	//0 = idle, 1 = walk, 2 = run, 3 = jump
	def state = 0
	
	//true = left, false = right
	boolean facing = true, ground = false, jumping = false
	
	def frameSpeedTen = [0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f]
	def frameSpeedEight = [0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f]
	
	def jumpTime = 10
	
	
	def makePlayerCat(screen){
		idleAnimation = screen.makeAnimSprite(0..9 as int[], frameSpeedTen as float[], 2, true)
		walkingAnimation = screen.makeAnimSprite(10..19 as int[], frameSpeedTen  as float[], 2, true)
		runningAnimation = screen.makeAnimSprite(20..27 as int[], frameSpeedEight as float[], 2, true)
		jumpingAnimation = screen.makeAnimSprite(30..37 as int[], frameSpeedEight as float[], 2, true)
	}
	
	def setState(newState){
		state = newState
	}
	def setFace(face){
		facing = face
	}
	
	def onGround(){
		ground = true
	}
	def falling(){
		ground = false
	}
	
	def setVX(spd){
		vx = spd
	}
	
	def update(delta){
		idleAnimation.update(delta)
		walkingAnimation.update(delta)
		runningAnimation.update(delta)
		jumpingAnimation.update(delta)
		
		//Movement
		if(jumping && jumpTime > 0){
			vy--
			jumpTime--
		}else{
			vy = 0
		}
		if(jumpTime == 0){
			jumpTime = 10
			jumping = false
		}
		vx = 0
		switch(state){
			case 1:
				if(facing){
					vx = -1.5
				}else{				
					vx = 1.5
				}
				break
			case 2:
				if(facing){
					vx = -2
				}else{				
					vx = 2
				}
				break
		}
			
		if(!ground && !jumping){
			vy = 3
			state = 3
		}
		
		y += vy
		x += vx
	}	

	def draw(screen){
		switch(state){
			case 0:
				screen.spriteAnim(idleAnimation, x, y, facing, false)	
				break
			case 1:
				screen.spriteAnim(walkingAnimation, x, y, facing, false)
				break
			case 2:
				screen.spriteAnim(runningAnimation, x, y, facing, false)
				break
			case 3:
				screen.spriteAnim(jumpingAnimation, x, y, facing, false)
				break
		}
	}
}
