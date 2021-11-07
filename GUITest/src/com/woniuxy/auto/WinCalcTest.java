package com.woniuxy.auto;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;

public class WinCalcTest {
	
	//瀹氫箟windows娑堟伅锛屽搴旂偣鍑诲拰鍏抽棴锛屼互16杩涘埗琛ㄧず鍏跺��
	private static int BM_CLICK = 0x00F5;
	private static int WM_CLOSE = 0x0010;
	
	public static void main(String[] args) {
		WinCalcTest calc = new WinCalcTest();
		calc.calcTest();
		//杩愯CMD绐楀彛
//		try {
//			Runtime runtime  = Runtime.getRuntime();
//			Process process = runtime.exec("ping www.baidu.com");
//			InputStream is = process.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is,"GBK");
//			BufferedReader br = new BufferedReader(isr);
//			String line = "", body = "";
//			while((line = br.readLine()) != null) {
//				body += line + "\n";
//			}
//			System.out.println("body");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
				
	}
	public void calcTest() {
		try {
			this.runApp("calc.exe");
			Thread.sleep(2000);
			//璇嗗埆鍜屾搷浣滆绠楀櫒绐楀彛鍙婇噷闈㈢殑鍚勪釜鍏冪礌
			int parentHandle = this.findWindow("SciCalc", "Calculator");
			int window = 0, control = 0;   // 瀹氫箟鎸夐挳鐨勫彞鏌�
			window = this.findWindow("SciCalc", "Calculator");
			
			control = this.findWindowEX(window, "Button", "3");
			this.sendMessage(control, BM_CLICK);
			Thread.sleep(1000);
			control = this.findWindowEX(window, "Button", "+");
			this.sendMessage(control, BM_CLICK);
			Thread.sleep(1000);
			control = this.findWindowEX(window, "Button", "2");
			this.sendMessage(control, BM_CLICK);
			Thread.sleep(1000);
			control = this.findWindowEX(window, "Button", "=");
			this.sendMessage(control, BM_CLICK);
			
			//鎸夌粍鍚堥敭Ctrl+C灏嗙粨鏋滃垎鏀埌鍓垏鏉�
			this.keyCopy();
			
			//浠庡壀鍒囨澘涓彇鍑哄垰鎵嶅鍒剁殑鏁版嵁锛屼笌鏈熸湜缁撴灉杩涜姣旇緝锛屽凡瀹炵幇鏂█
			if(this.getClipboardText().equals("5")) {
				System.out.println("娴嬭瘯鎴愬姛.");
			}
			else {
				System.out.println("娴嬭瘯澶辫触");
			}
			this.sendMessage(parentHandle, WM_CLOSE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public int findWindow(String windowClass, String windowCaption) throws Exception {
		//鍔犺浇user32.dll鏂囦欢锛屽悓鏃跺埗瀹氳璋冪敤鐨勫嚱鏁癋indWindowA.
		//姝ゅ鍚庣紑蹇呴』鍔燗锛屽惁鍒橨native鏃犳硶瀹氫綅鍒癋indWindow锛屼笅鍚�
			JNative jnative = new JNative("user32.dll","FindwindowA");
			jnative.setRetVal(Type.INT);   // 璁剧疆鍏惰繑鍥炲�肩被鍨�
			jnative.setParameter(0, Type.STRING, windowClass);
			jnative.setParameter(1, Type.STRING,windowCaption);
			jnative.invoke();
			return Integer.parseInt(jnative.getRetVal()); //杩斿洖鎵惧埌鐨勫厓绱犵殑	
	}
	
	//鏄犲皠ueser32鐨凢indWindowEX鍑芥暟锛屽搴旂殑鍙傛暟涓�
	//1.parentHandle:鎺т欢鍏冪礌鎵�鍦ㄧ殑鐖跺彞鏌�
	//2.controlClass:鎺у埗鍏冪礌鐨勫搴旂被鍨�
	//3.controlCaption: 鎺у埗鍏冪礌瀵瑰簲鐨勬爣棰�
	public int findWindowEX(int parentHandle, String controlClass, String controlCaption) throws Exception {
		JNative jnative = new JNative("user32.dll","FindwindowA");
		jnative.setRetVal(Type.INT); 
		jnative.setParameter(0, Type.STRING, String.valueOf(parentHandle));
		jnative.setParameter(1, Type.INT, "0");
		jnative.setParameter(2, Type.STRING,controlClass);
		jnative.setParameter(3, Type.STRING,controlCaption);
		jnative.invoke();
		return Integer.parseInt(jnative.getRetVal()); //杩斿洖鎵惧埌鐨勫厓绱犵殑				
	}
	
	//澶勭悊Sendmessage鍑芥暟
	public void sendMessage(int parentHandle, int message) throws Exception {
		JNative jnative = new JNative("user32.dll","FindwindowA");
		jnative.setRetVal(Type.VOID); 
		jnative.setParameter(0, Type.STRING, String.valueOf(parentHandle));
		jnative.setParameter(1, Type.INT, String.valueOf(message));
		jnative.setParameter(2, Type.STRING,"0");
		jnative.setParameter(3, Type.STRING,"0");
		jnative.invoke();
		Thread.sleep(1000);
	}
	
	public void runApp(String processName) throws Exception {
		//杩愯DOS鍛戒护tasklist鏉ヨ幏鍙栬繘绋嬭〃
		Process proc = Runtime.getRuntime().exec("tasklist");
		InputStreamReader isr = new InputStreamReader(proc.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String line ="";
		boolean isOpened = false;
		while((line = br.readLine()) != null) {
			//濡傛灉鎵惧埌浜嗚繘绋嬪悕宸茬粡瀛樺湪浜庡垪琛ㄤ腑锛岃〃鏄庣▼搴忓凡缁忚繍琛�
			if(line.indexOf(processName) >= 0) {
				isOpened = true;
				break;
			}
		}
		if(!isOpened) {
			//杩愯绯荤粺鑷甫鐨勮绠楀櫒绋嬪簭
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(processName);
			Thread.sleep(2000);
		}
	}
	//璇诲彇鎿嶄綔绯荤粺鐨勫壀鍒囨澘鍐呭锛屼互鑾峰彇鍒拌绠楀櫒鐨勮繍绠楃粨鏋�
	public String getClipboardText() {
		String content = "";
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		//鑾峰彇鍓垏鏉夸腑鐨勫唴瀹�
		Transferable clipT = clip.getContents(null);
		if(clipT != null) {
			//妫�鏌ュ唴瀹规槸鍚︽槸鏂囨湰绫诲瀷锛屼篃鍙互鑾峰彇鍏朵粬绫诲瀷鐨勫壀鍒囨澘鍐呭
			if(clipT.isDataFlavorSupported(DataFlavor.stringFlavor));			
		}
		return content;
		
	}
	
	//鎸変笅Ctrl+C瀵硅绠楃粨鏋滆繘琛屽鍒�
	public void keyCopy() throws Exception {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_C);
		
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_C);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
