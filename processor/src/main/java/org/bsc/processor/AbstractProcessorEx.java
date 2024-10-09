package org.bsc.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 *
 * @author bsoorentino
 */
public abstract class AbstractProcessorEx extends AbstractProcessor {

    /**
     *
     */
    public class Context {

        public final Set<? extends TypeElement> annotations;
        public final RoundEnvironment roundEnv;

        final java.util.Map<String, String> optionMap ;
        /**
         *
         * @param annotations
         * @param roundEnv
         */
        public Context(Set<? extends TypeElement> annotations,
                RoundEnvironment roundEnv) {
            this.annotations = annotations;
            this.roundEnv = roundEnv;
            
            this.optionMap =  Optional.ofNullable(processingEnv.getOptions())
                                    .orElse( Collections.emptyMap() );
        }

        /**
         *
         * @return
         */
        public final java.util.Map<String, String> getOptionMap() {
            return processingEnv.getOptions();
        }

        /**
         *
         * @return
         */
        public java.util.List<? extends Element> elementFromAnnotations( ) {

            return annotations.stream()
                    .flatMap((e) -> roundEnv.getElementsAnnotatedWith(e).stream())
                    .collect(Collectors.toList());
        }

        /**
         * 
         * @return
         */
        public java.util.List<? extends Element> elementFromAnnotationsWithFilter( Predicate<? super TypeElement> filter ) {
        	
        		return annotations.stream()
        				.filter( filter )
        				.flatMap((e) -> roundEnv.getElementsAnnotatedWith(e).stream() )
        				.collect( Collectors.toList());
        }
    }
        
    protected void info(String fmt, Object... args) {
        final String msg = java.lang.String.format(fmt, (Object[]) args);
        processingEnv.getMessager().printMessage(Kind.NOTE, msg);
    }

    protected void warn(String fmt, Object... args) {
        final String msg = java.lang.String.format(fmt, (Object[]) args);
        processingEnv.getMessager().printMessage(Kind.WARNING, msg);
    }

    protected void warn(String msg, Throwable t) {
        processingEnv.getMessager().printMessage(Kind.WARNING, msg);
        t.printStackTrace(System.err);
    }

    protected void error(String fmt, Object... args) {
        final String msg = java.lang.String.format(fmt, (Object[]) args);
        processingEnv.getMessager().printMessage(Kind.ERROR, msg);
    }

    protected void error(String msg, Throwable t) {
        processingEnv.getMessager().printMessage(Kind.ERROR, msg);
        t.printStackTrace(System.err);
    }

    /**
     * @param subfolder subfolder (e.g. confluence)
     * @param filePath relative path (e.g. children/file.wiki)
     * @return
     * @throws IOException
     */
    protected FileObject getSourceOutputFile(Path subfolder,
            Path filePath) throws IOException {
        final Filer filer = processingEnv.getFiler();

        Element e = null;
        final FileObject res
                = filer.createResource(
                        StandardLocation.SOURCE_OUTPUT,
                        subfolder.toString(),
                        filePath.toString(),
                        e);
        return res;
    }

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.isEmpty() ) {
            return false;
        }

        info("PROCESSOR [%s] START", getClass().getName());

        final Context processinContext = new Context(annotations, roundEnv);

        try {
            return process(processinContext);
        } catch (Exception ex) {
            error("PROCESSING ERROR", ex);
        }
        return false;
    }

    /**
     * 
     * @param am
     * @param supplier
     * @return
     */
    protected <R extends Map<String,Object>> R toMapObject( AnnotationMirror am, java.util.function.Supplier<R> supplier ) {

		final Collector<Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>, R, R> c = 
				Collector.of( 
					supplier, 
					( map, entry ) -> 
						map.put( entry.getKey().getSimpleName().toString(), entry.getValue().getValue()),
					( v1, v2 ) -> v1
					);
					
	    final R result = am.getElementValues()
			.entrySet()
			.stream()
			.collect( c );
	    
	    return result;
    
    }

    /**
     * 
     * toJsonObject( am, ( builder ) -> builder.build() );
     * 
     * 
     * @param am
     * @param finisher
     * @return
     */
    protected <R> R toJsonObject( AnnotationMirror am, java.util.function.Function<JsonObjectBuilder, R> finisher ) {

		final Collector<Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>, JsonObjectBuilder, R> c = 
				Collector.of( 
					() -> Json.createObjectBuilder(), 
					( builder, entry ) -> {
						final String k =  entry.getKey().getSimpleName().toString();
						final Object v = entry.getValue().getValue();
						
						if( v == null ) builder.addNull(k);
						else if( v instanceof Boolean ) builder.add(k, (Boolean)v );
						else builder.add(k, String.valueOf(v));

					},
					( v1, v2 ) -> v1,
					finisher );
					
	    final R result = am.getElementValues()
			.entrySet()
			.stream()
			.collect( c );
	    
	    return result;
    
    }
    
    public abstract boolean process(Context processingContext) throws Exception;

}
