package org.bsc.java2typescript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 
 * @author softphone
 *
 */
@SuppressWarnings("serial")
public class TSType extends HashMap<String,Object> {

	protected TSType() {
		super(3);
	}
	
	public static TSType from( Class<?> cl, boolean export ) {
		return new TSType() {{
			put( "value", cl);
			put( "export", export);			
		}};
	}
	
	public static TSType from( Class<?> cl,  String alias, boolean export ) {
		return new TSType() {{
			put( "value", cl);
			put( "export", export);
			put( "alias", alias);
		}};
	}

	/**
	 * Assume that class is managed as Functional Interface
	 * 
	 * @param cl
	 * @param alias
	 * @return
	 */
	public static TSType functional( Class<?> cl,  String alias ) {
		return new TSType() {{
			put( "value", cl);
			put( "alias", alias);
			put("functional", true);
		}};
	}

	public static TSType from( Class<?> cl ) {
		return new TSType() {{ put( "value", cl); }};
	}
	
	/**
	 * 
	 * @return
	 */
	public Class<?> getValue() {
		return  getClassFrom(super.get("value"));
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isExport() {
		return (boolean) super.getOrDefault("export", false);
	}
	
	/**
	 * 
	 * @return
	 */
	public TSType setExport( boolean export ) {
		super.put("export", export);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFunctionalInterface() {
		return ((boolean) super.getOrDefault("functional", false)) || 
				(getValue().isInterface() && getValue().isAnnotationPresent(FunctionalInterface.class));
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasAlias() {
		final String alias =  (String) super.get("alias");
		return alias!=null && !alias.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAlias() {
		return (String) super.get("alias");
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getTypeName() {
		return (hasAlias()) ? getAlias() : getValue().getName();
	}

	/**
	 * 
	 * @return
	 */
	public final String getSimpleTypeName() {
		return (hasAlias()) ? getAlias() : getValue().getSimpleName();
	}

    /**
    *
    * @param type
    * @return
    */
   public Set<Field> getFields() {
       
        final Predicate<Field> std = f ->
            !f.isSynthetic() &&
            Modifier.isPublic( f.getModifiers() ) &&
            Character.isJavaIdentifierStart(f.getName().charAt(0)) &&
            f.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

        return Stream.concat( Stream.of(getValue().getFields()), Stream.of(getValue().getDeclaredFields()) )
            .filter(std)
            .collect( Collectors.toSet( ) );

   }
          
    /**
    *
    * @param type
    * @return
    */
   public Set<Method> getMethods() {
       final Predicate<Method> include = m ->
           !m.isBridge() &&
           !m.isSynthetic() &&
           Modifier.isPublic( m.getModifiers() ) &&
           Character.isJavaIdentifierStart(m.getName().charAt(0)) &&
           m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

       return Stream.concat( Stream.of(getValue().getMethods()), Stream.of(getValue().getDeclaredMethods()) )
           .filter(include)
           .collect( Collectors.toSet( ) );

   }
   
	
    /**
     *
     * @param dt
     * @return
     */
    private Class<?> getClassFrom( Object dt ) {
        	if( dt instanceof Class ) return (Class<?>)dt;
        	
    		try {
            return Class.forName(dt.toString());
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException(String.format("class not found [%s]",dt), e1);
        }
    }
    
	@Override
	public boolean equals(Object o) {
		return getValue().equals(((TSType)o).getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

}
