package com.woniuxy.bet;

import com.woniuxy.common.Common;

public class MainTestRunner {
	
	Common common = new Common();
	public static void main(String[] args) {
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", "192.168.1.40");
		System.setProperty("http.proxyPort", "8888");
		MainTestRunner runner = new MainTestRunner();
		runner.startTest();

	}
	
	public void startTest() {	
		while (true) {
			System.out.println("测试用例执行开始……");
			
			long startTime = System.currentTimeMillis();
		
			OfficeBet ob = new OfficeBet();
			ob.mainTest();
			
			CrebitBet cb = new CrebitBet(); 
			cb.mainTest();
			
			long endTime = System.currentTimeMillis();
			System.out.println("测试用例执行结束……");
			
		}				
	}

}

