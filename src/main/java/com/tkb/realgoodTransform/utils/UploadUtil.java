package com.tkb.realgoodTransform.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上傳元件
 * @author Felix
 * @version 創建時間：2022-03-08
 */
public class UploadUtil {
	private Random rand = new Random();
//	public static String upload(String filePath, String fileStream, String fileName
//			, Integer id, String extension) throws IOException {
//		File file = new File(filePath);
//		//資料夾不存在則新增資料夾
//		if(!file.exists())
//		{
//		    file.mkdirs();
//		    try {
//		        file.createNewFile();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}
//		//若上傳檔案為空則回傳原本檔名
//		if (fileStream == null || "".equals(fileStream)) {
//			return fileName;
//		}
//		//以"年月日時分秒ID副檔名"來重新命名檔案
//		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
//				+ String.format("%06d", id) + "." + extension;
//		try {			
//			InputStream is = new FileInputStream(fileStream);
//			OutputStream os = new FileOutputStream(filePath + "/" + fileName);
//			byte[] buffer = new byte[1024];
//			int length = 0;
//
//			while (-1 != (length = is.read(buffer))) {
//				os.write(buffer, 0, length);
//			}
//			os.close();
//			is.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return fileName;
//	}
//	
	public String uploadv2(String filePath, MultipartFile image, Integer id) throws IOException {
		
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		byte[] bytes = image.getBytes();
		String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
		
		String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
//		Random random = new Random();
		int nextInt = this.rand.nextInt(900) + 100;
		
		String finalPath = filePath + time + nextInt + suffix;
		File locationFile = new File(finalPath);
		
		Files.write(locationFile.toPath(), bytes);
		
		
		return time + nextInt + suffix;
	}
	
	
	/**
	 * 多重上傳機制，回傳以年日月時分秒ID組成的檔名，為了要一次上傳多個檔案，會驗證資料夾內是否有重複檔名，資料夾內檔案過多會造成上傳速度降低
	 * @param filePath
	 * @param fileStream
	 * @param fileName
	 * @param id
	 * @param extension
	 * @return String fileName
	 * @throws IOException
	 */
//	public static String uploadMultipart(String filePath, String fileStream, String fileName
//			, Integer id, String extension) throws IOException {
//		File file = new File(filePath);
//		//資料夾不存在則新增資料夾
//		if(!file.exists())
//		{
//		    file.mkdirs();
//		    try {
//		        file.createNewFile();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}
//		//若上傳檔案為空則回傳原本檔名
//		if (fileStream == null) {
//			return fileName;
//		}
//		//以"年月日時分秒ID"來重新命名檔案
//		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
//				+ String.format("%06d", id);
//		java.io.File folder = new java.io.File(filePath);
//		String[] list = folder.list();
//		//比對資料夾內是否有重複檔名
//		for (String s : list) {
//			//比對去掉副檔名的兩個檔案是否相同，s.substring(0,s.lastIndexOf(46))為去掉副檔名的檔案名稱
//			if (fileName.equals(s.substring(0,s.lastIndexOf(46)))) {
//				//以Integer.valueOf(fileName.substring(10,14))+1來做秒數+1的重新命名
//				fileName = fileName.substring(0,10)
//						+ String.format("%04d",(Integer.valueOf(fileName.substring(10,14)) + 1))
//						+ String.format("%06d", id);
//			}
//		}
//		//加上副檔名
//		fileName += "." + extension;
//		try {			
//			InputStream is = new FileInputStream(fileStream);
//			OutputStream os = new FileOutputStream(filePath + "/" + fileName);
//			byte[] buffer = new byte[1024];
//			int length = 0;
//
//			while (-1 != (length = is.read(buffer))) {
//				os.write(buffer, 0, length);
//			}
//			os.close();
//			is.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return fileName;
//	}
	
	public static void delete(String filePath) throws IOException {
		Boolean bool = false;
		File file = new File(filePath);
		bool = file.delete();
		System.out.println("File delete : "+bool);
	}
}
