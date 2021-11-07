package com.woniuxy.cbt.test;

import com.woniuxy.cbt.core.AutoLogger;
import com.woniuxy.cbt.core.Common;
import com.woniuxy.cbt.core.Httprequestor;
import com.woniuxy.cbt.core.Reporter;

public class AgileoneProposal {

	private Common common = new Common();
	private Reporter report = new Reporter();
	private Httprequestor hr = new Httprequestor();

	public void mainTest() {
		this.prepare();
		this.testAdd();
		this.testDelet();
		this.testEdit();
		this.finish();

	}

	// 测试执行前的准备工作
	private void prepare() {
		System.out.println("需求提案-接口测试开始……");
		// 为Reporter对象定义当前模块名称
		Reporter.module = "需求提案";

		// 先登录到Agileone
		String postUrl = "http://localhost/agileone/index.php/common/login";
		String postParam = "username=admin&password=admin&savelogin=true";
		hr.sendPost(postUrl, postParam);
	}

	// 测试完成后的收尾工作
	private void finish() {
		System.out.println("需求提案-接口测试完成……");
		// 注销当前的登录状态
		hr.sendGet("http://localhost/agileone/index.php/common/logout", "");

	}

	//Action组件实现新增需求提案的操作
	public String doAddProposal(String type, String importance, String headline, String content) {
		String postUrl = "http://localhost/agileone/index.php/proposal/add";
		String postParam = "type=" + type + "&importance=" + importance + "&headline=" + headline + "&content="
				+ content + "&processresult=;";
		String resp = hr.sendPost(postUrl, postParam);
		return resp;
	}

	//Test组件用于测试新增需求提案
	public void testAdd() {
		// 设置测试用例的基础信息
		String caseId = "Agileone_Proposal_001";
		String caseDesc = "需求模块-功能测试-正常新增";

		String type = "Enhancement";
		String importance = "High";
		String headline = "这是需求提案的标题-" + common.getRandom(10000, 99999);
		String content = "这是需求提案的内容-" + common.getRandom(10000, 99999);

		try {
			String respAdd = this.doAddProposal(type, importance, headline, content);
			respAdd = respAdd.replace("\n", "");
			if (respAdd.replace("\n", "").matches("\\d{1,5}")) {
				String result = Reporter.PASS;
				report.writelog(caseId, caseDesc, result, "", "");
				AutoLogger.log.info("测试成功！");
			} else {
				String result = Reporter.FAIL;
				String error = "操作后信息为：" + respAdd;
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
				AutoLogger.log.info("测试失败！");
			}
		} catch (Exception e) {
			String result = Reporter.ERROR;
			String error = e.getMessage();
			String screenshot = report.captureScreen();
			report.writelog(caseId, caseDesc, result, error, screenshot);
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}

	// 获取proposalID
	public String getProposalId() {
		String postUrl = "http://localhost/agileone/index.php/proposal/query";
		String postParam = "currentpage=1";
		String resp = hr.sendPost(postUrl, postParam);
		String left = "proposalid\":\"";
		String right = "\",\"projectid";
		String respId = common.getItem(resp, left, right);
		// int Id = Integer.parseInt(respId);
		return respId;
	}

	// Action组件实现删除需求提案
	public String doDelete(String proposalId) {
		String postUrl = "http://localhost/agileone/index.php/proposal/delete";
		String postParam = "proposalid=" + proposalId;
		String resp = hr.sendPost(postUrl, postParam);
		return resp;
	}

	// Test组件用于测试删除需求提案
	public void testDelet() {
		// 设置测试用例的基础信息
		String caseId = "Agileone_Proposal_002";
		String caseDesc = "需求模块-功能测试-删除需求提案测试";
		String proposalId = this.getProposalId();
		try {
			String respDele = this.doDelete(proposalId);
			respDele = respDele.replace("\n", "");
			// 返回值断言
			if (respDele.equals("1")) {
				String result = Reporter.PASS;
				report.writelog(caseId, caseDesc, result, "", "");
				AutoLogger.log.info("测试成功！");
			} else {
				String result = Reporter.FAIL;
				String error = "操作后信息为：" + respDele;
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
				AutoLogger.log.info("测试失败！");
			}
		} catch (Exception e) {
			String result = Reporter.ERROR;
			String error = e.getMessage();
			String screenshot = report.captureScreen();
			report.writelog(caseId, caseDesc, result, error, screenshot);
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}

	// Action组件实现修改需求提案
	public String doEdit(String proposalId, String type, String importance,String headline, String content) {
		
		String postUrl = "http://localhost/agileone/index.php/proposal/edit";
		String postParam = "proposalid=" + proposalId + "&type="+ type +"&importance=" + importance + "&headline="+ headline
				 + "&content=" + content;
		String respedit = hr.sendPost(postUrl, postParam);
		return respedit;
	}
	
	// Test组件测试编辑需求提案
	public void testEdit() {
		String caseId = "Agileone_Proposal_003";
		String caseDesc = "需求模块-功能测试-编辑需求提案测试";
		
		String proposalId = this.getProposalId();
		String type = "Enhancement";
		String importance = "High";
		String headline = "这是编辑提案的标题-" + common.getRandom(10000, 99999);
		String content = "这是编辑提案的内容-" + common.getRandom(10000, 99999);
		try {
			String respet = this.doEdit(proposalId, type, importance, headline, content);
			respet = respet.replace("\n", "");
			if (respet.equals("1")) {
				String result = Reporter.PASS;
				report.writelog(caseId, caseDesc, result, "", "");
				AutoLogger.log.info("测试成功！");
			} else {
				String result = Reporter.FAIL;
				String error = "操作后信息为：" + respet;
				String screenshot = report.captureScreen();
				report.writelog(caseId, caseDesc, result, error, screenshot);
				AutoLogger.log.info("测试失败！");
			}
		} catch (Exception e) {
			String result = Reporter.ERROR;
			String error = e.getMessage();
			String screenshot = report.captureScreen();
			report.writelog(caseId, caseDesc, result, error, screenshot);
			AutoLogger.log.error(e,e.fillInStackTrace());
		}	
	}
}
