package com.woniuxy.cbt.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Common {
		
		//定义一个暂停脚本运行的方法，并且生成一个指定范围内的随机暂停时间
		public void sleepRandom(int min, int max) {
			Random myrand = new Random();
			int temp = myrand.nextInt(min);
			int time = temp + (max - min) +1;
			try {
				Thread.sleep(time*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//指定范围的随机数 100-220  120:0~199
		public int getRandom(int min, int max) {
			Random myrand = new Random(); 
			int gap = max - min;
			int temp = myrand.nextInt(gap);
			int random = temp + min;
			return random;
		}
		
		//生成当前的日期或时间字符串
		public String getDateTine(String format) {
			SimpleDateFormat formatter = new SimpleDateFormat();
			Date date = new Date();
			String now = formatter.format(date.getTime());
			return now;
		}
		//读取关键配置信息
		public String getConfigData(String Key) {
			Properties prop = new Properties();
			String value = "";
			String userDir = System.getProperty("user.dir");
			String file = userDir + "\\data\\global.conf";
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStream in = new BufferedInputStream(fis);
				prop.load(in);
				value = prop.getProperty(Key);
			}catch (IOException e) {
				System.out.println(e.getMessage());
			}
			return value;
		}
		
		// 根据正则表达式的左右边界来获取被其中夹的值
		public String getItem(String source, String left, String right) {
			String data = "";
			String regex = "("+ left +")(.+/?)("+ right +")";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(source);
			if (m.find()) {
				data = m.group(2);
			}
			return data;
		}
		
		//根据正则表达式的左右边界来获取所有被其中夹着的值，并以list类型保存
		public List<String> getItems(String response, String left, String right){
			List<String> data = new ArrayList<String>();
			//(.+?)表示使用非贪婪模式查找被左右边界包裹的最小数据内容
			String regex = "("+ left +")(.+/?)("+ right +")";
			//设置匹配模式为Pattern.DOTALL.此时，可以匹配换行符\n
			//Pattern p = Pattern.compile(regex, Pattern.DOTALL);
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(response);
			while (m.find()) {
				data.add(m.group(2));
			}
			return data;
		}
		
		//根据一个响应的JSon字符串处理为一个List<String, Map>对象返回
		public List<Map<String, String>> getJsonList(String source){
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {
				JSONArray ja = new JSONArray(source);
				//此处循环少一次的目的是去除JSON数据最后一条
				for(int i=0; i<ja.length()-1; i++) {
					Map<String, String> map = new HashMap<String, String>();
					JSONObject jo = ja.getJSONObject(i);
					Iterator keys = jo.keys();
					while (keys.hasNext()) {
						String key = keys.next().toString();
						map.put(key, jo.get(key).toString());
					}
					list.add(map);
					
				}
			}catch (JSONException e) {
				System.out.println(e.getMessage());
			}
			return list;
		}
		
		//读取Excel并返回到二维数组中
		public String[][] readExcel(String fileNnmae, String sheetName){
			File file = new File(fileNnmae);
			String[][] data = null;
			try {
				FileInputStream fis = new FileInputStream(file);
				jxl.Workbook rwb = Workbook.getWorkbook(fis);
				
				Sheet sheet = rwb.getSheet(sheetName);
				//获取到sheet中的行和列总数量，用于定义二维数组
				int rowCount = sheet.getRows();
				int colCount = sheet.getColumns();
				//默认情况下，第一行定义列名，所以数据内容应该会少一行
				data = new String[rowCount-1][colCount];
				
				//遍历表格中的行和列，并赋值给data数组
				//循环从i=1开始，是为了让开表格第一行
				for(int i=1; i<rowCount; i++) {
					Cell[] cells = sheet.getRow(i);
					//遍历当前行中的每一列，并赋值给data数组
					for(int j=0; j<cells.length; i++) {
						data[i-1][j] = cells[j].getContents();
					}
				}
				fis.close();
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return data ;
		}
		
		//调用WinRar压缩命令压缩测试报告所在的目录
		public void compressByWinrar() {
			String userDir = System.getProperty("user.dir");
			String folder = userDir + "\\report\\" + Reporter.folder;
			String rarName = Reporter.folder + ".rar";
			String command = "\"c:\\Program Files (x86)\\WinRAR.exe\" a";
			command += userDir + "\\report\\" + rarName + " " + folder;
			try {
				Runtime.getRuntime().exec(command);
			}catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//调用javamail组件实现邮件发送
		public void sendRrportByEmail() {
			try {
				//第一步，配置邮件会话信息：如服务器，协议类型等
				Properties props = new java.util.Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", "smtp.exmail.qq..com");
				props.put("mail.transport.protocal", "smtp");
				
				Session mailSession = Session.getDefaultInstance(props);
				mailSession.setDebug(false);
				
				//第二步，处理发件人与收件人信息
				Address sender = new InternetAddress("your sendEmail account");
				Address receiver1 = new InternetAddress("myworkmail@qq.com");
				Address receiver2 = new InternetAddress("otherworkmail@qq.com");
				Address[] receivers = {receiver1,receiver2};
				
				MimeMessage mimiMessage = new MimeMessage(mailSession);
				mimiMessage.setFrom(sender);
				mimiMessage.addRecipients(Message.RecipientType.TO, receivers);
				
				//第三步：设置邮件的标题与内容
				mimiMessage.setSubject("Agileone自动化测试报告");
				
				String mailbody =
					"<font face ='微软雅黑'>被测试对象：Agileone" + Reporter.version+".<br>";
				mailbody += "测试时间：" + this.getDateTine("yyyy-MM-dd HH:mm:ss");
				mailbody += ".<br>持续时间：" + Reporter.duration/1000 + " 秒.<br>";
				
				mailbody += "========================================<br>";
				mailbody += "针对具体的侧四结果及错误详情，请查阅邮件附件.<br>";
				mailbody += "本邮件有Java自动化测试程序发出，勿需回复!</font>";
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(mailbody, "text/html;charset=UTF-8");
			
			//第四步：将HTML测试报告的压缩文件作为邮件附件发送
			MimeBodyPart fileBodyPart = new MimeBodyPart();
			String userDir = System.getProperty("user.dir");
			String rarFile = userDir + "\\report\\" + Reporter.folder +".rar";
			FileDataSource fileDataSource = new FileDataSource(rarFile);
			fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
			fileBodyPart.setFileName(fileDataSource.getName());
			
			//第五步：将邮件正文和邮件附件添加进邮件中
			Multipart container = new MimeMultipart();
			container.addBodyPart(textBodyPart);
			container.addBodyPart(fileBodyPart);
			
			mimiMessage.setContent(container);
			mimiMessage.saveChanges();
			
			//第六步：设置邮件账号信息并正式发送邮件
			Transport transport = mailSession.getTransport("smtp");
			transport.connect("smtp.exmail.qq.com","你的登录邮箱账号","邮箱密码");
			transport.sendMessage(mimiMessage, mimiMessage.getAllRecipients());
			transport.close();
			
			System.err.println("已经将HTML测试报告成功发送出.");
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
}
