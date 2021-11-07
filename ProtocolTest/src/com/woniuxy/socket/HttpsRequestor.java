package com.woniuxy.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpsRequestor {
	private String  cookie ="";
	
	public String sendGet(String getURl) {
		//接收的输出流
		String result ="", line="";
		HttpsURLConnection urlConnection = null;
		try {
		
			//创建SSLContext对象，并使用我们制定的信任的管理器初始化
			TrustManager[] tm = { new MyX509TrustManager()};
			SSLContext  sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			//从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
		
			//要访问的URL地址
			URL url = new URL(getURl);
			//连接访问地址
		    urlConnection = (HttpsURLConnection)url.openConnection();    //用httpUrl接收
			
			//本次连接相关的参数设置
			
		    urlConnection.setSSLSocketFactory(ssf);
		    urlConnection.setConnectTimeout(100000);
		    urlConnection.setReadTimeout(10000);
			urlConnection.setUseCaches(false);  
			urlConnection.setRequestMethod("GET");  
			urlConnection.setRequestProperty("Cookie", this.cookie);
			
			//设置urlConnection头部信息
			urlConnection.setRequestProperty("Cache-Control", "no-cache");  //设置请求的字段值
			urlConnection.setInstanceFollowRedirects(false);   //是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookie);
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
			
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
		HttpsURLConnection urlConnection = null;
		try {
			//创建SSLContext对象，并使用我们制定的信任的管理器初始化
			TrustManager[] tm = { new MyX509TrustManager()};
			SSLContext  sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			//从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(postUrl);     // 打开和URL之间的连接
			urlConnection = (HttpsURLConnection)url.openConnection();
			
			// 设置通用的请求属性
			urlConnection.setSSLSocketFactory(ssf);
			urlConnection.setDoOutput(true);             //设置是否向httpUrlConnection输出
			urlConnection.setDoInput(true);  
			urlConnection.setConnectTimeout(100000);
		    urlConnection.setReadTimeout(10000);
			urlConnection.setUseCaches(false);  //设置是否从httpUrlConnection读入，默认情况下是true
			urlConnection.setRequestMethod("POST");      //设定请求的方法为"post"
			urlConnection.setUseCaches(false);            //是否可以使用缓存，Post 请求不能使用缓存
			urlConnection.setInstanceFollowRedirects(true);   //是否使用重定向
			urlConnection.setRequestProperty("Cookie", this.cookie);
			
			//输出流 将条件输入到请求地址   注意在请求的时候一定是先写入在写出
			//定义一个PrintWriter对象，用于将Post请求正文发送给服务端
			PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
			out.print(postParam);  //对服务器输出
			out.flush();
			
			
			this.readCookie(urlConnection);  //处理Set-cookie


			
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
	
	//读取每条响应中的set-cookies字段值，并拼接到cookie中
		private void readCookie(HttpURLConnection urlConnection) {
			Map<String, List<String>> fields = urlConnection.getHeaderFields();
			List<String> cookies = fields.get("Set-Cookie");
			if(cookies !=null &&cookies.size()>0) { //此处必须判断，因为并不是每一个响应都有Set-Cookie
				for(int i=0; i<cookies.size();i++) {
					//利用字符串处理方法获取字段中第一个分号前的内容
					String value = cookies.get(i).split(";")[0];
					//去除重复内容后将获取到的内容拼接到cookie后，用于下请求
					this.cookie += value +";";	
				}
				//System.out.println(this.cookie);
			}
	
		}
	
		public int  getResponseCode(String getURl) {
			//接收的输出流
			int responseCode = -1;
			String result ="",line= "";
			
			try {
				//要访问的URL地址
				URL url = new URL(getURl);
				//连接访问地址
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();    //用httpUrl接收
				
				//对该HTTp连接进行配置，设定使用方法   必须先设置参数在做其他
				
				urlConnection.setUseCaches(false);  //是否可以使用缓存
				urlConnection.setRequestProperty("Cache-Control", "no-cache");  //设置请求的字段值
				urlConnection.setInstanceFollowRedirects(false);   //是否使用重定向
				urlConnection.setRequestProperty("Cookie", this.cookie);
				
				//建立连接
				urlConnection.connect();
				responseCode = urlConnection.getResponseCode();
				
				
				urlConnection.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
	    	}	
			return responseCode;
		
			}	

}
