package org.bsc.java2typescript;

import org.bsc.java2typescript.transformer.TSJavaClass2DeclarationTransformer;
import org.bsc.java2typescript.transformer.TSJavaClass2StaticDefinitionTransformer;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * @author bsorrentino
 */
public class TSConverter extends TSConverterStatic {
    public enum Compatibility {
        NASHORN, RHINO, GRAALJS;

        public String javaType(String fqn) {
            switch (this.ordinal()) {
                case 1:
                    return format("Packages.%s", fqn);
                default:
                    return format("Java.type(\"%s\")", fqn);
            }
        }
    }

    final Compatibility compatibility;

    public TSConverter(Compatibility compatibility) {
        super();
        this.compatibility = compatibility;
    }

    /**
     * @return
     */
    public final boolean isRhino() {
        return compatibility == Compatibility.RHINO;
    }

    /**
     * @param type
     * @param declaredTypeMap
     * @return
     */
    public String processStatic(TSType type, java.util.Map<String, TSType> declaredTypeMap) {

        final TSConverterContext ctx = TSConverterContext.of(type, declaredTypeMap, compatibility);

        final TSJavaClass2StaticDefinitionTransformer transformer = new TSJavaClass2StaticDefinitionTransformer();

        return transformer.apply(ctx).toString();
    }

    /**
     * @param m
     * @param type
     * @param declaredTypeMap
     * @param packageResolution
     * @return
     */
    public <E extends Executable> String getMethodParametersAndReturnDecl(E m, TSType type,
                                                                          java.util.Map<String, TSType> declaredTypeMap, boolean packageResolution) {

        return TSConverterContext.of(type, declaredTypeMap, compatibility)
                .getMethodParametersAndReturnDecl(m, packageResolution);
    }


    /**
     * @param level
     * @param tstype
     * @param declaredTypeMap
     * @return
     */
    public String processClass(int level, TSType tstype, java.util.Map<String, TSType> declaredTypeMap) {

        final TSConverterContext ctx = TSConverterContext.of(tstype, declaredTypeMap, compatibility);

        final TSJavaClass2DeclarationTransformer transformer = new TSJavaClass2DeclarationTransformer();

        return transformer.apply(ctx).toString();

    }

}
