package com.wonniuxy.cbt.atm;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Common {
	
	//定义静态成员变量driver，让各个模块全部使用同一的driver实例
	private static WebDriver driver = null;
	
	//定义静态域，只在Common类加载是执行一次，避免多次实例化
	static {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		System.setProperty("D:\\Program Files\\MozillaFirefox\\firefox.exe", "drivers/geckodriver.exe");
		driver = new ChromeDriver();
		// 全局设置，当元素识别不到的时候，可以接受的最长等待时间
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// 全局设置，页面加载最长等待时间。
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		// 全局设置，关于JavaScript代码异步处理的超时时间。ajax请求。
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
		// 全局设置，窗口最大化
		try{driver.manage().window().maximize();}catch (Exception e) {}
	}
	
	//返回当前类中的成员变量driver的实例
	public WebDriver getWebDriver() {
		return driver;
	}
	
	//实现waitForElementPresent等待方法以提升代码的健壮性
	public void waitForElementPresent(By by) {
		for(int i=1; i<=10; i++) {
			try {
				Thread.sleep(1000);
				driver.findElement(by);
				break;
			} catch(NoSuchElementException ne) {
				System.out.println("正在寻找元素，第" + i + " 遍.");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	//保留方法：isElementPresent，用于判断元素是否存在
	public boolean isElementExist(By by) {
		try {
			driver.findElement(by);
			return true;
		}catch(NoSuchElementException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//定义一个暂停脚本运行的方法，并且生成一个指定范围内的随机暂停时间
		public void sleepRandom(int min, int max) {
			Random myrand = new Random();
			int temp = myrand.nextInt(min);
			int time = temp + (max - min) +1;
			try {
				Thread.sleep(time*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	//指定范围的随机数 100-220  120:0~199
	public int getRandom(int min, int max) {
		Random myrand = new Random();
		int gap = max - min;
		int temp = myrand.nextInt(gap);
		int random = temp + min;
		return random;
	}
	
	//让driver关闭当前浏览器，结束运行
	public void closeBrowser() {
		try {
			driver.quit();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
