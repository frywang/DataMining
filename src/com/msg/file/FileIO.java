package com.msg.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileIO
{

	/**
	 * 创建文件
	 * 
	 * @param filename：文件名
	 */
	public static void createFile(String filename)
	{
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("文件<<" + filename + ">>已经存在，无需再次创建！");
		}
	}

	/**
	 * 打开FileInputStream
	 * 
	 * @param filepath
	 *            为文件路径
	 * @return FileInputStream 返回FileOutputStream对象
	 */
	public static FileInputStream openFileInputStream(String filepath)
	{
		if (null == filepath || "".equals(filepath.trim())) {
			System.out.println("输入的文件名为空，请重新处理");
			return null;
		}
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return fileInputStream;
	}

	/**
	 * 关闭FileInputStream流
	 * 
	 * @param fileInputStream
	 *            要关闭的输入字节流
	 */
	public static void closeFileInputStream(FileInputStream fileInputStream)
	{
		try {
			if (null != fileInputStream) {
				fileInputStream.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 新建输入字符流对象,此处默认设置为utf-8编码
	 * 
	 * @param fileInputStream
	 *            通过此字节流构建字符流对象
	 * @return 返回打开的输入字符流对象
	 */
	public static InputStreamReader openInputStreamReader(FileInputStream fileInputStream)
	{
		if (null == fileInputStream) {
			System.out.println("FileInputStream为空！");
			return null;
		}
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return inputStreamReader;
	}

	/**
	 * 关闭InputStreamReader
	 * 
	 * @param inputStreamReader
	 *            要关闭的InputStreamReader
	 */
	public static void closeInputStreamReader(InputStreamReader inputStreamReader)
	{
		try {
			if (null != inputStreamReader) {
				inputStreamReader.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 缓冲字符流对象
	 * 
	 * @param inputStreamReader
	 *            字符流对象
	 * @return 返回缓冲字符流对象
	 */
	public static BufferedReader openBufferedReader(InputStreamReader inputStreamReader)
	{
		if (null == inputStreamReader) {
			System.out.println("InputStreamReader为空！");
			return null;
		}
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		return bufferedReader;
	}

	/**
	 * 关闭缓冲输入字符流对象
	 * 
	 * @param bufferedReader
	 *            关闭的缓冲字符流对象
	 */
	public static void closeBufferedReader(BufferedReader bufferedReader)
	{
		try {
			if (null != bufferedReader) {
				bufferedReader.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 创建一个输出字节流对象
	 * 
	 * @param filepath
	 *            文件路径
	 * @return 返回创建的字节流对象
	 */
	public static FileOutputStream openFileOutputStream(String filepath)
	{
		if (null == filepath || "".equals(filepath.trim())) {
			System.out.println("输入的文件路径为空！");
			return null;
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return fileOutputStream;
	}

	/**
	 * 关闭输出字节流对象
	 * 
	 * @param fileOutputStream
	 *            要关闭的输出字节流对象
	 */
	public static void closeFileOutputStream(FileOutputStream fileOutputStream)
	{
		try {
			if (null != fileOutputStream) {
				fileOutputStream.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 创建输出字符流对象
	 * 
	 * @param fileOutputStream
	 *            通过此字节输出流创建字符输出流
	 * @return 返回创建的字符输出流
	 */
	public static OutputStreamWriter openOutputStreamWriter(FileOutputStream fileOutputStream)
	{
		if (null == fileOutputStream) {
			System.out.println("FileOutputStream为空！");
			return null;
		}
		OutputStreamWriter outputStreamWriter = null;
		try {
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return outputStreamWriter;
	}

	/**
	 * 关闭输出字符流
	 * 
	 * @param outputStreamWriter
	 *            要关闭的对象
	 */
	public static void closeOutputStreamWriter(OutputStreamWriter outputStreamWriter)
	{

		try {
			if (null != outputStreamWriter) {
				outputStreamWriter.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * 创建一个缓冲字符流对象
	 * 
	 * @param outputStreamWriter
	 *            通过此字符流创建
	 * @return 返回创建的缓冲字符流
	 */
	public static BufferedWriter openBufferedWriter(OutputStreamWriter outputStreamWriter)
	{
		if (null == outputStreamWriter) {
			System.out.println("输入的OutputStreamWriter为空！");
			return null;
		}
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		return bufferedWriter;
	}

	/**
	 * 关闭缓冲字符流
	 * 
	 * @param bufferedWriter
	 *            要关闭的流对象
	 */
	public static void closeBufferedWriter(BufferedWriter bufferedWriter)
	{
		try {
			if (null != bufferedWriter) {
				bufferedWriter.flush();
				bufferedWriter.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 按行读取文件中所有的数据
	 * 
	 * @param fis
	 *            读取内容的对象
	 * @return 返回一个列表
	 */
	public static List<String> readLines(String filename)
	{
		if (null == filename || "".equals(filename)) {
			System.out.println("FileInputStream为空！");
			return null;
		}
		FileInputStream fis = openFileInputStream(filename);
		InputStreamReader isr = FileIO.openInputStreamReader(fis);
		BufferedReader br = FileIO.openBufferedReader(isr);

		List<String> lines = new ArrayList<String>();
		String line = null;

		try {
			while (null != (line = br.readLine())) {
				if(!"".equals(line.trim()) 
						&& !"AVP:".equals(line.trim())
						&& !"TAG:".equals(line.trim())
						&& !"INFO:".equals(line.trim())
						&& !"TYPES:".equals(line.trim())){
					lines.add(line);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		FileIO.closeBufferedReader(br);
		FileIO.closeInputStreamReader(isr);
		FileIO.closeFileInputStream(fis);
		return lines;
	}

	/**
	 * 按行写入文件
	 * 
	 * @param bufferedWriter
	 *            要写入的文件流
	 * @param words
	 *            要写入的内容
	 * @return int 返回写入的条数
	 */
	public static int writeLine(BufferedWriter bufferedWriter, String words)
	{
		if (null == bufferedWriter || null == words) {
			System.out.println(bufferedWriter + "和" + words + "有一为空！");
			return 0;
		}
		int flag = 0;
		try {
			bufferedWriter.write(words);
			bufferedWriter.newLine();
			flag = 1;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return flag;
	}

	/**
	 * 读取文件中的内容
	 * 
	 * @param src
	 *            源文件
	 * @return 返回字符串
	 */
	public static String read(File src)
	{
		StringBuffer res = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(src));
			while ((line = reader.readLine()) != null) {
				res.append(line + "\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return res.toString();
	}

	/**
	 * 将content内容写入文件
	 * 
	 * @param content
	 *            写入的内容
	 * @param dist
	 *            写入的文件
	 * @return boolean 返回一个布尔值
	 */
	public static boolean write(String content, File dist)
	{
		try {
			FileOutputStream writer = new FileOutputStream(dist);
			writer.write(content.getBytes());
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * 获取从控制台输入的一行字符串
	 * 
	 * @return 返回输入的字符串
	 */
	public static String getCMD()
	{
		BufferedReader bufferedReader = null;

		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		String str = "";
		try {
			str = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println("没有读到数据，请重新输入1-9：");
		}
		return str;
	}

	/**
	 * 将字符串列表内容写入文件中
	 * 
	 * @param strList
	 *            字符串列表
	 * @param fileName
	 *            文件名
	 */
	public static void writeFile(List<String> strList, String fileName)
	{
		FileOutputStream fos = openFileOutputStream(fileName);
		OutputStreamWriter osw = openOutputStreamWriter(fos);
		BufferedWriter bw = openBufferedWriter(osw);

		for (String str : strList) {
			writeLine(bw, str);
		}

		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);
	}

}
