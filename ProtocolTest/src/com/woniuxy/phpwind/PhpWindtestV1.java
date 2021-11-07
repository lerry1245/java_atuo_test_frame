package com.woniuxy.phpwind;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.woniuxy.http.HttpRequest;

public class PhpWindtestV1 {
	private static final String String = null;
	HttpRequest request = new HttpRequest();

	// public String ValidateToken= "";

	public static void main(String[] args) {

		PhpWindtestV1 pt = new PhpWindtestV1();
//		pt.testLoginOK();
//        pt.testLoginFail();
//        pt.addPhpWindPosts();
//        pt.register();

	}
    //瀹炵幇鐧诲綍鐨勬柟娉�
	public String doLogin(String username, String password) { //
		String resp = "";
		String postUrl = "http://localhost:8081/phpwind/login.php?";
		String postParam = "step=2&lgt=0&pwuser=" + username + "&pwpwd=" + password;
		resp = request.sendPost(postUrl, postParam);
		return resp;
	}
    //娴嬭瘯鐧诲綍鎴愬姛  "admin", "wen1234567"
	public void testLoginOK() {
		String loginResp = this.doLogin("a01", "qwe123");
		// System.out.println(loginResp);

		if (loginResp.contains("鎮ㄥ凡缁忛『鍒╃櫥褰�")) {
			System.out.println("鐧诲綍娴嬭瘯_鎴愬姛");
		} else {
			System.out.println("鐧诲綍娴嬭瘯_澶辫触");
		}

	}
    //娴嬭瘯鐧诲綍澶辫触
	public void testLoginFail() {
		String loginResp = this.doLogin("admin", "wen1234567");
		// System.out.println(loginResp);
		if (loginResp.contains("瀵嗙爜閿欒鎴栧畨鍏ㄩ棶棰橀敊璇� ")) {
			System.out.println("鐧诲綍澶辫触娴嬭瘯_鎴愬姛");
		} else {
			System.out.println("鐧诲綍澶辫触娴嬭瘯_澶辫触");
		}
	}

	public void addPhpWindPosts() {
		String getUrl = "http://localhost:8081/phpwind/post.php?fid=8";  
		String response = request.sendGet(getUrl, "");
		System.out.println(response);
		// 鍒╃敤宸﹀彸杈圭晫鏂瑰紡鏉ュ畾浣嶅姩鎬侀獙璇佺爜
		String regex = "(.*\"verify\" value=\")(.+)(\" />.*)";
		//String regex = "(verifyhash = ')(.+)(';)";
		String VerifyCode = this.getVerifyCode(response, regex);
		System.out.println(VerifyCode);
		String postUrl = "http://localhost:8081/phpwind/post.php?fid=8";
		String postParam = "magicname=&magicid=&verify=" + VerifyCode
				+ "&atc_title=鎴戣涔板椹皍yt&atc_iconid=0&atc_content=鎴戣涔板椹拌嚦灏婄増锛屽姫鍔涘姞娌癸紒锛侊紒濂ュ姏缁�"
				+ "&atc_autourl=1&atc_usesign=1&atc_convert=1&atc_rvrc=0&atc_enhidetype=rvrc"
				+ "&atc_money=0&atc_credittype=money&atc_desc1=&attachment_1&att_special1=0&att_ctype1=money"
				+ "&atc_needrvrc1=0&step=2&pid=&action=new&fid=8tid=&article=0&special=0";
		String resp = request.sendPost(postUrl, postParam);
//		System.out.println(resp);
		if (resp.contains("鍙戝笘瀹屾瘯鐐瑰嚮杩涘叆涓婚鍒楄〃")) {
			System.out.println("鍙戝笘娴嬭瘯_鎴愬姛");
		} else {
			System.out.println("鍙戝笘娴嬭瘯_澶辫触");
		}

	}

	public void register() {
		for (int i = 0; i <= 10; i++) {
			String username = "aaa0" + i;
			String email = "aaa0" + i + "%40qq.com";
			String postUrl = "http://localhost:8081/phpwind/register.php? ";
			String postParam = "forward=&step=2&regname=" + username + "&regpwd=qwe123&regpwdrepeat=qwe123&regemail="
					+ email + "&rgpermit=1";
			String resp = request.sendPost(postUrl, postParam);
//			String getcookie = request.cookie;
//			System.out.println(getcookie);
			request.clearCookie();
			if (resp.contains("step=finish")) {
				System.out.println(username + ":娉ㄥ唽鎴愬姛");
			} else {
				System.out.println(username + ":娉ㄥ唽澶辫触");
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("鐢ㄦ埛娉ㄥ唽瀹屾垚");

    }
	// 鑾峰彇鍙戝笘椤甸潰鐨勫姩鎬侀獙璇佺爜
	public String getVerifyCode(String response, String regex) {
		String verifyCode = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(response);
		if (m.find()) {
			verifyCode = m.group(2);
			// 鑾峰彇杩斿洖甯︽崲琛屽氨瑕佹埅鍙�
			// verifyCode= verifyCode.substring(0,8);
		}
		return verifyCode;
	}
}
