package com.woniuxy.bet;

import java.util.HashMap;
import java.util.Map;

import com.sun.swing.internal.plaf.basic.resources.basic;
import com.woniuxy.http.HttpRequest;

public class Register {
	HttpRequest hRequest = new HttpRequest();

	public static void main(String[] args) {
		
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", "192.168.1.40");
		System.setProperty("http.proxyPort", "8888");
		Register reg = new Register();
		reg.doregister();

	}
	
	public void doregister() {
		Map<String, String> map=new HashMap<String,String>();
		String id = "ASP.NET_SessionId=r2nbfpbsk1tll4r4yvvx1fz3; CurrentSkin=haocai; XYHandicap=0; multiSelect=False; skinStyle=haocai-red";
		String xrefer ="XMLHttpRequest";
		map.put("Cookie", id);
		map.put("X-Requested-With", xrefer);
		hRequest.addHeader(map);
		
		for(int n=13; n<=13; n++) {
		String postUrl = "http://dqtgshcweb.gb666.net/Home/Register";
		String account = "bbc"+n;
		String postData = "account="+account+"bbc1212&password=aaaa2222&repassword=aaaa2222&phone=15245878457&ModelType=9&verifycode=";
		String respreg = hRequest.sendPost(postUrl, postData);
		System.out.println(respreg);
		try {
			Thread.sleep(90);
		} catch (InterruptedException e) {}
		if(respreg.contains("注册成功")) {
			System.out.println(account + "注册成功");
	   }else {
		   System.out.println(account + "注册失败");
	}
		
	}
	}
}
