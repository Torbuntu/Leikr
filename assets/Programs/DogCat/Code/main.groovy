class DogCat extends leikr.Engine {

	Player p = new Player()
    void create(){
        loadMap("map")
        p.makePlayerCat(screen)
    }
    void update(float delta){
        p.update(delta)
    }
    void render(){	
		map() 
		
		p.draw(screen)
    }	
}

