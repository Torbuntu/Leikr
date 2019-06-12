class Enemy{
	def x = 72 
	def y = 72 
	def width =8 
	def height =8 
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
	
	def Enemy(x, y, vs, l, r, keyA, keyB){
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
	
	def Enemy(x, y, vs, l, r, width, height, f, spid, alive, remove, animTime, walkAnim, waIndex, keyA, keyB){
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
	
	def Enemy(x, y, vs, l, r, width, height, f, spid, alive, remove, animTime, walkAnim, waIndex, keyA, keyB, health){
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
}
