package org.bsc.java2typescript;

import org.bsc.java2typescript.transformer.TSJavaClass2DeclarationTransformer;
import org.bsc.java2typescript.transformer.TSJavaClass2StaticDefinitionTransformer;

import java.lang.reflect.Executable;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * @author bsorrentino
 */
public class Java2TSConverter extends TSConverterStatic {
    public static class Builder {

        private Compatibility compatibility = Compatibility.NASHORN;
        private boolean foreignObjectPrototype = false;

        private Builder() {}

        public Builder compatibility(Compatibility compatibility) {
            this.compatibility = compatibility;
            return this;
        }

        public Builder compatibility(String compatibility) {

            this.compatibility
                    = Optional.ofNullable(compatibility)
                        .map( v -> {
                            try {
                                return Java2TSConverter.Compatibility.valueOf(v.toUpperCase());
                            } catch( Exception e ) {
                                return Compatibility.NASHORN;
                            }
                        })
                        .orElse( Compatibility.NASHORN );
            return this;
        }

        public Builder foreignObjectPrototype(boolean foreignObjectPrototype) {
            this.foreignObjectPrototype = foreignObjectPrototype;
            return this;
        }

        public Builder foreignObjectPrototype(String foreignObjectPrototype) {
            this.foreignObjectPrototype =
                    Optional.ofNullable(foreignObjectPrototype)
                        .map( v -> {
                            try {
                                return Boolean.valueOf(v);
                            } catch( Exception e ) {
                                return false;
                            }
                        })
                        .orElse( false );

            return this;
        }

        public Java2TSConverter build() {
            return new Java2TSConverter(
                    new Options( compatibility, foreignObjectPrototype) );
        }
    }

    /**
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public enum Compatibility {
        NASHORN, RHINO, GRAALJS;

        public String javaType(String fqn) {
            return (this == RHINO ) ?
                    format("Packages.%s", fqn) :
                    format("Java.type(\"%s\")", fqn);
        }
    }

    public static class Options {
        public final Compatibility compatibility;
        public final boolean foreignObjectPrototype;

        private Options(Compatibility compatibility, boolean foreignObjectPrototype) {
            this.compatibility = compatibility;
            this.foreignObjectPrototype = foreignObjectPrototype;
        }

        public static Options of(Compatibility compatibility, boolean foreignObjectPrototype) {
            return new Options(compatibility, foreignObjectPrototype);
        }

        public static Options of(Compatibility compatibility) {
            return new Options(compatibility, false);
        }

        public static Options ofDefault() {
            return new Options(Compatibility.NASHORN, false);
        }
    }

    final Options options;

    final TSJavaClass2DeclarationTransformer javaClass2DeclarationTransformer;
    final TSJavaClass2StaticDefinitionTransformer JavaClass2StaticDefinitionTransformer;

    private Java2TSConverter(Options options) {
        super();
        this.options = options;

        javaClass2DeclarationTransformer = new TSJavaClass2DeclarationTransformer();
        JavaClass2StaticDefinitionTransformer = new TSJavaClass2StaticDefinitionTransformer();
    }

    public final boolean isRhino() {
        return options.compatibility == Compatibility.RHINO;
    }

    public <E extends Executable> String getMethodParametersAndReturnDecl(E m,
                                                                          TSType type,
                                                                          Map<String, TSType> declaredTypeMap,
                                                                          boolean packageResolution)
    {

        return TSConverterContext.of(type, declaredTypeMap, options)
                .getMethodParametersAndReturnDecl(m, packageResolution);
    }

    public String javaClass2StaticDefinitionTransformer(TSType tstype,
                                                        Map<String, TSType> declaredTypeMap) {

        return TSConverterContext.of(tstype, declaredTypeMap, options)
                .apply( JavaClass2StaticDefinitionTransformer )
                .toString();
    }

    public String javaClass2DeclarationTransformer(int level,
                                                   TSType tstype,
                                                   Map<String, TSType> declaredTypeMap) {

        return TSConverterContext.of(tstype, declaredTypeMap, options)
                .apply(javaClass2DeclarationTransformer)
                .toString();

    }

}
