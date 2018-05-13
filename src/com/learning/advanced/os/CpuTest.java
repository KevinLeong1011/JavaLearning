package com.learning.advanced.os;

public class CpuTest {
	public static void main(String[] args){
		int cpuNums = Runtime.getRuntime().availableProcessors(); // 获取CPU数量
		System.out.println(cpuNums + "");
	}
}
