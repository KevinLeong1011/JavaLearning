package com.learning.advanced.concurrent.atomic;

import java.util.concurrent.TimeUnit;

public class BarWorker implements Runnable {
	private static boolean exists = false;

	private String name;

	public BarWorker(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		if (!exists) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// do nothing
			}
			exists = true;
			System.out.println(name + " enter");
			try {
				System.out.println(name + " working");
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// do nothing
			}
			System.out.println(name + " leave");
			exists = false;
		} else {
			System.out.println(name + " give up");
		}
	}

	public static void main(String[] args) {
		BarWorker bar1 = new BarWorker("bar1");
		BarWorker bar2 = new BarWorker("bar2");
		new Thread(bar1).start();
		new Thread(bar2).start();
	}
}
