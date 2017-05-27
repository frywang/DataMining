package com.msg.txt;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.msg.file.FileIO;

/**
 * 对词林文件的操作
 * 
 * @author MSG
 *
 */
public class CilinIO
{

	private static String CILIN = "cilin.txt";

	/**
	 * 获取词林中的同义词
	 * 
	 * @param filepath
	 *            要获取同义词的文件，其中每个概念词占一行
	 */
	public static void findSynonym(String filepath)
	{
		if (null == filepath || "".equals(filepath.trim())) {
			System.out.println("输入的文件路径名为空，请重新输入！");
			return;
		}

		// 将文本中内容读入List中
		List<String> synonymWords = FileIO.readLines(filepath);
		List<String> lines = FileIO.readLines(CILIN);

		// 创建需要写出的文件
		FileOutputStream fos = FileIO.openFileOutputStream(filepath.replace(".txt", "") + "_词林.txt");
		OutputStreamWriter osw = FileIO.openOutputStreamWriter(fos);
		BufferedWriter bw = FileIO.openBufferedWriter(osw);

		// 对词条进行逐条的比较写出（是否需要加入多线程加快速度？）
		for (String synonymWord : synonymWords) {
			StringBuffer stringBuffer = new StringBuffer(synonymWord);
			for (String line : lines) {
				if (line.contains(" " + synonymWord + " ")) {
					String allWords = line.replace("= ", "");
					String[] splitWords = allWords.trim().split(" ");
					int len = splitWords.length;
					for (int i = 0; i < len; i++) {
						if (!splitWords[i].equals(synonymWord)) {
							stringBuffer.append("#" + splitWords[i]);
						}
					}
				}
			}
			if (!stringBuffer.toString().equals(synonymWord)) {
				FileIO.writeLine(bw, stringBuffer.toString());
			}
			System.out.println(stringBuffer.toString());
		}

		// 关闭各种文件流
		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);
	}

}
