/*
 * 【背景】
 * 			经典的“生产者”和“消费者”模型中，通过队列可以很便利地实现两者之间的数据共享。 
 * 			假设我们有若干生产者线程，另外又有若干个消费者线程。
 * 			如果生产者线程需要把准备好的数据共享给消费者线程，可以利用队列的方式来解决。 
 * 			但是生产者和消费者在数据处理速度可能有快慢问题，从而导致队列或满或空；
 * 			如果生产者产出数据的速度大于消费者消费的速度，队列满时，生产者必须暂停（阻塞生产者线程），反之，需要阻塞消费者
 * 			在concurrent包发布以前，在多线程环境下，程序员必须自己控制这些细节，兼顾效率和线程安全引入高复杂度。
 * 			因此BlockingQueue的必要性就体现出来了。
 * 【阻塞场景】
 * 			队列空时，阻塞消费者线程；
 *			队列满时，阻塞生产者线程；
 * 【ArrayBlockingQueue】
 * 			基于数组的阻塞队列实现
 * 			ArrayBlockingQueue内部，维护了一个定长数组，以便缓存队列中的数据对象
 * 			除了一个定长数组外，其内部还保存着两个整形变量，分别标识着队列的头部和尾部在数组中的位置。
 * 			生产者放入数据和消费者获取数据共用同一个锁对象，由此也意味着两者无法真正并行运行，这点不同于LinkedBlockingQueue；
 * 			按照实现原理来分析，ArrayBlockingQueue完全可以采用分离锁，从而实现生产者和消费者操作的完全并行运行。
 * 			Doug Lea之所以没这样去做，也许是因为ArrayBlockingQueue的数据写入和获取操作已经足够轻巧，以至于引入独立的锁机制，除了给代码带来额外的复杂性外，其在性能上完全占不到任何便宜。
 * 			ArrayBlockingQueue和LinkedBlockingQueue间还有一个明显的不同之处在于，前者在插入或删除元素时不会产生或销毁任何额外的对象实例，而后者则会生成一个额外的Node对象。
 * 			这在长时间内需要高效并发地处理大批量数据的系统中，其对于GC的影响还是存在一定的区别。
 * 			而在创建ArrayBlockingQueue时，我们还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁。
 * 【LinkedBlockingQueue】
 * 			基于链表的阻塞队列，同ArrayListBlockingQueue类似，其内部也维持着一个数据缓冲队列（该队列由一个链表构成）
 * 			当生产者往队列中放入一个数据时，队列会从生产者手中获取数据，并缓存在队列内部，而生产者立即返回；
 * 			只有当队列缓冲区达到最大值缓存容量时（LinkedBlockingQueue可以通过构造函数指定该值），才会阻塞生产者队列，直到消费者从队列中消费掉一份数据，生产者线程会被唤醒，反之对于消费者这端的处理也基于同样的原理。
 * 			而LinkedBlockingQueue之所以能够高效的处理并发数据，还因为其对于生产者端和消费者端分别采用了独立的锁来控制数据同步，这也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列的并发性能。
 * 			需要注意的是，其容量默认无限大，若不指定其容量大小，生产者的速度大于消费者的速度，在队列满之前内存可能消耗殆尽。
 * 【DelayQueue】
 * 			只有当其指定的延迟时间到了，才能够从队列中获取到该元素。
 * 			没有大小限制的队列，因此往队列中插入数据的操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻塞。
 * 			使用场景：DelayQueue使用场景较少，但都相当巧妙，常见的例子比如使用一个DelayQueue来管理一个超时未响应的连接队列。
 * 【PriorityBlockingQueue】
 * 		 	优先级的判断通过构造函数传入的Comparator对象来决定
 * 			需要注意的是PriorityBlockingQueue并不会阻塞数据生产者，而只会在没有可消费的数据时，阻塞数据的消费者。
 * 			因此使用的时候要特别注意，生产者生产数据的速度绝对不能快于消费者消费数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。
 * 			在实现PriorityBlockingQueue时，内部控制线程同步的锁采用的是公平锁。
 *【SynchronousQueue】
 *			一种无缓冲的等待队列，类似于无中介的直接交易，有点像原始社会中的生产者和消费者，生产者拿着产品去集市销售给产品的最终消费者，而消费者必须亲自去集市找到所要商品的直接生产者，如果一方没有找到合适的目标，那么对不起，大家都在集市等待。
 *			相对于有缓冲的BlockingQueue来说，少了一个中间经销商的环节（缓冲区），如果有经销商，生产者直接把产品批发给经销商，而无需在意经销商最终会将这些产品卖给那些消费者，由于经销商可以库存一部分商品，因此相对于直接交易模式，总体来说采用中间经销商的模式会吞吐量高一些（可以批量买卖）；但另一方面，又因为经销商的引入，使得产品从生产者到消费者中间增加了额外的交易环节，单个产品的及时响应性能可能会降低。
 *			声明一个SynchronousQueue有两种不同的方式，它们之间有着不太一样的行为。
 *			公平模式和非公平模式的区别:
 *					如果采用公平模式：SynchronousQueue会采用公平锁，并配合一个FIFO队列来阻塞多余的生产者和消费者，从而体系整体的公平策略；
 *					但如果是非公平模式（SynchronousQueue默认）：SynchronousQueue采用非公平锁，同时配合一个LIFO队列来管理多余的生产者和消费者，而后一种模式，如果生产者和消费者的处理速度有差距，则很容易出现饥渴的情况，即可能有某些生产者或者是消费者的数据永远都得不到处理。
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
		// 声明一个容量为10的缓存队列
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

		Producer producer1 = new Producer(queue);
		Producer producer2 = new Producer(queue);
		Producer producer3 = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// 启动线程
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);
		service.execute(consumer);

		// 执行10s
		Thread.sleep(10 * 1000);
		producer1.stop();
		producer2.stop();
		producer3.stop();

		Thread.sleep(2000);
		// 退出Executor
		service.shutdown();
	}

	static class Producer implements Runnable {

		public Producer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		public void run() {
			String data = null;
			Random rdm = new Random();

			System.out.println("启动生产者线程！");
			try {
				while (isRunning) {
					System.out.println("正在生产数据...");
					Thread.sleep(rdm.nextInt(DEFAULT_RANGE_FOR_SLEEP));

					data = "data:" + count.incrementAndGet();
					System.out.println("将数据：" + data + "放入队列...");
					if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
						System.out.println("放入数据失败：" + data);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				System.out.println("退出生产者线程！");
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
			System.out.println("启动消费者线程！");
			Random r = new Random();
			boolean isRunning = true;
			try {
				while (isRunning) {
					System.out.println("正从队列获取数据...");
					String data = queue.poll(2, TimeUnit.SECONDS);
					if (null != data) {
						System.out.println("拿到数据：" + data);
						System.out.println("正在消费数据：" + data);
						Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
					} else {
						// 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
						isRunning = false;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				System.out.println("退出消费者线程！");
			}
		}

		private BlockingQueue<String> queue;
		private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
	}
}
