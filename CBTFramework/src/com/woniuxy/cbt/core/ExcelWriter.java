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
	 * ��ȡ���������ݣ����������������ݵ����������ɽ����Ĵ�������ʵ����������д������
	 * @method  excelWriter���캯����ɶԽ����Ĵ��� 
	 * 			setStyleָ��������д������ʱ�ĸ�ʽ
	 * 			writeFailCell����ʧ��ʱ�Ժ�ɫ����д�� 
	 *			writeCell����������Ժ�ɫ����д�뵥Ԫ�� 
	 *			useSheetָ��ʹ�õ�sheet
	 *			writeLine����д������ 
	 * 			save����д������ݵ�excel�ļ��У���ɲ����������ã�����excel�ļ������ݡ�
	 */

	// ���ڴ�Ž�������ݵ�xlsx��ʽ�Ĺ�����
	private XSSFWorkbook xssfWorkbook = null; 
	// ���ڴ�Ž�������ݵ�xls��ʽ�Ĺ�����
	private HSSFWorkbook hssfWorkbook = null;
	// ������sheetҳ
	private Sheet sheet;
	// ���ڶ�ȡ���������ݸ��Ƶ��������ļ������
	private FileOutputStream stream = null;
	// ���ڴ洢������·���ĳ�Ա�����������ڱ�����ʱ�����ж�
	private String path = null;
	// ��Ԫ���ʽ
	private CellStyle style = null;
	// ���������
	public int rows = 0;

	/*
	 * ����������casePahth�����������resultPath����casePahth�е����ݸ��Ƶ�resultPath��
	 * @param casePahth������·�� 
	 * 		  resultPath�����·��
	 */
	public ExcelWriter(String casePath, String resultPath) {
		// ��ȡ�������׺��
		String Origintype = casePath.substring(casePath.indexOf("."));
		// �ж���xls����xlsx��ʽ��������ڴ��д���������Ĺ�����
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
		// ������ָ�ʽ�������ϣ����ļ���ʧ��
		if (xssfWorkbookRead == null && hssfWorkbookRead == null) {  
			System.out.println("Excel�ļ���ʧ�ܣ�");
			return;
		}

		// ��ȡ������׺��
		String resultType = resultPath.substring(resultPath.indexOf("."));
		// ȷ��������ʽΪexcel��ʽ
		if (resultType.equals(".xlsx") || resultType.equals(".xls")) {
			try {
				// ���ݽ������ļ�����Ϊ���ļ����ڴ��п��ٿռ�
				File file = new File(resultPath);
				try {
					// �ڴ������洴�����ļ�
					file.createNewFile();
				} catch (Exception e1) {
					// ����ʧ�ܣ���ʾ·���Ƿ�����ֹͣ����
					System.out.println("�ļ�·���Ƿ������飡");
					e1.printStackTrace();
					return;
				}
				// ���ڽ���������ļ������stream
				stream = new FileOutputStream(file);
				// ���������е�����д���ļ������stream
				if (hssfWorkbookRead != null) {
					hssfWorkbookRead.write(stream);
					// �ر����������ڴ��еĸ���
					hssfWorkbookRead.close();
				} else {
					xssfWorkbookRead.write(stream);
					xssfWorkbookRead.close();
				}
				// �ر��Ѿ�д�������������ݵ��ļ���
				stream.close();

				// ���ڽ���������ļ�������(����֮���ٶ�ȡ���������)
				FileInputStream in = new FileInputStream(file);
				// �жϽ���ļ��ĺ�׺��03�滹��07��excel
				if (resultType.equals(".xlsx")) {
					try {
						//ͨ���ļ������������ڴ��д��������Ĺ�����
						xssfWorkbook = new XSSFWorkbook(in);
						sheet = xssfWorkbook.getSheet("Sheet1");
						//��ȡ�������
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
				//�ر��ļ�������
				in.close();
				//����Ա��������ļ�·����ֵΪresultPath����ʾ������Ѿ��ɹ�������
				path = resultPath;
				// ����Ĭ����ʽΪ��һ����Ԫ�����ʽ
				setStyle(0, 0);
				// System.out.println(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("д����ļ���ʽ����");
		}
	}

	// ������ʽΪExcel��ָ����Ԫ�����ʽ
	public void setStyle(int rowNo, int column) {
		Row row = null;
		Cell cell = null;
		try {
			// ��ȡָ����
			row = sheet.getRow(rowNo);
			// ��ȡָ����
			cell = row.getCell(column);
			// System.out.println(cell.getStringCellValue());
			// ����ָ����Ԫ����ʽ
			style = cell.getCellStyle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * ������ִ�н��ʧ��ʱ��ʹ�ø÷������Ժ�ɫ����д��excel
	 * @param   r��Ԫ������
	 * 			l��Ԫ������
	 * 			value����ֵ
	 */
	public void writeFailCell(int rowNo, int column, String value) {
		Row row = null;
		try {
			// ��ȡָ����
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �в����ڣ��򴴽�
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		// �ڸ��У��½�ָ���еĵ�Ԫ��
		Cell cell = row.createCell(column);
		// ���õ�Ԫ��ֵ
		cell.setCellValue(value);
		// ���õ�Ԫ����ʽ
		CellStyle failStyle = null;
		// �½�������ʽ
		Font font = null;
		// ���ݲ�ͬ��excel�汾����ʵ����
		if (hssfWorkbook != null) {
			font = hssfWorkbook.createFont();
			failStyle = hssfWorkbook.createCellStyle();
		} else {
			font = xssfWorkbook.createFont();
			failStyle = xssfWorkbook.createCellStyle();
		}
		// ����������ɫΪ��ɫ
		font.setColor(IndexedColors.RED.index);
		// ��������ɫ��Ϊ��Ԫ����ʽ
		failStyle.setFont(font);
		// ���ö�Ӧ��Ԫ����ʽ
		cell.setCellStyle(failStyle);
	}

	// ָ������sheet
	public void useSheet(String sheetName) {
		if (sheet != null) {
			if (hssfWorkbook != null)
				sheet = hssfWorkbook.getSheet(sheetName);
			else
				sheet = xssfWorkbook.getSheet(sheetName);
				rows = sheet.getPhysicalNumberOfRows();
		} else
			System.out.println("δ��Excel�ļ���");
	}

	//��Ĭ�ϸ�ʽ��Ԫ��д�����ݣ�����ʹ����writeFailCellһ��
	public void writeCell(int rowNo, int column, String value) {
		Row row = null;
		try {
			// ��ȡָ����
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �в����ڣ��򴴽�
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		// �ڸ��У��½�ָ���еĵ�Ԫ��
		Cell cell = row.createCell(column);
		// ���õ�Ԫ��ֵ
		cell.setCellValue(value);
		// ���õ�Ԫ����ʽ
		cell.setCellStyle(style);
	}

	// д��һ���е�����
	public void writeLine(int rowNo, List<String> list) {
		Row row = null;
		try {
			// ��ȡָ����
			row = sheet.getRow(rowNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �в����ڣ��򴴽�
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		Cell cell = null;
		for (int i = 0; i < list.size(); i++) {
			// �ڸ��У��½�ָ���еĵ�Ԫ��
			cell = row.createCell(i);
			// ���õ�Ԫ��ֵ
			cell.setCellValue(list.get(i));
			// ���õ�Ԫ����ʽ
			cell.setCellStyle(style);
		}
	}

	//����������ڴ��еĹ��������ݱ��浽�����ļ���
	public void save() {
		// ���������ļ���resultPath��δ�������򲻱���
		if (path != null) {
			try {
				//���ڽ����·�������ļ������
				stream = new FileOutputStream(new File(path));
				//��������workbook������������д��������У���д���ļ�
				if (xssfWorkbook != null) {
					xssfWorkbook.write(stream);
					xssfWorkbook.close();
				} else {
					if (hssfWorkbook != null) {
						hssfWorkbook.write(stream);
						hssfWorkbook.close();
					} else {
						System.out.println("δ��Excel�ļ���");
					}
				}
				//�ر����������ɽ��ڴ���workbookд���ļ��Ĺ��̣������ļ���
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
