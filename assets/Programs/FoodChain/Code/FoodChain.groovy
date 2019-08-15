import Food
import Supers
import SaveUtil
import leikr.GameRuntime;//haaaaaack

import org.mini2Dx.core.input.*

class FoodChain extends leikr.Engine {

    int state = 0//0=title, 1=instructions, 2=Game play, 3=gameover, 4=Acheivments
    int bSpeed = 0, btnSpeed = 0, dropSpeed = 0, blink = 0, flipNext = 0

    boolean page = false//used for getting to page 2 of isntructions
    //play variables

    boolean select = false, usingSwap = false
    int hungerSpeed = 0//The speed of hunger will increase with each level

    int megaScore = 10 // used for advancing to next level, 128 = max
    int level = 1 //might settle on 4 levels
    int lives = 3
    int available = 1//a meter of available moves
    int row=8, col=8
    def jar = new Food[col][row]
    int fruits= 0, veggies = 0, meats = 0, drinks = 0
    boolean fSuper = false, vSuper = false, mSuper = false, dSuper = false
	
    Supers supers = new Supers()
    SaveUtil saveUtil = new SaveUtil()
    int	high_score = saveUtil.loadScore()
	
    int bombs = 1, swaps = 1, bombDur = 0, bombX, bombY, swapX, swapY, goX, goY
	
    def goWave = [6,4,2,0,0,2,4,6]
	
    int shake = 0, bgX = 0, bgY = 0
    boolean aDairyQueen = false, aMeatPump = false, aGrassGreener = false, aAllOrangeJuice = false, aFive = false, aTen = false, aFifteen = false, aTwenty = false, aBombBastic = false, aYeOleSwitcheroo = false
	
    int airEaten = 0
    int newAcheivmentIcon = 0, achSpeed = 100
	
    //cursor x, y and flash
    int cx=0, cy=0, cf = 0
	
    int starving = 200
    //END play variables
	
    def init(){
        airEaten = 0
        high_score = saveUtil.loadScore()
        aDairyQueen = false
        aMeatPump = false
        aGrassGreener = false
        aAllOrangeJuice = false
        aFive = false
        aTen = false
        aFifteen = false
        aTwenty = false
        aBombBastic = false
        aYeOleSwitcheroo = false
		
        shake = 0
        bgX = 0
        bgY = 0
		
        starving = 200
        newAcheivmentIcon = 0
        achSpeed = 100
        state = 0//0=title, 1=instructions, 2=Game play, 3=gameover, 4 = achievments
        bSpeed = 0
        dropSpeed = 0
        hungerSpeed = 0 
        page = false
        //title variables
        blink = 0
        //end title variables
		
        //play variables
        flipNext = 0
        select = false
        megaScore = 10 // used for advancing to next level, 128 = max
        level = 1 
        lives = 3
        available = 1

        jar = new Food[col][row]
        fruits= 0
        veggies = 0
        meats = 0
        drinks = 0
        fSuper = false
        vSuper = false
        mSuper = false
        dSuper = false
        Supers supers = new Supers()
        bombs = 1
        swaps = 1
		
        //cursor x, y and flash
        cx=0
        cy=0
        cf=0
        //END play variables
    }
    
