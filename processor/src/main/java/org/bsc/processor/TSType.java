package org.bsc.processor;

import java.util.HashMap;


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

	protected static TSType from( Class<?> cl ) {
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
