import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mini2Dx.core.input.*

class ControllerUtil extends leikr.Engine {
    
    def settings = [:]
    def saved = [:]
    def index = "X";
    def dpad_h = false
    def dpad_v = false
    
    def controllers
    def controller
    def ERROR = false
    def unpress = false
    def activeController = false
    def finished = false
    
    int X;
    int A;
    int B;
    int Y;
    int LEFT_BUMPER;
    int RIGHT_BUMPER;
    int SELECT;
    int START;
    int UP;
    int DOWN;
    int RIGHT;
    int LEFT;

    int HORIZONTAL_AXIS;
    int VERTICAL_AXIS;
    
    def lastButtonPressed
    void readDataProps(){
        Properties prop = new Properties();
        try(InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream);

            settings.X = (prop.getProperty("btn_x") != null) ? Integer.parseInt(prop.getProperty("btn_x")) : 0;
            settings.A = (prop.getProperty("btn_a") != null) ? Integer.parseInt(prop.getProperty("btn_a")) : 1;
            settings.B = (prop.getProperty("btn_b") != null) ? Integer.parseInt(prop.getProperty("btn_b")) : 2;
            settings.Y = (prop.getProperty("btn_y") != null) ? Integer.parseInt(prop.getProperty("btn_y")) : 3;

            settings.LEFT_BUMPER = (prop.getProperty("btn_lbumper") != null) ? Integer.parseInt(prop.getProperty("btn_lbumper")) : 4;
            settings.RIGHT_BUMPER = (prop.getProperty("btn_rbumper") != null) ? Integer.parseInt(prop.getProperty("btn_rbumper")) : 5;

            settings.SELECT = (prop.getProperty("btn_select") != null) ? Integer.parseInt(prop.getProperty("btn_select")) : 8;
            settings.START = (prop.getProperty("btn_start") != null) ? Integer.parseInt(prop.getProperty("btn_start")) : 9;

            settings.UP = (prop.getProperty("btn_up") != null) ? Integer.parseInt(prop.getProperty("btn_up")) : -1;
            settings.DOWN = (prop.getProperty("btn_down") != null) ? Integer.parseInt(prop.getProperty("btn_down")) : 1;
            settings.LEFT = (prop.getProperty("btn_left") != null) ? Integer.parseInt(prop.getProperty("btn_left")) : -1;
            settings.RIGHT = (prop.getProperty("btn_right") != null) ? Integer.parseInt(prop.getProperty("btn_right")) : 1;
            
            settings.HORIZONTAL_AXIS = (prop.getProperty("axis_horizontal") != null) ? Integer.parseInt(prop.getProperty("axis_horizontal")) : 0;
            settings.VERTICAL_AXIS = (prop.getProperty("axis_vertical") != null) ? Integer.parseInt(prop.getProperty("axis_vertical")) : 1;


            
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
        saved = [X:false, A:false,B:false,Y:false,SELECT:false,START:false,LEFT_BUMPER:false,RIGHT_BUMPER:false,UP:false,DOWN:false,LEFT:false,RIGHT:false]
    }
    
    
     @Override
    public void onButtonDown(GamePad controller, int buttonCode){
        println buttonCode
        lastButtonPressed = buttonCode
        unpress = false
	}
	@Override
	public void onButtonUp(GamePad controller, int buttonCode){
        println buttonCode
        if(lastButtonPressed == buttonCode){
        	unpress = true
        }
	}
	@Override
	public void onAxisChanged(GamePad controller, int axisCode, float value) {
		println("axis: $axisCode, value: $value")
        if(dpad_h){
        	HORIZONTAL_AXIS = axisCode
        }
        if(dpad_v){
        	VERTICAL_AXIS = axisCode
        }
        if(value.toInteger() == 0){
        	unpress = true
        }else{
        	lastButtonPressed = value.toInteger()
        	unpress = false
        }
	}
    
