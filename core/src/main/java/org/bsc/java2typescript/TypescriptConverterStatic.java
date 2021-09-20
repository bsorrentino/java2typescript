package org.bsc.java2typescript;

import static java.lang.String.format;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author bsorrentino
 *
 */
public abstract class TypescriptConverterStatic {
    
    static final String ENDL = ";\n";

    public static final List<TSType> PREDEFINED_TYPES = Arrays.asList(
            TSType.of(Class.class),
            TSType.of(Serializable.class),
            TSType.of(Closeable.class),
            TSType.of(AutoCloseable.class),
            TSType.of(Cloneable.class),
            TSType.of(RandomAccess.class)
         );


    /**
     *
     */
    static BiPredicate<TSType, TSType> isNamespaceMatch = (a, b) -> 
        b.supportNamespace()  && a.getNamespace().equals(b.getNamespace()) ;

    /**
     *
     */
    static BiPredicate<Class<?>,Type> typeParameterMatch = (declaringClass, type) ->
        type instanceof TypeVariable && Arrays.stream(declaringClass.getTypeParameters())
            .map(tp -> tp.getName())
            .anyMatch(name -> name.equals(((TypeVariable<?>) type).getName()))
                    ;

    static void log( String fmt, Object ...args ) {
        if( Boolean.getBoolean("debug") ) System.out.printf( fmt, args);
    }
    
