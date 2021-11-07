package com.woniuxy.auto;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

import java.awt.Toolkit;


public class MonkeyTest {
	
	private Robot robot = null;

	public static void main(String[] args) {
		MonkeyTest monkey = new MonkeyTest();
		try {
			monkey.robot = new Robot();
			Runtime.getRuntime().exec("calc.exe");
			Thread.sleep(3000);
			
			for(int i=0; i<30; i++) {
				monkey.randomMove();
				Thread.sleep(200);
				monkey.randomMouse();
				Thread.sleep(200);
				monkey.randomKey();
				Thread.sleep(200);
			}
			Thread.sleep(2000);
			//缁勫悎閿瓵lt+F4鍏抽棴鐐瑰墠绐楀彛
			monkey.robot.keyPress(KeyEvent.VK_ALT);
			monkey.robot.keyPress(KeyEvent.VK_F4);
			monkey.robot.keyRelease(KeyEvent.VK_ALT);
			monkey.robot.keyRelease(KeyEvent.VK_F4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	//闅忔満鏁板瓧閿�
	public void randomKey() {
		//10涓暟瀛楅敭瀵瑰簲鐨凨eyEvent鍊�
		int[] keys = {0X31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
		int index = (int)(Math.random()*keys.length);
		robot.keyPress(keys[index]);
		robot.keyRelease(keys[index]);
	}
	//闅忔満榧犳爣宸﹀彸閿崟鍑�
	public void randomMouse() {
		int random = (int)(Math.random()*10);
		if(random >= 5) {
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);  
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
		else {
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);  
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}
	}
	
	public void randomMove() {
		//鑷姩鑾峰彇灞忓箷澶у皬
		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		int screenwidth = (int)screenSize.width;
		int screenheight = (int)screenSize.height;
		
		//闅忔満灞忓箷鐨勭偣
		Random myrand = new Random();
		int x = myrand.nextInt(screenwidth);
		int y = myrand.nextInt(screenheight-100);
		robot.mouseMove(x, y);
	}

}
