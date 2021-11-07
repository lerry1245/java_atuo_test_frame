package com.woniuxy.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FeiQClient {

	public static void main(String[] args) {
		FeiQClient  fc = new FeiQClient();
		fc.sendMsg();
	}
	//利用Java处理UDP数据包协议
	public void sendMsg(){
		for(int i=1; i<99999; i++) {
			long packetId = System.currentTimeMillis();
			String name = "admin";    //发送者姓名和主机名
			String host = "DESKTOP-GE5JS39H";
			long command = 0x00000020L; //IPMSG_SENDMSG;
			String content = "This is the message form Java"+ i;
			//拼装符合规则的数据包
			String message = "1.0:" + packetId + ":" + name + ":"
					+ host + ":" + command + ":" + content;
			try {
				//用Inetaddress方法寻址
				InetAddress  target =  InetAddress.getByName("192.168.1.250");
				byte[] buf = message.getBytes("GBK");
				//建立UDP连接并将数据包送出
				DatagramSocket ds = new DatagramSocket();  //实例化UDP对象
				DatagramPacket dp = new DatagramPacket(buf, buf.length, target, 2425);
				ds.send(dp);
				ds.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("消息发送完成");
	}

}
