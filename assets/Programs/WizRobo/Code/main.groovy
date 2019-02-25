import leikr.Engine
class WizRobo extends Engine {
	
    def wiz = [:]	
	def bolt = [:]
	
	def enemy1 = [:]
	def enemy2 = [:]
	def enemy3 = [:]
	def enemy4 = [:]
	
	def title = true
	def level = 0
	def lvl1start = false
	
    def i = [:]
    
    def time = 0
			
    Random rand
    
    def solid(x,y){
        float mx = (x)/8 
        float my = (y)/8
        int cellid = mapGet(mx,my)
        if(	cellid > 60 ){
            return true
        }
        return false
    }	  
	
    def getTile(x,y){
        float mx = (x)/8 
        float my = (y)/8
        return mapGet(mx,my)
    }

    
    void init(){
    	wiz = [x: 10, y: 100, vx: 0, vy: 0, spid: 0, f: false, g: true, jmsid: 4,
    			jumping: false, s: false, walkAnim: [1,2,3,0], waIndex: 0, 
    			waTime: 0, left: "Left", right: "Right", up: "Up", down:"Down", charged: false, chargedTime: 0, cf: false ]
    			
    	bolt = [x:0, y:0, vx:0, spid: 0, spids: [5,6,7,8], charge: 0, attack: false, hit: false]
    	
    	enemy1 = [x: 72, y: 72, spid: 10, alive: true]
    			
    	loadMap("intro")
    }
    
    void initlvl1(){
    	wiz = [x: 16, y: 150, vx: 0, vy: 0, spid: 0, f: false, g: true, jmsid: 4,
    			jumping: false, s: false, walkAnim: [1,2,3,0], waIndex: 0, 
    			waTime: 0, left: "Left", right: "Right", up: "Up", down:"Down", charged: false, chargedTime: 0, cf: false ]
    			
    	bolt = [x:0, y:0, vx:0, spid: 0, spids: [5,6,7,8], charge: 0, attack: false, hit: false]
    	
    	loadMap("lvl1")
    }
    
    
    void movep(){        
        	
        wiz.vx = 0
        wiz.spid = 0
        if(key(wiz.left) && !solid(wiz.x-1, wiz.y) && !solid(wiz.x-1, wiz.y+7)){		
            wiz.vx =-1           
            wiz.f = true
            wiz.w = true
        }
        if(key(wiz.right) && !solid(wiz.x+9,wiz.y) && !solid(wiz.x+9,wiz.y+7)){
            wiz.vx = 1         
            wiz.f = false
            wiz.w = true
        }
        if(wiz.vx == 0){
            wiz.w = false
        }
        if( solid(wiz.x,wiz.y+8+wiz.vy) || solid(wiz.x+8,wiz.y+8+wiz.vy)){
            wiz.vy=0
            wiz.g = true
        }else{
            wiz.vy = wiz.vy+0.2
            wiz.g = false
        }
		
        if(key(wiz.up) && wiz.vy==0){        	
        	wiz.vy=-3.5            
            wiz.g = false 
        }	
        if(key(wiz.down) && ladder()){
        	wiz.vy = 1
        }
		if(wiz.vy < 0 && (solid(wiz.x, wiz.y-1)||solid(wiz.x+8, wiz.y-1))){
			wiz.vy = 0
		}
		
        if( wiz.vy<0 && (solid(wiz.x+wiz.vx,wiz.y+wiz.vy) || solid(wiz.x+8+wiz.vx,wiz.y+wiz.vy))){
            wiz.g = true  
        }   
		
     
        if(!wiz.g){
            wiz.spid = wiz.jmsid
        }
        
        if(wiz.g && !wiz.w){
            wiz.spid = 0
        }
		
        if(wiz.w && wiz.g){   
    		wiz.spid = wiz.walkAnim[wiz.waIndex] 		
    		if(wiz.waTime > 8){
    			wiz.waIndex++
    			wiz.waTime = 0
    		}    	
    	}
    	if(wiz.waIndex > 3){
    		wiz.waIndex = 0
    	}
    	
    	if(key("X")){
    		chargep()
    	}
    	if(key("Z")){
    		attackp()
    	}
    	
    	if(ladder()){
            
            if(key(wiz.down)){
                wiz.vy = 0.6
            }
            if(key(wiz.up)){
                wiz.vy = -0.6
            }
        }	
		
        wiz.x=wiz.x+wiz.vx
        wiz.y=wiz.y+wiz.vy
    }
    
