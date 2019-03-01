import leikr.Engine
class WizRobo extends Engine {
	
    def wizard = [:]	
    def bolt = [:]
    def enemies = []
    def enemyDeadSpids = [84,85,86]
	
    def title = true
    def level = 0
    def lvl1start = false
    def lvl2start = false
    def lvl3start = false
    def lvl4start = false
    def lvl5start = false
    def lvl6start = false
    def lvl7start = false
    def lvl8start = false
	
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
    
    void movep(){        
        wizard.waTime++
        wizard.vx = 0
        wizard.spid = 0
        //check left or right moving
        if((key(wizard.left) || button(BTN.LEFT)) && !solid(wizard.x-1, wizard.y) && !solid(wizard.x-1, wizard.y+7)){		
            wizard.vx =-1           
            wizard.f = true
            wizard.w = true
        }
        if((key(wizard.right) || button(BTN.RIGHT)) && !solid(wizard.x+9,wizard.y) && !solid(wizard.x+9,wizard.y+7)){
            wizard.vx = 1         
            wizard.f = false
            wizard.w = true
        }
        
        //check walking
        if(wizard.vx == 0){
            wizard.w = false
        }
        
        //check gravity
        if( solid(wizard.x,wizard.y+8+wizard.vy) || solid(wizard.x+7,wizard.y+8+wizard.vy)){
            wizard.vy=0
            wizard.g = true
        }else{
            wizard.vy = wizard.vy+0.2
            wizard.g = false
        }
	
        //check jumping.
        if(((key(wizard.up) || button(BTN.A)) || key("Up")) && wizard.vy==0){        	
            wizard.vy=-3.5            
            wizard.g = false 
        }	
        
        //check on ground
        if(wizard.vy < 0 && (solid(wizard.x, wizard.y-1)||solid(wizard.x+8, wizard.y-1))){
            wizard.vy = 0
        }		
        if( wizard.vy<0 && (solid(wizard.x+wizard.vx,wizard.y+wizard.vy) || solid(wizard.x+8+wizard.vx,wizard.y+wizard.vy))){
            wizard.g = true  
        }   
		
        // jumping
        if(!wizard.g){
            wizard.spid = wizard.jmsid
        }
        //standing
        if(wizard.g && !wizard.w){
            wizard.spid = 0
        }
        
        //walking
        if(wizard.w && wizard.g){   
            wizard.spid = wizard.walkAnim[wizard.waIndex] 		
            if(wizard.waTime > 8){
                wizard.waIndex++
                wizard.waTime = 0
            }    	
    	}
    	if(wizard.waIndex > 3){
            wizard.waIndex = 0
    	}
    	
        //action buttons
    	if(key("X") || button(BTN.Y)){
            chargep()
    	}
    	if(key("Z") || button(BTN.B)){
            attackp()
    	}
    	
    	if(ladder()){
            wizard.vy = 0
            if(key(wizard.down) || button(BTN.DOWN) || key("Down") ){
                wizard.vy = 0.6
            }
            if(key(wizard.up) || button(BTN.UP) || key("Up")){
                wizard.vy = -0.6
            }            
        }	
        
        if(bolt.charge == 30){
            wizard.vx = wizard.vx + (wizard.vx / 2)
        }
		
        wizard.x=wizard.x+wizard.vx
        wizard.y=wizard.y+wizard.vy
        if(bolt.attack){
            attackb()
    	}else{
            bolt.x = 0
            bolt.y = 0
        }
        
    	if(wizard.charged){
            wizard.chargedTime++
            if(wizard.chargedTime > 2){
                wizard.chargedTime = 0  
                wizard.cf = !wizard.cf  			
            }
    	}
    }
    
    def ladder(){
    	if(getTile(wizard.x, wizard.y+8+wizard.vy) == 33 || getTile(wizard.x+8, wizard.y+8+wizard.vy) == 33 ){
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
            wizard.charged = true
    	}
    }
    void attackp(){
    	if(bolt.attack){
            return
    	}
    	wizard.charged = false
    	bolt.attack = true
    	bolt.f = wizard.f
    	if(wizard.f){
            bolt.vx = -3   		
    	}else{
            bolt.vx = 3
    	}
    	bolt.x = wizard.x
    	bolt.y = wizard.y
    	bolt.spid = bolt.spids[(bolt.charge/10).toInteger()]
    }
    
