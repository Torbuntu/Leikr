# Hacking Leikr

#### Version: 0.0.12

#### Languages

The only officialy supported programming language on Leikr is Groovy. But since Leikr is built on the JVM this means there are ways around this limitation.

Leikr comes with the option to compile programs for release. This means it creates typical JVM `name.class` files and outputs them in the program's `Code/Compiled/` directory. 

You see where this is going, right?

Using this strategy one could manually compile other JVM compatible languages and put the class files into `Compiled` to be run by Leikr. 

Languages tested with the method: Java, Kotlin, Scala, Clojure and Python (Jython)

As long as the main game file can extend the Leikr Engine API class then this should work with other JVM languages as well. 

#### all examples assume a project is created and a folder called `Compiled` is inside `Code`

# [Example using Java](#example-using-java)

#### Sample Code
```java
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

You have to add the Leikr jar to the classpath in order for the compiler to know what `Engine` is. In this example I have it in the Code directory with the java class.

After I generate my class file:

`> mv Java.class Compiled/` 

this will move the class file into the `Compiled` directory for running.

Don't forget to make sure the `program.properties` file has `use_compiled = true`

Then when you run Leikr and `run Java` it should load! Super neat.

# [Example using Kotlin](#example-using-kotlin)
#### Assumes you have Kotlin installed
```kotlin
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

# [Example in Scala](#example-using-scala)

#### Assumes you already installed Scala and have it setup on your classpath/have the jar locally available.

```scala
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

`> scalac -cp Leikr-x.x.x.jar Scala.scala -deprecation -no-specialization` 

I used some extra params for debugging purpose ;) Make sure to `cp Leikr-x.x.x.jar` to add Leikr to the classpath so Scala knows where to find Engine.

`> mv Scala.class Compiled/`

Now! For Scala you will need the `scala-library.jar` in the classpath to make this work. So to hack this bad boy into the system we need to launch Leikr a special way:

`> java -cp scala-library.jar:Leikr-x.x.x.jar leikr.desktop.DesktopLauncher`

this command adds `scala-library.jar` as well as `Leikr-x-x-x.jar` to the classpath, and then uses `leikr.desktop.DesktopLauncher` as the main class to target.

And that should do it! Super weird. Super awesome. Super hacks.


# [Example using Clojure](#example-using-clojure)

#### In this example my project is called MyClojure

After playing around for too long I discovered that it was easiest to manage Clojure with the `clojure.jar`. So to do that I ran through the building from github source:

```
git clone https://github.com/clojure/clojure.git
cd clojure
mvn -Plocal -Dmaven.test.skip=true package
```

Which I found on [clojure's website](https://clojure.org/guides/getting_started)

From wherever you build that jar, move it into the `Code` directory where we will be making our project.

(example: `Programs/MyClojure/Code/` )

Getting this to work with Leikr has been the toughest so far (and I thought Scala was bad).

To make things easier right away, we need to make some modifications to the `Sys/mysecurity.policy`.

on line 8 we need to add "write" `permission java.util.PropertyPermission "*", "read,write";`

Inside our project `Code` folder I make a script to make building the files much easier.

```Bash
#!/bin/bash
rm -rf classes Compiled && mkdir Compiled classes

java -cp clojure.jar:Leikr-x.x.x.jar:src/  clojure.main -e "(compile 'MyClojure)"

