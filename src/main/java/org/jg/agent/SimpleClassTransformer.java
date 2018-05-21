package org.jg.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SimpleClassTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, 
							String className, 
							Class<?> classBeingRedefined,
							ProtectionDomain protectionDomain, 
							byte[] classfileBuffer) throws IllegalClassFormatException {
	
	
		System.out.println(className);
		
		return classfileBuffer;
	}
}



//https://henning.kropponline.de/2015/08/02/a-java-agent-example-javaagent/
//https://github.com/gousiosg/java-callgraph/tree/master/src/main/java/gr/gousiosg/javacg/dyn
//https://blogs.sap.com/2016/03/09/java-bytecode-instrumentation-using-agent-breaking-into-java-application-at-runtime/
//https://zeroturnaround.com/rebellabs/how-to-inspect-classes-in-your-jvm/
//https://github.com/zeroturnaround/callspy
//https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html


