# JavacMan

## Disclaimer

This project is a personal recreation of the classic video game **Pac-Man**, developed for learning purposes.  
All rights to the original **Pac-Man** game, characters, and assets belong to [Bandai Namco Entertainment](https://www.bandainamcoent.com/).  
This project is not affiliated with, endorsed by, or associated with Bandai Namco Entertainment in any way.

## Credits

I learned how to develop video games in Java thanks to tutorials by [RyiSnow](https://www.youtube.com/watch?v=om59cwR7psI&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&ab_channel=RyiSnow), and as a result, some parts of my code are similar to their tutorial.

All assets used in this project were **created by me**:  
🎵 **Sound Effects:** Generated using [sfxr.me](https://sfxr.me/) and [BeepBox](https://www.beepbox.co/).  
🎨 **Graphics:** Designed with [Pixilart](https://www.pixilart.com/).

## Description

**JavacMan** is a personal project inspired by the classic arcade game **Pac-Man**.  
The goal of this project is to recreate the core gameplay mechanics while improving my skills in **Java game development**.

In **JavacMan**, players navigate a maze, collect items, and avoid enemies, just like in the original game.  
However, this version includes custom assets and minor gameplay adjustments to give it a unique touch.

## Game Configuration

When running **JavacMan**, the program automatically creates a configuration folder in the user's home directory:

- **Windows**: `C:\Users\YourName\JavacMan\`
- **macOS**: `/Users/YourName/JavacMan/`
- **Linux**: `/home/JavacMan/`

This folder contains **three `.dat` files**, which store the game's configuration settings.

You can safely delete this folder at any time. However, the game will automatically recreate it the next time the program is run.

# How to run

## Requirements

Java >= 23 is required to run this program. You can download the correct version [here](https://www.oracle.com/fr/java/technologies/downloads/#jdk23-windows).

For Windows users : If needed, you can set a PATH system variable.
```
C:\Program Files\Java\[YOUR_JAVA_FOLDER]\bin
```

If you execute these commands you should have similar outputs :

```
java --version
>> java 23.0.1 2024-10-15
   Java(TM) SE Runtime Environment (build 23.0.1+11-39)
   Java HotSpot(TM) 64-Bit Server VM (build 23.0.1+11-39, mixed code, sharing)
```
```
javac --version
>> javac 23.0.1
```
```
jar --version
>> jar 23.0.1
```

## Compilation

**In the root directory :**

This command compiles all the Java source files located in the src directory and outputs the resulting .class files
to the out/production/JavacMan directory :
```
javac -sourcepath ./src/ -d ./out/production/JavacMan ./src/io/github/killiansra/javacman/main/Main.java
```

This command copies all the resource files (images, sounds, etc.) from the res directory to the out/production/JavacMan directory :
```
cp -r ./res/* ./out/production/JavacMan/
```

## .jar file creation

**In the root directory :**

This command creates a `.jar` file named `JavacMan.jar`. The `cfm` options specify that the command will create the
file (`c`), include all files from the specified directory (`f`), and use the manifest file (`m`) provided. The `-C` option
changes to the `out/production/JavacMan` directory to include its contents in the .jar file.
```
jar cfm JavacMan.jar META-INF/MANIFEST.MF -C out/production/JavacMan .
```

## Run the .jar file

**In the root directory**

This command executes the `JavacMan.jar` file.
```
java -jar .\JavacMan.jar
```

> **Note** : You can also double-click the `.jar` file to launch the game.

# License
[![License: CC BY-NC-ND 4.0](https://licensebuttons.net/l/by-nc-nd/4.0/88x31.png)](https://creativecommons.org/licenses/by-nc-nd/4.0/)
