
import org.mini2Dx.core.input.*

class TestCont extends leikr.Engine {
	
	def btn = []
	def axis = []
	
    @Override
    public void onButtonDown(GamePad cont, int buttonId){
        println("::: Debug Button :::")
    	println("Controller: $cont , Value: $buttonId")
    	btn.add(buttonId) 
    }
    @Override
    public void onButtonUp(GamePad cont, int buttonId){
    	btn -= buttonId 
    }
    
    @Override
    public void onAxisChanged(GamePad controller, int axisCode, float buttonId) {
    	println("::: Debug Axis :::")
    	println("Controller: $controller , Axis: $axisCode , Value: $buttonId")
		axis = [axisCode, buttonId]
    }
	
    void create(){    	
       
    }
    
    //START UPDATE
    void update(float delta){
    	
    }
    //END UPDATE
    
    //START RENDER
    void render(){	
    	
		text("Button down: $btn", 0, 25, 240, 11 )
		text("Arrow down: $axis", 0, 100, 240, 11 )
    }
    
}