    void create(){
        loadImages()
        //readDataProps()
    }
    void update(float delta){
        if(activeController){
            if(key("Q")){
                saved = [X:false, A:false,B:false,Y:false,SELECT:false,START:false,LEFT_BUMPER:false,RIGHT_BUMPER:false,UP:false,DOWN:false,LEFT:false,RIGHT:false]
            }

            if(!saved.X){
                index = "X"
                if(unpress){
                    if(saveToProps("btn_x")){
                        unpress = false
                        saved.X = true
                    }
                }
                return
            }
            if(!saved.A){
                index = "A"
                if(unpress){
                    if(saveToProps("btn_a")){                    
                        unpress = false
                        saved.A = true
                    }
                }
                return
            }
            if(!saved.B){
                index = "B"
                if(unpress){
                    if(saveToProps("btn_b")){                    
                        unpress = false
                        saved.B = true
                    }
                }
                return
            }
            if(!saved.Y){
                index = "Y"
                if(unpress){
                    if(saveToProps("btn_y")){                    
                        unpress = false
                        saved.Y = true
                    }
                }
                return
            }
            if(!saved.SELECT){
                index = "SELECT"
                if(unpress){
                    if(saveToProps("btn_select")){                    
                        unpress = false
                        saved.SELECT = true
                    }
                }
                return
            }
            if(!saved.START){
                index = "START"
                if(unpress){
                    if(saveToProps("btn_start")){                    
                        unpress = false
                        saved.START = true
                    }
                }
                return
            }
            if(!saved.LEFT_BUMPER){
                index = "LEFT_BUMPER"
                if(unpress){
                    if(saveToProps("btn_lbumper")){                    
                        unpress = false
                        saved.LEFT_BUMPER = true
                    }
                }
                return
            }
            if(!saved.RIGHT_BUMPER){
                index = "RIGHT_BUMPER"
                if(unpress){
                    if(saveToProps("btn_rbumper")){                    
                        unpress = false
                        saved.RIGHT_BUMPER = true
                    }
                }
                return
            }
            if(!saved.UP){
                dpad_h = false
                dpad_v = true
                index = "UP"
                if(unpress){
                    if(saveToProps("btn_up")){                    
                        unpress = false
                        saved.UP = true
                    }
                }
                return
            }
            if(!saved.DOWN){
                dpad_h = false
                dpad_v = true
                index = "DOWN"
                if(unpress){
                    if(saveToProps("btn_down")){                    
                        unpress = false
                        saved.DOWN = true
                    }
                }
                return
            }
            if(!saved.LEFT){
                dpad_h = true
                dpad_v = false
                index = "LEFT"
                if(unpress){
                    if(saveToProps("btn_left")){                    
                        unpress = false
                        saved.LEFT = true
                    }
                }
                return
            }
            if(!saved.RIGHT){
                dpad_h = true
                dpad_v = false
                index = "RIGHT"
                if(unpress){
                    if(saveToProps("btn_right")){                    
                        unpress = false
                        saved.RIGHT = true
                    }
                }
                return
            }
            finished = true
        }
    }


    def saveToProps(pname){
        Properties prop = new Properties();
        try (InputStream instream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(instream);
//            for(def names: prop.propertyNames()){
//                println names
//            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false
        }
        try(FileOutputStream stream = new FileOutputStream(new File("Data/system.properties"))){
            prop.setProperty(pname, lastButtonPressed.toString())
            if(dpad_h){
                prop.setProperty("axis_horizontal",HORIZONTAL_AXIS.toInteger().toString() )
            }
            if(dpad_v){
                prop.setProperty("axis_vertical",VERTICAL_AXIS.toInteger().toString())
            }
            prop.store(stream, null)
            
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
            return false
        }
        println "Save was successfull for $pname"
        return true
    }
    
    void drawButtons(){        
        if(button("UP") || key("Up")){
            sprite(1, 40,42,false, false, 2)
        }
        if(button("DOWN") || key("Down")){
            sprite(1, 40, 102, false, true, 2)
        }
        if(button("LEFT") || key("Left")){
            sprite(2, 10, 72, true, false,2)
        }
        if(button("RIGHT") || key("Right")){
            sprite(2, 70, 72, false, false,2)
        }
        
        if(button("LEFT_BUMPER") || key("Q")){
            sprite(0, 16,16,1)
            sprite(0, 32,16,1)
            sprite(0, 48,16,1)
            sprite(0, 64,16,1)
        }
        if(button("RIGHT_BUMPER") || key("W")){
            sprite(0, 160,16,1)
            sprite(0, 176,16,1)
            sprite(0, 192,16,1)
            sprite(0, 208,16,1)
        }
        if(button("X") || key("S")){
            sprite(3, 168, 40, false, false,2)
        }
        if(button("Y") || key("A")){
            sprite(4, 136, 72, false, false,2)
        }
        if(button("A") || key("X")){
            sprite(5, 200, 72, false, false,2)
        }
        if(button("B") || key("Z")){
            sprite(6, 168, 104, false, false,2)
        }
        if(button("SELECT") || key("'")){
            sprite(0, 80, 136, false, false,1)
            sprite(0, 96, 136, false, false,1)
        }
        if(button("START") || key("Enter")){
            sprite(0, 128, 136, false, false,1)
            sprite(0, 144, 136, false, false,1)
        }
    }
    void render(){	
        bgColor("200,200,200")
        
        drawButtons()
        image("controller",0,0)    
        if(ERROR || !activeController){
            text("No controller active.", 0,0,1)
        }else{
            if(finished){
            	text("Please Restart Leikr", 0,0,1)

            }else{
            	text("Please press: "+index, 0,0,1)

            }
        }
    }
}
