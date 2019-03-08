
import leikr.Engine

class Controllers extends Engine {
    
    
    int buttons = 12
    int index = 0
    def isSet = [:]
    def settings = [:]
    String settingButton = ""
    def allSet = false
    
    
    void create(){
        loadImages()
    	isSet = [left: false, right: false, up: false, down: false, lb: false, rb: false, select: false, start: false, a:false, b:false, x:false, y:false]
    }
    void update(float delta){
       
    }
    
    void drawButtons(){
        if(button(BTN.UP)){
            sprite32(1, 40,42,false, false)
        }
        if(button(BTN.DOWN)){
            sprite32(1, 40, 102, false, true)
        }
        if(button(BTN.LEFT)){
            sprite32(2, 10, 72, true, false)
        }
        if(button(BTN.RIGHT)){
            sprite32(2, 70, 72, false, false)
        }
        
        if(button(BTN.LEFT_BUMPER)){
            sprite16(0, 16,16)
            sprite16(0, 32,16)
            sprite16(0, 48,16)
            sprite16(0, 64,16)
        }
        if(button(BTN.RIGHT_BUMPER)){
            sprite16(0, 160,16)
            sprite16(0, 176,16)
            sprite16(0, 192,16)
            sprite16(0, 208,16)
        }
        if(button(BTN.X)){
            sprite32(3, 168, 40, false, false)
        }
        if(button(BTN.Y)){
            sprite32(4, 136, 72, false, false)
        }
        if(button(BTN.A)){
            sprite32(5, 200, 72, false, false)
        }
        if(button(BTN.B)){
            sprite32(6, 168, 104, false, false)
        }
        if(button(BTN.SELECT)){
            sprite16(0, 80, 136, false, false)
            sprite16(0, 96, 136, false, false)
        }
        if(button(BTN.START)){
            sprite16(0, 128, 136, false, false)
            sprite16(0, 144, 136, false, false)
        }
        FPS()
    }
    void render(){	
        bgColor(0.6f, 0.6f, 0.6f)
        
        drawButtons()
        image("controller",0,0)    
        
    }	
}
new Controllers()