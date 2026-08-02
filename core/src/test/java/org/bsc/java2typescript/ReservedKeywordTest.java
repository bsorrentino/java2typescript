package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Parameters whose Java name collides with a TypeScript reserved keyword must be renamed,
 * otherwise the generated declaration is not valid TypeScript.
 *
 * @see TSConverterStatic#getParameterName(Parameter)
 */
public class ReservedKeywordTest {

    // 'type', 'string' and 'number' are TS reserved words but perfectly valid Java identifiers,
    // so they survive to the reflection layer where getParameterName must rename them.
    interface Sample {
        void m(String type, int number, String string, String name);
    }

    private static Parameter param(int index) throws Exception {
        final Method m = Sample.class.getMethod("m", String.class, int.class, String.class, String.class);
        return m.getParameters()[index];
    }

    @Test
    public void reservedKeywordParametersGetSuffixed() throws Exception {
        // guard: this test is meaningless unless the module is compiled with -parameters
        assertTrue("expected real parameter names (compile with -parameters)", param(0).isNamePresent());

        assertEquals("type_p", TSConverterStatic.getParameterName(param(0)));
        assertEquals("number_p", TSConverterStatic.getParameterName(param(1)));
        assertEquals("string_p", TSConverterStatic.getParameterName(param(2)));
    }

    @Test
    public void nonReservedParameterKeptAsIs() throws Exception {
        assertEquals("name", TSConverterStatic.getParameterName(param(3)));
    }
}
