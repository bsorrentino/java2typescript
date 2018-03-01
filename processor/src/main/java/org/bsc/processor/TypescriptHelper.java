package org.bsc.processor;

import static java.lang.String.format;

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
    public static BiPredicate<Class<?>, Class<?>> isPackageMatch = (a, b) ->
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
		//System.out.println( format( fmt, (Object[])args));
	}

	public static final String processFunctionalInterface( TSType type  ) {
		Objects.requireNonNull(type, "argument 'type' is not defined!");

		return null;
	}

	/**
	 *
	 * @param type
	 * @param alias
	 * @return
	 */
	public static final String getAliasDeclaration( Class<?> type, String alias  ) {
		Objects.requireNonNull(type, "argument 'type' is not defined!");
		Objects.requireNonNull(alias, "argument 'alias' is not defined!");

		final TypeVariable<?>[] typeParameters = type.getTypeParameters();

		if( typeParameters!=null && typeParameters.length > 0 ) {

			final String pp = Arrays.stream(typeParameters).map( tp -> tp.getName() ).collect( Collectors.joining(",","<", ">"));
			return format( "type %s%s = %s%s;\n\n", alias, pp, type.getName(), pp );

		}

		return format( "type %s = %s;\n\n", alias, type.getName() );
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
    static String getClassDecl( TSType tstype,
    							java.util.Map<String, TSType> declaredClassMap )
    {

        final StringBuilder statement = new StringBuilder();
        final StringBuilder inherited = new StringBuilder();

        if( tstype.getValue().isInterface() ) {
            statement.append( "interface ");
        }
        else {

        		if( tstype.getValue().isEnum() ) statement.append( "/* enum */" );
            statement.append( "class ");

            final TSType superclass = TSType.from(tstype.getValue().getSuperclass());

            if( superclass!=null ) {
            		inherited
                		.append( " extends ")
                		.append( getTypeName(superclass, tstype, true) )
                        ;
            }
        }

        final Class<?>[] interfaces = tstype.getValue().getInterfaces();

        if(interfaces.length > 0 ) {

        		final String ifc = Arrays.stream(interfaces)
        							.map( c -> TSType.from(c) )
								.map( t -> getTypeName(t, tstype, true) )
								.collect( Collectors.joining(", "))
								;
        		inherited
        			.append( (tstype.getValue().isInterface()) ? " extends " : " implements ")
        			.append(	 ifc )
        			;


        }

        statement.append( getTypeName(tstype, tstype, true) );

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
     * @param declaringType
     * @return
     */
    static String getTypeName( TSType type, TSType declaringType, boolean packageResolution ) {

		final java.util.List<String> dc_parameters_list =
				Arrays.stream(declaringType.getValue().getTypeParameters())
					.map( (tp) -> tp.getName())
					.collect(Collectors.toList());

		final java.util.List<String> type_parameters_list =
				   Arrays.stream(type.getValue().getTypeParameters())
		    				.map( tp -> (dc_parameters_list.contains(tp.getName()) ) ? tp.getName() : "any" )
		    				.collect(Collectors.toList());

		final java.util.List<String>  parameters =
				   dc_parameters_list.size() == type_parameters_list.size() ? dc_parameters_list : type_parameters_list ;

		final Package currentNS = (packageResolution) ? declaringType.getValue().getPackage() : null;

		return new StringBuilder()
	                .append(
		                type.getValue().getPackage().equals(currentNS) || isFunctionalInterface.test(type.getValue())  ?
		                    type.getSimpleTypeName() :
		                    type.getTypeName()
		                 )
	                .append( getClassParametersDecl(parameters) )
	                .toString();
    }

    /**
     *
     * @param type
     * @param declaringMethod
     * @param declaredTypeMap
     * @param packageResolution
     * @param typeMatch
     * @param onTypeMismatch
     * @return
     */
	public static String convertJavaToTS(	Type type,
											Method declaringMethod,
											TSType declaringType,
											java.util.Map<String, TSType> declaredTypeMap,
											boolean packageResolution,
											Optional<Consumer<TypeVariable<?>>> onTypeMismatch)
	{
		Objects.requireNonNull(type, "Type argument is null!");
		Objects.requireNonNull(declaringMethod, "declaringMethod argument is null!");
		Objects.requireNonNull(declaringType, "declaringType argument is null!");
		Objects.requireNonNull(declaredTypeMap, "declaredTypeMap argument is null!");

		if( type instanceof ParameterizedType ) {

			final ParameterizedType pType = (ParameterizedType) type;

			final Class<?> rawType = (Class<?>)pType.getRawType();

			final TSType tstype = declaredTypeMap.get(rawType.getName());
			if( tstype==null ) {
	    			return format("any /*%s*/",rawType.getName());
	        }

			String result = pType.getTypeName()
					.replace( rawType.getName(), tstype.getTypeName()) // use Alias
					;

			if( isFunctionalInterface.test(rawType) || (packageResolution && isPackageMatch.test(rawType, declaringType.getValue())) ) {
				result = result.replace( rawType.getName(), rawType.getSimpleName());
			}

			final Type[] typeArgs = pType.getActualTypeArguments();

			for( Type t : typeArgs ) {
				if( t instanceof ParameterizedType ) {

					final String typeName = convertJavaToTS( t,
															declaringMethod,
															declaringType,
															declaredTypeMap,
															packageResolution,
															onTypeMismatch);
					log( "Parameterized Type %s - %s",  t, typeName );
					result = result.replace( t.getTypeName(), typeName);

				}
				else if(  t instanceof TypeVariable ) {

					log( "type variable: %s",  t );

					final TypeVariable<?> tv = (TypeVariable<?>)t;

					if( isStaticMethod( declaringMethod ) || !typeParameterMatch.apply(declaringType.getValue(), tv )) {

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

					final String name = convertJavaToTS( (Class<?>)t, declaringType, declaredTypeMap, packageResolution);

					final String commented = format("/*%s*/", t.getTypeName());
					result = result.replace( commented, "/*@*/")
												.replace(t.getTypeName(), name)
												.replace( "/*@*/", commented )
												;
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
																					declaringType,
																					declaredTypeMap,
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

			if( isStaticMethod( declaringMethod ) || !typeParameterMatch.apply(declaringType.getValue(), tv )) {

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

			final String name = convertJavaToTS( (Class<?>)type, declaringType, declaredTypeMap, packageResolution);
			return name;
		}
		else if( type instanceof WildcardType ) {
			throw new IllegalArgumentException( "type 'WildcardType' is a  not supported yet!");
		}
		else if( type instanceof GenericArrayType ) {
			final GenericArrayType t = (GenericArrayType)type;

			log( "generic array type: %s",  t.getGenericComponentType().getTypeName() );
			//throw new IllegalArgumentException( format("type <%s> 'GenericArrayType' is a  not supported yet!", type));

			return ( typeParameterMatch.apply(declaringType.getValue(), t.getGenericComponentType() ))  ?
					format("[%s]", t.getGenericComponentType() ) :
					format("[any/*%s*/]", t.getGenericComponentType() );
		}

		throw new IllegalArgumentException( "type is a  not recognised type!");
	}

    /**
     *
     * @param type
     * @param declaringType
     * @param declaredTypeMap
     * @param packageResolution
     * @return
     */
    private static String convertJavaToTS(	Class<?> type,
    											TSType declaringType,
    											java.util.Map<String, TSType> declaredTypeMap,
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
        		return format( "[%s]", convertJavaToTS(type.getComponentType(), declaringType, declaredTypeMap,packageResolution));
        }

        final TSType tt = declaredTypeMap.get( type.getName() );
        if( tt!=null ) {
        		return getTypeName(tt, declaringType, packageResolution);
        }

        return format("any /*%s*/",type.getName());

    }

}
