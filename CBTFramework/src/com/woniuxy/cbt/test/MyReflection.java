package com.woniuxy.cbt.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.woniuxy.cbt.core.ExcelReader;
import com.woniuxy.cbt.core.ExcelWriter;




public class MyReflection {
	public static AgileonePropGUI web;
	public static String filepath;

	public static void mainrun(String type) throws Exception {
		// 获取项目的绝对路径
		filepath =System.getProperty("user.dir");
		// 根据不同的自动化类型，拼接用例文件和结果文件的路径
		String filename = "/cases/";    //源文件
		String fileres = "/result/result-";   //复制的目标文件
		//
		try {
			System.out.println("log:info：文件路径：" + filepath);
			String date = createDate("yyyyMMdd+HH-mm-ss");
			String starttime=createDate("yyyy-MM-dd HH:mm:ss");
			switch (type) {  
			case "web":
				filename += "WebCases.xlsx";
				fileres += "WebCases" + date + ".xlsx";
				break;
			case "http":
				filename += "HTTPLogin.xlsx";
				fileres += "HTTPLogin" + date + ".xlsx";
				break;
			case "app":
				filename += "APPCases.xlsx";
				fileres += "APPCases" + date + ".xlsx";
				break;
			case "soap":
				filename += "SOAPLogin.xlsx";
				fileres += "SOAPLogin" + date + ".xlsx";
				break;
			default:
				filename += "webCases.xls";
				fileres += "webCases.xls";
				System.out.println("log:error：类型错误！已经默认执行UI自动化。");
				break;
			}
			System.out.println("用例文件路径：" + filepath + filename);
			GetCase(filepath + filename, filepath + fileres, type);// 设置用例文件的路径
		} catch (Exception e) {
			System.out.println("log:error：获取文件位置失败，请检查。");
			e.printStackTrace();
		}

		System.out.print("输入回车，退出...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void GetCase(String file, String fileres, String type) {
		// 打开Excel
		ExcelReader excelr = new ExcelReader(file);
		ExcelWriter excelw = new ExcelWriter(file, fileres);

		List<String> list = null;
		// 根据传入的用例和结果文件路径，实例化关键字类的成员变量
		
		web = new AgileonePropGUI(excelw);
		
		
		for (int line = 0; line < excelr.rows; line++) {
			list = excelr.readLine(line);
			System.out.println(list);
			if ((list.get(0) != null || list.get(1) != null)
					&& (!list.get(0).equals("null") || !list.get(1).equals("null"))
					&& (list.get(0).length() > 0 || list.get(1).length() > 0)) {
				;
			} else {
				switch (type) {
				case "web":
					web.line = line;
					runUIWithInvoke(list);
					break;
				default:
					break;
				}
			}
		}
		excelr.close();
		excelw.save();
	}
	private static void runUIWithInvoke(List<String> list) {
		try {
			//
			Method webMethod = web.getClass().getDeclaredMethod(list.get(3).toString());
			// invoke语法，需要输入类名以及相应的方法用到的参数
			webMethod.invoke(web);
			return;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(list.get(3).toString(), String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			uis.invoke(web, list.get(4));
			return;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(list.get(3).toString(), String.class, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			uis.invoke(web, list.get(4), list.get(5));
			return;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(list.get(3).toString(), String.class, String.class,
					String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			uis.invoke(web, list.get(4), list.get(5), list.get(6));
			return;
		} catch (Exception e) {
		}
	}
	
	private static String createDate(String dateFormat) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String createdate = sdf.format(date);
		return createdate;
	}

}
