package org.jg.demo.service;

import java.util.Collection;

public class DemoService {

	
	public Object doSomething(String abc, Object o, Integer i, Collection col, Object[] array) throws InterruptedException {
		
		System.out.println("DemoService: /doSometing");
		System.out.println(new Object[] {abc, o, i, col, array});
		doSomething1();
		return new Integer(doSomething1());
		
		//Thread.sleep(600000);
	}
	
	public int doSomething1() {
		System.out.println(123);
		return 123;
	}
	
}
