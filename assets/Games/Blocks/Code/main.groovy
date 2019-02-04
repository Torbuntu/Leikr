//With help from: https://github.com/digitsensitive/tic-80-tutorials/tree/master/tutorials/breakout
import leikr.Engine
class Blocks extends Engine {
	Random rand
	def pieceStructures = []
	def sequence
	int gridXCount 
    int gridYCount 

    int pieceXCount 
    int pieceYCount 
    
    def pieceX
    def pieceY
    def pieceRotation
    def pieceType
    
    float timerLimit 
    def timer
    def inert
    
    def canPieceMove(testX, testY, testRotation){
		for (int x in 1..pieceXCount){
		    for (int y in 1..pieceYCount){
		        def testBlockX = testX + x
		        def testBlockY = testY + y

		        if (pieceStructures[pieceType][testRotation][y][x] != 'w' && (testBlockX < 1 || testBlockX > gridXCount || testBlockY > gridYCount || inert[testBlockY][testBlockX] != 'w')){
		            return false
		        }
		    }
		}
		
		return true
	}
	
	void newSequence(){
		sequence = []
		for (int pieceTypeIndex in 1..pieceStructures.size()){
		    def position = rand.nextInt(sequence.size() + 1)
		    sequence.add(position, pieceTypeIndex)            
		}
	}
	
	void newPiece(){
        pieceX = 3
        pieceY = 0
        pieceRotation = 1
        pieceType = sequence.remove(0)

        if (sequence == 0){
            newSequence()
        }
    }
    
    void reset(){
        inert = [:]
        for (int y in 1..gridYCount){
            inert[y] = [:]
            for (int x in 1..gridXCount){
                inert[y][x] = 'w'
            }
        }

        newSequence()
        newPiece()

        timer = 0
    }
    
    void lupdate(dt){	
		timer = timer + dt
		if (timer >= timerLimit ){
		    timer = timer - timerLimit

		    def testY = pieceY + 1
		    if (canPieceMove(pieceX, testY, pieceRotation) ){
		        pieceY = testY
		    }else{
		        // Add piece to inert
		        for (int y in 1..pieceYCount){
		            for(int x in 1..pieceXCount){
		                def block = pieceStructures[pieceType][pieceRotation][y][x]
		                if (block != 'w'){
		                    inert[pieceY + y][pieceX + x] = block
		                }
		            }
		        }

		        // Find complete rows
		        for (int y in 1..gridYCount ){
		            def complete = true
		            for (int x in 1..gridXCount ){
		                if (inert[y][x] == 'w') {
		                    complete = false
		                }
		            }
		            
		            if (complete) {
		               for (int removeY = y; removeY <= 2; removeY--){
		                    for (int removeX in 1..gridXCount ){
		                        inert[removeY][removeX] = inert[removeY - 1][removeX]
		                    }
		                    
		                }
		            
		                for (int removeX in 1..gridXCount ){
		                    inert[1][removeX] = 'w'
		                }
		            }
		        }

		        newPiece()

		        if (!canPieceMove(pieceX, pieceY, pieceRotation) ){
		            reset()
		        }
		    }
		}
    }
    
    
    void input(){
		if ( key("X") ){
		    def testRotation = pieceRotation + 1
		    if (testRotation > pieceStructures[pieceType] ){
		        testRotation = 1
		    }

		    if (canPieceMove(pieceX, pieceY, testRotation) ){
		        pieceRotation = testRotation
		    }

		}else if(  key("Z") ){
		    def testRotation = pieceRotation - 1
		    if (testRotation < 1 ){
		        testRotation = pieceStructures[pieceType]
		    }

		    if (canPieceMove(pieceX, pieceY, testRotation) ){
		        pieceRotation = testRotation
		    }
		    
		}else if(  key( "Left") ){
		    def testX = pieceX - 1

		    if( canPieceMove(testX, pieceY, pieceRotation) ){
		        pieceX = testX
		    }

		}else if( key( "Right") ){
		    def testX = pieceX + 1

		    if (canPieceMove(testX, pieceY, pieceRotation) ){
		        pieceX = testX
		    }

		}else if( key("C") ){
		    while (canPieceMove(pieceX, pieceY + 1, pieceRotation)){
		        pieceY = pieceY + 1
		        timer = timerLimit
		    }
		}
    }
    
