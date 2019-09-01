class SpriteViewer extends leikr.Engine {
	int idx = 0, sx=156, sy=108, ss=0
	int mx = 0, my = 0
	int vx = 220, vy = 140
	int t = 0
	int mode = 0 //0 = display, 1 = edit, 2 = start edit, 3 = finish edit, 4 = play+stop
	String sheetName = "SpriteViewer"
	int exitX = 229, exitY = 4, exitS = 7
	
	//For animation start and finish variables.
	// st and fn are the String reps of the integers, they get
	// converted when Enter is pressed after typing some numbers.
	int start=0, fin=0
	String st = "", fn = ""
	int animSpeed = 30
	
	
	@Override
    public boolean keyTyped(char c) {
    	if(mode == 1){
    		try{
    			if ((int)c == 8 && sheetName.length() > 0) {
                    sheetName = sheetName.substring(0, sheetName.length() - 1);
                }
    			if ((int) c > 64 && (int) c < 127) {
                    sheetName = sheetName + c;
                }
    		}catch(Exception ex){
    			println "char error: "+ex.setStackTrace()
    			ex.printStackTrace()
    		}
    	}
    	//48 - 57 : numbers 0 to 9
    	if((int)c >= 48 && (int)c <= 57){
			if(mode == 2){
				try{
					st += c
				}catch(Exception ex){
					println "Parse int error on start"
				}
			}
			if(mode == 3){
				try{
					fn += c
				}catch(Exception ex){
					println "Parse int error on finish"
				}
			}
    	}
        return true;
    }
	
    void create(){
        loadImages()
    }
    void update(float delta){
    	t++
        mx = mouseX() as int
        my = mouseY() as int
        
        if(keyPress("E")){
        	if(mode != 1) {
        		sheetName = ""
        		mode = 1//enter edit mode
        		return
        	}        	
        }
        
        if(keyPress("Enter") ){
        	if(mode == 1) loadSpriteSheet(sheetName)
        	if(mode == 2){
        		try{
        			start = Integer.parseInt(st)        
        		}catch(Exception ex){
        			st = ""
        			start = 0
        		}		
        	} 
        	if(mode == 3){
        		try{
        			fin = Integer.parseInt(fn)
        		}catch(Exception ex){
        			fin = 0
        			fn = ""
        		}
        	}
        	
        	mode = 0//exit edit mode
        }
        
        //idle mode
        if(mode == 0){
        	if(keyPress("S")) {
        		mode = 2//set start mode
        		start = 0
        		st = ""
        	}
        	if(keyPress("F")) {
        		mode = 3//set finish mode
        		fin = 0
        		fn = ""
        	}        	
        }
        
        //mode 4 is play animation mode
        if(mode == 4){
        	if(t>animSpeed){
        		if(idx >= fin) {
        			idx = start
        		}else{
        			idx++
        		}
        		t = 0
        	} 
        }
              
        if(keyPress("Right") || button("RIGHT") && t > 20){
        	if (ss < 3) ss++
        	t=0
        }
        if(keyPress("Left") || button("LEFT") && t > 20){
        	if (ss > 0) ss--
        	t=0
        }
        if(keyPress("Up") || button("UP") && t > 20){
        	idx++
        	t=0
        }
        if(keyPress("Down") || button("DOWN") && t > 20){
        	if(idx > 0) idx--
        	t=0
        }
        if(keyPress("Space")){
        	println "Space"
        	if(mode == 4) {
        		mode = 0	
        	}else{
        		mode = 4
        	}
        }
        
        if(keyPress("Q")) {
        	if(animSpeed > 1) animSpeed --
        }
        if(keyPress("W")){
        	if(animSpeed < 30) animSpeed ++
        }
        
        if(mouseClick()){
        	//exit
        	if(point(mx, my, exitX, exitY, exitS, exitS)) pause()
        	//ss
        	if(my >= 136 && my <= 144){
        		if(mx >= 220 && mx <= 222) ss = 0
        		if(mx >= 223 && mx <= 225) ss = 1
        		if(mx >= 226 && mx <= 228) ss = 2
        		if(mx >= 229 && mx <= 231) ss = 3
        	}
        	//idx 
        	if(mx >= 205 && mx <= 212){
    			if(my >= 127 && my <= 133 && t > 20) {
    				idx++
    				t=0
    			}
    			if(my >= 138 && my <= 144 && t > 20) {
    				idx--
    				t=0
    			}
    		}  
        }
        
        switch(ss){
        	case 0:
        		vx = 220
        		vy = 140
        		sx = 156
        		sy = 108
        		break;
        	case 1:
        		vx = 223
        		vy = 138
        		sx = 152
        		sy = 104
        		break;
    		case 2:
        		vx = 226
        		vy = 136
        		sx = 144
        		sy = 96
        		break;
    		case 3:
        		vx = 229
        		vy = 134
        		sx = 128
        		sy = 80
        		break;
        	default:
        		vx = 220
        		vy = 140
        		sx = 156
        		sy = 108
        		break;
        }             
    }
    void render(){	
    	
    	//Sprite, red X for index out of bounds on IDX
    	try{
			sprite(idx,sx,sy,ss)
    	}catch(Exception ex){
    		text("X", 156, 108, 23)
    	}
    	//Text display
    	text("ID: $idx", 16, 32, 32)
    	text("Size: $ss", 16, 48, 32)
    	if(mode != 1){
    		text("Sheet: $sheetName", 16, 64, 32)
    	}else if(mode == 1){
    		text("Sheet: $sheetName", 16, 64, 30)
    	}
    	if(mode != 2){
    		text("Start: $start", 16, 80, 32)
    	}else if(mode == 2){
    		text("Start: $start", 16, 80, 30)
    	}
    	
    	if(mode != 3){
    		text("Fin: $fin", 16, 96, 32)
    	} else if(mode == 3){
    		text("Fin: $fin", 16, 96, 30)
    	}
    	
    	text("A-Speed: $animSpeed", 16, 128, 32)
    	
    	switch(mode){
    		case 0:
    			text("Mode: idle", 16, 112, 32)
    			break;
			case 1:
    			text("Mode: sheet edit", 16, 112, 32)
    			break;
			case 2:
    			text("Mode: start edit", 16, 112, 32)
    			break;
			case 3:
    			text("Mode: fin edit", 16, 112, 32)
    			break;
			case 4:
    			text("Mode: play", 16, 112, 32)
    			break;
    		default:
    		
    			break;
    	}
    
    	//Background image
		image("BG", 0,0)
		drawColor(6)
		rect(vx, vy, 1, 1)
		//Mouse
		drawColor(5)
		line(mx, my, mx+4, my + 5)
    }
}

