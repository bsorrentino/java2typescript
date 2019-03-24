
import validator = require("validator");


export function test() {

    print( "validator.isCreditCard( 'XXXXXXXXXXXXX')", 
        validator.isCreditCard( 'XXXXXXXXXXXXX') );

}

