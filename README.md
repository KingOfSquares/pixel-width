# pixel-width
Library for calculating and working with pixel width for characters in Minecraft: Java Edition.

This library is mainly made for [Adventure](https://github.com/KyoriPowered/adventure) Component support, 
but works to some degree for normal Strings and chars.

This library consists of two modules:

### pixel-width-core
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
The core module includes:
- Infrastructure to create a PixelWidthSource/ContextualPixelWidthSource which can be used to get pixel width of text.
- A default CharacterWidthFunction that has pixel width values for characters that appear in the standard ascii.png assets file

#### Usage:
```java

class MyClass {
  
  //Default PixelWidthSource which uses the provided DefaultCharacterWidthFunction
  final PixelWidthSource source = PixelWidthSource.pixelWidth();
  final Component component = Component.text("tuba is").append(Component.text(" GREAT", Style.style(TextDecoration.BOLD)));
  
  final float componentWidth = source.width(component);
}
```

### pixel-width-utils
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
The utils module provides features that depend on pixel width calculation to manipulate Components or text in Components in some way.
Currently, this module supplies the following features:
- A simple API that can center text in a Component

#### Usage

```java

class MyClass{

  //Default PixelWidthSource which uses the provided DefaultCharacterWidthFunction
  final PixelWidthSource source = PixelWidthSource.pixelWidth();
  final Component component = Component.text("WELCOME");
  
  //If no custom width is provided the default chat width is used for calculation
  final Component centeredComponent = CenterAPI.center(component, Component.text(".", Style.style(TextDecoration.OBFUSCATED)));
}
```
Above example sent in chat:
<img src="https://gcdnb.pbrd.co/images/15EpazdS9AV2.png?o=1" alt="Image showing result of above Usage Example for centering text">
