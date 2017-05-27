package com.msg.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于文件路径的操作
 * 
 * @author MSG
 *
 */
public class FilePath
{

	/**
	 * 功能：获取一个目录下所有的文件列表
	 * 
	 * @param filepath
	 *            要获取所有文件绝对路径的目录
	 * @return 包含目录下所有文件的绝对路径字符串的列表
	 */
	public static List<String> getFileList(String filepath)
	{
		List<String> fileList = new ArrayList<String>();
		File path = new File(filepath);
		if (!path.exists()) {
			System.out.println("目录<<" + filepath + ">>不存在，请重新输入！");
		}
		if (path.exists() && path.isDirectory()) {
			File[] files = path.listFiles();
			if (files.length != 0) {
				for (File file : files) {
					if (!file.isDirectory()) {
						fileList.add(file.getAbsolutePath());
					} else {
						fileList.addAll(getFileList(file.getAbsolutePath()));
					}
				}
			} else {
				System.out.println("目录<<" + filepath + ">>中没有文件");
			}
		}
		return fileList;
	}

	/**
	 * 读取一个文件夹下的所有子文件夹（不跨目录）
	 * 
	 * @param superPath
	 *            文件夹
	 * @return 返回存放了所有子文件夹字符串的列表
	 */
	public static List<String> getPathList(String superPath)
	{
		List<String> list = new ArrayList<String>();

		File superfile = new File(superPath);

		if (!superfile.exists() || !superfile.isDirectory()) {
			System.out.println("文件夹不存在，请重试！");
			return null;
		}

		File[] paths = superfile.listFiles();

		for (File path : paths) {
			if (path.exists() && path.isDirectory()) {
				list.add(path.getName());
				// System.out.println(path.getName());
			}
		}

		return list;
	}

	public static void createPaths(String filename)
	{
		List<String> listOfNames = FileIO.readLines(filename);

		for (String strOfNames : listOfNames) {
			System.out.println(strOfNames);
			File file = new File("G:\\分类\\" + strOfNames);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	// 把文件夹从第一个位置移到另一个位置
	public static void movePath(String firstPath, String secondPath)
	{
		File first = new File(firstPath);
		String firstName = first.getName();
		String secondPaths = secondPath + "\\" + firstName;
		File second = new File(secondPaths);
		if (first.exists() && !second.exists()) {
			File[] files = first.listFiles();
			for (File file : files) {
				File changeFile = new File(secondPaths + "\\" + file.getName());
				file.renameTo(changeFile);
			}
			first.renameTo(second);
		}
	}

	public static void countNo(String path)
	{
		List<String> list = getPathList(path);
		int count = 0;
		for (String name : list) {
			File file = new File(path + "\\" + name);
			File[] files = file.listFiles();
			if (null == files || 0 == files.length) {
				count++;
				System.out.println(file.getName());
			}
		}
		System.out.println("还有" + count + "个");
	}

	/**
	 * 创建目录
	 * 
	 * @param pathname：目录名
	 */
	public static void createPath(String pathname)
	{
		File path = new File(pathname);
		if (!path.exists()) {
			path.mkdirs();
			// System.out.println("目录" + pathname + "已经创建");
		}
	}

	/**
	 * 批量替换层级文件夹下的文件
	 * 
	 * @param changeFilePath
	 *            要替换的文件夹
	 * @param classFilesPath
	 *            被替换的文件夹
	 * @return 返回替换的条目数
	 */
	public static int changeAllFiles(String changeFilePath, String classFilesPath)
	{
		File dirFile = new File(changeFilePath);
		int iNum = 0;
		if (dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();
			List<String> lists = getFileList(classFilesPath);
			// Set<String> sets = new HashSet<String>();
			int len = files.length;
			for (int i = 0; i < len; i++) {
				String changeFileName = files[i].getName().trim();
				for (String strPath : lists) {
					File classFile = new File(strPath);
					String classFileName = classFile.getName().trim();
					// String classFilePath = classFile.getAbsolutePath();
					if (changeFileName.equals(classFileName)) {
						classFile.delete();
						// sets.add(classFileName.split("_")[0]);
						files[i].renameTo(new File(strPath));
						System.out.println("" + files[i].getName());
						iNum++;
					}
				}
			}
		}
		System.out.println("-------------------------------------------------------");
		System.out.println("共替换了" + iNum + "个文本数据");
		return iNum;
	}

	/**
	 * 批量替换层级文件夹下的文件
	 * 
	 * @param changePath
	 *            要替换的文件夹
	 * @param classPath
	 *            被替换的文件夹
	 * @return 返回替换的条目数
	 */
	public static int changeFiles(String changePath, String classPath)
	{
		File dirFile = new File(changePath);
		int iNum = 0;
		if (dirFile.isDirectory()) {

			/******** 获取单层目录下所有文件的绝对路径 ********/
			File[] files = dirFile.listFiles();

			/********* 获取文件夹下所有的文件绝对路径 *********/
			List<String> lists = getFileList(classPath);
			int len = files.length;

			for (int i = 0; i < len; i++) {

				String changeName = files[i].getName().trim();

				for (String strPath : lists) {
					File classFile = new File(strPath);
					String className = classFile.getName().trim();

					if (changeName.equals(className)) {

						FileInputStream fis = FileIO.openFileInputStream(strPath);
						InputStreamReader isr = FileIO.openInputStreamReader(fis);
						BufferedReader br = FileIO.openBufferedReader(isr);

						try {
							String lineTxt = br.readLine();
							lineTxt = br.readLine();
							lineTxt = br.readLine();
							if (null == lineTxt || "".equals(lineTxt.trim())) {

								FileIO.closeBufferedReader(br);
								FileIO.closeInputStreamReader(isr);
								FileIO.closeFileInputStream(fis);

								//classFile.delete();
								String strPath1 = strPath.replace("食物", "食物1");
								File file1 = new File(strPath1);
								if (!file1.exists()) {
									String papapa = file1.getParent();
									new File(papapa).mkdirs();
								}
								files[i].renameTo(file1);
								// System.out.println(file1.getParent());
								System.out.println("替换了" + files[i].getName());

								iNum++;
							}
						} catch (IOException e1) {
							System.out.println(e1.getMessage());
						}
					}
				}
			}
		}
		System.out.println("-------------------------------------------------------");
		System.out.println("共替换了" + iNum + "个文本数据");
		return iNum;
	}
	
	
	public static List<String> getLines(String filePath){
		List<String> lineList = new ArrayList<String>();
		
		BufferedReader br = null;
		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
			while(null != (line = br.readLine())){
				if(!"".equals(line) && line.contains("$$$") && !line.contains("问题暂时为空后续添加")){
					lineList.add(line.substring(0, line.indexOf("$$$")));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != br){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return lineList;
	}
	
	
	
	public static void combineAllFiles(String path){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream("all闲聊.txt"),"utf-8"));
			
			List<String> fileList = getFileList(path);
			for(String file:fileList){
				if(file.contains("闲聊")){
					List<String> lineList = getLines(file);
					for(String line: lineList){
						bw.write(line);
						bw.newLine();
					}
				}
			}
			bw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != bw){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

}
