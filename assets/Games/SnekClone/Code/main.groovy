//With help from: https://github.com/nesbox/TIC-80/wiki/Snake-Clone-tutorial
import leikr.Engine
class SnekClone extends Engine {

	def dirs = [:]
	def direction = [:]
	def food = [x:0,y:0]
	def snake = []
	def mbdy = [:]
	def head = [x:14, y:8]		
	def bdy = [x:14, y:9]
	def tail = [x:14, y:10]
	def gameOver = true
	def first = true
	def t = 0
	def score = 0
	Random rand	
	
	void newGame(){
	 	score = 0
	 	t = 0
	 	snake = []
		snake.addAll([head, bdy, tail])
		direction = dirs.up
		setFood()
	}
	
	boolean gotFood(snek){
		return snek[0].x == food.x && snek[0].y == food.y
	}
	
	void setFood(){
		food = [x: rand.nextInt(29), y:rand.nextInt(19)]
				
		snake.each{
			if (it.x == food.x && it.y == food.y){
				setFood()
			}
		}
	}
	
	void move(){
		def last_dir = direction
		
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
		
		if(snake[0].x+direction.x == snake[1].x && snake[0].y+direction.y==snake[1].y){
			direction = last_dir
		}
	}
	
	void drawSneks(){
		snake.each{
			sprite(0, (it.x*8), (it.y*8))
			//square((it.x*8), (it.y*8),8,8)
		}
	}
	
	void create(){				
		rand = new Random()
		dirs.up = [x:0, y:-1]		
		dirs.down = [x:0, y:1]
		dirs.right = [x:1, y:0]
		dirs.left = [x:-1, y:0]		
		newGame()
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
				
			snake.each{
				if(mbdy.x == it.x && mbdy.y == it.y && mbdy != snake[snake.size()-1]){
					gameOver = true
				}
			}
				
			snake.add(0,mbdy)
				
			if(!gotFood(snake)){
				snake.remove(snake.size()-1)
			}else{
				setFood()
				score++
			}
			t=0
		}
		move()
	}
	
	void render(){	
		if(gameOver){
			if(!first){
				text("Game Over, Snek ate themself!", 0, 10, 10)
				text("Final Score: " + score, 0, 20, 10)
			}else{
				text("Snek Clone!", 0, 20, 10)
			}
			text("Press `START` to play", 0, 30, 10)
		}else{
			bgColor(0.1f,0.1f,0.1f)
					
			drawSneks()
			drawColor(11)
			square((food.x*8), (food.y*8), 8, 8)
			
			text("Score: "+score, 0, 0, 10)
		}
	}	
}
