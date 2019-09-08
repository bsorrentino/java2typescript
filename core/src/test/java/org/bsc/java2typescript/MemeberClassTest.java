package org.bsc.java2typescript;

import static org.hamcrest.core.IsEqual.equalTo;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import org.bsc.java2typescript.TypescriptConverter.Compatibility;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author bsorrentino
 *
 */
public class MemeberClassTest extends AbstractConverterTest {

    
    @Test
    public void testMemberClassInfos() {
        
        final TSType type = TSType.from( java.util.Map.Entry.class );
                
        Assert.assertThat(type.supportNamespace() , equalTo(true));
        Assert.assertThat(type.getNamespace() , equalTo("java.util"));
        
        Assert.assertThat(type.getTypeName() , equalTo("java.util.Map$Entry"));
        Assert.assertThat(type.getSimpleTypeName() , equalTo("Map$Entry"));
    }
    
    @Test
    public void testMemberClass() throws Exception {
        
        TypescriptConverter.Context ctx = 
                converter.contextOf(TSType.from( java.util.Map.Entry.class ), declaredTypeMap(), Compatibility.NASHORN);
        
        Assert.assertThat(ctx, IsNull.notNullValue());
        
        final String result  = ctx.getClassDecl().toString();
    
        Assert.assertThat( result, IsNull.notNullValue());      
        Assert.assertThat( result, equalTo("interface Map$Entry<K, V> {"));

    }
    
    @Test
    public void testMemberClassUsage() throws Exception {
        
        final Class<?> type = java.util.Map.class;
        
        final Method m = type.getMethod("entrySet");         
        final Type rType = m.getGenericReturnType();

        final String result = TypescriptConverter.convertJavaToTS(rType, m, 
                TSType.from(type),
                declaredTypeMap( TSType.from(java.util.Set.class), TSType.from(java.util.Map.Entry.class)), 
                true, 
                Optional.empty());

        Assert.assertThat( result, equalTo("Set<Map$Entry<K, V>>"));
    }

}
