import Bolt
class Wizard{
    float x = 10 
    float y = 100 
    float vx = 0 
    float vy = 0 
    float width = 8 
    float height = 8 
    int spid = 0 
    boolean f = false 
    boolean g = true 
    int jmsid = 4
    boolean jumping = false
    boolean s = false 
    int[] walkAnim = [1,2,3,0] 
    int waIndex = 0 
    boolean walking = false
    int waTime = 0
    String left = "Left"
    String right = "Right" 
    String up = "Space" 
    String down ="Down" 
    boolean charged = false 
    int chargedTime = 0 
    boolean cf = false
    int scrolls = 0 
    int health = 3
    
    void setWizard(float x, float y, float vx, float vy, float width, float height,
    int spid, boolean f, boolean g, int jmsid, boolean jumping, boolean s, int[] walkAnim,
    int waIndex, boolean walking, int waTime, String left, String right, String up, String down,
    boolean charged, int chargedTime, boolean cf, int scrolls, int health){
    	this.x = x
    	this.y = y
    	this.vx = vx
    	this.vy = vy
    	this.width = width
    	this.height = height
    	this.spid = spid
    	this.f = f
    	this.g = g
    	this.jmsid = jmsid
    	this.jumping = jumping
    	this.s = s
    	this.walkAnim = walkAnim
    	this.waIndex = waIndex
    	this.waTime = waTime
    	this.left = left
    	this.right = right
    	this.up = up
    	this.down = down
    	this.charged = charged
    	this.chargedTime = chargedTime
    	this.cf = cf
    	this.scrolls = scrolls
    	this.health = health
    }
    
    boolean solid(lsm, x, y){
        int cellid = getTile(lsm, x, y)
        return (cellid > 60 && cellid < 170)
    }	
    
    int getTile(lsm, x, y){
        def mx = ((x)/8 )
        def my = ((y)/8 )
        return lsm.getMapTile(mx,my)
    }
    
    void updateWizard(lsm, lk, audio, bolt){
    	waTime++
        vx = 0
        spid = 0
        //check left or right moving
        if(lk.key(left) && !solid(lsm, x-1, y) && !solid(lsm, x-1, y+7)){		
            vx =-1           
            f = true
            walking = true
        }
        if(lk.key(right) && !solid(lsm, x+8,y) && !solid(lsm, x+8,y+7)){
            vx = 1         
            f = false
            walking = true
        }
        
        //check walking
        if(vx == 0){
            walking = false
        }
        
        //check gravity
        if( solid(lsm, x+1,y+8+vy) || solid(lsm, x+7,y+8+vy)){
            vy=0
            g = true
        }else{
            vy = vy+0.2
            g = false
        }
	
        //check jumping.
        if((lk.key(up) || lk.key("Up")) && vy==0){        	
            vy=-3.5            
            g = false 
        }	
        
        //check on ground
        if(vy < 0 && (solid(lsm, x, y+1)||solid(lsm, x+7, y-1))){
            vy = 0
        }		
        if( vy<0 && (solid(lsm, x+vx,y+vy) || solid(lsm, x+8+vx,y+vy))){
            g = true  
        }   
		
        // jumping
        if(!g){
            spid = jmsid
        }
        //standing
        if(g){
            spid = 0
        }
        
        //walking
        if(walking && g){   
            spid = walkAnim[waIndex] 		
            if(waTime > 8){
                waIndex++
                waTime = 0
            }    	
    	}
    	if(waIndex > 3){
            waIndex = 0
    	}
    	
        //action buttons
    	if(lk.key("X") ){
            chargep(bolt)
    	}
    	if(lk.key("Z") ){
            attackp(bolt, audio)
    	}
    	
    	if(ladder(lsm)){
            vy = 0
            if(lk.key(down) || lk.key("Down") ){
                vy = 0.6
            }
            if(lk.key(up) || lk.key("Up")){
                vy = -0.6
            }            
        }	
        
        x=x+vx
        y=y+vy
        
        if(charged){
            chargedTime++
            if(chargedTime > 2){
                chargedTime = 0  
                cf = !cf  			
            }
    	}
    }
    
        
    boolean ladder(lsm){
    	if(getTile(lsm, x, y+8+vy) == 33 || getTile(lsm, x+8, y+8+vy) == 33 ){
            return true
    	}else{
            return false
    	}
    }
    
        
    void chargep(bolt){
    	if(bolt.attack){
            return
    	}	
    	if(bolt.charge < 30){
            bolt.charge++
    	}else{
            charged = true
    	}
    }
    
    void attackp(bolt, audio){
    	if(bolt.attack){
            return
    	}
    	charged = false
    	bolt.attack = true
    	audio.playSound("fire.wav")
    	bolt.f = f
    	if(f){
            bolt.vx = -3   		
    	}else{
            bolt.vx = 3
    	}
    	bolt.x = x
    	bolt.y = y
    	bolt.spid = bolt.spids[(bolt.charge/10).toInteger()]
    }
    

}
