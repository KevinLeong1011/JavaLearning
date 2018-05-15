/*
 * ��������
 * 			����ġ������ߡ��͡������ߡ�ģ���У�ͨ�����п��Ժܱ�����ʵ������֮������ݹ��� 
 * 			���������������������̣߳������������ɸ��������̡߳�
 * 			����������߳���Ҫ��׼���õ����ݹ�����������̣߳��������ö��еķ�ʽ������� 
 * 			���������ߺ������������ݴ����ٶȿ����п������⣬�Ӷ����¶��л�����գ�
 * 			��������߲������ݵ��ٶȴ������������ѵ��ٶȣ�������ʱ�������߱�����ͣ�������������̣߳�����֮����Ҫ����������
 * 			��concurrent��������ǰ���ڶ��̻߳����£�����Ա�����Լ�������Щϸ�ڣ����Ч�ʺ��̰߳�ȫ����߸��Ӷȡ�
 * 			���BlockingQueue�ı�Ҫ�Ծ����ֳ����ˡ�
 * ������������
 * 			���п�ʱ�������������̣߳�
 *			������ʱ�������������̣߳�
 * ��ArrayBlockingQueue��
 * 			�����������������ʵ��
 * 			ArrayBlockingQueue�ڲ���ά����һ���������飬�Ա㻺������е����ݶ���
 * 			����һ�����������⣬���ڲ����������������α������ֱ��ʶ�Ŷ��е�ͷ����β���������е�λ�á�
 * 			�����߷������ݺ������߻�ȡ���ݹ���ͬһ���������ɴ�Ҳ��ζ�������޷������������У���㲻ͬ��LinkedBlockingQueue��
 * 			����ʵ��ԭ����������ArrayBlockingQueue��ȫ���Բ��÷��������Ӷ�ʵ�������ߺ������߲�������ȫ�������С�
 * 			Doug Lea֮����û����ȥ����Ҳ������ΪArrayBlockingQueue������д��ͻ�ȡ�����Ѿ��㹻���ɣ���������������������ƣ����˸������������ĸ������⣬������������ȫռ�����κα��ˡ�
 * 			ArrayBlockingQueue��LinkedBlockingQueue�仹��һ�����ԵĲ�֮ͬ�����ڣ�ǰ���ڲ����ɾ��Ԫ��ʱ��������������κζ���Ķ���ʵ�����������������һ�������Node����
 * 			���ڳ�ʱ������Ҫ��Ч�����ش�����������ݵ�ϵͳ�У������GC��Ӱ�컹�Ǵ���һ��������
 * 			���ڴ���ArrayBlockingQueueʱ�����ǻ����Կ��ƶ�����ڲ����Ƿ���ù�ƽ����Ĭ�ϲ��÷ǹ�ƽ����
 * ��LinkedBlockingQueue��
 * 			����������������У�ͬArrayListBlockingQueue���ƣ����ڲ�Ҳά����һ�����ݻ�����У��ö�����һ�������ɣ�
 * 			���������������з���һ������ʱ�����л�����������л�ȡ���ݣ��������ڶ����ڲ������������������أ�
 * 			ֻ�е����л������ﵽ���ֵ��������ʱ��LinkedBlockingQueue����ͨ�����캯��ָ����ֵ�����Ż����������߶��У�ֱ�������ߴӶ��������ѵ�һ�����ݣ��������̻߳ᱻ���ѣ���֮������������˵Ĵ���Ҳ����ͬ����ԭ��
 * 			��LinkedBlockingQueue֮�����ܹ���Ч�Ĵ��������ݣ�����Ϊ����������߶˺������߶˷ֱ�����˶�����������������ͬ������Ҳ��ζ���ڸ߲���������������ߺ������߿��Բ��еز��������е����ݣ��Դ�������������еĲ������ܡ�
 * 			��Ҫע����ǣ�������Ĭ�����޴�����ָ����������С�������ߵ��ٶȴ��������ߵ��ٶȣ��ڶ�����֮ǰ�ڴ�������Ĵ�����
 * ��DelayQueue��
 * 			ֻ�е���ָ�����ӳ�ʱ�䵽�ˣ����ܹ��Ӷ����л�ȡ����Ԫ�ء�
 * 			û�д�С���ƵĶ��У�����������в������ݵĲ����������ߣ���Զ���ᱻ��������ֻ�л�ȡ���ݵĲ����������ߣ��Żᱻ������
 * 			ʹ�ó�����DelayQueueʹ�ó������٣������൱������������ӱ���ʹ��һ��DelayQueue������һ����ʱδ��Ӧ�����Ӷ��С�
 * ��PriorityBlockingQueue��
 * 		 	���ȼ����ж�ͨ�����캯�������Comparator����������
 * 			��Ҫע�����PriorityBlockingQueue�������������������ߣ���ֻ����û�п����ѵ�����ʱ���������ݵ������ߡ�
 * 			���ʹ�õ�ʱ��Ҫ�ر�ע�⣬�������������ݵ��ٶȾ��Բ��ܿ����������������ݵ��ٶȣ�����ʱ��һ���������պľ����еĿ��ö��ڴ�ռ䡣
 * 			��ʵ��PriorityBlockingQueueʱ���ڲ������߳�ͬ���������õ��ǹ�ƽ����
 *��SynchronousQueue��
 *			һ���޻���ĵȴ����У����������н��ֱ�ӽ��ף��е���ԭʼ����е������ߺ������ߣ����������Ų�Ʒȥ�������۸���Ʒ�����������ߣ��������߱�������ȥ�����ҵ���Ҫ��Ʒ��ֱ�������ߣ����һ��û���ҵ����ʵ�Ŀ�꣬��ô�Բ��𣬴�Ҷ��ڼ��еȴ���
 *			������л����BlockingQueue��˵������һ���м侭���̵Ļ��ڣ���������������о����̣�������ֱ�ӰѲ�Ʒ�����������̣����������⾭�������ջὫ��Щ��Ʒ������Щ�����ߣ����ھ����̿��Կ��һ������Ʒ����������ֱ�ӽ���ģʽ��������˵�����м侭���̵�ģʽ����������һЩ����������������������һ���棬����Ϊ�����̵����룬ʹ�ò�Ʒ�������ߵ��������м������˶���Ľ��׻��ڣ�������Ʒ�ļ�ʱ��Ӧ���ܿ��ܻή�͡�
 *			����һ��SynchronousQueue�����ֲ�ͬ�ķ�ʽ������֮�����Ų�̫һ������Ϊ��
 *			��ƽģʽ�ͷǹ�ƽģʽ������:
 *					������ù�ƽģʽ��SynchronousQueue����ù�ƽ���������һ��FIFO��������������������ߺ������ߣ��Ӷ���ϵ����Ĺ�ƽ���ԣ�
 *					������Ƿǹ�ƽģʽ��SynchronousQueueĬ�ϣ���SynchronousQueue���÷ǹ�ƽ����ͬʱ���һ��LIFO�������������������ߺ������ߣ�����һ��ģʽ����������ߺ������ߵĴ����ٶ��в�࣬������׳��ּ��ʵ��������������ĳЩ�����߻����������ߵ�������Զ���ò�������
 */
