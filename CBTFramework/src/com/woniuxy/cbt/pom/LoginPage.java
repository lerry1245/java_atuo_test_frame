package com.woniuxy.cbt.pom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

	//默认情况下线根据id查找，在根据那么查找
	private WebElement username;
	private WebElement password;
	private WebElement savelogin;
	
	//也可以通过指定@FindBy注解明确查找方法
	@FindBy(id ="Login")
	private WebElement login;
	
	public void doLogin(String username, String password, boolean savelogin) {
		this.username.clear();
		this.username .sendKeys(username);
		this.password .clear();
		this.password .sendKeys(password);
		if(!savelogin)
			this.savelogin.click();
		this.login .click();
	}

}