    void drawBlock(block, x, y){
        def colors = [
            'w': [0.87, 0.87, 0.87],
            'i': [0.47, 0.76, 0.94],
            'j': [0.93, 0.91, 0.42],
            'l': [0.49, 0.85, 0.76],
            'o': [0.92, 0.69, 0.47],
            's': [0.83, 0.54, 0.93],
            't': [0.97, 0.58, 0.77],
            'z': [0.66, 0.83, 0.46],
            'preview': [0.75, 0.75, 0.75]
        ]
        def color = colors[block]
        drawColor(3)
       
        def blockSize = 20
        def blockDrawSize = blockSize - 1
        rect(((x - 1) * blockSize).toFloat(),((y - 1) * blockSize).toFloat(), blockDrawSize, blockDrawSize, true)
    }
    
    
    
	void create(){	
		
		rand = new Random()	
		pieceStructures = [
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['i', 'i', 'i', 'i'],
		            ['w', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'i', 'w', 'w'],
		            ['w', 'i', 'w', 'w'],
		            ['w', 'i', 'w', 'w'],
		            ['w', 'i', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['w', 'o', 'o', 'w'],
		            ['w', 'o', 'o', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['j', 'j', 'j', 'w'],
		            ['w', 'w', 'j', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'j', 'w', 'w'],
		            ['w', 'j', 'w', 'w'],
		            ['j', 'j', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['j', 'w', 'w', 'w'],
		            ['j', 'j', 'j', 'w'],
		            ['w', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'j', 'j', 'w'],
		            ['w', 'j', 'w', 'w'],
		            ['w', 'j', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['l', 'l', 'l', 'w'],
		            ['l', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'l', 'w', 'w'],
		            ['w', 'l', 'w', 'w'],
		            ['w', 'l', 'l', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'w', 'l', 'w'],
		            ['l', 'l', 'l', 'w'],
		            ['w', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['l', 'l', 'w', 'w'],
		            ['w', 'l', 'w', 'w'],
		            ['w', 'l', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['t', 't', 't', 'w'],
		            ['w', 't', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 't', 'w', 'w'],
		            ['w', 't', 't', 'w'],
		            ['w', 't', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 't', 'w', 'w'],
		            ['t', 't', 't', 'w'],
		            ['w', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 't', 'w', 'w'],
		            ['t', 't', 'w', 'w'],
		            ['w', 't', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['w', 's', 's', 'w'],
		            ['s', 's', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['s', 'w', 'w', 'w'],
		            ['s', 's', 'w', 'w'],
		            ['w', 's', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		    [
		        [
		            ['w', 'w', 'w', 'w'],
		            ['z', 'z', 'w', 'w'],
		            ['w', 'z', 'z', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		        [
		            ['w', 'z', 'w', 'w'],
		            ['z', 'z', 'w', 'w'],
		            ['z', 'w', 'w', 'w'],
		            ['w', 'w', 'w', 'w'],
		        ],
		    ],
		]	
		gridXCount = 10
		gridYCount = 18

		pieceXCount = 4
		pieceYCount = 4

		timerLimit = 0.5
		reset()
	}

	void update(float delta){
		input()
		lupdate(delta)
	}
	
	void render(){	
		def offsetX = 2
		def offsetY = 5

		for (int y in 1..gridYCount ){
		    for (int x in 1..gridXCount ){
		        drawBlock(inert[y][x], x + offsetX, y + offsetY)
		    }
		}

		for (int y in 1..pieceYCount ){
		    for (int x in 1..pieceXCount ){
		        def block = pieceStructures[pieceType][pieceRotation][y][x]
		        if( block != 'w' ){
		            drawBlock(block, x + pieceX + offsetX, y + pieceY + offsetY)
		        }
		    }
		}

		for (int y in 1..pieceYCount ){
		    for (int x in 1..pieceXCount ){
		        def block = pieceStructures[sequence[sequence]][1][y][x]
		        if( block != 'w' ){
		            drawBlock('preview', x + 5, y + 1)
		        }
		    }
		}
	}	
}
