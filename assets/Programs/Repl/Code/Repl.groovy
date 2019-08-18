class Repl extends leikr.Engine {
	def failed = false
	
	String tmp = ""
	def lineList = []
	def command
	def args = []
	
	def color = 1
	
	@Override
	public boolean keyTyped(char character){
		if((int)character >= 32 && (int) character <= 126){
			tmp += character
		}
			
		if((int)character == 8 && tmp.length() > 0) {
			tmp = tmp.substring(0, tmp.length() -1)
		}
		
		if((int)character == 13) {
			try{			
				if(tmp == "print") {
					tmp = ""				
					println lineList
					return
				}
				if(tmp == "clear"){
					tmp = ""
					lineList = []
					return
				}
				lineList += tmp				
				tmp = ""
			}catch(Exception ex){
				println(ex)
			}
			failed = false
		}
	}
	
    void create(){
		loadImages()   
    }
    void update(float delta){
        
    }
    void render(){	
    
    	if(!failed){
			try{
				lineList.each{
					command = it.substring(0, it.indexOf("("))				
					args = it.substring(it.indexOf("(")+1, it.indexOf(")")).split(", ")				
					this.invokeMethod(command, args as Object[])
				}
			
			}catch(Exception ex){
				println(ex)
				failed = true
			}
		}
		
    	text(tmp, 0, 0, 240, color)
    }
    
    
    
    def text(Object[] arg){
    	switch(arg.size()){
    		case 4:
    		text(arg[0] as String, arg[1] as float, arg[2] as float, arg[3] as int)
    		break
    		case 5:
    		text(arg[0] as String, arg[1] as float, arg[2] as float, arg[3] as float, arg[4] as int)
    		break
    	}    	
    }
    
    
    
    
    
    //Image drawing
    void image(Object[] arg){
    	switch(arg.size()){
    		case 3:
    			image(arg[0] as String, arg[1] as float, arg[2] as float)
    		break
    		case 5:
    			image(arg[0] as String, arg[1] as float, arg[2] as float, arg[3] as float, arg[4] as float)
    		break
    		case 6:
    			image(arg[0] as String, arg[1] as float, arg[2] as float, arg[3] as float, arg[4] as float, arg[5] as boolean)
    		break
    	}
    }
    
    //Shape Drawing
    void drawColor(Object[] arg){
    	println "new color: $arg"
    	color = arg[0]
    	drawColor(arg[0] as int)
    }
    
    def pixel(Object[] arg){
    	switch(arg.size()){
    		case 3:
    			pixel(arg[0] as int, arg[1] as int, arg[2] as int)
    		break
    	}
    }
    
    def rect(Object[] arg){
    	switch(arg.size()){
    		case 4:
    			rect(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int)
    		break
    		case 5:
    		    rect(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int, arg[4] as boolean)
    		break
    	}
    }
    
    void circle(Object[] arg){
    	switch(arg.size()){
    		case 3:
    			circle(arg[0] as int, arg[1] as int, arg[2] as int)
    		break
    		case 4:
    			circle(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as boolean)
    		break
    	}
    }
    
    void triangle(Object[] arg){
    	switch(arg.size()){
    		case 6:
    			triangle(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int, arg[4] as int, arg[5] as int)
    		break
    		case 7:
    			triangle(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int, arg[4] as int, arg[5] as int, arg[6] as boolean)
    		break
    	}
    }
    
    void line(Object[] arg){
    	line(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int)
    }
    
    void clip(Object[] arg){
    	switch(arg.size()){
    		case 0:
    			clip()
    		break
    		case 4:
    			clip(arg[0] as int, arg[1] as int, arg[2] as int, arg[3] as int)
    		break
    	}
    }
    
    
    void sprite(Object[] arg){
    	switch(arg.size()){
    		case 3:
    			sprite(arg[0] as int, arg[1] as float, arg[2] as float)
    		break
    		case 4:
    			sprite(arg[0] as int, arg[1] as float, arg[2] as float, arg[3] as int)
    		break
    		case 5:
    			sprite(arg[0] as int, arg[1] as float, arg[2] as float, arg[3] as boolean, arg[4] as boolean)
    		break
    		case 6:
    			sprite(arg[0] as int, arg[1] as float, arg[2] as float, arg[3] as boolean, arg[4] as boolean, arg[5] as int)
    		break
    	}
    }
    
}

