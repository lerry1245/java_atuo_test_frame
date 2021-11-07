package com.woniuxy.http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AgileoneCommon {
	private static String driverClassName = "com.mysql.jdbc.Driver";
	//定义数据库连接字符串
	private static String url ="jdbc:mysql://localhost:3306/agileone?"
			+"user=root&password=wen123456&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	
	
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
    //获取一列
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



}
