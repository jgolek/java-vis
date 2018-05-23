package org.jg.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ClassLogger implements ClassFileTransformer {
	
	private ClassPool classPool;
	private String packagePrefix;

	public ClassLogger(String packagePrefix) {
        this.packagePrefix = packagePrefix;
		this.classPool = ClassPool.getDefault();
	}
	
	@Override
	public byte[] transform(ClassLoader      loader, 
							String           classNamePath, 
							Class<?>         classBeingRedefined,
							ProtectionDomain protectionDomain, 
							byte[]           classfileBuffer
							) throws IllegalClassFormatException 
	{

		String className = classNamePath.replace("/", ".");
		
		if(className.startsWith(this.packagePrefix) || className.startsWith("java.lang.Thread")) {

			try {
				CtClass ctClass = classPool.get(className);
			
				CtMethod[] methods = ctClass.getDeclaredMethods();
				
				for (CtMethod ctMethod : methods) {
					String methodName = className + "." + ctMethod.getName();
					System.out.println(methodName);
					
					ctMethod.insertBefore("org.jg.agent.RuntimeCallTree.enterMethod(\""+methodName+"\");");
					ctMethod.insertAfter("org.jg.agent.RuntimeCallTree.leaveMethod();");
				}
				
				return ctClass.toBytecode();

			} catch (NotFoundException | CannotCompileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return classfileBuffer;
			} 
		}
		
		return classfileBuffer;			
		
	}
}
