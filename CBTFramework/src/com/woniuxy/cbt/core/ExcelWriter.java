package com.woniuxy.cbt.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelWriter {
	/*
	 * 读取用例表内容，并复制用例表内容到结果表中完成结果表的创建，并实现向结果表中写入数据
	 * @method  excelWriter构造函数完成对结果表的创建 
	 * 			setStyle指定向结果表写入内容时的格式
	 * 			writeFailCell用例失败时以红色字体写入 
	 *			writeCell正常情况下以黑色字体写入单元格 
	 *			useSheet指定使用的sheet
	 *			writeLine整行写入内容 
	 * 			save保存写入的内容到excel文件中，完成操作后必须调用，否则excel文件无内容。
	 */

	// 用于存放结果表内容的xlsx格式的工作簿
	private XSSFWorkbook xssfWorkbook = null; 
	// 用于存放结果表内容的xls格式的工作簿
	private HSSFWorkbook hssfWorkbook = null;
	// 工作的sheet页
	private Sheet sheet;
	// 用于读取用例表内容复制到结果表的文件输出流
	private FileOutputStream stream = null;
	// 用于存储结果表的路径的成员变量，便于在保存结果时进行判断
	private String path = null;
	// 单元格格式
	private CellStyle style = null;
	// 表的总行数
	public int rows = 0;

	/*
	 * 根据用例表casePahth，创建结果表resultPath，将casePahth中的内容复制到resultPath中
	 * @param casePahth用例表路径 
	 * 		  resultPath结果表路径
	 */
	public ExcelWriter(String casePath, String resultPath) {
		// 截取用例表后缀名
		String Origintype = casePath.substring(casePath.indexOf("."));
		// 判断是xls还是xlsx格式，完成在内存中创建用例表的工作簿
		XSSFWorkbook xssfWorkbookRead = null;
		if (Origintype.equals(".xlsx")) {
			try {
				xssfWorkbookRead = new XSSFWorkbook(new File(casePath)); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		HSSFWorkbook hssfWorkbookRead = null;
		if (Origintype.equals(".xls")) {
			try {
				hssfWorkbookRead = new HSSFWorkbook(new FileInputStream(new File(casePath)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 如果两种格式均不符合，则文件打开失败
		if (xssfWorkbookRead == null && hssfWorkbookRead == null) {  
			System.out.println("Excel文件打开失败！");
			return;
		}

		// 截取结果表后缀名
		String resultType = resultPath.substring(resultPath.indexOf("."));
		// 确定结果表格式为excel格式
		if (resultType.equals(".xlsx") || resultType.equals(".xls")) {
			try {
				// 根据结果表的文件名，为该文件在内存中开辟空间
				File file = new File(resultPath);
				try {
					// 在磁盘上面创建该文件
					file.createNewFile();
				} catch (Exception e1) {
					// 创建失败，提示路径非法，并停止创建
					System.out.println("文件路径非法，请检查！");
					e1.printStackTrace();
					return;
				}
				// 基于结果表，创建文件输出流stream
				stream = new FileOutputStream(file);
				// 将用例表中的内容写入文件输出流stream
				if (hssfWorkbookRead != null) {
					hssfWorkbookRead.write(stream);
					// 关闭用例表在内存中的副本
					hssfWorkbookRead.close();
				} else {
					xssfWorkbookRead.write(stream);
					xssfWorkbookRead.close();
				}
				// 关闭已经写入了用例表内容的文件流
				stream.close();

				// 基于结果表，创建文件输入流(创建之后再读取结果表内容)
				FileInputStream in = new FileInputStream(file);
				// 判断结果文件的后缀是03版还是07版excel
				if (resultType.equals(".xlsx")) {
					try {
						//通过文件输入流，在内存中创建结果表的工作簿
						xssfWorkbook = new XSSFWorkbook(in);
						sheet = xssfWorkbook.getSheet("Sheet1");
						//获取最大行数
						rows = sheet.getPhysicalNumberOfRows();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (resultType.equals(".xls")) {
					try {
						hssfWorkbook = new HSSFWorkbook(in);
						sheet = hssfWorkbook.getSheet("Sheet1");
						rows = sheet.getPhysicalNumberOfRows();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//关闭文件输入流
				in.close();
				//将成员变量结果文件路径赋值为resultPath，表示结果表已经成功创建。
				path = resultPath;
				// 设置默认样式为第一个单元格的样式
				setStyle(0, 0);
				// System.out.println(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("写入的文件格式错误！");
		}
	}

	// 设置样式为Excel中指定单元格的样式
	public void setStyle(int rowNo, int column) {
		Row row = null;
		Cell cell = null;
		try {
			// 获取指定行
			row = sheet.getRow(rowNo);
			// 获取指定列
			cell = row.getCell(column);
			// System.out.println(cell.getStringCellValue());
			// 保存指定单元格样式
			style = cell.getCellStyle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 当用例执行结果失败时，使用该方法，以红色字体写入excel
	 * @param   r单元格行数
	 * 			l单元格列数
	 * 			value输入值
	 */
	public void writeFailCell(int rowNo, int column, String value) {
		Row row = null;
		try {
			// 获取指定行
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 行不存在，则创建
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		// 在该行，新建指定列的单元格
		Cell cell = row.createCell(column);
		// 设置单元格值
		cell.setCellValue(value);
		// 设置单元格样式
		CellStyle failStyle = null;
		// 新建字体样式
		Font font = null;
		// 根据不同的excel版本进行实例化
		if (hssfWorkbook != null) {
			font = hssfWorkbook.createFont();
			failStyle = hssfWorkbook.createCellStyle();
		} else {
			font = xssfWorkbook.createFont();
			failStyle = xssfWorkbook.createCellStyle();
		}
		// 设置字体颜色为红色
		font.setColor(IndexedColors.RED.index);
		// 将字体颜色作为单元格样式
		failStyle.setFont(font);
		// 设置对应单元格样式
		cell.setCellStyle(failStyle);
	}

	// 指定工作sheet
	public void useSheet(String sheetName) {
		if (sheet != null) {
			if (hssfWorkbook != null)
				sheet = hssfWorkbook.getSheet(sheetName);
			else
				sheet = xssfWorkbook.getSheet(sheetName);
				rows = sheet.getPhysicalNumberOfRows();
		} else
			System.out.println("未打开Excel文件！");
	}

	//以默认格式向单元格写入内容，参数使用与writeFailCell一致
	public void writeCell(int rowNo, int column, String value) {
		Row row = null;
		try {
			// 获取指定行
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 行不存在，则创建
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		// 在该行，新建指定列的单元格
		Cell cell = row.createCell(column);
		// 设置单元格值
		cell.setCellValue(value);
		// 设置单元格样式
		cell.setCellStyle(style);
	}

	// 写入一整行的内容
	public void writeLine(int rowNo, List<String> list) {
		Row row = null;
		try {
			// 获取指定行
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 行不存在，则创建
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		Cell cell = null;
		for (int i = 0; i < list.size(); i++) {
			// 在该行，新建指定列的单元格
			cell = row.createCell(i);
			// 设置单元格值
			cell.setCellValue(list.get(i));
			// 设置单元格样式
			cell.setCellStyle(style);
		}
	}

	//将结果表在内存中的工作簿内容保存到磁盘文件中
	public void save() {
		// 如果结果表文件（resultPath）未创建，则不保存
		if (path != null) {
			try {
				//基于结果表路径创建文件输出流
				stream = new FileOutputStream(new File(path));
				//将结果表的workbook工作簿的内容写入输出流中，即写入文件
				if (xssfWorkbook != null) {
					xssfWorkbook.write(stream);
					xssfWorkbook.close();
				} else {
					if (hssfWorkbook != null) {
						hssfWorkbook.write(stream);
						hssfWorkbook.close();
					} else {
						System.out.println("未打开Excel文件！");
					}
				}
				//关闭输出流，完成将内存中workbook写入文件的过程，保存文件。
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
