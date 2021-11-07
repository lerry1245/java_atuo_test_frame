package com.woniuxy.phpwind;

import java.net.URLEncoder;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.common.Common;
import com.woniuxy.http.HttpRequest;

public class LxTest {

	HttpRequest hr = new HttpRequest();
	PhpWindCommon hcom = new PhpWindCommon();
	static int period = 0;
	static int fstatus = 0;
	String baseUrl = "http://dqtgshcweb.gb666.net";

	public static void main(String[] args) {
		//设置代理服务器，让fiddler可监控该代码的请	
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost","192.168.1.40");
		System.setProperty("http.proxyPort","8888");
		LxTest lt = new LxTest();
		lt.dologin();
		lt.dobet("178"); 
		
	}

	public void dologin() { 
		try {
			String postUrl = baseUrl + "/Home/login";
			String postData = "username=a02&password=8196658ecaeceb870d0ad3053dd579d2&validateCode=";
			//System.out.println(hcom.md5Secret("qwe123"));
			String resplogin = hr.sendPost(postUrl, postData);
			String loginfo = JsonPath.read(resplogin, "$.info");
			if (loginfo.contains("登录成功")) {
				System.out.println("接口登录成功……");
			} else {
				System.out.println("接口登录失败……");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//获取开盘的状态
	private int getOpStatus(String gameid) {
		String respget = hr.sendGet( baseUrl + "/Shared/GetNewPeriod?gameid="+ gameid, "");
		int fstatus = JsonPath.read(respget, "$.fstatus");
		System.out.println("fstatus为：" + fstatus);
		return fstatus;
	}
	
    //获取期数信息
	private int getPeriodId(String gameid) {
		String respget = hr.sendGet(baseUrl + "/Shared/GetNewPeriod?gameid=" + gameid, "");
		int periodid = JsonPath.read(respget, "$.fid");
		System.out.println("fid为：" + periodid);
		return periodid;
	}

	public void dobet(String gameid) {
		long sequence = System.currentTimeMillis();
		period = this.getPeriodId(gameid);
		fstatus = this.getOpStatus(gameid);
		if (fstatus == 1) {
			String postUrl = baseUrl+"/OfficialAddOrders/AddOrders";
			// 快三198
			 String orderList ="[{\"i\":21339,\"c\":\"17\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":2,\"ts\":1612974560}]";
			//"[{\"i\":25332,\"c\":\"大\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":2,\"ts\":1612974560},{\"i\":25333,\"c\":\"小\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":2,\"ts\":1612974560}]";
			System.err.println("orderList:" + orderList);
			 // 分分彩
			//String orderList = "[{\"i\":24253,\"c\":\"5|6|4\",\"n\":1,\"t\":1,\"k\":0,\"m\":2,\"a\":0.2,\"ts\":1612978596}]";
			String datalist = URLEncoder.encode(orderList);
			System.err.println(datalist);
			String postparam1 = "gameId="+ gameid + "&periodId=" + period + "&isSingle=false&canAdvance=false&orderList="    
					+ datalist + "";

			System.out.println(postparam1);
			String resplogin = hr.sendPost(postUrl, postparam1);
			String loginfo = JsonPath.read(resplogin, "$.info");
			if (loginfo.contains("投注成功！")) {
				System.out.println("接口投注成功……");
			} else {
				System.out.println("接口投注失败……");
			}
		}
		else {		
			System.err.println("游戏未开盘……");
			this.dobet(gameid);
		}
	}

}
