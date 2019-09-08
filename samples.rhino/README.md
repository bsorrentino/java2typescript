## HOW RUN SAMPLE

### install javascript packages

> npm install

### Run using:  "[Rhino](https://github.com/mozilla/rhino)"

**Generate TypeScript stuff**

> mvn clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

**Compile typescript**

> npm run build:main

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

**Run sample**

> mvn exec:exec

### Run using:  "[Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)" - DEPRECATED

**Generate TypeScript stuff**

> mvn -P nashorn clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

**Compile typescript**

> npm -P nashorn run build:main

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

**Run sample**

> mvn -P nashorn exec:exec@jsrun
