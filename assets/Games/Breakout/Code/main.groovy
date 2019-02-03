//With help from: https://github.com/digitsensitive/tic-80-tutorials/tree/master/tutorials/breakout
import leikr.Engine
class Breakout ext}s Engine {

	int bgColor 
 	int score 
 	int lives 
	def player = [:]
	def ball = [:]
	def bricks = [:]
	int brickCountWidth
 	int brickCountHeight 
	
	void newGame(){
		bgColor = 0
 		score = 0
 		lives = 3
		player = [ x: (240/2)-12, y: 120, width: 24, height: 4, color: 3, speed: [x: 0, max: 4]]
		ball = [x: player.x+(player.width/2)-1.5, y: player.y-5, width: 3, height: 3, color: 14, deactive: true, speed: [x: 0, y: 0, max: 1.5]]
		brickCountWidth = 19
 		brickCountHeight = 12
 		bricks = []
 		for (int i in 0..brickCountHeight){
  			for( int j in 0..brickCountWidth){
   				def brick = [
    				x: 10+j*11,
    				y: 10+i*5,
    				width: 10,
    				height: 4,
    				color: i+1
   				]
   				bricks.add(0, brick)
  			}
 		}
	}
	
	void input(){
 		float sx = player.speed.x
 		float smax = player.speed.max

		// move to left
		if (key("Left") ){
		   	if (sx>-smax ){
				sx=sx-2
		   	}else{
				sx=-smax
			}
		}

	  	//move to right
	  	if (key("Right") ){
	   		if (sx<smax){
	   			sx=sx+2
	   		}else{
				sx=smax
	   		}
	  	}

  		player.speed.x=sx
  		player.speed.max=smax

  		if (ball.deactive){
   			ball.x = player.x+(player.width/2)-1.5
   			ball.y = player.y-5

  			if (btn(5) ){
   				ball.speed.x = Math.floor(math.random())*2-1
   				ball.speed.y = -1.5
   				ball.deactive = false
  			}
 		}
	}
	
	void lupdate(){
		float px = player.x
		float psx = player.speed.x
		float smax = player.speed.max

 		//update player position
 		px=px+psx

 		// reduce player speed
 		if ( psx != 0 ) {
  			if (psx > 0 ){
   				psx=psx-1
  			}else{
   				psx=psx+1
  			}
 		}

 		player.x=px
 		player.speed.x=psx
 		player.speed.max=smax

 		// update ball position
 		ball.x = ball.x + ball.speed.x
 		ball.y = ball.y + ball.speed.y

 		// check max ball speed
 		if (ball.speed.x > ball.speed.max){ 
  			ball.speed.x = ball.speed.max
 		}	
	}
	
	void collisions(){
 		// player <-> wall collision
 		playerWallCollision()

 		// ball <-> wall collision
 		ballWallCollision()

 		// ball <-> ground collision
 		ballGroundCollision()

 		// player <-> ball collision
 		playerBallCollision()

 		// ball <-> brick collision
 		ballBrickCollision()
	}
	
	
	void playerWallCollision(){
 		if (player.x < 0 ){
	 		player.x = 0
 		}else if( player.x+player.width > 240 ){
  			player.x = 240 - player.width
 		}
	}

	void ballWallCollision(){
 		if (ball.y < 0){
  			// top
  			ball.speed.y = -ball.speed.y    
 		}else if (ball.x < 0 ) {
  			// left
  			ball.speed.x = -ball.speed.x
 		}else if ( ball.x > 240 - ball.width) {
  			// right
  			ball.speed.x = -ball.speed.x
 		}
	}

	void ballGroundCollision(){
 		if (ball.y > 136 - ball.width ){
  			// reset ball
  			ball.deactive = true
  			// loss a life
  			if (lives > 0 ){
   				lives = lives - 1
  			}else if (lives == 0 ){
   				// game over
   				gameOver()
  			}
 		}
	}

	void playerBallCollision()
	 	if (collide(player,ball)) {
	  		ball.speed.y = -ball.speed.y
	  		ball.speed.x = ball.speed.x + 0.3*player.speed.x
	 	}
	}

	void collide(a,b)
 		// get parameters from a and b
 		float ax = a.x
 		float ay = a.y
 		float aw = a.width
 		float ah = a.height
 		float bx = b.x
 		float by = b.y
 		float bw = b.width
 		float bh = b.height

		 // check collision    
		 if (ax < bx+bw && ax+aw > bx && ay < by+bh && ah+ay > by) {
			 // collision
			 return true
		 }
	 	// no collision
	 	return false
	}

	void ballBrickCollision()
		bricks.each{
			// get parameters
			float x = it.x
			float y = it.y
			float w = it.width
			float h = it.height

			// check collision
			if (collide(ball, it)) {
				// collide left or right side
			  	if (y < ball.y && ball.y < y+h && ball.x < x || x+w < ball.x) {
					ball.speed.x = -ball.speed.x
			  	}
			  	// collide top or bottom side		
			   	if (ball.y < y || ball.y > y && x < ball.x && ball.x < x+w ){
				 	ball.speed.y = -ball.speed.y
			   	}
			   	bricks.remove(it)
			   	score = score + 1
			}
		 }
	}

	void draw()
		drawGameObjects()
		drawGUI()
	}

	void drawGameObjects()
 		// draw player
 		rect(player.x,
  player.y,
  player.width,
  player.height,
  player.color)

	 	// draw ball
	 	rect(ball.x, ball.y, ball.width, ball.height, ball.color)

	 	// draw bricks
	 	bricks.each{
	 
			rect(it.x, it.y, it.width, it.height, bricks[i].color)
		}
	}

void drawGUI()
 print("Score ",5,1,7)
 print(score,40,1,7)
 print("Score ",5,0,15)
 print(score,40,0,15)
 print("Lives ",190,1,7)
 print(lives,225,1,7)
 print("Lives ",190,0,15)
 print(lives,225,0,15)
}

void gameOver()
 print("Game Over",(240/2)-6*4.5,136/2)
 spr(0,240/2-4,136/2+10)
  if btn(5) {
   init()
  }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void create(){				
		newGame()
	}

	void update(float delta){
	
	}
	
	void r}er(){	
		
	}	
}
