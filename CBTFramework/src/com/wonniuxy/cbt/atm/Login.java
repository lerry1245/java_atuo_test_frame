package com.wonniuxy.cbt.atm;

import java.io.Closeable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login {

	//定义WebDriver实例，用于当前类的测试
	private Common common = new Common();
	private WebDriver driver = common.getWebDriver();
	
	public void mianTest() {
		try {
			this.prepare();
			this.testLoginFail();
			this.testLoginPass();
			//this.finish();
		}catch (Exception e) {
			// TODO: handle exception
		}
		}
		
		
	
	//测试执行前的准备工作
	private void prepare() {
		driver.get("http://localhost/agileone/index.php");
	}
	
	//测试完成后的收尾工作
	private void finish() {
		common.closeBrowser();
		
	}
	
	//ACtion组件实现登录操作
	public void doLogin(String username, String password) {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login")).click();
		common.sleepRandom(3, 5);	
	}
	
	//Test组件测试登录失败情况
	public void testLoginFail() {
		this.doLogin("admin", "wrongpwd");
		String msg = driver.findElement(By.id("msg")).getText();
		common.sleepRandom(1, 2);	
		if(msg.contains("出错啦: 密码输入错误 ...")) {
			System.out.println("登录密码错误验证-成功");
		}
		else {
			System.out.println("登录密码错误验证-失败");
		}
		
	}
	
	//Test组件测试登录失败情况
	public void testLoginPass() {
		this.doLogin("admin", "admin");
		String msg = driver.findElement(By.xpath("//a[text()='个人设定']")).getText();
		if(msg.contains("个人设定")) {
			System.out.println("标准登录验证-成功");
		}
		else {
			System.out.println("标准登录验证-失败");
		}
		
	}
}
