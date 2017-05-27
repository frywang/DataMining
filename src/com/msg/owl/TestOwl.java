package com.msg.owl;

import com.msg.file.FileIO;

/**
 * Project Name: Data Author: MSG Time: 2016年8月23日 下午3:20:01
 */

public class TestOwl
{

	public static void main(String[] args)
	{
		String filePath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		OwlBase jena = new OwlBase(filePath);

		while (true) {
			System.out.println("请输入概念类");
			String strClass = FileIO.getCMD();
			if ("-1".equals(strClass)) {
				jena.closeModel();
				System.exit(0);
			}
			Long start = System.currentTimeMillis();
			// jena.getClasses(strClass);
			// jena.getDicts(strClass);
			// jena.getWrongDict("test");
			//jena.getClassDicts(strClass);
			jena.getClasses(0, strClass);
			Long stop = System.currentTimeMillis();
			System.out.println("共用去" + (stop - start) + "毫秒");
		}
	}

}
