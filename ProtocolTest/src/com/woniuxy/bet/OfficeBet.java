package com.woniuxy.bet;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.common.ExcelWriter;
import com.woniuxy.common.Common;
import com.woniuxy.common.DataDrivenOfInter;
import com.woniuxy.common.ExcelReader;

public class OfficeBet {

	// 把当前的执行时间加入到结果文件名中
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
	String createdate = sdf.format(date);
	// 打开Excel表进行读取
	ExcelReader excelR = new ExcelReader("data/OfficeBetCase.xls");
	// 复制用例中的内容到结果中，并执行之后的写入操作
	ExcelWriter excelW = new ExcelWriter("data/OfficeBetCase.xls", "result/Res-OfficeBetCase" + createdate + ".xls");

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
		this.doOfficebet(gameid);

	}

	public void dologin() {
		try {
			String postUrl = baseUrl + "/Home/login";
			String postData = "username=a01&password=8196658ecaeceb870d0ad3053dd579d2&validateCode=";
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
	private int[] getIfon(String gameid) {
		int[] b = new int[2];
		String respget = ddr.testGet(baseUrl + "/Shared/GetNewPeriod?gameid=" + gameid, "");
		int fstatus = JsonPath.read(respget, "$.fstatus");
		int periodid = JsonPath.read(respget, "$.fid");
		b[0] = fstatus;
		b[1] = periodid;
		System.out.println("fstatus为：" + fstatus);
		return b;
	}

	public void doOfficebet1(String gameid) {
		ExcelReader excel = new ExcelReader("data/OfficeBetCase.xls");
		long sequence = System.currentTimeMillis();
		for (int i = 1; i < excel.rows; i++) {
			int n = common.getRandom(1, excel.rows);

			long currentTime = System.currentTimeMillis();
			List<String> list = excel.readLine(n);
			gameid = list.get(1);
			int[] b = this.getIfon(gameid);
			fstatus = b[0];
			period = b[1];
			System.out.println(list);

			if (fstatus == 1) {
				String postUrl = baseUrl + "/OfficialAddOrders/AddOrders";
				String orderList = "[{\"i\":" + list.get(5) + ",\"c\":\"" + list.get(6)
						+ "\",\"n\":1,\"t\":1,\"k\":0,\"m\":1,\"a\":2,\"ts\":" + currentTime + "}]";
				// System.out.println("orderList:" + orderList);
				String datalist = URLEncoder.encode(orderList);
				// System.out.println(datalist);
				String postparam = "gameId=" + gameid + "&periodId=" + period
						+ "&isSingle=false&canAdvance=false&orderList=" + datalist + "";

				System.out.println(postparam);
				String resp = ddr.testPost(postUrl, postparam);
				common.sleepRandom(2, 4);
				String betinfo = JsonPath.read(resp, "$.info");
				ddr.assertSame("$.info", "投注成功！");
				if (betinfo.contains("投注成功！")) {
					System.out.println("接口投注成功……");

				} else {
					System.out.println("接口投注失败……");

				}
			} else {
				System.err.println("游戏未开盘……");
				this.doOfficebet1(gameid);
			}
		}
	}

	public void doOfficebet(String gameid) {
		try {
			ExcelReader excel = new ExcelReader("data/OfficeBetCase.xls");
			long sequence = System.currentTimeMillis();
			for (int i = 1; i < excel.rows; i++) {
				int n = common.getRandom(i, excel.rows);
				long currentTime = System.currentTimeMillis();
				List<String> list = excel.readLine(n);
				gameid = list.get(1);
				int[] info = this.getIfon(gameid);
				fstatus = info[0];
				period = info[1];
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
					String resplogin = ddr.testPost(postUrl, postparam1);
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
					this.doOfficebet(gameid);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