    def ladder(){
    	if(getTile(wiz.x, wiz.y+8+wiz.vy) == 33 || getTile(wiz.x+8, wiz.y+8+wiz.vy) == 33 ){
    		return true
    	}else{
    		return false
    	}
    }
    
    void chargep(){
    	if(bolt.attack){
    		return
    	}	
    	if(bolt.charge < 30){
    		bolt.charge++
    	}else{
    		wiz.charged = true
    	}
    }
    void attackp(){
    	if(bolt.attack){
    		return
    	}
    	wiz.charged = false
    	bolt.attack = true
    	bolt.f = wiz.f
    	if(wiz.f){
    		bolt.vx = -3   		
    	}else{
    		bolt.vx = 3
    	}
    	bolt.x = wiz.x
    	bolt.y = wiz.y
    	bolt.spid = bolt.spids[bolt.charge/10.toInteger()]
    }
    
    void attackb(){
    	if((bolt.f && solid(bolt.x, bolt.y) || solid(bolt.x, bolt.y+7)) || (bolt.f && solid(bolt.x+8, bolt.y) || solid(bolt.x+8, bolt.y+7))){
    		bolt.attack = false
    		bolt.charge = 0
    	}
    	
    	if(bolt.x >= enemy1.x && bolt.x+4 <= enemy1.x+8 && bolt.y >= enemy1.y && bolt.y + 4 <= enemy1.y+8){
    		bolt.attack = false
    		bolt.charge = 0
    		enemy1.alive = false
    	}
    	bolt.x = bolt.x + bolt.vx
    }
    
    void lvl1Update(){
    	if(!lvl1start){
    		initlvl1()
    		lvl1start = true    		
    	}
    	
    }
  
    void create(){		
        loadImages()
        rand = new Random()
        loadMap("title")
    }

    void update(float delta){
    	if(title){
    		if(key("X") || key("Space")){
    			title = false
    			init()
    		}
    		return
    	}
    
    	wiz.waTime++
    	movep()
    	if(bolt.attack){
    		attackb()
    	}
    	if(wiz.charged){
    		wiz.chargedTime++
    		if(wiz.chargedTime > 2){
    			wiz.chargedTime = 0  
    			wiz.cf = !wiz.cf  			
    		}
    	}
    	
    	switch(level){    	
    		case 1:
    			lvl1Update()
    			break;
    		default:    			    	
				if(wiz.y < 0){
					level++
				}
				if(!enemy1.alive){
    				mapSet(enemy1.x/8, enemy1.y/8, 88)
    			}
				break;    			
    	}

    }
    
    void renderDefault(){
    	if(enemy1.alive){
    		sprite(enemy1.spid, enemy1.x, enemy1.y)
		}
    }
    void renderlvl1(){
    	if(enemy1.alive){
    		sprite(enemy1.spid, enemy1.x, enemy1.y)
    	}
    	if(enemy2.alive){
    		sprite(enemy2.spid, enemy2.x, enemy2.y)
    	}
    }	
	
    void render(){
        bgColor(0.1f,0.1f,0.1f)
		map()	
		
		if(title){
			text("Escape the dungeon!", 46, 32, 1)
			return
		}
			
		if(wiz.charged){
			sprite(9, (wiz.x).toFloat(), (wiz.y-8).toFloat(), wiz.cf, false)
		}
		sprite(wiz.spid, wiz.x.toFloat(), wiz.y.toFloat(), wiz.f, false)
		if(bolt.attack){
			sprite(bolt.spid, bolt.x, bolt.y, bolt.f, false)
		}
		switch(level){
			case 1:
				renderlvl1()
				break;
				
			default:
				renderDefault()
				break;
		
		}
    }
}
