package com.ideatech.ams.image.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class RequestUploadImageUtils {

	public static FileItemStream httpRequest2FileItemStream(HttpServletRequest request) {
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart)
				return null;
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				FileItemStream fileItem = iter.next();
				if (!fileItem.isFormField()) {
					return fileItem;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File writeIs2File(String tempFilePath, String fileName, InputStream is) {
		OutputStream os = null;
		File file = null;
		try {
			/*File fileFloder = new File(tempFilePath + File.separator + System.currentTimeMillis() + RandomStringUtils.randomNumeric(5));
			if (!fileFloder.exists())
				fileFloder.mkdirs();*/
			fileName = FilenameUtils.getName(fileName);
			file = new File(tempFilePath + File.separator + fileName);
			os = new FileOutputStream(file);
			IOUtils.copy(is, os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
		return file;
	}

	public static File writeIs2File(String tempFilePath, FileItemStream fileItemStream,String fileName) {
		
		try {
			return writeIs2File(tempFilePath, fileName, fileItemStream.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
