package com.woniuxy.http;

public class AgileoneProposal {

	HttpRequest request = new HttpRequest();
	AgileoneCommon ac = new AgileoneCommon();
	String cookie = this.getLoginCookie();


	public static void main(String[] args) {
		AgileoneProposal ap = new AgileoneProposal();
//		at.testAddNotice();
//		ap.testAddProposalFail();
		ap.testAddProposalOK();
	
		ap.testEditProposal();
		ap.testQueryProposal();
		ap.testDeletProposal();

	}

	// 获取登录时的loginsession
	public String getLoginCookie() {
		String postUrl = "http://localhostagileone/index.php/common/login";
		String postParam = "username=admin&password=admin";
		String respLogin = request.sendPost(postUrl, postParam);
		String respcookie = request.cookie;
		// System.out.println(respcookie);
		return respcookie;
	}

	// 执行新增需求提案
	public String doAddProposal(String headline, String content) {
		String postUrl = "http://localhost/agileone/index.php/proposal/add";
		String postParam = "type=Requirement&importance=Medium&headline=" + headline + "&content=" + content
				+ "&processresult=;" + cookie;
		String resp = request.sendPost(postUrl, postParam);
		//System.out.println(resp);
		return resp;

	}

	// 增加需求提案失败测试
	public void testAddProposalFail() {
		String resp = this.doAddProposal("这是一条需求提案", "这是需求提案的内容");
		if (resp.contains("proposal_exist")) {
			System.out.println("需求提案重复提交验证_成功");
		} else {
			System.out.println("需求提案重复提交验证_失败");
		}
	}

	// 增加需求提案成功测试
	public String testAddProposalOK() {
		int countBerfore = ac.getCountByTable("proposal");
		long sequence = System.currentTimeMillis(); // 从Unix元年（1970-1-1 0:0:0:0）到现在的毫秒数
		String headline = "这是需求提案的标题" + sequence, contents =  "这是需求提案的内容" + sequence;
		String resp = this.doAddProposal(headline,contents);
		// 第一种检查手段：正向检查响应的内容：使用正则表达式
		if ((resp.replace("\n", "")).matches("\\d{1,5}")) {
			System.out.println("新增需求提案-正向检查-成功");
		} else {
			System.out.println("新增需求提案-正向检查-失败");
		}
		// 第二种检查手段，逆向判断，如果没有出现错误的响应，则说明正常
		if (resp.contains("no_permisson") || resp.contains("proposal_exist") || resp.contains("rang_passeord")) {
			System.out.println("新增需求提案-逆向检查-失败" + resp);
		} else {
			System.out.println("新增需求提案-逆向检查-成功");
		}

		// 第四种：直接通过数据来判断，利用JDBC操作MYSQL数据库
		int countAfter = ac.getCountByTable("proposal");
		if (countAfter == (countBerfore + 1)) {
			System.out.println("新增需求提案-数据库检查-成功");
		} else {
			System.out.println("新增需求提案-数据库检查-失败");
		}

		// 第四种扩展：直接查看标题是否存在数据库中
		//int proposalID = Integer.parseInt(resp);
		String proposalID = resp;
		String sql = "select headline from proposal where proposalid ="+proposalID;
		String content = ac.checkColumnContent(sql);
		if (content.equals("这是需求提案的标题" + sequence)) {
			System.out.println("新增需求提案标题验证-成功");

		} else {
			System.out.println("新增需求提案标题验证-失败");
		}
		return resp;
	}


	// 测试删除需求提案接口
	
	public void testDeletProposal() {
		//int proposalID = Integer.parseInt(this.testAddProposalOK());
		String proposalID =this.testAddProposalOK();
		int countBerfore = ac.getCountByTable("proposal");
		String postUrl = "http://localhost/agileone/index.php/proposal/delete";
		String postParam = "proposalid="+ proposalID  + cookie;
		String respdele = request.sendPost(postUrl, postParam);
		//返回值断言
		if ((respdele.replace("\n", "")).equals("1")) {
			System.out.println("删除提案测试_成功");
		} else {
			System.out.println("删除提案测试_失败");
			
		}
		//通过数据判断
		int countAfter = ac.getCountByTable("proposal");
		if (countAfter == (countBerfore - 1)) {
			System.out.println("删除需求提案-数据库检查-成功");
		} else {
			System.out.println("删除需求提案-数据库检查-失败");
		}

	}

	// 测试编辑需求提案接口
	public void testEditProposal() {
		//int proposalID = Integer.parseInt(this.testAddProposalOK());
		String proposalID =this.testAddProposalOK();
		String sql = "select headline from proposal where proposalid ="+proposalID;
		String contentBefore = ac.checkColumnContent(sql);
		long sequence = System.currentTimeMillis();
		String postUrl = "http://localhost/agileone/index.php/proposal/edit";
		String postParam = "proposalid=" + proposalID +"&type=Requirement&importance=Medium&headline=这是一条需求提案-" + sequence + "&"
				+ "content=这是需求提案的内容edit&processresult=;" + cookie;
		String respedit = request.sendPost(postUrl, postParam);
		//返回值断言
		if ((respedit.replace("\n", "")).equals("1")) {
			System.out.println("编辑提案测试_成功");
		} else {
			System.out.println("编辑提案测试_失败");
		}
		//利用数据库断言
		String contentAfter = ac.checkColumnContent(sql);
		if (contentAfter.equals(contentBefore)) {
			System.out.println("编辑需求提案标题数据库验证-失败");

		} else {
			System.out.println("编辑需求提案标题数据库验证-成功");
		}
	}

	// 测试查询需求提案接口
	public void testQueryProposal() {
		//int proposalID = Integer.parseInt(this.testAddProposalOK());
		String proposalID =this.testAddProposalOK();
		String postUrl = "http://localhost/agileone/index.php/proposal/query";
		String postParam = "proposalid="+proposalID+";" + cookie;
		String respedit = request.sendPost(postUrl, postParam);
		//返回值断言
		if ((respedit.replace("\n", "")).contains("\"totalRecord\":1")) {
			System.out.println("查询需求提案测试_成功");
		} else {
			System.out.println("查询需求提案测试_失败");
		}
	    
		//查询数据库断言
		String sql = "select proposalid from proposal where proposalid ="+ proposalID;
		String content = ac.checkColumnContent(sql);
		//int result = Integer.parseInt(content);
		if (content.equals(proposalID)) {
			System.out.println("查询需求提案数据库验证-成功");

		} else {
			System.out.println("查询需求提案数据库验证-失败");
		}
	}

}

//// 第三种：通过查询来判断
//String queryURL = "http://localhost/agileone/index.php/proposal/query";
//String queryParam = "currentpage=1&proposalid=&creator=admin&type=Requirement&importance=Medium&headline=&content=&processresult=";
//String respQuery = request.sendPost(queryURL, queryParam); // 查询新增的公告
//String checkStr = "{\"totalRecord\":"; // 返回结果中字符
//int posLeft = respQuery.indexOf(checkStr) + checkStr.length(); // 拿到查询字符的坐下标
//int posRight = respQuery.indexOf("}"); // 拿到查询字符的右下标
//String destStr = respQuery.substring(posLeft, posRight); // 获取目标字符串
//if (Integer.parseInt(destStr) == 1) {
//	System.out.println("新增需求提案-查询检查-成功");
//} else {
//	System.out.println("新增需求提案-查询检查-失败");
//}
