package org.bsc.java2typescript;

import java.lang.reflect.Constructor;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Public constructors are rendered as TypeScript {@code constructor( ... )} declarations by
 * {@link TSConverterContext#getConstructorDecl(Constructor)}.
 */
public class PublicConstructorTest extends AbstractConverterTest {

    static class Bean {
        public Bean() {}
        public Bean(String name, int count) {}
        public Bean(String... tags) {}
    }

    private TSConverterContext ctx() {
        return TSConverterContext.of(TSType.of(Bean.class),
                declaredTypeMap(),
                Java2TSConverter.Options.of(Compatibility.NASHORN));
    }

    @Test
    public void noArgConstructor() throws Exception {
        final Constructor<?> c = Bean.class.getConstructor();
        // empty parameter list renders as "(  )" to match the existing method-decl convention
        assertEquals("constructor(  )", ctx().getConstructorDecl(c));
    }

    @Test
    public void constructorWithParameters() throws Exception {
        final Constructor<?> c = Bean.class.getConstructor(String.class, int.class);
        assertEquals("constructor( name:string, count:int )", ctx().getConstructorDecl(c));
    }

    @Test
    public void varArgsConstructor() throws Exception {
        final Constructor<?> c = Bean.class.getConstructor(String[].class);
        assertEquals("constructor( ...tags:string[] )", ctx().getConstructorDecl(c));
    }
}
