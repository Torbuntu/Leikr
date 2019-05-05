//With help from: https://github.com/nesbox/TIC-80/wiki/Snake-Clone-tutorial
import leikr.Engine
class SnekClone2 extends Engine {

	def dirs = [:]
	def direction = [:]	
	def direction2 = [:]
	def food = [x:0,y:0]
	def snake = []
	def snake2 = []
	def mbdy = [:]
	def mbdy2 = [:]
	def head = [x:14, y:8]		
	def bdy = [x:14, y:9]
	def tail = [x:14, y:10]	
	def head2 = [x:16, y:8]		
	def bdy2 = [x:16, y:9]		
	def tail2 = [x:16, y:10]
	def gameOver = true
	def first = true
	def loser = ""
	def t = 0
	def score = 0
	def score2 = 0
	
	void newGame(){
	 	score = 0
	 	score2 = 0
	 	t = 0
	 	snake = []
	 	snake2 = []
		snake.addAll([head, bdy, tail])				
		snake2.addAll([head2, bdy2, tail2])
		direction = dirs.up
		direction2 = dirs.up
		setFood()
	}
	
	boolean gotFood(snek){
		return snek[0].x == food.x && snek[0].y == food.y
	}
	
	void setFood(){
		food = [x: randInt(29), y:randInt(19)]
			
		snake.each{
			if (it.x == food.x && it.y == food.y){
				setFood()
			}
		}
		snake2.each{
			if (it.x == food.x && it.y == food.y){
				setFood()
			}
		}
	}
	
	void move(){
		def last_dir = direction
		def last_dir2 = direction2
		
		if(key("Up")||button(BTN.UP,0)){
			direction = dirs.up
		}
		if(key("Down")||button(BTN.DOWN,0)){
			direction = dirs.down
		}
		if(key("Left")||button(BTN.LEFT,0)){
			direction = dirs.left
		}
		if(key("Right")||button(BTN.RIGHT,0)){
			direction = dirs.right
		}
		
		if(key("W")||button(BTN.UP,1)){
			direction2 = dirs.up
		}
		if(key("S")||button(BTN.DOWN,1)){
			direction2 = dirs.down
		}
		if(key("A")||button(BTN.LEFT,1)){
			direction2 = dirs.left
		}
		if(key("D")||button(BTN.RIGHT,1)){
			direction2 = dirs.right
		}
		
		if(snake[0].x+direction.x == snake[1].x && snake[0].y+direction.y==snake[1].y){
			direction = last_dir
		}
		if(snake2[0].x+direction2.x == snake2[1].x && snake2[0].y+direction2.y==snake2[1].y){
			direction2 = last_dir2
		}
	}
	
	void drawSneks(){
		if(score>score2){
			//drawColor(4)
			snake.each{
				sprite(0, (it.x*8), (it.y*8))
				//square((it.x*8), (it.y*8),8,8)
			}	
			//drawColor(2)
			snake2.each{
				sprite(1, (it.x*8), (it.y*8))
				//square((it.x*8), (it.y*8),8,8)
			}
		}else{		
			//drawColor(2)	
			snake2.each{
				sprite(1, (it.x*8), (it.y*8))
				//square((it.x*8), (it.y*8),8,8)
			}	
			//drawColor(4)
			snake.each{
				sprite(0, (it.x*8), (it.y*8))
				//square((it.x*8), (it.y*8),8,8)
			}
		}
	}
	
	void create(){		
		dirs.up = [x:0, y:-1]		
		dirs.down = [x:0, y:1]
		dirs.right = [x:1, y:0]
		dirs.left = [x:-1, y:0]		
		newGame()
                usePixels()
	}

	void update(float delta){
		FPS()
		if(gameOver && key("Space") || button(BTN.START)){
			gameOver = false
			first = false
			newGame()
		}
		t+=delta
		
		if(!gameOver && t>0.1){
			mbdy = [x: (snake[0].x+direction.x)%30, y: (snake[0].y+direction.y)%20]
			if(mbdy.x < 0) mbdy.x = 29
			if(mbdy.y < 0) mbdy.y = 19
				
			mbdy2 = [x: (snake2[0].x+direction2.x)%30, y: (snake2[0].y+direction2.y)%20]
			if(mbdy2.x < 0) mbdy2.x = 29				
			if(mbdy2.y < 0) mbdy2.y = 19
				
			snake.each{
				if(mbdy.x == it.x && mbdy.y == it.y && mbdy != snake[snake.size()-1]){
					gameOver = true
					loser = "Player 1"
				}
			}			
			snake2.each{
				if(mbdy2.x == it.x && mbdy2.y == it.y && mbdy2 != snake2[snake2.size()-1]){
					gameOver = true
						loser = "Player 2"
				}
			}
				
			snake.add(0,mbdy)
			snake2.add(0,mbdy2)
				
			if(!gotFood(snake)){
				snake.remove(snake.size()-1)
			}else{
				setFood()
                                clpx()
				score++
			}
			if(!gotFood(snake2)){
				snake2.remove(snake2.size()-1)
			}else{
				setFood()
                                clpx()
				score2++
			}
			t=0
		}
		move()
	}
	
	void render(){	
		if(gameOver){
			if(!first){
				text("Game Over, \n"+loser+" ate themselves!", 0, 10, 10)
				text("Final Score, p1:" + score + " , p2: "+score2, 0, 30, 10)
			}else{
				text("Snek 2 Clone!", 0, 20, 10)
			}
			text("Press `START` to play", 0, 40, 10)
		}else{
			bgColor(0.1f,0.1f,0.1f)
					
			drawSneks()
			drawColor(11)
			rect((food.x*8), (food.y*8), 8, 8)
			
			text("p1: "+score, 0, 0, 10)
			text("p2: "+score2, 0, 150, 10)
		}
	}	
}
