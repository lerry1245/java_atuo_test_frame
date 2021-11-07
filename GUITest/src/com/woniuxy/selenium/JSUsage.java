package com.woniuxy.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.woniuxy.core.Common;

public class JSUsage {
	
	private WebDriver driver = null;
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
		
		public void jsexec() {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			String jsContent = 
					"document.getElementById('username').style.border='solid 2px red';"
					+"docnment.getElementById('username').value='admin';"
					+"docment.getElementById('password').value='admin';"
					+"document.getElementById('login').click();";
			jse.executeScript(jsContent);
			common.sleep(2);
			driver.findElement(By.partialLinkText("需求提案"));
			common.sleep(2);
			
			jsContent = "document.getElementById('headline').value='这是需求提案的标题' ;"
					+"setEContent('content','这是需求提案的内容-1245');";
			jse.executeScript(jsContent);
			
			driver.findElement(By.partialLinkText("公告管理")).click();
			common.sleep(2);
			
			//利用JavaScript操作HTML元素的方式实现公告的新增
			String jsContentEXP = "headline = document.getElementById('headline');"
					//此处为公告的标题和内容，并高亮显示标题文本框
					+"headline.style.border='solid 3px red';"
					+"headline.value='这是一个标题-1234';"
					//此处调用在线HTML编辑器的接口setKEContent直接为内容部分赋值
					+"setKEContent('content','这是公告的内容-12345');"
					+"document.getElementById('add').click();";
			jse.executeScript(jsContentEXP);
					
		}

}
