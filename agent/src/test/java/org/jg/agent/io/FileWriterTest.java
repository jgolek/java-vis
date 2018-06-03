package org.jg.agent.io;

import java.io.File;

import org.jg.agent.RuntimeCallTree;
import org.junit.Test;

public class FileWriterTest {
	
	
	@Test
	public void testCallTreeIsWritenToFile() throws Exception {
		
		//given
		enterMethod("{org.Test: test1} |");
		enterMethod("org.Test.test1.test1_1");
		leaveMethod("org.Test.test1.test1_1");
		leaveMethod("{org.Test: test1} |");

		enterMethod("org.Test.test2");
		enterMethod("org.Test.test2.test2_1");
		enterMethod("org.Test.test2.test2_1_1");
		leaveMethod("org.Test.test2.test2_1_1");
		enterMethod("org.Test.test2.test2_1_2");		
		leaveMethod("org.Test.test2.test2_1_2");
		leaveMethod("org.Test.test2.test2_1");
		enterMethod("org.Test.test2.test2_2");
		leaveMethod("org.Test.test2.test2_2");
		
		File outputFolder = new File("./build/tmp/runtime");
		//then
		//File expectedOutputThreadFile = new File(outputFolder, "thread_main.txt");
		
		//assertEquals(true, expectedOutputThreadFile.exists());
		//System.out.println(FileUtils.readFileToString(expectedOutputThreadFile, "UTF-8"));
	}
	
	private void enterMethod(String name) {
		RuntimeCallTree.enterMethod(name, new Object[] {});
	}

	private void leaveMethod(String name) {
		RuntimeCallTree.leaveMethod(name, new Object[] {});
	}

}
