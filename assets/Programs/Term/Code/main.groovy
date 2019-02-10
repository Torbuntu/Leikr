import leikr.Engine
import com.badlogic.gdx.Input.Keys
class Term extends Engine {

	Random rand	
	String textOutput = ""
	
	@Override
	public boolean keyTyped(char character){
		// your touch down code here
		if((int)character > 30) textOutput += character
		return true; // return true to indicate the event was handled
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACKSPACE && textOutput.length() > 0){
			textOutput = textOutput.substring(0, textOutput.length()-1)			
		}
		return false;
	}	
	
	void render(){	
		bgColor(16)
		text(textOutput, 0, 0, 230, 1)
	}	
}
