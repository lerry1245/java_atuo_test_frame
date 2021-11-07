package com.woniuxy.auto;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;




public class RobotUsage {

	public static void main(String[] args) {
		//任何在命令行可以运行的命令，均可以由exec方法调用
		try {
			Runtime.getRuntime().exec("calc.exe");
			Thread.sleep(2000);
			
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_3);
			Thread.sleep(200);
			robot.keyPress(KeyEvent.VK_ADD);
			Thread.sleep(200);
			robot.keyPress(KeyEvent.VK_2);
			Thread.sleep(200);
			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
			
			//鼠标操作
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);  //左键单击
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			
			Thread.sleep(2000);
			
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);  //右键单击
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
			
			Thread.sleep(2000);
			//组合键Alt+F4关闭点前窗口
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_F4);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_F4);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
