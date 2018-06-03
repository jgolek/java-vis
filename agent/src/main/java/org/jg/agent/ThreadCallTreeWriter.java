package org.jg.agent;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

public class ThreadCallTreeWriter {

	
	PrintWriter writer;
	
	String prefix = "";
	
	long writtenMethods = 0;

	Yaml yaml;
	
	public ThreadCallTreeWriter(PrintWriter writerArg) {
		this.writer = writerArg;
		
		DumperOptions ops = new DumperOptions();
		this.yaml = new Yaml(ops);
	}
	
	public void writeCalledMethod(CalledMethod calledMethod, Object[] arguments) {

		List<String> split = Arrays.asList((calledMethod.name.split("\\.")));
		String name = split.get(split.size() - 2) + "." + split.get(split.size() - 1) + "()";
		String packageName = String.join(".", split.subList(0, split.size() - 2));
		//this.writer.write(prefix + name + " : " + packageName+ "\n");
		
		this.writer.write(prefix + "-\n");
		
		writeValue("name",      	name);
		writeValue("startTime", 	System.currentTimeMillis());
		writeValue("packageName",   packageName);
		writeArray("arguments",     arguments);
		this.writer.write(prefix + "  children: \n");
		
		prefix = prefix + "  ";
		writtenMethods = writtenMethods + 1;
	}
	
	public void leaveCalledMethod(Object returnValue) {
		prefix = prefix.substring(0, prefix.length() - 2);
		
		writeReturnValue(returnValue);
		
		this.writer.flush();
	}

	private void writeReturnValue(Object returnValue) {
		//writeValue("returnValue", null);
		//
		//if(returnValue != null) {
		//	prefix = prefix + "  ";
		//	writeObject(returnValue);
		//	prefix = prefix.substring(0, prefix.length() - 2);
		//}
	}

	public void close() {
		writeValue("endTime", System.currentTimeMillis());
		writeValue("calledMethods", writtenMethods);
		this.writer.close();
	}

	private void writeArray(String name, Object[] arguments) {
		this.writer.write(prefix + "  "+name+": \n");
		prefix = prefix + "  ";
		for (Object arg : arguments) {
			this.writer.write(prefix + "-\n");
			writeObject(arg);
		}
		prefix = prefix.substring(0, prefix.length() - 2);
	}

	private void writeObject(Object arg) {
		
		if(arg == null) {
			//writeValue("type",  "Unkown");
			writeValue("value", "null");
		}else {
			//writeValue("type", arg.getClass().getSimpleName());
			
			if(arg.getClass().isArray()) {
				if("String[]".equals(arg.getClass().getSimpleName())) {
					String[] arg2 = (String[])arg;
					if(arg2.length < 10) {
						writeValue("value", Arrays.toString(arg2));
					}else{
						writeValue("value", "TO_MANY_VALUES");
					}
				}
				
				//List<Object> asList = Arrays.asList(arg);
				writeValue("value", "NOT_SUPPORTED");
			}else {
				writeValue("value", arg);
			}
		}
		
	}

	
	private void writeValue(String argName, Object value) {
		String valueOf = yaml.dumpAs(String.valueOf(value), Tag.STR, FlowStyle.FLOW).replaceAll("\n", "");
		if(valueOf.startsWith("|")) {
			//System.out.println(value);
			//System.out.println(String.valueOf(value));
		}else {
			this.writer.write(prefix + "  "+argName+": "+ valueOf + "\n");
		}
	}

}
