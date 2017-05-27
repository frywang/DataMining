package com.msg.txt;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.msg.file.FileIO;

public class BaikeIO
{

	/**
	 * 清理百科词条，取得别称及同义词等
	 * 
	 * @param baikepath
	 *            清理的百科文件的路径
	 */
	public static void cleanBaike(String baikepath)
	{
		List<String> lines = FileIO.readLines(baikepath);

		FileOutputStream fos = FileIO.openFileOutputStream(baikepath.replace(".txt", "") + "_清理.txt");
		OutputStreamWriter osw = FileIO.openOutputStreamWriter(fos);
		BufferedWriter bw = FileIO.openBufferedWriter(osw);

		for (String line : lines) {
			String[] words = line.split("#");
			StringBuffer sb = new StringBuffer(words[0]);
			if (!"null".equals(words[4])) {
				String[] allWords = words[4].trim().replaceAll("”", "").replaceAll("、", "#").replaceAll(" ", "#")
						.split("#");
				for (String word : allWords) {
					if (!word.equals(words[0])) {
						sb.append("#" + word.trim());
					}
				}

				FileIO.writeLine(bw, sb.toString());
			}
		}
		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);
		System.out.println("清理" + baikepath + "完成");
	}

}
