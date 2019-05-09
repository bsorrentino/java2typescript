/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@Java2TS(declare = {
		@Type(value=java.lang.Throwable.class),
		@Type(value=java.lang.RuntimeException.class, export=true),
		@Type(java.lang.System.class),

		@Type(value=java.util.Collection.class),
		@Type(value=java.util.List.class),
		@Type(value=java.util.Arrays.class, export=true),

		@Type(java.nio.file.Files.class),
		@Type(java.nio.file.Path.class),
		@Type(java.nio.file.Paths.class),
		@Type(java.nio.file.AccessMode.class),
		@Type(java.util.stream.Collector.class),

		@Type(value=java.net.URI.class, export=true),
		@Type(java.net.URL.class),

		@Type(value=java.util.concurrent.Callable.class, export=true),
		@Type(value=java.util.concurrent.ConcurrentHashMap.class, export=true),
		@Type(value=java.lang.management.MemoryType.class, export=true),

		@Type(value=java.util.concurrent.CompletableFuture.class, export=true),
		@Type(value=java.util.concurrent.Executor.class),
        
		// Member Classes
		@Type(value=java.util.Map.Entry.class),
		//@Type(value= javax.swing.text.AbstractDocument.class),
		//@Type(value= javax.swing.text.AbstractDocument.AttributeContext.class),

		@Type(value=java.util.function.Consumer.class, alias="Consumer", export=true),
		
		@Type( value=org.bsc.java2ts.JSExecutor.class, export=true)

})
package org.bsc.java2ts.jdk8;


import org.bsc.processor.annotation.Java2TS;
import org.bsc.processor.annotation.Type;
