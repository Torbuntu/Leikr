import leikr.Engine
class Controllers extends Engine {
	
	int buttons = 12
	int index = 0
	def isSet = [:]
	def settings = [:]
    void create(){
    	isSet = [left: false, right: false, up: false, down: false, lb: false, rb: false, select: false, start: false, a:false, b:false, x:false, y:false]
    }
    void update(float delta){}
    void render(){	
        bgColor(16)
        
        text("Button " + isSet[index], 0, 8, 232, 1) 
        
    }	
}
