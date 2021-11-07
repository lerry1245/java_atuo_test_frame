package com.woniuxy.http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.chrono.HijrahChronology;

public class AgileoneTest {
	HttpRequest request = new HttpRequest();
	String cookie = this.getLoginCookie();

	public static void main(String[] args) {
		AgileoneTest at = new AgileoneTest();
		at.testAddNotice();
		at.testAddProposal();
//		at.testDeletProposal();
//		at.testEditProposal();
		at.testQueryProposal();

	}
    //测试增加公告接口
	public void testAddNotice() {
		//String cookie = this.getLoginCookie();
		String postUrl2 = "http://localhost/agileone/index.php/notice/add";
		String postParam2 = "headline=This is a notice&content=This is content of notice&scope=0&expireddate=2021-11-15"
				+ "&" + cookie;
		System.out.println(postParam2);
		String resp = request.sendPost(postUrl2, postParam2);
		System.out.println(resp);
		if (resp.replace("\n", "").matches("(.*)(\\d{1,5})(.*)")) {
			System.out.println("添加公告-成功");
		} else {
			System.out.println("添加公告-失败");
		}
	}
	
	
	
	//测试删除需求提案接口
	public void testDeletProposal() {
		
		
		String postUrl = "http://localhost/agileone/index.php/proposal/delete";
		String postParam ="proposalid=19"+cookie;
		String respdele = request.sendPost(postUrl, postParam);
		System.out.println(respdele);
		if((respdele.replace("\n", "")).equals("1")) {
			System.out.println("删除提案测试_成功");
		}
		else{
			System.out.println("删除提案测试_失败");
		}
	}
	//测试编辑需求提案接口
	public void testEditProposal() {
		long sequence = System.currentTimeMillis();
		String postUrl = "http://localhost/agileone/index.php/proposal/edit";
		String postParam ="proposalid=12&type=Requirement&importance=Medium&headline=这是一条需求提案-"+sequence+"&"
				+"content=这是需求提案的内容edit&processresult=;"+cookie;
		String respedit = request.sendPost(postUrl, postParam);
		System.out.println(respedit);
		if((respedit.replace("\n", "")).equals("1")) {
			System.out.println("编辑提案测试_成功");
		}
		else{
			System.out.println("编辑提案测试_失败");
		}
	}
	
	//测试查询需求提案接口
	public void testQueryProposal() {
		String postUrl = "http://localhost/agileone/index.php/proposal/query";
		String postParam ="proposalid=11;"+cookie;
		String respedit = request.sendPost(postUrl, postParam);
		System.out.println(respedit);
		if((respedit.replace("\n", "")).contains("\"totalRecord\":1")) {
			System.out.println("查询需求提案测试_成功");
		}
		else{
			System.out.println("查询需求提案测试_失败");
		}
	}
	
