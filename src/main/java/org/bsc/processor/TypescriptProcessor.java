package org.bsc.processor;

import static org.bsc.processor.TypescriptHelper.convertJavaToTS;
import static org.bsc.processor.TypescriptHelper.getClassDecl;
import static org.bsc.processor.TypescriptHelper.getName;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.tools.FileObject;

import io.reactivex.Observable;
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
    
    /**
     *
     * @param processingContext
     * @return
     */
    @Override
    public boolean process( Context processingContext ) throws Exception {
            
        final String targetFile = processingContext.getOptionMap().getOrDefault("ts.outfile", "out.d.ts");
        
        final FileObject out = super.getSourceOutputFile( Paths.get("ts"), Paths.get(targetFile));
        
        info( "output [%s]", out.getName() );
        
        final java.io.Writer w = out.openWriter();
        
        rxEnumerateDeclaredPackageAndClass( processingContext )
                .toMap( (clazz) -> clazz.getName() )
                .flatMapObservable( (declaredClasses) -> 
                    Observable.fromIterable(declaredClasses.values())
                            .map( (clazz) -> processClass( java.beans.Introspector.getBeanInfo(clazz), declaredClasses )))
                .doOnComplete( () -> w.close() )
                .subscribe( ( s ) -> w.append( s ) )
                
                ;
        return true;
    }
    
    private String getPropertyDecl( Class<?> declaringClass, PropertyDescriptor pd, java.util.Map<String, Class<?>> declaredClassMap ) {

        final StringBuilder sb = new StringBuilder();
        
        sb.append(pd.getName());
        if( declaringClass.isInterface()) sb.append('?');

        sb.append(':');
        
    	final Type rType =  pd.getReadMethod().getGenericReturnType();
    	if( rType instanceof ParameterizedType ) {

    		final Type pClass =  ((ParameterizedType)rType).getActualTypeArguments()[0];
    		
    		try {
        		
				final String name = getName( pClass, pd.getPropertyType());
				
				final String r = rType.getTypeName().replaceAll(pClass.getTypeName(), name);

				info( "[%s] [%s] [%s] [%s]", pd.getName(), pClass.getTypeName(), rType.getTypeName(), r);
				
				return sb.append( r )
						.toString();
				
			} catch (ClassNotFoundException e) {
				
				warn( "type [%s] not found!", pClass.getTypeName());
			}
    			
    	}

        final String tsType = convertJavaToTS( pd.getPropertyType(), 
                                                declaringClass, 
                                                declaredClassMap);
       
        return sb.append(tsType)
                 .toString();

    }
    
    
    private String getMethodDecl( final Method m, java.util.Map<String, Class<?>> declaredClassMap ) {
        final Class<?> returnType = m.getReturnType();
        
        final StringBuilder sb = new StringBuilder();
        
        if( Modifier.isStatic(m.getModifiers()) ) sb.append("static ");
        
        sb.append(m.getName());
        if( m.getDeclaringClass().isInterface()) sb.append('?');
        sb.append("( ");
        
        final Parameter[] params = m.getParameters();
        
        if( params.length > 0 ) {
            Arrays.asList(params)
                    .forEach( (tp) -> sb.append( tp.getName())
                                        .append(':')
                                        .append( convertJavaToTS(tp.getType(),m.getDeclaringClass(),declaredClassMap) )
                                        .append(",")
                            );
            sb.deleteCharAt( sb.length()-1 );
        }
        
        sb.append(" ):");
        
    	// Check if there is a paramized type
    	final Type rType =  m.getGenericReturnType();
    	if( rType instanceof ParameterizedType ) {

    		final Type pClass =  ((ParameterizedType)rType).getActualTypeArguments()[0];
    		
    		try {
        		
				final String name = getName( pClass, m.getDeclaringClass());
				
				final String r = rType.getTypeName().replaceAll(pClass.getTypeName(), name);

				info( "[%s] [%s] [%s] [%s]", m.getName(), pClass.getTypeName(), rType.getTypeName(), r);
				
				return sb.append( r )
						.toString();
				
			} catch (ClassNotFoundException e) {
				
				warn( "type [%s] not found!", pClass.getTypeName());
			}
    			
    	}

    	final String tsType = convertJavaToTS(  returnType,
                m.getDeclaringClass(),
                declaredClassMap);

        
    	return  sb.append(tsType)
    			.toString();
        
    }
    

    private String processClass(  BeanInfo bi, java.util.Map<String, Class<?>> declaredClassMap )   {
        
        final Class<?> type = bi.getBeanDescriptor().getBeanClass();
        
        final StringBuilder sb = new StringBuilder();
        
        final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        final java.util.Set<String> propertySet = 
                new java.util.LinkedHashSet<>(pds.length);
        
        Arrays.asList(pds)
            .stream()
            .filter( TypescriptHelper::isPropertyValid )
            .forEach( (pd) -> propertySet.add( getPropertyDecl( type, pd, declaredClassMap) ) );

        final Method[] methods = type.getDeclaredMethods();
        final java.util.Set<String> methodSet = 
                new java.util.LinkedHashSet<>(methods.length);

        Arrays.asList(methods)
        .stream()
        .filter( (md) -> { // Remove setter and getter
            return !Arrays.asList(pds).stream().anyMatch( (pd) -> {
                    final Method rm = pd.getReadMethod();
                    final Method wm = pd.getWriteMethod();
                    return (md.equals(rm) || md.equals(wm));
                });
        })
        .forEach((md) -> methodSet.add( getMethodDecl(md, declaredClassMap) ) );
       
        sb.append( "declare namespace " )
           .append(type.getPackage().getName())
           .append(" {\n\n")
            ;
        
        sb.append( getClassDecl(type, declaredClassMap) )
          .append("\n\n");
        

        propertySet.stream().sorted().forEach((decl) -> {
            sb.append( '\t' )
              .append(decl)
              .append(  ENDL );

        });
        methodSet.stream().sorted().forEach( (decl) -> {
            sb.append( '\t' )
              .append(decl)
              .append(  ENDL );

        });
        
        sb.append("\n}\n")
            .append("\n}\n");
        
        /*        
        Arrays.asList(t.$0.getMethodDescriptors()).stream().forEach( (md) -> {

            final Class<?> returnType = md.getMethod().getReturnType();
            info( String.format(  "\t%s", getMethodDecl(md.getMethod(), t.$1)) );

        });
        */
        return sb.toString();
           
    }
    
    
   

    private Observable<Class<?>> rxEnumerateDeclaredPackageAndClass( final Context processingContext ) {
           
		final  Observable<Class<?>> result = processingContext.rxElementFromAnnotations()
            .doOnNext((e) -> info( "Anotation [%s]", e.getKind().name()) )
            .filter( (e) -> ElementKind.PACKAGE==e.getKind() || ElementKind.CLASS==e.getKind() )
            .concatMap( (e) -> Observable.fromIterable(e.getAnnotationMirrors()) ) 
            .doOnNext((m) -> info( "Mirror [%s]", m.toString() ))
            .concatMap( (am) -> Observable.fromIterable(am.getElementValues().entrySet() ))
            .filter( (entry) -> "declare".equals(String.valueOf(entry.getKey().getSimpleName())) )
            .flatMap( (entry) -> {
                final AnnotationValue av =  entry.getValue();
                return Observable.fromIterable((List<? extends AnnotationValue>)av.getValue());
            })
            .map( (av) -> av.getValue() )
            //.doOnNext((av) -> info( "AnnotationValue [%s]",av) )
            .ofType(DeclaredType.class)
            //.doOnNext((dt) -> info( "DeclaredType [%s]",dt) )
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