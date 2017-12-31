package org.bsc.processor;

import static java.lang.String.format;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface TypescriptHelper {
	
	
	/**
	 *
	 * @param type
	 * @return
	 */
	static BeanInfo getBeanInfo(final Class<?> type) {
		try {
			return java.beans.Introspector.getBeanInfo(type);
		} catch (IntrospectionException e) {
			throw new Error(e);
		}
	}
	
    /**
     * 
     * @param m
     * @return
     */
    static boolean isStaticMethod( Method m ) {
        final int modifier = m.getModifiers();
        
        return (Modifier.isStatic( modifier) && 
        			Modifier.isPublic( modifier )) ;
    }
    
    /**
     * 
     * @param m
     * @return
     */
    static boolean isFactoryMethod( Method m ) {

    		return (isStaticMethod(m) && 
        			m.getReturnType().equals(m.getDeclaringClass()));
    }
	
	/**
	 * 
	 * @param pd
	 * @return
	 */
    static boolean isPropertyValid( PropertyDescriptor pd ) {
        return !( "class".equalsIgnoreCase(pd.getName()) );
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
        final StringBuilder inherited = new StringBuilder();
        
        if( type.isInterface() ) {
            statement.append( "interface ");      
        }
        else {
            
        		if( type.isEnum() ) statement.append( "/* enum */" );
            statement.append( "class ");
                      
            final Class<?> superclass = type.getSuperclass();

            if( superclass!=null ) {
            		inherited
                		.append( " extends ")
                		.append( getName(superclass, type, true) )      
                        ;
            }
        }
        
        final Class<?>[] interfaces = type.getInterfaces();
        
        if(interfaces.length > 0 ) {
        	          
        		final String ifc = Arrays.stream(interfaces)
								.map( (c) -> getName(c,type, true) )
								.collect( Collectors.joining(", "))
								;
        		inherited
        			.append( getSimpleName(type) )
        			.append( (type.isInterface()) ? " extends " : " implements ")
        			.append(	 ifc )
        			;
        			 
       
        }

        statement.append( getSimpleName(type) );
        
        if( inherited.length()>0 ) {
        		
        		statement.append("/*")
        					.append( inherited )
        					.append("*/");
        }
        
        return statement.append( " {")
        					.toString();
        
    }
	
    /**
     * 
     * @param type_parameters_list
     * @return
     */
    static String getClassParametersDecl( java.util.List<String> type_parameters_list ) {
        
        if( type_parameters_list.isEmpty() ) return "";
        
        return 	format("<%s>", type_parameters_list
        								.stream()
        								.collect( Collectors.joining(", ")) );
    }

    /**
     * 
     * @param type
     * @return
     */
    static String getClassParametersDecl( Class<?> type ) {   
    	
       return getClassParametersDecl( 
    		   Arrays.stream(type.getTypeParameters())
    		   	.map( (tp) -> tp.getName() )
    		   	.collect(Collectors.toList())
    		   );
    }
     
    /**
     * 
     * @param type
     * @return
     */
    static String getSimpleName( Class<?> type ) {
        return type.getSimpleName().concat(getClassParametersDecl(type));
    }

    /**
     * 
     * @param type
     * @return
     */
    static String getName( Class<?> type ) {
        return type.getName().concat(getClassParametersDecl(type));
    }

    /**
     * 
     * @param type
     * @param declaringClass
     * @return
     * @throws ClassNotFoundException
     */
    static String getName( Type type, Class<?> declaringClass, boolean packageResolution ) throws ClassNotFoundException {
    	
		final Class<?> clazz = Class.forName(type.getTypeName());
		
		return getName( clazz, declaringClass, packageResolution );
    	
    }
    
    /**
     * 
     * @param type
     * @param declaringClass
     * @return
     */
    static String getName( Class<?> type, Class<?> declaringClass, boolean packageResolution ) {
        
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
   
	final Package currentNS = (packageResolution) ? declaringClass.getPackage() : null;

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
     * @param packageResolution
     * @return
     */
    static String convertJavaToTS(	Class<?> type, 
    									Class<?> declaringClass, 
    									java.util.Map<String, Class<?>> declaredClassMap, 
    									boolean packageResolution ) 
    {
		
    		if( type == null ) return "any";
    	
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
                return getName(type, declaringClass, packageResolution);
        }
        
        return format("any /* %s */",type.getName());
		
    }

}
