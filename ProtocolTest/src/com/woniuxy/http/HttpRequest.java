package com.woniuxy.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
	// 定义类级变量（成员变量）cookie，所有的响应中的cookie值全部附加到改变量上。
	public String cookie = "";
	//成员变量headers，用于存放需要加载的头域参数。
	private Map<String, String> headers = new HashMap<String, String>();
	//是否添加header，默认不添加
	private boolean addHeaderFlag = false;

	public void clearCookie() { 
		this.cookie = "";
	}

	// 使用java实现http的get请求，并返回响应的内容
	// URLConnection
	public String sendGet(String getURl, String param) {
		// 接收的输出流
		String result = "", line = "";

		try {
			if (param != null && !param.equals(""))
				getURl = getURl + "?" + param;
			// 要访问的URL地址
			URL url = new URL(getURl);
			// 连接访问地址
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 用httpUrl接收

			// 对该HTTp连接进行配置，设定使用方法 必须先设置参数在做其他
			urlConnection.setUseCaches(false); // 是否可以使用缓存
			urlConnection.setRequestProperty("Cache-Control", "no-cache"); // 设置请求的字段值
			urlConnection.setInstanceFollowRedirects(true); // 是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookie);
			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			urlConnection.setRequestProperty("Content-type","application/json");
//			urlConnection.setRequestProperty("User-Agent",
//					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
			
			//通过是否添加头域的标识符判断是否执行头域参数添加操作
			if (addHeaderFlag = true) {
				//从头域map中遍历添加头域
				Set<String> headerKeys = headers.keySet();
				for (String key : headerKeys) {
					urlConnection.setRequestProperty(key, headers.get(key));
				}
			}

			// 建立连接
			urlConnection.connect();
			this.readCookie(urlConnection); // 处理Set-cookie

			// ****************************************
			// InputStream is = urlConnection.getInputStream(); //字节流读取，用户无法知道对应的文本内容
			// InputStreamReader isr =new
			// InputStreamReader(urlConnection.getInputStream(),"UTF-8"); //字符流读取
			// 取得输入流，并使用br读取
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8")); // 按行读取
			while ((line = br.readLine()) != null) {
				result += line + "\n";
				// result += line;
			}
			br.close();
			urlConnection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 使用java实现HTTP的post请求，并返回响应的内容。
	public String sendPost(String postUrl, String postParam) {
		String body = "", line = "";
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(postUrl); // 打开和URL之间的连接
			urlConnection = (HttpURLConnection) url.openConnection();

			// 设置通用的请求属性
			urlConnection.setDoOutput(true); // 设置是否向httpUrlConnection输出
			urlConnection.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true     
			urlConnection.setRequestMethod("POST"); // 设定请求的方法为"post"
//			urlConnection.setUseCaches(false); // 是否可以使用缓存，Post 请求不能使用缓存
			urlConnection.setInstanceFollowRedirects(true); // 是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookie);
//			urlConnection.setRequestProperty("User-Agent", "Java-Client");
			urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Content-type","application/json");
			

			//通过是否添加头域的标识符判断是否执行头域参数添加操作
			if (addHeaderFlag = true) {
				//从头域map中遍历添加头域
				Set<String> headerKeys = headers.keySet();
				for (String key : headerKeys) {
					urlConnection.setRequestProperty(key, headers.get(key));
				}
			}

			// 输出流 将条件输入到请求地址 注意在请求的时候一定是先写入在写出
			// 定义一个PrintWriter对象，用于将Post请求正文发送给服务端
			PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
			out.print(postParam); // 对服务器输出
			out.flush();

			this.readCookie(urlConnection); // 处理Set-cookie

			// 读取服务器端的响应头，并解析出对应的set-cookie字段。
			// 有可能某些响应头里面并没有set-cookie字段。
			// map是一个键值对，第一个参数表示key,第二个参数表示value。
//			Map<String, List<String>> fields = urlConnection.getHeaderFields();
//			List<String> cookies = fields.get("Set-Cookie");
//			if(cookies !=null) {
//				for(int i=0; i<cookies.size();i++) {
//					String value = cookies.get(i).substring(0, cookies.get(i).indexOf(";"));
//					this.cookie += value +";";	
//				}
//				System.out.println(this.cookie);
//			}

			// 获取到输入输出流 包装
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8")); // 按行读取
			while ((line = in.readLine()) != null) {
				body += line + "\n";
				// body += line;
			}
			// System.out.println(body);
			out.close();     
			in.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	

	
	//添加头部信息
//	public void addheader(HttpURLConnection urlConnection) {
//		urlConnection.setRequestProperty(key, value);
//		
//	}

	// 读取每条响应中的set-cookies字段值，并拼接到cookie中
	private void readCookie(HttpURLConnection urlConnection) {
		Map<String, List<String>> fields = urlConnection.getHeaderFields();
		List<String> cookies = fields.get("Set-Cookie");
		if (cookies != null && cookies.size() > 0) { // 此处必须判断，因为并不是每一个响应都有Set-Cookie
			for (int i = 0; i < cookies.size(); i++) {
				// 利用字符串处理方法获取字段中第一个分号前的内容
				String value = cookies.get(i).split(";")[0];
				// 去除重复内容后将获取到的内容拼接到cookie后，用于下次请求
				this.cookie += value + ";";
			}
		 //System.out.println(this.cookie);
		}

	}
	
    
	/**
	 * 设置添加头域标志位为true，并且通过传递头域map，实例化成员变量headers
	 * @param headerMap传递的头域参数map
	 */
	public void addHeader(Map<String, String> headerMap) {
		headers = headerMap;
		addHeaderFlag = true;
	}
	
	public int getResponseCode(String getURl) {
		// 接收的输出流
		int responseCode = -1;
		String result = "", line = "";
		try {
			// 要访问的URL地址
			URL url = new URL(getURl);
			// 连接访问地址
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 用httpUrl接收

			// 对该HTTp连接进行配置，设定使用方法 必须先设置参数在做其他

			urlConnection.setUseCaches(false); // 是否可以使用缓存
			urlConnection.setRequestProperty("Cache-Control", "no-cache"); // 设置请求的字段值
			urlConnection.setInstanceFollowRedirects(true); // 是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookie);

			// 建立连接
			urlConnection.connect();
			responseCode = urlConnection.getResponseCode();

			urlConnection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseCode;

	}

	// 使用java实现HTTP请求，并将响应的内容保存到文件中
	public void getImage(String imgUrl) {
		try {
			URL url = new URL(imgUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			String filePath = "E:\\" + imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
			File outFile = new File(filePath);
			OutputStream os = new FileOutputStream(outFile);
			byte[] buff = new byte[1024];

			while (true) {
				int readed = is.read(buff);
				if (readed == -1) {
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

	// 获取发帖页面的动态验证码
	public String getItem(String response, String regex) {
		String verifyCode = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(response);
		if (m.find()) {
			verifyCode = m.group(2);
		}
		return verifyCode;
	}

}
