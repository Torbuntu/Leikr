class SpriteDemo extends leikr.Engine {
	float rotation = 0

    void update(float delta){
        rotation = (rotation > 360) ? 1 : rotation + 1.5
    }
    void render(){	
		bgColor(4)
		drawLineSegment(7, 110, 0, 110, 160)
		drawString(7, "Comments are in the code.", 10, 0)
		
		
		drawString(7, "Regular Sprites", 10, 140)
		/*
		The most generic sprite render simply draws an ID at an X and Y coord.
		
		This is the same as `sprite 0, 10, 10, false, false`
		*/
		sprite 0, 10, 10
		
		//Adding some flip values for flipX and flipY
		sprite 0, 10, 20, true, false
		sprite 0, 10, 30, false, true
		sprite 0, 10, 40, true, true
				
		//Adding a bit of rotation in degrees. The rotation must be a float variable
		sprite 0, 30, 10, rotation
		
		//And with rotation?
		sprite 0, 30, 20, true, false, rotation
		sprite 0, 30, 30, false, true, rotation
		sprite 0, 30, 40, true, true, rotation
		
		/*
		These methods can also be used with a size parameter.
		The size parameter is an integer from 0 to 3.
		0 - 8x8
		1 - 16x16
		2 - 32x32
		3 - 64x64		
		*/ 
		
		//Most generic sprite render. This is the same as `sprite 0, 10, 10, false, false`
		sprite 1, 60, 10, 1
		
		//Adding some flipp values for flipX and flipY
		sprite 1, 60, 30, true, false, 1
		sprite 1, 60, 50, false, true, 1
		sprite 1, 60, 70, true, true, 1
				
		//Adding a bit of rotation in degrees. The rotation must be a float variable
		sprite 1, 90, 10, rotation, 1
		
		//sprite id, x, y, flipX, flipY, rotation, size
		sprite 1, 90, 30, true, false, rotation, 1
		sprite 1, 90, 50, false, true, rotation, 1
		sprite 1, 90, 70, true, true, rotation, 1
		
		
		/*
		One of the sprite methods takes a list of sprite ID's as a parameter.
		
		This method only has two variants. One with, and one without the flipX
		and flipY params.
				
		*/
		sprite([4,5,6,7], 10, 80, 2, 2)
		
		sprite([4,5,6,7], 30, 80, 2, 2, true, false)
		
		
		sprite([4,5,6,7], 20, 100, 2, 2, 1)
		
		sprite([4,5,6,7], 60, 100, 2, 2, true, false, 1)
		
		
		
		drawString(7, "Scaled Sprites", 150, 140)
		/*
		The spriteSc method is used to draw smaller sprites scaled up 
		or for scaling larger sprites down.
		
		This can be used for some fun animations. Such as enemy deaths.

		Drawn next to a regular sprite, another is scaled to 2 times the size
		When only one scale param is given, it scaled in both directions.
		*/
		sprite 0, 120, 30 //normal
		spriteSc 0, 120, 14, 2f
		spriteSc 1, 116, 34, 0.5, 1
		
		//Now with two different directions. Notice going half the width.
		spriteSc 0, 140, 18, 0.5f, 3f
		spriteSc 1, 140, 40, 4, 0.1, 1
		
		//rotation of course
		spriteSc 0, 170, 20, 3f, 0.7f, rotation
		spriteSc 1, 200, 20, 3f, 0.3f, rotation, 1
		
		//And with the flips!
		spriteSc 0, 170, 60, 3f, 0.7f, true, false
		spriteSc 1, 200, 60, 3f, 0.3f, false, true, 1
		
		spriteSc 0, 170, 100, 3f, 0.7f, true, false, rotation
		spriteSc 1, 200, 100, 3f, 0.3f, false, true, rotation, 1
    }
}

