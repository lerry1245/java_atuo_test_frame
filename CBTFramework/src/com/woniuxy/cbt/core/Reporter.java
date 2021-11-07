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

	// ���徲̬��Ա����������ͳ�Ƶ�ǰ���Եİ汾��ģ��
	// ������Ա�����ĳ�ʼֵֻ��һ���ο�ֵ����ִ�в����ǻ��޸�
	// �汾��Ϣ������ִ�й��̿�ʼ�Ǹ��ƣ�һ����������ֵ��ֵһ��
	// ģ����Ϣ����ATMģ����ÿһ��ģ�͵�prepare�и�ֵһ��
	public static String version = "2.1.3";
	public static String module = "����ģ��";

	// ����ĳһ��ִ�в���ʱ�������������ɵı���ͽ�ͼĿ¼
	public static String folder = "20210119";

	// ������Խű�ִ��ʱ�䣬����ʱ���ȥ��ʼʱ�伴�ɣ���λ�����룩
	public static long duration = 100000;

	// ������Խ����ͳһ�����磺�ɹ���ʧ�ܣ����󣬺���
	// �������Ա������ⶨ����Խ�������硰�ɹ�������Ϊ��ͨ����
	public static String PASS = "�ɹ�";
	public static String FAIL = "ʧ��";
	public static String ERROR = "����";
	public static String IGNORE = "����";

	// �������ݿ������ַ���Ϣ
	private String driverClassName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/test?"
			+ "user=root&password=&useUnicode=true&autoReconnect=true&useSSL=false&characterEncoding=utf-8";

	// ���徲̬����룬��������Reporterdʱ�㴴�����Ա���Ŀ¼
	static {
		createFolder();
	}

	// ������������writeLog�������м򵥲���
	public static void main(String[] args) {
		Reporter rp = new Reporter();

		Reporter.version = "2.1.3";
		Reporter.module = "�����᰸";
		String caseId = "Agileone_proposal_001";
		String caseDesc = "���������᰸ģ�����������-������������ļ��";
		String result = Reporter.PASS;
		String error = "";
		String screenshot = "";
		rp.writelog(caseId, caseDesc, result, error, screenshot);

		String screenName = rp.captureScreen();
		System.out.println(screenName);
		rp.generateReport();

	}

	// ������Ա������ִ�г���
	public void writelog(String caseId, String caseDesc, String result, String error, String screenshot) {

		// ɾ�����ݿ���ʷ���Խ������
		// this.clearlog();

		// ���������ű���ִ��ʱ��
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String runtime = formatter.format(date.getTime());
		// ����SQL���
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

	// �����ʷ��־
	public void clearlog() {
		// String sql = "delete from ptdata where 1= 1";
		String sql = "Truncate Table report";
		try {
			Connection conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.close(); // ����֮��ر����ݿ�����
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("ɾ����ʷ��־");
	}

	// �ڵ�ǰ��Ŀ��reportĿ¼�°��յ������ڴ���һ��Ŀ¼
	public static void createFolder() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String folder = formatter.format(date.getTime());

		String userDir = System.getProperty("user.dir");
		File file = new File(userDir + "\\report\\" + folder + "\\");
		if (!file.exists()) {
			file.mkdir();
			System.out.println("��ͼ�ͱ����ļ��д����ɹ�.");
		} else {
			System.out.println("��ͼ�ͱ����ļ����Ѿ�����.");
		}
		System.out.println("�´������ļ���Ϊ��" + folder);
		Reporter.folder = folder;
	}

	// ���������ʱ�����߳����쳣ʱ�����ֳ���ͼ
	public String captureScreen() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		String image = formatter.format(date.getTime()) + ".png";

		// ����ͼ�ļ�ͳһ���浽��ǰ��ĿĿ¼�µ�reportĿ¼��
		String userDir = System.getProperty("user.dir");
		String path = userDir + "\\report\\" + folder +"\\" + image;

		// ����Webdriverʵ�ֽ�ͼ���������䱣���·��
//		WebDriver driver = selenium.getWebDriver();
//		TakesScreenshot s = (TakesScreenshot)driver;
//		File file = s.getScreenshotAs(OutputType.FILE);
//		try {
//			FileUtils.copyFile(file, new File(path));
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}

		// ����Java��Robot�������ȫ����ͼ
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		File file2 = new File(path);
		try {
			Robot robot = new Robot();
			// ��ʾ��ͼ����Ϊ0,0��ԭ�㿪ʼ����Ļ����ߺ��������ȫ����ͼ
			Rectangle rec = new Rectangle(0, 0, width, height);
			BufferedImage screen = robot.createScreenCapture(rec);
			ImageIO.write(screen, "png", file2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return image;
	}

	// ����ģ���������յ�HTML���Ա���
	public void generateReport() {
		String template = this.readHtmlTemplate();

		// ��ȡִ�а汾�����ڲ�ѯ�����ʹ���ģ�����$test-version
		String testVersion = Reporter.version;
		template = template.replace("$test-version", version);

		// ��ȡִ��ʱ�䣬������ת��Ϊ���Ӽ��㣬����ģ�����$test-duration
		float duration = Reporter.duration / 60000f;
		// ���ñ�����С��
		DecimalFormat df = new DecimalFormat("#.##");
		String testDuration = String.valueOf(df.format(duration));
		template = template.replace("$test-duration", testDuration);

		// ������Ե�ִ��ʱ�䣬��ģ�����$test-date
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String testDate = formatter.format(date.getTime());
		template = template.replace("$test-date", testDate);

		// �����ĸ����ͱ�������ͳ�Ʊ���ִ�еĳɹ���ʧ�ܣ�����ͺ��Լ������͵ı���
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
				// ����temp�������������ɱ���ÿһ�в��Խ��
				String temp = "<tr height='40'>\r\n";

				// ��temp���������ݸ������ݿ��е����ݽ�������
				temp += "<td with='7%'>" + rs.getInt("id") + "</td>\r\n";
				temp += "<td with='10%'>" + rs.getString("module") + "</td>\r\n";
				temp += "<td with='7%'>" + rs.getString("caseid") + "</td>\r\n";
				temp += "<td with='24%'>" + rs.getString("casedesc") + "</td>\r\n";

				// �������ݿ��еõ���result�ֶε�ֵ�����в�ͬ�Ĵ�������ͳ����������ʾ��ͬ������ɫ
				if (rs.getString("result").contains("�ɹ�")) {
					passCount++;
					temp += "<td with='7%'  bgcolor='#61ff87'>" + rs.getString("result") + "</td>\r\n";
				} else if (rs.getString("result").contains("ʧ��")) {
					failCount++;
					temp += "<td with='7%'  bgcolor='#FC5753'>" + rs.getString("result") + "</td>\r\n";
				} else if (rs.getString("result").contains("����")) {
					failCount++;
					temp += "<td with='7%'  bgcolor='#FFA28A'>" + rs.getString("result") + "</td>\r\n";
				} else {
					failCount++;
					temp += "<td with='7%'  bgcolor='#00BEFF'>" + rs.getString("result") + "</td>\r\n";
				}

				temp += "<td with='15%'>" + rs.getString("runtime") + "</td>\r\n";
				temp += "<td with='18%'>" + rs.getString("error") + "</td>\r\n";
				temp += "<td with='12%'>";

				// ����д�����Ϣ�������Ż��н�ͼ�������ɽ�ͼ�ĳ�����
				if (rs.getString("error").length() > 0) {
					temp += "<a href='" + rs.getString("screenshot") + "'>�鿴��ͼ</a>";
				}
				temp += "</td>\r\n";
				temp += "</tr>\r\n";

				// ��temp�����������ÿһ��������ӵ�testResult������
				testResult.append(temp);
			}

			// �����������Ľ�������滻
			template = template.replace("$pass-count", String.valueOf(passCount));
			template = template.replace("$fail-count", String.valueOf(failCount));
			template = template.replace("$error-count", String.valueOf(errorCount));
			template = template.replace("$ignore-count", String.valueOf(ignoreCount));

			template = template.replace("$test-result", testResult.toString());

			// ��ʽ�����Ա���д���ļ��У����Բ鿴
			this.writeHtmlReport(template);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	// ��ȡģ���ļ�����
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
			System.out.println("��ȡ���Ա���ģ��ʧ�ܣ�" + e.getMessage());
		}
		return content;
	}

	// д����Ա�������
	private void writeHtmlReport(String content) {
		String userDir = System.getProperty("user.dir");
		String folder = Reporter.folder;
		Date date = new Date();
		// ����һ��HTML���Ա�����ļ�·�����ļ���
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
			System.out.println("HTML���Ա������ɳɹ�.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HTML���Ա�������ʧ��." + e.getMessage());
		}
	}

	// ���������ݿ�֮�������
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driverClassName).newInstance();
			// new com.mysql.jdbc.Driver(); Ҳ����ֱ������ʵ����
			conn = DriverManager.getConnection(url); // �������ݿ�����
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
}
