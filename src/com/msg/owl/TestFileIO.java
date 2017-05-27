package com.msg.owl;

public class TestFileIO
{

	public static void main(String[] args)
	{
		String filePath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		OwlFileIO ofi = new OwlFileIO(filePath);
		ofi.createFiles("食物", "G:\\Qieyin\\食物");

	}

}
