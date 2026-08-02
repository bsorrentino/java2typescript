# Changelog



<!-- "name: Unreleased" is a release tag -->

## [Unreleased](https://github.com/bsorrentino/langgraph4j/releases/tag/Unreleased) ()





### Documentation

 -  update changelog ([ce0ab8ca5f85f7f](https://github.com/bsorrentino/langgraph4j/commit/ce0ab8ca5f85f7f7a94fe3452936fe4f5318632e))







<!-- "name: v2.0.0" is a release tag -->

## [v2.0.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v2.0.0) (2026-08-02)

### Features

 *  **processor**  declare types discovered by scanning the classpath ([ae6c950d446ba5e](https://github.com/bsorrentino/langgraph4j/commit/ae6c950d446ba5ec65b6c5d4549e7ba6b5246629))
     > ts.scan option to enumerate public types from compiled class files
     > Scanned types are added after the annotated ones, so a type named by both keeps its explicit attributes.
     > Co-Authored-By: Claude Opus 5 &lt;noreply@anthropic.com&gt;
   
 *  **processor**  add "declare" type attribute ([f49380f1928ead7](https://github.com/bsorrentino/langgraph4j/commit/f49380f1928ead7ba279baab54c457614a065780))
     > defaults to true (no change), when false does not export class to typescript.
     > Useful when needing to parse types for the new inheritance optimization without re-export them.
     > (practical use case : zapAddons build depending on zaproxy classes, already exported in another file)
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  implement inheritance and extend classes ([6c8d8863810c1c7](https://github.com/bsorrentino/langgraph4j/commit/6c8d8863810c1c709e70d2b4b685417f9c300b5c))
     > extends available classes and don&#x27;t  redefine inherited methods/fields.
     > Greatly reduces generated typescript file size (~x3 or x5)
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  alias nested types so Outer.Inner resolves ([c006399d32f1f3c](https://github.com/bsorrentino/langgraph4j/commit/c006399d32f1f3c8ff11930967a494d7349e297f))
     > Nested types are emitted flattened as Outer$Inner siblings, but a host
     > runtime accesses them as Outer.Inner. Emit a &#x60;namespace Outer&#x60; that
     > merges with &#x60;class Outer&#x60; and re-exposes each emitted nested type under
     > its simple name (type alias, plus a value alias for classes/enums), so
     > both Outer.Inner and the flattened Outer$Inner resolve. Generic nested
     > types carry their type parameters through the alias.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit a static `class` member on class declarations ([101dade36829cba](https://github.com/bsorrentino/langgraph4j/commit/101dade36829cba65c51fd671e8ca18322fcf84b))
     > Every Java class value exposes &#x60;.class&#x60; (its java.lang.Class object), so
     > emit &#x60;static class:java.lang.Class&lt;any&gt;&#x60; on each class declaration to let
     > &#x60;SomeClass.class&#x60; type-check. Interfaces are skipped (no runtime value,
     > and &#x60;static&#x60; is illegal on a TS interface). Also give java.lang.Class a
     > minimal shape (getResource) instead of an empty interface.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  accept a TS array for Collection/Iterable parameters ([eb2253e96915e6c](https://github.com/bsorrentino/langgraph4j/commit/eb2253e96915e6c7784543653f7232795760d693))
     > Widen a Collection/Iterable parameter type to &#x60;T | (E)[]&#x60; so a plain
     > array is assignable where a host runtime auto-converts arrays to a Java
     > collection. Applied to method and constructor parameters only; return
     > types are unchanged, so a returned collection keeps its full interface.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit protected abstract methods in class declarations ([5fac0bc1cec1f06](https://github.com/bsorrentino/langgraph4j/commit/5fac0bc1cec1f062b5096c9a5050100f1c4df915))
     > testIncludeMethod dropped all non-public methods, omitting protected
     > abstract methods that are part of the callable contract every concrete
     > subclass must implement. Include them; public members are unchanged and
     > ordinary protected/private concrete methods stay excluded.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **processor**  generate a typed Java.type() registry ([d0b7943e7babe3e](https://github.com/bsorrentino/langgraph4j/commit/d0b7943e7babe3e9e63112c10a06325da8598553))
     > Generate a &lt;out&gt;-registry.d.ts mapping each class/enum&#x27;s fully-qualified
     > name to &#x27;typeof &lt;ns&gt;.X&#x27;, and re-enable the typed Java.type overload in
     > headerD.ts so Java.type(&quot;known.Class&quot;) returns the concrete class instead
     > of any. The registry interface name is configurable via the &#x27;ts.registry&#x27;
     > processor option (default JavaTypeRegistry), so distinct projects can each
     > emit their own (e.g. ZapApiTypeRegistry). Interfaces have no typeof value
     > form and are excluded, falling through to the generic type(k:string):T
     > overload. The header template carries a {{REGISTRY_TYPE}} placeholder
     > substituted at generation time.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  bind Java.type() values via typeof in -types.ts ([a13010118da951f](https://github.com/bsorrentino/langgraph4j/commit/a13010118da951f5fd5a1a4088e704365d7b289b))
     > Classes and enums now expose their full static side in the .d.ts, so the
     > runtime binding collapses to a single line -- export const X: typeof &lt;ns&gt;.X
     > &#x3D; Java.type(&quot;...&quot;) -- instead of a duplicated XStatic interface listing
     > every constructor and static method. Interfaces have no typeof value form,
     > so they keep their explicit ...Static type (functional SAM construct
     > signature + static methods).
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit static members in class declarations (.d.ts) ([0e40a071be07445](https://github.com/bsorrentino/langgraph4j/commit/0e40a071be074456c4b260d04bde8d714e278bce))
     > Stop stripping static methods from the .d.ts class for exported types.
     > Static methods already carry the &#x27;static&#x27; keyword, so the class now
     > exposes its full static side and &#x27;typeof &lt;class&gt;&#x27; is the complete
     > class-object type. This lets the &lt;out&gt;-types.ts runtime binding collapse
     > to &#x27;typeof &lt;class&gt;&#x27; (next commit) instead of duplicating every signature
     > in a separate XStatic interface.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit Java enums as native string-valued TS enums ([6f2aa5ecf03ebd2](https://github.com/bsorrentino/langgraph4j/commit/6f2aa5ecf03ebd226f24451992f9021ac5adfecb))
     > Emit an enum as a native TypeScript &#x60;enum X { A &#x3D; &quot;A&quot;, ... }&#x60; inside its
     > namespace instead of a class with commented-out constants. Each constant
     > maps to its own name as a string, so GraalJS&#x27; read-as-string coercion
     > (&#x60;plugin.getMode() &#x3D;&#x3D; &quot;SAFE&quot;&#x60;) type-checks, while the nominal enum type
     > still rejects passing a bare string where a Java enum is required - which
     > GraalJS also rejects at runtime. The Java Enum machinery (values/valueOf/
     > ordinal) is intentionally dropped in favour of the TS-native model.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit public constructors in class declarations ([6632b5ecb730b2a](https://github.com/bsorrentino/langgraph4j/commit/6632b5ecb730b2ae8f205dc848159fec34ea50e4))
     > Add TSConverterContext.getConstructorDecl and emit public constructors
     > from the declaration transformer. Handles varargs (rendered as a rest
     > parameter) and parameters named after TS reserved keywords (renamed via
     > getParameterName). No type parameters are emitted since a TS constructor
     > cannot declare its own; interfaces have no constructors so none leak into
     > a TS interface.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  emit public fields in class/interface declarations ([dd071206e32db75](https://github.com/bsorrentino/langgraph4j/commit/dd071206e32db752be06653b38a5082b76213ab6))
     > Add TSConverterContext.getFieldDecl and emit public fields from the
     > declaration transformer. Static fields become &#x27;static readonly&#x27; (commented
     > out on interfaces, where &#x27;static&#x27; is illegal), final fields become
     > &#x27;readonly&#x27;. Enum constants are filtered out to avoid duplicating the
     > declarations produced by processEnumDecl.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  suffix parameters named after TS reserved keywords ([8e7ec6fcf88ef64](https://github.com/bsorrentino/langgraph4j/commit/8e7ec6fcf88ef647a72f27648ebdbf0b67a08299))
     > Java parameter names such as &#x27;type&#x27;, &#x27;string&#x27; or &#x27;number&#x27; are valid Java
     > identifiers but reserved words in TypeScript, producing invalid
     > declarations. Rename any such parameter by appending &#x27;_p&#x27;, replacing the
     > previous one-off &#x27;function&#x27; -&gt; &#x27;func&#x27; special case with a full keyword
     > set. Backed by a Set for O(1) lookup.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  resolve arbitrarily nested member types ([5634c8d7f35610e](https://github.com/bsorrentino/langgraph4j/commit/5634c8d7f35610ebab7db20a83cd7d9963838b3f))
     > The source name of a nested type uses &#x27;.&#x27; for both package and nesting
     > separators (a.b.Outer.Inner), but the class loader expects &#x27;$&#x27; for
     > nesting (a.b.Outer$Inner). Since the package boundary is unknown, convert
     > trailing dots to &#x27;$&#x27; one at a time, right to left, and return the first
     > name that loads. This handles arbitrarily deep nesting
     > (a.b.Outer$Middle$Inner), not just a single level.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  **core**  load @Type classes without running static initializers ([4ad1c2c92e9e824](https://github.com/bsorrentino/langgraph4j/commit/4ad1c2c92e9e82404545a9ac0c8caaa311e289d7))
     > Resolve annotated classes with Class.forName(name, false, loader) instead
     > of the 1-arg form, so generation reads class metadata (methods, fields,
     > modifiers) without triggering static initializers. Initializing an
     > application&#x27;s classes outside their own runtime can throw
     > ExceptionInInitializerError when a static block relies on framework state
     > that has not been bootstrapped.
     > Co-Authored-By: Claude Opus 4.8 &lt;noreply@anthropic.com&gt;
   
 *  move to java 17 ([b0c1e25248993c2](https://github.com/bsorrentino/langgraph4j/commit/b0c1e25248993c2f95e62e8832731886e4b7c485))
   
 *  **rhino**  refine rhino implementation ([3ad34312db3abc9](https://github.com/bsorrentino/langgraph4j/commit/3ad34312db3abc91e75a86c55dc1edc3430a7106))
     > 1. don&#x27;t use jvm-npm anymore
     > 2. use native commonjs module loader
     > 3. don&#x27;t use parcel bundler anymore
   

### Bug Fixes

 -  **core**  re-declare methods that narrow an inherited return type ([6cf5b4d6093b92d](https://github.com/bsorrentino/langgraph4j/commit/6cf5b4d6093b92d1c262fd75aeb3df81b6378a39))
     > Return type was not checked previously on methods prototype,
     > so overriden methods with different return were not re-declared.
     > example:
     > &#x60;&#x60;&#x60;java
     > ParentClass {
     > public ParentClass m();
     > }
     > ChildClass extends ParentClass {
     > @Override public ChildClass m();
     > }
     > &#x60;&#x60;&#x60;
     > Will now correctly re-declare in TS the &#x60;m&#x60; method with return value of type ChildClass.
     > Co-Authored-By: Claude Opus 5 &lt;noreply@anthropic.com&gt;

 -  **processor**  skip types that cannot be introspected ([a906691609b6a59](https://github.com/bsorrentino/langgraph4j/commit/a906691609b6a5943a5d13db442dc998d14328b9))
     > Class loading resolves references lazily, so a class whose method
     > signatures name absent types loads fine and only fails when its members
     > are read. That happened deep inside a transformer and aborted generation
     > for every type at once, which optional dependencies make routine: a UI
     > class referencing JavaFX, a driver referencing a browser library.
     > Probe the members while scanning, so such a type is rejected up front and
     > reported by name, and guard the annotation declared types the same way.
     > Co-Authored-By: Claude Opus 5 &lt;noreply@anthropic.com&gt;


### Refactor

 -  **core**  update code with java17 features ([95c9c6d605003f0](https://github.com/bsorrentino/langgraph4j/commit/95c9c6d605003f0b3eee4b2c3287d29a7fb252b5))
   

### Test 

 -  generate sample output ([665c7bcb95bbf3d](https://github.com/bsorrentino/langgraph4j/commit/665c7bcb95bbf3dddb8f980fc962602c8f0e1e59))
   
 -  **core**  fix tests made invalid by changes ([7ac510791b053d2](https://github.com/bsorrentino/langgraph4j/commit/7ac510791b053d21e3c6a821541dc4dbe8fa7cce))
    > removed &quot;no static in interface&quot; claude error
 > fixed test with args of type &quot;java.util.list&lt;Type&gt;&quot; where declaration now also allows &quot;(Type)[]&quot; as  GraalJs converts Lists to native JS arrays.

 -  **rhino**  unit test refinements ([cccfc52fa32f1b1](https://github.com/bsorrentino/langgraph4j/commit/cccfc52fa32f1b11587e1e3329cbaacbaf82dbb9))
   

### Documentation

 -  update readme with new features ([f8067b6368dcbf1](https://github.com/bsorrentino/langgraph4j/commit/f8067b6368dcbf148b1e61e4305e230110f6bf3c))
     > list plugin options
     > give example gradle config to generate types for a full project

 -  update changelog ([717e2a31f4067f0](https://github.com/bsorrentino/langgraph4j/commit/717e2a31f4067f0e3a2a1b0efcd7ec89c2fb5a46))

 -  update readme ([fd28f841311567a](https://github.com/bsorrentino/langgraph4j/commit/fd28f841311567aeb2a709ab4c03b7e549b2cf02))

 -  update changelog ([48e1d767e58b79e](https://github.com/bsorrentino/langgraph4j/commit/48e1d767e58b79e9beb31d781a1b220334f4775c))


### ALM 

 -  bump to next version 2.0.0 ([f70e479f5602a0f](https://github.com/bsorrentino/langgraph4j/commit/f70e479f5602a0ffed990a358298c70be20940d6))
   
 -  update Maven plugin versions and remove unused distribution management ([845a39aebd6acb6](https://github.com/bsorrentino/langgraph4j/commit/845a39aebd6acb6b4eed261e0c85eb8e524578f5))
   
 -  **samples/graaljs**  update GraalVM dependencies to remove '-community' suffix ([114fd9a3debb35d](https://github.com/bsorrentino/langgraph4j/commit/114fd9a3debb35d3f2ee0c37389ed14755f178dc))
   
 -  update graalvm version to 25.1.3 ([9bc327969690471](https://github.com/bsorrentino/langgraph4j/commit/9bc327969690471123079983c8039cd2624b7cae))
   
 -  update rhino dependency version to 1.9.1 ([5998c730f1eddd0](https://github.com/bsorrentino/langgraph4j/commit/5998c730f1eddd0b30c9012e37f1af4321588057))
   
 -  update Maven version requirement to [3.9.0,) ([80d81ccf53430bf](https://github.com/bsorrentino/langgraph4j/commit/80d81ccf53430bf69c73e88be5e806182a080e78))
   
 -  bump to SNAPSHOT ([cbc32cee1969ad8](https://github.com/bsorrentino/langgraph4j/commit/cbc32cee1969ad87168f17d017c69c05f757dcdf))
   
 -  bump to SNAPSHOT ([4c7961694fa07e6](https://github.com/bsorrentino/langgraph4j/commit/4c7961694fa07e6364edb58a2c07873ade0c227f))
   
 -  bump to next intermediate version ([a46e7285576cda8](https://github.com/bsorrentino/langgraph4j/commit/a46e7285576cda851ef3303409f20fb2cf07a9c5))
   
 -  bump to next snapshot ([e4d0d37831972fc](https://github.com/bsorrentino/langgraph4j/commit/e4d0d37831972fce29ed07a74d245230a856f821))
   
 -  add maven wrapper ([a333fcb1f8fa978](https://github.com/bsorrentino/langgraph4j/commit/a333fcb1f8fa9789702b6a3eb3480f8c649732ce))
   
 -  move to java17 ([c63243e028c7012](https://github.com/bsorrentino/langgraph4j/commit/c63243e028c7012844e7dd39cc11ed487c778b25))
   
 -  **graaljs**  bump graalvm + graaljs versions ([544d8d1a98f3e0d](https://github.com/bsorrentino/langgraph4j/commit/544d8d1a98f3e0d6db2c97316cf6224471cea76c))
   
 -  add keyname for sign artifact ([08be7a0c05f6258](https://github.com/bsorrentino/langgraph4j/commit/08be7a0c05f6258ff2bc791838cd292b80369094))
   
 -  bump to SNAPSHOT ([c81e3ebf3935e0c](https://github.com/bsorrentino/langgraph4j/commit/c81e3ebf3935e0cc48f31a1da7476176c20d736b))
   





<!-- "name: v1.4.0" is a release tag -->

## [v1.4.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.4.0) (2024-10-07)


### Bug Fixes

 -  **generation**  add missing brackets ([c0e4030bdfe2a1d](https://github.com/bsorrentino/langgraph4j/commit/c0e4030bdfe2a1da6213bdbd9879feb85165cbcb))
     > issue #34

 -  **converter**  Java List inherit JS Array ([53146a0ef98d979](https://github.com/bsorrentino/langgraph4j/commit/53146a0ef98d9796e9b1c54c3543a56d24802f4e))
     > issue #34

 -  **converter**  test for 'foreign-object-prototype' ([0a440ec9bc40368](https://github.com/bsorrentino/langgraph4j/commit/0a440ec9bc40368fcb16dfd3e3f615aa45027622))
     > add unit test for &#x27;foreign-object-prototype&#x27;
     > issue #34

 -  **converter**  test for 'foreign-object-prototype' ([733926cfdb33e50](https://github.com/bsorrentino/langgraph4j/commit/733926cfdb33e5008e30375aec273e876371761f))
     > add unit test for &#x27;foreign-object-prototype&#x27;
     > issue #34

 -  **converter**  support for 'foreign-object-prototype' ([a4f53840b37589e](https://github.com/bsorrentino/langgraph4j/commit/a4f53840b37589ea9901cdaa47084d10951587be))
     > if options compatibility&#x3D;graaljs and foreignobjectprototype&#x3D;true the
     > conversion on a java.util.List remove methods which signature clash with
     > javascript Array
     > issue #34


### Refactor

 -  **pom**  remove warning ([20f9be360dafd8b](https://github.com/bsorrentino/langgraph4j/commit/20f9be360dafd8b1984a2a688d582ed34a45f454))
   
 -  **dts**  update directory tree structure ([3e547da4219ed5c](https://github.com/bsorrentino/langgraph4j/commit/3e547da4219ed5cbba2ff6b8aa7252122f42b7d8))
   
 -  **code**  clean code ([9d1bfba4c41a272](https://github.com/bsorrentino/langgraph4j/commit/9d1bfba4c41a272e410a7db908721c47d1927709))
    > issue #34

 -  **code**  add transformer plugin ([213c383c21b7c85](https://github.com/bsorrentino/langgraph4j/commit/213c383c21b7c851890325e459bbba9ef023c523))
   

### Test 

 -  **rhino**  evaluate build-in support for commonjs modules ([4190c469ad6b6f8](https://github.com/bsorrentino/langgraph4j/commit/4190c469ad6b6f837d4e32a316ed74052613b536))
   


### ALM 

 -  bump to new version ([04469b6cebbec61](https://github.com/bsorrentino/langgraph4j/commit/04469b6cebbec61bcc7f9b7f082fbad40254b2d6))
   
 -  bump rhino version ([8d0bb5e57293956](https://github.com/bsorrentino/langgraph4j/commit/8d0bb5e57293956def1d56adcd07e33c0f5f6853))
   





<!-- "name: v1.3.1" is a release tag -->

## [v1.3.1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.3.1) (2021-10-12)











<!-- "name: v1.3.0" is a release tag -->

## [v1.3.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.3.0) (2021-09-20)











<!-- "name: v1.2.0" is a release tag -->

## [v1.2.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.2.0) (2019-09-08)











<!-- "name: v1.1.0" is a release tag -->

## [v1.1.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.1.0) (2019-02-12)











<!-- "name: v1.0.0" is a release tag -->

## [v1.0.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0.0) (2018-06-05)











<!-- "name: v1.0-rc1" is a release tag -->

## [v1.0-rc1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-rc1) (2018-05-18)











<!-- "name: v1.0-beta2" is a release tag -->

## [v1.0-beta2](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta2) (2018-05-09)











<!-- "name: v1.0-beta1" is a release tag -->

## [v1.0-beta1](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.0-beta1) (2018-04-16)











<!-- "name: v0.2.0" is a release tag -->

## [v0.2.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.2.0) (2018-03-27)











<!-- "name: v0.1.0" is a release tag -->

## [v0.1.0](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.1.0) (2018-03-04)









