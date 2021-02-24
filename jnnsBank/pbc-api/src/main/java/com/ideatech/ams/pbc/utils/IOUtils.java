package com.ideatech.ams.pbc.utils;

import com.ideatech.common.MyLog;

import java.io.*;

public class IOUtils {
	public static String readFile(String filePath, String encoding, boolean rmBlankLine) {
		BufferedReader br = null;
		StringBuffer retStr = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
			String str = "";
			while ((str = br.readLine()) != null) {
				if (rmBlankLine) {
					retStr.append(str.trim());
				} else {
					retStr.append(str.trim()).append("\r\n");
				}
			}
//			br.close();
		} catch (FileNotFoundException e) {
			if (MyLog.isError)
				e.printStackTrace();
			return null;
		} catch (IOException e) {
			if (MyLog.isError)
				e.printStackTrace();
			return null;
		} finally {
//			br = null;
			org.apache.commons.io.IOUtils.closeQuietly(br);
		}
		return retStr.toString();
	}

	public static void buildFile(String filePath, byte[] bytes) {
		BufferedOutputStream bos = null;
		try {
			File pfile = new File(filePath).getParentFile();
			if (!pfile.exists())
				pfile.mkdirs();
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bos.write(bytes);
//			bos.close();
		} catch (FileNotFoundException e) {
			if (MyLog.isError)
				e.printStackTrace();
		} catch (IOException e) {
			if (MyLog.isError)
				e.printStackTrace();
		} finally {
//			bos = null;
			org.apache.commons.io.IOUtils.closeQuietly(bos);
		}
	}

	public static void buildFile(String filePath, String data, String encoding) {
		BufferedWriter bw = null;
		try {
			File pfile = new File(filePath).getParentFile();
			if (!pfile.exists())
				pfile.mkdirs();
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
			bw.write(data);
//			bw.close();
		} catch (FileNotFoundException e) {
			if (MyLog.isError)
				e.printStackTrace();
		} catch (IOException e) {
			if (MyLog.isError)
				e.printStackTrace();
		} finally {
//			bw = null;
			org.apache.commons.io.IOUtils.closeQuietly(bw);
		}
	}

}
