package org.jg.agent.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.jg.agent.RuntimeCallTree;
import org.jg.agent.ThreadCallTreeWriter;
import org.jg.agent.ThreadCallTreeWriterFactory;
import org.junit.Test;

public class FileWriterTest {
	
	
	@Test
	public void testCallTreeIsWritenToFile() throws Exception {
		
		//given
		
		RuntimeCallTree.enterMethod("org.Test.test1");
		RuntimeCallTree.enterMethod("org.Test.test1.test1_1");
		RuntimeCallTree.leaveMethod();
		RuntimeCallTree.leaveMethod();

		RuntimeCallTree.enterMethod("org.Test.test2");
		RuntimeCallTree.enterMethod("org.Test.test2.test2_1");
		RuntimeCallTree.enterMethod("org.Test.test2.test2_1_1");
		RuntimeCallTree.leaveMethod();
		RuntimeCallTree.enterMethod("org.Test.test2.test2_1_2");		
		RuntimeCallTree.leaveMethod();
		RuntimeCallTree.leaveMethod();
		RuntimeCallTree.enterMethod("org.Test.test2.test2_2");
		RuntimeCallTree.leaveMethod();
		
		File outputFolder = new File("./build/tmp/runtime");
		//then
		File expectedOutputThreadFile = new File(outputFolder, "thread_main.txt");
		
		assertEquals(true, expectedOutputThreadFile.exists());
		
		System.out.println(FileUtils.readFileToString(expectedOutputThreadFile, "UTF-8"));
		
	}

}
