import Enemy
import Wizard
import Bolt

class WizRobo extends leikr.Engine {
    def wizard 
    def bolt 
    def enemies = []
    def enemy1, enemy2, enemy3, enemy4, enemy5
    def enemyDeadSpids = [85,86,87]
    
    def healthSpids = [193,193,193]
    def energySpids = [194,194,194]
	
	def gameOver = false
    def title = true
    def level = 0
    def lvl1start = false
    def lvl2start = false
    def lvl3start = false
    def lvl4start = false
    def lvl5start = false    
    def lvlFinalstart = false
	    
    def time = 0
    def tmpAnimSprite 
		    
    def solid(x,y){
        float mx = (x)/8 
        float my = (y)/8
        int cellid = mapGet((int)mx,(int)my)
        if(	cellid > 60 && cellid < 170){
            return true
        }
        return false
    }	  
	
    def getTile(x,y){
        float mx = (x)/8 
        float my = (y)/8
        return mapGet((int)mx,(int)my)
    }
    
    void movep(){        
        wizard.waTime++
        wizard.vx = 0
        wizard.spid = 0
        //check left or right moving
        if((key(wizard.left) || button(BTN.LEFT)) && !solid(wizard.x-1, wizard.y) && !solid(wizard.x-1, wizard.y+7)){		
            wizard.vx =-1           
            wizard.f = true
            wizard.walking = true
        }
        if((key(wizard.right) || button(BTN.RIGHT)) && !solid(wizard.x+8,wizard.y) && !solid(wizard.x+8,wizard.y+7)){
            wizard.vx = 1         
            wizard.f = false
            wizard.walking = true
        }
        
        //check walking
        if(wizard.vx == 0){
            wizard.walking = false
        }
        
        //check gravity
        if( solid(wizard.x+1,wizard.y+8+wizard.vy) || solid(wizard.x+7,wizard.y+8+wizard.vy)){
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
        if(wizard.vy < 0 && (solid(wizard.x, wizard.y+1)||solid(wizard.x+7, wizard.y-1))){
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
        if(wizard.g){
            wizard.spid = 0
        }
        
        //walking
        if(wizard.walking && wizard.g){   
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
    	if(wizard.health < 1){
            level = 0
            gameOver = true
            loadMap("title")
            title = false
            lvl1start = false
            lvl2start = false
            lvl3start = false
            lvl4start = false
            lvl5start = false
            lvlFinalstart = false
    	}
    }
    
    def ladder(){
    	if(getTile(wizard.x, wizard.y+8+wizard.vy) == 33 || getTile(wizard.x+8, wizard.y+8+wizard.vy) == 33 ){
            return true
    	}else{
            return false
    	}
    }
    
    def walkingDust(){
    	def i = randInt(8)
		def j = randInt(8)
		if(wizard.f){			
			pixel(14, (int)(wizard.x + 9+i), (int)(wizard.y -j+6))
    		pixel(14, (int)(wizard.x + 9+j), (int)(wizard.y -i+6))
		}else{
			pixel(14, (int)(wizard.x-i-1), (int)(wizard.y -j+6))
    		pixel(14, (int)(wizard.x-j-1), (int)(wizard.y -i+6))
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
    	bolt.x = (int)wizard.x
    	bolt.y = (int)wizard.y
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
            mapSet((int)((wizard.x+2)/8), (int)((wizard.y+4)/8), 0)
            wizard.scrolls++
        }
        if(tile == 193 && wizard.health < 3){
            wizard.health++
            mapRemove((int)((wizard.x+2)/8), (int)((wizard.y+4)/8))
        }
    }   
    
    //assumes x,y,w,h on each object
    boolean collide(a,b){
        return (a.x < b.x + b.width) && (a.x + a.width > b.x) && (a.y < b.y + b.height) && (a.y + a.height > b.y)
    }
    
    // INIT methods
    void init(){
    	wizard = new Wizard()    			
    	bolt = new Bolt()
        
    	def enemy1 = new Enemy()
        enemies = [] // only needed when the game loops around during development.
        enemies.add(enemy1)

    	loadMap("intro")
    }
    
    void initlvl1(){
        wizard.x = 10
        wizard.y = 144
        
    	def enemy1 = new Enemy(208, 64, 0.2, 208, 224, [x: 28, y: 3], [x:28, y:2])    
        def enemy2 = new Enemy( 64, 80,   0,  64,  64, [x: 29, y: 3], [x:29, y:2])
        enemies = []
        enemies.addAll([enemy1, enemy2]) 
    	loadMap("lvl1")
    }
    
    void initlvl2(){
        wizard.x = 0
        wizard.y = 24
        
    	def enemy1 = new Enemy(  72, 96, 0.2,  72,   88, [x: 27, y: 17], [x:28, y:17])        
        def enemy2 = new Enemy( 152, 72, 0.2, 152,  168, [x: 27, y: 18], [x:28, y:18])        
        def enemy3 = new Enemy( 208, 72, 0.2, 208,  224, [x: 27, y: 19], [x:28, y:19])
        enemies = []
        enemies.addAll([enemy1, enemy2, enemy3])
    	loadMap("lvl2")
    }
    
    void initlvl3(){
        wizard.x = 220
        wizard.y = 0
        
    	def enemy1 = new Enemy(   8,  64, 0.2,   8,  24, [x: 26, y: 13], [x:26, y:14])      
        def enemy2 = new Enemy(   8, 144, 0.2,   8,  56, [x: 27, y: 13], [x:27, y:14])        
        def enemy3 = new Enemy( 184,  80, 0.2, 184, 200, [x: 28, y: 13], [x:28, y:14]) 
        def enemy4 = new Enemy(  32,  80, 0.2,  32,  48, [x: 29, y: 13], [x:29, y:14])
        enemies = []
        enemies.addAll([enemy1, enemy2, enemy3, enemy4])
        
    	loadMap("lvl3")
    }
    
    void initlvl4(){
        wizard.x = 10
        wizard.y = 110
        
    	def enemy1 = new Enemy(  24,  88, 0.2,  24,  40, [x: 3, y: 17],  [x:3, y:18])       
        def enemy2 = new Enemy(  96,  88, 0.2,  96, 104, [x: 17, y: 2],  [x:17, y:3])     
        def enemy3 = new Enemy(  96,  24, 0.2,  56, 128, [x: 29, y: 9], [x:29, y:10])   
        def enemy4 = new Enemy(   8, 144, 0.2,   8,  16, [x: 28, y: 10], [x:28, y:9])
        def enemy5 = new Enemy( 168,  80, 0.2, 168, 168, [x: 27, y: 9], [x:27, y:10])
        enemies = []
        enemies.addAll([enemy1, enemy2, enemy3, enemy4, enemy5])
        
    	loadMap("lvl4")
    }
    
    void initlvl5(){
    	wizard.x = 10
        wizard.y = 80
        
    	def enemy1 = new Enemy(40, 72, 0.6, 40, 184, 32, 32, false, 60,  true, false, 0, [60,61,62,63],  0, [x: 27, y: 1], [x:28, y:1],  8)
        enemies = [] // only needed when the game loops around during development.
        enemies.add(enemy1)

    	loadMap("lvl5")
    }
        
    void initlvlFinal(){
        wizard.x = 10
        wizard.y = 100
        
    	def enemy1 = new Enemy(190, 120, 0.2, 8, 200, 32, 32, false, 60, true, false, 0, [60,61,62,63], 0, [x: 28, y: 3], [x:28, y:2], 8)  
    	
    	def enemy2 = new Enemy(30, 120, 0.6, 8, 200, 32, 32, false, 60, true, false, 0, [60,61,62,63], 0, [x: 28, y: 3], [x:28, y:2],  8)
        
        enemies = []
        enemies.addAll([enemy1,enemy2]) 
    	loadMap("lvlFinal")
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
                enemy.remove = true
                mapRemove((int)(enemy.x/8), (int)(enemy.y/8))
            }else{
                enemy.alive = false
                enemy.x = ceil(enemy.x)
                enemy.x = enemy.x - enemy.x%8
                mapRemove(enemy.keyA.x, enemy.keyA.y)
                mapRemove(enemy.keyB.x, enemy.keyB.y)
                mapSet((int)(enemy.x/8), (int)(enemy.y/8), 88)
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
            	def num = randInt(2)            	
                if(num==1){
                    def x = it.x/8
                    def y = it.y/8
                    mapSet((int)x, (int)y, 193)
                }
            }
        }
        enemies.removeAll{it.remove}
    }
    
    void updateEnemies(){
        if(!enemies.isEmpty()){
            enemies.each{
                enemyAnimation(it)
                moveEnemy(it, it.l, it.r)
                boltHitEnemy(it)            
            }
        }
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
        updateEnemies()
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
        updateEnemies()
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
        updateEnemies()
    }
    
    void lvl4Update(){
    	if(!lvl4start){
            initlvl4()
            lvl4start = true    		
    	}    	
        if(wizard.x>234){
            level++
            lvl4start = false
        }
        updateEnemies()
    }
    
    void lvl5Update(){
    	if(!lvl5start){
            initlvl5()
            lvl5start = true    		
    	}    	       
        if(wizard.y < 6){
            level++
            lvl5start = false
            return
        }
        enemies.each{
            enemyAnimation(it)
            moveEnemy(it, it.l, it.r)
            if(collide(bolt, it) && bolt.charge == 30){  
                bolt.attack = false
                bolt.charge = 0
                if(it.health > 0){
                    it.health--
                }       
                if(it.health == 0){
                    it.alive = false
                    it.remove = true
                    
                    mapSet((int)((it.x+16)/8), (int)((it.y+16)/8), 29)//Drop scroll for the victor
                    
                    mapSet(it.keyA.x, it.keyA.y, 33)
                    mapSet(it.keyB.x, it.keyB.y, 33)
                    
                    mapSet(it.keyA.x, it.keyA.y+5, 33)
                    mapSet(it.keyB.x, it.keyB.y+5, 33)
                    
                    mapSet(it.keyA.x, it.keyA.y+6, 33)
                    mapSet(it.keyB.x, it.keyB.y+6, 33)
                    
                    mapSet(it.keyA.x, it.keyA.y+7, 33)
                    mapSet(it.keyB.x, it.keyB.y+7, 33)
                    
                    mapSet(it.keyA.x, it.keyA.y+8, 33)
                    mapSet(it.keyB.x, it.keyB.y+8, 33)
                }  
            }	
    	}
        
    }
    
    void lvlFinalUpdate(){
    	if(!lvlFinalstart){
            initlvlFinal()
            lvlFinalstart = true    		
    	}    	       
        if(enemies.isEmpty()){
            level = 0
            gameOver = true
            loadMap("title")
            lvlFinalstart = false
            return
        }
        enemies.each{
            enemyAnimation(it)
            moveEnemy(it, it.l, it.r)
            if(collide(bolt, it) && bolt.charge == 30){  
                bolt.attack = false
                bolt.charge = 0
                if(it.health > 0){
                    it.health--
                }       
                if(it.health == 0){
                    it.alive = false
                    it.remove = true
                }  
            }	
    	}
        
    }
  
    void create(){		
        loadImages()
        loadMap("title")
    	tmpAnimSprite = makeAnimSprite([1,2,3,0] as int[], [0.5f,0.5f,0.5f,0.5f] as float[], true)
    
    }
    
    void updateGui(){
        if(bolt.charge < 5){
            energySpids = [194,194,194]
        }
        if(bolt.charge >= 5){
            energySpids[0] = 195
        }
        if(bolt.charge >= 10){
            energySpids[0] = 196
        }
        if(bolt.charge >= 15){
            energySpids[1] = 195
        }
        if(bolt.charge >= 20){
            energySpids[1] = 196
        }
        if(bolt.charge >= 25){
            energySpids[2] = 195 
        }
        if(bolt.charge >= 30){
            energySpids[2] = 196
        }
        if(bolt.attack){
            energySpids = [194,194,194]
        }
        
        if(wizard.health == 0){
            healthSpids = [193,193,193]
        }
        if(wizard.health == 1){
            healthSpids = [192,193,193]
        }
        if(wizard.health == 2){
            healthSpids = [192,192,193]
        }
        if(wizard.health == 3){
            healthSpids = [192,192,192]
        }
    }

    void update(float delta){
		FPS()
		tmpAnimSprite.update(delta)
        debuglvl()       
        if(gameOver){
        	if(key("Enter") || button(BTN.START)){
        		title = true
        		gameOver = false
        		return
        	}
        }
    	if(title){
            if(key("Enter") || button(BTN.START)){
                title = false
                init()
            }
            return
    	}    
    	removeDebris()
    	movep()
    	updateGui()
        
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
            if(!enemies[0].alive && !enemies[0].remove){
                mapSet((int)(enemies[0].x/8), (int)(enemies[0].y/8), 88)
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
        case 4:
            lvl4Update()
            break;
        case 5:
            lvl5Update()
            break;
        case 6:
            lvlFinalUpdate()
            break;
        default:    			    	
            level = 0
            title = true
            loadMap("title")
            break;    			
    	}
        
        if(!enemies.isEmpty()){
            enemies.each{
                if(collide(wizard, it)){
                    wizard.health--
                    if(wizard.f){
                        wizard.x = wizard.x +8
                        if(solid(wizard.x-4, wizard.x-4)){
                    		wizard.x = wizard.x - 4
                    	}
                    }else{
                        wizard.x = wizard.x -8
                        if(solid(wizard.x-4, wizard.x-4)){
                    		wizard.x = wizard.x + 4
                    	}
                    }
                    wizard.y -= 8
                    
                }
            }
        }
    }
    
    void renderGui(){
        
        sprite(energySpids[0], 0, 0)
        sprite(energySpids[1], 8, 0)
        sprite(energySpids[2], 16, 0)
        
        sprite(healthSpids[0], 104, 0)
        sprite(healthSpids[1], 96, 0)
        sprite(healthSpids[2], 88, 0)
        
        text("lvl:"+level, 112, 0, 32)
        
        sprite(29, 160, 0)
        text(":"+wizard.scrolls, 168, 0, 32)
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
    void renderlvl4(){    
        if(enemies.isEmpty()){
            return
        }
        enemies.each{it ->
            if(it.alive){            	
                sprite(it.spid, it.x, it.y, it.f, false)
            }
        }  
    }	
    void renderlvl5(){    
        if(enemies.isEmpty()){
            return
        }
        enemies.each{it ->
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false, 2)
                text("["+it.health+"]", (it.x+8).toFloat(), (it.y-8).toFloat(), 23)
            }
        }  
    }	
    void renderlvlFinal(){    
        if(enemies.isEmpty()){
            return
        }
        enemies.each{it ->
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false, 2)
                text("["+it.health+"]", (it.x+8).toFloat(), (it.y-8).toFloat(), 23)
            }
        }  
    }	
    
    void renderGameOver(){
    	text("Game Over!", 46, 32, 32)
    	text("Thanks for playing this demo.", 8, 42, 32)
    	text("Press Enter to play again.", 8, 54, 32)
    }
	
    void render(){
        if(gameOver){
        	renderGameOver()
        	return
        }
        
		
        if(title){
            image("stonewall", 0,0)
	    	map()
            text("Escape the dungeon!", 46, 32, 32)
            text("Move: arrows. Jump: Space. Charge: X. Shoot: Z. Start: Enter", 12, 110, 116, 32)
            return
        }else{
	    	image("stonewall", 0,8)
            map()	
		}

		if(key("S")){
            text("Scrolls: "+wizard.scrolls, 46, 32, 32)
        }
		
        renderGui()
        if(wizard.charged){
            sprite(9, (wizard.x).toFloat(), (wizard.y-8).toFloat(), wizard.cf, false)
        }
        sprite(wizard.spid, wizard.x.toFloat(), wizard.y.toFloat(), wizard.f, false)
      //  if(wizard.walking) walkingDust()
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
        case 4:
            renderlvl4()
            break;
        case 5:
            renderlvl5()
            break;
        case 6:
            renderlvlFinal()
            break;
        default:
            renderDefault()
            break;
        }      
        
        spriteAnim(tmpAnimSprite, 10, 10)  
    }
    
    
    
    
    //Debug
    void debuglvl(){    	
        if(key("0")){
            level = 0
            title = true
            lvl1start = false
            lvl2start = false
            lvl3start = false
            lvl4start = false
            lvl5start = false    
            lvlFinalstart = false
        }
        if(key("1")){
            level = 1
            lvl2start = false
            lvl3start = false
            lvl4start = false
            lvl5start = false
            lvlFinalstart = false
        }
        if(key("2")){
            level = 2
            lvl1start = false
            lvl3start = false
            lvl4start = false
            lvl5start = false
            lvlFinalstart = false
        }
        if(key("3")){
            level = 3
            lvl1start = false
            lvl2start = false
            lvl4start = false
            lvl5start = false
            lvlFinalstart = false
        }
        if(key("4")){
            level = 4
            lvl1start = false
            lvl2start = false
            lvl3start = false
            lvl5start = false
            lvlFinalstart = false
        }
        if(key("5")){
            level = 5
            lvl1start = false
            lvl2start = false
            lvl3start = false
            lvl4start = false
            lvlFinalstart = false
        }
        if(key("6")){
            level = 6
            lvl1start = false
            lvl2start = false
            lvl3start = false
            lvl4start = false
            lvl5start = false
        }
    }
        
}