    @Override
    public void onPovChanged(GamePad gamePad, int povCode, PovState povState) {
        println("::: Debug POV :::")
        println("Controller $gamePad , Code: $povCode , State $povState")
    }
	
	
    @Override
    public void onButtonDown(GamePad cont, int buttonId){
        println("::: Debug Button :::")
    	println("Controller: $cont , Value: $buttonId")
//        switch(state){
//        case 2:
//            if(buttonId == btnCode("SELECT")){
//                state = 4
//                sfx("shift")
//            }
//            if(usingSwap){
//                moveCursor()
//                if(buttonId == btnCode("RIGHT_BUMPER")){
//                    doSwap(cx, cy)
//                }
//                return
//            }
//            if(buttonId == btnCode("RIGHT_BUMPER") && swaps > 0){
//                swapStart(cx, cy)
//                return
//            }
//            break;
//        case 3:
//            if(buttonId == btnCode("SELECT") ){
//                init()
//                sfx("shift")
//            }
//            break;
//        case 4:
//            if(buttonId == btnCode("SELECT")){
//                state = 2
//                sfx("shift")
//            }
//            break;
//        default:
//            break;
//        				
//        }
    }
    @Override
    public void onAxisChanged(GamePad controller, int axisCode, float buttonId) {
    	println("::: Debug Axis :::")
    	println("Controller: $controller , Axis: $axisCode , Value: $buttonId")
//        if(state == 2){
//            if (axisCode == verticalAxis()) {
//                if(buttonId == btnCode("UP") && cy > 0){
//                    cy--
//                }
//                if(buttonId == btnCode("DOWN") && cy < 7){
//                    cy++
//                }
//            }
//            if (axisCode == horizontalAxis()) {
//                if (buttonId == btnCode("RIGHT") && cx < 7) {
//                    cx++
//                }
//                if (buttonId == btnCode("LEFT") && cx > 0) {
//                    cx--
//                }
//					
//            }
//        }
				
    }
	
	
	
	
    void create(){
        loadImages()      	
       
    }
    
