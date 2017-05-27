package com.msg.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.junit.Test;

public class Count
{

	/**
	 * 统计已经上传了多少txt文件
	 * 
	 * @param dir
	 *            层次路径
	 * @return 返回上传的文件数目
	 */
	public static int countUpload(String dir)
	{
		List<String> fileList = FilePath.getFileList(dir);
		int numFinished = 0;
		int numAll = 0;
		for (String strFileName : fileList) {
			numAll++;
			FileInputStream fileInputStream = FileIO.openFileInputStream(strFileName);
			InputStreamReader inputStreamReader = FileIO.openInputStreamReader(fileInputStream);
			BufferedReader bufferedReader = FileIO.openBufferedReader(inputStreamReader);
			try {
				String lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
				if (null == lineTxt || "".equals(lineTxt.trim())) {
					System.out.println(strFileName.substring(strFileName.lastIndexOf("\\") + 1) + "没有上传");
				} else {
					numFinished++;
				}
			} catch (IOException e1) {
				// e1.printStackTrace();
				System.out.println(e1.getMessage());
			} finally {
				FileIO.closeBufferedReader(bufferedReader);
				FileIO.closeInputStreamReader(inputStreamReader);
				FileIO.closeFileInputStream(fileInputStream);
			}
		}
		System.out.println("共上传了" + numFinished + "个文件");
		System.out.println("共有" + numAll + "个文件");
		return numFinished;
	}

	/**
	 * 为尚没有上传数据的文本写入提示数据、基本已没用
	 * 
	 * @param filePath
	 *            对未上传的文本加入替代语句
	 */
	public static void fill(String filePath)
	{
		List<String> fileList = FilePath.getFileList(filePath);
		for (String strFileName : fileList) {
			FileInputStream fileInputStream = FileIO.openFileInputStream(strFileName);
			InputStreamReader inputStreamReader = FileIO.openInputStreamReader(fileInputStream);
			BufferedReader bufferedReader = FileIO.openBufferedReader(inputStreamReader);
			String lineTxt = null;
			try {
				lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println(e.getMessage());
			}
			FileIO.closeBufferedReader(bufferedReader);
			FileIO.closeInputStreamReader(inputStreamReader);
			FileIO.closeFileInputStream(fileInputStream);
			if (null == lineTxt || "".equals(lineTxt.trim())) {
				File tempFile = new File(strFileName);
				if (tempFile.isFile() && tempFile.exists()) {
					tempFile.delete();
				}
				OutputStreamWriter outputStreamWriter = FileIO
						.openOutputStreamWriter(FileIO.openFileOutputStream(strFileName));
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				String splitName = strFileName.substring(strFileName.lastIndexOf("\\") + 1, strFileName.indexOf("_"));
				if (strFileName.contains("知识")) {
					FileIO.writeLine(bufferedWriter, "##" + splitName + "##");
					FileIO.writeLine(bufferedWriter, splitName + "知识类问题暂时为空后续添加$$$" + splitName + "知识类答案暂时为空后续添加");
				} else if (strFileName.contains("闲聊")) {
					FileIO.writeLine(bufferedWriter, "##" + splitName + "##");
					FileIO.writeLine(bufferedWriter, splitName + "闲聊类问题暂时为空后续添加$$$" + splitName + "闲聊类答案暂时为空后续添加");
				}
				System.out.println("写入成功！");
				FileIO.closeBufferedWriter(bufferedWriter);
				FileIO.closeOutputStreamWriter(outputStreamWriter);
			}
		}
	}
	
	/**
	 * 统计已经上传了多少txt文件
	 * 
	 * @param dir
	 *            层次路径
	 * @return 返回上传的文件数目
	 */
	public static int countUpload1(String dir)
	{
		List<String> fileList = FilePath.getFileList(dir);
		int numFinished = 0;
		int numFinished1 = 0;
		int numAll = 0;
		for (String strFileName : fileList) {
			numAll++;
			FileInputStream fileInputStream = FileIO.openFileInputStream(strFileName);
			InputStreamReader inputStreamReader = FileIO.openInputStreamReader(fileInputStream);
			BufferedReader bufferedReader = FileIO.openBufferedReader(inputStreamReader);
			String lineTxt = null;
			try {
				lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
				lineTxt = bufferedReader.readLine();
				if (null == lineTxt || "".equals(lineTxt.trim()) ) {
					System.out.println(strFileName.substring(strFileName.lastIndexOf("\\") + 1) + "没有上传");
				} else {
					while (null != (lineTxt = bufferedReader.readLine())) {
						if(!"".equals(lineTxt))
						{
							numFinished ++;
						}
					}
					numFinished1 ++;
				}
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			} finally {
				FileIO.closeBufferedReader(bufferedReader);
				FileIO.closeInputStreamReader(inputStreamReader);
				FileIO.closeFileInputStream(fileInputStream);
			}
		}
		System.out.println("共上传了" + numFinished1 + "个文件");
		System.out.println("共有" + numAll + "个文件");
		return numFinished;
	}
	
	
	

	/**
	 * @param filepath
	 *            所在的文件夹
	 */
	public static void countCode(String filepath)
	{
		List<String> fileList = FilePath.getFileList(filepath);
		int count = 0;
		for (String str : fileList) {
			int num = FileIO.readLines(str).size();
			count += num;
		}
		System.out.println("共有" + count + "行代码！");
	}

	@Test
	public void count()
	{
		System.out.println(countUpload1("G:/Qieyin/食物1"));
	}

}
