package org.bsc.processor;

import static java.lang.String.format;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     
        final StringBuilder statement = new StringBuilder();
        
        if( type.isInterface() ) {

            statement.append( "interface ")
                .append( getSimpleName(type) )
                .append( "/*") // COMMENT INHERITED
                ;
        
        }
        else {
            
            statement.append( "class ")
                .append( getSimpleName(type) )
                .append( "/*") // COMMENT INHERITED
                ;
            
            
            final Class<?> inherited = type.getSuperclass();

            if( isSuperclassValid(inherited) ) {
                statement.append( " extends ")
                  .append( getName(inherited, type) )      
                        ;
            }
        }
        
        final Class<?>[] inherited = type.getInterfaces();
        
        if(inherited.length > 0 ) {
        	          
        		statement.append( (type.isInterface()) ? " extends " : " implements ");
            
        		Arrays.stream(inherited).forEach( (c) -> statement.append( getName(c,type) ).append(","));
        
            statement.deleteCharAt( statement.length()-1 );
        }

        
        return statement.append("*/").append( " {").toString();
        
    }
	
    static String getClassParametersDecl( java.util.List<String> type_parameters_list ) {
        
        if( type_parameters_list.isEmpty() ) return "";
        
        final StringBuilder decl = new StringBuilder( 

        		type_parameters_list.stream()
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
    	
       return getClassParametersDecl( 
    		   Arrays.stream(type.getTypeParameters())
    		   	.map( (tp) -> tp.getName() )
    		   	.collect(Collectors.toList())
    		   );
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
        
        final java.util.List<String> dc_parameters_list = 
        		Arrays.stream(declaringClass.getTypeParameters())
        			.map( (tp) -> tp.getName())
        			.collect(Collectors.toList());
        
       final java.util.List<String> type_parameters_list = 
    		   Arrays.stream(type.getTypeParameters())
        				.map( (tp) -> (dc_parameters_list.contains(tp.getName()) ) ? tp.getName() : "any" )
        				.collect(Collectors.toList());
       
       final java.util.List<String>  parameters = 
    		   dc_parameters_list.size() == type_parameters_list.size() ? dc_parameters_list : type_parameters_list ;
       
       boolean isFunctionaInterface = ( type.isInterface() && type.isAnnotationPresent(FunctionalInterface.class));
       
        return new StringBuilder()
                .append( 
	                type.getPackage().equals(currentNS) || isFunctionaInterface  ? 
	                    type.getSimpleName() : 
	                    type.getName()
	                 )
                .append( getClassParametersDecl(parameters) )
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
