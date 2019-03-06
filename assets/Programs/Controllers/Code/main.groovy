import leikr.Engine
class Controllers extends Engine {
	
	int buttons = 12
	int index = 0
	def isSet = [:]
	def settings = [:]
	
	
	
	public static final String LGPM = "lgpm"; //leikr gamepad mapping

    public static Preferences prefs = Gdx.app.getPreferences(LGPM);

    public static String loadControllerMappings() {
        return prefs.getString("controllerMappings", "");
    }

    public static void saveControllerMappings(String json) {
        prefs.putString("controllerMappings", json);
        prefs.flush();
    }
	
	
	private void switchStep() {
        switch (currentStep) {
            case 0:
                mappings.resetMappings(controller);
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move RIGHT");
                inputToRecord = MyControllerMapping.AXIS_HORIZONTAL;
                break;
            case 1:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move LEFT");
                inputToRecord = MyControllerMapping.AXIS_HORIZONTAL;
                break;
            case 2:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move DOWN");
                inputToRecord = MyControllerMapping.AXIS_VERTICAL;
                break;
            case 3:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move UP");
                inputToRecord = MyControllerMapping.AXIS_VERTICAL;
                break;
            case 4:
            case 5:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "X");
                inputToRecord = MyControllerMapping.BUTTON_X;
                break;
            case 6:
            case 7:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Y");
                inputToRecord = MyControllerMapping.BUTTON_Y;
                break;
            case 8:
            case 9:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "A");
                inputToRecord = MyControllerMapping.BUTTON_A;
                break;
            case 10:
            case 11:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "B");
                inputToRecord = MyControllerMapping.BUTTON_B;
                break;
            case 12:
            case 13:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "ENTER/START");
                inputToRecord = MyControllerMapping.BUTTON_START;
                break;
            case 14:
            case 15:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "ESCAPE/SELECT");
                inputToRecord = MyControllerMapping.BUTTON_SELECT;
                break;
            case 16:
            case 17:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Left Bumper");
                inputToRecord = MyControllerMapping.BUTTON_LBUMPER;
                break;
            case 18:
            case 19:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Right Bumper");
                inputToRecord = MyControllerMapping.BUTTON_RBUMPER;
                break;
            default:
                instructionLabel.setText("Finished");
                ControllerMappingManager.saveControllerMappings(mappings.toJson().toJson(JsonWriter.OutputType.json));
                skipButton.setText("OK");

                inputToRecord = -1;
        }
    }
	
	
    void create(){
    	isSet = [left: false, right: false, up: false, down: false, lb: false, rb: false, select: false, start: false, a:false, b:false, x:false, y:false]
    }
    void update(float delta){}
    void render(){	
        bgColor(16)
        
        text("Button " + isSet.left, 0, 8, 232, 1) 
        
    }	
}
