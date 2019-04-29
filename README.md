[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# Leikr
A Work in Progress rewrite of Leikr16 

Powered by [Mini2Dx](https://mini2dx.org/)

![](title_menu.gif?raw=true)

## Running from source 

#### note: `Programs` and `Data` are in the `/leikr/assets/` directory. 

1. git clone the project. Then enter the `leikr` directory.

`git clone https://github.com/torbuntu/leikr.git`

2. Run the gradlew command: 

`./gradlew desktop:launchDesktop`

## Building a release 

to build a release on your platform run: `./gradlew clean build bundleNative`

the release will show up in the `./leikr/desktop/build/` directory and then under the platform you are running on.
for example on linux `desktop/build/linux/`

then copy the `Data` and `Programs` folders from the `leikr/assets/` directory to the location of the launch script. You should end up with the following:

```
Data/
Programs/
Leikr
libs/
```



## Starting your own Program

Projects structure
```
Data/
Programs/
  YourGameName/
     /Art/
     /Audio/
        /Sound/
        /Music/
     /Code/     
     /Maps/
     /Sprites/
     program.properties
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

Sprites: 2,048 max. Each sprite gets split into 8x8, 16x16, 32x32 and 64x64 pixel sprites.

Art: Can load images to be used as backgrounds or however else you wish.

Maps: .tmx maps loadabled from `Maps` directory. (currently unlimited) Animated tiles supported.

Audio: .WAV files loaded from `Audio/Sound` and `Audio/Music`  (Limitations TBD)

Controls: 2xUSB controllers/Full Keyboard. Tested using SNES style controllers on Linux. Note: The controllers are configurable using the Program `Controllers` which comes with the system. It maps inputs to the system properties file in `Data`
