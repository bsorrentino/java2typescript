## HOW RUN SAMPLE

This examples use "[GraalJs](https://github.com/graalvm/graaljs)"

### install javascript packages

> npm install

### Generate TypeScript stuff

> mvn clean package

This task will generate `jdk8.d.ts` and `jdk8-types.ts` into folder `ts/j2ts`

### Compile typescript

> npm run build

This task use [parcel](https://parceljs.org/) to bundle all modules in a single javascript file in `target/js/main.js`

### Run sample

> npm start
