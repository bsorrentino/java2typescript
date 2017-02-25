package org.bsc.processor;

import io.reactivex.Observable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
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
                    
            /**
             * 
             * @param annotations
             * @param roundEnv
             */
            public Context( Set<? extends TypeElement> annotations,
                            RoundEnvironment roundEnv ) 
            {
                    this.annotations    = annotations;
                    this.roundEnv       = roundEnv;
            }

            /**
             * 
             * @return 
             */
            public final com.sun.source.util.Trees getTrees() {
                return com.sun.source.util.Trees.instance(processingEnv);
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
            public Observable<? extends Element> rxElementFromAnnotations() {

                    return Observable
                            .fromIterable(annotations)
                            .flatMap( (e) -> Observable.fromIterable(roundEnv.getElementsAnnotatedWith(e) ))
                                ;
            }
        }

    	protected void info(String fmt, Object...args) {
		final String msg = java.lang.String.format( fmt, (Object[])args);
		processingEnv.getMessager().printMessage(Kind.NOTE, msg );
	}

	protected void warn(String fmt, Object...args) {
		final String msg = java.lang.String.format( fmt, (Object[])args);
		processingEnv.getMessager().printMessage(Kind.WARNING, msg);
	}

	protected void warn(String msg, Throwable t) {
		processingEnv.getMessager().printMessage(Kind.WARNING, msg);
		t.printStackTrace(System.err);
	}

	protected void error(String fmt, Object...args) {
		final String msg = java.lang.String.format( fmt, (Object[])args);
		processingEnv.getMessager().printMessage(Kind.ERROR, msg);
	}

	protected void error(String msg, Throwable t) {
		processingEnv.getMessager().printMessage(Kind.ERROR, msg );
		t.printStackTrace(System.err);
	}
        
        
	/**
	 * @filer
	 * @param subfolder subfolder (e.g. confluence)
	 * @param filePath  relative path (e.g. children/file.wiki)
	 * @return
	 * @throws IOException
	 */
	protected FileObject getSourceOutputFile( Filer filer, 
                                            Path subfolder,
                                            Path filePath) throws IOException 
        {

		Element e = null;
		final FileObject res = 
                        filer.createResource(
                                StandardLocation.SOURCE_OUTPUT,
				subfolder.toString(), 
                                filePath.toString(), 
                                e);
		return res;
	}
        
    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        info( "PROCESSOR START");
        
        if (roundEnv.processingOver())
                return false;
                
        final Context processinContext = new Context( annotations, roundEnv );
        
        return process( processinContext );
    }

    public abstract boolean process( Context processingContext );
        
}