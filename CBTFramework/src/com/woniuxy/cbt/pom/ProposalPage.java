package com.woniuxy.cbt.pom;

import org.apache.http.conn.util.PublicSuffixList;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;


import com.woniuxy.cbt.core.SeleniumDriver;

public class ProposalPage {
	private WebElement type;
	private WebElement importance;
	private WebElement headline;
	@FindBy(css="img.ke-common-icon.ke-icon-source")
	private WebElement code;
	@FindBy(css="textarea.ke-textarea")
	private WebElement content;
	private WebElement processresult;
	private WebElement add;
	private WebElement edit;
	private WebElement search;
	@FindBy(xpath = "(//label[@onclick='doDelete(this)'])[2]")
	private WebElement delet;
	
	public void doAdd(String type, String importance, String headline, String content) {
		new Select(this.type).selectByValue(type);
		new Select(this.importance).selectByValue(importance);
		this.headline.sendKeys(headline);
		this.code.click();
		this.content.sendKeys(content);
		this.add.click();
	}
	
	public void doDelete() {
		this.delet.click();		
		Common common = new Common();
		common.getWebDriver().switchTo().alert().accept();
	}
	
}
