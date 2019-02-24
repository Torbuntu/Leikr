//With help from: https://github.com/digitsensitive/tic-80-tutorials/tree/master/tutorials/breakout
import leikr.Engine
class Breakout extends Engine {

    int bgColor 
    int score 
    int lives 
    def player = [:]
    def ball = [:]
    def bricks = []
    int brickCountWidth
    int brickCountHeight 
    Random rand
	
    void newGame(){
        bgColor = 0
        score = 0
        lives = 3
        player = [ x: (240/2)-12, y: 120, width: 24, height: 4, color: 3, speed: [x: 0, max: 4]]
        ball = [x: player.x+(player.width/2)-1.5, y: player.y-5, width: 3, height: 3, color: 14, deactive: true, speed: [x: 0, y: 0, max: 0.8]]
        brickCountWidth = 19
        brickCountHeight = 12
        bricks = []
        for (int i in 0..brickCountHeight){
            for( int j in 0..brickCountWidth){
                def brick = [
                    sid: j,
                    x: 10+j*11,
                    y: 10+i*5,
                    width: 8,
                    height: 3
                ]
                bricks.add(0, brick)
            }
        }
    }
	
    void input(){
        float sx = player.speed.x
        float smax = player.speed.max

        // move to left
        if (key("Left") ){
            if (sx>-smax ){
                sx=sx-2
            }else{
                sx=-smax
            }
        }

        //move to right
        if (key("Right") ){
            if (sx<smax){
                sx=sx+2
            }else{
                sx=smax
            }
        }

        player.speed.x=sx
        player.speed.max=smax

        if (ball.deactive){
            ball.x = player.x+(player.width/2)-1.5
            ball.y = player.y-5

            if (key("Space") ){
                ball.speed.x = Math.floor(Math.random())*2-1
                ball.speed.y = -1.5
                ball.deactive = false
            }
        }
    }
	
    void lupdate(){
        float px = player.x
        float psx = player.speed.x
        float smax = player.speed.max

        //update player position
        px=px+psx

        // reduce player speed
        if ( psx != 0 ) {
            if (psx > 0 ){
                psx=psx-1
            }else{
                psx=psx+1
            }
        }

        player.x=px
        player.speed.x=psx
        player.speed.max=smax

        // update ball position
        ball.x = ball.x + ball.speed.x
        ball.y = ball.y + ball.speed.y

        // check max ball speed
        if (ball.speed.x > ball.speed.max){ 
            ball.speed.x = ball.speed.max
        }	
    }
	
    void collisions(){
        // player <-> wall collision
        playerWallCollision()

        // ball <-> wall collision
        ballWallCollision()

        // ball <-> ground collision
        ballGroundCollision()

        // player <-> ball collision
        playerBallCollision()

        // ball <-> brick collision
        ballBrickCollision()
 		
    }
	
	
    void playerWallCollision(){
        if (player.x < 0 ){
            player.x = 0
        }else if( player.x+player.width > 240 ){
            player.x = 240 - player.width
        }
    }

    void ballWallCollision(){
        if (ball.y < 0){
            // top
            ball.speed.y = -ball.speed.y    
        }else if (ball.x < 0 ) {
            // left
            ball.speed.x = -ball.speed.x
        }else if ( ball.x > 240 - ball.width) {
            // right
            ball.speed.x = -ball.speed.x
        }
    }

    void ballGroundCollision(){
        if (ball.y > 136 - ball.width ){
            // reset ball
            ball.deactive = true
            // loss a life
            if (lives > 0 ){
                lives--
            }
        }
    }

    void playerBallCollision(){
        if (collide(player,ball)) {
            ball.speed.y = -ball.speed.y
            ball.speed.x = ball.speed.x + 0.3*player.speed.x
        }
    }

    def collide(a,b){
        // get parameters from a and b
        float ax = a.x
        float ay = a.y
        float aw = a.width
        float ah = a.height
        float bx = b.x
        float by = b.y
        float bw = b.width
        float bh = b.height

        // no collision
        return (ax < bx + bw) && (ax + aw > bx) && (ay < by + bh) && (ah + ay > by)
    }

    void ballBrickCollision(){
        def iter = bricks.iterator()
        while (iter.hasNext()) {
            // get parameters
            def it = iter.next()
            float x = it.x
            float y = it.y
            float w = it.width
            float h = it.height

            // check collision
            if (collide(ball, it)) {
                // collide left or right side
                if ((y < ball.y && ball.y < y+h) && (x < ball.x || ball.x < x+w)) {
                    ball.speed.x = -ball.speed.x
                }
                // collide top or bottom side		
                if ((ball.y < y+h || ball.y > y) && (x < ball.x && ball.x < x+w) ){
                    ball.speed.y = -ball.speed.y
                }
                score++
                bricks.remove(it)	
                return // This has to return or else null is thrown 
            }
        }
    }

    void draw(){
        drawGameObjects()
        drawGUI()
    }

    void drawGameObjects(){
        // draw player
        drawColor(player.color)
        rect(player.x.toFloat(), player.y.toFloat(), player.width.toFloat(), player.height.toFloat())

        // draw ball
        drawColor(ball.color)
        rect(ball.x.toFloat(), ball.y.toFloat(), ball.width.toFloat(), ball.height.toFloat())
	 	
        // draw bricks
        bricks.each{
            sprite(it.sid, it.x.toFloat(), it.y.toFloat())
        }
	
    }

    void drawGUI(){
        text("Score: "+ score,5,1,7)
        text("Lives: "+ lives,100,0,15)
    }

    void gameOver(){
        text("Game Over",((200/2)-6*4.5).toFloat(),(100/2).toFloat(), 15)
        sprite(0,(240/2-4).toFloat(),(136/2+10).toFloat())
        if (key("Space")) {
            newGame()
        }
    }
	
    @Override
    public boolean mouseMoved(int x, int y) {
    	println x/3 + " : " +y/3
        return false;
    }

    void create(){		
        rand = new Random()		
        newGame()
    }

    void update(float delta){
        input()
        if (lives>0){
            lupdate()
            collisions()		
        }
    }
	
    void render(){	
        if(lives>0){
            draw()
        }else{
            gameOver()
        }
    }	
}
