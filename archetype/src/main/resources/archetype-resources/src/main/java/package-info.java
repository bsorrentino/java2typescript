 @Java2TS(declare = {	
		// Declare class(s)
		@Type(java.nio.file.Path.class),
		@Type(value=java.net.URI.class, export=true),
		@Type(java.net.URL.class),
		 
		// Utility Class(s)
		@Type(value=java.nio.file.Paths.class, export=true),
		@Type(value=java.util.Arrays.class, export=true),
 		@Type(value=java.nio.file.Files.class, export=true),

		 // Member Class(s)
		@Type(value=java.util.Map.Entry.class),

		// Native functional Interface(s)
		@Type(value=java.util.concurrent.Callable.class),

		// Declare as Functional Interface


 })

package ${package};

import org.bsc.processor.annotation.Java2TS;
import org.bsc.processor.annotation.Type;

