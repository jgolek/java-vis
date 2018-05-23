package org.jg.agent;

import java.util.ArrayList;
import java.util.List;

public class CalledMethod {

	public final String name;
	public List<CalledMethod> childCalledMethods = new ArrayList<CalledMethod>();
	private CalledMethod lastCalledMethod;
	public final CalledMethod parentMethod;

	public CalledMethod(String nameArg, CalledMethod parentMethodArg) {
		this.name = nameArg;
		this.parentMethod = parentMethodArg;
	}

	public void add(CalledMethod calledMethod) {

		childCalledMethods.add(calledMethod);
		lastCalledMethod = calledMethod;
		
	}

	
}
