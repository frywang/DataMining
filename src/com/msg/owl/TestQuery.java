package com.msg.owl;

/**
 * Project Name: Data Author: MSG Time: 2016年9月7日 下午3:04:03
 */

public class TestQuery
{

	public static void main(String[] args)
	{

		String filePath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		OwlQuery query = new OwlQuery(filePath);

		Long start = System.currentTimeMillis();

		Long stop = System.currentTimeMillis();

		System.out.println((stop - start) + "毫秒");

		System.out.println(query.getOntClass("Thing"));
		query.closeModel();

	}

}
