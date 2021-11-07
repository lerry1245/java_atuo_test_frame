package com.woniuxy.cbt.pom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage {

	@FindBy(linkText = "个人设定")
	private WebElement personal;
	@FindBy(linkText = "注销")
	private WebElement logout;
	@FindBy(linkText = "※ 公告管理 ※")
	private WebElement notice;
	@FindBy(linkText = "※ 需求提案 ※")
	private WebElement proposal;
	//元素较多，此处略
	
	public void gotoProposal() {
		this.proposal.click();
	}
	
}
