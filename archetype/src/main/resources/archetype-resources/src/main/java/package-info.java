 @Java2TS(declare = {
 		@Type(value=java.util.Arrays.class, export=true),

 		@Type(value=java.nio.file.Files.class, export=true),
 		@Type(java.nio.file.Path.class),
 		@Type(value=java.nio.file.Paths.class, export=true),

 		@Type(value=java.net.URI.class, export=true),
 		@Type(java.net.URL.class)

 })

package ${package};

import org.bsc.processor.annotation.Java2TS;
import org.bsc.processor.annotation.Type;

