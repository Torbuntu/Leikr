import groovy.transform.CompileStatic
@CompileStatic
class Wizard{
    float x = 10 
    float y = 100 
    float vx = 0 
    float vy = 0 
    float width = 8 
    float height = 8 
    int spid = 0 
    boolean f = false 
    boolean g = true 
    int jmsid = 4
    boolean jumping = false
    boolean s = false 
    int[] walkAnim = [1,2,3,0] 
    int waIndex = 0 
    boolean walking = false
    int waTime = 0
    String left = "Left"
    String right = "Right" 
    String up = "Space" 
    String down ="Down" 
    boolean charged = false 
    int chargedTime = 0 
    boolean cf = false
    int scrolls = 0 
    int health = 3
    
    def setWizard(float x, float y, float vx, float vy, float width, float height,
    int spid, boolean f, boolean g, int jmsid, boolean jumping, boolean s, int[] walkAnim,
    int waIndex, boolean walking, int waTime, String left, String right, String up, String down,
    boolean charged, int chargedTime, boolean cf, int scrolls, int health){
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