    //START UPDATE
    void update(float delta){
    	bSpeed++
    	dropSpeed++
        switch(state){
        case 0:
            music("title", true)
            if(keyPress("Space") || bp("SELECT")) {
                sfx("start")
                state++
                col.times{i->
                    row.times{j->
                        jar[i][j] = new Food(randInt(7), randInt(100))
                    }
                }
            }
            if(blink > 20) blink = 0
            blink++
            break;
        case 1://Instructions
            if(blink > 20) blink = 0
            blink++
            if(keyPress("Space") || bp("SELECT") ){
                sfx("start")
                stopAllMusic()
                state++
            }
        		
            if(keyPress("Left") || bp("LEFT")){
                sfx("shift")
                page = false
            }
        		
            if(keyPress("Right") || bp("RIGHT") ){
                sfx("shift")
                page = true
            }
        		
            break;
        case 2://Game play
            if(flipNext > 20) flipNext = 0
            if(cf > 20) cf = 0
            cf++
            flipNext++
        		
            //hunger per level
            switch(level){
            case 1:
                if(hungerSpeed > 100){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 2:
                if(hungerSpeed > 80){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 3:
                if(hungerSpeed > 60){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 4:
                if(hungerSpeed > 40){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 5:
                if(hungerSpeed > 30){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                if(hungerSpeed > 20){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                if(hungerSpeed > 15){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            default:
                if(hungerSpeed > 10){
                    megaScore--
                    hungerSpeed=0
                }
                break;
            }
            if(megaScore <= 0) megaScore = 0
            hungerSpeed++
        		
        		
            //CHECL AVAILABLE MOVES
            if(!movesExist()){
                if(lives > 0){
                    sfx("noMove")
                    shake = 15
                    lives--
                    resetBoard()
                    megaScore = megaScore - 46
                }else{        				
                    state=3//game over
                }
            }
            if(available > 32) available = 32
        		
            //START STATE 1 INPUT
            handleInput()
        		
            if(bombDur > 0){
                bombDur--
            }
        		
            //START STATE 1 UPDATE DROP
            updateFill()
        		
            //CHECK LVL UP
            if(megaScore >= 128){
                megaScore = 0
                level++
                high_score = level
                saveUtil.saveScore(high_score)
                lives++
                sfx("lifeUp")        			
                if(lives>9) lives = 10
            }
            if(megaScore == 0){
                starving--
                if(starving == 0){
                    starving = 200
                    lives--
                    sfx("noMove")
                    shake = 15
                }
            }
            if(megaScore > 0){
                starving = 200
            }
        		
            if(lives == 0){
                state = 3
                saveUtil.saveScore(high_score)
            }
            //CHECK ACHEIVMENTS
            checkAcheivments()
            if(newAcheivmentIcon > 0) newAcheivmentIcon--
        		
        		
            break;
        case 3://Game over
            if(keyPress("Space") ){
                init()
                sfx("shift")
            }
            if(blink > 20) blink = 0
            blink++
            break;
        case 4: //Acheivments
            if(keyPress("Space") ){
                state = 2
                sfx("shift")
            }
        		
            break;
        }
    }
    //END UPDATE
    
    //START RENDER
    void render(){	
        switch(state){
        case 0:
            image("title",0,0)
            if(blink > 10){
                text("Press Space/Select", 58, 124, 120, 1, 16)
            }else{
                text("Press Space/Select", 58, 124, 120, 1, 32)
            }
            text("High Score: $high_score", 58, 108, 120, 1, 16)
            text("High Score: $high_score", 59, 109, 120, 1, 32)
        		
    			
            break;
        case 1:
            drawInstructions(page)
            break;
        case 2://game play
            //background image. Obvi
            if(shake>0){
                bgX = randInt(-2, 2)
                shake--
            }else{
                bgX = 0
            }
            image("bg",bgX,bgY)
        		
            //Draws the items in the jar
            col.times{i->
                row.times{j->
                    if(i ==cx && j==cy){
                        if(cf > 10){
                            jar[i][j].draw(screen,(int)(96+ i*16),(int)(16+j*16), true)
                        }else{
                            jar[i][j].draw(screen,(int)(96+ i*16),(int)(16+j*16), false)
                        }	    				
                    }else{    				
                        jar[i][j].draw(screen,(int)(96+ i*16),(int)(16+j*16))
                    }
                }
            }
	    		
            //draw cursor
            drawCursor()
	    		
            //Draw health
            lives.times{
                sprite(80, 8, (int)(8*it+8) )
            }
	    		
            //Draw container scores
            drawScores()
	    		
            //Draw megaScore
            drawColor(23)	    		
            rect(96, 7, megaScore, 4, true)
	    		
            //draw available
            if(available > 15) drawColor(6)
            rect(232, 144, 2, -available*4, true)
            if(available <= 4){
                sprite(81, 229, 148)
            }
	    		
            if(megaScore == 0 && cf > 10){
                sprite(81, 90, 5)
            }
	    		
            //Draw bombs and swaps
            bombs.times{
                if(it > 4){
                    sprite(82, 80, (int)(8*it-32))
                }else{
                    sprite(82, 70, (int)(8*it+8))
                }
            }
            swaps.times{
                if(it > 4){
                    sprite(83, 80, (int)(8*it+31))
                }else{
                    sprite(83, 70, (int)(8*it+71))
                }
            }
	    		
            if(bombDur > 0){
                sprite(2, (int)(96+bombX*16), (int)(16+bombY*16), 3)
            }
	    		
            if(newAcheivmentIcon > 0){
                if(cf>10)
                spriteSc(69, 98, 150, 0.0f)
                else
                spriteSc(69, 98, 150, 0.5f)
            }
	    		
            //temp
            text("Level: $level", 8, 150, 18)
	    	        		
            break;
        case 3:// Gameover condition
            8.times{
                sprite(it, 56+it*16, 64+goWave[it], 1)
            }
            text("GAME OVER", 0, 32, 240, 1, 32)
            drawColor(50)//black
        		
            rect(56, 122, 124, 60, true)
            if(blink > 10){        			
                text("Press Space/Select", 58, 124, 120, 1, 16)
            }else{
                text("Press Space/Select", 58, 124, 120, 1, 32)
            }
            text("High Score: $high_score", 58, 108, 120, 1, 16)
            text("High Score: $high_score", 59, 109, 120, 1, 32)
        		
            break;
        		
        case 4: //Acheivments
            image("awards", 0,0)
            text("Acheivments", 0, 16, 240, 1, 32)
            drawAcheivments()
            if(airEaten>0) text("Air eaten: $airEaten", 8, 130, 240, 0, 16)
				

            break;
        }
    }
    
    //END RENDER
    
    
    
    def movesExist(){
    	def match = 0
    	col.times{i->
            row.times{j->
                int tp = jar[i][j].type
                if((i > 0 && jar[i-1][j].type != tp || i == 0) 
                    && (i < col-1 && jar[i+1][j].type != tp || i == 7) 
                    && (j > 0 && jar[i][j-1].type != tp || j == 0) 
                    && (j< row-1 && jar[i][j+1].type != tp || j == 7) ) {
                    //no matches yet
                }else{
                    match++
                }
            }
        }
        available = match
        boolean swpBmbAvl = false
        if(match==0){
            jar.each{
                it.each{ p->
                    if(p.type == 10 || p.type == 11)swpBmbAvl = true
                }				
            }
        }
        if(match == 0 && !mSuper && !vSuper && !fSuper && !dSuper && bombs == 0 && swaps == 0 && !swpBmbAvl) return false//No moves
        return true
    }
    
    
    def checkMatches(x,y){
    	def tp = jar[x][y].type//temp type
    	if(tp==8)airEaten++
        if(tp == 10){
            bombs++
            if(bombs>10)bombs=10
            jar[x][y].type = 8
            sfx("item")
            return
        }
        if(tp == 11){
            swaps++
            if(swaps>10)swaps=10
            jar[x][y].type = 8
            sfx("item")
            return
        }
        //If no matches on any sides in middle.
        if(	(x > 0 && jar[x-1][y].type != tp || x == 0) 
            && (x < col-1 && jar[x+1][y].type != tp || x == 7) 
            && (y > 0 && jar[x][y-1].type != tp || y == 0) 
            && (y < row-1 && jar[x][y+1].type != tp || y == 7) ) {
            sfx("select")
            return
        }

	    
    	jar[x][y].type = 8
    	sfx("eat")
    	int score = matchesScoring(tp, x, y)
      	
    	foodTypeScoreCheck(tp, score)
    }
    
    def checkMatchesExplode(x,y){
    	def tp = jar[x][y].type//temp type
    	if(tp == 10){
            bombs++
            if(bombs>10)bombs=10
            jar[x][y].type = 8
            return
        }
        if(tp == 11){
            swaps++
            if(swaps>10)swaps=10
            jar[x][y].type = 8
            return
        }
        //If no matches on any sides in middle.
        if(	(x > 0 && jar[x-1][y].type != tp || x == 0) 
            && (x < col-1 && jar[x+1][y].type != tp || x == 7) 
            && (y > 0 && jar[x][y-1].type != tp || y == 0) 
            && (y < row-1 && jar[x][y+1].type != tp || y == 7) ) {
            jar[x][y].type = 8
            foodTypeScoreCheck(tp, 2)
            return
        }
	    
    	jar[x][y].type = 8
    	
        int score = matchesScoring(tp, x, y)
        foodTypeScoreCheck(tp, score)
    }
    
    def matchesScoring(tp, x, y){    
    	int score = -3//sets the score correctly for the i variable offset
    	
    	//Scan the x axis (horiz)
    	int i = 1
    	while(x-i >= 0){
            if(jar[x-i][y].type == tp){
                jar[x-i][y].type = 8
            } else{
                break
            }
            i++
    	}
    	score += i
    	i = 1
    	while(x+i < col){
            if(jar[x+i][y].type == tp) {
                jar[x+i][y].type = 8
            }else{
                break
            }
            i++
    	}
    	score += i
    	//Scan the y axis (verti)
    	i = 1
    	while(y-i >= 0){
            if(jar[x][y-i].type == tp){
                jar[x][y-i].type = 8
            } else{
                break
            }
            i++
    	}
    	score += i
    	i = 1
    	while(y+i < row){
            if(jar[x][y+i].type == tp) {
                jar[x][y+i].type = 8
            }else{
                break
            }
            i++
    	}
    	
    	//UPDATE SCORE AND MEGASCORE APPLYING MULTIPLYER
    	score += i
    	if(score < 3)  megaScore += score
    	if(score == 3) megaScore += score * 3
    	if(score == 4) megaScore += score * 3
    	if(score == 5) megaScore += score * 5
    	if(score == 6) megaScore += score * 6
    	if(score > 6)  megaScore += score * 10
    	
    	return score
    }
    
    def foodTypeScoreCheck(t, score){	
    	//if drink type
    	if(t == 0 || t == 1) {
            drinks += score
            if(drinks >= 41){
                drinks = 41
                dSuper = true
            } 
    	}
    	//if veggie type
    	if(t == 2 || t == 3) {
            veggies += score
            if(veggies >= 41) {
                veggies = 41
                vSuper = true
            }
    	}
    	//if fruit type
    	if(t == 4 || t == 5) {
            fruits += score
            if(fruits >= 41) {
                fruits = 41
                fSuper = true
            }
    	}
    	//if meat type
    	if(t == 6 || t == 7) {
            meats += score
            if(meats >= 41) {
                meats = 41
                mSuper = true
            }
    	}
    }
    
    //RESET BOARD
    def resetBoard(){
        col.times{i->
            row.times{j->
                jar[i][j] = new Food(randInt(7), randInt(100))
            }
        }		
    }
    //END RESET BOARD
    
    //BOMB POWER
    def explode(x,y){
    	bombDur = 10
    	shake = 10
    	
    	bombX = x-1
    	bombY = y-1
    	
    	int a = x-1
    	int b = y-1
    	
    	if(a<0)a=0
    	if(b<0)b=0
    	
    	3.times{
            3.times{
                if(a>7)a=7
                if(b>7)b=7
                checkMatchesExplode(a, b)
                a++
            }
            a-=3
            b++
    	}    	
    }
    //END BOMB POWER
    
    //SWAP POWER
    def swapStart(x,y){
    	swapX = x
    	swapY = y
    	usingSwap = true
    }
    def doSwap(x,y){
    	def temp = jar[swapX][swapY].type
    	jar[swapX][swapY].type = jar[x][y].type
    	jar[x][y].type = temp
    	usingSwap = false
    	swaps--
    	sfx("swap")
    }
    //END SWAP POWER
    
    //STATE 1 INPUT 
    def handleInput(){
    	//Debug		
        if(keyPress("C")){
            checkAvailableMoves()
        }
        //End debug
		
        //ENTER ACHEIVMENT PAGE
        if(keyPress("Space") ) {
            state = 4
            sfx("shift")
        }  
		
        if(usingSwap){
            moveCursor()
            if(keyPress("W")){
                doSwap(cx, cy)
            }
            return
        }
		
        //Check for Super usage input
        if(mSuper && (keyPress("X") || bp("X") )){
            sfx("feast")
            mSuper = false
            megaScore += supers.useSuper(jar, 6, 7)
            meats = 0
        }
        if(vSuper && (keyPress("Y") || bp("Y"))){
            sfx("feast")
            vSuper = false
            megaScore += supers.useSuper(jar, 2, 3)
            veggies = 0
        }
        if(fSuper && (keyPress("Z") || bp("X") )){
            sfx("feast")
            fSuper = false
            megaScore += supers.useSuper(jar, 4, 5)
            fruits = 0
        }
        if(dSuper && (keyPress("B") || bp("B") )){
            sfx("feast")
            dSuper = false
            megaScore += supers.useSuper(jar, 0, 1)
            drinks = 0
        }
        //Bombs or Swaps
        if( (keyPress("Q") || (bp("LEFT_BUMPER"))) && bombs > 0){
            sfx("bomb")
            bombs--
            explode(cx, cy)
        }
        if( (keyPress("W") && swaps > 0)){
            swapStart(cx, cy)
            return
        }
		
        //Cursor movement
        if(!select){
            moveCursor()
            if((keyPress("A")|| bp("A"))) {
                select = true
                dropSpeed = 0
            }
        }else{			
            checkMatches(cx, cy)
			
            select = false
        }
    }
    //END STATE 1 INPUT
    
    
    //MOVEMENT
    def moveCursor(){
    	if(keyPress("Right") && cx < 7) {
            cx ++
        }
        if(keyPress("Left") && cx > 0) {
            cx --
        }
        if(keyPress("Up") && cy > 0) {
            cy--
        }
        if(keyPress("Down") && cy < 7){
            cy++
        } 
    }
    //END MOVEMENT
    
    //STATE 1 UPDATE DROP
    //updateDrop checks every position to see if an item can be dropped a row.
    def updateFill(){
    	if(dropSpeed > 6){
            dropSpeed = 0
            col.times{i->
                if(jar[i][0].type==8) jar[i][0] = new Food(randInt(7), randInt(100))
                row.times{j->
                    if(j==row)return
                    if(j+1 < row && jar[i][j].type!=8 && jar[i][j+1].type==8){
                        def tmp = jar[i][j]
                        jar[i][j] = jar[i][j+1]
                        jar[i][j+1] = tmp   
                    }
                }
            }
        }
    }
    //END STATE 1 UPDATE DROP
    
    //Draw cursor
    def drawCursor(){
    	if(usingSwap){
            drawColor(9)
            rect((int)(96+cx*16), (int)(16+cy*16), 16, 16)
            drawColor(20)
            rect((int)(96+swapX*16), (int)(16+swapY*16), 16, 16)
            //Draw line between the swapping tiles
            drawColor(22)
            line((int)(96+swapX*16)+8, (int)(16+swapY*16)+8, (int)(96+cx*16)+8, (int)(16+cy*16)+8)
            return
    	}
    	if(cf>10){
            drawColor(21)
            rect((int)(96+cx*16), (int)(16+cy*16), 16, 16)
            drawFeastAvailable(true)
        }else{
            drawColor(24)
            rect((int)(96+cx*16), (int)(16+cy*16), 16, 16)
            drawFeastAvailable(false)
        }
    }
    
    def drawFeastAvailable(boolean a){
    	if(a){
            if(meats == 41)
            spriteSc(65, 29, 55, 0)
            if(veggies == 41)
            spriteSc(66, 53, 55, 0)
            if(fruits == 41)
            spriteSc(67, 29, 120, 0)
            if(drinks == 41)
            spriteSc(68, 53, 120, 0)
        }else{
            if(meats == 41)
            spriteSc(65, 29, 55, 0.5f)
            if(veggies == 41)
            spriteSc(66, 53, 55, 0.5f)
            if(fruits == 41)	
            spriteSc(67, 29, 120, 0.5f)
            if(drinks == 41)
            spriteSc(68, 53, 120, 0.5f)
        }
    }
    
    //Draw the container scores:
    def drawScores(){
        drawColor(23)
        rect(24, 48, 16, -meats,   true)//meats
        drawColor(20)
        rect(48, 48, 16, -veggies, true)//veggies
        drawColor(15)
        rect(24, 112, 16, -fruits,  true)//fruits
        drawColor(32)
        rect(48, 112, 16, -drinks,  true)//drinks
    }
    
    def drawInstructions(page){
    	drawColor(7)
    	rect(0,0,240,160, true)//bg gray  
    	if(page){
            text("How to Play. Pg 2", 0, 0, 240, 1, 16)
            sprite(34,0,0 )
    		
            sprite(10, 8, 16, 1)
            text("Collect bombs to blast away 3x3 squares with chain reaction explosions! Use `Q` or Left Bumper.", 32, 16, 200, 32)
    		
    		
            sprite(11, 8, 60, 1)
            text("Collect swap tiles to be able to swap any two tiles on the board! Use `W` or Right Bumper.", 32, 60, 200, 32)
    		
            text("Find the biggest combos to earn the most points to fill your hunger meter and progress to the next level.", 0, 100, 240, 32)
    	}else{
            image("instruct", 0,0)
			
            text("How to Play. Pg 1", 0, 0, 240, 1, 16)
			
            text("Fill these to activate a Feast!\nEach container holds a food type: Drinks, Veggies, Fruits or Meats.", 38,18, 200, 32)
			
            //Draw types
            8.times{
                sprite(it, 14 + (it*18), 62, 1)
            }
			
            //Draw arrows
            2.times{
                sprite(32+it, 14+(it*9), 80)
            }
            2.times{
                sprite(34+it, 14+(it*9), 88)
            }
            text("Move the cursor with Arrow Keys/D-pad\n`A` to make selection.",38,80, 200, 32 )
			
            //Hearts
            sprite(80, 8, 108)
            text("Hearts are used up when you run out of moves. Each level earns an extra life!", 16, 108, 200, 32)
            sprite(33, 232, 0)
    	}  
    	
        if(blink > 10){
            text("Press Space/Select", 58, 142, 120, 1, 16)
			
			
        }else{
            text("Press Space/Select", 58, 142, 120, 1, 32)
        }
    }
    
    def checkAcheivments(){
    	if(level >= 5 && !aFive) {
            sfx("award")
            aFive = true
            newAcheivmentIcon = achSpeed
    	}
    	if(level >= 10 && !aTen) {
            sfx("award")
            aTen = true
            newAcheivmentIcon = achSpeed
    	}
    	if(level >= 15 && !aFifteen){
            sfx("award")
            aFifteen = true
            newAcheivmentIcon = achSpeed
    	} 
    	if(level >= 20 && !aTwenty){
            sfx("award")
            aTwenty = true
            newAcheivmentIcon = achSpeed
    	} 
    	
    	if(bombs == 10 && !aBombBastic){
            sfx("award")
            aBombBastic = true
            newAcheivmentIcon = achSpeed
    	} 
    	if(swaps == 10 && !aYeOleSwitcheroo){
            sfx("award")
            aYeOleSwitcheroo = true
            newAcheivmentIcon = achSpeed
    	} 
    	
    	if(meats >= 40 && !aMeatPump) {
            sfx("award")
            aMeatPump = true
            newAcheivmentIcon = achSpeed
    	}
    	if(veggies >= 40 && !aGrassGreener) {
            sfx("award")
            aGrassGreener = true
            newAcheivmentIcon = achSpeed
    	}
    	if(fruits >= 40 && !aAllOrangeJuice) {
            sfx("award")
            aAllOrangeJuice = true
            newAcheivmentIcon = achSpeed
    	}
    	if(drinks >= 40 && !aDairyQueen) {
            sfx("award")
            newAcheivmentIcon = achSpeed
            aDairyQueen = true
    	}
    }

    def drawAcheivments(){
        if(aFive) {sprite(19, 16, 40, 1)}else{sprite(23, 16, 40, 1)}
        if(aTen) {sprite(20, 64, 40, 1)}else{sprite(23, 64, 40, 1)}
        if(aFifteen) {sprite(21, 112, 40, 1)}else{sprite(23, 112, 40, 1)}
        if(aTwenty) {sprite(22, 160, 40, 1)}else{sprite(23, 160, 40, 1)}
		
        if(aBombBastic) {sprite(10, 16, 104,1)}else{sprite(23, 16, 104, 1)}
        if(aYeOleSwitcheroo){ sprite(11, 64, 104,1)}else{sprite(23, 64, 104, 1)}
        if(aMeatPump) {sprite(13, 112, 104,1)}else{sprite(23, 112, 104, 1)}
        if(aAllOrangeJuice) {sprite(15, 160, 104,1)}else{sprite(23, 160, 104, 1)}
		
        if(aDairyQueen) {sprite(12, 208, 40,1)}else{sprite(23, 208, 40, 1)}
        if(aGrassGreener){ sprite(14, 208, 104,1)}else{sprite(23, 208, 104, 1)}
    }
	
    boolean bp(b){
        if(button(b) && bSpeed > btnSpeed ){
            bSpeed = 0
            return true
        }
        return false
    }

}