    void attackb(){
    	if((bolt.f && solid(bolt.x, bolt.y) || solid(bolt.x, bolt.y+7)) || (bolt.f && solid(bolt.x+4, bolt.y) || solid(bolt.x+4, bolt.y+7))){
            bolt.attack = false
            bolt.charge = 0
            return
    	}
        if(bolt.x > 240 || bolt.x < 0){
            bolt.attack = false
            bolt.charge = 0
            return
        }        
    	bolt.x = bolt.x + bolt.vx
    }  
    
    void checkScroll(){
        int tile = getTile(wizard.x+2, wizard.y+2)
        if( tile == 29 || tile == 30 || tile == 31){
            mapSet((wizard.x+2)/8, (wizard.y+2)/8, 0)
            wizard.scrolls++
        }
    }   
    
    //assumes x,y,w,h on each object
    boolean collide(a,b){
        return a.x < b.x + b.w && a.x + a.w > b.x && a.y < b.y + b.h && a.y + a.h > b.y
    }
    
    // INIT methods
    void init(){
    	wizard = [x: 10, y: 100, vx: 0, vy: 0, w:8, h:8, spid: 0, f: false, g: true, jmsid: 4,
            jumping: false, s: false, walkAnim: [1,2,3,0], waIndex: 0, 
            waTime: 0, left: "Left", right: "Right", up: "Space", down:"Down", charged: false, chargedTime: 0, cf: false,
            scrolls: 0]
    			
    	bolt = [x:0, y:0, vx:0, w:8, h:8, spid: 0, spids: [5,6,7,8], charge: 0, attack: false, hit: false]
        
    	def enemy1 = [x: 72, y: 72, w:8, h:8, f:false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 1, y: 0]]
        enemies = [] // only needed when the game loops around during development.
        enemies.add(enemy1)

