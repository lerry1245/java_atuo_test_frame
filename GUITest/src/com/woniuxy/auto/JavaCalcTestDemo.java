package com.woniuxy.auto;

import java.awt.Component;
import java.awt.Frame;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JavaCalcTestDemo {

	public static void main(String[] args) {
		
//		JavaSwingCalc calc = new JavaSwingCalc();
//		calc.windowInit();
//		calc.buttonInvoke();
		
//		JavaSwingCalc.main(new String[0]);
		
		//获取JVM自己的环境变量(与操作系统的环境变量类似)
//		String userDir = System.getProperty("user.dir");
//		String jarFile = userDir + "\\lib\\JavaSwingCalc.jar";
//		try {
//			Runtime.getRuntime().exec("java -jar" + jarFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//利用反射机制完成运行
		
		try {
			Class<?> calzz = Class.forName("com.woniuxy.auto.JavaSwingCalc");
			Method main = calzz.getMethod("main", String[].class);
			main.invoke(calzz, new String[1]);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取当前Java的应用程序窗口
		Frame[] frames = JFrame.getFrames();
		for (int i=0; i<frames.length; i++) {
			//System.out.println(frames[i].getTitle());
			if(frames[i].getTitle().equals("JavaSwing演示程序")) {
				JFrame jf = (JFrame)frames[i];
				Component[] controls = jf.getContentPane().getComponents();
				for(int j=0; j<controls.length; j++) {
					//System.out.println(controls[j].getName());
					if(controls[j].getName() !=null && controls[j].getName().equals("txtNumberX")) {
						JTextField txtNumberX = (JTextField)controls[j];
						txtNumberX.setText("200");	
					}
					if(controls[j].getName() !=null && controls[j].getName().equals("btnCalc")) {
						JButton btnCalc = (JButton)controls[j];
						btnCalc.doClick();
					}
				}
				
			}
		}

	}

}
