package com.woniuxy.phpwind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.woniuxy.http.HttpRequest;

public class PhpWindTest {

	private static final String String = null;
	HttpRequest request = new HttpRequest();

	// public String ValidateToken= "";

	public static void main(String[] args) {

		PhpWindTest pt = new PhpWindTest();

//      pt.register();
//		pt.ramdomPostMany();
		pt.ramdomReplyMany();


	}

	public String doLogin(String username, String password) { //
		String resp = "";
		String postUrl = "http://localhost:8081/phpwind/login.php?";
		String postParam = "step=2&lgt=0&pwuser=" + username + "&pwpwd=" + password;
		resp = request.sendPost(postUrl, postParam);
		return resp;
	}
    //随机大量发帖
	public void ramdomPostMany() {
		for (int i = 0; i <= 10; i++) {
			request.clearCookie();
			this.randomPostThree();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//随机大量回帖
	public void ramdomReplyMany() {
		for (int i = 1; i <= 10; i++) {
			request.clearCookie();
			this.randomReply();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 相对简单的方法实现随机发帖
	public void randomPostOne() {
		// 首先实现随机登录
		int uid = (int) (Math.random() * 50);
		String resp = this.doLogin("a0" + uid, "qwe123");
		if (resp.contains("您已经顺利登录")) {
			System.out.println("a0" + uid + "随机登录_成功");
		} else {
			System.out.println("a0" + uid + "随机登录_失败");
		}
		// 查找注销的验证
		String respon = request.sendGet("http://localhost:8081/phpwind/login.php", "");
		String VerifyCode = this.getItem(respon, "(quit&verify=)(.+)(\">)");
		System.out.println(VerifyCode);
		// 构建随机的板块
		int fid = (int) (Math.random() * 12) + 6; // 随机6到17的fid
		// 随机标题和内容
		int sequence = (int) (Math.random() * 500000);
		String headline = "这是一条Java推送标题" + sequence;
		String content = "这是一条Java推送内容" + sequence;
		this.addPosts(headline, content, fid);
		// 注销用户
		request.sendGet("http://localhost:8081/phpwind/login.php?action=quit&verify=" + VerifyCode, "");
	}

	public void randomPostTow() {
		// 首先实现随机登录
		String[][] usrepass = { { "a03", "qwe123" }, { "a010", "qwe123" }, { "a015", "qwe123" }, { "a023", "qwe123" },
				{ "a033", "qwe123" }, { "a043", "qwe123" }, { "a027", "qwe123" }, { "a031", "qwe123" } };
		int upId = (int) (Math.random() * 8);
		String resp = this.doLogin(usrepass[upId][0], usrepass[upId][1]);
		if (resp.contains("您已经顺利登录")) {
			System.out.println(usrepass[upId][0] + "随机登录_成功");
		} else {
			System.out.println(usrepass[upId][0] + "随机登录_失败");
		}
		// 查找注销的验证
		String respon = request.sendGet("http://localhost:8081/phpwind/login.php", "");
		String VerifyCode = this.getItem(respon, "(quit&verify=)(.+)(\">)");
		// 构建随机的板块
		int[] fids = { 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		int fid = (int) (Math.random() * 11); // 取fids 0到10的下标数
		// 随机标题和内容
		int sequence = (int) (Math.random() * 50000000);
		String headline = "这是一条Java推送标题" + sequence;
		String content = "这是一条Java推送内容" + sequence;
		this.addPosts(headline, content, fid);
		// 注销用户
		request.sendGet("http://localhost:8081/phpwind/login.php?action=quit&verify=" + VerifyCode, "");
	}

	public void randomPostThree() {
		// 首先实现随机登录
		int uid = (int) (Math.random() * 50);
		String resp = this.doLogin("a0" + uid, "qwe123");
		if (resp.contains("您已经顺利登录")) {
			System.out.println("a0" + uid + "随机登录_成功");
		} else {
			System.out.println("a0" + uid + "随机登录_失败");
		}
		// 构建随机板块（左右边界的方式完成）
		// 1.获取响应页面上的源代码
		String respon = request.sendGet("http://localhost:8081/phpwind/", "");
		// 2.设定左右边界来查找所有板块的编号
		List<String> fids = this.getItems(respon, "(\"thread.php\\?fid=)(.+)(\" target=\"_blank)");
		//System.out.println(fids.size());
		// 生成一个随机数在list对象中取值
		int index = (int) (Math.random() * (fids.size()));
		int fid = Integer.parseInt(fids.get(index));		
//		for(int i =0;i<fids.size();i++) {
//			System.out.println(fids.get(i));
//		}
		// 随机标题和内容
		int sequence = (int) (Math.random() * 50000000);
		String headline = "这是一条Java推送标题" + sequence;
		String content = "这是一条Java推送内容" + sequence;
		this.addPosts(headline, content, fid);
		// 注销用户(在服务端已经清理cookie相当于注销)
		// request.sendGet("http://localhost:8081/phpwind/login.php?action=quit&verify="
		// + VerifyCode, "");
	} 

    //实现发帖并断言
	public void addPosts(String headline, String content, int fid) {
		String getUrl = "http://localhost:8081/phpwind/post.php?fid="+ fid;
		String response = request.sendGet(getUrl, "");
		// 利用左右边界方式来定位动态验证码
		String regex = "(.*\"verify\" value=\")(.+)(\" />.*)";
		String VerifyCode = this.getItem(response, regex);
		String postUrl = "http://localhost:8081/phpwind/post.php?";
		String postParam = "magicname=&magicid=&verify=" + VerifyCode + "&atc_title=" + headline
				+ "&atc_iconid=0&atc_content=" + content
				+ "&atc_autourl=1&atc_usesign=1&atc_convert=1&atc_rvrc=0&atc_enhidetype=rvrc"
				+ "&atc_money=0&atc_credittype=money&atc_desc1=&attachment_1&att_special1=0&att_ctype1=money"
				+ "&atc_needrvrc1=0&step=2&pid=&action=new&fid=" + fid + "&tid=&article=0&special=0";
		String resp = request.sendPost(postUrl, postParam);
		// System.out.println(resp);
		if (resp.contains("发帖完毕点击进入主题列表")) {
			System.out.println("板块"+fid+"发帖测试_成功");
		} else {
			System.out.println("板块"+fid+"发帖测试_失败");
		}

	}
    //实现注册
	public void register() {
		for (int i = 2; i < 10; i++) {
			String username = "a0" + i;
			String email = "5526056" + i + "%40qq.com";
			String postUrl = "http://localhost:8081/phpwind/register.php? ";
			String postParam = "forward=&step=2&regname=" + username + "&regpwd=qwe123&regpwdrepeat=qwe123&regemail="
					+ email + "&rgpermit=1";
			String resp = request.sendPost(postUrl, postParam);
			if (resp.contains("step=finish")) {
				System.out.println(username + ":注册成功");
			} else {
				System.out.println(username + ":注册失败");
			}
		}
	}
	//随机回帖
    public void randomReply() {
    	request.clearCookie();
    	//1.随机用户登录
    	int uid = (int) (Math.random() * 50);
		String resp = this.doLogin("a0" + uid, "qwe123");
		if (resp.contains("您已经顺利登录")) {
			System.out.println("a0" + uid + "随机登录_成功");
		} else {
			System.out.println("a0" + uid + "随机登录_失败");
		}
		//2.随机板块，获取所有的板块编号Fid
		String respon = request.sendGet("http://localhost:8081/phpwind/", "");
		List<String> fids = this.getItems(respon, "(\"thread.php\\?fid=)(.+)(\" target=\"_blank)");
		//System.out.println("共有板块"+fids.size());
		int fidsIndex = (int) (Math.random() * (fids.size()));
		int fid = Integer.parseInt(fids.get(fidsIndex));
		//System.out.println("随机板块为："+ fid);
		//3.随机页面，获取某个板块的页数
		String pageUrl = "http://localhost:8081/phpwind/thread.php?fid=" + fid;
		String pageResp = request.sendGet(pageUrl, "");
		String pagecount = this.getItem(pageResp, "(Pages: 1/)(.+)(&nbsp; &nbsp; &nbsp;)");
		//处理只有一页的时候
		int randomPage = 0;
		String page = "&page=\"";
		if(pagecount !="") {
			//System.out.println("页数有："+ pagecount);
			randomPage = (int)(Math.random()*(Integer.parseInt(pagecount)));
			//Random myrandom = new Random();
			//randomPage = myrandom.nextInt(Integer.parseInt(pagecount));
		}
		else {
			page = "";
		}
		//4.随机板块随机页面，获取随机的帖子编号tid
		String pageurl = "http://lcalhost:8081/phpwind/thread.php?fid=16";
		String tidUrl = "http://localhost:8081/phpwind/thread.php?fid="+fid+ page + randomPage;
		String tidResp = request.sendGet(pageUrl, "");
		List<String> tids = this.getItems(pageResp, "(\"read.php\\?tid=)(.+)(&page=)");
		//System.out.println("tid共有："+ tids.size());
		int tidsIndex = (int)(Math.random()*tids.size());
		int tid = Integer.parseInt(tids.get(tidsIndex));
		//随机回复的帖子的内容
		long time = System.currentTimeMillis();
		String headline = "这是Java回帖的标题" + time;
		String content = "这是Java回帖的内容" + time;
		this.reply(headline, content, fid, tid);
		//每次登录清空服务器的cookie
		request.clearCookie();
    	
    }
    //实现回帖的方法并断言
	public void reply(String headline,String content,int fid,int tid) {
		String getUrl = "http://localhost:8081/phpwind/post.php?fid="+ fid;
		String response = request.sendGet(getUrl, "");
		// 利用左右边界方式来定位动态验证码
		String regex = "(.*\"verify\" value=\")(.+)(\" />.*)";
		String verifyCode = this.getItem(response, regex);
		String postUrl ="http://localhost:8081/phpwind/post.php?";
		String postParam = "magicname=&magicid=&verify=" + verifyCode + "&atc_title=Re:这是一条Java推送标题42917980"
				+ "&atc_iconid=0&atc_content="+content
				+ "&atc_autourl=1&atc_usesign=1&atc_convert=1&atc_rvrc=0&atc_enhidetype=rvrc"
				+ "&atc_money=0&atc_credittype=money&atc_desc1=&attachment_1&att_special1=0&att_ctype1=money"
				+ "&atc_needrvrc1=0&step=2&pid=&action=reply&fid=" + fid + "&tid="+ tid +"&article&special=0";
		String resp = request.sendPost(postUrl, postParam);
		//System.out.println(postParam);
		//System.out.println(resp);
		if(resp.contains("发帖完毕点击进入主题列表")) {
			System.out.println("板块："+ fid +",贴号：" + tid + ",回帖测试_成功");
		}
		else {
			System.out.println("板块："+ fid +",贴号：" + tid + ",回帖测试_失败");
		}
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

		// 通过左右边界查找多条记录
		public List<String> getItems(String response, String regex) {
			List<String> items = new ArrayList<String>();
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(response);
			while (m.find()) {
				items.add(m.group(2));
				//System.out.println(m.group(2));
			}
			return items;
		}
}
