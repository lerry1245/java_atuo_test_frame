package com.woniuxy.cbt.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.javascript.host.event.MSGestureEvent;
import com.woniuxy.cbt.core.Common;
import com.woniuxy.cbt.core.ExcelWriter;
import com.woniuxy.cbt.core.Httprequestor;
import com.woniuxy.cbt.core.Reporter;
import com.woniuxy.cbt.core.SeleniumDriver;

public class AgileonePropGUI {
	// 定义WebDriver实例，用于当前类的测试
	private Common common = new Common();
	private SeleniumDriver selenium = new SeleniumDriver("ie");
	private WebDriver driver = selenium.getWebDriver();
	private Reporter report = new Reporter();
	private Httprequestor hr = new Httprequestor();

	// 声明写excel对象。
	public ExcelWriter webExcel = null;
	// 当前行数的成员变量
	public int line = 0;
	
	public AgileonePropGUI() {
	}

	public AgileonePropGUI(ExcelWriter excel) {
		webExcel = excel;
	}

	public void mainTest() {
		this.prepare();
		this.testAdd();
		common.sleepRandom(2, 3);
		this.testDelete();
		common.sleepRandom(2, 3);
		this.testEdit();
		common.sleepRandom(2, 3);
		this.finish();

	}

	// 测试执行前的准备工作
	private void prepare() {
		System.out.println("需求提案测试开始……");
		// 为Reporter对象定义当前模块名称
		Reporter.module = "需求提案";
//		driver.get("http://localhost/agileone/");
		new AgileoneNotice().doLogin("admin", "admin");
		common.sleepRandom(2, 5);
		driver.findElement(By.linkText("※ 需求提案 ※")).click();
	}

	// 测试完成后的收尾工作
	private void finish() {
		driver.get("http://localhost/agileone/");
		driver.findElement(By.linkText("注销")).click();
		selenium.closeBrowser();
		System.out.println("需求提案测试完成……");
	}

	// ACtion组件实现需求提案的新增操作
	private void doAdd(String type, String importance, String headline, String content) {
		if (!selenium.isElementPresent(By.id("headline"))) {
			System.out.println("系统并未跳转到需求提案页面，无法进行操作.");
			// driver.findElement(By.linkText("※ 需求提案 ※")).click();
			driver.get("http://localhost/agileone/index.php/proposal");
			this.doAdd(type, importance, headline, content);
		}
		Select se1 = new Select(driver.findElement(By.id("type")));
		se1.selectByValue(type);
		Select se2 = new Select(driver.findElement(By.id("importance")));
		se2.selectByValue(importance);
		driver.findElement(By.id("headline")).sendKeys(headline);
		String css1 = "img.ke-common-icon.ke-icon-source";
		driver.findElement(By.cssSelector(css1)).click();
		String css2 = "textarea.ke-textarea";
		driver.findElement(By.cssSelector(css2)).sendKeys(content);
		driver.findElement(By.id("add")).click();
		common.sleepRandom(2, 4);
	}

	// Test组件用于测试需求提案新增功能
	public void testAdd() {
		// 设置用例基础信息
		String caseId = "Agileone_PropGUI_001";
		String caseDesc = "需求模块-功能测试-正常新增";

		String type = "Enhancement";
		String importance = "High";
		String headline = "这是需求提案的标题-" + common.getRandom(10000, 99999);
		String content = "这是需求提案的内容-" + common.getRandom(10000, 99999);
		try {
			this.doAdd(type, importance, headline, content);
			common.sleepRandom(2, 4);
			String msg = driver.findElement(By.id("msg")).getText();
			if (msg.matches("^成功啦: 新增数据成功.+\\d+$")) {
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

	// 实现获取需求提案Id的方法
	public String getproposalId() {
		String postUrl = "http://localhost/agileone/index.php/notice/query";
		String postParam = "currentpage=1";
		String resp = hr.sendPost(postUrl, postParam);
		String left = "proposalid\":\"";
		String right = "\",\"projectid";
		String respId = common.getItem(resp, left, right);
		return respId;
	}

	// Action组件实现对某一条需求提案的删除操作
	private void doDelete(int index) {
		driver.findElement(By.linkText("※ 需求提案 ※")).click();
		String xpath = "(//label[@onclick='doDelete(this)'])[" + index + "]";
		driver.findElement(By.xpath(xpath)).click();
		common.sleepRandom(2, 3);
		driver.switchTo().alert().accept();
	}

	// Test组件用于测试需求提案的删除功能
	public void testDelete() {
		// 设置用例基础信息
		String caseId = "Agileone_PropGUI_002";
		String caseDesc = "需求模块-功能测试-删除测试";

		try {
			this.doDelete(3);
			String msg = driver.findElement(By.id("msg")).getText();
			if (msg.matches("^成功啦: 删除数据成功.+\\d+$")) {
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

	// Action组件实现编辑需求提案
	private void doEdit(String proposalId, String headline, String content) {
		driver.findElement(By.linkText("※ 需求提案 ※")).click();
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

	// Test组件测试编辑需求提案
	private void testEdit() {
		String caseId = "Agileone_ProposalGUI_003";
		String caseDesc = "需求模块-功能测试-编辑需求提案测试";

		String proposalId = this.getproposalId();
		String type = "Enhancement";
		String importance = "High";
		String headline = "这是编辑提案的标题-" + common.getRandom(10000, 99999);
		String content = "这是编辑提案的内容-" + common.getRandom(10000, 99999);
		try {
			this.doEdit(proposalId, headline, content);
			String msg = driver.findElement(By.id("msg")).getText();
			common.sleepRandom(2, 3);
		} catch (Exception e) {
		}
//			if (msg.contains("成功啦: 更新数据成功 -> 编号="+ proposalId)) {
//				String result = Reporter.PASS;
//				report.writelog(caseId, caseDesc, result, "", "");
//			} else {
//				String result = Reporter.FAIL;
//				String error = "操作后信息为：" + msg;
//				String screenshot = report.captureScreen();
//				report.writelog(caseId, caseDesc, result, error, screenshot);
//			}
//		} catch (Exception e) {
//			String result = Reporter.ERROR;
//			String error = e.getMessage();
//			String screenshot = report.captureScreen();
//			report.writelog(caseId, caseDesc, result, error, screenshot);
//
//		}
		this.assertContains("msg", "成功啦: 更新数据成功 -> 编号=", caseId, caseDesc);

	}

	public void assertContains(String msg, String expectres, String caseId, String caseDesc) {
		try {

			if (msg.contains(expectres)) {
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
