import leikr.Engine
class SnekClone extends Engine {
	
	def dirs = [:]
	def direction = [:]
	Random rand
	def t = 0
	def score = 0
	
	def food = [:]
	def snake = []
	
	boolean gotFood(){
		return snake[0].x == food.x && snake[0].y == food.y
	}
	
	void setFood(){
		food.x = rand.nextInt(29)
		food.y = rand.nextInt(16)
		snake.each{
			if (it.x == food.x && it.y == food.y){
				setFood()
			}
		}
	}
	
	void move(){
		def last_dir = direction
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
		if(snake[0].x+direction.x == snake[1].x && snake[0].y+direction.y==snake[1].y){
			direction = last_dir
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
		
		food.x = 0
		food.y = 0
		direction = dirs.up
		setFood()
	}

	void update(float delta){
		t+=delta
	}
	
	void render(){
		
		bgColor(0.1f,0.1f,0.1f)
		def mbdy = [:]
		if(t>0.1){
			
			mbdy.x = (snake[0].x+direction.x)%30
			mbdy.y = (snake[0].y+direction.y)%17
			snake.add(0,mbdy)
			
			if(!gotFood()){
				snake.remove(snake.size()-1)
			}else{
				setFood()
				score++
			}
			t=0
		}
		move()
				
		//draw snake
		snake.each{
			square((it.x*8).toFloat(), (it.y*8).toFloat(),8,8,9)
		}		
		//draw food
		square((food.x*8).toFloat(), (food.y*8).toFloat(), 8, 8, 11)
		
		text(direction.x + " : "+direction.y, 0, 10, 10)
		text(mbdy.x + " : " + mbdy.y, 0, 20, 10)
		text(snake[0].x + " : " + snake[0].y, 0, 40, 10)
		text("Score: "+score, 0, 30, 10)
	}	
}
