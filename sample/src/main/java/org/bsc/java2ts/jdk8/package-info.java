/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@Java2TS(declare = {
		@Type(java.lang.System.class),

		@Type(value=java.util.List.class),
		@Type(value=java.util.Arrays.class, export=true),

		@Type(java.nio.file.Files.class),
		@Type(java.nio.file.Path.class),
		@Type(java.nio.file.Paths.class),
		@Type(java.nio.file.AccessMode.class),


		@Type(value=java.net.URI.class, export=true),
		@Type(java.net.URL.class),

		@Type(value=java.util.concurrent.Callable.class, alias="Callable"),

		// Member Classes
		@Type(value=java.util.Map.Entry.class),
		//@Type(value= javax.swing.text.AbstractDocument.class),
		//@Type(value= javax.swing.text.AbstractDocument.AttributeContext.class),



})
package org.bsc.java2ts.jdk8;


import org.bsc.processor.annotation.Java2TS;
import org.bsc.processor.annotation.Type;
