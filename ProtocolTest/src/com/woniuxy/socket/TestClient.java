package com.woniuxy.socket;

import java.io.OutputStream;
import java.net.Socket;

public class TestClient {

	public static void main(String[] args) {
		TestClient  tc = new TestClient();
		tc.tcpSend("192.168.1.250", 1000);
		

	}
	/*
	 * 利用代码来处理TCP连接步骤：
	 * 1.通过指定IP地址和端口号建立与对方的连接
	 * 2.通过输出流发送数据包
	 * 3.关闭TCP连接
	  * 核心对象：Socket
	 *@param ipAddr 接收服务器IP  port 接收服务器端口
	 */
	public void tcpSend(String ipAddr, int port) {
		for(int i=1;i<=10;i++) {
		   String	content ="第二轮：" + i + "次，这是消息的内容";
			try {
				//通过指定的服务器端IP和端口号建立TCP连接
				Socket socket = new Socket(ipAddr,port);
				//通过定义字节输出流，奖消息发送到服务器
				OutputStream out = socket.getOutputStream();
				byte[] buf = content.getBytes("GBK");
				out.write(buf);
				out.flush();  //释放缓存
				//关闭TCP连接
				socket.close();
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

}
