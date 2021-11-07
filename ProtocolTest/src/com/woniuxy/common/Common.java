package com.woniuxy.common;

import java.util.Random;

public class Common {

	// 定义一个暂停脚本运行的方法，并且生成一个指定范围内的随机暂停时间
	public void sleepRandom(int min, int max) {
		Random myrand = new Random();
		int temp = myrand.nextInt(min);
		int time = temp + (max - min) + 1;
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 指定范围的随机数 100-220 120:0~199
	public int getRandom(int min, int max) {
		Random myrand = new Random();
		int gap = max - min;
		int temp = myrand.nextInt(gap);
		int random = temp + min;
		return random;
	}
}
