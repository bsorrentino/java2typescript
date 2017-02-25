package org.bsc.processor;

import io.reactivex.Observable;
import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import org.kohsuke.MetaInfServices;

/**
 * 
 * @author bsoorentino
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.bsc.processor.*")
//@SupportedOptions({ "TS.outdir" })
@MetaInfServices(Processor.class)
public class TypescriptProcessor extends AbstractProcessorEx {

    
    /**
     *
     * @param processingContext
     * @return
     */
    @Override
    public boolean process( Context processingContext ) {
            
        rxEnumerateDeclaredPackageAndClass( processingContext )
                .toMap( (clazz) -> clazz.getName() )
                .flatMapObservable( (declaredClasses) -> 
                    Observable.fromIterable(declaredClasses.values())
                            .map( (clazz) -> new Tuple2( java.beans.Introspector.getBeanInfo(clazz), declaredClasses )))
                .doOnNext( this::processClass )
                .subscribe( (clazz) -> info( "class [%s]", clazz ) )
                ;
        return true;
    }
    
    private void processClass(  final Tuple2<BeanInfo, java.util.Map<String, Class<?>>> t )   {
        
        final Class<?> type = t.$0.getBeanDescriptor().getBeanClass();
        
        info( "class %s", type.getName());
        
        info( String.format( "properties:") );
        Arrays.asList(t.$0.getPropertyDescriptors()).stream().forEach( (pd) -> {
            
            info( String.format(  "\t%s:%s", pd.getName(), convertJavaToTS(pd.getPropertyType(), t.$1)));
        });

        info( String.format( "methods:"));
        Arrays.asList(type.getDeclaredMethods()).stream().forEach( (md) -> {
 
            final Class<?> returnType = md.getReturnType();
            info( String.format(  "\t%s():%s", md.getName(), convertJavaToTS(returnType, t.$1)));
           
        });
                
        /*        
        Arrays.asList(t.$0.getMethodDescriptors()).stream().forEach( (md) -> {

            final Class<?> returnType = md.getMethod().getReturnType();
            info( String.format(  "\t%s():%s", md.getName(), convertJavaToTS(returnType, t.$1)));

        });
        */
    }
    
    
    private String convertJavaToTS( Class<?> type, java.util.Map<String, Class<?>> declaredClassMap ) {
		
        //info( "java type [%s]", type );
        if( type==Void.class || type.equals(Void.TYPE) ) return "void";
        if( type.isAssignableFrom(Boolean.class) || type.equals(Boolean.TYPE) ) return type.isPrimitive() ? "boolean" : "boolean" ;
        if( type.isAssignableFrom(Integer.class) || type.equals(Integer.TYPE)) return type.isPrimitive() ? "number"  : "number" ;
        if( type.isAssignableFrom(String.class) ) return "string";
        
        if( declaredClassMap.containsKey(type.getName()) ) {
                return type.getSimpleName();
        }
        
        return "any";
		
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