    	loadMap("intro")
    }
    
    void initlvl1(){
        wizard.x = 10
        wizard.y = 144
        
    	def enemy1 = [x: 208, y: 64, vs: 0.2, l: 208, r: 224, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 28, y: 3], keyB: [x:28, y:2]]        
        def enemy2 = [x: 64, y: 80, vs: 0, l:64, r:64, w: 8, h: 8, f:false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 29, y: 3], keyB: [x:29, y:2]]
        enemies = []
        enemies.addAll([enemy1, enemy2]) 
    	loadMap("lvl1")
    }
    
    void initlvl2(){
        wizard.x = 0
        wizard.y = 16
        
    	def enemy1 = [x: 72, y: 96, vs: 0.2, l:72, r:88, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 27, y: 17], keyB: [x:28, y:17]]        
        def enemy2 = [x: 152, y: 72, vs: 0.2, l:152, r:168, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 27, y: 18], keyB: [x:28, y:18]]        
        def enemy3 = [x: 208, y: 72, vs: 0.2, l: 208, r:224, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 27, y: 19], keyB: [x:28, y:19]]
        enemies = []
        enemies.addAll([enemy1, enemy2, enemy3])
    	loadMap("lvl2")
    }
    
    void initlvl3(){
        wizard.x = 220
        wizard.y = 0
        
    	def enemy1 = [x: 8, y: 64, vs: 0.2, l:8, r:24, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 26, y: 13], keyB: [x:26, y:14]]        
        def enemy2 = [x: 8, y: 144, vs: 0.2, l:8, r:56, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 27, y: 13], keyB: [x:27, y:14]]        
        def enemy3 = [x: 184, y: 80, vs: 0.2, l:184, r:200, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 28, y: 13], keyB: [x:28, y:14]]        
        def enemy4 = [x: 32, y: 80, vs: 0.2, l:32, r:48, w: 8, h: 8, f: false, spid: 10, alive: true, remove: false, animTime: 0, walkAnim: [10,11,12,13], waIndex: 0, keyA: [x: 29, y: 13], keyB: [x:29, y:14]]
        enemies = []
        enemies.addAll([enemy1, enemy2, enemy3, enemy4])
        
    	loadMap("lvl3")
    }
    
    // ENEMY methods
    void moveEnemy(enemy, x, y){
        if(enemy.alive){
            if(enemy.x < x || enemy.x > y){
                enemy.vs = -enemy.vs
                enemy.f = !enemy.f
            }
            enemy.x = enemy.x + enemy.vs            
        }
    }
    
    void boltHitEnemy(enemy){
        if(collide(bolt, enemy) && bolt.charge == 30){  
            bolt.attack = false
            bolt.charge = 0
            if(!enemy.alive){
                mapRemove(enemy.x/8, enemy.y/8)
                enemy.remove = true
            }else{
                enemy.alive = false
            }            
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
    
    void removeDebris(){
        enemies.each{
            if(it.remove){
                mapRemove(it.x/8, it.y/8)
            }
        }
        enemies.removeAll{it.remove}
    }
    
    // UPDATE methods
    void lvl1Update(){
    	if(!lvl1start){
            initlvl1()
            lvl1start = true    		
    	}    	
        if(wizard.x>236){
            level++
            lvl1start = false
        }
        if(enemies.isEmpty()){
            return
        }
        enemies.each{
            enemyAnimation(it)
            moveEnemy(it, it.l, it.r)
            boltHitEnemy(it)
            if(!it.alive){
                mapSet(it.x/8, it.y/8, 88)
                mapRemove(it.keyA.x, it.keyA.y)
                mapRemove(it.keyB.x, it.keyB.y)
            }
        }
    }
    
    void lvl2Update(){
    	if(!lvl2start){
            initlvl2()
            lvl2start = true    		
    	}    	
        if(wizard.y>160){
            level++
            lvl2start = false
        }
        if(enemies.isEmpty()){
            return
        }
        enemies.each{
            enemyAnimation(it)
            moveEnemy(it, it.l, it.r)
            boltHitEnemy(it)
            if(!it.alive){
                mapSet(it.x/8, it.y/8, 88)
                mapRemove(it.keyA.x, it.keyA.y)
                mapRemove(it.keyB.x, it.keyB.y)
            }
        }
    }
    
    void lvl3Update(){
    	if(!lvl3start){
            initlvl3()
            lvl3start = true    		
    	}    	
        if(wizard.x>234){
            level++
            lvl3start = false
        }
        if(enemies.isEmpty()){
            return
        }
        enemies.each{
            enemyAnimation(it)
            moveEnemy(it, it.l, it.r)
            boltHitEnemy(it)
            if(!it.alive){
                mapSet(it.x/8, it.y/8, 88)
                mapRemove(it.keyA.x, it.keyA.y)
                mapRemove(it.keyB.x, it.keyB.y)
            }
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
    	removeDebris()
    	movep()
    	
        checkScroll() 
    	
    	switch(level){    
        case 0:
            if(wizard.y < 0){
                level++
            }
            if(enemies.isEmpty()){
                return
            }
            enemyAnimation(enemies[0])
            boltHitEnemy(enemies[0])
            if(!enemies[0].alive){
                mapSet(enemies[0].x/8, enemies[0].y/8, 88)
                mapSet(enemies[0].keyA.x,enemies[0].keyA.y,33)
            }
            break;
        case 1:
            lvl1Update()
            break;
        case 2:
            lvl2Update()
            break;
        case 3:
            lvl3Update()
            break;
        default:    			    	
            level = 0
            title = true
            loadMap("title")
            break;    			
    	}
        
        debuglvl()
        
    }
    
    void renderDefault(){
        if(enemies.isEmpty()){
            return
        }
    	if(enemies[0].alive){
            sprite(enemies[0].spid, enemies[0].x, enemies[0].y)
        }
    }
    void renderlvl1(){
        if(enemies.isEmpty()){
            return
        }
        enemies.each{it ->
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false)
            }
        }    	
    }	
    void renderlvl2(){   
        if(enemies.isEmpty()){
            return
        }
    	enemies.each{it ->
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false)
            }
        }  
    }	
    void renderlvl3(){    
        if(enemies.isEmpty()){
            return
        }
    	enemies.each{it ->
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false)
            }
        }  
    }	
	
    void render(){
        image("stonewall", 0,8)
        map()	
        if(key("S")){
            text("Scrolls: "+wizard.scrolls, 46, 32, 1)
        }
		
        if(title){
            text("Escape the dungeon!", 46, 32, 1)
            return
        }
			
        if(wizard.charged){
            sprite(9, (wizard.x).toFloat(), (wizard.y-8).toFloat(), wizard.cf, false)
        }
        sprite(wizard.spid, wizard.x.toFloat(), wizard.y.toFloat(), wizard.f, false)
        if(bolt.attack){
            sprite(bolt.spid, bolt.x, bolt.y, bolt.f, false)
        }
        switch(level){
        case 0:
            renderDefault()
            break;
        case 1:
            renderlvl1()
            break;
        case 2:
            renderlvl2()
            break;
        case 3:
            renderlvl3()
            break;
        default:
            renderDefault()
            break;
        }
        
        text("Charge: "+bolt.charge, 0,0, 1)
    }
    
    
    
    
    //Debug
    void debuglvl(){
        if(key("1")){
            level = 1
            lvl2start = false
            lvl3start = false
        }
        if(key("2")){
            level = 2
            lvl1start = false
            lvl3start = false
        }
        if(key("3")){
            level = 3
            lvl2start = false
            lvl1start = false
        }
    }
        
}
