import Player
import Menu

class DogCat extends leikr.Engine {

    def p
    Menu menu = new Menu()
	
    int mapX = 0, mapY = 0
	
    // Engine methods
    void create(){
        loadMap("map")
        music("Map", true)
        p  = new Player(screen)
    }  
    
    void update(float delta){
    	if(menu.activeMenu){
            if(key("Enter")) menu.activeMenu = false
            return
    	}
        p.setState(0)
        if(key("Left") && !solid(p.x+10, p.y+16) && p.x+2 > 0){
            p.vx = -1.5f
            p.setFace(true)
            if(key("L-Shift")) {
                p.setState(2)
                p.vx = -2
            }else{
                p.setState(1)
            }		
            if(p.x < 80 && mapX < 0){
                if(key("L-Shift")){
                    mapX = mapX +2
                }else{
                    mapX++
                }
                p.vx
                p.vx = 0
            }
			
        }
        if(key("Right") && !solid(p.x+20, p.y+16)){
            p.vx = 1.5f			
            p.setFace(false)
            if(key("L-Shift")){
                p.setState(2)//2 running
                p.vx = 2
            }else{
                p.setState(1)//1 walking
            }
			
            if(p.x + p.vx > 120 && mapX > -1030) {
                if(key("L-Shift")){
                    mapX = mapX - 2
                }else{
                    mapX--
                }
                p.vx = 0
            }
        }
        if((key("Up") || key("Z") || key("Space")) && p.vy == 0 && !p.jumping){
            sfx("jump")
            p.setState(3)//3 jumping
            p.vy = -0.5f
            p.jumping = true
        }
		
        if(solid(p.x+12,p.y+32) || solid(p.x+20, p.y+32)){
            p.onGround()
        }else{
            p.falling()
        }
		
        //shroom
        if(shroom(p.x+12,p.y+32) || shroom(p.x+20, p.y+32) && p.jumping) {
            p.setState(3)//3 jumping
            p.vy = -2f
            p.jumping = true
        }
        coin(p.x+12, p.y+15)
        coin(p.x+20, p.y+15)
        p.update(delta)
			println mapX	
    }
    
    void render(){	
    	if(menu.activeMenu) {
            menu.drawMenu(screen)
            return
    	}
    	bgColor(10)
    	
        map(mapX, mapY) 
		
        p.draw(screen)
        text("Coins: " + p.coin, 0,8,1)
    }	
    //End engine methods
    
    //Start Helper methods 
    def solid(x,y){
        float mx = (x - mapX) /32 
        float my = (y - mapY) /32
        int id = mapGet(floor(mx),floor(my))       
        return	( id > 81 && id <= 97)
    }	
    
    def shroom(x,y){
    	float mx = (x-mapX)/32
    	float my = (y-mapY)/32
    	int id = mapGet(floor(mx), floor(my))
    	return  id == 30
    }
    def coin(x,y){
    	float mx = (x-mapX)/32
    	float my = (y-mapY)/32
    	int id = mapGet(floor(mx), floor(my))
    	if(id == 43) {
            mapSet(floor(mx), floor(my), -1)
            p.coin++
    	}
    }
}

