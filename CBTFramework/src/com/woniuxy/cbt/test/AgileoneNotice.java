package com.woniuxy.cbt.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.woniuxy.cbt.core.Common;
import com.woniuxy.cbt.core.Httprequestor;
import com.woniuxy.cbt.core.Reporter;
import com.woniuxy.cbt.core.SeleniumDriver;

public class AgileoneNotice {

	private Common common = new Common();
	private SeleniumDriver selenium = new SeleniumDriver("ie");
	private WebDriver driver = selenium.getWebDriver();
	private Reporter report = new Reporter();
	private Httprequestor hr = new Httprequestor();

	public void mainTest() {
		this.prepare();
		this.testAdd();
		common.sleepRandom(2, 3);
		this.testDelete();
		this.testEdit();
		this.finish();
	}

	// 测试执行前的准备工作:打开首页并执行登录
	private void prepare() {
		System.out.println("公告测试开始……");
		// 为Reporter对象定义当前模块名称
		Reporter.module = "公告管理";
		
		driver.get("http://localhost/agileone/");
		// 如果之前已经登录，则将其注销
		if (selenium.isElementPresent(By.linkText("个人设定"))) {
			driver.findElement(By.linkText("注销")).click();
			this.prepare();
		}
		else {
			this.doLogin("admin", "admin");
			common.sleepRandom(2, 5);
		}
	}

	// 测试完成后的收尾工作
	private void finish() {
		driver.get("http://localhost/agileone/");
		driver.findElement(By.linkText("注销")).click();
//		selenium.closeBrowser();
		System.err.println("公告测试完成……");
	}

	// ACtion组件实现登录操作,但此处不对登录进行测试，只是作为一个前置条件
	public void doLogin(String username, String password) {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login")).click();
		common.sleepRandom(3, 5);
	}
	
	//Action组件实现新增公告的操作
	private void doAdd(String headline, String content) {
		try {
			driver.findElement(By.linkText("※ 公告管理 ※")).click();
			//driver.get("http://localhost/agileone/index.php/notice");
			if(!selenium.isElementPresent(By.id("headline"))) {
				System.out.println("系统并未跳转到公告管理页面，无法进行操作.");
				this.doAdd(headline, content);
			}
	    	driver.findElement(By.id("headline")).clear();
	    	driver.findElement(By.id("headline")).sendKeys(headline);
	    	driver.findElement(By.cssSelector("img.ke-common-icon.ke-icon-source")).click();
	    	driver.findElement(By.cssSelector("textarea.ke-textarea")).sendKeys(content);
	    	driver.findElement(By.id("add")).click();
	    	common.sleepRandom(2, 5);
			
		}catch (Exception e) {
			//
		}
	}

	//Test组件用于测试公告的新增功能
	public void testAdd() {
		//设置本测试用例的基础信息
		String caseId = "Agileone_NoticeGUI_001";
		String caseDesc = "公告管理模块-功能测试-正常新增";
		
		String headline = "公告的标题-" + common.getRandom(10000, 99999);
		String content = "公告的内容-" + common.getRandom(10000, 99999);
		try {
			this.doAdd(headline, content);
			String msg = driver.findElement(By.id("msg")).getText();
			//("^成功啦：新增数据成功. +\\d+s")
			if(msg.contains("成功啦: 新增数据成功")) {
				String result = Reporter.PASS;
				report.writelog(caseId, caseDesc, result, "", "");
			}else {
				String result = Reporter.FAIL;
				String error = "操作后信息为：" + msg;
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
			}
		} catch (Exception e) {
			String result = Reporter.ERROR;
			String error =e.getMessage();
			String screenshot = report.captureScreen();
			report.writelog(caseId, caseDesc, result, error, screenshot);
		}
	}
	//实现获取公告Id的方法
	public String getNoticeId() {
		String postUrl = "http://localhost/agileone/index.php/notice/query";
		String postParam = "currentpage=1";
		String resp = hr.sendPost(postUrl, postParam);
		String left = "proposalid\":\"";
		String right = "\",\"projectid";
		String respId = common.getItem(resp, left, right);
		return respId;
	}
	//Action组件实现公告的删除操作
	private void doDelete(String noticeId) {
		driver.findElement(By.linkText("※ 公告管理 ※")).click(); 	  	
    	String xpath = "(//label[@onclick='doDelete(this)'])[1]";
    	driver.findElement(By.xpath(xpath)).click();
    	common.sleepRandom(2, 3);
    	driver.switchTo().alert().accept();  //点击对话框确定按钮
    	common.sleepRandom(2, 3);
	}
	//Test组件用于测试公告的删除功能
	private void testDelete() {
		// 设置用例基础信息
		String caseId = "Agileone_noticeGUI_002";
		String caseDesc = "公告管理模块-功能测试-删除测试";
		String noticeId = this.getNoticeId();
		try {
			this.doDelete(getNoticeId());
			String  msg = driver.findElement(By.id("msg")).getText();
	    	if(msg.contains("成功啦: 删除数据成功 -> 编号=" + noticeId)) {
	    		String result = Reporter.PASS;
				report.writelog(caseId, caseDesc, result, "", "");
			}else {
				String result = Reporter.FAIL;
				String error = "操作后信息为：" + msg;
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
			}
		} catch (Exception e) {
			String result = Reporter.ERROR;
			String error =e.getMessage();
			String screenshot = report.captureScreen();
			System.out.println("图片名称为：" + screenshot);
			report.writelog(caseId, caseDesc, result, error, screenshot);
		}
	}
	
	// Action组件实现编辑公告操作
		private void doEdit(String proposalId, String headline, String content) {
			driver.findElement(By.linkText("※ 公告管理 ※")).click();
			common.sleepRandom(2, 3);

			String xpath = "(//label[@onclick='goEdit(this,true)'])[1]";
			driver.findElement(By.xpath(xpath)).click();
			common.sleepRandom(2, 3);
			driver.findElement(By.id("headline")).clear();
			driver.findElement(By.id("headline")).sendKeys(headline);
			driver.findElement(By.className("ke-common-icon")).click();
			driver.findElement(By.className("ke-textarea")).clear();
			driver.findElement(By.className("ke-textarea")).sendKeys(content);
			driver.findElement(By.id("edit")).click();
			common.sleepRandom(2, 3);
		}

		// Test组件测试编辑公告
		private void testEdit() {
			String caseId = "Agileone_noticeGUI_003";
			String caseDesc = "公告管理模块-功能测试-编辑测试";

			String proposalId = this.getNoticeId();
			String type = "Enhancement";
			String importance = "High";
			String headline = "这是编辑公告的标题-" + common.getRandom(10000, 99999);
			String content = "这是编辑公告的内容-" + common.getRandom(10000, 99999);
			try {
				this.doEdit(proposalId, headline, content);
				String msg = driver.findElement(By.id("msg")).getText();
				common.sleepRandom(2, 3);
				if (msg.contains("成功啦: 更新数据成功 -> 编号="+ proposalId)) {
					String result = Reporter.PASS;
					report.writelog(caseId, caseDesc, result, "", "");
				} else {
					String result = Reporter.FAIL;
					String error = "操作后信息为：" + msg;
					String screenshot = report.captureScreen();
					report.writelog(caseId, caseDesc, result, error, screenshot);
				}
			} catch (Exception e) {
				String result = Reporter.ERROR;
				String error = e.getMessage();
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
			}
		}
	
}
