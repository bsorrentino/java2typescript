import {
    CompletableFuture,
    JSExecutor,
    Optional,
    RuntimeException
} from './j2ts/jdk8-types';

function doSometing() {
    return CompletableFuture.completedFuture("DONE!");
}

function error(msg:string) {
    throw msg;
}

export function test() {
print(`
========================================

CompletableFuture TEST

========================================
`);

    const currentThreadExecutor = new JSExecutor();
    
    let future = CompletableFuture.supplyAsync( { get:  () => {

        //throw "ERROR0"
        return "complete";

    }}, currentThreadExecutor );


    future
        //.thenApply( result => {throw "ERROR1"} )
        .thenAccept( { accept: result => print(result) } )
        .exceptionally( { apply: (err) => print( "ERROR:", err ) } )
        ;

    let future1 = CompletableFuture.supplyAsync( { get: () => {

        Optional.ofNullable( null ).orElseThrow( { get: () => new RuntimeException("RTE") } )

        return "complete1";

    }}, currentThreadExecutor);


    future1
        .thenApply( { apply: result => error("ERROR1") } )
        .exceptionally( { apply: err => print( "ERROR 1:", err ) } )
        .thenAccept( { accept: result => error("ERROR2") } )
        .exceptionally( { apply: (err) => print( "ERROR 2:", err ) } )
        ;

    let future2 = doSometing().thenAccept( { accept: result => print(result) } );

}
