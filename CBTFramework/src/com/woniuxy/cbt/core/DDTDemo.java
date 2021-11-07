package com.woniuxy.cbt.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class DDTDemo {
	
	//定义数据库连接字符串信息
	private String driverClassName = "com.mysql.jdbc.Driver";
	private static String url ="jdbc:mysql://localhost:3306/test?"
			+"user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	public static void main(String[] args) {
		DDTDemo ddt = new DDTDemo();
		
	}
	
	//从既定字符串数组中随机返回一个
	public String getRandomByArray() {
		String[] msgliSt = {
			"我本将心向明月，奈何明月照沟渠",
			"这星期就下了两场雨，第一场三天，第二场四天",
			"人和猪的区别就是，猪一直是猪，而人有时却不是人",
			"永远有多远，你小子就给我滚多远",
			"自从我变成的狗屎，就再也没有人踩在我头上了",
			"天啊！我的衣服有瘦了",
			"人生没有哦彩排，每天都是直播；不仅收视率低，而且工资不高。",
			"见到我以后你会突然发现，啊，原来帅也可以这么具体呀！",
			"人生就像一个茶几，虽然不大，但是充满了悲剧。",
			"问：你喜欢我哪一点？答：我喜欢你离我远一点！",			
		};
		Random random = new Random();
		int randNum = Math.abs(random.nextInt(msgliSt.length));
		return msgliSt[randNum];
		
	}
	//指定范围的随机整数 100-220  120:0~199
		public int getRandomByInt(int min, int max) {
			Random myrand = new Random();
			int temp = myrand.nextInt(min);
			int random = temp +(max - min) + 1;
			return random;
		}
	//返回数量众多的随机字符串
		public String getRandomByString() {
			String[] array1 = {"在花园里","在草地上","在皇宫里","在凌霄殿","在后花园",
					"在迪拜","在花果山","在山洞里","在你心里","在火车上","在房顶上","在雨中"};
		String[] array2 = {"与李白","与小彭","与嫦娥","与西施","与汤唯","与杜甫","与丁元英",
					"与法海","与习近平","马云","李彦宏","马化腾","刘存浩","许三多"};
		String[] array3 = {"吃饭","逛街","看电影","喝酒","聊天","打架","画画","读书","玩游戏","下棋","偷桃",
				"卖肉","练武","要饭","耍宝","搬砖","买菜","干活","种地","洗澡","划水","逃跑","私奔","约会"};
		String randomString = "";
		for(int i=0; i<10; i++) {
			Random myRandom = new Random();
			int index1 = myRandom.nextInt(array1.length);
			int index2 = myRandom.nextInt(array2.length);
			int index3 = myRandom.nextInt(array3.length);
			randomString += array1[index1] + array2[index2] + "一起" + array3[index3] + this.getRandomByInt(10000, 9999);
		}
		return randomString;
	}
	//将整个数组的内容保存到二维数组中
		public String[] getDatbaseData() {
			String sql = "select * from user where userid=?";
			String[] data = new String[4];
			try {
				PreparedStatement ps =this.getConnection().prepareStatement(sql);
				ps.setInt(1, 5);
				ResultSet rs = ps.executeQuery();
				rs.first();
				data[0] =rs.getString("username");
				data[1] =rs.getString("password");
				data[2] =rs.getString("blance");
				data[3] =rs.getString("phone");
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return data;		
		}
		
		public String[] getDatabaseData2(String sql, String[] cols) {
			String[] data = new String[cols.length];
			try {
				PreparedStatement ps = this.getConnection().prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				rs.first();
				for(int i=0; i<cols.length; i++) {
					data[i] =rs.getString(cols[i]);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		
		//建立与数据库之间的连接
		public Connection getConnection() {
			Connection conn = null;
			try {			
				Class.forName(driverClassName).newInstance();
			    //new com.mysql.jdbc.Driver(); 也可以直接这样实例化
			    conn = DriverManager.getConnection(url);  // 建立数据库连接
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return conn;			
		}
}