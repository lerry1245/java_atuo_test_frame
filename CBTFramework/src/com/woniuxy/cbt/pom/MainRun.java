package com.woniuxy.cbt.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class MainRun {

	public static void main(String[] args) {
		Common common = new Common();
		WebDriver driver = common.getWebDriver();
		driver.get("http://localhost/agileone");
		LoginPage lPage =PageFactory.initElements(driver, LoginPage.class);
		lPage.doLogin("admin", "admin", false);
		MainPage mPage =PageFactory.initElements(driver, MainPage.class);
		mPage.gotoProposal();
		
		ProposalPage pp =PageFactory.initElements(driver, ProposalPage.class);
		String type = "Enhancement";
		String importance = "High";
		String headline = "这是需求提案的标题-" + common.getRandom(10000, 99999);
		String content = "这是需求提案的内容-" + common.getRandom(10000, 99999);
		pp.doAdd(type, importance, headline, content);
		pp.doDelete();

	}

}
