package com.msg.ontology.search.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.msg.ontology.Ontology;
import com.msg.ontology.dict.TripleDict;
import com.msg.ontology.search.impl.SearchComplex;

public class ComplexSearchTest
{
	/**
	 * 从控制台获取输入
	 * @return 字符串
	 */
	public static String getCMD()
	{
		BufferedReader br = null;

		br = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args)
	{
		String filepath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		Ontology ont = new Ontology(filepath,true);
		SearchComplex cs = new SearchComplex(ont);

		while (true)
		{
			System.out.println("*******************************************************");
			System.out.println("***********    1. 获得某概念全部属性及值。  ***********");
			System.out.println("***********    2. 查询属性。                ***********");
			System.out.println("*******************************************************");
			System.out.println("请输入数字1-2(-1退出)：");
			String str = getCMD();
			switch (str) {
			case "-1":
				ont.closeModel();
				System.exit(0);
			case "1":
				System.out.println("输入查询的内容");
				String name = getCMD();
				Long start = System.currentTimeMillis();
				List<TripleDict> tripleList = cs.searchRs(name);
				for(TripleDict triple: tripleList)
				{
					System.out.println(triple);
				}
				Long stop = System.currentTimeMillis();
				System.out.println("耗时" + (stop - start) + "ms");
				break;
			case "2":
				while (true) 
				{
					System.out.println("输入查询的内容，以空格分开(-1退出本次循环)");
					String strs = getCMD();
					if ("-1".equals(strs)) 
					{
						break;
					}
					String[] strArray = strs.split(" ");
					if(1 == strArray.length)
					{
						Long starttime = System.currentTimeMillis();
						List<TripleDict> value = cs.searchRBySynAndNick(strArray[0], "", "");
						for(TripleDict td:value)
						{
							System.out.println(td);
						}
						Long stoptime = System.currentTimeMillis();
						System.out.println("耗时" + (stoptime - starttime) + "ms");
					}
					else if (2 == strArray.length) 
					{
						Long starttime = System.currentTimeMillis();
						List<TripleDict> value = cs.searchRBySynAndNick(strArray[0], strArray[1], "");
						for(TripleDict td:value)
						{
							System.out.println(td);
						}
						Long stoptime = System.currentTimeMillis();
						System.out.println("耗时" + (stoptime - starttime) + "ms");

					} 
					else if(3 == strArray.length) 
					{
						Long starttime = System.currentTimeMillis();
						List<TripleDict> value = cs.searchRBySynAndNick(strArray[0], strArray[1], strArray[2]);
						for(TripleDict td:value)
						{
							System.out.println(td);
						}
						Long stoptime = System.currentTimeMillis();
						System.out.println("耗时" + (stoptime - starttime) + "ms");
					}
				}
				break;
			default:
				System.out.println("数字输入错误，请重新输入！");
				break;
			}
		}
	}
}
