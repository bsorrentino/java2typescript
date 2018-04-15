package org.bsc.processor;

import static org.bsc.java2typescript.TypescriptConverter.PREDEFINED_TYPES;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.tools.FileObject;

import org.bsc.java2typescript.TSType;
import org.bsc.java2typescript.TypescriptConverter;;

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
    		TSType.from(java.util.Optional.class, true),
    		
            TSType.from(java.lang.Comparable.class),
            TSType.from(java.util.function.Function.class, "Func", false),
            TSType.from(java.util.function.BiFunction.class, "BiFunction", false),
            TSType.from(java.util.function.Consumer.class, "Consumer", false),
            TSType.from(java.util.function.BiConsumer.class, "BiConsumer", false),
            TSType.from(java.util.function.UnaryOperator.class, "UnaryOperator", false),
            TSType.from(java.util.function.BinaryOperator.class, "BinaryOperator", false),
            TSType.from(java.util.function.Supplier.class, "Supplier", false),
            TSType.from(java.util.function.Predicate.class, "Predicate", false),
            TSType.from(java.util.function.BiPredicate.class, "BiPredicate", false),
            TSType.from(java.lang.Runnable.class, "Runnable", false)
            


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
	        
	        types.addAll(PREDEFINED_TYPES);     
	        	
		    final java.util.Map<String, TSType> declaredTypes = 
		    			types.stream()
		    			.collect( Collectors.toMap( tt -> tt.getValue().getName() , tt -> tt  ));
	
		    final TypescriptConverter converter = new TypescriptConverter();
		    
			types.stream()
				.filter( tt -> !PREDEFINED_TYPES.contains(tt) )
				.map( tt -> converter.processClass( 0, tt, declaredTypes))
				.forEach( wD_append );

			wT.append( "/// <reference path=\"").append(definitionsFile).append("\"/>" ).append( "\n\n");
			types.stream()
				.filter( t -> t.isExport() )
				.map( t -> converter.processStatic( t, declaredTypes))
				.forEach( wT_append );

        } // end try-with-resources

        return true;
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
	            .map( am -> toMapObject(am, () -> TSType.from( Void.class) ) )				
    				.collect( Collectors.toSet() )
            ;
    }

}
