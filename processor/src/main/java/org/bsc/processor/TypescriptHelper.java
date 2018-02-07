package org.bsc.processor;

import static java.lang.String.format;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TypescriptHelper {
	
    /**
     * 
     */
    public static BiPredicate<Class<?>, Class<?>> isPackageMatch = (a, b ) -> 
		a.getPackage().equals(b.getPackage()) ;
    
	/**
	 * 
	 */
	public static Predicate<Class<?>> isFunctionalInterface = type ->
		type.isInterface() && type.isAnnotationPresent(FunctionalInterface.class);
		
	/**
	 * 
	 */
	public static BiFunction<Class<?>,TypeVariable<?>, Boolean> typeParameterMatch = (declaringClass, typeParameter) -> 		 
		Arrays.stream(declaringClass.getTypeParameters())
					.map( (tp) -> tp.getName())
					.anyMatch( name -> name.equals(typeParameter.getName()))
					;

	private final static Consumer<String> log = msg -> System.out.println(msg);

	/**
	 * 
	 * @param p
	 * @return
	 */
	public static final String getParameterName( Parameter p ) {
		final String name = p.getName();
		
		switch( name ) {	
		case "function":
			return "func";
		default:
			return name;
		}
	}
	
	
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
                		.append( getTypeName(superclass, type, true) )      
                        ;
            }
        }
        
        final Class<?>[] interfaces = type.getInterfaces();
        
        if(interfaces.length > 0 ) {
        	          
        		final String ifc = Arrays.stream(interfaces)
								.map( (c) -> getTypeName(c,type, true) )
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
    static String getTypeName( Class<?> type ) {
        return type.getName().concat(getClassParametersDecl(type));
    }

    /**
     * 
     * @param type
     * @param declaringClass
     * @return
     * @throws ClassNotFoundException
     */
    static String getTypeName( Type type, Class<?> declaringClass, boolean packageResolution ) throws ClassNotFoundException {
    	
		final Class<?> clazz = Class.forName(type.getTypeName());
		
		return getTypeName( clazz, declaringClass, packageResolution );
    	
    }
    
    /**
     * 
     * @param type
     * @param declaringClass
     * @return
     */
    static String getTypeName( Class<?> type, Class<?> declaringClass, boolean packageResolution ) {
        
		final java.util.List<String> dc_parameters_list = 
				Arrays.stream(declaringClass.getTypeParameters())
					.map( (tp) -> tp.getName())
					.collect(Collectors.toList());
	    
		final java.util.List<String> type_parameters_list = 
				   Arrays.stream(type.getTypeParameters())
		    				.map( tp -> (dc_parameters_list.contains(tp.getName()) ) ? tp.getName() : "any" )
		    				.collect(Collectors.toList());
	   
		final java.util.List<String>  parameters = 
				   dc_parameters_list.size() == type_parameters_list.size() ? dc_parameters_list : type_parameters_list ;
	   
		//boolean isFunctionalInterface = ( type.isInterface() && type.isAnnotationPresent(FunctionalInterface.class));
	   
		final Package currentNS = (packageResolution) ? declaringClass.getPackage() : null;
	
		return new StringBuilder()
	                .append( 
		                type.getPackage().equals(currentNS) || isFunctionalInterface.test(type)  ? 
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
     * @throws Exception
     */
	public static String convertJavaToTS(	Type type, 
											Class<?> declaringClass, 
											java.util.Map<String, Class<?>> declaredClassMap,
											boolean packageResolution)  
	{
		
    

		if( type instanceof ParameterizedType ) {

			final ParameterizedType pType = (ParameterizedType) type;
			
			final Class<?> rawType = (Class<?>)pType.getRawType();

			if( !declaredClassMap.containsKey(rawType.getName()) ) {
	    			return format("any /*%s*/",rawType.getName());
	        }
			
			String result = pType.getTypeName();
						
			if( isFunctionalInterface.test(rawType) || (packageResolution && isPackageMatch.test(rawType, declaringClass)) ) {					
				result = result.replace( rawType.getName(), rawType.getSimpleName());
			}
				
			final Type[] typeArgs = pType.getActualTypeArguments();
			
			for( Type t : typeArgs ) {
				if( t instanceof ParameterizedType ) {
					
					final String typeName = convertJavaToTS( t, declaringClass, declaredClassMap, packageResolution);
					log.accept(format( "Parameterized Type %s - %s",  t, typeName ));	
					result = result.replace( t.getTypeName(), typeName);
								
				}
				else if(  t instanceof TypeVariable ) {

					log.accept(format( "type variable: %s",  t ));	
					
					final TypeVariable<?> tv = (TypeVariable<?>)t;
					
					if( !typeParameterMatch.apply(declaringClass, tv )) {
						final String name = tv.getName();
						result = result.replaceAll( "<"+name, "<any")
	                                .replaceAll( ",\\s"+name, ", any")
	                                .replaceAll( name +">", "any>")
	                                ;
						
					}
					continue;
				}
				else if( t instanceof Class ) {
					log.accept(format( "class: %s",  t.getTypeName() ));	
					
					final String name = convertJavaToTS( (Class<?>)t, declaringClass, declaredClassMap, packageResolution);
					
					result = result.replace(t.getTypeName(), name);
				}
				else if( t instanceof WildcardType ) {
					//throw new IllegalArgumentException( format("type argument <%s> 'WildcardType' is a  not supported yet!", t));
					result = result.replace(t.getTypeName(), format( "any/*%s*/", t));
				}
				else if( t instanceof GenericArrayType ) {
					throw new IllegalArgumentException( format("type argument <%s> 'GenericArrayType' is a  not supported yet!", t));					
				}
				
			}
			
			return result;
		}
		else if(  type instanceof TypeVariable ) {
			log.accept(format( "class: %s",  type.getTypeName() ));	

			final TypeVariable<?> tv = (TypeVariable<?>)type;
			
			if( !typeParameterMatch.apply(declaringClass, tv )) {
				final String name = tv.getName();						
				return format("any/*%s*/", name);
			}

			return type.getTypeName();			
		}
		else if( type instanceof Class ) {
			log.accept(format( "class: %s",  type.getTypeName() ));	

			final String name = convertJavaToTS( (Class<?>)type, declaringClass, declaredClassMap, packageResolution);
			return name;
		}		
		else if( type instanceof WildcardType ) {
			throw new IllegalArgumentException( "type 'WildcardType' is a  not supported yet!");		
		}
		else if( type instanceof GenericArrayType ) {
			final GenericArrayType t = (GenericArrayType)type;
			log.accept(format( "generic array type: %s",  t.getGenericComponentType().getTypeName() ));	
			//throw new IllegalArgumentException( format("type <%s> 'GenericArrayType' is a  not supported yet!", type));	
			
			return ( typeParameterMatch.apply(declaringClass, (TypeVariable<?>) t.getGenericComponentType() ))  ? 
					format("[%s]", t.getGenericComponentType() ) :
					format("[any/*%s*/]", t.getGenericComponentType() );
		}
		
		throw new IllegalArgumentException( "type is a  not recognised type!");
	}

    /**
     * 
     * @param type
     * @param declaringClass
     * @param declaredClassMap
     * @param packageResolution
     * @return
     */
    private static String convertJavaToTS(	Class<?> type, 
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
        		return getTypeName(type, declaringClass, packageResolution);
        }
        
        return format("any /*%s*/",type.getName());
		
    }

}
