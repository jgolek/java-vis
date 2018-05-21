package org.jg.agent;

import java.util.ArrayList;
import java.util.List;

public class ThreadCallTree {

	public final String threadName;

	public final List<CalledMethod> calledMethods = new ArrayList<CalledMethod>();
	
	public ThreadCallTree(String nameArg) {
		this.threadName = nameArg;
	}

	public void enterMethod(String calledMethodName) {
		
		CalledMethod calledMethod = new CalledMethod(calledMethodName);
		
		calledMethods.add(calledMethod);
	}
}
