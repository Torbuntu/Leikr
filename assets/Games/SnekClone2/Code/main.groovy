import leikr.Engine
class SnekClone extends Engine {
	
	def dirs = [:]
	def direction = [:]
	
	def dirs2 = [:]
	def direction2 = [:]
	
	Random rand
	def t = 0
	def score = 0
	def score2 = 0
	
	def food = [:]
	def snake = []
	def snake2 = []
	
	def gameOver = true
	def first = true
	def loser = ""
	
	void newGame(){
	 	score = 0
	 	score2 = 0
	 	loser = ""
	 	
	 	t = 0
	 	snake = []
	 	snake2 = []
	 	
		def head = [:]
		head.x = 15
		head.y = 8
		snake.add(head)
		def bdy = [:]
		bdy.x = 14
		bdy.y = 8
		snake.add(bdy)
		def tail = [:]
		tail.x = 13
		tail.y = 8
		snake.add(tail)
		
		def head2 = [:]
		head2.x = 18
		head2.y = 8
		snake2.add(head2)
		def bdy2 = [:]
		bdy2.x = 17
		bdy2.y = 8
		snake2.add(bdy2)
		def tail2 = [:]
		tail2.x = 16
		tail2.y = 8
		snake2.add(tail2)
		
		food.x = 0
		food.y = 0
		direction = dirs.up
		direction2 = dirs2.up
		setFood()
	}
	
	boolean gotFood(){
		return snake[0].x == food.x && snake[0].y == food.y
	}
	boolean gotFood2(){
		return snake2[0].x == food.x && snake2[0].y == food.y
	}
	
	void setFood(){
		food.x = rand.nextInt(29)
		food.y = rand.nextInt(19)
		def on1 = false
		def on2 = false
		
		snake.each{
			if (it.x == food.x && it.y == food.y){
				on1 = true
			}
		}
		snake2.each{
			if (it.x == food.x && it.y == food.y){
				on2 = true
			}
		}
		if(on1 && on2){
			setFood()
		}
	}
	
	void move(){
		def last_dir = direction
		def last_dir2 = direction2
		
		if(key("Up")){
			direction = dirs.up
		}
		if(key("Down")){
			direction = dirs.down
		}
		if(key("Left")){
			direction = dirs.left
		}
		if(key("Right")){
			direction = dirs.right
		}
		
		if(key("W")){
			direction2 = dirs2.up
		}
		if(key("S")){
			direction2 = dirs2.down
		}
		if(key("A")){
			direction2 = dirs2.left
		}
		if(key("D")){
			direction2 = dirs2.right
		}
		
		if(snake[0].x+direction.x == snake[1].x && snake[0].y+direction.y==snake[1].y){
			direction = last_dir
		}
		if(snake2[0].x+direction2.x == snake2[1].x && snake2[0].y+direction2.y==snake2[1].y){
			direction2 = last_dir2
		}
	}
	
	void create(){				
		rand = new Random()
		dirs.up = [:]
		dirs.up.x = 0
		dirs.up.y = -1
		
		dirs.down = [:]
		dirs.down.x = 0
		dirs.down.y = 1
		
		dirs.right = [:]
		dirs.right.x = 1
		dirs.right.y = 0
		
		dirs.left = [:]
		dirs.left.x = -1
		dirs.left.y = 0
		
		dirs2.up = [:]
		dirs2.up.x = 0
		dirs2.up.y = -1
		
		dirs2.down = [:]
		dirs2.down.x = 0
		dirs2.down.y = 1
		
		dirs2.right = [:]
		dirs2.right.x = 1
		dirs2.right.y = 0
		
		dirs2.left = [:]
		dirs2.left.x = -1
		dirs2.left.y = 0
		
		newGame()
	}

	void update(float delta){
		t+=delta
	}
	
	void render(){
		if(key("Space")){
			gameOver = false
			first = false
			newGame()
		}	
		
		if(gameOver){
			if(!first){
				text("Game Over, "+loser+" ate themselves!", 0, 10, 10)
				text("Final Score, p1:" + score + " , p2: "+score2, 0, 20, 10)
			}
			text("Press `Space` to play", 0, 30, 10)
		}else{
			bgColor(0.1f,0.1f,0.1f)
			def mbdy = [:]
			def mbdy2 = [:]
			if(t>0.1){	
				mbdy.x = (snake[0].x+direction.x)%30
				if(mbdy.x < 0) mbdy.x = 29
				mbdy.y = (snake[0].y+direction.y)%20
				if(mbdy.y < 0) mbdy.y = 19
				snake.each{
					if(mbdy.x == it.x && mbdy.y == it.y && mbdy != snake[snake.size()-1]){
						gameOver = true
						loser = "Player 1"
					}
				}
				
				mbdy2.x = (snake2[0].x+direction2.x)%30
				if(mbdy2.x < 0) mbdy2.x = 29
				mbdy2.y = (snake2[0].y+direction2.y)%20
				if(mbdy2.y < 0) mbdy2.y = 19
				snake2.each{
					if(mbdy2.x == it.x && mbdy2.y == it.y && mbdy2 != snake2[snake2.size()-1]){
						gameOver = true
						loser = "Player 2"
					}
				}
				
				snake.add(0,mbdy)
				snake2.add(0,mbdy2)
				
				if(!gotFood()){
					snake.remove(snake.size()-1)
				}else{
					setFood()
					score++
				}
				if(!gotFood2()){
					snake2.remove(snake2.size()-1)
				}else{
					setFood()
					score2++
				}
				t=0
			}
			
			move()
					
			//draw snake
			snake.each{
				square((it.x*8).toFloat(), (it.y*8).toFloat(),8,8,9)
			}	
			snake2.each{
				square((it.x*8).toFloat(), (it.y*8).toFloat(),8,8,2)
			}	
			//draw food
			square((food.x*8).toFloat(), (food.y*8).toFloat(), 8, 8, 11)
			
			text("p1: "+score, 0, 10, 10)
			text("p2: "+score2, 0, 20, 10)
		}
	}	
}
