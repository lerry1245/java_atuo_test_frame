package com.woniuxy.auto;

import java.awt.Component;
import java.awt.Frame;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JavaCalcTest {

	public static void main(String[] args) {
		JavaCalcTest  calcTest = new JavaCalcTest();
		calcTest.startAPP();
		calcTest.doTest();

	}
	public void doTest() {
		JFrame jf = this.getFrame("JavaSwing演示程序");
		JTextField numberx = (JTextField)this.getComponent(jf, "txtNumberX");
		numberx.setText("10");
		JTextField numbery = (JTextField)this.getComponent(jf, "txtNumberY");
		numberx.setText("10");
		
		JComboBox type = (JComboBox)	this.getComponent(jf, "calcType");	
		type.setSelectedItem("乘法");
		JButton btnCalc = (JButton)this.getComponent(jf, "btnCalc");
		btnCalc.doClick();
	}
	
	public void startAPP() {
		try {
			Class<?> calzz = Class.forName("com.woniuxy.auto.JavaSwingCalc");
			Method main = calzz.getMethod("main", String[].class);
			main.invoke(calzz, new String[1]);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JFrame getFrame(String title) {
		Frame[] frames = JFrame.getFrames();
		JFrame jf = null;
		for (int i=0; i<frames.length; i++) {
			if(frames[i].getTitle().equals(title)) {
				jf = (JFrame)frames[i];				
			}
		}
		return jf;
	}
	
	public Component getComponent(JFrame jf, String name) {
		Component[] controls = jf.getContentPane().getComponents();
		Component control = null;
		for(int i=0; i<controls.length; i++) {
			if(controls[i].getName() !=null &&
					controls[i].getName().equals(name)) {
				control = controls[i];
			}
		
	   }
		return control;
	}	
		
		
}
