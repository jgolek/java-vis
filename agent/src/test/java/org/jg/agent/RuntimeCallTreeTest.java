package org.jg.agent;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RuntimeCallTreeTest {
	
	@Before
	public void setup() {
		RuntimeCallTree.runtimeThreads.clear();
	}
	
	@Test
	public void testEnterOneMethodCall() throws Exception {
		
		//given
		enterMethod("org.Test.test");
		
		//then
		Collection<ThreadCallTree> threads = RuntimeCallTree.getThreads();
		
		ThreadCallTree     threadCallTree = threads.iterator().next();
		List<CalledMethod> calledMethods = threadCallTree.calledMethods;
		CalledMethod       calledMethod  = calledMethods.get(0);
		
		//assertEquals("Test worker", threadCallTree.threadName);
		assertEquals("org.Test.test", calledMethod.name);
	}

	private void enterMethod(String name) {
		RuntimeCallTree.enterMethod(name, new Object[] {});
	}

	private void leaveMethod(String name) {
		RuntimeCallTree.leaveMethod(name, new Object[] {});
	}
	
	
	@Test
	public void testEnterANestedMethodCall() throws Exception {
		
		enterMethod("org.Test.test");
		enterMethod("org.Test.testInTest");
		enterMethod("org.Test.testInTestInTest");
		
		//then
		Collection<ThreadCallTree> threads = RuntimeCallTree.getThreads();
		
		ThreadCallTree     threadCallTree = threads.iterator().next();
		List<CalledMethod> calledMethods = threadCallTree.calledMethods;
		CalledMethod       calledMethod  = calledMethods.get(0);
		
		List<CalledMethod> calledChildMethods = calledMethod.childCalledMethods;
		
		CalledMethod nestedMethod       = calledChildMethods.get(0);
		CalledMethod nestedNestedMethod = nestedMethod.childCalledMethods.get(0);
		
		assertEquals("org.Test.testInTest", nestedMethod.name);	
		assertEquals("org.Test.testInTestInTest", nestedNestedMethod.name);
	}
	
	@Test
	@Ignore
	public void testEnterSequenceMethodCall() throws Exception {
		
		//given
		enterMethod("org.Test.test1");
		leaveMethod("org.Test.test1");

		enterMethod("org.Test.test2");
		leaveMethod("org.Test.test2");
		
		//then
		Collection<ThreadCallTree> threads = RuntimeCallTree.getThreads();
		
		ThreadCallTree     threadCallTree = threads.iterator().next();
		List<CalledMethod> calledMethods  = threadCallTree.calledMethods;
		CalledMethod       calledMethod1  = calledMethods.get(0);
		CalledMethod       calledMethod2  = calledMethods.get(1);

		assertEquals("org.Test.test1", calledMethod1.name);	
		assertEquals("org.Test.test2", calledMethod2.name);
	}
	
	@Test
	@Ignore
	public void testEnterSequenceNestedMethodCall() throws Exception {
		
		//given
		enterMethod("org.Test.test1");
		enterMethod("org.Test.test1.test1_1");
		leaveMethod("org.Test.test1.test1_1");
		leaveMethod("org.Test.test1");

		enterMethod("org.Test.test2");
		enterMethod("org.Test.test2.test2_1");
		enterMethod("org.Test.test2.test2_1_1");
		leaveMethod("org.Test.test2.test2_1_1");
		enterMethod("org.Test.test2.test2_1_2");		
		leaveMethod("org.Test.test2.test2_1_2");
		leaveMethod("org.Test.test2.test2_1");
		enterMethod("org.Test.test2.test2_2");
		leaveMethod("org.Test.test2.test2_2");

		
		
		//then
		Collection<ThreadCallTree> threads = RuntimeCallTree.getThreads();
		
		ThreadCallTree     threadCallTree  = threads.iterator().next();
		List<CalledMethod> calledMethods   = threadCallTree.calledMethods;
		CalledMethod       calledMethod1   = calledMethods.get(0);
		CalledMethod       calledMethod1_1 = calledMethod1.childCalledMethods.get(0);
		
		CalledMethod       calledMethod2   = calledMethods.get(1);
		CalledMethod       calledMethod2_1 = calledMethod2.childCalledMethods.get(0);
		CalledMethod       calledMethod2_2 = calledMethod2.childCalledMethods.get(1);

		CalledMethod       calledMethod2_1_1 = calledMethod2_1.childCalledMethods.get(0);
		CalledMethod       calledMethod2_1_2 = calledMethod2_1.childCalledMethods.get(1);

		
		assertEquals("org.Test.test1", calledMethod1.name);	
		assertEquals("org.Test.test1.test1_1", calledMethod1_1.name);	
		
		assertEquals("org.Test.test2", calledMethod2.name);
		assertEquals("org.Test.test2.test2_1", calledMethod2_1.name);	
		assertEquals("org.Test.test2.test2_2", calledMethod2_2.name);	
		assertEquals("org.Test.test2.test2_1_1", calledMethod2_1_1.name);	
		assertEquals("org.Test.test2.test2_1_2", calledMethod2_1_2.name);	

	}
}
