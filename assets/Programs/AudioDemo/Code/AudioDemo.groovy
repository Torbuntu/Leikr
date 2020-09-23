class AudioDemo extends leikr.Engine {

	boolean playing = false, paused = false
	String action = "play", subAction = "pause"
    def soundIndex = 0, offset = 0
    def pan = 0f
    
    def files = new File("Programs/AudioDemo/Audio/Sound").listFiles()
    
    // override the onResume to handle if we should continue music or not.
    void onResume(){
    	if(paused && playing){
    		resumeAudio()
    		paused = false
    	}
    }
    
    // override onPause to pause music and manage our paused variable
    void onPause(){
    	pauseAudio()
    	paused = true
    }
    
    void update(){
        if(keyPress("Space")){
        	if(playing) {
        		pauseAudio()
        		playing = false
        		action = "play"
    		} else {
        		playMusic "DST-TDT.wav", true
        		playing = true
        		action = "stop"
    		}
        }
        
        if(keyPress("P") && playing){
        	if(paused) {
        		resumeAudio()
        		paused = false
        		subAction = "pause"
    		}else{
    			pauseAudio()
    			paused = true
    			subAction = "resume"
    		}
        }
        
        if(keyPress("Down")) soundIndex++
        if(keyPress("Up")) soundIndex--
        if(soundIndex < 0) soundIndex = 79
        if(soundIndex > 79) soundIndex = 0
        
        if(keyPress("S")){
        	String n = files[soundIndex].getName()
        	playSound n, 1f, 1f, pan
        }
        
        offset = 0
        if(soundIndex > 9) offset = 10
        if(soundIndex > 19) offset = 20
        if(soundIndex > 29) offset = 30
        if(soundIndex > 39) offset = 40
        if(soundIndex > 49) offset = 50
        if(soundIndex > 59) offset = 60
        if(soundIndex > 69) offset = 70
        
        if(keyPress("Left")) pan-=0.1f
        if(keyPress("Right")) pan+=0.1f
        if(pan > 1)pan = 1
        if(pan<-1)pan = -1
    }
    void render(){	
		bgColor(4)
		drawRect 7, 0, 0, 124, 44
		drawRect 7, 2, 2, 120, 40
		drawString 7, "Music Demo", 20, 4
		drawString(7, "Press Space to $action", 10, 14)
		if(playing)drawString(7, "Press P to $subAction", 10, 24)
		
		drawRect 7, 0, 54, 124, 56
		drawRect 7, 2, 56, 120, 52
		
		drawString 7, "Sound Demo", 20, 60
		drawString(7, "Press S to play\nPress Up or Down to select\nLeft or Right to adjust pan", 10, 70)
		
		drawString(7, "pan: $pan", 10, 90)
		
		drawRect(7, 126, 54, 110, 90) 
		drawRect(7, 128, 56, 106, 86)
		10.times{it->
			if (soundIndex == it+offset) {
				drawString(31, it + offset + ". "+files[it+offset].getName(), 132, 8*it+60)
			} else {
				drawString 7, it + offset + ". "+files[it+offset].getName(), 136, 8*it+60
			}
		}
		
		drawString 7, "index: $soundIndex", 10, 100
    }
}