	//获取登录时的loginsession
	public String getLoginCookie() {
		String postUrl = "http://localhost/agileone/index.php/common/login";
		String postParam = "username=admin&password=admin";
		String respLogin = request.sendPost(postUrl, postParam);
		String respcookie = request.cookie;
		//System.out.println(respcookie);
		return respcookie;
		
	}
    //增加需求提案接口测试
	public void testAddProposal() {
//		int countBerfore = this.getCountFromDB("select count(*) from proposal");
		long sequence = System.currentTimeMillis(); // 从Unix元年（1970-1-1 0:0:0:0）到现在的毫秒数		
		String headline = "这是一条需求提案-" + sequence;
		//System.out.println(headline);
		String postUrl = "http://localhost/agileone/index.php/proposal/add";
		String postParam = "type=Requirement&importance=Medium&headline="+headline+"&content=这是需求提案的内容&processresult=;"+cookie;
//		String postParam = cookie+"type=Requirement&importance=Medium&headline="+headline+"&content=这是需求提案的内容&processresult=";
		String resp = request.sendPost(postUrl, postParam);
		System.out.println(resp);
		// 第一种检查手段：正向检查响应的内容：使用正则表达式
//		if (resp.replace("\n", "").matches("(.*)(\\d{1,5})(.*)")) { // \d或[0,9]:表达一个数字，
//			System.out.println("新增需求提案-正向检查-成功");
//		} else {
//			System.out.println("新增需求提案-正向检查-失败" + resp);
//		}
		// 第二种检查手段，逆向判断，如果没有出现错误的响应，则说明正常
//		if (resp.contains("no_permisson") || resp.contains("proposal_exist") || resp.contains("rang_passeord")) {
//			System.out.println("新增需求提案-逆向检查-失败" + resp);
//		} else {
//			System.out.println("新增需求提案-逆向检查-成功");
//		}
//		// 第三种：通过查询来判断
//		String queryURL = "http://localhost:8081/agileone/index.php/proposal/query";
//		String queryParam = "currentpage=1&proposalid=&creator=admin&type=Requirement&importance=Medium&headline=&content=&processresult=";
//		String respQuery = request.sendPost(queryURL, queryParam); // 查询新增的公告
//		String checkStr = "{\"totalRecord\":"; // 返回结果中字符
//		int posLeft = respQuery.indexOf(checkStr) + checkStr.length(); // 拿到查询字符的坐下标
//		int posRight = respQuery.indexOf("}"); // 拿到查询字符的右下标
//		String destStr = respQuery.substring(posLeft, posRight); // 获取目标字符串
//		if (Integer.parseInt(destStr) == 1) {
//			System.out.println("新增需求提案-查询检查-成功");
//		} else {
//			System.out.println("新增需求提案-查询检查-失败");
//		}
//
//		// 第四种：直接通过数据来判断，利用JDBC操作MYSQL数据库
//		int countAfter = this.getCountFromDB("select count(*) from proposal");
//		if (countAfter == (countBerfore + 1)) {
//			System.out.println("新增需求提案-数据库检查-成功");
//		}
//
//		else {
//			System.out.println("新增需求提案-数据库检查-失败");
//		}
//
		// 第四种扩展：直接查看标题是否存在数据库中

		if (this.checkCountFromDB("select headline from proposal where headline =' 标题 ' ", "标题")) {
			System.out.println("新增需求提案-数据库检查2-成功");

		} else {
			System.out.println("新增需求提案-数据库检查2-成功");
		}

	}

	public int getCountFromDB(String sql) {
		String driverClassName = "com.mysql.Driver";
		String url = "jdbc:myql://localhost:3306:/agileone?"
				+"user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";
		Connection conn = null;
		int count = 0;
		String actual = "";

		try {
			Class.forName(driverClassName).newInstance(); // 实例化驱动程序
			conn = DriverManager.getConnection(url); // 建立数据库连接
			Statement stmt = conn.createStatement(); // 定义一个SQL语句执行器
			ResultSet rs = stmt.executeQuery(sql); // 执行SQL语句，并返回结果
			rs.next(); // 将结果集的游标定位到第一行
			count = rs.getInt(1); // 读取第一列的数据
			actual = rs.getString("headline"); // 读取第一列的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}

	public boolean checkCountFromDB(String sql, String expect) {
		String driverClassName = "com.mysql.Driver";
		String url = "jdbc:mysql://localhost:3306/agileone?"
				+"user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";
		Connection conn = null;
		String actual = "";

		try {
			Class.forName(driverClassName).newInstance(); // 实例化驱动程序
			conn = DriverManager.getConnection(url); // 建立数据库连接
			Statement stmt = conn.createStatement(); // 定义一个SQL语句执行器
			ResultSet rs = stmt.executeQuery(sql); // 执行SQL语句，并返回结果
			rs.next(); // 将结果集的游标定位到第一行
			actual = rs.getString("headline"); // 读取第一列的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (actual.equals(expect)) {
			return true;
		} else {
			return false;
		}

	}

}
