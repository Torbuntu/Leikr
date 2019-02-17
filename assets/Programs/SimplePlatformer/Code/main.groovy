import leikr.Engine
class SimplePlatformer extends Engine {
	
    def p1 = [:]
    def p2 = [:]
	
		
    def i = [:]
    def bc = [:]
    def sc = [:]
	
    int height = 15
    int offX = 0
    int offY = 0
		
    Random rand
    
    def solid(x,y){
        float mx = (x)/8+offX 
        float my = (y)/8+offY
        int cellid = mapGet(mx,my)
        if(	cellid < 4 && cellid >= 0 ){
            return true
        }
        return false
    }
	
    def isLadder(x,y){
        return getSolid(x,y) == 18
    }
	
    def getSolid(x,y){
        float mx = (x)/8+offX 
        float my = (y)/8+offY
        return mapGet(mx,my)
    }
	
    void setSolid(x,y){
        float mx = (x)/8+offX 
        float my = (y)/8+offY
        mapSet(mx,my, 16)
    }
	
	
    void moveClouds(){
        bc.x -= 0.2
        sc.x -= 0.2
        if(bc.x < -8) {
            bc = [x: 240, y:(rand.nextInt(height)*10).toFloat()]
        }
        if(sc.x < -8){
            sc = [x: 240, y: (rand.nextInt(height)*10).toFloat()]
        }
    }    
    
    
    void move(p){        
        def play = false		
        p.vx = 0
        p.sid = p.stsid
        if(key(p.left) && !solid(p.x+1, p.y)){		
            p.vx =-1
            i.x += 0.5
            p.f = 0
            p.w = true
        }
        if(key(p.right) && !solid(p.x+7,p.y)){
            p.vx = 1
            i.x -= 0.5
            p.f = 1
            p.w = true
        }
        if(p.vx == 0){
            p.w = false
        }
        if( solid(p.x,p.y+8+p.vy) || solid(p.x+7,p.y+8+p.vy)){
            p.vy=0
            p.g = true
        }else{
            p.vy = p.vy+0.2
            p.g = false
        }
		
        if(key(p.up) && p.vy==0){
            p.vy=-3.5
            play = true
            p.g = false
        }	
		
        if( p.vy<0 && (solid(p.x+p.vx,p.y+p.vy) || solid(p.x+7+p.vx,p.y+p.vy)) || isLadder(p.x,p.y+8+p.vy) || isLadder(p.x+7,p.y+8+p.vy) ){
            p.vy=0
            p.g = true
            play = false
        }   
		
        if(play) sfx("jump")
        if(!p.g){
            p.sid = p.jmsid
        }
        if(p.g && !p.w){
            p.sid = p.stsid
        }
		
        if(p.g && p.w){
            p.sid = p.wlkAnim[p.wlkIndex]
            if(p.wlkTime>5){
                p.wlkIndex++
                p.wlkTime = 0
            }
        }
        if(p.wlkIndex > 3){
            p.wlkIndex = 0
        }
		
        attack(p)
		
        if(isLadder(p.x,p.y+8+p.vy) || isLadder(p.x+7,p.y+8+p.vy)){
            if(p.climbTime > 5){
                p.climbIndex++
                p.climbTime = 0
            }
            if(key(p.down)){
                p.vy = 0.4
                if(p.climbIndex > 2) p.climbIndex = 0
                p.sid = p.climbAnim[p.climbIndex]
                p.climbTime++
            }
            if(key(p.up)){
                p.vy = -0.4
                if(p.climbIndex > 2) p.climbIndex = 0
                p.sid = p.climbAnim[p.climbIndex]
                p.climbTime++
            }
        }	
		
        p.x=p.x+p.vx
        p.y=p.y+p.vy
        if(p.x < 0 ){
            p.x = 0
//            if(offX > 1){
//                offX = 0
//                p.x = 232
//            }else{
//                p.x = 0
//            }
        }	
        if(p.x > 236){
            p.x = 236
//            if(offX < 1){
//                offX = 30
//                p.x = 0
//            }else{
//                p.x = 236
//            }			
        }
    }
	
