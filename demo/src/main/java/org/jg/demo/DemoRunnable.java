package org.jg.demo;

import org.jg.demo.service.DemoService;

public class DemoRunnable implements Runnable{

	@Override
	public void run() {
		DemoService demoService = new DemoService();
		demoService.doSomething();
	}
}
