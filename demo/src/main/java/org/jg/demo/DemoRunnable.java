package org.jg.demo;

import java.util.ArrayList;

import org.jg.demo.service.DemoService;

public class DemoRunnable implements Runnable{

	@Override
	public void run() {
		DemoService demoService = new DemoService();
		try {
			demoService.doSomething("fooBar", new Object(), 4711, new ArrayList<>(), new Integer[] {1,2,3} );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
