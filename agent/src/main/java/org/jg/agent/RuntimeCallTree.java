package org.jg.agent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeCallTree {
	
	static Map<String, ThreadCallTree> runtimeThreads = new HashMap();
	
	public static ThreadCallTreeWriterFactory callTreeWriterFactory = new ThreadCallTreeWriterFactory();

	private static ThreadCallTreeWriter threadWriter;

	public static void enterMethod(String calledMethodName, Object[] arguments) {
		
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		
		ThreadCallTree currentThreadCallTree = runtimeThreads.get(currentThreadName);
		
		if(currentThreadCallTree == null) {
			
			DateFormat df = new SimpleDateFormat("HHmmss_SS");
			String timestamp = df.format(new Date());
			threadWriter = callTreeWriterFactory.create(timestamp, currentThreadName);
			
			ThreadCallTree threadCallTree = new ThreadCallTree(currentThreadName, threadWriter);
			runtimeThreads.put(threadCallTree.threadName, threadCallTree);
			currentThreadCallTree = threadCallTree;
			
		}
		
		currentThreadCallTree.enterMethod(calledMethodName, arguments);
	}

	public static Collection<ThreadCallTree> getThreads() {
		return runtimeThreads.values();
	}

	public static void leaveMethod(String calledMethodName, Object returnValue) {
		
		Thread currentThread = Thread.currentThread();
		String currentThreadName = currentThread.getName();
		
		ThreadCallTree currentThreadCallTree = runtimeThreads.get(currentThreadName);
		CalledMethod lastCalledMethod = currentThreadCallTree.leaveMethod(calledMethodName, returnValue);
		if(lastCalledMethod == null) {
			runtimeThreads.remove(currentThreadName);
			threadWriter.close();
		}
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
