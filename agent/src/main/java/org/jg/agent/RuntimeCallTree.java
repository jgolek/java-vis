package org.jg.agent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeCallTree {
	
	static Map<String, ThreadCallTree> runtimeThreads = new HashMap();
	
	public static ThreadCallTreeWriterFactory callTreeWriterFactory = new ThreadCallTreeWriterFactory();

	public static void enterMethod(String calledMethodName) {
		
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		
		ThreadCallTree currentThreadCallTree = runtimeThreads.get(currentThreadName);
		
		if(currentThreadCallTree == null) {
			ThreadCallTreeWriter threadWriter = callTreeWriterFactory.create(currentThreadName);
			
			ThreadCallTree threadCallTree = new ThreadCallTree(currentThreadName, threadWriter);
			runtimeThreads.put(threadCallTree.threadName, threadCallTree);
			currentThreadCallTree = threadCallTree;
			
		}
		
		currentThreadCallTree.enterMethod(calledMethodName);
	}

	public static Collection<ThreadCallTree> getThreads() {
		return runtimeThreads.values();
	}

	public static void leaveMethod(String calledMethodName) {
		
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		
		ThreadCallTree currentThreadCallTree = runtimeThreads.get(currentThreadName);
		currentThreadCallTree.leaveMethod(calledMethodName);
	}
	
	public static String getString() {
		
		StringBuffer sb = new StringBuffer();
		Collection<ThreadCallTree> t = getThreads();
		
		for (ThreadCallTree threadCallTree : t) {
			sb.append("Thread: " + threadCallTree.threadName + "\n");
			
			toStringMethods(sb, threadCallTree.calledMethods, "  ");
			
		}
		
		return sb.toString();
	}

	private static void toStringMethods(StringBuffer sb, List<CalledMethod> calledMethods, String prefix) {
		for (CalledMethod calledMethod : calledMethods) {
			List<String> split = Arrays.asList((calledMethod.name.split("\\.")));
			String name = split.get(split.size() - 2) + "." + split.get(split.size() - 1) + "(...)";
			String packageName = String.join(".", split.subList(0, split.size() - 2));
			
			sb.append(prefix + name + " : " + packageName+ "\n");
			toStringMethods(sb, calledMethod.childCalledMethods, prefix + "  ");
		}
	}
}
