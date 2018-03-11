package org.bsc.processor;

import static org.bsc.processor.TypescriptHelper.convertJavaToTS;
import static org.bsc.processor.TypescriptHelper.getClassDecl;
import static org.bsc.processor.TypescriptHelper.getParameterName;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.tools.FileObject;

/**
 *
 * @author bsoorentino
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.bsc.processor.*")
@SupportedOptions({ "ts.outfile" })
@org.kohsuke.MetaInfServices(javax.annotation.processing.Processor.class)
public class TypescriptProcessor extends AbstractProcessorEx {

    final static String ENDL = ";\n";

    static final List<TSType> PREDEFINED_TYPES = Arrays.asList(
    		TSType.from(Class.class),
    		TSType.from(Serializable.class),
    		TSType.from(Closeable.class),
    		TSType.from(AutoCloseable.class),
    		TSType.from(Comparable.class),
    		TSType.from(Cloneable.class),
    		TSType.from(RandomAccess.class),
    		TSType.from(Function.class, "Func", false),
    		TSType.from(BiFunction.class, "BiFunc", false),
    		TSType.from(Consumer.class),
    		TSType.from(BiConsumer.class),
    		TSType.from(UnaryOperator.class),
    		TSType.from(BinaryOperator.class),
    		TSType.from(Supplier.class),
    		TSType.from(Predicate.class),
    		TSType.from(BiPredicate.class),
    		TSType.from(Runnable.class)
    	);

    static final List<TSType> REQUIRED_TYPES = Arrays.asList(
    		TSType.from(java.lang.String.class,true),
    		TSType.from(java.lang.Iterable.class,true),
    		TSType.from(java.util.Iterator.class,true),
    		TSType.from(java.util.Collection.class),
    		TSType.from(java.util.Collections.class, true),
    		TSType.from(java.util.List.class),
    		TSType.from(java.util.Set.class),
    		TSType.from(java.util.Map.class),
    		TSType.from(java.util.stream.Stream.class, true),
    		TSType.from(java.util.stream.Collectors.class,true),
    		TSType.from(java.util.Optional.class, true)
    	);
    
    /**
     * 
     * @param file
     * @param header
     * @return
     * @throws IOException
     */
    private java.io.Writer openFile( Path file, String header ) throws IOException {
    	
        final FileObject out = super.getSourceOutputFile( Paths.get("ts"), file );
        
        info( "output file [%s]", out.getName() );

        final java.io.Writer w = out.openWriter();
        
        try(final java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(header) ) {
    			int c; while( (c = is.read()) != -1 ) w.write(c);
        }
        
        return w;
    }
    /**
     *
     * @param processingContext
     * @return
     */
    @Override
    public boolean process( Context processingContext ) throws Exception {

        final String targetDefinitionFile		= processingContext.getOptionMap().getOrDefault("ts.outfile", "out");
        //final String compatibility 		= processingContext.getOptionMap().getOrDefault("compatibility", "nashorn");
       
        final String definitionsFile	= targetDefinitionFile.concat(".d.ts");
        final String typesFile		= targetDefinitionFile.concat("-types.ts");
        
        try( 
        		final java.io.Writer wD = openFile( Paths.get(definitionsFile), "headerD.ts" ); 
        		final java.io.Writer wT = openFile( Paths.get(typesFile), "headerT.ts" );  
        	) {
        	    
        		final Consumer<String> wD_append = s -> {
					try {
						wD.append( s );
					} catch (IOException e) {
						error( "error adding [%s]", s);
					}        			
        		};
        		final Consumer<String> wT_append = s -> {
					try {
						wT.append( s );
					} catch (IOException e) {
						error( "error adding [%s]", s);
					}        			
        		};
        		
	        final Set<TSType> types = enumerateDeclaredPackageAndClass( processingContext );

	        types.addAll(REQUIRED_TYPES);

	        // Generate Alias
			wD.append("//\n")
			  .append("// TYPE ALIASES\n")
			  .append("//\n\n");
			types.stream()
				.filter( t -> !t.isFunctionalInterface() )
				.filter( t -> t.hasAlias() )
				.map( t -> TypescriptHelper.getAliasDeclaration(t.getValue(), t.getAlias()) )
				.forEach( wD_append  );

	        	types.addAll(PREDEFINED_TYPES);     
	        	
		    final java.util.Map<String, TSType> declaredTypes = 
		    			types.stream()
		    			.collect( Collectors.toMap( tt -> tt.getValue().getName() , tt -> tt  ));
	
			types.stream()
				.filter( tt -> !PREDEFINED_TYPES.contains(tt) )
				.map( tt -> processClass( 0, tt, declaredTypes))
				.forEach( wD_append );

			wT.append( "/// <reference path=\"").append(definitionsFile).append("\"/>" ).append( "\n\n");
			types.stream()
				.filter( t -> t.isExport() )
				.map( t -> processStatic( t, declaredTypes))
				.forEach( wT_append );

        } // end try-with-resources

        return true;
    }
    /**
     * 
     * @param m
     * @param declaringType
     * @param declaredTypeMap
     * @return
     */
    protected <E extends Executable> String getMethodParametersAndReturnDecl(	E m, 
    														TSType declaringType, 
    														java.util.Map<String, TSType> declaredTypeMap,
    														boolean packageResolution ) 
    {
		final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);
    		
    		final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName()) ;
    		
        final Parameter[] params = m.getParameters();

        final String params_string =
        		Arrays.stream(params)
        				.map( (tp) -> {
        					
        					final String name = getParameterName(tp);
        					
        					
        					if( tp.isVarArgs() ) {   
        						
        						String type = null;
        						if( tp.getParameterizedType() instanceof GenericArrayType ) {
        							
        							type = convertJavaToTS( ((GenericArrayType)tp.getParameterizedType()).getGenericComponentType(),
											m,
											declaringType,
											declaredTypeMap, 
											packageResolution, 
											Optional.of( addTypeVar )) ;
        						}
        						else {
        							type = convertJavaToTS( tp.getType().getComponentType(),
            														m,
            														declaringType,
            														declaredTypeMap, 
            														packageResolution, 
            														Optional.of( addTypeVar )) ;
        						}
    							return String.format( "...%s:%s[]", name, type );
        						
        					}
        					
        					final String type = convertJavaToTS( tp.getParameterizedType(),
        														m,
        														declaringType,
        														declaredTypeMap, 
        														packageResolution, 
        														Optional.of( addTypeVar )) ;
        					return String.format( "%s:%s", name, type );
        				})
        				.collect(Collectors.joining(", "))
        				;

        final Type returnType =  ( m instanceof Method ) ?
        								((Method)m).getGenericReturnType() :
        								declaringType.getValue();
        								
	   final String  tsReturnType = 
		    			convertJavaToTS(	returnType,
		    							m,
		    							declaringType,
		    							declaredTypeMap,
		    							packageResolution,
		    							Optional.of( addTypeVar ));
	   
	   
	    	final StringBuilder result = new StringBuilder();
	    	
	    	if( !TypeVarSet.isEmpty() ) {
	    		result
	    			.append('<')
    				.append( TypeVarSet.stream().collect(Collectors.joining(",")))
    				.append('>');	    		
	    	}
	    	return  result
	    	        		.append("( ")
	    	        		.append(params_string)
	    				.append(" ):")
	    				.append(tsReturnType)
	    				.toString();
    }
    
    /**
     *
     * @param m
     * @param declaringClass
     * @param declaredClassMap
     * @return
     */
    private String getMethodDecl( final Method m, TSType declaringClass, java.util.Map<String, TSType> declaredClassMap ) {

        final StringBuilder sb = new StringBuilder();

        if( Modifier.isStatic(m.getModifiers()) ) {

        		if( declaringClass.getValue().isInterface() ) {
        			sb.append( "// ");
        		}

        		sb.append("static ").append(m.getName());

        }
        else {

        		sb.append(m.getName());

        }

        //if( m.getDeclaringClass().isInterface()) sb.append('?');
        sb.append( getMethodParametersAndReturnDecl(m, declaringClass, declaredClassMap, true) );

        return  sb.toString();

    }

    /**
    *
    * @param type
    * @return
    */
   private Set<Field> getFields( final TSType type ) {
	   
		final Predicate<Field> std = f ->
			!f.isSynthetic() &&
			Modifier.isPublic( f.getModifiers() ) &&
			Character.isJavaIdentifierStart(f.getName().charAt(0)) &&
			f.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		return Stream.concat( Stream.of(type.getValue().getFields()), Stream.of(type.getValue().getDeclaredFields()) )
			.filter(std)
			.collect( Collectors.toSet( ) );

   }
    
    /**
     *
     * @param type
     * @return
     */
    private Set<Method> getMethods( final TSType type) {
		final Predicate<Method> include = m ->
			!m.isBridge() &&
			!m.isSynthetic() &&
			Modifier.isPublic( m.getModifiers() ) &&
			Character.isJavaIdentifierStart(m.getName().charAt(0)) &&
			m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		return Stream.concat( Stream.of(type.getValue().getMethods()), Stream.of(type.getValue().getDeclaredMethods()) )
			.filter(include)
			.collect( Collectors.toSet( ) );

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     *
     * @param sb
     * @param type
     * @param declaredTypeMap
     */
    private void processNestedClasses( StringBuilder sb, int level, TSType tstype, java.util.Map<String, TSType> declaredTypeMap ) {

        final Class<?> nestedClasses[] = tstype.getValue().getClasses();

        if( nestedClasses.length == 0 ) return;

        sb.append( "export module " )
     	  .append(tstype.getSimpleTypeName())
    	      .append(" {\n\n")
    	      ;

        Stream.of(nestedClasses)
        		.filter( distinctByKey( c -> c.getSimpleName() ) )
        		.map( cl ->  TSType.from(cl) )
        		.map( t -> processClass( level + 1, t, declaredTypeMap) )
        		.forEach( decl -> sb.append(decl) );

        sb.append("\n} // end module ")
        		.append(tstype.getSimpleTypeName())
        		.append('\n')
        	;
    }

    
    private StringBuilder processEnumDecl( StringBuilder sb, TSType type ) {
		if( type.getValue().isEnum() ) {
			type.setExport(true); // force export
	   		Arrays.stream( type.getValue().getEnumConstants() )
			.forEach( (c) -> 
				sb.append( '\t' )
	              .append( "// ")
	              .append(  c.toString() )
	              .append( ':')
	              .append( type.getSimpleTypeName() )
	              .append( ';' )
	              .append(  '\n' )
			);
	   		sb.append('\n');
		}
		
		return sb;
		
    }

    private StringBuilder processEnumType( StringBuilder sb, TSType type ) {
		if( type.getValue().isEnum() ) {
	   		Arrays.stream( type.getValue().getEnumConstants() )
			.forEach( (c) -> 
				sb.append( '\t' )
	              .append(  c.toString() )
	              .append( ':')
	              .append( type.getTypeName() )
	              .append( ';' )
	              .append(  '\n' )
			);
	   		sb.append('\n');
		}
		
		return sb;
		
    }
    
    /**
     * 
     * @param declaredClass
     * @return
     */
    private String processStatic( TSType type, java.util.Map<String, TSType> declaredClassMap ) {
    	
    		final StringBuilder sb = new StringBuilder();
    		
    		sb.append("interface ")
			.append( type.getSimpleTypeName() )
			.append("Static {\n\n")
			;
        
    		processEnumType(sb, type)
			//Append class property
			.append("\treadonly class:any;\n");

    		Stream.of(type.getValue().getConstructors())
			.filter( c -> Modifier.isPublic(c.getModifiers()) )
			.forEach( c -> {
				sb.append("\tnew")
					.append( getMethodParametersAndReturnDecl( c, type, declaredClassMap, false) )
					.append(ENDL);
				}
			);
    		
    		final java.util.Set<Method> methodSet =
    		        getMethods( type )
    		        .stream()
    		        .filter( TypescriptHelper::isStatic )
    		        .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<>() ));
    	        
		if( !methodSet.isEmpty() ) {
        		
        		methodSet.stream()
	            		.sorted( (a,b) -> a.getName().compareTo(b.getName()))
	            		.forEach( md ->
		    	        sb.append( '\t' )
		    	        	  .append(md.getName()) 
		    	          .append( getMethodParametersAndReturnDecl( md, type, declaredClassMap, false) )
		    	          .append(  ENDL ))
	    	    		;
        }
        
        sb.append( "}\n\n" )
        		.append("export const ")
        		.append(type.getSimpleTypeName())
        		.append(": ")
        		.append(type.getSimpleTypeName())
        		.append("Static = Java.type(\"")
        		.append( type.getValue().getName() )
        		.append("\")")
        		.append( ENDL )
        		.append("\n\n")
        		;
        		
    		return sb.toString();
    }
    
    
    /**
     *
     * @param bi
     * @param declaredClassMap
     * @return
     */
    private String processClass( int level, TSType tstype, java.util.Map<String, TSType> declaredClassMap )   {

        final StringBuilder sb = new StringBuilder();

        final String namespace = tstype.getValue().getPackage().getName();

        if( !tstype.getValue().isMemberClass() )
	        sb.append( "declare namespace " )
	           .append(namespace)
	           .append(" {\n\n")
	            ;

        getClassDecl( sb, tstype, declaredClassMap).append("\n\n");

        processEnumDecl(sb, tstype);

        final java.util.Set<Method> methodSet =
    	        getMethods( tstype )
    	        .stream()
    	        .filter( md -> (tstype.isExport() && TypescriptHelper.isStatic(md))==false )
    	        .filter( (md) -> {
	        		final String name = md.getName();
	        		return !( 	name.contains("$")		|| // remove unnamed
        						name.equals("getClass")  ||
        						name.equals("hashCode")  ||
	        					name.equals("wait")		||
	        					name.equals("notify")	||
	        					name.equals("notifyAll") );
	        })
    	        .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<Method>() ));

        methodSet.stream()
	        .map( md -> getMethodDecl(md, tstype, declaredClassMap) )
	        .sorted().forEach( (decl) ->
	            sb.append( '\t' )
	              .append(decl)
	              .append(  ENDL ))
	        		;

        sb.append("\n} // end ")
        		.append(tstype.getSimpleTypeName())
        		.append('\n');

        // NESTED CLASSES
        if( level == 0 ) processNestedClasses( sb, level, tstype, declaredClassMap);

        if( !tstype.getValue().isMemberClass() )
        		sb.append("\n} // end namespace ")
        			.append( namespace )
        			.append('\n');

        return sb.toString();

    }

    /**
     *
     * @param entry
     * @return
     */
    @SuppressWarnings("unchecked")
	private List<? extends AnnotationValue> getAnnotationValueValue(
    		java.util.Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry )
    {

        final AnnotationValue av =  entry.getValue();
        return (List<? extends AnnotationValue>)av.getValue();

    }
    	
    /**
     *
     * @param processingContext
     * @return
     */
    private java.util.Set<TSType> enumerateDeclaredPackageAndClass( final Context processingContext ) {
    		
    		return
			processingContext.elementFromAnnotations( Optional.empty() ).stream()
	            .peek( e -> info( "Anotation [%s]", e.getKind().name()) )
	            .filter( e -> ElementKind.PACKAGE==e.getKind() || ElementKind.CLASS==e.getKind() )
	            .flatMap( e -> e.getAnnotationMirrors().stream() )
	            .peek( m -> info( "Mirror [%s]", m.toString() ))
	            .flatMap( am -> am.getElementValues()
	            						.entrySet()
	            						.stream()
	            						.filter( entry -> "declare".equals(String.valueOf(entry.getKey().getSimpleName())) ))
	            .flatMap( entry -> this.getAnnotationValueValue(entry).stream() )
	            .map( av -> av.getValue() )
	            .filter( v -> v instanceof AnnotationMirror).map( v -> ((AnnotationMirror)v) )
	            .map( am -> toMapObject(am, () -> new TSType() ) )				
    				.collect( Collectors.toSet() )
            ;
    }

    /**
     * 
     * @param sb
     * @param m
     */
	@Deprecated
    @SuppressWarnings("unused")
    private StringBuilder appendStaticMethodTypeParameters( final StringBuilder sb, final Method m ) {
		final TypeVariable<?>[] return_type_parameters = m.getReturnType().getTypeParameters();

		if( return_type_parameters.length > 0 ) {

			final String pp = 
					Arrays.asList( return_type_parameters )
    				.stream()
    				.map( t -> t.getName() )
    				.collect(Collectors.joining(",", "<", ">")) ;
			
			sb.append( pp );

        }
		
		return sb;
    }
    

}
