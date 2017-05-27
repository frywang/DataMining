package com.msg.main;

import com.msg.excel.ExcelWrite;
import com.msg.file.Count;
import com.msg.file.FileIO;
import com.msg.file.FilePath;
import com.msg.owl.OwlBase;
import com.msg.txt.BaikeIO;
import com.msg.txt.CilinIO;
import com.msg.txt.SynonymIO;

/**
 * Project Name: Data Author: MSG Time: 2016年8月22日 上午8:17:07
 * 查询子类，查询父类，查询id，查询同义词，查询属性
 */

public class DataMain
{

	/**
	 * 主函数，对所有功能统一处理
	 * 
	 * @param args
	 *            字符串
	 */
	public static void main(String[] args)
	{
		/*
		if (!"qieyinChild.owl".equals(args[0])) {
			System.out.println("输入命令出错，请确认owl文件后重新输入命令");
		}
		String filePath = args[0];
		*/
		String filePath = "/home/fry/Documents/svn/ai/trunk/ontology/qieyinChild.owl";
		
		OwlBase ow = new OwlBase(filePath);

		while (true) {
			System.out.println("*******************************************************");
			System.out.println("***********    1. 输出txt并统计数据条数。   ***********");
			System.out.println("***********    2. 从词林中发现同义词。      ***********");
			System.out.println("***********    3. 清理百科爬取的同义词。    ***********");
			System.out.println("***********    4. 合并同义词。              ***********");
			System.out.println("***********    5. 一键合并。                ***********");
			System.out.println("***********    6. 查询本体库中的数据。      ***********");
			System.out.println("***********    7. 查询带父类的数据。        ***********");
			System.out.println("***********    8. 导出同义词文本库。        ***********");
			System.out.println("***********    9. 替换层级下的空文件。      ***********");
			System.out.println("***********    10. 统计已上传多少文件。     ***********");
			System.out.println("***********    11. 导出某个概念的子类。     ***********");
			System.out.println("***********    12. 导出某个概念下层级。     ***********");
			System.out.println("***********    13. 导出所有的概念词等。     ***********");
			System.out.println("*******************************************************");
			System.out.println("请输入数字1-13(-1退出)：");
			String str = FileIO.getCMD();
			switch (str) {
			case "-1":
				ow.closeModel();
				System.exit(0);

			case "1":
				System.out.println("请输入excel文件所在的路径：");
				String strExcelPath = FileIO.getCMD();
				System.out.println("请输入txt要存到的文件夹");
				String strFinishedPath = FileIO.getCMD();
				ExcelWrite ex = new ExcelWrite();
				ex.writeAllExcel(strExcelPath, strFinishedPath);
				System.out.println("输出txt并统计条目数完成,统计数据在统计.xlsx中");
				break;
			case "2":
				System.out.println("请输入概念词txt文件名：");
				String strTxtPath = FileIO.getCMD();
				CilinIO.findSynonym(strTxtPath);
				System.out.println("输出同义词完成，同义词在XXX_词林.txt中");
				break;
			case "3":
				System.out.println("请输入百科txt文件名（格式：XXX_[百度|互动].txt）：");
				String strBaikePath = FileIO.getCMD();
				BaikeIO.cleanBaike(strBaikePath);
				System.out.println("清理完成，保存在文件XXX_[百度|互动]_清理.txt中");
				break;
			case "4":
				System.out.println("请输入要合并的概念词的txt文件名（格式：XXX.txt）：");
				String strSynPath = FileIO.getCMD();
				SynonymIO.mergeSynonym(strSynPath);
				System.out.println("合并完成，保存在文件XXX_最终同义词.txt中");
				break;
			case "5":
				System.out.println("请输入要合并的概念词的文件名（格式：XXX不带.txt）的目录：");
				String strMergerPath = FileIO.getCMD();
				CilinIO.findSynonym(strMergerPath + ".txt");
				BaikeIO.cleanBaike(strMergerPath + "_百度.txt");
				BaikeIO.cleanBaike(strMergerPath + "_互动.txt");
				SynonymIO.mergeSynonym(strMergerPath + ".txt");
				System.out.println("合并完成，保存在文件XXX_最终同义词.txt中");
				break;
			case "6":
				while (true) {
					System.out.println("请输入要查询的概念词、同义词或昵称(-1退出)：");
					String strClass = FileIO.getCMD();
					if ("-1".equals(strClass)) {
						break;
					} else {
						// WordDict word = ow.findUltWords(strClass);
						// long start = System.currentTimeMillis();
						// if(null != word)
						// System.out.println(word);
						// System.out.println("用时：" +
						// (System.currentTimeMillis() - start) + "ms");
					}
				}
				break;
			case "7":
				break;
			case "8":
				System.out.println("请输入要导出的最顶层的类：");
				String strClass = FileIO.getCMD();
				ow.getClassDicts(strClass);
				System.out.println("已经导出到" + strClass + ".txt");
				break;
			case "9":
				System.out.println("请输入替换数据所在的路径：");
				String changeFile = FileIO.getCMD();
				System.out.println("请输入层级数据所在的路径：");
				String classFile = FileIO.getCMD();
				FilePath.changeFiles(changeFile, classFile);
				break;
			case "10":
				System.out.println("请输入已上传文件所在的文件夹：");
				String uploadPath = FileIO.getCMD();
				Count.countUpload(uploadPath);
				break;
			case "11":
				while (true) {
					System.out.println("请输入你要导出的概念类(-1退出)：");
					String outClassName = FileIO.getCMD();
					if ("-1".equals(outClassName)) {
						break;
					}
					ow.getClasses(outClassName);
				}
				break;
			case "12":
				while (true) {
					System.out.println("请输入你要导出的概念类(-1退出)：");
					String outClassName = FileIO.getCMD();
					if ("-1".equals(outClassName)) {
						break;
					}
					ow.getClasses(0, outClassName);
				}
				break;
			case "13":
				break;
			default:
				System.out.println("重新输入！");
				break;
			}
		}
	}

}
