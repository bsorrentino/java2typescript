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
import java.util.Objects;
import java.util.Optional;
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
	private static BiFunction<Class<?>,Type, Boolean> typeParameterMatch = (declaringClass, type) ->	 	
		( type instanceof TypeVariable ) ?
				Arrays.stream(declaringClass.getTypeParameters())
					.map( (tp) -> tp.getName())
					.anyMatch( name -> name.equals(((TypeVariable<?>)type).getName())) :
				false
					;

	private final static void log( String fmt, Object ...args ) {
		System.out.println( format( fmt, (Object[])args));
	}

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
     * @param declaringMethod
     * @param declaredClassMap
     * @param packageResolution
     * @param typeMatch
     * @param onTypeMismatch
     * @return
     */
	public static String convertJavaToTS(	Type type, 
											Method declaringMethod, 
											Class<?> declaringClass,	
											java.util.Map<String, Class<?>> declaredClassMap,
											boolean packageResolution,
											Optional<Consumer<TypeVariable<?>>> onTypeMismatch)  
	{
		Objects.requireNonNull(type, "Type argument is null!");
		Objects.requireNonNull(declaringMethod, "declaringMethod argument is null!");
		Objects.requireNonNull(declaringClass, "declaringClass argument is null!");
		Objects.requireNonNull(declaredClassMap, "declaredClassMap argument is null!");
		
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
					
					final String typeName = convertJavaToTS( t, 
															declaringMethod, 
															declaringClass,
															declaredClassMap, 
															packageResolution,
															onTypeMismatch);
					log( "Parameterized Type %s - %s",  t, typeName );	
					result = result.replace( t.getTypeName(), typeName);
								
				}
				else if(  t instanceof TypeVariable ) {

					log( "type variable: %s",  t );	
					
					final TypeVariable<?> tv = (TypeVariable<?>)t;
					
					if( isStaticMethod(declaringMethod) || !typeParameterMatch.apply(declaringClass, tv )) {

						if( onTypeMismatch.isPresent() ) {
							 onTypeMismatch.get().accept(tv);
						 }
						 else {
							final String name = tv.getName();
							result = result.replaceAll( "<"+name, "<any")
		                                .replaceAll( ",\\s"+name, ", any")
		                                .replaceAll( name +">", "any>")
		                                ;	 
						 }
					}
					continue;
				}
				else if( t instanceof Class ) {
					log( "class: %s",  t.getTypeName() );	
					
					final String name = convertJavaToTS( (Class<?>)t, declaringClass, declaredClassMap, packageResolution);
					
					result = result.replace(t.getTypeName(), name);
				}
				else if( t instanceof WildcardType ) {
					final WildcardType wt = (WildcardType) t;
					
					final Type[] lb = wt.getLowerBounds();
					final Type[] ub = wt.getUpperBounds();
					
					log( "Wildcard Type : %s lb:%d up:%d",  type.getTypeName(), lb.length, ub.length );	
					
					if( lb.length <= 1 && ub.length==1) {
						final Type tt  = (lb.length==1) ? lb[0] : ub[0];
						
						result = result.replace( wt.getTypeName(), convertJavaToTS( tt, 
																					declaringMethod, 
																					declaringClass, 
																					declaredClassMap, 
																					packageResolution,
																					onTypeMismatch));
					}
					else {
						result = result.replace(wt.getTypeName(), format( "any/*%s*/", wt));						
					}
				}
				else if( t instanceof GenericArrayType ) {
					throw new IllegalArgumentException( format("type argument <%s> 'GenericArrayType' is a  not supported yet!", t));					
				}
				
			}
			
			return result;
		}
		else if(  type instanceof TypeVariable ) {
			log( "class: %s",  type.getTypeName() );	

			final TypeVariable<?> tv = (TypeVariable<?>)type;
			
			if( isStaticMethod(declaringMethod) || !typeParameterMatch.apply(declaringClass, tv )) {

				final String name = tv.getName();						

				if( onTypeMismatch.isPresent() ) {
					 onTypeMismatch.get().accept(tv);
					 return name;
				}
				
				return format("any/*%s*/", name);
			}

			return type.getTypeName();			
		}
		else if( type instanceof Class ) {
			log( "class: %s",  type.getTypeName() );	

			final String name = convertJavaToTS( (Class<?>)type, declaringClass, declaredClassMap, packageResolution);
			return name;
		}		
		else if( type instanceof WildcardType ) {
			throw new IllegalArgumentException( "type 'WildcardType' is a  not supported yet!");		
		}
		else if( type instanceof GenericArrayType ) {
			final GenericArrayType t = (GenericArrayType)type;
			
			log( "generic array type: %s",  t.getGenericComponentType().getTypeName() );	
			//throw new IllegalArgumentException( format("type <%s> 'GenericArrayType' is a  not supported yet!", type));	
			
			return ( typeParameterMatch.apply(declaringClass, t.getGenericComponentType() ))  ? 
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
        
        if( type.isArray()) {
        		//return format("[any] /* %s */",type.getName());        		
        		return format( "[%s]", convertJavaToTS(type.getComponentType(), declaringClass, declaredClassMap,packageResolution));
        }
        
        if( declaredClassMap.containsKey(type.getName()) ) {
        		return getTypeName(type, declaringClass, packageResolution);
        }
        
        return format("any /*%s*/",type.getName());
		
    }

}
