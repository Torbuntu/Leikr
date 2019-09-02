import groovy.transform.CompileStatic
@CompileStatic
class Enemy{
	float x = 72 
	float y = 72 
	float width =8 
	float height =8 
	boolean f =false 
	int spid = 10 
	boolean alive = true 
	boolean remove = false 
	int animTime = 0 
	int[] walkAnim = [10, 11, 12, 13] 
	int waIndex = 0 
	Map keyA = [x: 1, y: 1] 
	Map keyB = [x: 1, y: 1]
	float vs
	int l
	int r
	int health
	
	def Enemy(){
	
	}
	
	def Enemy(float x, float y, float vs, int l, int r, Map keyA, Map keyB){
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
	
	Enemy(float x, float y, float vs, int l, int r, float width, float height, boolean f,
	int spid, boolean alive, boolean remove, int animTime, int[] walkAnim, int waIndex, Map keyA, Map keyB){
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
	
	Enemy(float x, float y, float vs, int l, int r, float width, float height, boolean f,
	int spid, boolean alive, boolean remove, int animTime, int[] walkAnim, int waIndex, Map keyA, Map keyB, int health){
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
