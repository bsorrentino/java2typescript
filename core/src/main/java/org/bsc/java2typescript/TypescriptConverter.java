package org.bsc.java2typescript;

import static java.lang.String.format;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypescriptConverter {

    final static String ENDL = ";\n";

    public static final List<TSType> PREDEFINED_TYPES = Arrays.asList(
            TSType.from(Class.class),
            TSType.from(Serializable.class),
            TSType.from(Closeable.class),
            TSType.from(AutoCloseable.class),
            TSType.from(Cloneable.class),
            TSType.from(RandomAccess.class)
         );


        /**
         *
         */
        public static BiPredicate<TSType, TSType> isNamespaceMatch = (a, b) -> 
            b.supportNamespace()  && a.getNamespace().equals(b.getNamespace()) ;


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
        
        private final static void debug( String fmt, Object ...args ) {
            System.out.print( "DEBUG: ");       
            System.out.println( format( fmt, (Object[])args));
        }

        /**
         * 
         * @param type
         * @return
         */
        public static final String processFunctionalInterface( TSType type  ) {
            Objects.requireNonNull(type, "argument 'type' is not defined!");

            return null;
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
         * @return
         */
        public static boolean isFunctionalInterface( final Class<?> c ) {
            if( !c.isInterface()) return false;
            if( c.isAnnotationPresent(FunctionalInterface.class)) return true;
            
            return Arrays.stream(c.getDeclaredMethods()).filter( m -> Modifier.isAbstract(m.getModifiers()) ).count() == 1;
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
         * @param declaringMember
         * @param declaredTypeMap
         * @param packageResolution
         * @param typeMatch
         * @param onTypeMismatch
         * @return
         */
        public static <M extends Member> String convertJavaToTS(    Type type,
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

            /**
             * 
             */
            final Predicate<TypeVariable<?>> typeMismatch = ( tv ) -> {
                if( isStatic(declaringMember) ) return true;
                if( declaringMember instanceof Constructor ) return true;
                return !typeParameterMatch.apply(declaringType.getValue(), tv );
            };
            
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

                if( packageResolution && isNamespaceMatch.test(tstype, declaringType) ) {
                    result = result.replace( tstype.getTypeName(), tstype.getSimpleTypeName());
                }

                final Type[] typeArgs = pType.getActualTypeArguments();

                for( Type t : typeArgs ) {
                    if( t instanceof ParameterizedType ) {

                        final String typeName = convertJavaToTS( t,
                                                                declaringMember,
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

                            final String s = convertJavaToTS( tt,                           
                                    declaringMember,
                                    declaringType,
                                    declaredTypeMap,
                                    packageResolution,
                                    onTypeMismatch);

                            result = result.replace( wt.getTypeName(), s);

                            // CHECK FOR NESTED WILDCARDTYPE
                            if( tt instanceof ParameterizedType &&
                                Stream.of((Type[])((ParameterizedType)tt).getActualTypeArguments())
                                    .anyMatch( arg -> (arg instanceof WildcardType) ))
                            {
                                final Class<?> clazz = (Class<?>) (((ParameterizedType)tt).getRawType());
                                final String typeName = wt.getTypeName().replace( clazz.getName(), clazz.getSimpleName());
                                result = result.replace( typeName, s);
                            }
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
                log( "class: %s",  type.getTypeName() );

                final String name = convertJavaToTS( (Class<?>)type, declaringType, declaredTypeMap, packageResolution);
                return name;
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

        /**
         *
         * @param type
         * @param declaringType
         * @param declaredTypeMap
         * @param packageResolution
         * @return
         */
        private static String convertJavaToTS(  Class<?> type,
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
         * @param declaredClass
         * @return
         */
        public String processStatic( TSType type, java.util.Map<String, TSType> declaredTypeMap ) {
            
                final Context ctx = new Context( type, declaredTypeMap);
           
                ctx.append("interface ")
                .append( ctx.type.getSimpleTypeName() )
                .append("Static {\n\n")
                ;
            
                ctx.processEnumType();
                
                //Append class property
                ctx.append("\treadonly class:any;\n");

                Stream.of(ctx.type.getValue().getConstructors())
                .filter( c -> Modifier.isPublic(c.getModifiers()) )
                .forEach( c -> {
                    ctx.append("\tnew")
                        .append( getMethodParametersAndReturnDecl( ctx, c, false) )
                        .append(ENDL);
                    }
                );
                
                final java.util.Set<Method> methodSet =
                        ctx.type.getMethods()
                        .stream()
                        .filter( TypescriptConverter::isStatic )
                        .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<>() ));
                    
            if( !methodSet.isEmpty() ) {
                    
                    methodSet.stream()
                            .sorted( (a,b) -> a.getName().compareTo(b.getName()))
                            .forEach( md ->
                            ctx.append( '\t' )
                                  .append(md.getName()) 
                              .append( getMethodParametersAndReturnDecl( ctx, md, false) )
                              .append(  ENDL ))
                            ;
            }
            
            ctx.append( "}\n\n" )
                    .append("export const ")
                    .append(ctx.type.getSimpleTypeName())
                    .append(": ")
                    .append(ctx.type.getSimpleTypeName())
                    .append("Static = Java.type(\"")
                    .append( ctx.type.getValue().getName() )
                    .append("\")")
                    .append( ENDL )
                    .append("\n\n")
                    ;
                    
             return ctx.toString();
        }
   
        /**
         * 
         * @param m
         * @param type
         * @param declaredTypeMap
         * @param packageResolution
         * @return
         */
        public <E extends Executable> String getMethodParametersAndReturnDecl( 
                E m, 
                TSType type, 
                java.util.Map<String, TSType> declaredTypeMap, 
                boolean packageResolution ) 
        {
            final Context ctx = new Context(type, declaredTypeMap);
            
            return getMethodParametersAndReturnDecl(ctx, m, packageResolution);
        }
        
        /**
         * 
         * @param ctx
         * @param m
         * @param packageResolution
         * @return
         */
        private <E extends Executable> String getMethodParametersAndReturnDecl( Context ctx,  E m, boolean packageResolution ) 
        {
            final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);
                
            final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName()) ;
                
            final Parameter[] params = m.getParameters();

            final String params_string =
                    Arrays.stream(params)
                            .map( (tp) -> {
                                
                                final String name = getParameterName(tp);
                                                         
                                if( tp.isVarArgs() ) {   
                                    
                                    String type = null;
                                    if( tp.getParameterizedType() instanceof GenericArrayType ) {
                                        
                                        type = convertJavaToTS( ((GenericArrayType)tp.getParameterizedType()).getGenericComponentType(),
                                                m,
                                                ctx.type,
                                                ctx.declaredTypeMap, 
                                                packageResolution, 
                                                Optional.of( addTypeVar )) ;
                                    }
                                    else {
                                        type = convertJavaToTS( tp.getType().getComponentType(),
                                                                        m,
                                                                        ctx.type,
                                                                        ctx.declaredTypeMap, 
                                                                        packageResolution, 
                                                                        Optional.of( addTypeVar )) ;
                                    }
                                    return String.format( "...%s:%s[]", name, type );
                                    
                                }
                                
                                final String type = convertJavaToTS( tp.getParameterizedType(),
                                                                    m,
                                                                    ctx.type,
                                                                    ctx.declaredTypeMap, 
                                                                    packageResolution, 
                                                                    Optional.of( addTypeVar )) ;
                                return String.format( "%s:%s", name, type );
                            })
                            .collect(Collectors.joining(", "))
                            ;

            final Type returnType =  ( m instanceof Method ) ?
                                            ((Method)m).getGenericReturnType() :
                                                ctx.type.getValue();
                                            
           final String  tsReturnType = 
                            convertJavaToTS(    returnType,
                                            m,
                                            ctx.type,
                                            ctx.declaredTypeMap,
                                            packageResolution,
                                            Optional.of( addTypeVar ));
           
           
                final StringBuilder result = new StringBuilder();
                
                if( !TypeVarSet.isEmpty() ) {
                    result
                        .append('<')
                        .append( TypeVarSet.stream().collect(Collectors.joining(",")))
                        .append('>');               
                }
                return  result
                                .append("( ")
                                .append(params_string)
                            .append(" ):")
                            .append(tsReturnType)
                            .toString();
        }    

        /**
         * 
         * @param ctx
         * @param m
         * @return
         */
        private String getMethodDecl( Context ctx, final Method m, boolean optional ) {

           final StringBuilder sb = new StringBuilder();

           if( Modifier.isStatic(m.getModifiers()) ) {

                   if( ctx.type.getValue().isInterface() ) {
                       sb.append( "// ");
                   }

                   sb.append("static ").append(m.getName());
           }
           else {
                   sb.append(m.getName());
                   if( optional ) sb.append('?');
           }

           sb.append( getMethodParametersAndReturnDecl(ctx,  m, true) );

           return  sb.toString();

       }

        /**
         * 
         * @param type
         * @param declaredTypeMap
         * @return
         */
       public String getClassDecl( TSType type, java.util.Map<String, TSType> declaredTypeMap ) {
           final Context ctx = new Context(type, declaredTypeMap);
           
           return ctx.getClassDecl().toString();
          
       }
        
       
       class Context implements Cloneable {
           final TSType type;
           final java.util.Map<String, TSType> declaredTypeMap;
           final StringBuilder sb = new StringBuilder();
           
            public Context(TSType type, Map<String, TSType> declaredClassMap) {
                Objects.requireNonNull(type, "type is null!");
                Objects.requireNonNull(declaredClassMap, "declaredClassMap is null!");

                this.type = type;
                this.declaredTypeMap = declaredClassMap;
            }
            
            /**
             * 
             * @param cs
             * @return
             */
            Context append( CharSequence cs ) {
                sb.append(cs);
                return this;
            }
            
            /**
             * 
             * @param ch
             * @return
             */
            Context append( char ch ) {
                sb.append(ch);
                return this;
            }
            
            /**
            * 
            * @return
            */
           Context getClassDecl()
           {

               final StringBuilder inherited = new StringBuilder();

               if( type.getValue().isInterface() ) {
                   sb.append( "interface ");
               }
               else {

                   if( type.getValue().isEnum() ) sb.append( "/* enum */" );
                   
                   if( type.hasAlias()) sb.append("declare ");
                   
                   sb.append( "class ");

                   final TSType superclass = TSType.from(type.getValue().getSuperclass());

                   if( superclass!=null ) {
                           inherited
                               .append( " extends ")
                               .append( getTypeName(superclass, type, true) )
                               ;
                   }
               }

               final Class<?>[] interfaces = type.getValue().getInterfaces();

               if(interfaces.length > 0 ) {

                       final String ifc = Arrays.stream(interfaces)
                                           .map( c -> TSType.from(c) )
                                       .map( t -> getTypeName(t, type, true) )
                                       .collect( Collectors.joining(", "))
                                       ;
                       inherited
                           .append( (type.getValue().isInterface()) ? " extends " : " implements ")
                           .append(     ifc )
                           ;


               }
               
               sb.append( getTypeName(type, type, true) );

               if( inherited.length()>0 || type.hasAlias()) {

                   sb.append("/*");
                   
                   if( type.hasAlias() )        sb.append( type.getValue().getName() );
                   if( inherited.length()>0 )   sb.append( inherited );
                   
                   sb.append("*/");
               }

               sb.append( " {");
                                   
               return this;
           }

           /**
            * 
            * @return
            */
           Context processEnumDecl() {
               if( type.getValue().isEnum() ) {
                   type.setExport(true); // force export
                   Arrays.stream( type.getValue().getEnumConstants() )
                   .forEach( (c) -> 
                       sb.append( '\t' )
                         .append( "// ")
                         .append(  c.toString() )
                         .append( ':')
                         .append( type.getSimpleTypeName() )
                         .append( ';' )
                         .append(  '\n' )
                   );
                   sb.append('\n');
               }
               
               return this;
               
           }
           
           /**
           *
           * @param sb
           * @param type
           * @param declaredTypeMap
           */
          Context processNestedClasses( int level ) {

              final Class<?> nestedClasses[] = type.getValue().getClasses();

              if( nestedClasses.length == 0 ) return this;

              sb.append( "export module " )
                .append(type.getSimpleTypeName())
                    .append(" {\n\n")
                    ;

              Stream.of(nestedClasses)
                      .filter( distinctByKey( c -> c.getSimpleName() ) )
                      .map( cl ->  TSType.from(cl) )
                      .map( t -> processClass( level + 1, t, declaredTypeMap) )
                      .forEach( decl -> sb.append(decl) );

              sb.append("\n} // end module ")
                      .append(type.getSimpleTypeName())
                      .append('\n')
                  ;
              return this;
          }

          
          private Context processEnumType( ) {
              
              if( type.getValue().isEnum() ) {
                  Arrays.stream( type.getValue().getEnumConstants() )
                  .forEach( (c) -> 
                      sb.append( '\t' )
                        .append(  c.toString() )
                        .append( ':')
                        .append( type.getTypeName() )
                        .append( ';' )
                        .append(  '\n' )
                  );
                  sb.append('\n');
              }
              
              return this;
              
          }
          

        public Context clone() {
            return new Context(type, declaredTypeMap);
        }
         /**
          * 
          */
        @Override
        public String toString() {
            return sb.toString();
        }

           
        }
       
       
        /**
        *
        * @param bi
        * @param declaredTypeMap
        * @return
        */
       public String processClass( int level, TSType tstype, java.util.Map<String, TSType> declaredTypeMap )   {

           final Context ctx = new Context( tstype, declaredTypeMap);
           
           if( tstype.supportNamespace() )
               ctx.append( "declare namespace " )
                  .append( tstype.getNamespace() )
                  .append(" {\n\n")
                   ;

           ctx.getClassDecl().append("\n\n");

           if( tstype.isFunctionalInterface() ) {
               
               tstype.getMethods().stream()
               .filter( m -> Modifier.isAbstract(m.getModifiers()) )
               .findFirst()
               .ifPresent( m -> ctx.append( '\t' )
                                   .append( getMethodParametersAndReturnDecl( ctx, m, false) )
                                   .append( ENDL )) ;
              
               tstype.getMethods().stream()
               .filter( m -> !Modifier.isAbstract(m.getModifiers()) )
               .map( m -> getMethodDecl(ctx, m, true /*optional*/) )
               .sorted()
               .forEach( decl ->
                   ctx.append( '\t' )
                     .append(decl)
                     .append(  ENDL ))
                       ;
               
 
           } else {
               
               ctx.processEnumDecl();

               final java.util.Set<Method> methodSet =
                       tstype.getMethods()
                       .stream()
                       .filter( md -> (tstype.isExport() && isStatic(md))==false )
                       .filter( (md) -> {
                           final String name = md.getName();
                           return !(   name.contains("$")      || // remove unnamed
                                       name.equals("getClass")  ||
                                       name.equals("hashCode")  ||
                                       name.equals("wait")     ||
                                       name.equals("notify")   ||
                                       name.equals("notifyAll") );
                   })
                   .collect( Collectors.toCollection(() -> new java.util.LinkedHashSet<Method>() ));

               methodSet.stream()
                   .map( md -> getMethodDecl(ctx, md, false /*optional*/) )
                   .sorted().forEach( (decl) ->
                       ctx.append( '\t' )
                         .append(decl)
                         .append(  ENDL ))
                           ;
           }

           ctx.append("\n} // end ")
                   .append(tstype.getSimpleTypeName())
                   .append('\n');

           // NESTED CLASSES
           //if( level == 0 ) ctx.processNestedClasses( level );

           if( tstype.supportNamespace() )
                   ctx.append("\n} // end namespace ")
                       .append( tstype.getNamespace() )
                       .append('\n');

           return ctx.toString();

       }
        
}
