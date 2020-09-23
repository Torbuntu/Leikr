class MapDemo extends leikr.Engine {
	
	boolean grid = false, selected = false, layer = false
	int id = -1
    void create(){
        loadMap("map")
    }
    void update(float delta){
    	if(keyPress("G"))grid = !grid
		if(keyPress("S")){
			selected = false
			id = -1
		}
		
    	if(mouseClick()){
    		if(!selected){
    			id = getIntId(mouseX(), mouseY())
    			selected = true
    		}else{
    			setMapTile(id, mouseX()/16, mouseY()/16, 1)
    		}
    	}
    	if(keyPress("L"))layer = !layer
    }
    void render(){	
    	bgColor("85,180,255")
		if(layer) {
			drawMap(0,0,1)
			drawString(7, "Layer 1", 0, 150)
		}else {
			drawMap()
			drawString(7, "All Layers", 0, 150)
		}
		drawString(8, "<- ID: ${getIntId(mouseX(),mouseY())}", mouseX(), mouseY())
		if(selected){
			drawString(7, "Selected: $id\nPress `S` to unselect", 0,0)
		}else{
			drawString(7, "Click a tile to copy", 0,0)
		}
    	if(grid) drawGrid()
    }
    
    void drawGrid(){
    	//vert
    	10.times{it->
    		drawLineSegment(7, 0, it*16, 240, it*16 )
    	}
    	//horiz
    	15.times{it->
    		drawLineSegment(7, it*16, 0, it*16, 160)
    	}
    }
    
    /*
    Returns the ID given the x,y in tile map coords.
    To get the tile map coordinates from the screen coordinates
    we need to divide by the size of the tiles. In this demo
    we use 16x16 tiles.
    */
    int getIntId(x,y){
    	if(layer) return getMapTile(floor(x/16), floor(y/16), 1) 
    	else return getMapTile(floor(x/16), floor(y/16))
    }
}

