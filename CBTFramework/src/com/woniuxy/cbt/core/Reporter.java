package com.woniuxy.cbt.core;

import java.awt.Dimension;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.NeedsLocalLogs;

import com.sun.mail.handlers.image_gif;

import net.sourceforge.htmlunit.corejs.javascript.ast.NewExpression;


public class Reporter {
	
	SeleniumDriver selenium = new SeleniumDriver("ie");

	// 定义静态成员变量，用于统计当前测试的版本和模块
	// 下述成员变量的初始值只是一个参考值，在执行测试是会修改
	// 版本信息在整个执行过程开始是复制，一个测试周期值赋值一次
	// 模板信息按照ATM模型在每一个模型的prepare中赋值一次
	public static String version = "2.1.3";
	public static String module = "公共模块";

	// 定义某一次执行测试时，根据日期生成的报告和截图目录
	public static String folder = "20210119";

	// 定义测试脚本执行时间，结束时间减去开始时间即可（单位：毫秒）
	public static long duration = 100000;

	// 定义测试结果的统一表达，比如：成功，失败，错误，忽略
	// 这样可以避免随意定义测试结果，比如“成功”定义为“通过”
	public static String PASS = "成功";
	public static String FAIL = "失败";
	public static String ERROR = "错误";
	public static String IGNORE = "忽略";

	// 定义数据库连接字符信息
	private String driverClassName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/test?"
			+ "user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	// 定义静态域代码，当加载类Reporterd时便创建测试报告目录
	static {
		createFolder();
	}

	// 利用主方法对writeLog方法进行简单测试
	public static void main(String[] args) {
		Reporter rp = new Reporter();

		Reporter.version = "2.1.3";
		Reporter.module = "需求提案";
		String caseId = "Agileone_proposal_001";
		String caseDesc = "测试需求提案模块的新增功能-正常输入情况的检查";
		String result = Reporter.PASS;
		String error = "";
		String screenshot = "";
		rp.writelog(caseId, caseDesc, result, error, screenshot);

		String screenName = rp.captureScreen();
		System.out.println(screenName);
		rp.generateReport();

	}

	// 定义测试报告的主执行程序
	public void writelog(String caseId, String caseDesc, String result, String error, String screenshot) {

		// 删除数据库历史测试结果数据
		// this.clearlog();

		// 定义测试你脚本的执行时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String runtime = formatter.format(date.getTime());
		// 定义SQL语句
		String sql = "insert into report(version,module,caseid,casedesc,result,runtime,error,screenshot)value(?,?,?,?,?,?,?,?)";
		try {
			Connection conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, version);
			ps.setString(2, module);
			ps.setString(3, caseId);
			ps.setString(4, caseDesc);
			ps.setString(5, result);
			ps.setString(6, runtime);
			ps.setString(7, error);
			ps.setString(8, screenshot);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 清除历史日志
	public void clearlog() {
		// String sql = "delete from ptdata where 1= 1";
		String sql = "Truncate Table report";
		try {
			Connection conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.close(); // 用完之后关闭数据库连接
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("删除历史日志");
	}

	// 在当前项目的report目录下按照当日日期创建一个目录
	public static void createFolder() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String folder = formatter.format(date.getTime());

		String userDir = System.getProperty("user.dir");
		File file = new File(userDir + "\\report\\" + folder + "\\");
		if (!file.exists()) {
			file.mkdir();
			System.out.println("截图和报告文件夹创建成功.");
		} else {
			System.out.println("截图和报告文件夹已经存在.");
		}
		System.out.println("新创建的文件夹为：" + folder);
		Reporter.folder = folder;
	}

	// 当代码出错时，或者出现异常时，将现场截图
	public String captureScreen() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		String image = formatter.format(date.getTime()) + ".png";

		// 将截图文件统一保存到当前项目目录下的report目录中
		String userDir = System.getProperty("user.dir");
		String path = userDir + "\\report\\" + folder +"\\" + image;

		// 利用Webdriver实现截图，并返回其保存的路径
//		WebDriver driver = selenium.getWebDriver();
//		TakesScreenshot s = (TakesScreenshot)driver;
//		File file = s.getScreenshotAs(OutputType.FILE);
//		try {
//			FileUtils.copyFile(file, new File(path));
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}

