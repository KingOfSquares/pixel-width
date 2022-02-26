# pixel-width
Library for calculating and working with pixel width for characters in Minecraft: Java Edition

This library consists of two modules:

**pixel-width-core**
```xml
<dependency>
            <groupId>solar.squares</groupId>
            <artifactId>pixel-width-core</artifactId>
            <version>1.0.0</version>
        </dependency>
```
```groovy
dependencies {
    implementation 'solar.squares:pixel-width-core:1.0.0'
}
```

Includes code to calculate pixel width of any Component/String/char and a set of default values 
that covers the characters appearing in the ascii.png assets file. Also includes ways to make your own system based on 
arbitrary context or custom character width functions.


**pixel-width-utils**
```xml
<dependency>
            <groupId>solar.squares</groupId>
            <artifactId>pixel-width-utils</artifactId>
            <version>1.0.0</version>
        </dependency>
```
```groovy
dependencies {
    implementation 'solar.squares:pixel-width-utils:1.0.0'
}
```

Includes code that uses pixel width calculation to work with Components in some way. Currently
only includes a simple system to center text in a Component.