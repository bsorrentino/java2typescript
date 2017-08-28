import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class JShellTest {


	static Set<Method> test1( final  Class<?> clazz ) {
		
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
}