mv classes/* Compiled/
```

This file quickly removes and rebuilds the `classes` and `Compiled` directories right away. `classes` is required for Clojure compiling to work. `Compiled` is where we will put our `.class` files so Leikr knows where to load them from.

The next command is the actual java command to compile the source. We pass in the class path for clojure and leikr, similar to Scala above but we also pass `src/` so it knows to find the source file. Then we tell java to use the `clojure.main` as the entry point. Then the `-e` flag to evaluate the following command which is our compile `(compile 'MyClojure)`. The Clojure compile command looks for a namespace, in this case `MyClojure` which I'll show you how that works in the source code.

Before we run this file we now need to actually make the directories, and the project file.

```Bash
mkdir classes Compiled src
touch src/MyClojure.clj
```

This has to be in the `src` directory because Clojure looks for it there. (we add it to the class path in the copmile script).

Now the Clojure code.

```Clojure
(ns MyClojure)
(gen-class
	:name MyClojure
	:main false
	:extends leikr.Engine)

	(def x (make-array Integer/TYPE 10))
	(def y (make-array Integer/TYPE 10))
	
	(def dx (make-array Integer/TYPE 10))
	(def dy (make-array Integer/TYPE 10))
	
	(def c (make-array Integer/TYPE 10))
	
	(defn -create [this]
		(dotimes [i 10]
			(aset x i (rand-int 230))
			(aset y i (rand-int 150))
			(aset dx i 1)
			(aset dy i 1)
			(aset c i (rand-int 32))))
	
	(defn -update [this delta]
		(dotimes [i 10]
			(if (> (aget x i) 230) (aset dx i -1) nil)
			(if (< (aget x i) 0) (aset dx i 1) nil)
			(aset x i (+ (aget x i) (aget dx i)))
			
			(if (> (aget y i) 150) (aset dy i -1) nil)
			(if (< (aget y i) 0) (aset dy i 1) nil)
			(aset y i (+ (aget y i) (aget dy i)))))
 
	(defn -render [this]
		(dotimes [i 10]
			(.setColor this (aget c i))
			(.fillRect this (bigdec (aget x i)) (bigdec (aget y i)) (bigdec 10) (bigdec 10) )))
```

This is my first ever Lisp dialect program, so please be gentle with the judgement on style.

I'm going to assume if you're looking at this section, you know pretty well how to do Lisp or Clojure related programming. If so, you won't need to know my attempt at explaining the following.

`(ns MyClojure)` sets the namespace for the file. This is how the compile command knows to find this.

`(gen-class :name MyClojure :main false :extends Leikr.Engine)` This will generate a class file, named MyClojure, which does *not* contain a main method, and which extends `leikr.Engine`. So far so good? Easy? Took me too long to get just this far.

```Clojure
	(def x (make-array Integer/TYPE 10))
	(def y (make-array Integer/TYPE 10))
	
	(def dx (make-array Integer/TYPE 10))
	(def dy (make-array Integer/TYPE 10))
	
	(def c (make-array Integer/TYPE 10))
```

Create a few array variables holding 10 Integers types. (x and y positions for the boxes and their speed values, and another for colors of each box)

#### create

```Clojure
	(defn -create [this]
		(dotimes [i 10]
			(aset x i (rand-int 230))
			(aset y i (rand-int 150))
			(aset dx i 1)
			(aset dy i 1)
			(aset c i (rand-int 32))))
```

This is where we override Leikr's `create` method. `dotimes [i 10]` is essentially like our for loop which will, ten times using i as iterater, add values to our arrays above.

#### update

```Clojure
	(defn -update [this delta]
		(dotimes [i 10]
			(if (> (aget x i) 230) (aset dx i -1) nil)
			(if (< (aget x i) 0) (aset dx i 1) nil)
			(aset x i (+ (aget x i) (aget dx i)))
			
			(if (> (aget y i) 150) (aset dy i -1) nil)
			(if (< (aget y i) 0) (aset dy i 1) nil)
			(aset y i (+ (aget y i) (aget dy i)))))
```

Similar to `create` above, except now we check if the values in the arrays bounce off the edges, and adjust the speed variables accordingly. (`aget` and `aset` are used to get and modify array values.) Unlike `create` `update` is passed the `float delta` value, so we pass that in `[this delta]`  but I don't use it.

#### render

```Clojure
	(defn -render [this]
		(dotimes [i 10]
			(.setColor this (aget c i))
			(.fillRect this (bigdec (aget x i)) (bigdec (aget y i)) (bigdec 10) (bigdec 10) )))
```

And finally our `render` which same as the other two, ten times runs through and first sets the color (getting this to work took me the longest. I could *NOT* for the life of me figure out how to call the super method!) then with that color it draws the box at the x y values. Super easy. heheh...

Now with that file in `Programs/MyClojure/Code/src/MyClojure.clj` we can run the shell script we created above.

`> ./clojurec.sh` 

(I call my file `clojurec.sh`)

Also, don't forget like the others to set `use_compiled = true` in `program.properties`

This will clear the `classes` and `Compiled` directories, and compile the source code into `classes`. Then it will move those files into `Compiled`. I am pretty sure that there must be a way to just compile directly into `Compiled` but I didn't really feel it was that big of a deal for this demo. 

For running, Leikr will require clojure in the classpath, so instead of using the script run this command

`> java -cp clojure.jar:Leikr-x.x.x.jar leikr.desktop.DesktopLauncher`

Similar to Scala.

Clojure is really bizarre, and not really straight forward to work for if you're used to Java or closer to Java looking languages. But it was kind of fun! I might come back to it in the future to do more random stuff. 


# [Example using Python with Jython](#example-using-python-with-jython)


To use Python on the JVM the easiest way that I could find is by using Jython. Jython is a Python 2 (I know...) implementation for the jvm.

The best Jython [Documentation](https://www.javadoc.io/doc/org.python/jython-standalone/2.7.1) I could find.

Firstly we will need to grab the latest [jython jar](https://www.jython.org/download) which I just grab from the jython website.

The page says it supports up to java 8, but it works fine (so far) for me using 12. 

Once we have our jar, I quick made a new directory called src `>mkdir src` and dropped the jar in there for easier management. Since like other languages, we will need this in our classpath for running and compiling.

I found compiling the regular `.py` file to be a major hassle. The file would not extend properly and I could not get it to compile to the correct `.class` name I was looking for. So I went an even more hacky route and decided to write a [shim](https://en.wikipedia.org/wiki/Shim_(computing)) to handle it instead.

The shim I wrote is in [Groovy](https://groovy-lang.org/), since that is the official Leikr language of choice.

My project is called `Python` so I name the Groovy [shim](https://en.wikipedia.org/wiki/Shim_(computing)) `Python` so that Leikr knows which class to load.

```Groovy
import org.python.util.PythonInterpreter
import org.python.core.*

class Python extends leikr.Engine{
	def interp = new PythonInterpreter();
	def cre, upd, ren
	
	PyFloat pf
	
	void create(){
		new File("Programs/Python/Code/Compiled/PythonShim.py").withInputStream { st ->
			interp.execfile(st)
		}
		interp.set("engine", this)
		
		cre = interp.get("create")
		upd = interp.get("update")
		ren = interp.get("render")
		
		cre.__call__()
	}
	
	void update(float delta){
		pf = new PyFloat(delta)
		upd.__call__(pf)
	}	
	
	void render(){
		ren.__call__()
	}
}
	
```

Firstly we need to import our [handy dandy](https://bluesclues.fandom.com/wiki/Handy_Dandy_Notebook) Python libraries. I grab everything from core and the most important piece the `PythonInterpreter` which will be handling out Python code and translations.

I create 3 variables `cre, upd, ren` which are just [proxy](https://www.merriam-webster.com/dictionary/proxy) objects for the Python versions of the `create, update, render` methods. 

What this means is that on each run of those methods in Groovy, it simply calls those methods from the actual Python script we will write. Nothing too serious there. The only thing to note is that if we want the `delta` value, we need to use a `PyFloat` variable and conver the `float delta` before passing it in using the `__call__(delta)` method.

Looking closer at `create()` 

```Groovy
void create(){
	new File("Programs/Python/Code/Compiled/PythonShim.py").withInputStream { st ->
		interp.execfile(st)
	}
	interp.set("engine", this)

	cre = interp.get("create")
	upd = interp.get("update")
	ren = interp.get("render")

	cre.__call__()
}
```

First line is very important. That points to the location of the actual Python file (which I've named `PythonShim.py` to not be confused with the Groovy class `Python.groovy` and loads it into a new File object, then using the closure `withInputStream { st ->` we run the Python interpreter on the input stream object `st` 

```Groovy
interp.execfile(st)
```

This command will parse and load the Python code so it can be run! Pretty awesome right? 

The very next line we run `interp.set("engine", this)` which will set the `Python` class object (which extends `leikr.Engine`) in the context to be usable by the `PythonShim`.

Now we have our shim file set up to do the following:

1. Extends the Leikr Engine to access API
2. Create a PythonInterpreter and proxy objects for the 3 runtime methods
3. Load in the PythonShim file and set the `engine` (lower case) object to the context
4. Gets the 3 runtime methods from the PythonShim and sets them to the proxy objects
5. Runs the proxy methods in the 3 runtime methods.

On to the actual Python code

```Python
import java.math.BigDecimal as bd
import random

# engine : this variable is defined and set by the groovy shim wrapper.

x = []
y = []
dx = []
dy = []
c = []

for i in range(0, 10):
    x.append(random.randint(0, 230))
    y.append(random.randint(0, 150))
    dy.append(1)
    dx.append(-1)
    c.append(random.randint(12, 30))

def create():
    print "Python Success"
#

def update(delta):
    global x, y, dx, dy

    for i in range(0, 10):
        if(x[i] < 0):
            dx[i] = 1

        if(x[i] > 230):
            dx[i] = -1

        if(y[i] < 0):
            dy[i] = 1

        if(y[i] > 150):
            dy[i] = -1

        x[i] = x[i] + dx[i]
        y[i] = y[i] + dy[i]
#

def render():
    for i in range(0, 10):
        engine.setColor(c[i])
        engine.fillRect(bd(x[i]), bd(y[i]), bd(10), bd(10))
#
```

The same demo as the others here, and it doesn't need too much explaining I'd imagine.

The weirdest part is we don't set `engine` in the file anywhere because that is being set by the `Python.groovy` file.

#### Compiling 

The Groovy class, since we don't need to mess with it once it is set up correctly, we just compile into a jvm .class file

(This assumes you have groovy installed)

`>  groovyc -cp jython-standalone-2.7.1.jar:Leikr-0.0.12.jar Python.groovy` 

Then we move the generated class files (should be 2 since there was a closure) into the `Compiled` directory. Since we were running in a `src` directory next to `Compiled` we can use this command:

`> mv Python\$_create_closure1.class ../Compiled/ && mv Python.class ../Compiled/`



#### Running

Before running make sure inside your project's `Code/Compiled/` directory you have both the `.class` files from the compile step and the `PythonShim.py` script.

Also make sure `program.properties` has `use_compiled = true` 

In the root of the Leikr project run this command (make sure you have a copy of the Jython jar there for the `-cp`)

`> java -cp jython-standalone-2.7.1.jar:Leikr-0.0.12.jar leikr.desktop.DesktopLauncher`


Now whenever you make changes to the Python file itself, all you have to do is reload the program (F5). 

Happy Hacking!