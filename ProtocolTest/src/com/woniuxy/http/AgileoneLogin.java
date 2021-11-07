package com.woniuxy.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AgileoneLogin {
	AgileoneTest agt = new AgileoneTest();
	HttpRequest request = new HttpRequest();

	public static void main(String[] args) {
		AgileoneLogin it = new AgileoneLogin();
		it.doLogin("admin", "admin","successful");
//		it.doLogin("admin1", "admin","user_invalid");
//		it.doLogin("admin", "admin1","password_invalid");
//		it.testDologinByArray();
//		it.testDologinByJDBC();
//		it.testDologinBytext();
//		it.bufferRead();
//		it.foreCrack();

	}
	public void testDologinByArray() {
		String[][]  testdata = {{"admin1", "admin1","successful"},
				                {"admin1", "admin","user_invalid"},
				                {"admin", "admin1","password_invalid"}
				                };
		for(int i=0; i<testdata.length;i++) {
			String username = testdata[i][0];
			String password = testdata[i][1];
			String expect = testdata[i][2];
			boolean result = this.doLogin(username, password, expect);
			System.out.println(result);
		}	                   	
	}
	public void testDologinByJDBC() {
		String driverClassName = "com.mysql.jdbc.Driver";
	    String url ="jdbc:mysql://localhost:3306/test_project?"
				+"user=root&password=wen123456&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";
		try {
			Class.forName(driverClassName).newInstance();
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			String sql ="select username,password,expect from user";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				String expect = rs.getString("expect");
				boolean result = this.doLogin(username, password, expect);
				System.out.println(result);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testDologinBytext() {		
		try {
			String path = System.getProperty("user.dir");
			File file = new File(path+"\\data\\testdata.csv");
			InputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader wr = new BufferedReader(reader);
			String line;
			while((line = wr.readLine()) != null){
				String[] data = line.split(",");
				boolean result = this.doLogin(data[0], data[1], data[2]);
				System.out.println(result);
			}
            in.close();
			reader.close();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean doLogin(String username,String password,String expect) {
		String postUrl = "http://localhost:8081/agileone/index.php/common/login";
		String postParam ="username="+username+"&password="+password;
		String resp = request.sendPost(postUrl, postParam);
		if (resp.contains(expect)) {
			return true;
		}
		else {
			return false;
		}
	}
	public void foreCrack() {
		HttpRequest hr = new HttpRequest();
		String driverClassName = "com.mysql.jdbc.Driver";
	    String url ="jdbc:mysql://localhost:3306/test_project?"
				+"user=root&password=wen123456&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";
		try {
			Class.forName(driverClassName).newInstance();
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			String sql ="select username,password from user";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				String postUrl = "http://localhost/agileone/index.php/common/login";
				String postParam ="username="+username+"&password="+password;
				String resp = request.sendPost(postUrl, postParam);
				System.out.println(resp);
				if(resp.contains("successful")) {
					System.out.println("暴力破解成功! 用户名："+username+"密码："+password);
					break;
				}
				else {
					System.out.println("正在暴力破解...");
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean doLogin(String username,String password) {
		String postUrl = "http://localhost:8081/agileone/index.php/common/login";
		String postParam ="username="+username+"&password="+password;
		String resp = request.sendPost(postUrl, postParam);
		if (resp.contains("successful")) {
			return true;
		}
		else if(resp.contains("user_invalid")){
			return true;
		}
		else if(resp.contains("password_invalid")){
			return true;
		}
		else {
			System.out.println("登录接口测试-失败。");
			return false;
		}	
	
	}
}
