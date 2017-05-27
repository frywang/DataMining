package com.msg.txt;

/**
 * Project Name: Data Author: MSG Time: 2016年8月31日 上午9:45:17
 */

public class TestTxt
{

	public static void main(String[] args)
	{
		String name = "日常用品";
		// CilinIO.findSynonym("日常用品.txt");
		// BaikeIO.cleanBaike(name + "_百度.txt");
		// BaikeIO.cleanBaike(name + "_互动.txt");
		SynonymIO.mergeSynonym(name + ".txt");
	}

}
