/*
 * 1. 线程状态：New、Runnable、Running、Blocked、Dead
 * 		RUNNABLE-->BLOCKED的原因：
 * 			1）sleep方法
			2）等待输入原因
			3）在当前线程中调用了其他线程的join方法
			4）当访问一个对象的方法时，该方法被锁定
 */
package com.learning.advanced.threading;

public class SimpleThreadTest {

}
