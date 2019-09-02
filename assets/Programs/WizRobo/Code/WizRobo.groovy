import Enemy
import Wizard
import Bolt
import groovy.transform.CompileStatic
@CompileStatic
class WizRobo extends leikr.Engine {
	
    boolean DEBUG = false;
	
    Wizard wizard 
    Bolt bolt 
    List<Enemy> enemies = []
    Enemy enemy1, enemy2, enemy3, enemy4, enemy5
    int[] enemyDeadSpids = [85,86,87]
    
    int[] healthSpids = [193,193,193]
    int[] energySpids = [194,194,194]
	
    boolean gameOver = false
    boolean title = true
    int level = 0
    boolean lvl1start = false
    boolean lvl2start = false
    boolean lvl3start = false
    boolean lvl4start = false
    boolean lvl5start = false    
    boolean lvlFinalstart = false
	    
    int time = 0
		    
    boolean solid(float x, float y){
        int cellid = getTile(x,y)
        if(	cellid > 60 && cellid < 170){
            return true
        }
        return false
    }	  
	
    int getTile(float x, float y){
        int mx = ((x)/8 ) as int
        int my = ((y)/8 ) as int
        return mapGet(mx,my)
    }
    
    void movep(){        
        wizard.updateWizard(screen, keyboard, controllerA, bolt)
		
       
        if(bolt.attack){
            attackb()
    	}else{
            bolt.x = 0
            bolt.y = 0
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
        int tile = getTile((wizard.x as int)+2, (wizard.y as int)+2)
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
    boolean collide(Bolt a, Enemy b){
        return (a.x < b.x + b.width) && (a.x + a.width > b.x) && (a.y < b.y + b.height) && (a.y + a.height > b.y)
    }
    
    boolean collide(Wizard a, Enemy b){
        return (a.x < b.x + b.width) && (a.x + a.width > b.x) && (a.y < b.y + b.height) && (a.y + a.height > b.y)
    }
    
    // INIT methods
    void init(){
    	wizard = new Wizard()    			
    	bolt = new Bolt()
        
    	Enemy enemy1 = new Enemy()
        enemies = [] // only needed when the game loops around during development.
        enemies.add(enemy1)

    	loadMap("intro")
    }
    
    void initlvl1(){
        wizard.x = 10
        wizard.y = 142
        
        enemies = []
        enemies.addAll([
        	new Enemy(208f, 64f, 0.2f, 208, 224, [x: 28, y: 3]as Map, [x:28, y:2]as Map) , 
        	new Enemy( 64f, 80f,   0f,  64,  64, [x: 29, y: 3]as Map, [x:29, y:2]as Map)
        	]
        ) 
    	loadMap("lvl1")
    	lvl1start = true
    }
    
    void initlvl2(){
        wizard.x = 0
        wizard.y = 24
        
        enemies = []
        enemies.addAll([
			new Enemy(  72f, 96f, 0.2f,  72,   88, [x: 27, y: 17]as Map, [x:28, y:17]as Map),        
			new Enemy( 152f, 72f, 0.2f, 152,  168, [x: 27, y: 18]as Map, [x:28, y:18]as Map),        
			new Enemy( 208f, 72f, 0.2f, 208,  224, [x: 27, y: 19]as Map, [x:28, y:19]as Map)
        ])
    	loadMap("lvl2")
    	lvl2start = true
    }
    
    void initlvl3(){
        wizard.x = 220
        wizard.y = 0
        
        enemies = []
        enemies.addAll([
			new Enemy(   8f,  64f, 0.2f,   8,  24, [x: 26, y: 13]as Map, [x:26, y:14]as Map), 
			new Enemy(   8f, 144f, 0.2f,   8,  56, [x: 27, y: 13]as Map, [x:27, y:14]as Map),   
			new Enemy( 184f,  80f, 0.2f, 184, 200, [x: 28, y: 13]as Map, [x:28, y:14]as Map),
			new Enemy(  32f,  80f, 0.2f,  32,  48, [x: 29, y: 13]as Map, [x:29, y:14]as Map)
        ])
        
    	loadMap("lvl3")
    	lvl3start=true
    }
    
    void initlvl4(){
        wizard.x = 10
        wizard.y = 110
        
        enemies = []
        enemies.addAll([
			new Enemy(  24f,  88f, 0.2f,  24,  40, [x: 3, y: 17]as Map,  [x:3, y:18]as Map),       
			new Enemy(  96f,  88f, 0.2f,  96, 104, [x: 17, y: 2]as Map,  [x:17, y:3]as Map),     
			new Enemy(  96f,  24f, 0.2f,  56, 128, [x: 29, y: 9]as Map, [x:29, y:10]as Map),   
			new Enemy(   8f, 144f, 0.2f,   8,  16, [x: 28, y: 10]as Map, [x:28, y:9]as Map),
			new Enemy( 168f,  80f, 0.2f, 168, 168, [x: 27, y: 9]as Map, [x:27, y:10] as Map)
        ])
        
    	loadMap("lvl4")
    	lvl4start = true
    }
    
    void initlvl5(){
    	wizard.x = 10
        wizard.y = 80
        
        enemies = [] // only needed when the game loops around during development.
        enemies.add(new Enemy(40f, 72f, 0.6f, 40, 184, 32f, 32f, false, 60,  true, false, 0, [60,61,62,63] as int[],  0, [x: 27, y: 1] as Map, [x:28, y:1] as Map,  8))

    	loadMap("lvl5")
    }
        
    void initlvlFinal(){
        wizard.x = 10
        wizard.y = 100
        
        
        enemies = []
        enemies.addAll([
			new Enemy(190f, 120f, 0.2f, 8, 200, 32f, 32f, false, 60, true, false, 0, [60,61,62,63] as int[], 0, [x: 28, y: 3] as Map, [x:28, y:2] as Map, 8),      	
			new Enemy(30f, 120f, 0.6f, 8, 200, 32f, 32f, false, 60, true, false, 0, [60,61,62,63] as int[], 0, [x: 28, y: 3] as Map, [x:28, y:2] as Map,  8)
        ]) 
    	loadMap("lvlFinal")
    }
    
    // ENEMY methods
    
    void boltHitEnemy(Enemy enemy){
        if(collide(bolt, enemy) && bolt.charge == 30){  
            bolt.attack = false
            bolt.charge = 0
            if(!enemy.alive){
                enemy.remove = true
                mapRemove((enemy.x as int)/8 as int,(enemy.y as int )/8 as int)
            }else{
                enemy.alive = false
                enemy.x = ceil(enemy.x)
                enemy.x = enemy.x - enemy.x%8
                mapRemove(enemy.keyA.x as int, enemy.keyA.y as int)
                mapRemove(enemy.keyB.x as int, enemy.keyB.y as int)
                mapSet((int)(enemy.x/8), (int)(enemy.y/8), 88)
            }                       
    	}
    }    

    void removeDebris(){
        for(Enemy it : enemies){
            if(it.remove){
            	int num = randInt(2)            	
                if(num==1){
                    int x = it.x/8 as int
                    int y = it.y/8 as int
                    mapSet(x, y, 193)
                }
            }
        }
        enemies.removeAll{it.remove}
    }
    
    void updateEnemies(){
        if(!enemies.isEmpty()){
        	for(Enemy it : enemies){
                it.enemyAnimation()
                it.moveEnemy()
                boltHitEnemy(it)            
            }
        }
    }
    
    void initLvl(int lvl){
    	switch(lvl){
    		case 1:
    			initlvl1()
    			break
    		case 2:
    			initlvl2()
    			break
    		case 3:
    			initlvl3()
    			break
    		case 4:
    			initlvl4()
    			break
    	}
    }
    void falseLevel(int lvl){
    	switch(lvl){
    		case 1:
    			lvl1start = false
    			break
    		case 2:
    			lvl2start = false
    			break
    		case 3:
    			lvl3start = false
    			break
    		case 4:
    			lvl4start = false
    			break
    	}
    }
    // UPDATE methods
    void lvlUpdate(int lvl, boolean lvlStart, float wizStart, int pos){
    	if(!lvlStart){
    		initLvl(lvl)
    	}
    	if(lvlStart && wizStart > pos){
    		level++
    		falseLevel(lvl)
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
        
        for(Enemy it : enemies){
            it.enemyAnimation()
            it.moveEnemy()
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
                    
                    mapSet(it.keyA.x as int, (it.keyA.y as int), 33)
                    mapSet(it.keyB.x as int, (it.keyB.y as int), 33)
                    
                    mapSet(it.keyA.x as int, (it.keyA.y as int)+5, 33)
                    mapSet(it.keyB.x as int, (it.keyB.y as int)+5, 33)
                    
                    mapSet(it.keyA.x as int, (it.keyA.y as int)+6, 33)
                    mapSet(it.keyB.x as int, (it.keyB.y as int)+6, 33)
                    
                    mapSet(it.keyA.x as int, (it.keyA.y as int)+7, 33)
                    mapSet(it.keyB.x as int, (it.keyB.y as int)+7, 33)
                    
                    mapSet(it.keyA.x as int, (it.keyA.y as int)+8, 33)
                    mapSet(it.keyB.x as int, (it.keyB.y as int)+8, 33)
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
        for(Enemy it in enemies){
            it.enemyAnimation()
            it.moveEnemy()
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
    	//HOST_INFO()
        if(keyPress("F1")) DEBUG = !DEBUG
        debuglvl()       
        if(gameOver){
            if(keyPress("Enter") || button("SELECT")){
                title = true
                gameOver = false
                return
            }
        }
    	if(title){
            if(keyPress("Enter") || button("SELECT")){
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
            enemies[0].enemyAnimation()
            boltHitEnemy(enemies[0])
            if(!enemies[0].alive && !enemies[0].remove){
                mapSet((int)(enemies[0].x/8), (int)(enemies[0].y/8), 88)
                mapSet(enemies[0].keyA.x as int,enemies[0].keyA.y as int,33)
            }
            break;
        case 1:
            lvlUpdate(1, lvl1start, wizard.x, 236)
            break;
        case 2:
            lvlUpdate(2, lvl2start, wizard.y, 160)
            break;
        case 3:
            lvlUpdate(3, lvl3start, wizard.x, 234)
            break;
        case 4:
            lvlUpdate(4, lvl4start, wizard.x, 234)
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
           	for(Enemy it in enemies){
                if(collide(wizard, it)){
                    wizard.health--
                    if(wizard.f){
                        wizard.x = wizard.x + 12
                        if(solid(wizard.x -4,wizard.x -4)){
                            wizard.x = wizard.x - 4
                    	}
                    }else{
                        wizard.x = wizard.x -12
                        if(solid(wizard.x-4,wizard.x -4)){
                            wizard.x = wizard.x  + 4
                    	}
                    }
                    wizard.y = wizard.y - 8
                    
                }
            }
        }
        FPS()
    }
    
    void renderGui(){
        
        sprite(energySpids[0], 0, 0)
        sprite(energySpids[1], 8, 0)
        sprite(energySpids[2], 16, 0)
        
        sprite(healthSpids[0], 104, 0)
        sprite(healthSpids[1], 96, 0)
        sprite(healthSpids[2], 88, 0)
        
        text("lvl:"+level, 112, 0, 1)
        
        sprite(29, 160, 0)
        text(":"+wizard.scrolls, 168, 0, 1)
    }

    void renderlvl(){
        for(Enemy e in enemies){
        	if(e.alive) sprite(e.spid, e.x, e.y, e.f, false)
        }
    }

    void renderBossLvl(){    
        for(Enemy it in enemies){
            if(it.alive){
                sprite(it.spid, it.x, it.y, it.f, false, 2)
                text("["+it.health+"]", it.x+8 as float, it.y-8 as float, 8)
            }
        }  
    }	
    
    void renderGameOver(){
    	text("Game Over!", 46, 32, 1)
    	text("Thanks for playing this demo.", 8, 42, 1)
    	text("Press Enter to play again.", 8, 54, 1)
    }
	
    void render(){
        if(gameOver){
            renderGameOver()
            return
        }
        
		
        if(title){
            image("stonewall", 0,0)
            map()
            text("Escape the dungeon!", 46, 32, 1)
            text("Move: arrows. Jump: Space. Charge: X. Shoot: Z. Start: Enter", 12, 110, 116, 1)
            return
        }else{
            image("stonewall", 0,8)
            map()	
        }
		
        renderGui()
        if(wizard.charged){
            sprite(9, wizard.x, wizard.y - 8 as float, wizard.cf, false)
        }
        sprite(wizard.spid, wizard.x, wizard.y, wizard.f, false)

        if(bolt.attack){
            sprite(bolt.spid, bolt.x, bolt.y, bolt.f, false)
        }
        
        if(enemies.isEmpty()) return;
        switch(level){
        case 5:
        case 6:
            renderBossLvl()
            break;
        default:
            renderlvl()
            break;
        }              
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
