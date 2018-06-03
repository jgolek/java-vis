package org.jg.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.Modifier;
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
		//System.out.println(className);
		CtClass etype = null;
		try {
			etype = ClassPool.getDefault().get("java.lang.Exception");
		} catch (NotFoundException e1) {
			e1.printStackTrace();
		}
		
		if(className.startsWith(this.packagePrefix) || className.startsWith("java.lang.Thread")) {

			try {
				CtClass ctClass = classPool.get(className);
			
				CtMethod[] methods = ctClass.getDeclaredMethods();
				
				for (CtMethod ctMethod : methods) {
					String methodName = className + "." + ctMethod.getName();
					//System.out.println(methodName + " Modifier: " +ctMethod.getModifiers());
					
					boolean isNotANativeMethod = !Modifier.isNative(ctMethod.getModifiers());
					boolean isAAbstractMethod  = !Modifier.isAbstract(ctMethod.getModifiers());
					
					
					if(isNotANativeMethod && isAAbstractMethod) { 
						ctMethod.insertBefore("org.jg.agent.RuntimeCallTree.enterMethod(\""+methodName+"\", $args);");
						
						CtClass returnType = ctMethod.getReturnType();
						
						if(returnType.isPrimitive() && returnType != CtClass.voidType) {
							CtPrimitiveType primitivReturnType = (CtPrimitiveType)ctMethod.getReturnType();
							
							String src = "org.jg.agent.RuntimeCallTree.leaveMethod(\""+methodName+"\","
									+ " new "+primitivReturnType.getWrapperName()+"($_));";
							ctMethod.insertAfter(src);
						}else{
							ctMethod.insertAfter("org.jg.agent.RuntimeCallTree.leaveMethod(\""+methodName+"\", $_ );");
						}
						
						ctMethod.addCatch("{ System.out.println($e); throw $e; }", etype);
					}
				}
				
				return ctClass.toBytecode();

			} catch (NotFoundException | CannotCompileException | IOException e) {
				e.printStackTrace();
				return classfileBuffer;
			} 
		}
		
		return classfileBuffer;			
		
	}
}
