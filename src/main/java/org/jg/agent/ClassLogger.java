package org.jg.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AttributeInfo;

public class ClassLogger implements ClassFileTransformer {
	
	private ClassPool classPool;

	public ClassLogger() {
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
		
		if(className.startsWith("org.jg.agent")) {
			
			//System.out.println(className);

			try {
				CtClass ctClass = classPool.get(className);
			
				CtMethod[] methods = ctClass.getDeclaredMethods();
				
				for (CtMethod ctMethod : methods) {
					String methodName = className + "." + ctMethod.getName();
					System.out.println(methodName);
					
					
					ctMethod.insertBefore("System.out.println(\"[Inst] Entering: "+methodName+"\");");
					ctMethod.insertAfter("System.out.println(\"[Inst] Leaving: "+methodName+"\");");
				
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
