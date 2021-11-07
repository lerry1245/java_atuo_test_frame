package com.woniuxy.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ylpttest {
	HttpRequest hr = new HttpRequest();

	public static void main(String[] args) {
		ylpttest ylt = new ylpttest();
		//ylt.getsid();
		ylt.dologin();
//		String sid = ylt.getsid();
//		System.out.println(sid);
	}

	public String  getsid() {
		String getUrl = "http://6rtj0qib.qaz2032.com/login";
		String resp = hr.sendGet(getUrl, "");
		//System.out.println(resp);
		String sidvalue = this.getItem(resp, "(\"sid\" value=).(.*?).( />)");
		return sidvalue;
		
		
	}
	
	public void  dologin() {
	    String sidvalue = this.getsid();
		String postUrl ="http://6rtj0qib.qaz2032.com/Login";
		//String postParam="username=hy1&password=f09caad93c556216048595de602044b0&validateCode=9999&sid="+sidvalue;
		String postParam="username=hy1&password=f09caad93c556216048595de602044b0&validateCode=9999&sid=fneaoavcyqdo4ovmg5fkcqjv";
		System.out.println(postParam);
		String resp = hr.sendPost(postUrl, postParam);
		System.out.println(resp);
	}
	public void dobet() {
		String postUrl = "http://6rtj0qib.qaz2032.com/AddOrders/SixOrder";
		String postParam = "force=false&orderlist=%5B%7B%22id%22%3A%221343%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221343%22%2C%22odds%22%3A%2242%22%7D%2C%7B%22id%22%3A%221344%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221344%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221345%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221345%22%2C%22odds%22%3A%2242%22%7D%2C%7B%22id%22%3A%221346%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221346%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221347%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221347%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221348%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221348%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221349%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221349%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221350%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221350%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221351%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221351%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221352%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221352%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221353%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221353%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221355%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221355%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221356%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221356%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221357%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221357%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221359%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221359%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221361%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221361%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221362%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221362%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221363%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221363%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221364%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221364%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221365%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221365%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221366%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221366%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221367%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221367%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221368%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221368%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221370%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221370%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221371%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221371%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221373%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221373%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221375%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221375%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221377%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221377%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221378%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221378%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221379%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221379%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221381%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221381%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221382%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221382%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221384%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221384%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221386%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221386%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221387%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221387%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221388%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221388%22%2C%22odds%22%3A%2242.5%22%7D%2C%7B%22id%22%3A%221390%22%2C%22pid%22%3A%22457%22%2C%22amount%22%3A100%2C%22goal%22%3A%221390%22%2C%22odds%22%3A%2242.5%22%7D%5D&handicap=1&periodID=103117";
		String resp = hr.sendPost(postUrl, postParam);
		System.out.println(resp);
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
