
const java_string = Java.type("java.lang.String");

export function String( v:any ) {
    return java_string.valueOf(v);
}