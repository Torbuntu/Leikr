# Leikr
A Work in Progress remake of Leikr16 in Mini2dx

Currently a semi-functional runtime available. Using the `./gradlew clean build bundleNative` command to generate
an executable in the `desktop/build/linux` directory and placing a Code/ directory with `game.properties` (can be empty for now) and `main.groovy`. 
main.groovy contents to be runnable should be similar to the following template:

```
import leikr.Engine;
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
