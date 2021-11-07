package com.wonniuxy.cbt.atm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
	
//	//默认情况下线根据id查找，在根据那么查找
//	private WebElement username;
//	private WebElement password;
//	private WebElement savelogin;
//	
//	//也可以通过指定@FindBy注解明确查找方法
//	@FindBy(id ="Login")
//	private WebElement login;
//	
//	public void doLogin(String username, String password, boolean savelogin) {
//		this.username.clear();
//		this.username .sendKeys(username);
//		this.password .clear();
//		this.password .sendKeys(password);
//		if(!savelogin)
//			this.savelogin.click();
//		this.login .click();
//	}
	
	private Common common = new Common();
	private WebDriver driver = common.getWebDriver();
	
	//获取用户名文本框对象
	public WebElement getUsername() {
		WebElement element = driver.findElement(By.id("username"));
		return element;
	}
	
	//获取密码文本框对象
	public WebElement getPassword() {
		WebElement element = driver.findElement(By.id("password"));
		return element;
	}
	
	//获取登录信息复选框对象
	public WebElement getSavelogin() {
		WebElement element = driver.findElement(By.id("savelogin"));
		return element;
	}
	
	//获取登录按钮按钮对象
	public WebElement getLoginBtn() {
		WebElement element = driver.findElement(By.id("login"));
		return element;
	}

	//Action组件
	public void doLogin(String username, String password, boolean saveLogin) {
		this.getUsername().clear();
		this.getUsername().sendKeys(username);
		this.getPassword().clear();
		this.getPassword().sendKeys(password);
		if(!saveLogin)
			this.getSavelogin().click();
		this.getLoginBtn().click();
	}
		
}
