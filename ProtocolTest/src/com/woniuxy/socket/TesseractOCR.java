package com.woniuxy.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TesseractOCR {

	public static void main(String[] args) {
		TesseractOCR  ocr = new TesseractOCR();
		
//		String imageSrc = "http://www.woniuxy.com/PictureCheckCodeServlet";
//		String outputPath = "E:/Other/VerifyImage/myVerifyCode.jpg";
//		ocr.downLoad(imageSrc,outputPath);
		
		String result = ocr.recognizeText("E:/Other/VerifyImage/woniu.png", true);
        System.out.println(result);
	}
	public String recognizeText(String imageFile, boolean isChinese) {
		String result = "";  //保存读取到的识别内容并返回
		String tesseractExe = "D:/Tools/Tesseract-OCR/tesseract.exe";
		String output ="E:/Other/VerifyResult/output/text";
		//根据选项组装执行命令的字符串，注意参数之间需要加空格分隔开
		String command = tesseractExe + " " + imageFile + " " + output;
		if(isChinese) {
			command += " -l chi_sim";
		}
		else {
			command += " -l eng";
		}
		System.out.println(command); //输出命令进行确认
		
		//使用Process来获取执行命令的结果，并对其结果进行判断
		try {
			Process process = Runtime.getRuntime().exec(command);
			//操作系统错误代码0：成功   操作系统错误代码2：没有这样的文件或目录   操作系统错误代码1：操作不允许
			int exeCode = process.waitFor();
			//执行的结果代码如果为，表示命令执行成功
			if(exeCode ==0) {
				//读取到输出文件中的内容，并将其赋值改变量result
				InputStream fis = new FileInputStream(output + ".txt");
				InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				result = br.readLine();
				br.close();
			}
			else {
				System.out.println("本次识别操作命令未正常执行.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;

	}
	
	//文件下载代码，除了给定文件的URl地址外，在给定一个文件保存目录
		public void doDownload(String src, String path) {
			int dlstatus;
			try {
				//发送HTTP的GET请求获取到验证码图片
				URL url = new URL(src);
				File outfile = new File(path);
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
				
				//本次连接相关的参数设置
				urlConnection.setConnectTimeout(30000);
				urlConnection.setReadTimeout(30000);
				urlConnection.setUseCaches(true);
				urlConnection.setRequestMethod("GET");
				
				//建立与服务器的连接
				urlConnection.connect();
				dlstatus = urlConnection.getResponseCode();
				//判断访问状态
				if(dlstatus < 400) {
				
				    //通过字节流的读取的方式将得到的图片字节码写入到输出文件中
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
				}
				else{
					System.out.println("下载验证码失败，请确认.");
				   
				}
				
			} catch (Exception e) {
					e.printStackTrace();
				}
				
		}

}
