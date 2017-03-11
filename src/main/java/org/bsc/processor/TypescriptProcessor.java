package org.bsc.processor;

import io.reactivex.Observable;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
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
    
    private boolean isPropertyValid( PropertyDescriptor pd ) {
        return !( "class".equalsIgnoreCase(pd.getName()) );
    }
    
    private boolean isInterfaceValid( Class<?> type ) {
        
        return  type != null
                && type!=Serializable.class
                && type!=Closeable.class
                && type!=AutoCloseable.class
                && type!=Comparable.class
                ;
    }
    private boolean isSuperclassValid( Class<?> type ) {
        
        return type != null && type!=Object.class;
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
        
        final String tsType = convertJavaToTS(  
                                                returnType,
                                                m.getDeclaringClass(),
                                                declaredClassMap);
    
        return sb.append(" ):")
          .append(tsType)
          .toString();
        
    }
    
    private String getPropertyDecl( Class<?> declaringClass, PropertyDescriptor pd, java.util.Map<String, Class<?>> declaredClassMap ) {

        final StringBuilder sb = new StringBuilder();
        
        sb.append(pd.getName());
        if( declaringClass.isInterface()) sb.append('?');
        
        final String tsType = convertJavaToTS( pd.getPropertyType(), 
                                                declaringClass, 
                                                declaredClassMap);
        
        return sb.append(':')
                      .append(tsType)
                      .toString();

    }

    
    private String getClassParametersDecl( final TypeVariable<? extends Class<?>> parameters[] ) {
        
        if( parameters.length == 0  ) return "";
        
        final StringBuilder decl = new StringBuilder( 

                Arrays.stream(parameters)
                        .map( (t) -> t.getName() )
                        .reduce( "<", (a, b) -> new StringBuilder(a).append(b).append(',').toString()  )   

        );
        decl.deleteCharAt( decl.length()-1 ).append('>');


       return decl.toString();
    }

    private String getClassParametersDecl( Class<?> type ) {        
       return getClassParametersDecl(type.getTypeParameters());
    }
    
    
    private String getSimpleName( Class<?> type ) {
        return type.getSimpleName().concat(getClassParametersDecl(type));
    }

    private String getName( Class<?> type ) {
        return type.getName().concat(getClassParametersDecl(type));
    }
    
    private String getName( Class<?> type, Class<?> declaringClass ) {
        Package currentNS = declaringClass.getPackage();
        
        final TypeVariable<? extends Class<?>>[] dc = declaringClass.getTypeParameters();        
 
        final TypeVariable<? extends Class<?>> type_parameters[] = type.getTypeParameters();        
        
        return new StringBuilder()
                .append( 
                type.getPackage().equals(currentNS) ? 
                    type.getSimpleName() : 
                    type.getName()
                 )
                .append(getClassParametersDecl((dc.length == type_parameters.length) ? 
                        dc :
                        type_parameters
                    ))
                .toString();
    }
    
    
    private String getClassDecl( Class<?> type, java.util.Map<String, Class<?>>  declaredClassMap ) {
     
        final StringBuilder sb = new StringBuilder();
        
        if( type.isInterface() ) {

            sb.append( "interface ")
                .append( getSimpleName(type) )
                ;
        
        }
        else {
            
            sb.append( "class ")
                .append( getSimpleName(type) )
                ;
            final Class<?> inherited = type.getSuperclass();

            if( isSuperclassValid(inherited) ) {
                sb.append( " extends ")
                  .append( getName(inherited, type) )      
                        ;
            }
        }
        
        final Class<?>[] inherited = type.getInterfaces();

        if( inherited.length > 0 ) {
            sb.append( (type.isInterface()) ? " extends " : " implements ");
            Arrays.asList(inherited)
                    .stream()
                    .filter( this::isInterfaceValid )
                    .forEach( (c) -> sb.append( getName(c,type) ).append(","));
            sb.deleteCharAt( sb.length()-1 );
        }

        
        return sb.append( " {").toString();
        
    }

    private String processClass(  BeanInfo bi, java.util.Map<String, Class<?>> declaredClassMap )   {
        
        final Class<?> type = bi.getBeanDescriptor().getBeanClass();
        
        final StringBuilder sb = new StringBuilder();
        
        final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        final java.util.Set<String> propertySet = 
                new java.util.LinkedHashSet<>(pds.length);
        
        Arrays.asList(pds)
            .stream()
            .filter( this::isPropertyValid )
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
    
    
    private String convertJavaToTS( Class<?> type, Class<?> declaringClass, java.util.Map<String, Class<?>> declaredClassMap ) {
		
        //info( "java type [%s]", type );
        if( Void.class.isAssignableFrom(type) || type.equals(Void.TYPE) ) return "void";
        if( Boolean.class.isAssignableFrom(type) || type.equals(Boolean.TYPE) ) return type.isPrimitive() ? "boolean" : "boolean" ;
        if( Integer.class.isAssignableFrom(type) || type.equals(Integer.TYPE)) return type.isPrimitive() ? "number"  : "number" ;
        if( String.class.isAssignableFrom(type) ) return "string";
        
        if( declaredClassMap.containsKey(type.getName()) ) {
                return getName(type,declaringClass);
        }
        
        return "any";
		
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