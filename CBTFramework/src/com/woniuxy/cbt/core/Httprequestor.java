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
	//定义类级变量（成员变量）cookie，所有的响应中的cookie值全部附加到改变量上。
	private String cookieValue = "";
	
	public void clearCookie() {
		this.cookieValue ="";
	}
	//使用java实现http的get请求，并返回响应的内容
	//URLConnection
	public String sendGet(String getURl, String param) {
		//接收的输出流
		String result ="", line="";
		
		try {
			if (param != null && !param.equals(""))
				getURl = getURl + "?" + param;
			//要访问的URL地址
			URL url = new URL(getURl);
			//连接访问地址
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();    //用httpUrl接收
			
			//对该HTTp连接进行配置，设定使用方法   必须先设置参数在做其他
			
			urlConnection.setUseCaches(false);  //是否可以使用缓存
			urlConnection.setRequestProperty("Cache-Control", "no-cache");  //设置请求的字段值
			urlConnection.setInstanceFollowRedirects(true);   //是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookieValue);
			//urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			//建立连接
			urlConnection.connect();
			
			this.readCookie(urlConnection);  //处理Set-cookie

			//****************************************
			//InputStream is = urlConnection.getInputStream();  //字节流读取，用户无法知道对应的文本内容
			//InputStreamReader isr =new InputStreamReader(urlConnection.getInputStream(),"UTF-8"); //字符流读取
			// 取得输入流，并使用br读取
			BufferedReader br = new BufferedReader(new InputStreamReader(
					            urlConnection.getInputStream(), "UTF-8"));  //按行读取
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
	
	
	//使用java实现HTTP的post请求，并返回响应的内容。
	public String sendPost(String postUrl, String postParam) {
		String body ="", line ="";
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(postUrl);     // 打开和URL之间的连接
			urlConnection = (HttpURLConnection)url.openConnection();
			
			// 设置通用的请求属性
			urlConnection.setDoOutput(true);             //设置是否向httpUrlConnection输出
			urlConnection.setDoInput(true);              //设置是否从httpUrlConnection读入，默认情况下是true
			urlConnection.setRequestMethod("POST");      //设定请求的方法为"post"
			urlConnection.setUseCaches(false);            //是否可以使用缓存，Post 请求不能使用缓存
			urlConnection.setInstanceFollowRedirects(true);   //是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookieValue);
			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			
			//输出流 将条件输入到请求地址   注意在请求的时候一定是先写入在写出
			//定义一个PrintWriter对象，用于将Post请求正文发送给服务端
			PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
			out.print(postParam);  //对服务器输出
			out.flush();
			
			
			this.readCookie(urlConnection);  //处理Set-cookie

			//读取服务器端的响应头，并解析出对应的set-cookie字段。
			//有可能某些响应头里面并没有set-cookie字段。
			//map是一个键值对，第一个参数表示key,第二个参数表示value。
//			Map<String, List<String>> fields = urlConnection.getHeaderFields();
//			List<String> cookies = fields.get("Set-Cookie");
//			if(cookies !=null) {
//				for(int i=0; i<cookies.size();i++) {
//					String value = cookies.get(i).substring(0, cookies.get(i).indexOf(";"));
//					this.cookie += value +";";	
//				}
//				System.out.println(this.cookie);
//			}
			
			//获取到输入输出流  包装
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"));  //按行读取
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
	
    /*  去重cookie存在bug，此方法废弃
	//读取每条响应中的set-cookies字段值，并拼接到cookie中
		private void readCookie(HttpURLConnection urlConnection) {
			Map<String, List<String>> fields = urlConnection.getHeaderFields();
			List<String> cookies = fields.get("Set-Cookie");
			if(cookies !=null &&cookies.size()>0) { //此处必须判断，因为并不是每一个响应都有Set-Cookie
				for(int i=0; i<cookies.size();i++) {
					//利用字符串处理方法获取字段中第一个分号前的内容
					String value = cookies.get(i).split(";")[0];
					//去除重复内容后将获取到的内容拼接到cookie后，用于下次请求
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
				//获取一个Cookie值得key和value
				String key = cookie.split("=")[0];
				String value = cookie.split("=")[1];
				//先判断当前已有的cookieValue是否包含整条cookie，包含则跳过，否则继续判断cookieValue值里是否已经包含key
				//如果已经包含key值，则需要将其值用新获取到的value进行替换
				//以这个值举例：key1=value1;key2=value2;key3=value3;
				if(!this.cookieValue.contains(cookie)) {
					if(this.cookieValue.contains(key)) {
						//先通过该Key获取到 "key="及前面的值，赋值给临时变量
						int posEqual = cookieValue.indexOf(key) + key.length() +1 ;
						String tempStart = cookieValue.substring(0,posEqual);
						//再将cookieValue的值前面部分去掉
						this.cookieValue = cookieValue.substring(posEqual);
						//将剩余部分的第一个分号位置获取到，从0到这个位置就是旧的value
						int posSemicolon = cookieValue.indexOf(";");
						
						String tempEnd ="";
						//如果存在第1个分号且不是最后一个，则把第一个分号以后的值临时保存起来
						//如果不存在分号或者分号已经是最后一个值，则直接弃用这个旧的value
						if(posSemicolon >=0 && posSemicolon != cookie.lastIndexOf(";")) {
							tempEnd = this.cookieValue.substring(posSemicolon);
						}
						//最后将三个字符串连接在一起即可
						this.cookieValue = tempStart + value + tempEnd;
					}
					//如果不包含该key，说明是一个新的cookie值，附加到后面
					else {
						this.cookieValue += cookie + ";";
					}
				}
			}
		}
		
	}
	
		public int  getResponseCode(String getURl) {
			//接收的输出流
			int responseCode = -1;
			//String result ="",line= "";
			
			try {
				//要访问的URL地址
				URL url = new URL(getURl);
				//连接访问地址
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();    //用httpUrl接收
				
				//对该HTTp连接进行配置，设定使用方法   必须先设置参数在做其他
				
				urlConnection.setUseCaches(false);  //是否可以使用缓存
				urlConnection.setRequestProperty("Cache-Control", "no-cache");  //设置请求的字段值
				urlConnection.setInstanceFollowRedirects(false);   //是否使用重定向
				urlConnection.setRequestProperty("Cookie", this.cookieValue);
				
				//建立连接
				urlConnection.connect();
				responseCode = urlConnection.getResponseCode();
				
				
				urlConnection.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
	    	}	
			return responseCode;
		
			}	
	//使用java实现HTTP请求，并将响应的内容保存到文件中
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

	// 根据正则表达式的左右边界来获取被其中夹的值
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
}
