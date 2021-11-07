package com.woniuxy.cbt.test;

import com.woniuxy.cbt.core.Common;
import com.woniuxy.cbt.core.Reporter;

public class MainTestRunner {
	
	Common common = new Common();
	public static void main(String[] args) {
		MainTestRunner runner = new MainTestRunner();
		runner.startTest();

	}
	
	public void startTest() {
		Reporter.version = common.getConfigData("version");
		new Reporter().clearlog();
		System.out.println("测试用例执行开始……");
		long startTime = System.currentTimeMillis();
		
		AgileoneNotice notice = new AgileoneNotice();
		notice.mainTest();
		
		AgileonePropGUI prop = new AgileonePropGUI();
		prop.mainTest();
		
		AgileoneProposal proposal = new AgileoneProposal();
		proposal.mainTest();
		
		long endTime = System.currentTimeMillis();
		System.out.println("测试用例执行结束……");
		
		Reporter.duration = endTime - startTime;
		new Reporter().generateReport();
		
	}

}

