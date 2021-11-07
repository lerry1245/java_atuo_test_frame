package com.wonniuxy.cbt.atm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Proposal {
	//定义WebDriver实例，用于当前类的测试
		private Common common = new Common();
		private WebDriver driver = common.getWebDriver();
		
		public void mianTest() {
			this.prepare();
			this.testAdd();
			this.testDelete();
			this.finish();
			
		}
		
		//测试执行前的准备工作
		private void prepare() {
			//如果没有定位在登录的住窗口，则重新实现登录，确保运行正常
			if(!common.isElementExist(By.linkText("个人设定"))) {
				new Login().testLoginPass();
			}
			driver.findElement(By.linkText("※ 需求提案 ※")).click();
		}
		
		//测试完成后的收尾工作
		private void finish() {
			common.closeBrowser();
			System.out.println("测试完成关闭浏览器");
		}
		
		//ACtion组件实现需求提案的新增操作
		private void doAdd(String type, String importance, String headline, String content) {
			if(!common.isElementExist(By.id("headline"))) {
				System.out.println("系统并未跳转到需求提案页面，无法进行操作.");
				//driver.findElement(By.linkText("※ 需求提案 ※")).click();
				driver.get("http://localhost/agileone/index.php/proposal");
				this.doAdd(type, importance, headline, content);
			}
			Select se1 = new Select(driver.findElement(By.id("type")));
			se1.selectByValue(type);
			Select se2 = new Select(driver.findElement(By.id("importance")));
			se2.selectByValue(importance);
			driver.findElement(By.id("headline")).sendKeys(headline);
			String css1 ="img.ke-common-icon.ke-icon-source";
			driver.findElement(By.cssSelector(css1)).click();
			String css2 = "textarea.ke-textarea";
			driver.findElement(By.cssSelector(css2)).sendKeys(content);
			driver.findElement(By.id("add")).click();
			common.sleepRandom(2, 4);
		}
		
		//Test组件用于测试需求提案新增功能
		public void testAdd() {
			String type = "Enhancement";
			String importance = "High";
			String headline = "这是需求提案的标题-" + common.getRandom(10000, 99999);
			String content = "这是需求提案的内容-" + common.getRandom(10000, 99999);
			this.doAdd(type, importance, headline, content);
			common.sleepRandom(2, 4);
			String  result = driver.findElement(By.id("msg")).getText();
	    	if(result.matches("^成功啦: 新增数据成功.+\\d+$")) {
	    		System.err.println("新增需求提案测试-成功.");
	    	}
	    	else {
	    		System.err.println("新增需求提案测试-失败.");
	    	}
		}
		
		//Action组件实现对某一条需求提案的删除操作
		private void doDelete(int index) {
			String xpath = "(//label[@onclick='doDelete(this)'])["+ index +"]";
			driver.findElement(By.xpath(xpath)).click();
			driver.switchTo().alert().accept();
			common.sleepRandom(2, 3);
		}
		
		//Test组件用于测试需求提案的删除功能
		public void testDelete() {
			this.doDelete(3);
			String  restult = driver.findElement(By.id("msg")).getText();
	    	if(restult.matches("^成功啦: 删除数据成功.+\\d+$")) {
	    		System.err.println("删除需求提案测试-成功.");
	    	}
	    	else {
	    		System.err.println("删除需求提案测试-失败.");
	    	}
		}
}
