package org.jg.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {

	public static void premain(String agentArgument, Instrumentation instrumentation) {

		System.out.println("Test Java Agent");
		ClassLogger transformer = new ClassLogger();
		instrumentation.addTransformer(transformer);

	}

}
