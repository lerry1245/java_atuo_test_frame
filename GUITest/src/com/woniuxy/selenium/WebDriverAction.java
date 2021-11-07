package com.woniuxy.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class WebDriverAction {

	private WebDriver driver =null;
	private Actions  actions = null; 

	public static void main(String[] args) {
		WebDriverAction ada = new  WebDriverAction();
		ada.init();
		ada.testLogin();
		ada.end();

	}

	// 初始化环境
	public void init() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		driver = new ChromeDriver();
//		System.setProperty("D:\\Program Files\\MozillaFirefox\\firefox.exe", "drivers/geckodriver.exe");
//		driver = new FirefoxDriver();
		
		// 全局设置，当元素识别不到的时候，可以接受的最长等待时间
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// 全局设置，页面加载最长等待时间。
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		// 全局设置，关于JavaScript代码异步处理的超时时间。ajax请求。
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		// 全局设置，窗口最大化
		driver.manage().window().maximize();
		
		actions = new Actions(driver);

		// 打开agoleone首页
		driver.get("http://localhost:8081/agileone/");

	}
	public void testLogin() {
//		driver.findElement(By.id("username")).clear();	
//		actions.sendKeys(driver.findElement(By.id("username")), "admin").perform();
		
		actions.moveToElement(driver.findElement(By.id("username"))).perform();
		actions.click().perform();
		actions.sendKeys("admin").perform();
		driver.findElement(By.id("password")).clear();		
		actions.sendKeys(driver.findElement(By.id("password")), "admin").perform();
		actions.click(driver.findElement(By.id("login"))).perform();
		this.sleep(2);
		//actions.doubleClick(driver.findElement(By.partialLinkText("需求提案"))).perform();
		actions.contextClick(driver.findElement(By.partialLinkText("英文版"))).perform();
//		actions.moveToElement(driver.findElement(By.partialLinkText("需求提案"))).perform();
//		this.sleep(1);
//		actions.contextClick().perform();
		//this.sleep(2);
		actions.sendKeys("T").sendKeys(Keys.NULL).perform();   //利用快捷键T打开新窗口
		this.sleep(5);
		
		//利用两次向下箭头和一次回车键，实现新窗口打开链接
		actions.sendKeys(Keys.ARROW_DOWN).perform();
		this.sleep(2);
		actions.sendKeys(Keys.ARROW_DOWN).perform();
		this.sleep(2);
		actions.sendKeys(Keys.ENTER).perform();
		this.sleep(3);
		
		//利用control加tab键切换窗口
		actions.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).keyUp(Keys.CONTROL).sendKeys(Keys.NULL).perform();
		this.sleep(3);
	}
	public void end() {

		driver.quit();		
	}
	public void sleep(int scends) {
		try {
			Thread.sleep(scends*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}



