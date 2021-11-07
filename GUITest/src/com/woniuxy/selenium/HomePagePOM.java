package com.woniuxy.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.woniuxy.core.Common;

public class HomePagePOM {
	private WebDriver driver;
	Common common = new Common();
	
	public WebElement getUsername() {
		return driver.findElement(By.id("username"));
	}
	
	public WebElement getPassword() {
		return driver.findElement(By.id("password"));
	}
	
	public WebElement getSavelogin() {
		return driver.findElement(By.id("savelogin"));
	}
	
	public WebElement getLoginBtn() {
		return driver.findElement(By.id("login"));
	}
	
	public void Login(String username, String password, boolean ISsavelogin) {
		this.getUsername().clear();
		this.getUsername().sendKeys(username);
		this.getPassword().clear();
		this.getPassword().sendKeys(username);
		if(!ISsavelogin) {
			this.getSavelogin();
		}
		this.getSavelogin();
	}
	
	public void testLogin() {
		this.Login("admin", "admin", true);
		//断言
		if(common.isElementExist(driver, By.partialLinkText("个人设定"))) {
			System.out.println("登录成功");
		}
		else {
			System.out.println("登录成功");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) {
		

	}
	// 初始化环境
		public void init() {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
			// 全局设置，当元素识别不到的时候，可以接受的最长等待时间
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// 全局设置，页面加载最长等待时间。
			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			// 全局设置，关于JavaScript代码异步处理的超时时间。ajax请求。
			driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
			// 全局设置，窗口最大化
			driver.manage().window().maximize();
			// 浏览器行为模拟
//			driver.navigate().back();
//			driver.navigate().equals(null);
//			driver.navigate().refresh();

			// 打开agoleone首页
			driver.get("http://localhost:8081/agileone/");
		}

		// 完成后的扫尾工作
		public void end() {

			driver.quit();
		}


}
