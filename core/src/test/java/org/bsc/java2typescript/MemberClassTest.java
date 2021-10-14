package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 
 * @author bsorrentino
 *
 */
public class MemberClassTest extends AbstractConverterTest {

    
    @Test
    public void testMemberClassInfos() {
        
        final TSType type = TSType.of( java.util.Map.Entry.class );

        assertEquals(true, type.supportNamespace());
        assertEquals("java.util", type.getNamespace());

        assertEquals("java.util.Map$Entry", type.getTypeName() );
        assertEquals( "Map$Entry", type.getSimpleTypeName() );
    }
    
    @Test
    public void testMemberClass() throws Exception {

        final TSConverterContext ctx =
                TSConverterContext.of(  TSType.of( java.util.Map.Entry.class ),
                                        declaredTypeMap(),
                                        Java2TSConverter.Options.of(Compatibility.NASHORN ));
        
        assertNotNull(ctx);
        
        final String result  = ctx.getClassDecl().toString();
    
        assertNotNull( result );
        assertEquals( "interface Map$Entry<K, V> {", result );

    }
    
    @Test
    public void testMemberClassUsage() throws Exception {
        
        final Class<?> type = java.util.Map.class;
        
        final Method m = type.getMethod("entrySet");         
        final Type rType = m.getGenericReturnType();

        final String result = Java2TSConverter.convertJavaToTS(rType, m,
                TSType.of(type),
                declaredTypeMap( TSType.of(java.util.Set.class), TSType.of(java.util.Map.Entry.class)),
                true, 
                Optional.empty());

        assertEquals( "Set<Map$Entry<K, V>>", result );
    }

}
