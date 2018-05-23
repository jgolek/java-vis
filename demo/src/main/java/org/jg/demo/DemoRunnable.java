package org.jg.agent.demo;

import org.jg.agent.demo.service.DemoService;

public class DemoRunnable implements Runnable{

	@Override
	public void run() {
		DemoService demoService = new DemoService();
		demoService.doSomething();
	}
}
