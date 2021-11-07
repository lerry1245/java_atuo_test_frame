package com.woniuxy.cbt.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelReader {
	
	/*
	 * ��ɶ�excel�ļ��Ķ�ȡ
	 * @method	ExcelReader���캯������ȡexcel�ļ����ݵ�workbook��
	 * 			useSheet��ȡָ��sheetҳ
	 * 			close����ļ���ȡ���ͷ���Դ
	 * 			readNextLine��ȡ��ǰ�У����������ƶ�����һ��
	 * 			readLine��ȡָ����
	 * 			getCellValue��Ե�Ԫ�����ݲ�ͬ��ʽ���ж�ȡ
	 */
	
	
	//���ڶ�ȡexcel���������͵ĵ�Ԫ��ָ�����ڸ�ʽ
	private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
	//xlsx��ʽ�Ĺ�����
	private XSSFWorkbook xssfWorkbook;
	//xls��ʽ�Ĺ�����
	private HSSFWorkbook hssfWorkbook;
	//������sheetҳ
	private Sheet sheet;
	//�������
	public int rows = 0;
	//��ǰ��ȡ������
	private int lineNow = 0;

	//���캯����������Excel
	public ExcelReader(String path) {
		//��ȡ��׺��
		String type = path.substring(path.indexOf("."));
		//��ʼ���ļ���
		FileInputStream in = null;
		 try {
			 //ͨ���ļ�����excel�ļ�
			in = new FileInputStream(new File(path));
		} catch (FileNotFoundException e1) {
			// ��ȡʧ�������Excel��ȡʧ�ܵ���ʾ����ֹͣ
			e1.printStackTrace(); 
			return;
		}
		//�ж���xls����xlsx��ʽ
		if (type.equals(".xlsx")) {
			try {
				//�����xlsx��ʽ��ͨ���ļ��������ڴ��д���xssfworkbook������
				xssfWorkbook = new XSSFWorkbook(in);
				//��ʼ��sheetҳ
				sheet = xssfWorkbook.getSheet("Sheet1");
				//��ȡ�������
				rows = sheet.getPhysicalNumberOfRows();
				lineNow = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//�����xls��ʽ���򴴽�hssf���͹�����
		if (type.equals(".xls")) {
			try {
				hssfWorkbook = new HSSFWorkbook(in);
				sheet = hssfWorkbook.getSheet("Sheet1");
				rows = sheet.getPhysicalNumberOfRows();
				lineNow = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//�ر��ļ�������
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//���û��sheet���������Excelʧ�ܵ���ʾ
		if(sheet==null)
			System.out.println("Excel�ļ���ʧ�ܣ�");
	}

	//����������ʾ��ʽ
	public void setDateFormat(String dateFormat) {
		fmt = new SimpleDateFormat(dateFormat);
	}

	//���ö�ȡ��sheetҳ
	public void useSheet(String sheetName) {
		if(sheet!=null) {
			//���ݴ򿪵Ĺ��������ͣ������г�ʼ��sheetҳ
			if(hssfWorkbook!=null)
				sheet = hssfWorkbook.getSheet(sheetName);
			else
				sheet = xssfWorkbook.getSheet(sheetName);
			
			rows = sheet.getPhysicalNumberOfRows();
			lineNow = 0;
		}else
			System.out.println("δ��Excel�ļ���");
	}
	
	//��ȡ��ɣ��ر�Excel
	public void close() {
		try {
			if(hssfWorkbook!=null)
				hssfWorkbook.close();
			else
				xssfWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//��ȡlineNow�У�lineNow++
	public List<String> readNextLine() {
		//ʹ��list��ŵ�ǰ�е���Ϣ
		List<String> line = new ArrayList<String>();
		//��ȡ��ǰ��
		Row row = sheet.getRow(lineNow);
		//��ȡ��ǰ�еĵ�Ԫ����
		int cellCount = row.getPhysicalNumberOfCells();
		//ѭ����ȡ�����е�Ԫ������ݣ�����list��
		for (int c = 0; c < cellCount; c++) {
			line.add(getCellValue(row.getCell(c)));
		}
		lineNow++;
		return line;
	}

	//��ȡ������ָ������
	public List<String> readLine(int rowNo) {
		List<String> line = new ArrayList<String>();
		Row row = sheet.getRow(rowNo);
		int cellCount = row.getPhysicalNumberOfCells();
		for (int c = 0; c < cellCount; c++) {
			line.add(getCellValue(row.getCell(c)));
		}
		return line;
	}
	
	//��ȡָ����
	public List<String> readColumn(int colNo) {
		List<String> column = new ArrayList<String>();
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			column.add(getCellValue(row.getCell(colNo)));
		}
		return column;
	}
	
	//��ȡָ����Ԫ��
	public String readCell(int rowNo,int column) {
		String content;
		Row row = sheet.getRow(rowNo);
		content = getCellValue(row.getCell(column));
		return content;
	}
	
	//��Ե�Ԫ�����ݲ�ͬ��ʽ���ж�ȡ
	@SuppressWarnings("deprecation")
	private String getCellValue(Cell cell) {
		//����Ԫ������Ϊ��ʱ������null����������poi��ȡĳЩxls��ʽ��excel��ʱ�����ĳЩ�հ׸�ᱨ��ָ���쳣
		String cellValue = "null";
		if(cell==null)
			return cellValue;
		try {
			int cellType = cell.getCellType();
			//�����и�ʽתΪ�ַ���
			switch (cellType) {
			case Cell.CELL_TYPE_STRING: // �ı�
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC: // ���֡�����
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = fmt.format(cell.getDateCellValue()); // ������
				} else {
					Double d = cell.getNumericCellValue();
					DecimalFormat df = new DecimalFormat("#.##");
					cellValue = df.format(d);
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN: // ������
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK: // �հ�
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_ERROR: // ����
				cellValue = "����";
				break;
			case Cell.CELL_TYPE_FORMULA: // ��ʽ
				cellValue = "����";
				break;
			default:
				cellValue = "����";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellValue;
	}

}
