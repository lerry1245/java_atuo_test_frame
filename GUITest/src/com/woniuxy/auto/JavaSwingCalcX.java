package com.woniuxy.auto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

//通过导入的包我们可以看出Swing的应用程序任然会使用到AWT的对象

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JavaSwingCalcX extends JFrame {

	//定义界面上的元素控件并完成初始化操作(定义成员变量)
	private JPanel jPanel = new JPanel();   //定义窗口容器
	private JLabel jLable1 = new JLabel("运算数一：");  //定义提示标签
	private JLabel jLable2 = new JLabel("运算数二：");
	private JLabel jLable3 = new JLabel("运算类型：");
	private JLabel jLable4 = new JLabel("计算结果：");
	private JButton btnCalc = new JButton("计算");  //定义计算按钮
	private JButton btnClose = new JButton("关闭");  //定义关闭按钮
	
	//定义运算数一和运算数据二
	private JTextField txtNumberX = new JTextField("1");  
	private JTextField txtNumberY = new JTextField("2"); 
	
	//定义下拉框运算类型：加法、减法、乘法、除法
	private JComboBox<String> calcType = new JComboBox<String>();
	private JLabel lb1Result = new JLabel("结果在此处显示");	
	
	
	
	//程序的主入口方法，主要定义窗口
	public static void main(String[] args) {
		
		JavaSwingCalcX calc = new JavaSwingCalcX();
		calc.windowInit(); //初始化窗口设置
		calc.buttonInvoke();
	}
	
	//窗口程序 初始化操作，设定元素的大小，位置，初始值等。
	public void windowInit() {
		//定义窗口应用程序的基本属性
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(1260, 840);
		this.setTitle("JavaSwing演示程序");
		
		//定义各个控件的基本属性，大小和相对于住窗口的位置
		jLable1.setSize(new Dimension(340,60));
		jLable1.setFont(new Font("DiaLog",Font.BOLD,24));
		jLable1.setLocation(new Point(174,120));
		
		jLable2.setSize(new Dimension(240,60));
		jLable2.setFont(new Font("DiaLog",Font.BOLD,24));
		jLable2.setLocation(new Point(174,222));
		
		jLable3.setSize(new Dimension(240,60));
		jLable3.setFont(new Font("DiaLog",Font.BOLD,24));
		jLable3.setLocation(new Point(174,324));
		
		jLable4.setSize(new Dimension(240,60));
		jLable4.setFont(new Font("DiaLog",Font.BOLD,24));
		jLable4.setLocation(new Point(174,444));
		
		//设置字体大小
		lb1Result.setFont(new Font("DiaLog",Font.BOLD,24));
		btnCalc.setFont(new Font("DiaLog",Font.BOLD,24));
		btnClose.setFont(new Font("DiaLog",Font.BOLD,24));
		txtNumberX.setFont(new Font("DiaLog",Font.BOLD,24));
		txtNumberY.setFont(new Font("DiaLog",Font.BOLD,24));
		
		txtNumberX.setLocation(new Point(486,123));
		txtNumberX.setName("txtNumberX");
		txtNumberX.setSize(new Dimension(480,63));
		txtNumberY.setLocation(new Point(486,213));
		txtNumberY.setName("txtNumberY");
		txtNumberY.setSize(new Dimension(480,63));
		
		//为下拉框添加四个运算类型
		calcType.addItem("加法");
		calcType.setFont(new Font("DiaLog",Font.BOLD,20));
		calcType.addItem("减法");
		calcType.addItem("乘法");
		calcType.addItem("除法");
		calcType.setLocation(new Point(486,324));
		calcType.setName("calcType");
		calcType.setSize(new Dimension(480,63));
		lb1Result.setSize(new Dimension(450,60));
		lb1Result.setName("Lb1Result");
		lb1Result.setBackground(Color.white);
		lb1Result.setLocation(new Point(486,444));
		
		btnCalc.setLocation(new Point(273,570));
		btnCalc.setName("btnCalc");
		btnCalc.setSize(new Dimension(273,63));
		
		btnClose.setLocation(new Point(630,570));
		btnClose.setName("btnClose");
		btnClose.setSize(new Dimension(240,63));
		
		//将上述各种控件依次添加到串口容器中
		jPanel.setLayout(null);
		jPanel.add(jLable1);
		jPanel.add(jLable2);
		jPanel.add(jLable3);
		jPanel.add(jLable4);
		jPanel.add(txtNumberX);
		jPanel.add(txtNumberY);
		jPanel.add(lb1Result);
		jPanel.add(btnCalc);
		jPanel.add(btnClose);
		jPanel.add(calcType);
		
		this.setContentPane(jPanel);
	}
	
	public void buttonInvoke() {
		//绑定btnClose按钮的单击事件，并完成关闭应用程序的操作
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doClose();
			}
		});
		
		btnCalc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取到两个运算数的值并转换为doble类型
				double numberX = Double.parseDouble(txtNumberX.getText());
				double numberY = Double.parseDouble(txtNumberY.getText());
				//调用docalc并获取到运算结果，显示lb1Result控件中
				double result = doCalc(numberX,numberY);
				lb1Result.setText(String.valueOf(result));			
			}
		});	
	}
	
	//实现窗口的关闭操作
	public void doClose() {
		System.exit(1);	
	}
	
	//实现计算功能
	public double doCalc(double x, double y) {
		double result = 0;
		if(calcType.getSelectedItem().equals("加法")) {
			result = x + y;
		}
		else if(calcType.getSelectedItem().equals("减法")) {
			result = x - y;
		}
		else if(calcType.getSelectedItem().equals("乘法")) {
			result = x * y;
		}
		else {
			result = x / y;
		}
		return result;	
	}
}
