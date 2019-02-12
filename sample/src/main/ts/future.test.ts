import { 
    CompletableFuture,
    Executor,
    Optional,
    RuntimeException
} from "ts/jdk8-types";

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

    const currentThreadExecutor = new Executor( runnable => runnable() );
    
    let future = CompletableFuture.supplyAsync( () => {
    
        //throw "ERROR0"
        return "complete";

    }, currentThreadExecutor);

    
    future
        //.thenApply( result => {throw "ERROR1"} )
        .thenAccept( result => print(result) )
        .exceptionally( (err) => print( "ERROR:", err ) )
        ;

    let future1 = CompletableFuture.supplyAsync( () => {

        Optional.ofNullable( null ).orElseThrow( () => new RuntimeException("RTE") )

        return "complete1";

    }, currentThreadExecutor);

    
    future1
        .thenApply( result => error("ERROR1") )
        .exceptionally( err => print( "ERROR 1:", err ) )
        .thenAccept( result => error("ERROR2") )
        .exceptionally( (err) => print( "ERROR 2:", err ) )
        ;
    
    let future2 = doSometing().thenAccept( result => print(result) );

}