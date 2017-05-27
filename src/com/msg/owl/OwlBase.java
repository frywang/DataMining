package com.msg.owl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.msg.file.FileIO;
import com.msg.string.StringIO;

/**
 * 涉及到Jena和外部文件的处理
 * 
 * @author MSG
 */
public class OwlBase extends Owl
{

	/**
	 * 以父类的构造函数创造
	 * 
	 * @param filePath
	 *            owl文件或者ttl文件所在的路径
	 * @param urlPath
	 *            本体库中设置的URL路径
	 */
	public OwlBase(String filePath)
	{
		super(filePath);
	}

	/**
	 * 将某个概念类下的子类全部输出并输入一个excel文件中，能体现出层级关系。
	 * 
	 * @param num
	 *            一个标识符
	 * @param strClass
	 *            概念类
	 */
	public void getClasses(int num, String strClass)
	{
		OntClass ontClass = getOntClass(strClass);

		if (null == ontClass) {
			System.out.println("没有<<" + strClass + ">>这个类啊");
			return;
		}

		List<String> listClass = findClasses(num, ontClass);
		if (null == listClass || 0 == listClass.size()) {
			System.out.println("没有得到数据哎");
			return;
		}

		/** 写入一个excel文件中 **/
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(strClass);
		int iNum = 0;

		for (String str : listClass) {
			Row row = sheet.createRow(iNum);
			int i = StringIO.countChar(str, ',');
			Cell cell = row.createCell(i);
			cell.setCellValue(str.replaceAll(",", ""));
			iNum++;
		}
		FileOutputStream fos = FileIO.openFileOutputStream(strClass + ".xlsx");
		try {
			workbook.write(fos);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileIO.closeFileOutputStream(fos);
	}

	/**
	 * 将一个类下的所有子类写入文件中
	 * 
	 * @param cls
	 *            概念类
	 */
	public void getClasses(String cls)
	{
		OntClass ontClass = getOntClass(cls);
		if (null == ontClass) {
			System.out.println("没有这个概念类:" + cls);
			return;
		}
		List<String> listClass = findClasses(ontClass);
		if (null == listClass || 0 == listClass.size()) {
			System.out.println("没有获取到任何数据");
			return;
		}

		FileIO.writeFile(listClass, cls + ".txt");

		System.out.println("--------------------------------------");
		System.out.println("已经写入到文件" + cls + ".txt中");
	}

	/**
	 * 通过概念名，找到其同义词，昵称和父类
	 * 
	 * @param strName
	 *            概念类名
	 * @return 返回一个OwlClassDict对象
	 */
	public OwlClassDict getDict(String strName)
	{
		OntClass ontClass = getOntClass(strName);
		if (null == ontClass) {
			return null;
		}

		OwlClassDict dict = new OwlClassDict();

		/**** name不能是空，若是空，直接退出 ****/
		String name = ontClass.getLocalName();
		if (null == name) {
			System.out.println("name为空，出错");
			return null;
		}
		dict.setName(name);

		/********** 列出所有的同义词 ***********/
		Iterator<?> iterLabels = ontClass.listLabels("ZH");

		StringBuilder sbSyn = new StringBuilder("");
		while (iterLabels.hasNext()) {
			Literal label = (Literal) iterLabels.next();
			String synName = label.getLexicalForm();
			sbSyn.append(synName + " ");
		}
		dict.setSynname(sbSyn.toString().trim());

		/********** 列出所有的昵称 ************/
		Property nickname = getOntProperty("nickname");
		Iterator<?> iterNickname = ontClass.listPropertyValues(nickname);
		StringBuilder sbNick = new StringBuilder("");
		while (iterNickname.hasNext()) {
			String nick = ((Literal) iterNickname.next()).getString();
			sbNick.append(nick + " ");
		}
		dict.setNickname(sbNick.toString().trim());

		/************* 获得ID ************/
		Property pID = getOntProperty("id");
		Literal lID = (Literal) ontClass.getPropertyValue(pID);
		if (null != lID) {
			String id = lID.getLexicalForm();
			dict.setId(id);
		}

		/************* 获得imp **************/
		Property pImp = getOntProperty("imp");
		Literal lImp = (Literal) ontClass.getPropertyValue(pImp);
		if (null != lImp) {
			String imp = lImp.getLexicalForm();
			dict.setImp(imp);
		}
		// System.out.println(dict);
		return dict;
	}

	/**
	 * 通过同义词或者昵称获得概念名
	 * 
	 * @param strProp
	 *            属性名
	 * @param strName
	 *            同义词或者昵称
	 * @return 概念名的列表
	 */
	public List<String> getClassNames(String strProp, String strName)
	{
		List<String> listNames = new ArrayList<String>();
		Property synPro = getOntProperty(strProp);

		ResIterator res = ontModel.listSubjectsWithProperty(synPro, strName, "zh");

		/** 若是一个同义词有多个概念类呢？ **/
		while (res.hasNext()) {
			Resource ontClass = res.nextResource();
			String name = ontClass.getLocalName();
			listNames.add(name);
		}
		return listNames;
	}

	/**
	 * 通过任意词，返回包括概念词、同义词、昵称、父类、ID、imp的词条
	 * 
	 * @param strName
	 *            输入的可以是概念词、同义词和昵称
	 * @return OwlClassDict对象组成的列表
	 */
	public List<OwlClassDict> getDicts(String strName)
	{
		List<OwlClassDict> listDicts = new ArrayList<OwlClassDict>();
		OwlClassDict nameWord = getDict(strName);

		List<String> listNamesBySyn = getClassNames("label", strName);
		List<String> listNamesByNick = getClassNames("nickname", strName);

		if (null != nameWord) {
			listDicts.add(nameWord);
		}

		if (0 != listNamesBySyn.size()) {
			for (String strNameBySyn : listNamesBySyn) {
				if (!strName.equals(strNameBySyn)) {
					OwlClassDict synWord = getDict(strNameBySyn);
					if (null != synWord) {
						listDicts.add(synWord);
					}
				}
			}
		}

		if (0 != listNamesByNick.size()) {
			for (String strNameByNick : listNamesByNick) {
				OwlClassDict nickWord = getDict(strNameByNick);
				if (null != nickWord) {
					listDicts.add(nickWord);
				}
			}
		}
		return listDicts;
	}

	/**
	 * 找到相同同义词或昵称的字典
	 * 
	 * @param file
	 *            存放了相同同义词的文件
	 */
	public void getSameDict(String file)
	{
		List<String> listNames = FileIO.readLines(file);

		for (String word : listNames) {
			getDicts(word);
		}
	}

	/**
	 * 用来节省代码
	 * 
	 * @param sb
	 *            StringBuilder类的对象
	 * @param allNames
	 *            同义词或者昵称数组
	 * @param name
	 *            标示同义词还是昵称
	 * @return 返回那个StringBilder对象
	 */
	private StringBuilder formatDict(StringBuilder sb, String allNames, String name)
	{
		sb.append("##" + name);
		if (null != allNames) {
			String[] names = allNames.split(" ");
			if (null != names) {
				/********** 格式化输出 *********/
				for (int i = 0; i < names.length; i++) {
					if (0 == i) {
						sb.append(names[i]);
					} else {
						sb.append("_" + names[i]);
					}
				}
			} else {
				sb.append(" ");
			}
		} else {
			sb.append(" ");
		}
		return sb;
	}

	/**
	 * 获得概念strClass下的所有子类的id、同义词、昵称和指代
	 * 
	 * @param strClass
	 *            概念类名
	 */
	public List<OwlClassDict> getClassDicts(String strClass)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + strClass);
		if (null == ontClass) {
			System.out.println("此类不存在哦");
			return null;
		}
		FileOutputStream fos = FileIO.openFileOutputStream(strClass + ".txt");
		OutputStreamWriter osw = FileIO.openOutputStreamWriter(fos);
		BufferedWriter bw = FileIO.openBufferedWriter(osw);

		List<OwlClassDict> listDict = new ArrayList<OwlClassDict>();
		List<String> listClass = findClasses(ontClass);
		for (String str : listClass) {
			OwlClassDict dict = getDict(str);
			listDict.add(dict);
			StringBuilder sb = new StringBuilder("id=" + dict.getId() + "##name=" + dict.getName());
			String synNames = dict.getSynname();
			String nickNames = dict.getNickname();
			String alterNames = dict.getAltername();
			String imp = dict.getImp();
			formatDict(sb, synNames, "synName=");
			formatDict(sb, nickNames, "nickName=");
			formatDict(sb, alterNames, "alterName=");

			if (null != imp) {
				sb.append("##imp=" + imp);
			} else {
				sb.append("##imp= ");
			}

			// System.out.println(sb.toString());
			FileIO.writeLine(bw, sb.toString());
		}
		FileIO.closeBufferedWriter(bw);
		FileIO.closeOutputStreamWriter(osw);
		FileIO.closeFileOutputStream(fos);
		return listDict;
	}

}
