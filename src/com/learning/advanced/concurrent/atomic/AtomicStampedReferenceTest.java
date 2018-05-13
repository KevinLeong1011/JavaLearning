/*
 * 【前言】
 *  1. CAS问题
 *  		CAS是指Compare-and-swap或Compare-and-Set
 *  		CAS是一个原子操作，用于多线程环境下的同步。
 *  		它比较内存中的内容和给定的值，只有当两者相同时（说明其未被修改），才会修改内存中的内容。
 *  2. ABA问题
 *  		在多线程环境中，使用lock-free的CAS时，如果一个线程对变量修改2次，第2次修改后的值和第1次修改前的值相同，那么可能就会出现ABA问题。
 *  		假设有两个线程P1和P2，P1执行完int oldval=val后被其他线程抢占。P2线程在此期间修改了val的值（可能多次修改），但最终val的值和修改前一样。当P1线程之后运行CAS函数时，并不能发现这个问题。这就是ABA问题。
 *  3. 解决办法
 *  		一个常用的方法是添加额外的“tag”或“stamp”位来标记是指针是否被修改过。
 *  4. ABA问题实例
 *  		现有一个用单向链表实现的堆栈，栈顶为A，这时线程T1已经知道A.next为B，然后希望用CAS将栈顶替换为B。
 *  		在T1执行上面这条指令之前，线程T2介入，将A、B出栈，再pushD、C、A，而对象B此时处于游离状态。
 *  		此时轮到线程T1执行CAS操作，检测发现栈顶仍为A，所以CAS成功，栈顶变为B，但实际上B.next为null。
 *  		最终结果是，堆栈中只有了B，而C和D不再存在于堆栈中，平白无故就被丢弃了。
 */
package com.learning.advanced.concurrent.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {
	private static AtomicInteger atomicInt = new AtomicInteger(100);
      private static AtomicStampedReference atomicStampedRef = new AtomicStampedReference(100, 0);

      public static void main(String[] args) throws InterruptedException {
              Thread intT1 = new Thread(new Runnable() {
                      @Override
                      public void run() {
                              atomicInt.compareAndSet(100, 101);
                              atomicInt.compareAndSet(101, 100);
                      }
              });

              Thread intT2 = new Thread(new Runnable() {
                      @Override
                      public void run() {
                              try {
                                      TimeUnit.SECONDS.sleep(1);
                              } catch (InterruptedException e) {
                              }
                              boolean c3 = atomicInt.compareAndSet(100, 101);
                              System.out.println(c3); // true
                      }
              });

              intT1.start();
              intT2.start();
              intT1.join();
              intT2.join();

              Thread refT1 = new Thread(new Runnable() {
                      @Override
                      public void run() {
                              try {
                                      TimeUnit.SECONDS.sleep(1);
                              } catch (InterruptedException e) {
                              }
                              atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
                              atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
                      }
              });

              Thread refT2 = new Thread(new Runnable() {
                      @Override
                      public void run() {
                              int stamp = atomicStampedRef.getStamp();
                              try {
                                      TimeUnit.SECONDS.sleep(2);
                              } catch (InterruptedException e) {
                              }
                              boolean c3 = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
                              System.out.println(c3); // false
                      }
              });

              refT1.start();
              refT2.start();
      }
}
