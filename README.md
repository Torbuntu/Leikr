[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub tag](https://img.shields.io/github/tag/Torbuntu/Leikr)](https://GitHub.com/Torbuntu/Leikr/tags/)

# Leikr Game System
| [Wiki](https://github.com/torbuntu/leikr/wiki) | [itch.io](https://torbuntu.itch.io/leikr) | [Releases](https://github.com/torbuntu/leikr/releases) | [About](https://torbuntu.github.io/Leikr/docs/about) |

A Game System built in Java for writing games and programs in Groovy. (A Fantasy Console idea)

>Apache Groovy is a **powerful, optionally typed** and **dynamic** language, with **static-typing** and **static compilation** capabilities, for the Java platform aimed at improving developer productivity thanks to a concise, familiar and **easy to learn syntax**.
-- http://groovy-lang.org/

Powered by [Mini2Dx](https://mini2dx.org/)

| Preview |
|----|
| ![](showcase/title_showcase.png?raw=true) |

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
    Leikr-x.x.x.jar
    Sys/    
```

Depending on your system, you can either launch the system through the `Leikr` shell script or `Leikr.bat`. If you'd rather, You can even launch the system manually the java way using the command `java -jar Leikr-x.x.x.jar`


## Running from source 

#### notes: `Programs` and `Data` are in the `/leikr/assets/` directory. It is *NOT* recommended to run untrusted user programs from this method. Gradle currently does not apply the security sandbox policy. 

###### `>` is the terminal icon, don't type this.

###### Java is required to run this program. Best used with AdoptOpenJDK. 

1. git clone the project. Then enter the `leikr` directory.

```
> git clone https://github.com/torbuntu/leikr.git
> cd Leikr
```

2. Run the gradlew command: 

`>./gradlew desktop:launchDesktop` 

or 

`>gradlew desktop:launchDesktop` 

depending on your OS.

## Building a release 

to build a release on your platform run: 

`>./gradlew clean build desktop:deployLeikr` 

or 

`>gradlew clean build desktop:deployLeikr`

the release will show up in the `./leikr/desktop/build/libs` directory. You should end up with the following:

```
Data/
Programs/
Leikr
Leikr.bat
Leikr-x.x.x.jar
Sys/
```

## Starting your own Program

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

Add `main.groovy` in the `Code/` directory. 
main.groovy contents to be runnable should be similar to the following template:

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


## Specs: 

| Spec |      |
|----|----|
| Resolution | 240x160 |
| Sprites    | 2,048 max. Each sprite gets split into 8x8, 16x16, 32x32 and 64x64 pixel sprites. |
| Art | Can load images to be used as backgrounds or however else you wish. |
| Maps | .tmx maps loadabled from `Maps` directory. (currently unlimited) Animated tiles supported. |
| Audio | 16bit .WAV files loaded from `Audio/Sound` and `Audio/Music`  (Limitations TBD) |
| Controls | Primary: Keyboard, Mouse. Secondary: USB Controllers (snes layout). Tested using SNES style controllers on Linux. Note: The controllers are configurable using the Program `Controllers` which comes with the system. It maps inputs to the file `Data/system.properties` |


## Supported Platforms

| Platform | Support Level | Status | Notes |
|----|----|----|----|
| Desktop | Full | Up to date | Fedora and other Linux distributions officially supported first. Windows also known to work. No status on Apple computer builds available. Since this is a Java program, it *should just work* on any mainstream desktop OS. However, due to native libs this produce odd results on less common systems. |
| Raspberry Pi 3B+ | Partial | Outdated | This requires custom builds of the native libs for LibGDX and LWJGL. Due to this, builds for this come slowly or only when tim epermits. The only currently supported OS for this is default Raspbian with OpenGL drivers activated and GPU memory boosted. This will be a full release in the future.|
| Raspberry Pi 3A+ | Partial | Outdated | Same story as the 3B+. This board works great, and may replace the 3B+ as the primary target hardware. |
