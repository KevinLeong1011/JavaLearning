package com.learning.advanced.os;

public class CpuTest {
	public static void main(String[] args){
		int cpuNums = Runtime.getRuntime().availableProcessors(); // ��ȡCPU����
		System.out.println(cpuNums + "");
	}
}
