package org.bsc.java2typescript;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class TSNamespace {

    private final String name;

    private final Set<TSType> types;

    private TSNamespace(String name, Set<TSType> types) {
        this.name = name;
        this.types = Collections.unmodifiableSet(types);
    }

    public String getName() {
        return name;
    }

    public Set<TSType> getTypes() {
        return types;
    }

    public static TSNamespace of( String name, Set<TSType> types ) {
        return new TSNamespace( name, types );
    }

    @Override
    public String toString() {
        return format( "TSNamespace: { name: '%s', types: [%s]  }",
            name, getTypes().stream()
                    .map( TSType::toString ).collect(Collectors.joining(",\n")) );
    }
}