		// 利用Java的Robot对象完成全屏截图
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		File file2 = new File(path);
		try {
			Robot robot = new Robot();
			// 表示截图区域为0,0的原点开始到屏幕的最高和最宽处，即全屏截图
			Rectangle rec = new Rectangle(0, 0, width, height);
			BufferedImage screen = robot.createScreenCapture(rec);
			ImageIO.write(screen, "png", file2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return image;
	}

	// 根据模板生成最终的HTML测试报告
	public void generateReport() {
		String template = this.readHtmlTemplate();

		// 获取执行版本，用于查询条件和代理模板变量$test-version
		String testVersion = Reporter.version;
		template = template.replace("$test-version", version);

		// 获取执行时间，并将其转换为分钟计算，代理模板变量$test-duration
		float duration = Reporter.duration / 60000f;
		// 设置保留两小数
		DecimalFormat df = new DecimalFormat("#.##");
		String testDuration = String.valueOf(df.format(duration));
		template = template.replace("$test-duration", testDuration);

		// 代替测试的执行时间，即模版变量$test-date
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String testDate = formatter.format(date.getTime());
		template = template.replace("$test-date", testDate);

		// 定义四个整型变量用于统计本次执行的成功，失败，错误和忽略几种类型的变量
		int passCount = 0, failCount = 0;
		int errorCount = 0, ignoreCount = 0;
		String sql = "select * from report where version=? order by id";
		try {
			Connection conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, version);
			ResultSet rs = ps.executeQuery();
			StringBuffer testResult = new StringBuffer();
			while (rs.next()) {
				// 定义temp变量，用于生成表格的每一行测试结果
				String temp = "<tr height='40'>\r\n";

				// 将temp变量的内容根据数据库中的数据进行连接
				temp += "<td with='7%'>" + rs.getInt("id") + "</td>\r\n";
				temp += "<td with='10%'>" + rs.getString("module") + "</td>\r\n";
				temp += "<td with='7%'>" + rs.getString("caseid") + "</td>\r\n";
				temp += "<td with='24%'>" + rs.getString("casedesc") + "</td>\r\n";

				// 根据数据库中得到的result字段的值，进行不同的处理，包括统计数量和显示不同背景景色
				if (rs.getString("result").contains("成功")) {
					passCount++;
					temp += "<td with='7%'  bgcolor='#61ff87'>" + rs.getString("result") + "</td>\r\n";
				} else if (rs.getString("result").contains("失败")) {
					failCount++;
					temp += "<td with='7%'  bgcolor='#FC5753'>" + rs.getString("result") + "</td>\r\n";
				} else if (rs.getString("result").contains("错误")) {
					failCount++;
					temp += "<td with='7%'  bgcolor='#FFA28A'>" + rs.getString("result") + "</td>\r\n";
				} else {
					failCount++;
					temp += "<td with='7%'  bgcolor='#00BEFF'>" + rs.getString("result") + "</td>\r\n";
				}

				temp += "<td with='15%'>" + rs.getString("runtime") + "</td>\r\n";
				temp += "<td with='18%'>" + rs.getString("error") + "</td>\r\n";
				temp += "<td with='12%'>";

				// 如果有错误消息产生，才会有截图，才生成截图的超链接
				if (rs.getString("error").length() > 0) {
					temp += "<a href='" + rs.getString("screenshot") + "'>查看截图</a>";
				}
				temp += "</td>\r\n";
				temp += "</tr>\r\n";

				// 将temp变量处理掉的每一行数据添加到testResult变量中
				testResult.append(temp);
			}

			// 对上述遍历的结果进行替换
			template = template.replace("$pass-count", String.valueOf(passCount));
			template = template.replace("$fail-count", String.valueOf(failCount));
			template = template.replace("$error-count", String.valueOf(errorCount));
			template = template.replace("$ignore-count", String.valueOf(ignoreCount));

			template = template.replace("$test-result", testResult.toString());

			// 正式将测试报告写入文件中，用以查看
			this.writeHtmlReport(template);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	// 读取模板文件内容
	public String readHtmlTemplate() {
		String content = "";
		String userDir = System.getProperty("user.dir");
		String fileName = userDir + "\\report\\template\\default.html";
		File file = new File(fileName);
		try {
			InputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			while ((line = br.readLine()) != null) {
				content += line + "\r\n";
			}
			in.close();
			reader.close();
			br.close();
		} catch (Exception e) {
			System.out.println("读取测试报告模板失败：" + e.getMessage());
		}
		return content;
	}

	// 写入测试报告内容
	private void writeHtmlReport(String content) {
		String userDir = System.getProperty("user.dir");
		String folder = Reporter.folder;
		Date date = new Date();
		// 生成一个HTML测试报告的文件路劲和文件名
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		String time = formatter.format(date.getTime());
		String name = "\\testreport_" + time + ".html";

		String fileName = userDir + "\\report\\" + folder + name;
		File file = new File(fileName);
		try {
			OutputStream in = new FileOutputStream(file, false);
			OutputStreamWriter out = new OutputStreamWriter(in, "UTF-8");
			char[] ch = content.toCharArray();
			out.write(ch, 0, ch.length);
			out.flush();
			out.close();
			System.out.println("HTML测试报告生成成功.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HTML测试报告生成失败." + e.getMessage());
		}
	}

	// 建立与数据库之间的连接
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driverClassName).newInstance();
			// new com.mysql.jdbc.Driver(); 也可以直接这样实例化
			conn = DriverManager.getConnection(url); // 建立数据库连接
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
}
