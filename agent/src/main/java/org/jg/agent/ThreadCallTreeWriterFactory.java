package org.jg.agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ThreadCallTreeWriterFactory {

	public ThreadCallTreeWriter create(String timestamp, String threadName) {
		try {
			File file = new File("./runtime/"+timestamp+"_thread_"+threadName+".yaml");
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			return new ThreadCallTreeWriter(new PrintWriter(bufferedWriter));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	public ThreadCallTreeWriter create2(String threadName) {
		System.out.println("Thread " +threadName);
		return new ThreadCallTreeWriter(new PrintWriter(System.out));
	}

}
