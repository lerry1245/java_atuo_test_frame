package com.woniuxy.cbt.core;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumDriver {
	
	//定义静态成员变量driver，让各模块全部使用统一的driver实例
	private static WebDriver driver = null;
	
	//设置静态成员变量isInitiated,如果driver已经被初始化过则不再初始化
	public static boolean isInitiated;
	
	//私有化构造函数防止用户实例化多个
	private SeleniumDriver() {
		
	}
	
	//此处不再定义静态域，否则无法传递参数，只能用构造方法完成
	public SeleniumDriver(String browserType) {
		
		//如果driver还没有被初始化，才对其进行初始化
		if(!isInitiated) {
			if(browserType.equals("ie")) {
				System.setProperty("webdriver.ie.driver","drivers/IEDriver.exe");
				DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
				dc.setCapability("ignoreProtectedModeSettings", true);
				driver = new InternetExplorerDriver(dc);
			}
			else if(browserType.equals("chrome")) { 
				//"webdriver.chrome.driver", "drivers/chromedriver.exe"   //"c:\\Tools\\chromedriver.exe"
				System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
				driver = new ChromeDriver();
			}
			else {
				System.setProperty("webdriver.firefox.bin","c:\\Tools\\Mozilla Firefox\\firefox.exe");
				System.setProperty("webdriver.gecko.driver","c:\\Tools\\geckodriver.exe");
				driver = new FirefoxDriver();
			}
			isInitiated = true;
			// 全局设置，当元素识别不到的时候，可以接受的最长等待时间
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			// 全局设置，页面加载最长等待时间。
			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			// 全局设置，关于JavaScript代码异步处理的超时时间。ajax请求。
			driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
			// 全局设置，窗口最大化
			try{driver.manage().window().maximize();}catch (Exception e) {}
		}
	}
	
	//返回当前类中的成员变量driver的实例
			public  WebDriver getWebDriver() {
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
		public boolean isElementPresent(By by) {
			try {
				driver.findElement(by);
				return true;
			}catch(Exception e) {
				//e.printStackTrace();
				return false;
			}
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
