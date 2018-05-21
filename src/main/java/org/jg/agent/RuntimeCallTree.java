package org.jg.agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RuntimeCallTree {
	
	static Map<String, ThreadCallTree> runtimeThreads = new HashMap();

	public static void enterMethod(String calledMethodName) {
		
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		
		ThreadCallTree currentThreadCallTree = runtimeThreads.get(currentThreadName);
		
		if(currentThreadCallTree == null) {
			ThreadCallTree threadCallTree = new ThreadCallTree(currentThreadName);
			runtimeThreads.put(threadCallTree.threadName, threadCallTree);
			currentThreadCallTree = threadCallTree;
		}
		
		currentThreadCallTree.enterMethod(calledMethodName);
	}

	public static Collection<ThreadCallTree> getThreads() {
		return runtimeThreads.values();
	}

}
