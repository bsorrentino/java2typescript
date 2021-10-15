package org.bsc.java2typescript;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class Issue34Test extends AbstractConverterTest {

    @Test
    public void convertListTest() {

        final Java2TSConverter converter =
                Java2TSConverter.builder()
                    .compatibility(Java2TSConverter.Compatibility.GRAALJS)
                    .foreignObjectPrototype(true)
                    .build();

        int level = 0;

        final String conversion = converter.javaClass2DeclarationTransformer(
                    level,
                    TSType.of( java.util.List.class ),
                    Collections.emptyMap()
        );

        Assert.assertNotNull(conversion);

        final String[] lines = conversion.split( "\n");

        Assert.assertTrue( lines.length > 3);
        Assert.assertEquals(
                "interface List<E> extends Array<E>/* extends Collection<E> */ {",
                lines[2] );

        Optional<String> result = Arrays.stream(lines)
                            .map( String::trim )
                            .filter( l -> l.startsWith("forEach"))
                            .filter( l -> l.startsWith("indexOf"))
                            .filter( l -> l.startsWith("lastIndexOf"))
                            .filter( l -> l.startsWith("sort"))
                            .findFirst();

        Assert.assertFalse( result.isPresent() );
    }
}
