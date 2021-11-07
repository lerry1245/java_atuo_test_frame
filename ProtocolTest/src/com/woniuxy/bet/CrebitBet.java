package com.woniuxy.bet;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.common.Common;
import com.woniuxy.common.DataDrivenOfInter;
import com.woniuxy.common.ExcelReader;
import com.woniuxy.common.ExcelWriter;

public class CrebitBet {
	// 把当前的执行时间加入到结果文件名中
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
	String createdate = sdf.format(date);
	// 打开Excel表进行读取
	ExcelReader excelR = new ExcelReader("data/CreditBetCase.xls");
	// 复制用例中的内容到结果中，并执行之后的写入操作
	ExcelWriter excelW = new ExcelWriter("data/CreditBetCase.xls", "result/Res-CreditBetCase" + createdate + ".xls");

	// 创建关键字类
	DataDrivenOfInter ddr = new DataDrivenOfInter(excelW);
	// 通过list读取每行中的内容
	List<String> list = null;

	Common common = new Common();
	static int period = 0;
	static int fstatus = 0;
	String baseUrl = "http://dqtgshcweb.gb666.net";
	static String gameid = "";

	public void mainTest() {
		this.dologin();
		this.doCreditbet(gameid);
	}

	public void dologin() {
		try {
			String postUrl = baseUrl + "/Home/login";
			String postData = "username=testsun&password=8196658ecaeceb870d0ad3053dd579d2&validateCode=";
			// System.out.println(hcom.md5Secret("qwe123"));
			String resplogin = ddr.testPost(postUrl, postData);
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

	// 获取开盘的状态和期数
	private int[] getOpStatus(String gameid) {
		int[] b = new int[2];
		String respget = ddr.testGet(baseUrl + "/Shared/GetNewPeriod?gameid=" + gameid, "");
		int fstatus = JsonPath.read(respget, "$.fstatus");
		int periodid = JsonPath.read(respget, "$.fid");
		b[0] = fstatus;
		b[1] = periodid;
		System.out.println("fstatus为：" + fstatus);
		return b;
	}

	// 信用游戏下注
	public void doCreditbet(String gameid) {
		try {
			ExcelReader excel = new ExcelReader("data/CreditBetCase.xls");
			long sequence = System.currentTimeMillis();
			for (int i = 1; i < excel.rows; i++) {
				int n = common.getRandom(1, excel.rows);
				int amount = common.getRandom(50, 100);
				long currentTime = System.currentTimeMillis();
				List<String> list = excel.readLine(n);
				gameid = list.get(1);				
				int[]  b = this.getOpStatus(gameid);
				fstatus = b[0];
				period = b[1];
				
				System.out.println(list);
				if (fstatus == 1) {
					String postUrl = baseUrl + "/AddOrders/OtherOrder";
					String orderList = "[{\"id\":" + list.get(5) + ",\"amount\":\"" + amount + "\",\"goal\":"
							+ list.get(6) + ",\"odds\":\"" + list.get(7) + "\",\"timestamp\":" + currentTime + "}]";
					// System.out.println("orderList:" + orderList);
					String datalist = URLEncoder.encode(orderList);
					System.out.println(datalist);
					String postparam = "gameId=" + gameid + "&orderlist=" + datalist
							+ "&force=false&selectBack=0&MaxBack=0&periodId=" + period;

					System.out.println("postparam:" + postparam);
					String resp = ddr.testPost(postUrl, postparam);
					common.sleepRandom(2, 5);
//				String betinfo = JsonPath.read(resp, "$.info");
					ddr.assertSame("$.info", "下注成功");
					if (resp.contains("下注成功")) {
						System.out.println("接口投注成功……");

					} else {
						System.out.println("接口投注失败……");
					}
				} else {
					System.err.println("游戏未开盘……");
					this.doCreditbet(gameid);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
