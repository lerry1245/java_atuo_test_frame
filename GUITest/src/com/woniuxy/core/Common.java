package com.woniuxy.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;




public class Common {
	
	private static String driverClassName = "com.mysql.jdbc.Driver";
	//定义数据库连接字符串
	private static String url ="jdbc:mysql://localhost:3306/agileone?"
			+"user=root&password=wen123456&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	
	
	//判断元素是否存在的方法
	public boolean isElementExist(WebDriver driver,By by) {
		try {
			driver.findElement(by);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//生成一个区间随机数 100-220  120:0~199
	public int getRandom(int min, int max) {
		Random myrand = new Random();
		int gap = max - min;
		int temp = myrand.nextInt(gap);
		int random = temp + min;
		return random;
	}
	//暂停时间
	public void sleep(int scends) {
		try {
			Thread.sleep(scends*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {			
			Class.forName(driverClassName).newInstance();
		    //new com.mysql.jdbc.Driver(); 也可以直接这样实例化
		    conn = DriverManager.getConnection(url);  // 建立数据库连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;			
	}
    //通过表名获取表的数据总条数
	public int getCountByTable(String tableName) {
		String sql = "select count(*) from "+ tableName;
		int count = 0;
		try {
			PreparedStatement ps = this.getConnection().prepareStatement(sql);  // 定义一个SQL语句执行器
			ResultSet rs = ps.executeQuery(sql); // 执行SQL语句，并返回结果
			rs.next(); // 将结果集的游标定位到第一行
			count = rs.getInt(1); // 读取第一列的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}
    //获取表格第一列
	public String checkColumnContent(String sql) {			
		String content = "";
		try {
			PreparedStatement ps = this.getConnection().prepareStatement(sql);       // 定义一个SQL语句执行器
			ResultSet rs = ps.executeQuery(sql);         // 执行SQL语句，并返回结果
			rs.next();                                      // 将结果集的游标定位到第一行
			content = rs.getString(1);              // 读取第一列的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	//获取表格第一页数据的随机下标
	public int getRandomIndex(WebDriver driver, String tableName) {
		int tableCount = this.getCountByTable(tableName);
		int randomIndex = 0;
		Random myrand = new Random();
		if(tableCount >=30) {
			randomIndex = myrand.nextInt(30) + 1;			
		}
		else {
			randomIndex = myrand.nextInt(tableCount) + 1;
		}
		return randomIndex;
	}
	//获取到表格指定的行列（单元格）内容
	public String getTableCellText(WebDriver driver, String tableId, int row, int col) {
		//WebElement tablepath = driver.findElement(By.xpath("//tbody[@id='dataPanel']/tr[3]/td[2]"));
		String table = driver.findElement(By.xpath("//tbody[@id='dataPanel']/tr["+ row +"]/td["+col+"]")).getText();
		String xpthExp = "//tbody[@id='dataPanel']/tr["+ row +"]/td["+col+"]";
		WebElement tb = driver.findElement(By.xpath(xpthExp));
		return tb.getText();
	}
	
	//生成随机日期来解决过期日期的问题
	public String getRandomDate(WebDriver driver) {
		//生成一个随机天数，范围设置为1-30天
		int days = (int)(Math.random() * 29) + 1;
		//创建Calendar对象实例
		Calendar cal = Calendar.getInstance();
		//随机生成的天数增加进cal对象中
		cal.add(cal.DATE, days);
		//利用日期格式化输入
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String expireDate = format.format(cal.getTime());
		
		return expireDate;
		
	}
	//对文本内容随机组合
	public String getRandomText(WebDriver driver) {
		//实例化Random随机数对象
		Random myrandom = new Random();
		
		String[] textList = {
				"我本将心照明月，奈何明月照沟渠",
				"这星期就下了两场雨，第一场三天，第二产四天",
				"人和猪的区别就是：猪一直是猪，而人有时却不是人",
				"永远有多远，你小子就给我滚多远",
				"自从我变成了狗屎，就再也没有人踩在我头上了",
				"天啊！我的衣服又瘦了",
				"人生没有彩排，每天都是直播；不仅收视率低，而且工资不高",
				"见到我以后你会突然发现--啊，原来帅也可以这样的具体",
				"人生就像一个茶几虽然不大，但是充满了杯具",
				"问：你喜欢我哪一点？答：我喜欢你离我远一点",
		};
		int index = myrandom.nextInt(textList.length);
		String randText = textList[index];
		
		//定义一个随机的6位数字
		
	   int randNum = myrandom.nextInt(900000) + 100000;
	   
	   return randText + "-" + randNum;
			   
	}
	
	
	
	
	
	
	



}
