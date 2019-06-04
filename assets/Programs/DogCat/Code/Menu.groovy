class Menu {

	boolean activeMenu = true
	
	def drawMenu(screen){
		screen.text("Press ENTER to start!", 70, 50, 32)
		screen.sprite(0, 120, 80, 2)
	}
}
