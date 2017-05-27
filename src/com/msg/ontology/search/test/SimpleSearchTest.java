package com.msg.ontology.search.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.msg.ontology.Ontology;
import com.msg.ontology.dict.OwlDict;
import com.msg.ontology.search.impl.SearchSimple;

public class SimpleSearchTest
{
	/**
	 * 从控制台获取输入
	 * 
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
		Ontology ont = new Ontology(filepath);
		if(null == ont.getOntModel())
		{
			System.out.println("本体文件错误，请检查");
			return;
		}
		SearchSimple ss = new SearchSimple(ont);
		List<String> lll = ss.getAttrs();
		for(String ll: lll)
		{
			System.out.println(ll);
		}
		System.out.println("选择输入(-1退出)");
		while (true) 
		{
			System.out.println("*******************************************************");
			System.out.println("***********    1. 获得全部概念深度、路径。  ***********");
			System.out.println("***********    2. 获得以某概念为父类字典。  ***********");
			System.out.println("***********    3. 获得某概念所有父类字典。  ***********");
			System.out.println("***********    4. 以同义词或者昵称查询字典。***********");
			System.out.println("*******************************************************");
			System.out.println("请输入数字1-4(-1退出)：");
			String str = getCMD();
			switch (str) {
			case "-1":
				ont.closeModel();
				System.exit(0);
			case "1":
				List<String> words = ss.getThings("Thing");
				for(String word : words)
				{
					System.out.println(word);
				}
				ss.write2buffer(words, "Thing.txt");
				break;
			case "2":
				System.out.println("请输入概念名：");
				String cls = getCMD();
				List<OwlDict> dicts = ss.searchByParent(cls);
				for (OwlDict dict : dicts) {
					System.out.println(dict);
				}
				ss.write2buffer(dicts, cls + ".txt");
				break;
			case "3":
				System.out.println("请输入概念名：");
				String cmd = getCMD();
				List<OwlDict> parentdicts = ss.searchByChild(cmd);
				for(OwlDict dict : parentdicts)
				{
					System.out.println(dict.toString(true));
				}
				ss.write2buffer(parentdicts, cmd + ".txt");
				break;
			case "4":
				System.out.println("请输入概念名：");
				String name = getCMD();
				List<OwlDict> wordsByNames = ss.searchByNames(name);
				for(OwlDict dictByNames : wordsByNames)
				{
					System.out.println(dictByNames.toString(true));
				}
				ss.write2buffer(wordsByNames, name + ".txt");
				break;
			default:
				System.out.println("数字输入错误，请重新输入！");
				break;
			}
		}
	}

}
