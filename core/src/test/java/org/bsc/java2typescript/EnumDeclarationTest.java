package org.bsc.java2typescript;

import java.util.Collections;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A Java enum is emitted as a native, string-valued TypeScript enum (not a class), so GraalJS'
 * read-as-string coercion type-checks while a bare string is still rejected where the enum is
 * required.
 *
 * @see org.bsc.java2typescript.transformer.TSJavaClass2DeclarationTransformer
 */
public class EnumDeclarationTest extends AbstractConverterTest {

    enum Color { RED, GREEN, BLUE }

    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter = Java2TSConverter.builder().compatibility(Compatibility.NASHORN).build();
    }

    @Test
    public void enumBecomesNativeStringEnum() {

        final String result =
                converter.javaClass2DeclarationTransformer(0, TSType.of(Color.class), Collections.emptyMap());

        assertNotNull(result);

        // native TS enum, not a class
        assertTrue(result, result.contains("enum EnumDeclarationTest$Color {"));
        assertFalse(result, result.contains("class "));

        // each constant maps to its own name as a string
        assertTrue(result, result.contains("RED = \"RED\""));
        assertTrue(result, result.contains("GREEN = \"GREEN\""));
        assertTrue(result, result.contains("BLUE = \"BLUE\""));

        // wrapped in its package namespace
        assertTrue(result, result.contains("declare namespace org.bsc.java2typescript"));
    }
}
