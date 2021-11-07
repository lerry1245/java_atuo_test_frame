package com.woniuxy.socket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.woniuxy.http.HttpRequest;

public class JsonUsage {

	public static void main(String[] args) {
		
		JsonUsage ju = new JsonUsage();
//		ju.mapBasic();
//		ju.string2Json();
//		ju.obj2String();
//		ju.rs2string();
		ju.response2Json();
		
	}
		
	public void mapBasic() {
		List<Map<String,String>> list = new  ArrayList<Map<String, String>>();
		Map<String,String> map1 = new HashMap<String, String>();
		map1.put("name", "张三");
		map1.put("sex","male");
		map1.put("age", "30");
		map1.put("phone","15413265484");
		map1.put("addr", "Shenzhen");
		map1.put("school","Wuhan university");
		
		Map<String,String> map2 = new HashMap<String, String>();
		map2.put("name", "李四");
		map2.put("sex","male");
		map2.put("age", "27");
		map2.put("phone","15419875485");
		map2.put("addr", "Hangzhou");
		map2.put("school","Zhejiang university");
		
		list.add(map1);
		list.add(map2);
		
		for(int i=0; i<list.size();i++) {
			Map<String,String> map = list.get(i);
			for(Map.Entry<String, String> entry: map.entrySet()) {
				System.out.print(entry.getKey()+ ":" + entry.getValue()+ ",");
				
			}
			System.out.println();
		}
		Map<String, List> map3 = new HashMap<String, List>();
	}
		
	
	public void string2Json() {
		String source1 = "{'phone':'15413265484' , 'school':'Wuhan university' , 'sex':'male' , 'name':'张三'"
				+ " , 'addr':'Shenzhen' , 'age':'30'}";
		
		String source2 = "["
				+ "{'name':'张三' , 'sex':'male', 'age':'25','addr':'西安'},"
				+ "{'name':'李四'  , 'sex':'female', 'age':'22','addr':'北京'},"
				+ "{'name':'王五'  , 'sex':'male', 'age':'29','addr':'上海'}]";
		
		try {
			JSONObject jsbj = new  JSONObject(source1);
			System.out.println(jsbj.get("school"));
			System.out.println("=======================");
			
			JSONArray ja = new JSONArray(source2);
			for(int i=0;i<ja.length();i++) {
				JSONObject jo = ja.getJSONObject(i);
				System.out.print(jo.get("name"));
				System.out.print(jo.get("age"));
				System.out.println();
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
				
		
	}
	
	//通常在服务器端将某些对象生成json字符串在传给客户端
	public void obj2String() {
		Map<String,String> user1 = new HashMap<String, String>();
		user1.put("name", "张三");
		user1.put("sex","male");
		user1.put("age", "30");
		user1.put("phone","15413265484");
		user1.put("addr", "Shenzhen");
		user1.put("school","Wuhan university");
		
		Map<String,String> user2 = new HashMap<String, String>();
		user2.put("name", "李四");
		user2.put("sex","male");
		user2.put("age", "27");
		user2.put("phone","15419875485");
		user2.put("addr", "Hangzhou");
		user2.put("school","Zhejiang university");
		
		String[] names = {"张三", "李四", "王武", "赵丽"};
		
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("user-1", user1);
			jo.put("user-2", user2);
			jo.put("user-name", names);
			System.out.println(jo.toString());
		} catch (JSONException e) {
						e.printStackTrace();
		}
		
	}
	//把数据库查询而来的结果集ResultSet转换为json字符串	
	public void rs2string() {
		HttpRequest hr = new HttpRequest();
		String driverClassName = "com.mysql.jdbc.Driver";
	    String url ="jdbc:mysql://localhost:3306/test_project?"
				+"user=root&password=wen123456&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";
		try {
			Class.forName(driverClassName).newInstance();
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			String sql ="select * from user";
			ResultSet rs = stmt.executeQuery(sql);
			
			//获取到rs中的原数据
			ResultSetMetaData meta = rs.getMetaData();
			//获取列的总数
			int colCount = meta.getColumnCount();
			String jsonString = "";
			
			while(rs.next()) {
			JSONObject jo = new JSONObject();
			//JSONArray ja = new JSONArray();
			for(int i=1; i<colCount;i++) {
				String colName = meta.getColumnLabel(i);
				jo.put(colName, rs.getString(colName));
				//ja.put(rs.getString(colName));	
			}   
			    jsonString += jo.toString();
				//jsonString += ja.toString();
			
				}
			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//处理服务端响应
	public void response2Json() {
		com.woniuxy.http.HttpRequest hr = new HttpRequest();
		String postUrl = "http://www.woniuxy.com/SCourseByTyServlet";
		String postParam = "page=1";
		String response = hr.sendPost(postUrl, postParam);
		System.out.println(response);
//		try {
//			JSONObject jo = new JSONObject(response);
//			System.out.println(jo.get("name"));
//			JSONArray ja = jo.getJSONArray("roys");
//			for(int i=0; i<ja.length();i++) {
//				//System.out.println(ja.get(i));
//				//System.out.println(ja.getString(i));
//				JSONObject jotemp = new JSONObject(ja.getString(i));
//				System.out.println(jotemp.get("description"));
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	

}
