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
    def lvl2start = false
	
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
    	wiz = [x: 10, y: 100, vx: 0, vy: 0, w:8, h:8, spid: 0, f: false, g: true, jmsid: 4,
            jumping: false, s: false, walkAnim: [1,2,3,0], waIndex: 0, 
            waTime: 0, left: "Left", right: "Right", up: "Space", down:"Down", charged: false, chargedTime: 0, cf: false,
            scrolls: 0]
    			
    	bolt = [x:0, y:0, vx:0, w:8, h:8, spid: 0, spids: [5,6,7,8], charge: 0, attack: false, hit: false]
    	
    	enemy1 = [x: 72, y: 72, w:8, h:8, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
    			
    	loadMap("intro")
    }
    
    void initlvl1(){
        wiz.x = 16
        wiz.y = 150
        
    	enemy1 = [x: 208, y: 64, vs: 0.2, w: 8, h: 8, f: false, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
        
        enemy2 = [x: 64, y: 80, w: 8, h: 8, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
        
    	loadMap("lvl1")
    }
    
    void initlvl2(){
        wiz.x = 0
        wiz.y = 16
        
    	enemy1 = [x: 72, y: 96, vs: 0.2, w: 8, h: 8, f: false, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
        
        enemy2 = [x: 152, y: 72, vs: 0.2, w: 8, h: 8, f: false, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
        
        enemy3 = [x: 208, y: 72, vs: 0.2, w: 8, h: 8, f: false, spid: 10, alive: true, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0]
        
    	loadMap("lvl2")
    }
    
    
    void movep(){        
        wiz.waTime++
        wiz.vx = 0
        wiz.spid = 0
        //check left or right moving
        if((key(wiz.left) || button(BTN.LEFT)) && !solid(wiz.x-1, wiz.y) && !solid(wiz.x-1, wiz.y+7)){		
            wiz.vx =-1           
            wiz.f = true
            wiz.w = true
        }
        if((key(wiz.right) || button(BTN.RIGHT)) && !solid(wiz.x+9,wiz.y) && !solid(wiz.x+9,wiz.y+7)){
            wiz.vx = 1         
            wiz.f = false
            wiz.w = true
        }
        
        //check walking
        if(wiz.vx == 0){
            wiz.w = false
        }
        
        //check gravity
        if( solid(wiz.x,wiz.y+8+wiz.vy) || solid(wiz.x+7,wiz.y+8+wiz.vy)){
            wiz.vy=0
            wiz.g = true
        }else{
            wiz.vy = wiz.vy+0.2
            wiz.g = false
        }
	
        //check jumping.
        if(((key(wiz.up) || button(BTN.A)) || key("Up")) && wiz.vy==0){        	
            wiz.vy=-3.5            
            wiz.g = false 
        }	
        
        //check on ground
        if(wiz.vy < 0 && (solid(wiz.x, wiz.y-1)||solid(wiz.x+8, wiz.y-1))){
            wiz.vy = 0
        }		
        if( wiz.vy<0 && (solid(wiz.x+wiz.vx,wiz.y+wiz.vy) || solid(wiz.x+8+wiz.vx,wiz.y+wiz.vy))){
            wiz.g = true  
        }   
		
        // jumping
        if(!wiz.g){
            wiz.spid = wiz.jmsid
        }
        //standing
        if(wiz.g && !wiz.w){
            wiz.spid = 0
        }
        
        //walking
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
    	
        //action buttons
    	if(key("X") || button(BTN.Y)){
            chargep()
    	}
    	if(key("Z") || button(BTN.B)){
            attackp()
    	}
    	
    	if(ladder()){
            wiz.vy = 0
            if(key(wiz.down) || button(BTN.DOWN) || key("Down") ){
                wiz.vy = 0.6
            }
            if(key(wiz.up) || button(BTN.UP) || key("Up")){
                wiz.vy = -0.6
            }            
        }	
        
        if(bolt.charge == 30){
            wiz.vx = wiz.vx + (wiz.vx / 2)
        }
		
        wiz.x=wiz.x+wiz.vx
        wiz.y=wiz.y+wiz.vy
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
    	bolt.spid = bolt.spids[(bolt.charge/10).toInteger()]
    }
    
    void attackb(){
    	if((bolt.f && solid(bolt.x, bolt.y) || solid(bolt.x, bolt.y+7)) || (bolt.f && solid(bolt.x+8, bolt.y) || solid(bolt.x+8, bolt.y+7))){
            bolt.attack = false
            bolt.charge = 0
    	}
        if(bolt.x > 240 || bolt.x < 0){
            bolt.attack = false
            bolt.charge = 0
        }
    	bolt.x = bolt.x + bolt.vx
    }
    
    void boltHitEnemy(enemy){
        if(collide(bolt, enemy)){
            bolt.attack = false
            bolt.charge = 0
            enemy.alive = false
    	}
    }
    
    void checkScroll(){
        int tile = getTile(wiz.x+2, wiz.y+2)
        if( tile == 29 || tile == 30 || tile == 31){
            mapSet((wiz.x+2)/8, (wiz.y+2)/8, 0)
            wiz.scrolls++
        }
    }
    
    void enemyAnimation(enemy){
        enemy.animTime++      
        if(enemy.alive){   
            enemy.spid = enemy.walkAnim[enemy.waIndex] 		
            if(enemy.animTime > 4){
                enemy.waIndex++
                enemy.animTime = 0
            }    	
    	}
    	if(enemy.waIndex > 3){
            enemy.waIndex = 0
    	}
    }
    
    //assumes x,y,w,h on each object
    boolean collide(a,b){
        return a.x < b.x + b.w && a.x + a.w > b.x && a.y < b.y + b.h && a.y + a.h > b.y
    }
    
    void lvl1Update(){
    	if(!lvl1start){
            initlvl1()
            lvl1start = true    		
    	}    	
        if(wiz.x>236){
            level++
        }
        
        enemyAnimation(enemy1)
        enemyAnimation(enemy2)
        if(enemy1.alive){
            if(enemy1.x < 208 || enemy1.x > 224){
                enemy1.vs = -enemy1.vs
                enemy1.f = !enemy1.f
            }
            enemy1.x = enemy1.x + enemy1.vs            
        }
        boltHitEnemy(enemy1)
        boltHitEnemy(enemy2)
        if(!enemy1.alive){
            mapSet(enemy1.x/8, enemy1.y/8, 88)
            mapSet(28,1,0)
            mapSet(28,2,0)
        }
        if(!enemy2.alive){
            mapSet(enemy2.x/8, enemy2.y/8, 88)
            mapSet(29,1,0)
            mapSet(29,2,0)
        }
    }
    
    void lvl2Update(){
    	if(!lvl2start){
            initlvl2()
            lvl2start = true    		
    	}    	
        if(wiz.y>160){
            level++
        }
        
        enemyAnimation(enemy1)
        enemyAnimation(enemy2)
        enemyAnimation(enemy3)
        if(enemy1.alive){
            if(enemy1.x < 72 || enemy1.x > 88){
                enemy1.vs = -enemy1.vs
                enemy1.f = !enemy1.f
            }
            enemy1.x = enemy1.x + enemy1.vs            
        }
        if(enemy2.alive){
            if(enemy2.x < 152 || enemy2.x > 168){
                enemy2.vs = -enemy2.vs
                enemy2.f = !enemy2.f
            }
            enemy2.x = enemy2.x + enemy2.vs            
        }
        if(enemy3.alive){
            if(enemy3.x < 208 || enemy3.x > 224){
                enemy3.vs = -enemy3.vs
                enemy3.f = !enemy3.f
            }
            enemy3.x = enemy3.x + enemy3.vs            
        }
        boltHitEnemy(enemy1)
        boltHitEnemy(enemy2)
        boltHitEnemy(enemy3)
        if(!enemy1.alive){
            mapSet(enemy1.x/8, enemy1.y/8, 88)
            mapSet(27,17,0)
            mapSet(28,17,0)
        }
        if(!enemy2.alive){
            mapSet(enemy2.x/8, enemy2.y/8, 88)
            mapSet(27,18,0)
            mapSet(28,18,0)
        }
        if(!enemy3.alive){
            mapSet(enemy3.x/8, enemy3.y/8, 88)
            mapSet(27,19,0)
            mapSet(28,19,0)
        }
    }
  
    void create(){		
        loadImages()
        rand = new Random()
        loadMap("title")
    }

    void update(float delta){
    	if(title){
            if(key("X") || key("Space") || button(BTN.START)){
                title = false
                init()
            }
            return
    	}    
    	
    	movep()
    	
        checkScroll() 
    	
    	switch(level){    	
        case 1:
            lvl1Update()
            break;
        case 2:
            lvl2Update()
            break;
        default:    			    	
            if(wiz.y < 0){
                level++
            }
            enemyAnimation(enemy1)
            boltHitEnemy(enemy1)
            if(!enemy1.alive){
                mapSet(enemy1.x/8, enemy1.y/8, 88)
                mapSet(1,0,33)
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
            sprite(enemy1.spid, enemy1.x, enemy1.y, enemy1.f, false)
    	}
    	if(enemy2.alive){
            sprite(enemy2.spid, enemy2.x, enemy2.y)
    	}
    }	
    void renderlvl2(){        
    	if(enemy1.alive){
            sprite(enemy1.spid, enemy1.x, enemy1.y, enemy1.f, false)
    	}
    	if(enemy2.alive){
            sprite(enemy2.spid, enemy2.x, enemy2.y)
    	}
        if(enemy3.alive){
            sprite(enemy3.spid, enemy3.x, enemy3.y, enemy3.f, false)
    	}
    }	
	
    void render(){
        bgColor(0.1f,0.1f,0.1f)
        map()	
        if(key("S")){
            text("Scrolls: "+wiz.scrolls, 46, 32, 1)
        }
		
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
        case 2:
            renderlvl2()
            break;
        default:
            renderDefault()
            break;
		
        }
    }
}
