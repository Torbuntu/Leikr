[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub tag](https://img.shields.io/github/tag/Torbuntu/Leikr)](https://GitHub.com/Torbuntu/Leikr/tags/)

# Leikr Game System
| [Wiki](https://github.com/torbuntu/leikr/wiki) | [itch.io](https://torbuntu.itch.io/leikr) | [Releases](https://github.com/torbuntu/leikr/releases) | [About](https://torbuntu.github.io/Leikr/site/docs/about) | [Javadoc](https://torbuntu.github.io/Leikr/javadoc/index.html)

A Game System for making games and programs in Groovy. (A Fantasy Console idea)

<a href='https://flathub.org/apps/details/io.github.torbuntu.Leikr'><img width='240' alt='Download on Flathub' src='https://flathub.org/assets/badges/flathub-badge-en.png'/></a>

#### Why Groovy?

>Groovy: If you describe something as groovy, you mean that it is **attractive, fashionable, or exciting.**  https://www.collinsdictionary.com/dictionary/english/groovy

>Apache Groovy is a **powerful, optionally typed** and **dynamic** language, with **static-typing** and **static compilation** capabilities, for the Java platform aimed at improving developer productivity thanks to a concise, familiar and **easy to learn syntax**.
-- http://groovy-lang.org/


#### Technologies 
Powered by [Mini2Dx](https://mini2dx.org/). 

Once you've mastered Leikr and feel the need to expand into bigger waters, you can take your knowledge and move up to Mini2Dx itself.


| Preview |
|----|
| ![](showcase/showcase.gif?raw=true) |

## Running from release

#### note: If downloading from itch.io, make sure to document where the install path is for accessing the files.

A release is a folder which contains everything required to run the Leikr Game System. Releases can be found from one of the distribution sources (Currently: [itch.io](https://torbuntu.itch.io/leikr) or [github](https://github.com/torbuntu/leikr/releases))

Inside this folder should look something like this:

```
Leikr/
    Data/
    Programs/
    Leikr
    Leikr.bat
    Leikr.jar
    Sys/    
```

Depending on your system, you can either launch the system through the `Leikr` shell script or `Leikr.bat`. If you'd rather, You can even launch the system manually the java way using the command `java -jar Leikr.jar`


## Running from source 

#### notes: `Programs` and `Data` are in the `/leikr/assets/` directory. It is *NOT* recommended to run untrusted user programs from this method. Gradle currently does not apply the security sandbox policy. 

###### `>` is the terminal icon, don't type this.

###### Java is required to run this program. Best used with AdoptOpenJDK. Version 1.0.0 is tested with Java 15 at a minimum


1. git clone the project. Then enter the `leikr` directory.

```
> git clone https://github.com/torbuntu/leikr.git
> cd Leikr
```

2. Run the gradlew command: 

`>./gradlew launchDesktop` 

## Building a release 

to build a release: 

`>./gradlew clean build deployLeikr` 

the release will show up in the `Leikr/deploy` directory. You should end up with the following:

```
Data/
Programs/
Sys/
Leikr
Leikr.bat
Leikr.jar
```

## Starting your own Program

From the Leikr terminal view, you can run either `new GameName` to quickly generated a project from an internal template, or you can run `new` and walk through a generator.

Projects structure
```
Data/
Sys/
Programs/
  YourGameName/
     /Art/
         icon.png
         city.png
     /Audio/
        /Sound/
            walk.wav
        /Music/
            theme.wav
            intro.wav
     /Code/ 
         YourGameName.groovy
     /Maps/
         overworld.tmx
         underworld.tmx
     /Sprites/
         Sprites.png
     program.properties
```

In order for your game to be runnable, the main class file should be similar to the following template:

```java
//extend the engine to get the API 
class MyGame extends leikr.Engine{

  void create(){
      //Initialize classes and variables here. Run on first load
  }
  
  void update(float delta){
      //Run every frame. Used for updating variables and game state
  }
  
  void render(){
      //Draws to the screen after update.
  }
}

```

note however that all three of the main methods (create, update and render) are optional. You can make smaller graphic demos just using the `render` method.


## Specs: 

| Spec |      |
|----|----|
| Resolution | 240x160 |
| Sprites    | 128 max draw calls per frame. Sprites.png gets split into 8x8, 16x16, 32x32 and 64x64 pixel sprites. |
| Art | Can load images to be used as backgrounds or however else you wish. |
| Maps | .tmx maps loadabled from `Maps` directory. (Animated tiles are supported) |
| Audio | 16bit .WAV, .OGG, or MP3 files loaded from `Audio/Sound` and `Audio/Music` |
| Controls | Primary: Keyboard, Mouse. Secondary: USB Controllers (snes layout). Tested using SNES style controllers on Linux. Note: The controllers are configurable using the Program `Controllers` which comes with the system. It maps inputs to the file `Data/system.properties` |


#### Note on ARM builds:
The primary target will eventually be ARM devices (such as Raspberry Pi, Pine devices like Pinebook Pro and Pinetab, etc...)

if you plan to use the raspberry pi or other SBC, it works best to have compiled the projects on a faster computer beforehand. Compiling projects live on the pi can take a while depending on the size/complexity of your project. 
