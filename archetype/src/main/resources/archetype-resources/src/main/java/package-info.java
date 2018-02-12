 @Java2TS(declare = {
 		@Type(java.lang.Iterable.class),
 		@Type(java.lang.Comparable.class),
 		@Type(java.lang.Runnable.class),
 		@Type(java.lang.System.class),


 		@Type(java.util.stream.BaseStream.class),
 		@Type(value=java.util.stream.Stream.class,export=true),
 		@Type(value=java.util.stream.Collectors.class,export=true),

 		@Type(java.util.Iterator.class),
 		@Type(java.util.Comparator.class),
 		@Type(java.util.Collection.class),
 		@Type(java.util.Map.class),
 		@Type(java.util.List.class ),
 		@Type(java.util.Set.class),
 		@Type(value=java.util.Arrays.class, export=true),
 		@Type(java.util.HashMap.class),
 		@Type(java.util.HashSet.class),
 		@Type(java.util.ArrayList.class),
 		@Type(java.util.Optional.class),

 		@Type(value=java.nio.file.Files.class, export=true),
 		@Type(java.nio.file.Path.class),
 		@Type(value=java.nio.file.Paths.class, export=true),
 		@Type(java.nio.file.AccessMode.class),

 		@Type(java.util.function.UnaryOperator.class),
 		@Type(java.util.function.Consumer.class),
 		@Type(java.util.function.Predicate.class),

 		@Type(value=java.net.URI.class, export=true),
 		@Type(java.net.URL.class)

 })

package ${package};


import org.bsc.processor.annotation.Java2TS;
import org.bsc.processor.annotation.Type;