package com.learning.advanced.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueTest {
	public static void main(String[] args) throws InterruptedException {
		// ����һ������Ϊ10�Ļ������
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

		Producer producer1 = new Producer(queue);
		Producer producer2 = new Producer(queue);
		Producer producer3 = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		// ����Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// �����߳�
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);
		service.execute(consumer);

		// ִ��10s
		Thread.sleep(10 * 1000);
		producer1.stop();
		producer2.stop();
		producer3.stop();

		Thread.sleep(2000);
		// �˳�Executor
		service.shutdown();
	}

	static class Producer implements Runnable {

		public Producer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		public void run() {
			String data = null;
			Random rdm = new Random();

			System.out.println("�����������̣߳�");
			try {
				while (isRunning) {
					System.out.println("������������...");
					Thread.sleep(rdm.nextInt(DEFAULT_RANGE_FOR_SLEEP));

					data = "data:" + count.incrementAndGet();
					System.out.println("�����ݣ�" + data + "�������...");
					if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
						System.out.println("��������ʧ�ܣ�" + data);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				System.out.println("�˳��������̣߳�");
			}
		}

		public void stop() {
			isRunning = false;
		}

		private volatile boolean isRunning = true;
		private BlockingQueue<String> queue;
		private static AtomicInteger count = new AtomicInteger();
		private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
	}

	static class Consumer implements Runnable {

		public Consumer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		public void run() {
			System.out.println("�����������̣߳�");
			Random r = new Random();
			boolean isRunning = true;
			try {
				while (isRunning) {
					System.out.println("���Ӷ��л�ȡ����...");
					String data = queue.poll(2, TimeUnit.SECONDS);
					if (null != data) {
						System.out.println("�õ����ݣ�" + data);
						System.out.println("�����������ݣ�" + data);
						Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
					} else {
						// ����2s��û���ݣ���Ϊ���������̶߳��Ѿ��˳����Զ��˳������̡߳�
						isRunning = false;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				System.out.println("�˳��������̣߳�");
			}
		}

		private BlockingQueue<String> queue;
		private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
	}
}
