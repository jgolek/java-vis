package org.jg.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {

	public static void premain(String packagePrefix, Instrumentation instrumentation) {

		System.out.println("Package_Prefix " + packagePrefix);
		
		System.out.println("Test Java Agent");
		ClassLogger transformer = new ClassLogger(packagePrefix);
		instrumentation.addTransformer(transformer);
	}
}
 	