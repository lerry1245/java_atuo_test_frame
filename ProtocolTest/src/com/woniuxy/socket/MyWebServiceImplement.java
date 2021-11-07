package com.woniuxy.socket;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//
//import javax.jws.WebService;
//import javax.xml.ws.Endpoint;
//
//@WebService
//public class MyWebServiceImplement implements MyWebServiceInterface {
//
//	//涓存椂璋冭瘯鏈被鐨勪袱涓鍐欐柟娉�
//	public static void main(String[] args) {
//		
//		String address = "http://127.0.0.1:8899/ws/MyWebService";
//		//浣跨敤Endpoint绫绘彁渚涚殑publish鏂规硶鍙戝竷WebService锛屽彂甯冩槸瑕佷繚璇佷娇鐢ㄧ殑绔彛鍙锋病鏈夎鍏朵粬绋嬪簭鍗犵敤
//		Endpoint endpoint = Endpoint.publish(address, new MyWebServiceImplement());
//		System.out.println(address + "?WSDL");
//	}
//	    @Override
//		public String readFromFile(String fileName) {
//			String content ="";
//			
//			try {
//				File file = new File(fileName);
//				InputStream in = new FileInputStream(file);
//				InputStreamReader reader = new InputStreamReader(in,"GBK");
//				BufferedReader br = new BufferedReader(reader);
//				String line ="";
//				while((line = br.readLine()) != null) {
//					content += line + "\n";
//				}
//				br.close();
//				reader.close();
//				in.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return content;
//		}
//	
//	    
//		@Override
//		public String writeToFile(String fileName, String content) {
//			
//			try {
//				File file = new File(fileName);
//				OutputStream in = new FileOutputStream(file,true);
//				OutputStreamWriter writer = new OutputStreamWriter(in,"GBK");
//				BufferedWriter bw = new BufferedWriter(writer);
//				bw.write(content,0,content.length());
//				bw.flush();  //寮哄埗灏嗙紦鍐插尯鐨勫唴瀹瑰彂閫佸嚭鍘伙紝涓嶅繀绛夊埌缂撳啿鍖烘弧
//				bw.close();  //鍏抽棴娴�
//				writer.close();  //鍏抽棴娴�
//				in.close();  //鍏抽棴娴�  
//				return "鍐欏叆鎴愬姛";
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "鍐欏叆鎴愬姛";
//			}
//		}
//
//}
