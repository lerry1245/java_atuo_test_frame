package com.woniuxy.selenium;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.woniuxy.core.*;

public class AgileoneNoticeTest {
	
	//定义类成员变量driver，用于实例化webdriver
	private WebDriver driver;
	private Actions actions;
	Common common = new Common();
	
	//标准程序入口方法
	public static void main(String[] args) {
		AgileoneNoticeTest at = new AgileoneNoticeTest();
		at.init();
		at.testLogin();
		at.testadd();
//		at.testEdit();
//		at.testDelet();
		at.testLogout();
		at.end();

	}
	
	//初始化环境
	public void init() {
		System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
		driver = new ChromeDriver();
		//全局设置，当元素识别不到的时候，可以接受的最长等待时间
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		//全局设置，页面加载最长等待时间。
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		//全局设置，关于JavaScript代码异步处理的超时时间。ajax请求。
		driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
		//全局设置，窗口最大化
		driver.manage().window().maximize();
		//浏览器行为模拟
//		driver.navigate().back();
//		driver.navigate().equals(null);
//		driver.navigate().refresh();
		
	//打开agoleone首页
		driver.get("http://localhost/agileone/");
		boolean usernameIsExist = common.isElementExist(driver, By.id("username"));
		//断言一，验证页面元素是否存在
		if(usernameIsExist) {
			System.out.println("打开首页成功，开始进一步测试。");
		}
		else {
			System.out.println("打开首页失败，系统即将退出测试。");
			System.exit(0);			
		}
		//断言二，检查页面源代码判断
		String response = driver.getPageSource();
		if (response.contains("id=\"password\"")){
			System.out.println("打开首页成功，开始进一步测试。");
		}
		else {
			System.out.println("打开首页失败，系统即将退出测试。");
			System.exit(0);			
		}
		
	}
	
	//完成后的扫尾工作
	public void end() {

		driver.quit();		
	}
	
