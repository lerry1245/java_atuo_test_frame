package tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
 
/*
 * Tess4J测试类
 */
public class Tess4jTest {
 
    public static void main(String[] args){
    	Tess4jTest t4j = new Tess4jTest();
		String imageSrc = "http://csdqtjcag.lx901.com/Home/ValidateCode?";
		String outputPath = "E:/Other/VerifyImage/ValidateCode.gif";
		t4j.doDownload(imageSrc,outputPath);
		
    	String path = "E:\\WorkSpace\\ProtocolTest";
        //String outputPath = "E:/Other/VerifyImage/woniu.png";
    	
        String language = "chi_sim";
        String  result= t4j.recognizeImage(path, outputPath, language);
        System.out.println(result);
    }
    /*
     *@param   path 我的项目存放路径
     *@param   outputPath   识别图片的路径
     *@param   language  切换识别的语言  chi_sim ：简体中文， eng  根据需求选择语言库
     */
    public String recognizeImage(String path, String outputPath,String language) {
    	//String path = "E:\\WorkSpace\\ProtocolTest";    //
        //String path = System.getProperty("uesr.dir");
        
        File file = new File(outputPath);
        ITesseract instance = new Tesseract();
   
      /*
            *  获取项目根路径，例如： D:\IDEAWorkSpace\tess4J
       */
          File directory = new File(path);
          String courseFile = null;
          try {
              courseFile = directory.getCanonicalPath();
          } catch (IOException e) {
              e.printStackTrace();
          }
   
          //设置训练库的位置
          instance.setDatapath(courseFile + "//tessdata");
   
          instance.setLanguage(language);//chi_sim ：简体中文， eng  根据需求选择语言库
          String result = null;
          try {
              long startTime = System.currentTimeMillis();
              result =  instance.doOCR(file);
              long endTime = System.currentTimeMillis();
              //System.out.println("Time is：" + (endTime - startTime) + " 毫秒");
          } catch (TesseractException e) {
              e.printStackTrace();
          }
          
          //System.out.println("result: ");
          //System.out.println(result);
          return result;
    }
  //文件下载代码，除了给定文件的URl地址外，在给定一个文件保存目录
  		public void doDownload(String imageSrc, String outputPath) {
  			int dlstatus;
  			try {
  				//发送HTTP的GET请求获取到验证码图片
  				URL url = new URL(imageSrc);
  				File outfile = new File(outputPath);
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
