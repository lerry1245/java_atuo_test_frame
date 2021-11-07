package com.woniuxy.cbt.test;

import com.woniuxy.cbt.core.Common;
import com.woniuxy.cbt.core.SeleniumDriver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class KDTDemo {
	
	//定义二维数组logins用以代码关键字驱动
	private String[][] logins = {
			{"dologin","admin","wrongpwd"},
			{"dologin","wronguser","admin"},
			{"dologin","admin",""},
			{"dologin","admin","admin"}};
	
	public static void mian(String[] args) {
		System.out.println("关键字驱动");
//		KDTDemo kdt = new KDTDemo();
//		kdt.startLogin();
//		kdt.startAdd();
//		kdt.testChineseKey();
		
//		System.out.println(kdt.add("可变参数一",100));
//		System.out.println(kdt.add("可变参数一",100,200));
//		System.out.println(kdt.add("可变参数一",100,200,300));
	}
	
	public void startLogin() {
		SeleniumDriver sd = new SeleniumDriver("ie");
		sd.getWebDriver().get("http://localhost/agileone/");
		try {
			Class<?> clazz = Class.forName("com.woniuxy.cbt.test.AgileoneNotice");
			Object instance = clazz.newInstance();
			for(int i=0; i<logins.length; i++) {
				Method doLogin = clazz.getMethod(logins[i][0], 
						String.class ,String.class);
				doLogin.invoke(instance, logins[1][0],logins[1][2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startAdd() {
		Common common = new Common();
		String random =String.valueOf(common.getRandom(10000, 99999));
		
		SeleniumDriver sd =new SeleniumDriver("ie");
		sd.getWebDriver().get("http://localhost/agileone/");
		
		String userDir = System.getProperty("user.dir");
		String xlsFile = userDir + "\\data\\NoticeKeyword.xls";
		String[][] keywords = common.readExcel(xlsFile, "sheet1");
		
		try {
			Class<?> clazz = Class.forName("com.woniuxy.cbt.test.AgileoneNotice");
			Object instance = clazz.newInstance();
			
			for(int i=0; i<keywords.length;i++) {
				Method method = clazz.getDeclaredMethod(keywords[i][0], String.class, String.class);
				method.setAccessible(true);
				
				String param1 = keywords[i][1].replace("$random", random);
				String param2 = keywords[i][2].replace("$random", random);
				method.invoke(instance,param1,param2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	//定义可变参数时，在类型和参数数组名之间必须使用...
	public int add(String v, int...args) {
		System.out.println(v);
		int sum = 0;
		for(int i=0; i<args.length; i++) {
			sum += args[i];			
		}
		return sum;
	}
	
	//定义中文关键字驱动测试程序
	public void testChineseKey() {
		Map chineseKey = new HashMap();
		chineseKey.put("登录", "doLogin");
		chineseKey.put("新增公告", "doAdd");
		
		Common common = new Common();
		String random = String.valueOf(common.getRandom(10000, 9999));
		
		SeleniumDriver sd =new SeleniumDriver("ie");
		sd.getWebDriver().get("http://localhost/agileone/");
		
		String userDir = System.getProperty("user.dir");
		String xlsFile = userDir + "\\data\\NoticeKeyword.xls";
		String[][] keywords = common.readExcel(xlsFile, "sheet2");
		
		try {
			Class<?> clazz = Class.forName("com.woniuxy.cbt.test.agileoneNotice");
			Object instance = clazz.newInstance();
			
			for(int i=0; i<keywords.length;i++) {
				String key = chineseKey.get(keywords[i][0]).toString();
				Method method = clazz.getDeclaredMethod(key, String.class, String.class);
				method.setAccessible(true);
				
				String param1 = keywords[i][1].replace("$random", random);
				String param2 = keywords[i][2].replace("$random", random);
				method.invoke(instance,param1,param2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
}
