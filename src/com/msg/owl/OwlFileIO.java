package com.msg.owl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;

import com.msg.file.FileIO;

/**
 * Project Name: Data Author: MSG Time: 2016年9月5日 下午12:47:10
 */

public class OwlFileIO extends Owl
{

	public OwlFileIO(String filePath)
	{
		super(filePath);
	}

	/**
	 * 创建文件，以utf-8的格式创建，并在文件中预先写入数据
	 * 
	 * @param filepath
	 *            创建的文件路径名
	 */
	private void createUTF8(String filepath)
	{

		/************* 构建缓冲输出流 *************/
		FileOutputStream fos = FileIO.openFileOutputStream(filepath);
		OutputStreamWriter osw = FileIO.openOutputStreamWriter(fos);
		BufferedWriter bw = FileIO.openBufferedWriter(osw);

		/******** 将文件名提取出来 ********/
		String[] filenames = filepath.split("/");
		String filename = filenames[filenames.length - 1];
		String fileName = filename.split("_")[0];

		/******** 预先写入文件的数据 ********/
		FileIO.writeLine(bw, "##" + fileName + "##");
		if (filename.contains("知识")) {
			FileIO.writeLine(bw, fileName + "知识类问题暂时为空后续添加$$$" + fileName + "知识类答案暂时为空后续添加");
		} else if (filename.contains("闲聊")) {
			FileIO.writeLine(bw, fileName + "闲聊类问题暂时为空后续添加$$$" + fileName + "闲聊类答案暂时为空后续添加");
		}

		/******** 关闭输出缓冲流 ********/
		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);

		/********* 输出提示 *********/
		System.out.println(filepath + "文件创建成功");
	}

	/**
	 * 创建层级的文件
	 * 
	 * @param ontClass
	 *            最顶级的概念类
	 * @param dirPath
	 *            创建的层级文件路径
	 */
	private void createFile(OntClass ontClass, String dirPath)
	{

		/******** 获得概念类名 ********/
		String strClass = ontClass.getLocalName().trim();

		/***** 若是路径不存在，则创建 *****/
		File filepath = new File(dirPath);
		if (!filepath.exists()) {
			filepath.mkdir();
		}
		/******** 创建文件 ********/
		String filePath1 = dirPath + "/" + strClass + "_知识.txt";
		String filePath2 = dirPath + "/" + strClass + "_闲聊.txt";

		/*** 判断是否存在，防止重复生成 ***/
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);

		if (!file1.exists()) {
			createUTF8(filePath1);
		}
		if (!file2.exists()) {
			createUTF8(filePath2);
		}

		dirPath = dirPath + "/" + strClass;
		if (ontClass.hasSubClass()) {
			Iterator<OntClass> iter = ontClass.listSubClasses();
			/****** 递归生成 *****/
			while (iter.hasNext()) {
				OntClass ontSubClass = iter.next();
				createFile(ontSubClass, dirPath);
			}
		}
	}

	/**
	 * 根据类名创建层级结构的数据
	 * 
	 * @param strClass
	 *            概念类名
	 * @param filepath
	 *            保存到的路径
	 */
	public void createFiles(String strClass, String filepath)
	{
		OntClass ontClass = getOntClass(strClass);

		if (null == ontClass) {
			System.out.println("没有这个类哦，重新输入看看？");
			return;
		}
		createFile(ontClass, filepath);
	}

	/**
	 * 查找所有类别及子类并将概念类名和其标签不一致的结果 保存到一个hashmap中,以用于处理protege导入乱码现象
	 * 
	 * @param ontClass
	 *            查询名字错误的所有概念类的最顶级的父类
	 * @return HashMap<String, String> 存放概念名及其标签的hashmap对象
	 */
	private HashMap<String, String> getWrongClasses(OntClass ontClass)
	{

		HashMap<String, String> hashMap = new HashMap<String, String>();

		/******** 将类别和标签不同的情况记录下来 *********/
		if (!ontClass.hasSubClass()) {
			String className = ontClass.getLocalName().trim();
			String classLabel = ontClass.getLabel("ZH");
			if (null != classLabel) {
				if (!className.equals(classLabel.trim()) && className.length() > 20) {
					hashMap.put(className, classLabel);
				}
			} else {
				System.out.println("请注意，概念<<" + className + ">>没有设置Label！");
			}
		} else {
			Iterator<OntClass> iter = ontClass.listSubClasses();
			while (iter.hasNext()) {
				OntClass ontSubClass = iter.next();
				hashMap.putAll(getWrongClasses(ontSubClass));
			}
		}
		return hashMap;
	}

	/**
	 * 更改本体文件由于protege导致的乱码问题
	 * 
	 * @param strClass
	 *            以此字符串创建OntClass对象
	 */
	public void changeWrongClasses(String strClass)
	{
		int iChanged = 0;
		OntClass ontClass = getOntClass(strClass);
		if (null == ontClass) {
			System.out.println("没有这个类哦");
			return;
		}
		HashMap<String, String> hashMap = getWrongClasses(ontClass);

		File src = new File(filePath);
		String content = FileIO.read(src);
		Iterator<String> iter = hashMap.keySet().iterator();

		while (iter.hasNext()) {
			String key = iter.next();
			if ((content = content.replaceAll(key, hashMap.get(key))) != null) {
				System.out.println("已替换不搭配组合：" + key + "--" + hashMap.get(key));
				iChanged++;
			}
		}

		FileIO.write(content, src);

		if (iChanged == 0) {
			System.out.println("没发现protege导致的不同，无需替换!");
		} else {
			System.out.println("替换完成，共替换" + iChanged + "个组合!");
		}
	}

}
