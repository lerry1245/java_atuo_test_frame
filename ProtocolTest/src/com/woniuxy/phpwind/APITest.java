package com.woniuxy.phpwind;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.http.HttpRequest;

public class APITest {
	
	HttpRequest hr = new HttpRequest();
	String  baseUrl = "http://ydysapitest.wsx1263.com/api?";

	public static void main(String[] args) {
		
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost","192.168.1.40");
		System.setProperty("http.proxyPort","8888");
		
		APITest at = new APITest();
		at.dologin();
		at.getPeriodId();
		at.dobet();
	}
	public void getValidateToken() {
		long currentTime = System.currentTimeMillis();
	
		try {
			String getdata = baseUrl+"method=GetValidateTokenKey&_="+currentTime;
			System.out.println("getdata:" + getdata);
			String resptoken = hr.sendGet(getdata, "");
			System.err.println("resptokem返回值为：" + resptoken);
			String validatetoken = JsonPath.read(resptoken,"$.Data.Value");
			String sessionId = JsonPath.read(resptoken,"$.Data.SessionId");
			Map<String, String> map=new HashMap<String,String>();
			map.put("validateToken", validatetoken);
			map.put("ASP.NET_SessionId", sessionId);
			hr.addHeader(map);
			System.out.println("validatetoken:"+ validatetoken);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dologin() {
		long currentTime = System.currentTimeMillis();  
		try {
			this.getValidateToken();
			hr.clearCookie();
			String postData = "{\"param\":\"{\\\"userName\\\":\\\"fox6\\\",\\\"password\\\":\\\"130811dbd239c97bd9ce933de7349f20\\\",\\\"validateCode\\\":\\\"\\\",\\\"onlyFlag\\\":\\\"VUEX21f1f19edff198e2a2356bf4XXXX\\\",\\\"clientFlag\\\":\\\"WEB\\\"}\"}";
			String resplogin =hr.sendPost(baseUrl+"method=Login&_="+currentTime, postData);
			System.out.println(resplogin);
			String loginfo = JsonPath.read(resplogin,"$.Info");
			
			if(loginfo.contains("登录成功")) {
				System.out.println("接口登录成功……");
			}
			else {
				System.out.println("接口登录失败……");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private int  getPeriodId() {
		int periodid = 0;
		long currentTime = System.currentTimeMillis();
		String gameId = "{\"Id\"";
		String gameId2 = "169}";
		System.out.println(gameId+":"+gameId2);
		String engameId = URLEncoder.encode(gameId);
		String engameId2 = URLEncoder.encode(gameId2);
		String param = engameId+":"+engameId2;
		
		System.out.println(baseUrl+"method=GetRoyalPeriod&param="+param+"&_="+currentTime);
		String respget;
		try {
			respget = hr.sendGet(baseUrl+"method=GetRoyalPeriod&param="+param+"&_="+currentTime, "");
			System.out.println("respget为："+respget);
			periodid = JsonPath.read(respget, "$.Data.Fid");
			System.out.println("Fid为：" + periodid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return periodid;
	}
	
	public void dobet() {
		String gameId = "169";
		long sequence = System.currentTimeMillis();
		int period = this.getPeriodId();
		String postUrl = baseUrl+"method=OfficialAddOrders&_="+sequence;
		String orderList = "[{\\\\\\\"i\\\\\\\":21014,\\\\\\\"c\\\\\\\":\\\\\\\"2\\\\\\\",\\\\\\\"t\\\\\\\":1,\\\\\\\"n\\\\\\\":1,\\\\\\\"a\\\\\\\":2,\\\\\\\"m\\\\\\\":1,\\\\\\\"k\\\\\\\":0,\\\\\\\"ts\\\\\\\":1614452753756}]";
		String postData = "{\"param\":\"{\\\"GameId\\\":"+gameId+",\\\"PeriodId\\\":"+period+",\\\"orderlist\\\":\\\""+orderList+"\\\",\\\"canAdvance\\\":false}\"}";
		String respBet;
		try {
			respBet = hr.sendPost(postUrl, postData);
			System.out.println("respBet:"+respBet);
			String loginfo = JsonPath.read(respBet,"$.Info");
			if(loginfo.contains("下注成功！")) {
				System.out.println("接口投注成功……");
			}
			else {
				System.out.println("接口投注失败……");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
