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
		
		//����һ����ͣ�ű����еķ�������������һ��ָ����Χ�ڵ������ͣʱ��
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
		//ָ����Χ������� 100-220  120:0~199
		public int getRandom(int min, int max) {
			Random myrand = new Random(); 
			int gap = max - min;
			int temp = myrand.nextInt(gap);
			int random = temp + min;
			return random;
		}
		
		//���ɵ�ǰ�����ڻ�ʱ���ַ���
		public String getDateTine(String format) {
			SimpleDateFormat formatter = new SimpleDateFormat();
			Date date = new Date();
			String now = formatter.format(date.getTime());
			return now;
		}
		//��ȡ�ؼ�������Ϣ
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
		
		// ����������ʽ�����ұ߽�����ȡ�����ме�ֵ
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
		
		//����������ʽ�����ұ߽�����ȡ���б����м��ŵ�ֵ������list���ͱ���
		public List<String> getItems(String response, String left, String right){
			List<String> data = new ArrayList<String>();
			//(.+?)��ʾʹ�÷�̰��ģʽ���ұ����ұ߽��������С��������
			String regex = "("+ left +")(.+/?)("+ right +")";
			//����ƥ��ģʽΪPattern.DOTALL.��ʱ������ƥ�任�з�\n
			//Pattern p = Pattern.compile(regex, Pattern.DOTALL);
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(response);
			while (m.find()) {
				data.add(m.group(2));
			}
			return data;
		}
		
		//����һ����Ӧ��JSon�ַ�������Ϊһ��List<String, Map>���󷵻�
		public List<Map<String, String>> getJsonList(String source){
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {
				JSONArray ja = new JSONArray(source);
				//�˴�ѭ����һ�ε�Ŀ����ȥ��JSON�������һ��
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
		
		//��ȡExcel�����ص���ά������
		public String[][] readExcel(String fileNnmae, String sheetName){
			File file = new File(fileNnmae);
			String[][] data = null;
			try {
				FileInputStream fis = new FileInputStream(file);
				jxl.Workbook rwb = Workbook.getWorkbook(fis);
				
				Sheet sheet = rwb.getSheet(sheetName);
				//��ȡ��sheet�е��к��������������ڶ����ά����
				int rowCount = sheet.getRows();
				int colCount = sheet.getColumns();
				//Ĭ������£���һ�ж���������������������Ӧ�û���һ��
				data = new String[rowCount-1][colCount];
				
				//��������е��к��У�����ֵ��data����
				//ѭ����i=1��ʼ����Ϊ���ÿ�����һ��
				for(int i=1; i<rowCount; i++) {
					Cell[] cells = sheet.getRow(i);
					//������ǰ���е�ÿһ�У�����ֵ��data����
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
		
		//����WinRarѹ������ѹ�����Ա������ڵ�Ŀ¼
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
		
		//����javamail���ʵ���ʼ�����
		public void sendRrportByEmail() {
			try {
				//��һ���������ʼ��Ự��Ϣ�����������Э�����͵�
				Properties props = new java.util.Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", "smtp.exmail.qq..com");
				props.put("mail.transport.protocal", "smtp");
				
				Session mailSession = Session.getDefaultInstance(props);
				mailSession.setDebug(false);
				
				//�ڶ����������������ռ�����Ϣ
				Address sender = new InternetAddress("your sendEmail account");
				Address receiver1 = new InternetAddress("myworkmail@qq.com");
				Address receiver2 = new InternetAddress("otherworkmail@qq.com");
				Address[] receivers = {receiver1,receiver2};
				
				MimeMessage mimiMessage = new MimeMessage(mailSession);
				mimiMessage.setFrom(sender);
				mimiMessage.addRecipients(Message.RecipientType.TO, receivers);
				
				//�������������ʼ��ı���������
				mimiMessage.setSubject("Agileone�Զ������Ա���");
				
				String mailbody =
					"<font face ='΢���ź�'>�����Զ���Agileone" + Reporter.version+".<br>";
				mailbody += "����ʱ�䣺" + this.getDateTine("yyyy-MM-dd HH:mm:ss");
				mailbody += ".<br>����ʱ�䣺" + Reporter.duration/1000 + " ��.<br>";
				
				mailbody += "========================================<br>";
				mailbody += "��Ծ���Ĳ��Ľ�����������飬������ʼ�����.<br>";
				mailbody += "���ʼ���Java�Զ������Գ��򷢳�������ظ�!</font>";
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(mailbody, "text/html;charset=UTF-8");
			
			//���Ĳ�����HTML���Ա����ѹ���ļ���Ϊ�ʼ���������
			MimeBodyPart fileBodyPart = new MimeBodyPart();
			String userDir = System.getProperty("user.dir");
			String rarFile = userDir + "\\report\\" + Reporter.folder +".rar";
			FileDataSource fileDataSource = new FileDataSource(rarFile);
			fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
			fileBodyPart.setFileName(fileDataSource.getName());
			
			//���岽�����ʼ����ĺ��ʼ�������ӽ��ʼ���
			Multipart container = new MimeMultipart();
			container.addBodyPart(textBodyPart);
			container.addBodyPart(fileBodyPart);
			
			mimiMessage.setContent(container);
			mimiMessage.saveChanges();
			
			//�������������ʼ��˺���Ϣ����ʽ�����ʼ�
			Transport transport = mailSession.getTransport("smtp");
			transport.connect("smtp.exmail.qq.com","��ĵ�¼�����˺�","��������");
			transport.sendMessage(mimiMessage, mimiMessage.getAllRecipients());
			transport.close();
			
			System.err.println("�Ѿ���HTML���Ա���ɹ����ͳ�.");
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
}
