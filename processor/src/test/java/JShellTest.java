import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class JShellTest {


	static Set<Method> allMethods( final  Class<?> clazz ) {
		
		Predicate<Method> include = m -> 
			!m.isBridge() && 
			!m.isSynthetic() &&
			Character.isJavaIdentifierStart(m.getName().charAt(0)) && 
			m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		Set<Method> methods = new LinkedHashSet<>();
		Collections.addAll(methods, clazz.getMethods());
		methods.removeIf(include.negate());

		Stream.of(clazz.getDeclaredMethods())
			.filter(include)
			.forEach(methods::add);
		return methods;
	}
	
	static String testCollector() {
		
		Collector<String,StringBuffer,String> c = 
				Collector.of( 
						() -> new StringBuffer(),
						(sb, token) -> sb.append(token).append(','),
						(sb_left,sb_right) -> sb_left.append(sb_right),
						(sb) -> { 
							sb.deleteCharAt( sb.length()-1 );
							return sb.toString();
						}
					);
		
		return Arrays.asList( "A", "B", "C", "D", "E").stream().collect(c);
		
	}
}
