package org.bsc.java2typescript;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The {@code <out>-types.ts} runtime binding is slimmed to {@code typeof <class>} for classes and
 * enums (whose full static side lives in the {@code .d.ts}), while interfaces keep an explicit
 * {@code …Static} type because they have no {@code typeof} value form.
 *
 * @see org.bsc.java2typescript.transformer.TSJavaClass2StaticDefinitionTransformer
 */
public class StaticDefinitionTest extends AbstractConverterTest {

    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter = Java2TSConverter.builder().compatibility(Compatibility.NASHORN).build();
    }

    @Test
    public void classBindsViaTypeof() {

        final String result = converter.javaClass2StaticDefinitionTransformer(
                TSType.of(java.util.ArrayList.class).setExport(true),
                declaredTypeMap(TSType.of(java.util.ArrayList.class)));

        assertNotNull(result);
        // slim binding, no duplicated static interface
        assertTrue(result, result.contains("export const ArrayList: typeof java.util.ArrayList = Java.type(\"java.util.ArrayList\")"));
        assertFalse(result, result.contains("ArrayListStatic"));
    }

    @Test
    public void enumBindsViaTypeof() {

        final String result = converter.javaClass2StaticDefinitionTransformer(
                TSType.of(java.util.concurrent.TimeUnit.class).setExport(true),
                declaredTypeMap(TSType.of(java.util.concurrent.TimeUnit.class)));

        assertNotNull(result);
        assertTrue(result, result.contains("export const TimeUnit: typeof java.util.concurrent.TimeUnit = Java.type(\"java.util.concurrent.TimeUnit\")"));
        assertFalse(result, result.contains("TimeUnitStatic"));
    }

    @Test
    public void interfaceKeepsStaticType() {

        final String result = converter.javaClass2StaticDefinitionTransformer(
                TSType.of(java.lang.Runnable.class).setExport(true),
                declaredTypeMap(TSType.of(java.lang.Runnable.class)));

        assertNotNull(result);
        // interfaces have no `typeof` value form, so they keep the explicit static interface
        assertTrue(result, result.contains("interface RunnableStatic"));
        assertTrue(result, result.contains("export const Runnable: RunnableStatic = Java.type(\"java.lang.Runnable\")"));
    }
}
