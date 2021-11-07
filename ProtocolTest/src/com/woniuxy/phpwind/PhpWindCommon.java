package com.woniuxy.phpwind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhpWindCommon {
	public void readtxt() {
		try {
			String path = System.getProperty("user.dir");
			File file = new File(path + "\\data\\userdata.txt");
			InputStream in = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader wr = new BufferedReader(reader);
			String line;
			while ((line = wr.readLine()) != null) {
				String[] data = line.split(",");
			}
			in.close();
			reader.close();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// MD5加密
	public static String md5Secret(String plainText) {
		byte[] secrectBytes = null;
		try {
			secrectBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有MD5这个算法");
		}
		String md5code = new BigInteger(1, secrectBytes).toString(16); // 16进制数字
		// 如果生成的数字未满32位，不满用0补足
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code.toUpperCase();

	}

}
