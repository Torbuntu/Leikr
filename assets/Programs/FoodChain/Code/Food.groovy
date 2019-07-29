class Food{
	int type//0 = coffee, 7=drumstick, 8 = removed, 9 = bomb, 10 = swap
	
	Food(t, rand){
		switch(rand){
			case 1:
				type = 10
				break;
			case 2:
				type = 11
				break;
			default:
				type = t
				break;
		}
	}

	def draw(screen, int x, int y){
		if(type==8)return

		screen.sprite(type, x, y, false, false, 1)
	}
	//Adding the flip for when the cursor is hovering over
	def draw(screen, int x, int y, flip){
		if(type == 8)return

		screen.sprite(type, x, y, flip, false, 1)	
	}
}
