package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;

/**
 * 
 * @author bsorrentino
 *
 */
public abstract class AbstractConverterTest {
    
    final Java2TSConverter converter =
            Java2TSConverter.builder()
                    .compatibility(Compatibility.NASHORN)
                    .build();
    
    protected java.util.Map<String,TSType> declaredClassMap( Class<?> ... classes) {
        return Stream.of( classes )
            .collect( Collectors.toMap( c -> c.getName(), c -> TSType.of(c) ))
            ;       
    }
    protected java.util.Map<String,TSType> declaredTypeMap( TSType ... types) {
        return Stream.of( types )
            .collect( Collectors.toMap( t -> t.getValue().getName(), t -> t ))
            ;       
    }
    
    protected String getReturnType( Class<?> type, String methonName, Class<?> ...args ) throws Exception {
        return getReturnType(Collections.emptyMap(), type, methonName, (Class<?>[])args);
    }
    
    protected String getReturnType( java.util.Map<String, TSType> declaredClassMap, Class<?> type, String methodName, Class<?> ...args ) throws Exception 
    {
        final Method m = type.getMethod(methodName, (Class<?>[])args);          
        return getReturnType( declaredClassMap, type, m);
    }
    
    protected String getReturnType( java.util.Map<String, TSType> declaredClassMap, Class<?> type, Method m ) throws Exception 
    {
        final Type rType = m.getGenericReturnType();
        final String result = Java2TSConverter.convertJavaToTS(rType, m,
                TSType.of(type),
                declaredClassMap, 
                true, 
                Optional.empty());
        return result;
    }

}
