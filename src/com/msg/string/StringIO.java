package com.msg.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringIO
{

	/**
	 * 将字符串数组按照某个字符或字符串连接起来
	 * 
	 * @param join
	 *            连接所用的字符或字符串
	 * @param strArray
	 *            要连接的字符串数组
	 * @return 返回连接后的字符串
	 */
	public static String join(String join, String[] strArray)
	{
		StringBuffer stringBuffer = new StringBuffer();
		int len = strArray.length;
		for (int i = 1; i < len; i++) {
			if (i == (strArray.length - 1)) {
				stringBuffer.append(strArray[i]);
			} else {
				stringBuffer.append(strArray[i]).append(join);
			}
		}
		return new String(stringBuffer);
	}

	/**
	 * 去除字符串中的重复值
	 * 
	 * @param words要去重的字符串
	 * @return 返回去重之后的字符串
	 */
	public static String filterWords(String words)
	{
		if (null == words || "".equals(words.trim())) {
			System.out.println("输入的词为空");
			return null;
		}
		StringBuffer filter = new StringBuffer();
		String[] splitWords = words.split(" ");
		int len = splitWords.length;
		for (int i = 0; i < len; i++) {
			if (!filter.toString().contains(splitWords[i])) {
				filter.append(splitWords[i] + " ");
			}
		}
		return filter.toString();
	}

	/**
	 * 用来对字符串去除各种空符号，如\n\t\r等
	 * 
	 * @param str
	 *            要去处空符号的字符串
	 * @return 返回去除空格后的字符串
	 */
	public static String replaceBlank(String str)
	{
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 统计一个字符串中某字符出现的次数
	 * 
	 * @param str
	 *            待统计的字符串
	 * @param ch
	 *            统计的字符
	 * @return 返回字符出现的次数
	 */
	public static int countChar(String str, char ch)
	{
		int iNum = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch) {
				iNum++;
			}
		}
		return iNum;
	}

}
