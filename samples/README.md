## HOW RUN SAMPLE

### install javascript packages

> npm install

### Run using:  "[GraalJs](https://github.com/graalvm/graaljs)"

**Generate TypeScript stuff**

> mvn -f graaljs-pom.xml clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

**Compile typescript**

> npm run build:main

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

**Run sample**

> mvn -f graaljs-pom.xml exec:exec

### Run using:  "[Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)"

**Generate TypeScript stuff**

> mvn clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

**Compile typescript**

> npm run build:main

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

**Run sample**

> mvn exec:exec@jsrun


### Run using:  "[Rhino](https://github.com/mozilla/rhino)"

**Generate TypeScript stuff**

> mvn -P rhino clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

**Compile typescript**

> npm run build:main

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

**Run sample**

> mvn exec:exec
