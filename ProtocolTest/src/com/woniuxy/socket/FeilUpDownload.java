package com.woniuxy.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FeilUpDownload {

	public static void main(String[] args) {
		 FeilUpDownload updown = new FeilUpDownload();

	}
	
	public void testDownload() {
		String getUrl = "";
		String folder = "E:\\TestUSD";
	}
	public void testUpload() {
		String postUrl ="";
		String filePath = "E:\\";
		String name = "";
	}
	//文件下载代码，除了给定文件的URl地址外，在给定一个文件保存目录
	public void doDownload(String getUrl, String folder) {
		try {
			URL url = new URL(getUrl);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			
			//本次连接相关的参数设置
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);
			urlConnection.setUseCaches(true);
			urlConnection.setRequestMethod("GET");
			
			//建立与服务器的连接
			urlConnection.connect();
			
			//通过URL地址中的最后一个“/”作为分隔符，分隔出原始文件名，也可以自定义文件名
			int posLast = getUrl.lastIndexOf("/")+1;
			String fileName = getUrl.substring(posLast);
			
			//定义一个文件输出流，用于将下载的文件保存到硬盘
			File outfile = new File(folder + "\\"+ fileName);
			OutputStream os = new FileOutputStream(outfile);
			
			//定义一个输入流，用于从服务器端获取到该文件字节流
			InputStream  is = urlConnection.getInputStream();
			
			//新建一个字节数组，用于缓存从服务器端读取的内容
			byte[] buf = new byte[1024];
			int buflen = 0;  //定义每一次循环读取到的字节数组的长度
			
			while((buflen = is.read(buf)) != -1) {
				byte[] temp = new byte[buflen];
				//将buf中的内容复制到temp中
				//Object src : 原数组， int srcPos : 从元数据的起始位置开始，Object dest : 目标数组，
                //int destPos : 目标数组的开始起始位置，int length  : 要copy的数组的长度
				System.arraycopy(buf, 0, temp, 0, buflen);  				
				//将字节数组写入到outfile中
				os.write(temp);
			}
			
			//释放资源
			urlConnection.disconnect();
			os.close();
			is.close();
			System.out.println("文件下载完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//实现文件上传，提供Post请求的服务URL地址，含路径的文件名及上传控件name属性
	public void doUpload(String postUrl,String filePath,String name) {
		HttpURLConnection urlConnection =null;
		//定义boundary的值，可以随意设置，不一定非得按照浏览器的随机规则
		//String boundary = "-----------------------------7e116a2c6111e\r\n";
		String boundary ="###StartUploadFile###";
		try {
			URL	url = new URL(postUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
			
			//本次连接相关的参数设置
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			
			//利用boundary的值构建Post的请求头部字段内容
			urlConnection.setRequestProperty("Content-Type", 
					"multipart/form-data; boudary=" + boundary);
			
			//定义数据的输出流，用于向服务器写出字节流
			OutputStream os = urlConnection.getOutputStream();
			//此处使用DataOutputStream对输出流进行封装，这样可以直接按字符串写出
			DataOutputStream dos = new DataOutputStream(os);
			
			//写出请求正文的第一行内容，即第二部分的第一行内容，注意，必须使用\r\n换行
			dos.writeBytes("--"+ boundary + "\r\n");
			
			//其中name="fileToUpload"中的fileToUpload对应的是文件上传控件的name属性（不同系统控件属性名称不一样）
		    dos.writeBytes("Content-Dispositon: from-data; name=\""+ name +"\";"
		    		+ "filename=\""+ filePath +"\"");
		    
		    //远洋输出文件的http类型，其中image/jpg和image/png可通用
		    //如为其他类型，则请按照监控到的格式重写此处，注意此处是两个换行符
		    dos.writeBytes("Content-Type: image/jpg\r\n\r\n");
		    
		    //定义文件的输入流，以便于打开要上传的文件
		    FileInputStream fis = new FileInputStream(filePath);
		    DataInputStream dis = new DataInputStream(fis);
		    
		    //定义字节数组，用于将从文件中读取到的字节码输出到服务器管到
		    byte[] buf = new byte[1024];
		    int buflen = 0;
		    while((buflen =dis.read(buf)) != -1) {
		    	dos.write(buf, 0, buflen);
		    }
		    
		    //输出文件的第四部分，定义文件内容完成的结束标志
		   dos.writeBytes("\r\n--" + boundary + "--\r\n");
		   dos.flush();
		   
		   //正常读取服务器端的响应
		   InputStream is = urlConnection.getInputStream();
		   InputStreamReader isr = new InputStreamReader(is);
		   BufferedReader br = new BufferedReader(isr);
		   String body = "", line ="";
		   while((line = br.readLine()) != null) {
			   body += line +"\n";
		   }
		   System.out.println("文件上传成，响应为：\n" +body);
		   
		   //释放资源
		   is.close();
		   isr.close();
		   br.close();
		   dis.close();
		   fis.close();
		   os.close();
		   dos.close();
		   urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
