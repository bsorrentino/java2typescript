
const java_string = Java.type("java.lang.String");

export function toString( v:any ):java.lang.String {
    return java_string.valueOf(v);
}