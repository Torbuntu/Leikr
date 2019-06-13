class Wizard{
	def x = 10 
	def y = 100 
	def vx = 0 
	def vy = 0 
	def width = 8 
	def height = 8 
	def spid = 0 
	def f = false 
	def g = true 
	def jmsid = 4
	def jumping = false
	def s = false 
	def walkAnim = [1,2,3,0] 
	def waIndex = 0 
	def walking = false
    def waTime = 0
    def left = "Left"
    def right = "Right" 
    def up = "Space" 
    def down ="Down" 
    def charged = false 
    def chargedTime = 0 
    def cf = false
    def scrolls = 0 
    def health = 3
    
    def setWizard(x,y,vx,vy,width,height,spid,f,g,jmsid,jumping,s,walkAnim,waIndex,walking,waTime,left,right,up,down,charged,chargedTime,cf,scrolls,health){
    	this.x = x
    	this.y = y
    	this.vx = vx
    	this.vy = vy
    	this.width = width
    	this.height = height
    	this.spid = spid
    	this.f = f
    	this.g = g
    	this.jmsid = jmsid
    	this.jumping = jumping
    	this.s = s
    	this.walkAnim = walkAnim
    	this.waIndex = waIndex
    	this.waTime = waTime
    	this.left = left
    	this.right = right
    	this.up = up
    	this.down = down
    	this.charged = charged
    	this.chargedTime = chargedTime
    	this.cf = cf
    	this.scrolls = scrolls
    	this.health = health
    }
}
