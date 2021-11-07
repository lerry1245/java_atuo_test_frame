package com.woniuxy.common;

import java.util.HashMap;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.woniuxy.http.HttpRequest;

public class DataDrivenOfInter {
	public HttpRequest httpRequest;
	public Map<String, String> paramMap;
	// 加入成员变量，方便在每一行用例调用时，统一操作的行数、返回结果的断言、excel的写入。
	public String tmpResponse;
	public int line = 0; // 成员变量行数，用于在用例执行时保持执行行和写入行一致
	public ExcelWriter outExcel;

	public DataDrivenOfInter(String casePath, String resultPath) {
		httpRequest = new HttpRequest();
		paramMap = new HashMap<String, String>();
		outExcel = new ExcelWriter(casePath, resultPath);
	}

	public DataDrivenOfInter(ExcelWriter excel) {
		httpRequest = new HttpRequest();
		paramMap = new HashMap<String, String>();
		outExcel = excel;
	}

	public String testGet(String getURl, String input) {
		String response = null;
		try {
			String param = toParam(input);
			response = httpRequest.sendGet(getURl, param);
			tmpResponse = response;
			outExcel.writeCell(line, 8, response); 
			return response;
		} catch (Exception e) {
			outExcel.writeFailCell(line, 7, "FAIL");
			outExcel.writeFailCell(line, 8, "get方法发送失败，请检查log");
			return response;
		}

	}

	public String testPost(String postUrl, String input) {
		String response = null;
		try {
			String postParam = toParam(input);
			response = httpRequest.sendPost(postUrl, postParam);
			tmpResponse = response;
			outExcel.writeCell(line, 8, response);
			return response;
		} catch (Exception e) {
			outExcel.writeFailCell(line, 7, "FAIL");
			outExcel.writeFailCell(line, 8, "get方法发送失败，请检查log");
			return response;
		}

	}

	public String toParam(String origin) {
		String param = origin;
		for (String key : paramMap.keySet()) {
			param = param.replaceAll("\\{" + key + "\\}", paramMap.get(key));
		}
		return param;
	}

	public void assertSame(String jsonPath, String expect) {
		try {
			String actual = JsonPath.read(tmpResponse, jsonPath).toString();
			if (actual != null && actual.equals(expect)) {
				outExcel.writeCell(line, 7, "PASS");
			} else {
				outExcel.writeFailCell(line, 7, "FAIL");
			}
		} catch (Exception e) {
			outExcel.writeFailCell(line, 7, "FAIL");
		}
	}

	public void assertContains(String jsonPath, String expect) {
		try {
			String actual = JsonPath.read(tmpResponse, jsonPath).toString();
			if (actual != null && actual.contains(expect)) {

				outExcel.writeCell(line, 7, "PASS");
			} else {

				outExcel.writeFailCell(line, 7, "FAIL");
			}
		} catch (Exception e) {
			outExcel.writeFailCell(line, 7, "FAIL");
		}
	}
}
