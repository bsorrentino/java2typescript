package org.bsc.processor;

import static org.bsc.processor.TypescriptHelper.convertJavaToTS;
import static org.bsc.processor.TypescriptHelper.getClassDecl;
import static org.bsc.processor.TypescriptHelper.getName;
import static org.bsc.processor.TypescriptHelper.getSimpleName;
import static org.bsc.processor.TypescriptHelper.isStaticMethod;

import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.Consumer;
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

    static final List<Class<?>> PREDEFINED_CLASSES = Arrays.asList(
    		Class.class,
    		Serializable.class,
    		Closeable.class,
    		AutoCloseable.class,
    		Comparable.class,
    		Cloneable.class,
    		RandomAccess.class,
    		Consumer.class,
    		UnaryOperator.class,
    		Supplier.class,
    		Predicate.class,
    		Runnable.class
    	);

    static final List<TSType> REQUIRED_CLASSES = Arrays.asList(
    		new TSType(java.lang.String.class),
    		new TSType(java.util.Collection.class),
    		new TSType(java.util.Collections.class, true),
    		new TSType(java.util.List.class),
    		new TSType(java.util.Set.class),
    		new TSType(java.util.Map.class),
    		new TSType(java.util.stream.Stream.class, true),
    		new TSType(java.util.Optional.class, true)
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

        final String targetDefinitionFile	= processingContext.getOptionMap().getOrDefault("ts.outfile", "out");
        //final String compatibility 		= processingContext.getOptionMap().getOrDefault("compatibility", "nashorn");
       
        final String definitionsFile	= targetDefinitionFile.concat(".d.ts");
        final String typesFile		 = targetDefinitionFile.concat("-types.ts");
        
        try( 
        		final java.io.Writer wD = openFile( Paths.get(definitionsFile), "headerD.ts" ); 
        		final java.io.Writer wT = openFile( Paths.get(typesFile), "headerT.ts" );  
        	) {
        	    
	        final Set<TSType> types = enumerateDeclaredPackageAndClass( processingContext );

	        types.addAll(REQUIRED_CLASSES);
	        	        
	        final Set<Class<?>> classes = types.stream()
	        										.map( t -> t.getValue() )
	        										.collect( Collectors.toSet());
	
		    final java.util.Map<String, Class<?>> declaredClasses = 
		    		classes.stream().collect( Collectors.toMap( clazz -> clazz.getName() , clazz -> clazz ));
	
			PREDEFINED_CLASSES.forEach( clazz -> declaredClasses.put( clazz.getName(), clazz) );
	
			types.stream()
				.filter( t -> !PREDEFINED_CLASSES.contains(t.getValue()) )
				.map( t -> processClass( t, declaredClasses))
				.forEach( s -> {
					try {
						wD.append( s );
					} catch (IOException e) {
						error( "error adding [%s]", s);
					}
				} );

			wT.append( String.format("/// <reference path=\"%s\"/>", definitionsFile) ).append( "\n\n");
			types.stream()
				.filter( t -> t.isExport() )
				.map( t -> t.getValue() )
				.map( clazz -> processStatic( clazz, declaredClasses))
				.forEach( s -> {
					try {
						wT.append( s );
					} catch (IOException e) {
						error( "error adding [%s]", s);
					}
				} );

        } // end try-with-resources

        return true;
    }

    /**
     *
     * @param declaringClass
     * @param pd
     * @param declaredClassMap
     * @return
     */
    private String getPropertyDecl(	Class<?> declaringClass, 
    									PropertyDescriptor pd, 
    									java.util.Map<String, Class<?>> declaredClassMap ) {

	    final StringBuilder sb = new StringBuilder();

	    sb.append(pd.getName());
	    if( declaringClass.isInterface()) sb.append('?');

	    sb.append(':');

	    final Method getter = pd.getReadMethod();

	    if( getter != null ) {

			final Type rType = getter.getGenericReturnType();
			if( rType instanceof ParameterizedType ) {

				final Type pClass =  ((ParameterizedType)rType).getActualTypeArguments()[0];

				final String typeName = pClass.getTypeName();

				try {
					final String name = getName( pClass, pd.getPropertyType(), true);

					final String r = rType.getTypeName()
							.replaceAll(typeName, name)
							.replaceAll("<[\\w\\?]>", "<any>")
							;

					info( "getPropertyDecl: [%s] [%s] [%s] [%s]", pd.getName(), typeName, rType.getTypeName(), r);

					return sb.append( r ).toString();

				} catch (ClassNotFoundException e) {

					warn( "getPropertyDecl: type [%s] not found!", typeName);

				}

	    		}

	    }

	    final String tsType = convertJavaToTS( pd.getPropertyType(),
                                                declaringClass,
                                                declaredClassMap,
                                                true);

        return sb.append(tsType)
                 .toString();
	}

    /**
     * 
     * @param m
     * @param declaringClass
     * @param declaredClassMap
     * @return
     */
    private String getMethodParametersDecl(	Method m, 
    											Class<?> declaringClass, 
    											java.util.Map<String, Class<?>> declaredClassMap,
    											boolean packageResolution ) 
    {
        final Class<?> returnType = m.getReturnType();

        final Parameter[] params = m.getParameters();

        final String params_string =
        		Arrays.stream(params)
        				.map( (tp) ->
        					String.format( "%s:%s",
        						tp.getName(),
        						convertJavaToTS(tp.getType(),declaringClass,declaredClassMap, packageResolution) ) )
        				.collect(Collectors.joining(", "))
        				;

	    	final String tsType = 
	    			convertJavaToTS(	returnType,
	    							declaringClass,
	    							declaredClassMap,
	    							packageResolution);

	    	return  new StringBuilder()
	    	        		.append("( ")
	    	        		.append(params_string)
	    				.append(" ):")
	    				.append(tsType)
	    				.toString();
    }
    
    /**
     * 
     * @param sb
     * @param m
     */
    private void appendStaticMethodTypeParameters( final StringBuilder sb, final Method m ) {
		final TypeVariable<?>[] return_type_parameters = m.getReturnType().getTypeParameters();

		if( return_type_parameters.length > 0 ) {

			final String pp = 
					Arrays.asList( return_type_parameters )
    				.stream()
    				.map( t -> t.getName() )
    				.collect(Collectors.joining(",", "<", ">")) ;
			
			sb.append( pp );

        }
    }
    
    /**
     * 
     * @param m
     * @param declaringClass
     * @param declaredClassMap
     * @return
     */
    private String getFactoryMethodDecl( final Method m, Class<?> declaringClass, java.util.Map<String, Class<?>> declaredClassMap ) {

        final StringBuilder sb = new StringBuilder();

        	sb.append(m.getName());
    		
    		appendStaticMethodTypeParameters(sb, m);

        sb.append( getMethodParametersDecl(m, declaringClass, declaredClassMap, false) );

        return  sb.toString();

    }

    /**
     *
     * @param m
     * @param declaringClass
     * @param declaredClassMap
     * @return
     */
    private String getMethodDecl( final Method m, Class<?> declaringClass, java.util.Map<String, Class<?>> declaredClassMap ) {

        final StringBuilder sb = new StringBuilder();

        if( Modifier.isStatic(m.getModifiers()) ) {

        		if( declaringClass.isInterface() ) {
        			sb.append( "// ");
        		}

        		sb.append("static ").append(m.getName());
        		
        		appendStaticMethodTypeParameters(sb, m);

        }
        else {

        		sb.append(m.getName());

        }

        //if( m.getDeclaringClass().isInterface()) sb.append('?');
        sb.append( getMethodParametersDecl(m, declaringClass, declaredClassMap, true) );

        return  sb.toString();

    }

    /**
     *
     * @param type
     * @return
     */
    private Set<Method> getMethods( final Class<?> type) {
		final Predicate<Method> include = m ->
			!m.isBridge() &&
			!m.isSynthetic() &&
			Modifier.isPublic( m.getModifiers() ) &&
			Character.isJavaIdentifierStart(m.getName().charAt(0)) &&
			m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		return Stream.concat( Stream.of(type.getMethods()), Stream.of(type.getDeclaredMethods()) )
			.filter(include)
			.collect( Collectors.toSet( ) );

    }

    /**
     *
     * @param sb
     * @param type
     * @param declaredClassMap
     */
    private void processNestedClasses( StringBuilder sb, TSType tstype, java.util.Map<String, Class<?>> declaredClassMap ) {

    		final Class<?> type = tstype.getValue();
    		
        final Class<?> nestedClasses[] = type.getClasses();

        if( nestedClasses.length == 0 ) return;

        sb.append( "export module " )
     	  .append(type.getSimpleName())
    	      .append(" {\n\n")
    	      ;

        Arrays.stream(nestedClasses)
        		.map( cl ->  new TSType(cl) )
        		.map( t -> processClass(t, declaredClassMap) )
        		.forEach( (decl) -> sb.append(decl) );

        sb.append("\n} // end module ")
        		.append(type.getSimpleName())
        		.append('\n')
        	;
    }

    /**
     *
     * @param sb
     * @param type
     * @param declaredClassMap
     */
    private void processEnum( StringBuilder sb, Class<?> type, java.util.Map<String, Class<?>> declaredClassMap ) {
    		if( !type.isEnum() ) return ;

   		Arrays.stream( type.getEnumConstants() )
		.forEach( (c) -> {
			sb.append( '\t' )
              .append( "static ")
              .append(  c.toString() )
              .append( ':')
              .append( type.getSimpleName() )
              .append( ';' )
              .append(  '\n' )
              ;
		});

   		sb.append( '\n' );

    }

    
    /**
     * 
     * @param declaredClass
     * @return
     */
    private String processStatic( Class<?> type, java.util.Map<String, Class<?>> declaredClassMap ) {
    	
    		final StringBuilder sb = new StringBuilder();
    		
    		final java.util.Set<Method> methodSet =
	        getMethods( type )
	        .stream()
	        .filter( TypescriptHelper::isStaticMethod )
	        .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<Method>() ));
        
        if( !methodSet.isEmpty() ) {
        	
        		sb.append("interface ")
        			.append( type.getSimpleName() )
        			.append("Static {\n\n")
    			;
        		
        		methodSet.stream()
	            .map( md -> getFactoryMethodDecl(md, type, declaredClassMap) )
	            .sorted().forEach( (decl) ->
		    	        sb.append( '\t' )
		    	          .append(decl)
		    	          .append(  ENDL ))
	    	    		;
        }
        
        sb.append( "}\n\n" )
        		.append("export const ")
        		.append(type.getSimpleName())
        		.append(": ")
        		.append(type.getSimpleName())
        		.append("Static = Java.type(\"")
        		.append( type.getName() )
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
    private String processClass( TSType tstype, java.util.Map<String, Class<?>> declaredClassMap )   {

        final StringBuilder sb = new StringBuilder();

		final Class<?> type = tstype.getValue();
		
        final String namespace = type.getPackage().getName();

        if( !type.isMemberClass() )
	        sb.append( "declare namespace " )
	           .append(namespace)
	           .append(" {\n\n")
	            ;

        sb.append( getClassDecl(type, declaredClassMap) )
          .append("\n\n");

        processEnum(sb, type, declaredClassMap);

        /*
		final BeanInfo bi = getBeanInfo(type);
        
        final PropertyDescriptor[] pds = bi.getPropertyDescriptors();

		*/
        final java.util.Set<Method> methodSet =
    	        getMethods( type )
    	        .stream()
    	        .filter( md -> (tstype.isExport() && isStaticMethod(md))==false )
    	        .filter( (md) -> {
	        		final String name = md.getName();
	        		return !( 	name.contains("$")		|| // remove unnamed
        						name.equals("getClass")  ||
        						name.equals("hashCode")  ||
	        					name.equals("wait")		||
	        					name.equals("notify")	||
	        					name.equals("notifyAll") );
	        })
    	        /*
    	        .filter( md ->  {// Remove setter and getter
    	        	
    	            final boolean match = Arrays.asList(pds)
    	            			.stream()
    	            			.noneMatch( pd -> (md.equals(pd.getReadMethod()) || md.equals(pd.getWriteMethod())) );

    	            return match;
    	        })
    	        */
    	        .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<Method>() ));

        /*
        final java.util.Set<PropertyDescriptor> propertySet =
        		Arrays.stream(pds)
            .filter( TypescriptHelper::isPropertyValid )
            .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<PropertyDescriptor>(pds.length) ))
            ;

        propertySet.stream()
		    .map( pd -> {
		    		final boolean duplicate = methodSet.stream().anyMatch( m -> m.getName().equals(pd.getName()));
		    		final String decl = getPropertyDecl( type, pd, declaredClassMap);
		    		return ( duplicate ) ? "// ".concat(decl) : decl;
		    })
        		.sorted()
        		.forEach((decl) ->
	            sb.append( '\t' )
	              .append(decl)
	              .append(  ENDL ))
	              ;
        */
        
        methodSet.stream()
	        .map( md -> getMethodDecl(md, type, declaredClassMap) )
	        .sorted().forEach( (decl) ->
	            sb.append( '\t' )
	              .append(decl)
	              .append(  ENDL ))
	        		;

        sb.append("\n} // end ")
        		.append(getSimpleName(type))
        		.append('\n');

        // NESTED CLASSES
        processNestedClasses(sb, tstype, declaredClassMap);

        if( !type.isMemberClass() )
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
    
    
	@SuppressWarnings("serial")
	static class TSType extends HashMap<String,Object>{

		public TSType() {
			super(2);
		}
		public TSType( Class<?> cl ) {
			this();
			put( "value", cl);
		}
		public TSType( Class<?> cl, boolean export ) {
			this();
			put( "value", cl);
			put( "export", export);
			
		}

		public Class<?> getValue() {
			return  getClassFrom(super.get("value"));
		}
		
		public boolean isExport() {
			return (boolean) super.getOrDefault("export", false);
		}
		
	    /**
	     *
	     * @param dt
	     * @return
	     */
	    private Class<?> getClassFrom( Object dt ) {
	        	if( dt instanceof Class ) return (Class<?>)dt;
	        	
	    		try {
	            return Class.forName(dt.toString());
	        } catch (ClassNotFoundException e1) {
	            throw new RuntimeException(String.format("class not found [%s]",dt), e1);
	        }
	    }
		@Override
		public boolean equals(Object o) {
			return getValue().equals(((TSType)o).getValue());
		}

		@Override
		public int hashCode() {
			return getValue().hashCode();
		}
		
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


}
