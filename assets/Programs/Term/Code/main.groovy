import leikr.Engine
import leikr.Repository
import com.badlogic.gdx.Input.Keys
class Term extends Engine {

    Random rand	
    String txtOutput = ">"
    Repository repo
    boolean entered = false
	
    @Override
    public boolean keyTyped(char character){
        // your touch down code here
        if((int)character > 30) txtOutput += character
        return true; // return true to indicate the event was handled
    }
	
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Keys.BACKSPACE && txtOutput.length() > 1){
            if(entered){
                txtOutput = ">"
                return true
            }
            txtOutput = txtOutput.substring(0, txtOutput.length()-1)			
        }
        if(keycode == Keys.ENTER){
            if(txtOutput.length() > 1){
                process()
            }else{
                txtOutput = ">"
            }
        }
        return false;
    }	
	
    void process(){
        entered = true        
        txtOutput = txtOutput.substring(1, txtOutput.length())
        def command = txtOutput.split(" ")
        if(command[0].equals("lpm")){
            if(command[1].equals("install")){
                txtOutput = repo.lpmInstall(command[2], command[3], command[4])
            }
            if(command[1].equals("update")){
                txtOutput = repo.lpmUpdate(command[2])
            }
            return
        }
        txtOutput = ">"
    }
	
    void create(){
        repo = new Repository()
    }
    void update(float delta){}
    void render(){	
        bgColor(16)
        text(txtOutput, 0, 0, 232, 1)		
    }	
}
