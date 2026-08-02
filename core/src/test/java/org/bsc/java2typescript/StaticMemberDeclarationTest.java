package org.bsc.java2typescript;

import java.util.Collections;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Static members are emitted into the {@code .d.ts} class declaration (carrying the {@code static}
 * keyword) even for exported types, so {@code typeof <class>} exposes the full static side and the
 * {@code <out>-types.ts} binding can collapse to {@code typeof <class>}.
 *
 * @see org.bsc.java2typescript.transformer.TSJavaClass2DeclarationTransformer
 */
public class StaticMemberDeclarationTest extends AbstractConverterTest {

    static class WithStatics {
        public static String make() { return ""; }
        public String instanceMethod() { return ""; }
    }

    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter = Java2TSConverter.builder().compatibility(Compatibility.NASHORN).build();
    }

    @Test
    public void staticMethodEmittedForExportedType() {

        final String result = converter.javaClass2DeclarationTransformer(0,
                TSType.of(WithStatics.class).setExport(true),
                Collections.emptyMap());

        assertNotNull(result);
        // static method is present (previously stripped for exported types) and keeps `static`
        assertTrue(result, result.contains("static make"));
        // instance method still present
        assertTrue(result, result.contains("instanceMethod"));
    }
}
