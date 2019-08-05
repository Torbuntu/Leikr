import java.io.File //for bad guy code test
import com.badlogic.gdx.controllers.*;

class Shapes extends leikr.Engine {

    int x, y, x2, y2, r, t, amt, rx, ry, i, j

    void create(){
        amt = 5      
	    setController(new ControllerAdapter(){
			@Override
			public boolean buttonDown(Controller controller, int btCode) {
				
			}
		})
        
        //println new File("/").list() //bad guy code test
    }
    void update(float delta){

    }
    void render(){	

        t+=1
        
        for(j = 0; j < 240; j++){
        	drawColor(j%32)
            circle(j+3, 12, 1)
        }
 

    }
        
}	
