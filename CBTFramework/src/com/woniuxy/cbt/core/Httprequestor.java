package com.woniuxy.cbt.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Httprequestor {
	//�����༶��������Ա������cookie�����е���Ӧ�е�cookieֵȫ�����ӵ��ı����ϡ�
	private String cookieValue = "";
	
	public void clearCookie() {
		this.cookieValue ="";
	}
	//ʹ��javaʵ��http��get���󣬲�������Ӧ������
	//URLConnection
	public String sendGet(String getURl, String param) {
		//���յ������
		String result ="", line="";
		
		try {
			if (param != null && !param.equals(""))
				getURl = getURl + "?" + param;
			//Ҫ���ʵ�URL��ַ
			URL url = new URL(getURl);
			//���ӷ��ʵ�ַ
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();    //��httpUrl����
			
			//�Ը�HTTp���ӽ������ã��趨ʹ�÷���   ���������ò�����������
			
			urlConnection.setUseCaches(false);  //�Ƿ����ʹ�û���
			urlConnection.setRequestProperty("Cache-Control", "no-cache");  //����������ֶ�ֵ
			urlConnection.setInstanceFollowRedirects(true);   //�Ƿ�ʹ���ض���
			urlConnection.setRequestProperty("Cookie", this.cookieValue);
			//urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			//��������
			urlConnection.connect();
			
			this.readCookie(urlConnection);  //����Set-cookie

			//****************************************
			//InputStream is = urlConnection.getInputStream();  //�ֽ�����ȡ���û��޷�֪����Ӧ���ı�����
			//InputStreamReader isr =new InputStreamReader(urlConnection.getInputStream(),"UTF-8"); //�ַ�����ȡ
			// ȡ������������ʹ��br��ȡ
			BufferedReader br = new BufferedReader(new InputStreamReader(
					            urlConnection.getInputStream(), "UTF-8"));  //���ж�ȡ
			while ((line = br.readLine()) != null){
				result +=  line  +"\n";
				//result +=  line;
				
			}
			br.close();
			urlConnection.disconnect();
		
		} catch (Exception e) {
			e.printStackTrace();
    	}	
		return result;
	
		}
	
	
	//ʹ��javaʵ��HTTP��post���󣬲�������Ӧ�����ݡ�
	public String sendPost(String postUrl, String postParam) {
		String body ="", line ="";
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(postUrl);     // �򿪺�URL֮�������
			urlConnection = (HttpURLConnection)url.openConnection();
			
			// ����ͨ�õ���������
			urlConnection.setDoOutput(true);             //�����Ƿ���httpUrlConnection���
			urlConnection.setDoInput(true);              //�����Ƿ��httpUrlConnection���룬Ĭ���������true
			urlConnection.setRequestMethod("POST");      //�趨����ķ���Ϊ"post"
			urlConnection.setUseCaches(false);            //�Ƿ����ʹ�û��棬Post ������ʹ�û���
			urlConnection.setInstanceFollowRedirects(true);   //�Ƿ�ʹ���ض���
			urlConnection.setRequestProperty("Cookie", this.cookieValue);
			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			
			//����� ���������뵽�����ַ   ע���������ʱ��һ������д����д��
			//����һ��PrintWriter�������ڽ�Post�������ķ��͸������
			PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
			out.print(postParam);  //�Է��������
			out.flush();
			
			
			this.readCookie(urlConnection);  //����Set-cookie

			//��ȡ�������˵���Ӧͷ������������Ӧ��set-cookie�ֶΡ�
			//�п���ĳЩ��Ӧͷ���沢û��set-cookie�ֶΡ�
			//map��һ����ֵ�ԣ���һ��������ʾkey,�ڶ���������ʾvalue��
//			Map<String, List<String>> fields = urlConnection.getHeaderFields();
//			List<String> cookies = fields.get("Set-Cookie");
//			if(cookies !=null) {
//				for(int i=0; i<cookies.size();i++) {
//					String value = cookies.get(i).substring(0, cookies.get(i).indexOf(";"));
//					this.cookie += value +";";	
//				}
//				System.out.println(this.cookie);
//			}
			
			//��ȡ�����������  ��װ
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"));  //���ж�ȡ
			while ((line = in.readLine()) != null){
				body +=  line  +"\n";
				//body +=  line;
			}	
		//System.out.println(body);
	    out.close();
		in.close();
		urlConnection.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return body;
		
	}
	
    /*  ȥ��cookie����bug���˷�������
	//��ȡÿ����Ӧ�е�set-cookies�ֶ�ֵ����ƴ�ӵ�cookie��
		private void readCookie(HttpURLConnection urlConnection) {
			Map<String, List<String>> fields = urlConnection.getHeaderFields();
			List<String> cookies = fields.get("Set-Cookie");
			if(cookies !=null &&cookies.size()>0) { //�˴������жϣ���Ϊ������ÿһ����Ӧ����Set-Cookie
				for(int i=0; i<cookies.size();i++) {
					//�����ַ�����������ȡ�ֶ��е�һ���ֺ�ǰ������
					String value = cookies.get(i).split(";")[0];
					//ȥ���ظ����ݺ󽫻�ȡ��������ƴ�ӵ�cookie�������´�����
					this.cookie += value +";";	
				}
				//System.out.println(this.cookie);
			}
	
		}
		*/
	
	
	public void readCookie(HttpURLConnection urlConnection) {
		Map<String, List<String>> fields = urlConnection.getHeaderFields();
		List<String> cookies = fields.get("Set-Cookie");
		if(cookies != null && cookies.size() >0) {
			for(int i=0; i<cookies.size(); i++) {
				String cookie = cookies.get(i).split(";")[0];
				//��ȡһ��Cookieֵ��key��value
				String key = cookie.split("=")[0];
				String value = cookie.split("=")[1];
				//���жϵ�ǰ���е�cookieValue�Ƿ��������cookie����������������������ж�cookieValueֵ���Ƿ��Ѿ�����key
				//����Ѿ�����keyֵ������Ҫ����ֵ���»�ȡ����value�����滻
				//�����ֵ������key1=value1;key2=value2;key3=value3;
				if(!this.cookieValue.contains(cookie)) {
					if(this.cookieValue.contains(key)) {
						//��ͨ����Key��ȡ�� "key="��ǰ���ֵ����ֵ����ʱ����
						int posEqual = cookieValue.indexOf(key) + key.length() +1 ;
						String tempStart = cookieValue.substring(0,posEqual);
						//�ٽ�cookieValue��ֵǰ�沿��ȥ��
						this.cookieValue = cookieValue.substring(posEqual);
						//��ʣ�ಿ�ֵĵ�һ���ֺ�λ�û�ȡ������0�����λ�þ��Ǿɵ�value
						int posSemicolon = cookieValue.indexOf(";");
						
						String tempEnd ="";
						//������ڵ�1���ֺ��Ҳ������һ������ѵ�һ���ֺ��Ժ��ֵ��ʱ��������
						//��������ڷֺŻ��߷ֺ��Ѿ������һ��ֵ����ֱ����������ɵ�value
						if(posSemicolon >=0 && posSemicolon != cookie.lastIndexOf(";")) {
							tempEnd = this.cookieValue.substring(posSemicolon);
						}
						//��������ַ���������һ�𼴿�
						this.cookieValue = tempStart + value + tempEnd;
					}
					//�����������key��˵����һ���µ�cookieֵ�����ӵ�����
					else {
						this.cookieValue += cookie + ";";
					}
				}
			}
		}
		
	}
	
		public int  getResponseCode(String getURl) {
			//���յ������
			int responseCode = -1;
			//String result ="",line= "";
			
			try {
				//Ҫ���ʵ�URL��ַ
				URL url = new URL(getURl);
				//���ӷ��ʵ�ַ
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();    //��httpUrl����
				
				//�Ը�HTTp���ӽ������ã��趨ʹ�÷���   ���������ò�����������
				
				urlConnection.setUseCaches(false);  //�Ƿ����ʹ�û���
				urlConnection.setRequestProperty("Cache-Control", "no-cache");  //����������ֶ�ֵ
				urlConnection.setInstanceFollowRedirects(false);   //�Ƿ�ʹ���ض���
				urlConnection.setRequestProperty("Cookie", this.cookieValue);
				
				//��������
				urlConnection.connect();
				responseCode = urlConnection.getResponseCode();
				
				
				urlConnection.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
	    	}	
			return responseCode;
		
			}	
	//ʹ��javaʵ��HTTP���󣬲�����Ӧ�����ݱ��浽�ļ���
	public void getImage(String imgUrl) {
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			String filePath ="E:\\"+ imgUrl.substring(imgUrl.lastIndexOf("/")+1);
			File outFile = new File(filePath);
			OutputStream os = new FileOutputStream(outFile);
			byte[] buff = new byte[1024];
			
			while(true) {
				int readed = is.read(buff);
				if(readed == -1) {
					break;
				}
				os.write(buff);
			}
			is.close();
			os.close();
		    urlConnection.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

	// ����������ʽ�����ұ߽�����ȡ�����ме�ֵ
	public String getItem(String response, String left, String right) {
		String verifyCode = "";
		String regex = "("+ left +")(.+/?)("+ right +")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(response);
		if (m.find()) {
			verifyCode = m.group(2);
		}
		return verifyCode;
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
}
