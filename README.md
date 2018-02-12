# java2typescript

Java Processor to **generate [Typescript](https://www.typescriptlang.org/)  Definition file (.d.ts)** from whatever Java classes.

## What is it for ?

This is to help developing on **JVM javascript engine** (ie [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)/[Rhino](https://github.com/mozilla/rhino)) using [Typescript](https://www.typescriptlang.org/)

The main goal is having the definitions available in the modern IDE like [Visual Studio Code](https://code.visualstudio.com/) and [Atom](https://atom.io/) and then use the **intellisense** feature available for java classes within typescript

## What is it not for ?

It is not a transpiler from Java to Javascript like  [datathings/java2typescript](https://github.com/datathings/java2typescript)

## Similar projects

* [typescript-generator](https://github.com/vojtechhabarta/typescript-generator)

## Related Project

* [jvm-npm](https://github.com/bsorrentino/jvm-npm)

## Getting Started

### Use Maven Archetype

#### interactive mode
```
mvn archetype:generate \
-DarchetypeGroupId=org.bsc.processor \
-DarchetypeArtifactId=java2ts-processor-archetype \
-DarchetypeVersion=${project.version}
```

_Coming Soon ..._
