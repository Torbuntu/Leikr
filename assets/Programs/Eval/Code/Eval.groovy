class Eval extends leikr.Engine {
	
	def result
		
    void create(){   
    	compile("lib", "out")
    	loadLib("out")
    	result = newInstance("Test")
    }
 
    void update(float delta){
    	if(key("Space")){
    		result = compile("lib/Test").getTest()
    	}
    }
    
    void render(){	
    	//result should be 10 and "Hello, Help!"
		text("$result", 0, 1, 32)
    }
}