	//对agileone的登录功能进行测试
	public void testLogin() {
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.id("login")).click();
		common.sleep(1);
		boolean logOut = common.isElementExist(driver, By.linkText("注销"));
		if(logOut) {
			System.out.println("登录成功");
		}
		else {
			System.out.println("登录失败。");
			System.exit(0);
		}
		common.sleep(3);	
	}

	//对agileone的添加公告进行测试
    public void testadd() {
 //   	int countBefore = common.getCountByTable("proposal");
    	long nowTime = System.currentTimeMillis(); 
    	driver.findElement(By.linkText("※ 需求提案 ※")).click();
    	driver.findElement(By.id("headline")).clear();
    	driver.findElement(By.id("headline")).sendKeys("这是一条需求提案的标题"+ nowTime);
    	driver.findElement(By.className("ke-common-icon")).click();
    	driver.findElement(By.className("ke-textarea")).clear();
    	driver.findElement(By.className("ke-textarea")).sendKeys("这是一条需求提案的内容"+ nowTime);
    	driver.findElement(By.id("add")).click();
    	common.sleep(3);
    	//断言一，页面元素判断
    	String  massage = driver.findElement(By.id("msg")).getText();
    	if(massage.contains("成功啦: 新增数据成功 -> 编号=")) {
    		System.err.println("新增需求提案成功。");
    	}
    	else {
    		System.err.println("新增需求提案失败。");
    	}
//    	//断言二：利用表格断言
//    	String headLine = common.getTableCellText(driver, "dataTable", 1, 2);
//    	if(headLine.equals("这是一条需求提案的标题"+ nowTime)){
//    		System.err.println("新增需求提案成功。");
//    	}
//    	else {
//    		System.err.println("新增需求提案失败。");
//    	}
//    	//断言三：利用JDBC，判断表格数量进行断言
//    	int countAfter = common.getCountByTable("proposal");
//    	if(countAfter == countBefore +1){
//    		System.err.println("新增需求提案成功。");
//    	}
//    	else {
//    		System.err.println("新增需求提案失败。");
//    	}
	}
    
    //从列表中随机找一条记录进行编辑，并断言
    public void testEdit() {
    	long nowTime = System.currentTimeMillis(); 
    	driver.findElement(By.linkText("※ 需求提案 ※")).click();
    	common.sleep(2);
    	int randomIndex = common.getRandomIndex(driver, "proposal");
    	String xpath1 = "//tbody[@id='dataPanel']/tr["+ randomIndex +"]/td[5]/label[1]";
    	String xpath = "(//label[@onclick='goEdit(this,true)'])["+randomIndex+"]";
    	driver.findElement(By.xpath(xpath)).click();
    	common.sleep(1);
    	driver.findElement(By.id("headline")).clear();
    	driver.findElement(By.id("headline")).sendKeys("这是一条新需求提案的标题"+ nowTime);
    	driver.findElement(By.className("ke-common-icon")).click();
    	driver.findElement(By.className("ke-textarea")).clear();
    	driver.findElement(By.className("ke-textarea")).sendKeys("这是一条新需求提案的内容"+ nowTime);
    	driver.findElement(By.id("edit")).click();
    	common.sleep(3);
    	//断言一，页面元素判断
    	String  massage = driver.findElement(By.id("msg")).getText();
    	if(massage.contains("成功啦: 更新数据成功 -> 编号=")) {
    		System.err.println("编辑需求提案成功。");
    	}
    	else {
    		System.err.println("编辑需求提案失败。");
    	}
        //断言二：
//    	String headLine = common.getTableCellText(driver, "dataTable", randomIndex, 2);
//    	if(headLine.equals("这是一条新需求提案的标题"+ nowTime)){
//    		System.err.println("编辑需求提案成功。");
//    	}
//    	else {
//    		System.err.println("编辑需求提案失败。");
//    	}
    }
    
  //从列表中随机找一条记录进行删除，并断言
    public void testDelet() {
    	int countBefore = common.getCountByTable("proposal");
    	driver.findElement(By.linkText("※ 需求提案 ※")).click();
    	common.sleep(2);
    	int randomIndex = common.getRandomIndex(driver, "proposal");
    	
    	//获取表格第一列数据的ID
    	String  id = common.getTableCellText(driver, "dataTable", randomIndex, 1);
    	
    	String xpath1 = "//tbody[@id='dataPanel']/tr["+ randomIndex +"]/td[5]/label[2]";
    	String xpath = "(//label[@onclick='doDelete(this)'])["+randomIndex+"]";
    	driver.findElement(By.xpath(xpath)).click();
    	common.sleep(1);
    	driver.switchTo().alert().accept();  //点击对话框确定按钮
    	common.sleep(1);
    	
    	//断言一，页面元素判断
    	String  massage = driver.findElement(By.id("msg")).getText();
    	if(massage.contains("成功啦: 删除数据成功 -> 编号=" + id)) {
    		System.err.println("删除需求提案成功。");
    	}
    	else {
    		System.err.println("删除需求提案失败。");
    	}
  	
//    	//断言二：利用JDBC，判断表格数量进行断言
//    	int countAfter = common.getCountByTable("proposal");
//    	if(countAfter == countBefore - 1){
//    		System.err.println("删除需求提案成功。");
//    	}
//    	else {
//    		System.err.println("删除需求提案失败。");
//    	}  	
    }
    
  //对agileone的注销功能进行测试
    public void testLogout() {
    	driver.findElement(By.linkText("注销"));
    	common.sleep(1);
    	
//    	if (common.isElementExist(driver, By.id("username"))){
//    		System.err.println("注销成功。");
//    	}
//    	else {
//    		System.err.println("注销失败。");
//    		common.sleep(2);
//    	}  	
    }
    public void select() {
    	driver.findElement(By.partialLinkText("缺陷跟踪")).click();
    	common.sleep(1);
    	Select select = new Select(driver.findElement(By.id("status")));
    	//通过下拉条目中序号进行选择
    	select.deselectByIndex(2);
    	common.sleep(1);
    	//通过下拉条目中文本内容进行选择
    	select.selectByVisibleText("Closed");
    	//通过下拉条目中value值来进行选择
    	select.deselectByValue("Reopen");
    	
    	//获取下拉框的总条目数量，并存入List对象中
    	List<WebElement> options = select.getOptions();
    	int count = options.size();  //获取下拉列表的总条目数
    	//生成0~count之间的随机数 （0<= X < count)
    	int randomIndex = new Random().nextInt(count);
    	select.deselectByIndex(randomIndex);
    }
    //通过键盘下箭头对下拉框进行操作
    public void sleectByKeyboard() {
    	WebElement status = driver.findElement(By.id("status"));
    	status.click();
    	actions = new Actions(driver);
    	actions.sendKeys(Keys.DOWN).perform();
    	actions.sendKeys(Keys.DOWN).perform();
    	actions.sendKeys(Keys.DOWN).perform();
    	actions.sendKeys(Keys.DOWN).perform();
    	actions.sendKeys(Keys.ENTER).perform();	
    }
    
    //操作浏览器
    public  void browserBasic() {
		driver.navigate().back();
		driver.navigate().equals(null);
		driver.navigate().refresh();
		driver.navigate().to("url");   	
    }
    
    //截图操作
    public void capture() {
    	TakesScreenshot s = (TakesScreenshot)driver;
    	//将当前截图处理为文件，也可以处理为字节码为base64编码的文本
    	File f = s.getScreenshotAs(OutputType.FILE);
    	try {
    		//将当前页面的截图保存到E:\Capture.png文件中
    		FileUtils.copyFile(f, new File("E:/Capture.png"));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}