package org.bsc.java2typescript;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public record TSNamespace (String name, Set<TSType> types ) {

    public TSNamespace(String name, Set<TSType> types) {
        this.name = name;
        this.types = Collections.unmodifiableSet(types);
    }

    public static TSNamespace of( String name, Set<TSType> types ) {
        return new TSNamespace( name, types );
    }

    @Override
    public String toString() {
        return format( "TSNamespace: { name: '%s', types: [%s]  }",
            name, types().stream()
                    .map( TSType::toString ).collect(Collectors.joining(",\n")) );
    }
}
