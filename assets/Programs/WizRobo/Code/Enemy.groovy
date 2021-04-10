class Enemy{
	def x = 72 
	def y = 72 
	def width = 8 
	def height = 8 
	def f =false 
	def spid = 10 
	def alive = true 
	def remove = false 
	def animTime = 0 
	def walkAnim = [10, 11, 12, 13] 
	def waIndex = 0 
	def keyA = [x: 1, y: 1] 
	def keyB = [x: 1, y: 1]
	def vs
	def l
	def r
	def health
	
	def Enemy(){
	
	}
	
	def Enemy(def x, def y, def vs, def l, def r, def keyA, def keyB){
		this.x = x
		this.y = y
		this.vs = vs
		this.l = l
		this.r = r
		this.keyA = keyA
		this.keyB = keyB
		
		width = 8
		height = 8
		f = false
		spid = 10
		alive = true
		remove = false
		animTime = 0
		walkAnim = [10,11,12,13]
		waIndex = 0
	}
	
	Enemy(def x, def y, def vs, def l, def r, def width, def height, def f,
	def spid, def alive, def remove, def animTime, def walkAnim, def waIndex, def keyA, def keyB){
		this.x = x
		this.y = y
		this.vs = vs
		this.l = l
		this.r = r
		this.width = width
		this.height = height
		this.f = f
		this.spid = spid
		this.alive = alive
		this.remove = remove
		this.animTime = animTime
		this.walkAnim = walkAnim
		this.waIndex = waIndex
		this.keyA = keyA
		this.keyB = keyB
	}
	
	Enemy(def x, def y, def vs, def l, def r, def width, def height, def f,
	def spid, def alive, def remove, def animTime, def walkAnim, def waIndex, def keyA, def keyB, def health){
		this.x = x
		this.y = y
		this.vs = vs
		this.l = l
		this.r = r
		this.width = width
		this.height = height
		this.f = f
		this.spid = spid
		this.alive = alive
		this.remove = remove
		this.animTime = animTime
		this.walkAnim = walkAnim
		this.waIndex = waIndex
		this.keyA = keyA
		this.keyB = keyB
		this.health = health
	}
	
	void moveEnemy(){
        if(alive){
            if(x < l || x > r){
                vs = -vs
                f = !f
            }
            x = x + vs            
        }
    }
    
	void enemyAnimation(){
		animTime++      
		if(alive){   
			spid = walkAnim[waIndex] 		
			if(animTime > 4){
				waIndex++
				animTime = 0
			}    	
		}
		if(waIndex > 3){
			waIndex = 0
		}
	}
}
