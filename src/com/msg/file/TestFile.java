package com.msg.file;

/**
 * Project Name: Data Author: MSG Time: 2016年9月6日 下午6:07:30
 */

public class TestFile
{

	public static void main(String[] args)
	{
		//FilePath.changeFiles("G:\\Qieyin\\食物txt", "G:\\Qieyin\\食物");
		//Count.countUpload("G:\\Qieyin\\data");
		/*List<String> paths = FilePath.getFileList("G:\\Qieyin\\data");
		List<String> paths1 = FilePath.getFileList("G:\\Qieyin\\data2");
		FileIO.writeFile(paths, "paths.txt");
		FileIO.writeFile(paths1, "paths1.txt");*/
		
		//FilePath.combineAllFiles("哺乳动物");
		AddName addName = new AddName();
		addName.addName();
		
	}

}
