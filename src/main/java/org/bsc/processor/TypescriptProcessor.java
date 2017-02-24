package org.bsc.processor;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * 
 * @author bsoorentino
 */
public class TypescriptProcessor extends AbstractProcessorEx {

    /**
     *
     * @param processingContext
     * @return
     */
    @Override
    public boolean process( Context processingContext ) {
    
        return true;
    }

    private Observable<Class<?>> rxEnumerateDeclaredPackageAndClass( final Context processingContext ) {
        
        final  Observable<Class<?>> result = processingContext.rxElementFromAnnotations()
            .doOnNext((e) -> info( "Anotation [%s]", e.getKind().name()) )
            .filter( (e) -> ElementKind.PACKAGE==e.getKind() || ElementKind.CLASS==e.getKind() )
            .flatMap( (e) -> Observable.fromIterable(e.getAnnotationMirrors()) ) 
            .doOnNext((m) -> info( "Mirror [%s]", m.toString() ))
            .flatMap( (am) -> Observable.fromIterable(am.getElementValues().entrySet() ))
            .filter( (entry) -> "declare".equals(String.valueOf(entry.getKey().getSimpleName())) )
            .flatMap( (entry) -> {
                final AnnotationValue av =  entry.getValue();
                return Observable.fromIterable((List<? extends AnnotationValue>)av.getValue());
            })
            .map( (av) -> av.getValue() )
            .doOnNext((av) -> info( "AnnotationValue [%s]",av) )
            .ofType(DeclaredType.class)
            .doOnNext((dt) -> info( "DeclaredType [%s]",dt) )
            .onExceptionResumeNext(Observable.empty())
             .map((DeclaredType dt) -> {
                try {
                    return Class.forName(dt.toString());
                } catch (ClassNotFoundException e1) {
                    error( "class not found [%s]",dt );
                    throw new RuntimeException(String.format("class not found [%s]",dt), e1); 
                }
            });

            return result;
    }    
    
}