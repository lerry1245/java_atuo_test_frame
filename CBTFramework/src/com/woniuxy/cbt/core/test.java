package com.woniuxy.cbt.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class test {
	private SeleniumDriver selenium = new SeleniumDriver("ie");
	private WebDriver driver = selenium.getWebDriver();
	
	
	
	public static void main(String[] args) {
		test t = new test();
		t.prepare();
		
	}
	private void prepare() {
		
		driver.get("http://localhost/agileone/");
		// ���֮ǰ�Ѿ���¼������ע��
		if (selenium.isElementPresent(By.linkText("�����趨"))) {
			
			System.out.println("�ҵ�Ԫ�ء���");
		}
		else {
			System.out.println("δ�ҵ�Ԫ�أ�׼����½����");
			
		}
		
	}

	
}	

