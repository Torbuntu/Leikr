# Leikr
A Work in Progress remake of Leikr16 in Mini2dx

Projects structure
```
Games/
  /SomeGame
  /SomeGame2
  /SomeGame3
     /Art/
     /Audio/
        /Sound/
        /Music/
     /Code/     
     /Maps/
     /Sprites/
     game.properties
```

Add `main.groovy` in the `Code/` directory. 
main.groovy contents to be runnable should be similar to the following template:

```
//imports the leikr Engine class
import leikr.Engine

//extend the engine to get the API 
class MyGame extends Engine{

  void create(){
      //initialize classes and variables here. Run on first load
  }
  
  void update(float delta){
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

Art: Can load images to be used as backgrounds or however else you wish.

Maps: .tmx maps loadabled from `Maps` directory. (currently unlimited)

Audio: .WAV files loaded from `Audio/Sound` and `Audio/Music`  (Limitations TBD)

Controls: 2xUSB controllers/Full Keyboard. Tested using SNES style controllers. (Mouse TBD)
