# Hacking Leikr

#### Languages

The only officialy supported programming language on Leikr is Groovy. But since Leikr is built on the JVM this means there are ways around this limitation.

Leikr comes with the option to compile programs for release. This means it creates typical JVM `name.class` files and outputs them in the program's `Code/Compiled/` directory. 

You see where this is going, right?

Using this strategy one could manually compile other JVM compatible languages and put the class files into `Compiled` to be run by Leikr. 

Languages tested with the method: Java, Kotlin and Scala.

As long as the main game file can extend the Leikr Engine API class then this should work with other JVM languages as well. 

#### all examples assume a project is created and a folder called `Compiled` is inside `Code`
# Example using Java

#### Sample Code
```Java
import java.math.BigDecimal;
public class Java extends leikr.Engine{
	
	int[] x = new int[10];
	int[] y = new int[10];

	int[] dx = new int[10];
	int[] dy = new int[10];

	BigDecimal width = new BigDecimal(10);
	BigDecimal height = new BigDecimal(10);

	@Override
	public void create(){
		for(int i = 0; i < 10; i++){
			x[i] = randInt(0, 221);
			y[i] = randInt(0, 161);
			dx[i] = 1;
			dy[i] = 1;
		}
	}
	
	@Override
	public void update(float delta){
		for(int i = 0; i < 10; i++){
			if(x[i] < 0) dx[i] = 1;
			else if (x[i] > 230) dx[i] = -1;
			
			if(y[i] < 0) dy[i] = 1;
			else if (y[i] > 150) dy[i] = -1;
			
			x[i] += dx[i];
			y[i] += dy[i];
		}
	}

	@Override
	public void render(){                 
		for(int i = 0; i < 10; i++){ 	
			setColor(10+i);
			fillRect(new BigDecimal(x[i]), new BigDecimal(y[i]), width, height);
		}
	}
}
```

The command to compile: 
`> javac -cp Leikr-x.x.x.jar Java.java`

You have to add the Leikr jar to the classpath. In this example I have it in the Code directory with the java class.

After I generate my class file:
`> mv Java.class Compiled/` 

this will move the class file into the compiled directory for running.

Don't forget to make sure the `program.properties` file has `use_compiled = true`

Then when you run Leikr and `run Java` it should load! Super neat.

# Example using Kotlin
#### Assumes you have Kotlin installed
```Kotlin
import java.math.BigDecimal;
open class Kotlin : leikr.Engine() {

	var x = Array<Int>(10){0}
	var y = Array<Int>(10){0}

	var dx = Array<Int>(10){0}
	var dy = Array<Int>(10){0}

	var width = BigDecimal(10)
	var height = BigDecimal(10)

	override fun create(){
		for(i in 0..9){
			x[i] = randInt(0, 221)
			y[i] = randInt(0, 161)
			dx[i] = 1
			dy[i] = 1
		}
	}
    
	override fun update(delta: Float){	
		for(i in 0..9){
			if(x[i] < 0) dx[i] = 1
			else if (x[i] > 230) dx[i] = -1
			
			if(y[i] < 0) dy[i] = 1
			else if (y[i] > 150) dy[i] = -1
			
			x[i] += dx[i]
			y[i] += dy[i]
		}
	}
    
	override fun render(){                 
		for(i in 0..9){
			setColor(10+i)
			fillRect(BigDecimal(x[i]), BigDecimal(y[i]), width, height)
		}
	}
}	
```

Then the commands to compile and move:

`> kotlinc -cp Leikr-x.x.x.jar Kotlin.kt`

then

`> mv Kotlin.kt Compiled/`

# Example in Scala

```Scala
import java.math.BigDecimal;
class Scala extends leikr.Engine {

	var x:Array[Int] = new Array[Int](10)
	var y:Array[Int] = new Array[Int](10)

	var dx:Array[Int] = new Array[Int](10)
	var dy:Array[Int] = new Array[Int](10)

	var width:BigDecimal = new BigDecimal(10)
	var height:BigDecimal = new BigDecimal(10)
	
  	override def create(): Unit = {
		for( i <- 0 to 9){
			x(i) = randInt(0, 221)
			y(i) = randInt(0, 161)
			
			dx(i) = randInt(-1, 2)
			dy(i) = randInt(-1, 2)
		}
  	}
  	
  	override def update(delta: Float): Unit = {
		for( i <- 0 to 9){
			if(x(i) < 0) dx(i) = 1;
			else if (x(i) > 230) dx(i) = -1;

			if(y(i) < 0) dy(i) = 1;
			else if (y(i) > 150) dy(i) = -1;

			x(i) += dx(i);
			y(i) += dy(i);
		}
  	}
  	
	override def render(): Unit = {
		for(i <- 0 to 9){      
			setColor(10+i)
			fillRect(new BigDecimal(x(i)), new BigDecimal(y(i)), width, height)
		}
	}
}		
```

Commands

`> scalac -cp Leikr-0.0.12.jar Scala.scala -deprecation -no-specialization` 

I used some extra params for debugging purpose ;)

`> mv Scala.class Compiled/`

Now! For Scala you will need the `scala-library.jar` in the classpath to make this work. So to hack this bad boy into the system we need to launch Leikr a special way:

`> java -cp scala-library.jar:Leikr-x.x.x.jar leikr.desktop.DesktopLauncher`

And that should do it! Super weird. Super awesome. Super hacks.

