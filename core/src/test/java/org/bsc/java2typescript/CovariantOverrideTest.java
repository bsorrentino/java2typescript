package org.bsc.java2typescript;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * A subclass that narrows the return type of an inherited method must re-declare it.
 * <p>
 * Members of an emitted parent are inherited through {@code extends} and are deliberately not
 * re-listed, but a covariant override is not the same member: dropping it leaves the parent's
 * wider return type in force, which breaks the fluent builders it is normally used for
 * ({@code b.setParam(..).raise()} would resolve {@code setParam} to the parent type).
 *
 * @see org.bsc.java2typescript.transformer.TSJavaClass2DeclarationTransformer
 */
public class CovariantOverrideTest extends AbstractConverterTest {

    public static class Parent {
        public Parent setValue(String value) { return this; }
        public Parent setOther(int other) { return this; }
        public String untouched() { return ""; }
    }

    public static class Child extends Parent {
        @Override
        public Child setValue(String value) { return this; }
        public void raise() { }
    }

    /** Overrides the return type without narrowing it: nothing new to declare. */
    public static class SameReturnChild extends Parent {
        @Override
        public Parent setValue(String value) { return this; }
    }

    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter = Java2TSConverter.builder().compatibility(Compatibility.NASHORN).build();
    }

    private String declarationOf(Class<?> type) {
        return converter.javaClass2DeclarationTransformer(0, TSType.of(type),
                declaredClassMap(Parent.class, Child.class, SameReturnChild.class));
    }

    @Test
    public void narrowedReturnTypeIsRedeclared() {

        final String result = declarationOf(Child.class);

        assertTrue(result, result.contains("extends"));
        assertTrue("setValue must be re-declared with the narrowed return type:\n" + result,
                result.contains("setValue"));
        assertTrue("the narrowed return type must be the child:\n" + result,
                result.matches("(?s).*setValue\\([^)]*\\)\\s*:\\s*[^;]*Child.*"));
    }

    @Test
    public void unchangedInheritedMembersAreStillNotRelisted() {

        final String result = declarationOf(Child.class);

        // setOther and untouched are inherited unchanged: they come through `extends`.
        assertTrue("inherited-unchanged members must not be re-listed:\n" + result,
                !result.contains("setOther") && !result.contains("untouched"));
        // The class' own new member is of course declared.
        assertTrue(result, result.contains("raise"));
    }

    @Test
    public void overrideKeepingTheSameReturnTypeIsNotRedeclared() {

        final String result = declarationOf(SameReturnChild.class);

        assertTrue("an override that changes nothing adds no declaration:\n" + result,
                !result.contains("setValue"));
    }
}
