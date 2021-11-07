package com.woniuxy.cbt.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class DDTDemo {
	
	//�������ݿ������ַ�����Ϣ
	private String driverClassName = "com.mysql.jdbc.Driver";
	private static String url ="jdbc:mysql://localhost:3306/test?"
			+"user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	public static void main(String[] args) {
		DDTDemo ddt = new DDTDemo();
		
	}
	
	//�Ӽȶ��ַ����������������һ��
	public String getRandomByArray() {
		String[] msgliSt = {
			"�ұ����������£��κ������չ���",
			"�����ھ����������꣬��һ�����죬�ڶ�������",
			"�˺����������ǣ���һֱ����������ʱȴ������",
			"��Զ�ж�Զ����С�Ӿ͸��ҹ���Զ",
			"�Դ��ұ�ɵĹ�ʺ������Ҳû���˲�����ͷ����",
			"�찡���ҵ��·�������",
			"����û��Ŷ���ţ�ÿ�춼��ֱ�������������ʵͣ����ҹ��ʲ��ߡ�",
			"�������Ժ����ͻȻ���֣�����ԭ��˧Ҳ������ô����ѽ��",
			"��������һ���輸����Ȼ���󣬵��ǳ����˱��硣",
			"�ʣ���ϲ������һ�㣿����ϲ��������Զһ�㣡",			
		};
		Random random = new Random();
		int randNum = Math.abs(random.nextInt(msgliSt.length));
		return msgliSt[randNum];
		
	}
	//ָ����Χ��������� 100-220  120:0~199
		public int getRandomByInt(int min, int max) {
			Random myrand = new Random();
			int temp = myrand.nextInt(min);
			int random = temp +(max - min) + 1;
			return random;
		}
	//���������ڶ������ַ���
		public String getRandomByString() {
			String[] array1 = {"�ڻ�԰��","�ڲݵ���","�ڻʹ���","��������","�ں�԰",
					"�ڵϰ�","�ڻ���ɽ","��ɽ����","��������","�ڻ���","�ڷ�����","������"};
		String[] array2 = {"�����","��С��","���϶�","����ʩ","����Ψ","��Ÿ�","�붡ԪӢ",
					"�뷨��","��ϰ��ƽ","����","�����","����","�����","������"};
		String[] array3 = {"�Է�","���","����Ӱ","�Ⱦ�","����","���","����","����","����Ϸ","����","͵��",
				"����","����","Ҫ��","ˣ��","��ש","���","�ɻ�","�ֵ�","ϴ��","��ˮ","����","˽��","Լ��"};
		String randomString = "";
		for(int i=0; i<10; i++) {
			Random myRandom = new Random();
			int index1 = myRandom.nextInt(array1.length);
			int index2 = myRandom.nextInt(array2.length);
			int index3 = myRandom.nextInt(array3.length);
			randomString += array1[index1] + array2[index2] + "һ��" + array3[index3] + this.getRandomByInt(10000, 9999);
		}
		return randomString;
	}
	//��������������ݱ��浽��ά������
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
		
		//���������ݿ�֮�������
		public Connection getConnection() {
			Connection conn = null;
			try {			
				Class.forName(driverClassName).newInstance();
			    //new com.mysql.jdbc.Driver(); Ҳ����ֱ������ʵ����
			    conn = DriverManager.getConnection(url);  // �������ݿ�����
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return conn;			
		}
}