package org.jg.agent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ThreadCallTreeWriterFactory {

	public ThreadCallTreeWriter create(String threadName) {
		try {
			File file = new File("./runtime/thread_"+threadName+".txt");
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			return new ThreadCallTreeWriter(new PrintWriter(file));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	public ThreadCallTreeWriter create2(String threadName) {
		System.out.println("Thread " +threadName);
		return new ThreadCallTreeWriter(new PrintWriter(System.out));
	}

}