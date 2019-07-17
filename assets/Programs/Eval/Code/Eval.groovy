class Eval extends leikr.Engine {
	
	def result
    void create(){        
        loadLib("out/")
        result = newInstance("Test").get()        
    }
    
    void render(){	
    	//result should be 10
		text("$result", 0, 1, 32)
    }
    
    
    void update(float delta){}
}

