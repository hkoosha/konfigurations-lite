## Java Configuration Library [![Build Status](https://travis-ci.org/hkoosha/konfigurations.svg?branch=master)](https://travis-ci.org/hkoosha/konfigurations)

Simple, small and extendable zero dependency configuration management library.

## Maven

```xml
<dependency>
    <groupId>io.koosha.konfigurations</groupId>
    <artifactId>konfigurations-lite</artifactId>
    <version>8.0.0</version>
</dependency>
```

```groovy
compile group: 'io.koosha.konfigurations', name: 'konfigurations-lite', version: '8.0.0'
```

Development happens on the master branch. Releases are tagged. 


## Project goals:

- Lightweight, small, easy.
- Supporting multiple configuration sources.
- Configuration namespace management.

## Usage:

**Overrides**: Konfiguration sources can override each other. The first source 
which contains the requested key is selected and takes precedence.

**List, Map, Custom Type**: As long as the underlying source can parse it from 
the actual configuration source, it's possible. The ExtJacksonJsonSource uses 
Jackson for parsing json, so if the Jackson parser can parse the map / list /
object, so can the configuration. The same is true for ExtYamlSource which
uses snakeyaml. You could also implement your own source.

```java
KonfigurationFactory f    = KonfigurationFactory.getInstanceV8();

// Create as many sources as necessary
Konfiguration        json = f.jacksonJson ("myJsonSource", "some_valid_json...");
Konfiguration        yaml = f.snakeYaml   ("myYamlSource", "some_valid_yaml_provider");
Konfiguration        mem  = f.map         ("myMapSource",  Map.of(foo, bar, baz, quo));

// Kombine them (json takes priority over yaml and yaml over mem, and mem over net).
Konfiguration konfig = f.kombine(json, yaml, mem, net);

// Get the value, notice the .v()
boolean b = konfig.bool   ("some.konfig.key.deeply.nested");
int     i = konfig.int_   ("some.int");
long    l = konfig.long_  ("some.long");
String  s = konfig.string ("aString");
double  d = konfig.double_("double");

List<Invoice>       list = konfig.list("a.nice.string.list", Invoice.class);

// --------------

Integer maybe = konfig.int_("might.be.unavailable", 42);
assert maybe == 42;
```

### Assumptions / Limitations:
 - First source containing a key takes priority over others.

 - Currently, custom types and ALL the required fields corresponding to those
   read from json string, MUST be public. (This is actually a jackson limitation  
   (feature?) as it won't find private, protected and package local fields AND 
   classes (important: both class and fields, must be public)). This affects 
   list() and custom() methods.

 - TODO: Many more unit tests
