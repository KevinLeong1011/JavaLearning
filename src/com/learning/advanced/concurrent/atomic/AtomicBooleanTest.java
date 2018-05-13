package com.learning.advanced.concurrent.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

// 可以使用 Atomic变量来实现线程同步
public class AtomicBooleanTest implements Runnable {

	private static AtomicBoolean exists = new AtomicBoolean(false);

	private String name;

	public AtomicBooleanTest(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		if (exists.compareAndSet(false, true)) {

			System.out.println(name + " enter");
			try {
				System.out.println(name + " working");
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// do nothing
			}
			System.out.println(name + " leave");
			exists.set(false);
		} else {
			System.out.println(name + " give up");
		}

	}

	public static void main(String[] args) {
		AtomicBooleanTest bar1 = new AtomicBooleanTest("bar1");
		AtomicBooleanTest bar2 = new AtomicBooleanTest("bar2");
		new Thread(bar1).start();
		new Thread(bar2).start();
	}
}