    static void debug( String fmt, Object ...args ) {
        System.out.print( "DEBUG: ");       
        System.out.printf( fmt, args );
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
  public static <M extends Member> boolean isStatic(  M m ) {
   
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

           return (isStatic(m) &&
                   m.getReturnType().equals(m.getDeclaringClass()));
   }
   
   /**
    *
    * @param type_parameters_list
    * @return
    */
   static String getClassParametersDecl( java.util.List<String> type_parameters_list ) {

       if( type_parameters_list.isEmpty() ) return "";

       return  format("<%s>", type_parameters_list
                                       .stream()
                                       .collect( Collectors.joining(", ")) );
   }
   
   private static StringBuilder loadResourceByName( String name, StringBuilder result ) throws IOException {
       try(final java.io.InputStream is = TypescriptConverter.class.getClassLoader().getResourceAsStream(name) ) {
           int c; while( (c = is.read()) != -1 ) result.append((char)c);
       }
       
       return result;

   }
   
   /**
    * 
    * @param sb
    * @return
    * @throws IOException
    */
   public static StringBuilder loadDefaultDefinition( Optional<StringBuilder> sb ) throws IOException {
       
       Objects.requireNonNull(sb, "sb is null!");

       return loadResourceByName( "headerT.ts", sb.orElseGet( () -> new StringBuilder() ));

   }
   
   /**
    * 
    * @param sb
    * @return
    * @throws IOException
    */
   public static StringBuilder loadDefaultDeclarations( Optional<StringBuilder> sb ) throws IOException {
       
       Objects.requireNonNull(sb, "sb is null!");

       return loadResourceByName( "headerD.ts", sb.orElseGet( () -> new StringBuilder() ));

   }
      
   /**
    * 
    * @param keyExtractor
    * @return
    */
   public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
       Map<Object,Boolean> seen = new ConcurrentHashMap<>();
       return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
                       type.getValue().getPackage().equals(currentNS)  ?
                           type.getSimpleTypeName() :
                           type.getTypeName()
                        )
                   .append( getClassParametersDecl(parameters) )
                   .toString();
   }
   
   /**
   *
   * @param type
   * @param declaringType
   * @param declaredTypeMap
   * @param packageResolution
   * @return
   */
  static String convertJavaToTS(  Class<?> type,
                                          TSType declaringType,
                                          java.util.Map<String, TSType> declaredTypeMap,
                                          boolean packageResolution,
                                          Optional<Consumer<TypeVariable<?>>> onTypeMismatch )
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
              return format( "[%s]", convertJavaToTS(type.getComponentType(), 
                                                      declaringType, 
                                                      declaredTypeMap,
                                                      packageResolution,
                                                      Optional.empty()));
      }

      final TSType tt = declaredTypeMap.get( type.getName() );
      if( tt!=null ) {

          // FIX ISSUE ON NEW 
          onTypeMismatch.ifPresent( tm -> {
              Stream.of(((Class<?>)type).getTypeParameters())
              .filter( tv -> {
                  if( type.equals(declaringType.getValue()) ) return true;
                  return !typeParameterMatch.test(declaringType.getValue(), tv );    
              })
              .forEach( tv -> tm.accept(tv))
              ;

          });
      
          return getTypeName(tt, declaringType, packageResolution);
      }

      return format("any /*%s*/",type.getName());

  }

    /**
     *
     * @param type
     * @param declaringMember
     * @param declaringType
     * @param declaredTypeMap
     * @param packageResolution
     * @param onTypeMismatch
     * @param <M>
     * @return
     */
  public static <M extends Member> String convertJavaToTS(
                                          Type type,
                                          M declaringMember,
                                          TSType declaringType,
                                          java.util.Map<String, TSType> declaredTypeMap,
                                          boolean packageResolution,
                                          Optional<Consumer<TypeVariable<?>>> onTypeMismatch)
  {
      Objects.requireNonNull(type, "Type argument is null!");
      Objects.requireNonNull(declaringMember, "declaringMethod argument is null!");
      Objects.requireNonNull(declaringType, "declaringType argument is null!");
      Objects.requireNonNull(declaredTypeMap, "declaredTypeMap argument is null!");

      log( "PROCESSING MEMEBER: [%s]", declaringMember.getName());

      /**
       * 
       */
      final Predicate<TypeVariable<?>> typeMismatch = ( tv ) -> {
          if( isStatic(declaringMember) )              return true;
          if( declaringMember instanceof Constructor ) return true;
          return !typeParameterMatch.test(declaringType.getValue(), tv );
      };
      
      if( type instanceof ParameterizedType ) {

          final ParameterizedType pType = (ParameterizedType) type;

          final Class<?> rawType = (Class<?>)pType.getRawType();

          final TSType tstype = declaredTypeMap.get(rawType.getName());
          if( tstype==null ) {
                  return format("any /*%s*/",rawType.getName());
          }

          log( "ParameterizedType\n\t[%s]\n\traw[%s]\n\ttstype[%s]", 
          pType.getTypeName(),
          rawType.getName(),
          tstype.getTypeName() );

          String result = pType.getTypeName();
          if( rawType.isMemberClass() ) {
              result = result 
              .replace( rawType.getDeclaringClass().getName().concat("."), "" ) // use Alias
              ;                    
          }

          result = result.replace( rawType.getName(), tstype.getTypeName()) // use Alias
          ;

          if( packageResolution && isNamespaceMatch.test(tstype, declaringType) ) {
              result = result.replace( tstype.getTypeName(), tstype.getSimpleTypeName());
          }

          final Type[] typeArgs = pType.getActualTypeArguments();

          for( Type t : typeArgs ) {
              log( "TypeArgs [%s]", t.getTypeName());

              if( t instanceof ParameterizedType ) {

                  final String typeName = convertJavaToTS( t,
                                                          declaringMember,
                                                          declaringType,
                                                          declaredTypeMap,
                                                          packageResolution,
                                                          onTypeMismatch);
                  result = result.replace( t.getTypeName(), typeName);
                  log( "Parameterized Type\n\t%s\n\t%s\n\t%s",  t.getTypeName(), typeName, result );

              }
              else if(  t instanceof TypeVariable ) {

                  log( "type variable: %s",  t );

                  final TypeVariable<?> tv = (TypeVariable<?>)t;
                  if( typeMismatch.test(tv)) {

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

                  final String name = convertJavaToTS( (Class<?>)t, declaringType, declaredTypeMap, packageResolution, onTypeMismatch);

                  final String commented = format("/*%s*/", t.getTypeName());
                  result = result.replace( commented, "/*@*/")
                                              .replace(t.getTypeName(), name)
                                              .replace( "/*@*/", commented )
                                              ;
                  log( "Class Type\n\t%s\n\t%s",  t.getTypeName(),  result );
                                          }
              else if( t instanceof WildcardType ) {
                  
                  final WildcardType wt = (WildcardType) t;

                  final Type[] lb = wt.getLowerBounds();
                  final Type[] ub = wt.getUpperBounds();

                  log( "Wildcard Type: \n\t%s\n\tlb:%d\n\tup:%d",  wt.getTypeName(), lb.length, ub.length );

                  if( lb.length <= 1 && ub.length==1) {
                      final Type tt  = (lb.length==1) ? lb[0] : ub[0];

                      final String s = convertJavaToTS( tt,                           
                              declaringMember,
                              declaringType,
                              declaredTypeMap,
                              packageResolution,
                              onTypeMismatch);

                      result = result.replace( wt.getTypeName(), s);

                      if( tt instanceof ParameterizedType ) {
                          
                          // FIX ISSUE #7
                          result = result.replace( "? extends ", "" );
                          
                          // CHECK FOR NESTED WILDCARDTYPE
                          if( Stream.of(((ParameterizedType)tt).getActualTypeArguments())
                                  .anyMatch( arg -> (arg instanceof WildcardType) ))
                          {
                              //final Class<?> clazz = (Class<?>) (((ParameterizedType)tt).getRawType());

                              final String typeName = wt.getTypeName()
                                      //.replace( clazz.getName(), clazz.getSimpleName())
                                      .replace( "? extends ", "" )
                                      ;
                              result = result.replace( typeName, s);

                          }

                      }
                      log( "Wildcard Type(1):\n\t%s\n\t%s\n\t%s",  t.getTypeName(), s, result );
                  }
                  else {
                      result = result.replace(wt.getTypeName(), format( "any/*%s*/", wt));
                      log( "Wildcard Type(2)\n\t%s\n\t%s",  t.getTypeName(), result );
                  }
              }
              else if( t instanceof GenericArrayType ) {
                  //throw new IllegalArgumentException( format("type argument <%s> 'GenericArrayType' is a  not supported yet!", t));
              }

          }

          return result;
      }
      else if(  type instanceof TypeVariable ) {
          log( "class: %s",  type.getTypeName() );

          final TypeVariable<?> tv = (TypeVariable<?>)type;

          if( typeMismatch.test(tv) ) {

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
          final String result =  convertJavaToTS( (Class<?>)type, declaringType, declaredTypeMap, packageResolution, onTypeMismatch);
          log( "class:\n\t%s\n\t%s",  type.getTypeName(), result );
          return result;

      }
      else if( type instanceof WildcardType ) {
          throw new IllegalArgumentException( "type 'WildcardType' is a  not supported yet!");
      }
      else if( type instanceof GenericArrayType ) {
          final GenericArrayType t = (GenericArrayType)type;

          final Type componentType = t.getGenericComponentType();

          log( "generic array type: %s",  componentType.getTypeName() );

          final String tt = convertJavaToTS( componentType,
                                              declaringMember,
                                              declaringType,
                                              declaredTypeMap,
                                              packageResolution,
                                              onTypeMismatch);
          return format("[%s]", tt);
          
          
          //return ( typeParameterMatch.apply(declaringType.getValue(), componentType ))  ?
          //      format("[%s]", componentType ) :
          //      format("[any/*%s*/]", componentType );
          
      }

      throw new IllegalArgumentException( "type is a  not recognised type!");
  }

}
