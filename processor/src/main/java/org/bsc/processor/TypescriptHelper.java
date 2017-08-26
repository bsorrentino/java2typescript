package org.bsc.processor;

import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;
import static java.lang.String.format;

public interface TypescriptHelper {
	
    static boolean isPropertyValid( PropertyDescriptor pd ) {
        return !( "class".equalsIgnoreCase(pd.getName()) );
    }
    
    static boolean isSuperclassValid( Class<?> type ) {
        
        return	type != null 
        		&& type!=Object.class
        		;
    }
    
    /**
     * 
     * @param type
     * @param declaredClassMap
     * @param isSuperclassValid
     * @return
     */
    static String getClassDecl( Class<?> type, 
    							java.util.Map<String, 
    							Class<?>> declaredClassMap ) 
    {
     
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
        
        final Supplier<Stream<Class<?>>> s = () -> 
        		Arrays.asList(inherited)
        		.stream()
        		//.filter( TypescriptHelper::isInterfaceValid )
        		;
        
        if( s.get().count() > 0 ) {
            sb.append( (type.isInterface()) ? " extends " : " implements ");
            s.get().forEach( (c) -> sb.append( getName(c,type) ).append(","));
            sb.deleteCharAt( sb.length()-1 );
        }

        
        return sb.append( " {").toString();
        
    }
	
    static String getClassParametersDecl( final TypeVariable<? extends Class<?>> parameters[] ) {
        
        if( parameters.length == 0  ) return "";
        
        final StringBuilder decl = new StringBuilder( 

                Arrays.stream(parameters)
                        .map( (t) -> t.getName() )
                        .reduce( "<", (a, b) -> {
                        	
                        		return new StringBuilder(a)
                        					.append(b)
                        					.append(',')
                        					.toString();
                        	}
                        )   

        );
        decl.deleteCharAt( decl.length()-1 ).append('>');


       return decl.toString();
    }

    static String getClassParametersDecl( Class<?> type ) {        
       return getClassParametersDecl(type.getTypeParameters());
    }
     
    static String getSimpleName( Class<?> type ) {
        return type.getSimpleName().concat(getClassParametersDecl(type));
    }

    static String getName( Class<?> type ) {
        return type.getName().concat(getClassParametersDecl(type));
    }

    static String getName( Type type, Class<?> declaringClass ) throws ClassNotFoundException {
    	
		final Class<?> clazz = Class.forName(type.getTypeName());
		
		return getName( clazz, declaringClass );
    	
    }
    
    static String getName( Class<?> type, Class<?> declaringClass ) {
        final Package currentNS = declaringClass.getPackage();
        
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

    
    /**
     * 
     * @param type
     * @param declaringClass
     * @param declaredClassMap
     * @return
     */
    static String convertJavaToTS( Class<?> type, Class<?> declaringClass, java.util.Map<String, Class<?>> declaredClassMap ) {
		
        if( Void.class.isAssignableFrom(type) || type.equals(Void.TYPE) ) return "void";
        if( Boolean.class.isAssignableFrom(type) || type.equals(Boolean.TYPE) ) return type.isPrimitive() ? "boolean" : "boolean|null" ;
        if( Integer.class.isAssignableFrom(type) || type.equals(Integer.TYPE)) return type.isPrimitive() ? "int"  : "int|null" ;
        if( Long.class.isAssignableFrom(type) || type.equals(Long.TYPE)) return type.isPrimitive() ? "long"  : "long|null" ;
        if( Float.class.isAssignableFrom(type) || type.equals(Float.TYPE)) return type.isPrimitive() ? "float"  : "float|null" ;
        if( Double.class.isAssignableFrom(type) || type.equals(Double.TYPE)) return type.isPrimitive() ? "double"  : "double|null" ;
        if( Integer.class.isAssignableFrom(type) || type.equals(Integer.TYPE)) return type.isPrimitive() ? "int"  : "int|null" ;
        if( String.class.isAssignableFrom(type) ) return "string";
        
        if( char[].class.equals(type) ) return "chararray";
        if( byte[].class.equals(type) ) return "bytearray";
        
        if( type.isArray()) return format("[any] /* %s */",type.getName());
        
        if( declaredClassMap.containsKey(type.getName()) ) {
                return getName(type,declaringClass);
        }
        
        return format("any /* %s */",type.getName());
		
    }

}
