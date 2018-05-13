/*
 * ��ǰ�ԡ�
 *  1. CAS����
 *  		CAS��ָCompare-and-swap��Compare-and-Set
 *  		CAS��һ��ԭ�Ӳ��������ڶ��̻߳����µ�ͬ����
 *  		���Ƚ��ڴ��е����ݺ͸�����ֵ��ֻ�е�������ͬʱ��˵����δ���޸ģ����Ż��޸��ڴ��е����ݡ�
 *  2. ABA����
 *  		�ڶ��̻߳����У�ʹ��lock-free��CASʱ�����һ���̶߳Ա����޸�2�Σ���2���޸ĺ��ֵ�͵�1���޸�ǰ��ֵ��ͬ����ô���ܾͻ����ABA���⡣
 *  		�����������߳�P1��P2��P1ִ����int oldval=val�������߳���ռ��P2�߳��ڴ��ڼ��޸���val��ֵ�����ܶ���޸ģ���������val��ֵ���޸�ǰһ������P1�߳�֮������CAS����ʱ�������ܷ���������⡣�����ABA���⡣
 *  3. ����취
 *  		һ�����õķ�������Ӷ���ġ�tag����stamp��λ�������ָ���Ƿ��޸Ĺ���
 *  4. ABA����ʵ��
 *  		����һ���õ�������ʵ�ֵĶ�ջ��ջ��ΪA����ʱ�߳�T1�Ѿ�֪��A.nextΪB��Ȼ��ϣ����CAS��ջ���滻ΪB��
 *  		��T1ִ����������ָ��֮ǰ���߳�T2���룬��A��B��ջ����pushD��C��A��������B��ʱ��������״̬��
 *  		��ʱ�ֵ��߳�T1ִ��CAS��������ⷢ��ջ����ΪA������CAS�ɹ���ջ����ΪB����ʵ����B.nextΪnull��
 *  		���ս���ǣ���ջ��ֻ����B����C��D���ٴ����ڶ�ջ�У�ƽ���޹ʾͱ������ˡ�
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
