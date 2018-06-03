package org.jg.demo;

public class DemoMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running Demo Application");
	
		Thread thread1 = new Thread(new DemoRunnable(), "thread1");
		thread1.run();
		

		Thread thread2 = new Thread(new DemoRunnable(), "thread2");
		thread2.run();
		
	}
}
