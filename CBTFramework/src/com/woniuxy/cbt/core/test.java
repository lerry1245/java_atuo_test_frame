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
		// 如果之前已经登录，则将其注销
		if (selenium.isElementPresent(By.linkText("个人设定"))) {
			
			System.out.println("找到元素……");
		}
		else {
			System.out.println("未找到元素，准备登陆……");
			
		}
		
	}

	
}	

