package com.woniuxy.selenium;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.woniuxy.core.Common;

public class SwitchUsage {
	private WebDriver driver = null;
	private Actions  actions = null;
	Common common = new Common();

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
//		driver.navigate().back();
//		driver.navigate().equals(null);
//		driver.navigate().refresh();
		actions = new Actions(driver);

		// 打开agoleone首页
		driver.get("http://localhost:8081/agileone/");
	}

	// 完成后的扫尾工作
	public void end() {

		driver.quit();
	}

	// 对agileone的登录功能进行测试
	public void testLogin() {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.id("login")).click();
		common.sleep(1);
		boolean logOut = common.isElementExist(driver, By.linkText("注销"));
		if (logOut) {
			System.out.println("登录成功");
		} else {
			System.out.println("登录失败。");
			System.exit(0);
		}
		common.sleep(3);
	}

	// 从列表中随机找一条记录进行删除，并断言
	public void testDelet() {
		int countBefore = common.getCountByTable("proposal");
		driver.findElement(By.linkText("※ 需求提案 ※")).click();
		common.sleep(2);
		int randomIndex = common.getRandomIndex(driver, "proposal");

		// 获取表格第一列数据的ID
		String id = common.getTableCellText(driver, "dataTable", randomIndex, 1);

		String xpath1 = "//tbody[@id='dataPanel']/tr[" + randomIndex + "]/td[5]/label[2]";
		String xpath = "(//label[@onclick='doDelete(this)'])[" + randomIndex + "]";
		driver.findElement(By.xpath(xpath)).click();
		common.sleep(1);
		driver.switchTo().alert().accept(); // 点击对话框确定按钮
		common.sleep(1);
	}
	//切换新标签
	public void newTable() {
		actions.contextClick(driver.findElement(By.partialLinkText("需求提案"))).perform();
		common.sleep(2);
		actions.sendKeys("T").sendKeys(Keys.NULL).perform();   //利用快捷键T打开新窗口
		common.sleep(5);
		
		//利用两次向下箭头和一次回车键，实现新窗口打开链接
//		actions.sendKeys(Keys.ARROW_DOWN).perform();
//		common.sleep(2);
//		actions.sendKeys(Keys.ARROW_DOWN).perform();
//		common.sleep(2);
//		actions.sendKeys(Keys.ENTER).perform();
//		common.sleep(3);
		//利用control加tab键切换窗口
		actions.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).keyUp(Keys.CONTROL).sendKeys(Keys.NULL).perform();
		common.sleep(1);
		//可以正常操作新标签页中的元素
		driver.navigate().refresh();
		driver.findElement(By.id("processresult")).sendKeys("test1000");
		
	}
	//切换新窗口
	public void newWindow(String newWindowTitle) {
		actions.contextClick(driver.findElement(By.partialLinkText("需求提案"))).perform();
		common.sleep(2);	
		//利用两次向下箭头和一次回车键，实现新窗口打开链接
		actions.sendKeys(Keys.ARROW_DOWN).perform();
		common.sleep(2);
		actions.sendKeys(Keys.ARROW_DOWN).perform();
		common.sleep(2);
		actions.sendKeys(Keys.ENTER).perform();
		common.sleep(2);
		//通过窗口切换操作，切换到新窗口
		String currentHandle = driver.getWindowHandle();
		Set<String> windows = driver.getWindowHandles();
		for(String handle: windows) {
			//如果不等于当前正在操作的窗口句柄，则意味着是新窗口
			if(!handle.contentEquals(currentHandle)) {
				driver.switchTo().window(handle);
				if(driver.getTitle().equals(newWindowTitle)) {
					break;
				}
			}
		}
		driver.findElement(By.id("headline")).sendKeys("这是需求提案的标题");
		driver.switchTo().window(currentHandle);
	}
	
	// 进入iframe子页面
		public void intoIframe(String xpath) {
			try {
				WebElement frame = driver.findElement(By.xpath(xpath));
				driver.switchTo().frame(frame);
			} catch (Exception e) {
			}
		}

		// 退出子页面
		public void outIframe() {

			try {
				// 切回主页面
				driver.switchTo().defaultContent();
			} catch (Exception e) {
			}
		}

}
