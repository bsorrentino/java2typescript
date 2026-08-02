package org.bsc.java2typescript;

import java.util.Collections;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


/**
 * When using option "ignoreDeprecated", @Deprecated elements (classes, methods, fields...), will not be declared.
 *
 * @see TODO
 */
public class DeprecatedTest {

    // 'type', 'string' and 'number' are TS reserved words but perfectly valid Java identifiers,
    // so they survive to the reflection layer where getParameterName must rename them.
    class Deprecations {
        public static int validMember;
        @Deprecated public static int invalidMember;
        public void validMethod(String value) {};
        @Deprecated public void invalidMethod(String value) {};
        public class ValidClass { void m(){}; };
        @Deprecated public class InvalidClass { void m(){}; };
    }

    enum WithDeprecatedConstant {
        VALID,
        @Deprecated INVALID
    }

    @Deprecated
    public static class DeprecatedParent { }

    public static class LiveChild extends DeprecatedParent { }


    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter = Java2TSConverter.builder().compatibility(Compatibility.NASHORN).ignoreDeprecated(true).build();
    }

    @Test
    public void deprecatedNotDeclared() {

        final String result =
                converter.javaClass2DeclarationTransformer(0, TSType.of(Deprecations.class), Collections.emptyMap());

        assertNotNull(result);

        // members fields
        assertTrue(result, result.contains("static validMember:int"));
        assertFalse(result, result.contains("static invalidMember:int"));

        //methods
        assertTrue(result, result.contains("validMethod( value:string )"));
        assertFalse(result, result.contains("invalidMethod( value:string )"));

        //classes
        final String validClassRes =
                converter.javaClass2DeclarationTransformer(0, TSType.of(Deprecations.ValidClass.class), Collections.emptyMap());
        assertTrue(validClassRes, validClassRes.contains("class Deprecations$ValidClass"));
        final String invalidClassRes =
                converter.javaClass2DeclarationTransformer(0, TSType.of(Deprecations.InvalidClass.class), Collections.emptyMap());
        assertFalse(invalidClassRes, invalidClassRes.contains("class Deprecations$InvalidClass"));
    }

    @Test
    public void deprecatedEnumConstantNotDeclared() {

        final String result =
                converter.javaClass2DeclarationTransformer(0, TSType.of(WithDeprecatedConstant.class),
                        Collections.emptyMap());

        assertTrue(result, result.contains("VALID = \"VALID\""));
        assertFalse(result, result.contains("INVALID"));
    }

    /**
     * if parent is deprecated, undeprecated child should not extend it's parent.
     * Seem like it shouldn't happen, but it exists in practice. (ex: legacy interfaces)
     */
    @Test
    public void undeclaredParentIsExtendedOnlyInAComment() {

        final String result =
                converter.javaClass2DeclarationTransformer(0, TSType.of(LiveChild.class),
                        // DeprecatedParent absent, as the processor leaves it once filtered out.
                        Collections.singletonMap(LiveChild.class.getName(), TSType.of(LiveChild.class)));

        assertTrue("the parent must be inert, inside a comment:\n" + result,
                result.contains("class DeprecatedTest$LiveChild/* extends DeprecatedTest$DeprecatedParent*/"));
    }
}
