package org.jg.agent;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ThreadCallTreeWriter {

	PrintWriter writer;
	
	String prefix = "";
	
	public ThreadCallTreeWriter(PrintWriter writerArg) {
		this.writer = writerArg;
	}
	
	public void writeCalledMethod(CalledMethod calledMethod) {
		
		prefix = prefix + "  ";

		List<String> split = Arrays.asList((calledMethod.name.split("\\.")));
		String name = split.get(split.size() - 2) + "." + split.get(split.size() - 1) + "(...)";
		String packageName = String.join(".", split.subList(0, split.size() - 2));
		
		this.writer.write(prefix + name + " : " + packageName+ "\n");
	}

	public void leaveCalledMethod() {
		prefix = prefix.substring(0, prefix.length() - 2);
		this.writer.flush();
	}
}