    void attack(p){
        if(key(p.primary) && !p.attack){
            p.attack = true
            if(p.f == 1){
                p.vx = 1
            }else{
                p.vx = -1
            }
            p.vy = 0
            p.w = false	
            p.swd = true
            p.gun = false
        }
        if(key(p.secondary) && !p.attack){
            p.attack = true
            if(p.f == 1){
                p.vx = -1
            }else{
                p.vx = 1
            }
            p.vy = 0
            p.w = false
            p.swd = false
            p.gun = true
        }
        
        if(p.attack){
            p.sid = p.asid
            if(p.swd){
                p.attackSprite = p.swdAnim[p.swdIndex]
                if(p.swdTime > 2){
                    p.swdIndex++
                    p.swdTime = 0
                }
                if(p.swdIndex > 3){
                    p.swdIndex = 0
                    p.attack = false
                    p.attackSprite = p.swdAnim[p.swdIndex]
                }
            }else{
                p.attackSprite = p.gunAnim[p.gunIndex]
                if(p.gunTime > 2){
                    p.gunIndex++
                    p.gunTime = 0
                }
                if(p.gunIndex > 3){
                    p.gunIndex = 0
                    p.attack = false
                    p.attackSprite = p.gunAnim[p.gunIndex]
                    int bspeed = -2
                    int fx = -15
                    if(p.f == 1){
                        bspeed = 2
                        fx = 15
                    }
					
                    def bullet = [x: (p.x+fx).toFloat(), y: (p.y).toFloat(), vx: bspeed, hit: false]
                    p.bullets.add(bullet)
                }
            }
        }
    }
	
    
	
    void updateTime(p){
        p.wlkTime++
        p.swdTime++
        p.gunTime++
        p.climbTime++
        if(p.wlkTime > 20) p.wlkTime = 0
        if(p.swdTime > 20) p.swdTime = 0
        if(p.gunTime > 20) p.gunTime = 0
        if(p.climbTime > 20) p.climbTime = 0
    }
	
    void drawBullets(p){		
        if(p.f == 1){
            bulletHitWall(-2, p)
            p.bullets.each{		
                sprite(43, (it.x).toFloat(), it.y, false, false)
                it.x += it.vx
            }
        }else{
            bulletHitWall(8, p)
            p.bullets.each{
                sprite(43, (it.x).toFloat(), it.y, true, false)
                it.x += it.vx				
            }
        }
    }
	
    void bulletHitWall(front, p){
        for(def b in p.bullets){
            if(getSolid(b.x+front, b.y) == 4){
                b.hit = true
                setSolid(b.x+front, b.y)//kill 3
            }
		
            if(solid(b.x+front,b.y)){
                b.hit = true
            }
        }
        p.bullets.removeAll{it.hit == true}
    }
	
    void renderPlayer(p){
        if(p.f == 1) {
            if(p.attack){
                sprite(p.attackSprite, p.x+7, p.y, false, false)
            }
            sprite(p.sid, p.x, p.y, false, false)
        }else{
            if(p.attack){
                sprite(p.attackSprite, p.x-7, p.y, true, false)
            }
            sprite(p.sid, p.x, p.y, true, false)
        }
    }
	
    void create(){		
        loadMap("map")
        loadImages()
		
        rand = new Random()
        p1 = [sid: 4, asid: 34, stsid: 4, jmsid: 5, x: 100, y:120, vx: 0, vy: 0, f: 1, g: true, w: false, attack: false, swd: false, 
            gun:false, wlkAnim: [32,4,5,33], wlkIndex: 0, wlkTime: 0, climbAnim:[48,49,50], climbIndex:0, 
            climbTime: 0, attackSprite: 35, swdAnim: [35,36,37,38], swdIndex: 0, swdTime: 0, 
            gunAnim: [39,40,41,42], gunIndex: 0, gunTime: 0, bullets: [], left: "A", right: "D",
            up: "W", down: "S", primary: "F", secondary: "T"]
        
        p2 = [sid: 20, asid: 66, stsid: 20, jmsid: 21, x: 120, y:120, vx: 0, vy: 0, f: 1, g: true, w: false, attack: false, swd: false, 
            gun:false, wlkAnim: [64,20,21,65], wlkIndex: 0, wlkTime: 0, climbAnim:[80,81,82], climbIndex:0, 
            climbTime: 0, attackSprite: 35, swdAnim: [35,36,37,38], swdIndex: 0, swdTime: 0, 
            gunAnim: [39,40,41,42], gunIndex: 0, gunTime: 0, bullets: [], left: "Left", right: "Right",
            up: "Up", down: "Down", primary: "Insert", secondary: "Page Down"]
	
        i = [x:40, y:0]	
        bc = [x:30, y:(rand.nextInt(height)*10).toFloat()]
        sc = [x:180, y:(rand.nextInt(height)*10).toFloat()]
    }

    void update(float delta){
        moveClouds()
        move(p1)
        move(p2)
        updateTime(p1)
        updateTime(p2)
    }
	
	
	
    void render(){
        bgColor(0.1f,0.1f,0.1f)
        //FPS()
        image("moon", i.x,i.y)
		
        map(0,0, offX, offY, 240, 160)
		
        renderPlayer(p1)
        drawBullets(p1)
		
        renderPlayer(p2)
        drawBullets(p2)
				
        sprite16(3, bc.x, bc.y)
        sprite16(4, sc.x, sc.y)	
			
    }
}
