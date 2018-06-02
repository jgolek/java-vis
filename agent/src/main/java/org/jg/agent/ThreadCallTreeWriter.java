package org.jg.agent;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ThreadCallTreeWriter {

	PrintWriter writer;
	
	String prefix = "";
	
	long writtenMethods = 0;
	
	public ThreadCallTreeWriter(PrintWriter writerArg) {
		this.writer = writerArg;
	}
	
	public void writeCalledMethod(CalledMethod calledMethod) {

		List<String> split = Arrays.asList((calledMethod.name.split("\\.")));
		String name = split.get(split.size() - 2) + "." + split.get(split.size() - 1) + "()";
		String packageName = String.join(".", split.subList(0, split.size() - 2));
		//this.writer.write(prefix + name + " : " + packageName+ "\n");
		
		this.writer.write(prefix + "-\n");
		this.writer.write(prefix + "  name: \""+name+"\"\n");
		this.writer.write(prefix + "  startTime: "+System.currentTimeMillis()+"\n");
		this.writer.write(prefix + "  children: \n");
		
		prefix = prefix + "  ";
		writtenMethods = writtenMethods + 1;
	}

	public void leaveCalledMethod() {
		prefix = prefix.substring(0, prefix.length() - 2);
		this.writer.flush();
	}

	public void close() {
		this.writer.write(prefix + "  endTime: "+System.currentTimeMillis()+"\n");
		this.writer.write(prefix + "  calledMethods: "+writtenMethods+"\n");
		this.writer.close();
	}
}
