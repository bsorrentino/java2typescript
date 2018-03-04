

import {Stream,Arrays,URI,Collectors} from "${typescript-filename}-types";



let result = Stream.of( "HELLO", "JAVA2TS!").collect( Collectors.joining(" "));

print( result );
