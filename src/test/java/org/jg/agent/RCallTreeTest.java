package org.jg.agent;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class RCallTreeTest {
	
	@Test
	public void testEnterOneMethods() throws Exception {
		
		//given
		RuntimeCallTree.enterMethod("org.Test.test");
		
		//then
		Collection<ThreadCallTree> threads = RuntimeCallTree.getThreads();
		
		ThreadCallTree     threadCallTree = threads.iterator().next();
		List<CalledMethod> calledMethods = threadCallTree.calledMethods;
		CalledMethod       calledMethod  = calledMethods.get(0);
		
		assertEquals("main", threadCallTree.threadName);
		assertEquals("org.Test.test", calledMethod.name);
	}
}
