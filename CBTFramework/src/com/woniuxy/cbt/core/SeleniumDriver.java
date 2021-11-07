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
	
	//���徲̬��Ա����driver���ø�ģ��ȫ��ʹ��ͳһ��driverʵ��
	private static WebDriver driver = null;
	
	//���þ�̬��Ա����isInitiated,���driver�Ѿ�����ʼ�������ٳ�ʼ��
	public static boolean isInitiated;
	
	//˽�л����캯����ֹ�û�ʵ�������
	private SeleniumDriver() {
		
	}
	
	//�˴����ٶ��徲̬�򣬷����޷����ݲ�����ֻ���ù��췽�����
	public SeleniumDriver(String browserType) {
		
		//���driver��û�б���ʼ�����Ŷ�����г�ʼ��
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
			// ȫ�����ã���Ԫ��ʶ�𲻵���ʱ�򣬿��Խ��ܵ���ȴ�ʱ��
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			// ȫ�����ã�ҳ�������ȴ�ʱ�䡣
			driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
			// ȫ�����ã�����JavaScript�����첽����ĳ�ʱʱ�䡣ajax����
			driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
			// ȫ�����ã��������
			try{driver.manage().window().maximize();}catch (Exception e) {}
		}
	}
	
	//���ص�ǰ���еĳ�Ա����driver��ʵ��
			public  WebDriver getWebDriver() {
				return driver;
			}
	
	//ʵ��waitForElementPresent�ȴ���������������Ľ�׳��
		public void waitForElementPresent(By by) {
			for(int i=1; i<=10; i++) {
				try {
					Thread.sleep(1000);
					driver.findElement(by);
					break;
				} catch(NoSuchElementException ne) {
					System.out.println("����Ѱ��Ԫ�أ���" + i + " ��.");
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//����������isElementPresent�������ж�Ԫ���Ƿ����
		public boolean isElementPresent(By by) {
			try {
				driver.findElement(by);
				return true;
			}catch(Exception e) {
				//e.printStackTrace();
				return false;
			}
		}
		
		//��driver�رյ�ǰ���������������
		public void closeBrowser() {
			try {
				driver.quit();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

}
