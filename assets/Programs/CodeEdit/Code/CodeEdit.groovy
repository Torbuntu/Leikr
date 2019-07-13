import groovy.io.FileType

class CodeEdit extends leikr.Engine {

	def list = []
	def codeFile
	
	int cursor = 0
	int y = 0
	
	 private AssetManager assetManager;
    private UiContainer uiContainer;

    void create(){
        codeFile = new File("Programs/CodeEdit/Code/CodeEdit.groovy").text
		//Create fallback file resolver so we can use the default mini2Dx-ui theme
        FileHandleResolver fileHandleResolver = new FallbackFileHandleResolver(new ClasspathFileHandleResolver(), new InternalFileHandleResolver());

        //Create asset manager for loading resources
        assetManager = new AssetManager(fileHandleResolver);

        //Add mini2Dx-ui theme loader
        assetManager.setLoader(UiTheme.class, new UiThemeLoader(fileHandleResolver));

        //Load default theme
        assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);

        uiContainer = new UiContainer(this, assetManager);
    }
    void update(float delta){
         if(!assetManager.update()) {
                //Wait for asset manager to finish loading assets
                return;
        }
        if(!UiContainer.isThemeApplied()) {
                UiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
        }
        uiContainer.update(delta);
    }
    void render(){	
		codeFile.eachWithIndex{it, i -> 
			def color = i == cursor ? 16 : 13
			
			//text(it, i*8, y, color)
		}
			uiContainer.render(g);
		
		//Mouse
		drawColor(16)
		line((int)mouseX(), (int)mouseY(), (int)mouseX()+4, (int)mouseY()+4)
    }	
}

