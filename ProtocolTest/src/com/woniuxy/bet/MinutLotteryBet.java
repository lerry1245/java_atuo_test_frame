package com.woniuxy.bet;

import java.net.URLEncoder;
import java.util.List;

import org.apache.axis2.transport.http.ListingAgent;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.common.Common;
import com.woniuxy.common.ExcelReader;
import com.woniuxy.http.HttpRequest;
import com.woniuxy.phpwind.PhpWindCommon;

public class MinutLotteryBet {

	HttpRequest hr = new HttpRequest();
	PhpWindCommon hcom = new PhpWindCommon();
	Common common = new Common();
	static int period = 0;
	static int fstatus = 0;
	static String gameid = "";
	String baseUrl = "http://dqtgshcweb.gb666.net";

	public void mainTest() {
		this.dologin();
		this.dobet(gameid);

	}

	public void dologin() {
		try {
			String postUrl = baseUrl + "/Home/login";
			String postData = "username=a02&password=8196658ecaeceb870d0ad3053dd579d2&validateCode=";
			// System.out.println(hcom.md5Secret("qwe123"));
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

	// 获取开盘的状态
	private int getOpStatus(String gameid) {
		String respget = hr.sendGet(baseUrl + "/Shared/GetNewPeriod?gameid=" + gameid, "");
		int fstatus = JsonPath.read(respget, "$.fstatus");
		System.out.println("fstatus为：" + fstatus);
		return fstatus;
	}

	// 获取期数信息
	private int getPeriodId(String gameid) {
		String respget = hr.sendGet(baseUrl + "/Shared/GetNewPeriod?gameid=" + gameid, "");
		int periodid = JsonPath.read(respget, "$.fid");
		System.out.println("fid为：" + periodid);
		return periodid;
	}

	public void dobet(String gameid) {
		try {
			ExcelReader excel = new ExcelReader("data/OfficeBetCase.xls");
			long sequence = System.currentTimeMillis();
			for (int i = 1; i < (excel.rows) / 2; i++) {
				int n = common.getRandom(i, excel.rows);
				long currentTime = System.currentTimeMillis();
				List<String> list = excel.readLine(n);
				gameid = list.get(1);
				period = this.getPeriodId(gameid);
				fstatus = this.getOpStatus(gameid);
				System.out.println(list);

				if (fstatus == 1) {
					String postUrl = baseUrl + "/OfficialAddOrders/AddOrders";
					String[] ball = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10" };
					int a = common.getRandom(0, 10);
					int b = common.getRandom(0, 10);
					int c = common.getRandom(0, 10);
					int d = common.getRandom(0, 10);
					int e = common.getRandom(0, 10);
					int f = common.getRandom(0, 10);
					int g = common.getRandom(0, 10);
					int h = common.getRandom(0, 10);
					int j = common.getRandom(0, 10);
					int k = common.getRandom(0, 10);
					String orderList = "";
					if (list.get(4).equals("猜前十单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e] + ","
								+ ball[f] + "," + ball[g] + "," + ball[h] + "," + ball[f] + "," + ball[k];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前十复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e] + "|"
								+ ball[f] + "|" + ball[g] + "|" + ball[h] + "|" + ball[f] + "|" + ball[k];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前九复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e] + "|"
								+ ball[f] + "|" + ball[g] + "|" + ball[h] + "|" + ball[f];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前九单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e] + ","
								+ ball[f] + "," + ball[g] + "," + ball[h] + "," + ball[f];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前八复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e] + "|"
								+ ball[f] + "|" + ball[g] + "|" + ball[h];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前八单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e] + ","
								+ ball[f] + "," + ball[g] + "," + ball[h];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前七复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e] + "|"
								+ ball[f] + "|" + ball[g];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前七单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e] + ","
								+ ball[f] + "," + ball[g];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前六复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e] + "|"
								+ ball[f];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前六单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e] + ","
								+ ball[f];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前五复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d] + "|" + ball[e];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前五单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d] + "," + ball[e];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前四复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c] + "|" + ball[d];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前四单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c] + "," + ball[d];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前三复式")) {
						String content = ball[a] + "|" + ball[b] + "|" + ball[c];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前三单式")) {
						String content = ball[a] + "," + ball[b] + "," + ball[c];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前二复式")) {
						String content = ball[a] + "|" + ball[b];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜前二单式")) {
						String content = ball[a] + "," + ball[b];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else if (list.get(4).equals("猜冠军")) {
						String content = ball[a];
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + content
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					} else {
						orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + list.get(6)
								+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":" + 2 + ",\"ts\":" + currentTime + "}]";
					}
					System.out.println("orderList:" + orderList);
					String datalist = URLEncoder.encode(orderList);
					// System.err.println(datalist);
					String postparam1 = "gameId=" + gameid + "&periodId=" + period
							+ "&isSingle=false&canAdvance=false&orderList=" + datalist + "";

					// System.out.println(postparam1);
					String resplogin = hr.sendPost(postUrl, postparam1);
					common.sleepRandom(2, 7);
					String loginfo = JsonPath.read(resplogin, "$.info");
					System.out.println(loginfo);
					if (loginfo.contains("投注成功！")) {
						System.out.println("接口投注成功……");
					} else {
						System.out.println("接口投注失败……");
					}
				} else {
					System.err.println("游戏未开盘……");
					this.dobet(gameid);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
