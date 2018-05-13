package com.learning.advanced.concurrent;

import java.util.concurrent.*;

public class CallableTest {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool(); // Executors��һ�������࣬���ڴ�����������ExecutorService�̳߳�
//		1. newCachedThreadPool ����һ���ɻ����̳߳أ�����̳߳س��ȳ���������Ҫ���������տ����̣߳����޿ɻ��գ����½��̡߳�
//		2. newFixedThreadPool ����һ�������̳߳أ��ɿ����߳���󲢷������������̻߳��ڶ����еȴ���
//		3. newScheduledThreadPool ����һ�������̳߳أ�֧�ֶ�ʱ������������ִ�С�
//		4. newSingleThreadExecutor ����һ�����̻߳����̳߳أ���ֻ����Ψһ�Ĺ����߳���ִ�����񣬱�֤����������ָ��˳��(FIFO, LIFO, ���ȼ�)ִ�С�
		Task task = new Task();
		Future<Integer> result = executor.submit(task);
		executor.shutdown();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("���߳���ִ������");

		try {
			System.out.println("task���н��" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("��������ִ�����");
	}

	static class Task implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			System.out.println("���߳��ڽ��м���");
			Thread.sleep(3000);
			int sum = 0;
			for (int i = 0; i < 100; i++)
				sum += i;
			return sum;
		}
	}
}
