package org.bsc.java2typescript;

import java.lang.reflect.Field;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Public fields are rendered as TypeScript property declarations by
 * {@link TSConverterContext#getFieldDecl(Field)}.
 */
public class PublicFieldTest extends AbstractConverterTest {

    static class Bean {
        public static final String FOO = "x";
        public int count;
        public final String name;

        Bean() { this.name = ""; }
    }

    interface Iface {
        String BAR = "y"; // implicitly public static final
    }

    private TSConverterContext ctxFor(Class<?> type) {
        return TSConverterContext.of(TSType.of(type),
                declaredTypeMap(),
                Java2TSConverter.Options.of(Compatibility.NASHORN));
    }

    private String fieldDecl(Class<?> type, String fieldName) throws Exception {
        final Field f = type.getField(fieldName);
        return ctxFor(type).getFieldDecl(f);
    }

    @Test
    public void staticFinalFieldIsStaticReadonly() throws Exception {
        assertEquals("static readonly FOO:string", fieldDecl(Bean.class, "FOO"));
    }

    @Test
    public void plainFieldIsNameAndType() throws Exception {
        assertEquals("count:int", fieldDecl(Bean.class, "count"));
    }

    @Test
    public void finalInstanceFieldIsReadonly() throws Exception {
        assertEquals("readonly name:string", fieldDecl(Bean.class, "name"));
    }

    @Test
    public void staticFieldOnInterfaceIsCommentedOut() throws Exception {
        // 'static' is illegal on a TS interface member, so it must be commented out.
        assertEquals("// static readonly BAR:string", fieldDecl(Iface.class, "BAR"));
    }
}
