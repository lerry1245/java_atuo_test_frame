package com.woniuxy.http;

public class AgileoneNotice {
	HttpRequest  hr = new HttpRequest();

	public static void main(String[] args) {
		AgileoneNotice an = new AgileoneNotice();
		an.testHome();
		an.testLogin();
		an.testAddNotice();
		an.testAddProposal();
		

	}
	public void testHome() {
		String getUrl = "http://localhost/agileone/index.php";
		String respHome = hr.sendGet(getUrl, "");
	}
	public void testLogin() {
		String postUrl = "http://localhost/agileone/index.php/common/login";
		String postParam ="username=admin&password=admin";
		String respLogin = hr.sendPost(postUrl, postParam);
		if(respLogin.contains("successful")) {
			System.out.println("登录接口测试——成功");
		}
		else {
			System.out.println("登录接口测试——失败");
		}
	}
	public void testAddNotice() {
		String postUrl =  "http://localhost/agileone/index.php/notice/add";	 
		String postParam = "headline=This is a notice&content=This is content of notice&content=<img src=\\\"../Attachment/None/20200901200219.jpg\\\" border=\\\"0\\\" />&scope=0&expireddate=2020-11-15";
		String resp = hr.sendPost(postUrl, postParam);
		System.out.println(resp);
		if((resp.replace("\n", "")).matches("\\d{1,10}")) {
			System.out.println("新增公告测试——成功");
		}
		else {
			System.out.println("新增公告测试——失败");
		}
	}
	public void testAddProposal() {
//		String cookie = this.getLoginCookie();
//		int countBerfore = this.getCountFromDB("select count(*) from proposal");

		long sequence = System.currentTimeMillis(); // 从Unix元年（1970-1-1 0:0:0:0）到现在的毫秒数		
		String headline = "这是一条需求提案-" + sequence;
		//System.out.println(headline);
		String postUrl = "http://localhost/agileone/index.php/proposal/add";
		String postParam = "type=Requirement&importance=Medium&headline="+headline+"&content=这是需求提案的内容&processresult=";
		String resp = hr.sendPost(postUrl, postParam);
		//System.out.println(postParam);
		// 第一种检查手段：正向检查响应的内容：使用正则表达式
		if (resp.replace("\n", "").matches("(.*)(\\d{1,5})(.*)")) { // \d或[0,9]:表达一个数字，
			System.out.println("新增需求提案-正向检查-成功");
		} else {
			System.out.println("新增需求提案-正向检查-失败" + resp);
		}
	}

}
