# Leikr
A Work in Progress remake of Leikr16 in Mini2dx

Currently a semi-functional runtime available.
Using the `./gradlew clean build bundleNative` command to generate an executable in the `desktop/build/linux` directory and placing a `Code/` and a `Sprites/` directory along with `game.properties` (can be empty for now).

Add 4 sprite sheets to the `Sprites/` directory with the name scheme `Sprites_X.png` (X being the numbers 0 through 3). These sheets each need to be 128x128 pixels in size. They are chopped up in the sprite engine to be drawable at either 8x8, 16x16 or 64x64 pixels.

Add `main.groovy` in the `Code/` directory. 
main.groovy contents to be runnable should be similar to the following template:

```
//imports the leikr Engine class
import leikr.Engine

//extend the engine to get the API 
class MyGame extends Engine{

  void init(){
      //initialize classes and variables here. Run on first load
  }
  
  void update(){
      //Run every frame. Used for updating variables and game state
  }
  
  void render(){
      //Draws to the screen after update.
  }
}

```


WIP specs:
screen: 240x160 
Sprites: 1,024. 4 128x128 sprite sheets (256 sprites per sheet). Each sheet gets split into 8x8, 16x16 and 64x64 pixel sprites.
Maps: TBD (currently unlimited)
Audio: TBD (currently unavailable). Audio will eventually be loading WAV files from an Audio directory, not sure if this will be limited to a certain type or unlimited.
Controls: 2xUSB controllers/Keyboard. Tested using SNES style controllers. Mouse TBD.
