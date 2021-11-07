package com.woniuxy.http;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.woniuxy.socket.FeilUpDownload;

public class WoniuxycomTest {
	HttpRequest  hr = new HttpRequest();
	FeilUpDownload fud = new FeilUpDownload();

	public static void main(String[] args) {
		WoniuxycomTest wt = new WoniuxycomTest();
		wt.testLinks();
//		wt.downAllImages();
//		wt.lagou();
		wt.lagoujodDsc();
         
	}
	//测试网页中的所有超链接是否有效
	public void testLinks() {
		//1.通过一定方法找到所有的超链接
		//2.对所有的超链接发送get请求，并验证其响应，甚至验证响应码的内容
		String baseUrl = "http://www.woniuxy.com";
		String response = hr.sendGet(baseUrl+"/", "");		
		List<String> links = this.getItems(response, "(<a href=\")(.+?)(\")");
		List<String> newLinks = new ArrayList<String>();
		//过滤掉不符的URL
		for(int i=0; i<links.size();i++) {
			if(links.get(i).startsWith("/")) {
				newLinks.add(baseUrl + links.get(i));
			}
			else if(links.get(i).startsWith("http")) {
				newLinks.add(links.get(i));
			}
			else{
				//DoNothing
			}
		}
		for(int i=0; i<newLinks.size();i++) {
			int responseCode = hr.getResponseCode(newLinks.get(i));
			if(responseCode < 400){
				System.out.println("地址" + newLinks.get(i)+ "访问正常" );
			}
			else {
				System.out.println("地址" + newLinks.get(i)+ "访问不正常" );
			}
		}
	}
	
	//测试获取网页中的图片地址（蜗牛学院）
	public void  downAllImages() {
		String baseUrl = "http://www.woniuxy.com";
		String response = hr.sendGet(baseUrl+"/", "");	
		List<String> imageSrc = this.getItems(response, "(<img src=\")(.+?)(\")");
		for(int i=0; i<imageSrc.size();i++) {
			String imageUrl = baseUrl + "/" + imageSrc.get(i);
			fud.doDownload(imageUrl, "E:\\TestUD");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	//测试获取拉钩网的职位描述,并写入到本地文档中
	public void lagoujodDsc() {
		String response = hr.sendGet("https://www.lagou.com/zhaopin/ceshigongchengshi/2/", "");
		//System.out.println(response);
		List<String> positionLinks = this.getItems(response, "(position_link\" href=\")(.+?)(\")");
		for(int i=0; i<positionLinks.size()-1;i++) {
			//System.out.println(positionLinks.get(i));
			System.out.println("***************正在获取第"+(i+1)+"个职位详情******************");
			String respPosition = hr.sendGet(positionLinks.get(i), "");
			String jobDetails = this.getLines(respPosition, "(职位描述：</h3>)(.+?)(</div>)");
			jobDetails =jobDetails.replaceAll("<.+>", "");
			System.out.println(jobDetails);
			try {
				File file = new File("E:\\test.txt");
				// FileOutputStream的构造方法第二个参数为true，表示文件内容后追加内容
				OutputStream out = new FileOutputStream(file, true);
				OutputStreamWriter writer = new OutputStreamWriter(out, "GBK");
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write(jobDetails, 0, jobDetails.length());
				bw.flush(); // 清空缓存
				bw.close();
				writer.close(); // 关闭输入流
				out.close();
				System.out.println("写入成功");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("写入失败");
			}
		}
	}
	
	public void lagouLines() {
		String response = hr.sendGet("https://www.lagou.com/jobs/7500263.html?show=220f4e3c60f94f07b9b3329714a063f0", "");
		List<String> lines= this.getItems(response, "(任职要求)(.+?)(<</div>)");
//		for(int i=0; i<lines.size();i++) {			
//			System.out.println(lines.get(i));
//		}
	}
	
	// 获取发帖页面的动态验证码
		public String getItem(String response, String regex){
			String verifyCode = "";
			Pattern p = Pattern.compile(regex,Pattern.DOTALL);
			Matcher m = p.matcher(response);
			if (m.find()) {
				verifyCode = m.group(2);
			}
			return verifyCode;
		}

		// 通过左右边界查找多条记录
		public List<String> getItems(String response, String regex) {
			List<String> items = new ArrayList<String>();
			Pattern p = Pattern.compile(regex,Pattern.DOTALL);
			Matcher m = p.matcher(response);
			while (m.find()) {
				items.add(m.group(2));
				//System.out.println(m.group(2));
			}
			return items;
		}
		//匹配多行正则表达式
		public String getLines(String response, String regex) {
			String lines = "";
			Pattern p = Pattern.compile(regex,Pattern.DOTALL);
			Matcher m = p.matcher(response);
			while (m.find()) {
				lines = (m.group(2));
				//System.out.println(m.group(2));
			}
			return lines;
			
		}
}
