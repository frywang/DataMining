package com.msg.txt;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.msg.file.FileIO;

public class SynonymIO
{

	private static StringBuffer getSynonym(StringBuffer stringBuffer, String words)
	{
		if (null == stringBuffer || "".equals(words.trim())) {
			System.out.println("输入有问题，请重新输入！");
			return null;
		}
		String[] splitWords = words.trim().split("#");
		int len = splitWords.length;
		String word = stringBuffer.toString().trim().split(":")[0];
		if (word.equals(splitWords[0].trim())) {
			for (int i = 1; i < len; i++) {
				if (!stringBuffer.toString().trim().contains(":" + splitWords[i] + "、")
						&& !stringBuffer.toString().trim().contains("、" + splitWords[i] + "、")) {
					stringBuffer.append(splitWords[i] + "、");
				}
			}
		}
		return stringBuffer;
	}

	/**
	 * 合并百度百科和互动百科的词条
	 * 
	 * @param baidupath
	 *            百度百科文件
	 * @param hudongpath
	 *            互动百科文件
	 */
	public static void mergeSynonym(String classpath)
	{
		if (null == classpath || "".equals(classpath.trim())) {
			System.out.println("文件路径填写错误");
			return;
		}
		String baidupath = classpath.replace(".txt", "_百度_清理.txt");
		String hudongpath = classpath.replace(".txt", "_互动_清理.txt");
		String cilinpath = classpath.replace(".txt", "_词林.txt");
		String finishedpath = classpath.replace(".txt", "_最终同义词.txt");

		FileOutputStream finishedOutputStream = FileIO.openFileOutputStream(finishedpath);
		OutputStreamWriter outputStreamWriter = FileIO.openOutputStreamWriter(finishedOutputStream);
		BufferedWriter bufferedWriter = FileIO.openBufferedWriter(outputStreamWriter);

		List<String> baiduWords = FileIO.readLines(baidupath);
		List<String> hudongWords = FileIO.readLines(hudongpath);
		List<String> cilinWords = FileIO.readLines(cilinpath);

		List<String> classWords = FileIO.readLines(classpath);
		int num = 0;
		for (String classWord : classWords) {
			StringBuffer stringBuffer = new StringBuffer(classWord + ":");

			for (String baiduWord : baiduWords) {
				stringBuffer = getSynonym(stringBuffer, baiduWord);
			}

			for (String hudongWord : hudongWords) {
				stringBuffer = getSynonym(stringBuffer, hudongWord);
			}

			for (String cilinWord : cilinWords) {
				stringBuffer = getSynonym(stringBuffer, cilinWord);
			}

			if (null != stringBuffer) {
				// System.out.println(stringBuffer.toString());
				String strWords = stringBuffer.toString();
				FileIO.writeLine(bufferedWriter, strWords.substring(0, strWords.length() - 1));
				if (stringBuffer.toString().contains("、")) {
					num++;
				}
			}
		}

		System.out.println("共有" + num + "个有同义词！");
		FileIO.closeBufferedWriter(bufferedWriter);
		FileIO.closeOutputStreamWriter(outputStreamWriter);
		FileIO.closeFileOutputStream(finishedOutputStream);
	}
}
