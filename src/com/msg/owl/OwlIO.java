package com.msg.owl;

import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;

import com.msg.excel.ExcelReadDict;
import com.msg.file.FileIO;
import com.msg.ontology.util.GUID;

/**
 * Project Name: Data Author: MSG Time: 2016年9月5日 上午11:51:18
 */

public class OwlIO extends Owl
{

	public OwlIO(String filePath)
	{
		super(filePath);
	}

	/**
	 * 添加一个概念类
	 * 
	 * @param strClass
	 *            概念类的ID，此处用中文表示了
	 * @return OntClass 返回添加的概念类
	 */
	protected OntClass addClass(String strClass)
	{
		OntClass ontClass = getOntClass(strClass);
		if (null == ontClass) {
			ontClass = ontModel.createClass(urlBase + strClass);

			/**** 添加概念类的同时添加label ****/
			ontClass.addLabel(strClass, "zh");
			// 后期去掉这些代码
			System.out.println("添加概念<<" + strClass + ">>成功！");
		} else {
			System.out.println("概念类<<" + strClass + ">>已经存在，请确认后重新添加！");
		}
		return ontClass;
	}

	/**
	 * 从列表中添加多个类，返回添加类的数目
	 * 
	 * @param list
	 *            列表
	 * @return int 添加的类的数目
	 */
	public int addClasses(List<String> list)
	{
		if (null == list || 0 == list.size()) {
			System.out.println("列表为空，请重新添加！");
			return 0;
		}
		int iNum = 0;
		for (String strClass : list) {
			addClass(strClass);
			iNum++;
		}
		// 后期删除的提示代码
		System.out.println("-------------------------------------------------------------");
		System.out.println("共添加" + iNum + "个概念类");
		return iNum;
	}

	/**
	 * 添加单个属性
	 * 
	 * @param strPro
	 * @return OntProperty
	 */
	protected OntProperty addObjProperty(String strPro)
	{
		OntProperty ontProperty = ontModel.getObjectProperty(urlBase + strPro);
		if (null == ontProperty) {
			ontProperty = ontModel.createObjectProperty(urlBase + strPro);
			ontProperty.addLabel(strPro, "zh");
			System.out.println("成功添加属性" + strPro);
		} else {
			System.out.println(strPro + "已经存在，请确认后重新添加");
		}
		return ontProperty;
	}

	/**
	 * 从列表中添加多个属性
	 * 
	 * @param list
	 * @return int
	 */
	public int addObjProperties(List<String> list)
	{
		if (null == list || 0 == list.size()) {
			System.out.println("list为空，请重试");
			return 0;
		}
		int iNum = 0;
		for (String strPro : list) {
			addObjProperty(strPro);
			iNum++;
		}
		// 待删除代码
		System.out.println("-------------------------------------------------------------");
		System.out.println("共添加" + iNum + "个属性");
		return iNum;
	}

	/**
	 * 以只知道字符串的方式添加子类
	 * 
	 * @param strClass
	 *            子类概念名
	 * @param strSuper
	 *            父类概念名
	 */
	protected void addSub(String strClass, String strSuper)
	{
		OntClass ontClass = getOntClass(strClass);
		OntClass ontSuper = getOntClass(strSuper);
		if (ontClass != null && ontSuper != null) {
			ontSuper.addSubClass(ontClass);
		} else {
			System.out.println("添加子类出错，请确保父类和子类都存在！");
		}
	}

	/**
	 * 批量添加子类
	 * 
	 * @param list
	 *            子类概念名列表
	 * @param strSuper
	 *            父类概念名
	 * @return int 返回添加的子类数目
	 */
	public int addSubs(List<String> list, String strSuper)
	{
		int iNum = 0;
		if (list.size() != 0) {
			for (String str : list) {
				addSub(str, strSuper);
				iNum++;
			}
		} else {
			System.out.println("列表为空，请重新添加！");
		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("在父类" + strSuper + "下添加了" + iNum + "个子类");
		return iNum;
	}

	/**
	 * 给不同类之间添加property，构建三元组
	 * 
	 * @param strSubject
	 *            主语
	 * @param strObject
	 *            宾语
	 * @param strPro
	 *            谓语
	 */
	protected void addPro(String strSubject, String strObject, String strPro)
	{
		OntClass ontSubject = getOntClass(strSubject);
		OntClass ontObject = getOntClass(strObject);
		OntProperty ontPredicate = ontModel.getOntProperty(urlBase + strPro);
		if (null != ontPredicate && null != ontSubject && null != ontObject) {
			ontSubject.addProperty(ontPredicate, ontObject);
			System.out.println(strSubject + "和" + strSubject + "通过属性" + strPro + "联系在一起了！");
		} else {
			System.out.println("添加三元组失败，请确保类和属性都存在！");
		}
	}

	/**
	 * 添加单个实例
	 * 
	 * @param ontClass
	 *            概念类对象
	 * @param strIn
	 *            实例名
	 * @return Individual 实例对象
	 */
	protected Individual addIn(OntClass ontClass, String strIn)
	{
		Individual in = ontClass.createIndividual(urlBase + strIn);
		in.addLabel(strIn, "zh");
		System.out.println("给概念类" + ontClass.getLocalName() + "添加" + strIn + "成功！");
		return in;
	}

	/**
	 * 从列表中添加多个实例
	 * 
	 * @param ontClass
	 *            概念类对象
	 * @param list
	 *            保存实例名的列表
	 * @return int 返回添加的实例个数
	 */
	public int addIns(OntClass ontClass, List<String> list)
	{
		int num = 0;
		for (String strIn : list) {
			addIn(ontClass, strIn);
			num++;
		}
		System.out.println("----------------------------------------------------------");
		System.out.println("一共给概念类" + ontClass.getLocalName() + "添加了" + num + "个实例！");
		return num;
	}

	/**
	 * 批量添加概念类并将其作为某概念类的子类，并写入文件
	 * 
	 * @param fileName
	 *            保存了子类的文件路径
	 * @param strSuper
	 *            父类
	 */
	public void addChilds(String fileName, String strSuper)
	{
		List<String> list = FileIO.readLines(fileName);

		addClasses(list);
		addSubs(list, strSuper);

		write2file();
	}

	/**
	 * 给一个概念类批量添加同义词和昵称
	 * 
	 * @param wordDict
	 *            添加的同义词和昵称的字典
	 */
	private void addWords(OwlClassDict wordDict)
	{
		int iSyn = 0;
		int iNick = 0;
		String name = wordDict.getName();
		if (null == name) {
			System.out.println("name为空，有误，退出！");
			return;
		}
		OntClass ontClass = getOntClass(name);

		if (null == ontClass) {
			System.out.println("::::::::::::::没有<<" + name + ">>这个概念类啊？:::::::::::::::");
			return;
		}
		AnnotationProperty nickPro = ontModel.getAnnotationProperty(urlBase + "nickname");
		if (null == nickPro) {
			System.out.println("暂时没有nickname这个属性哦！");
			return;
		}

		String allSynName = wordDict.getSynname();
		String allNickname = wordDict.getNickname();

		String[] synNames = allSynName.split(" ");
		String[] nicknames = allNickname.split(" ");
		/************ 添加synName ***************/
		if (null != synNames) {
			for (String synName : synNames) {
				ontClass.addLabel(synName, "ZH");
				iSyn++;
			}
		}

		/************ 添加Nickname *************/
		if (null != nicknames) {
			for (String nickname : nicknames) {
				Literal literal = ontModel.createLiteral(nickname, "zh");
				ontClass.addLiteral(nickPro, literal);
				iNick++;
			}
		}

		System.out.println(name + "共添加" + iSyn + "个label," + iNick + "个nickname");
	}

	/**
	 * 向owl文件中添加多个label，昵称
	 */
	public void addAllWords(String excelFile)
	{

		/****** 从excel中获取同义词和昵称 *******/
		ExcelReadDict excelXml = new ExcelReadDict(excelFile);
		List<OwlClassDict> wordDicts = excelXml.getExcelContent();

		for (OwlClassDict wordDict : wordDicts) {
			addWords(wordDict);
			System.out.println(wordDict + "添加完成！");
		}
		write2file();
	}

	/**
	 * 向概念类中添加ID
	 * 
	 * @param strClass
	 *            类名
	 * @param id
	 *            要添加的ID
	 */
	private void addID(String strClass, String id)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + strClass);
		if (null == ontClass) {
			System.out.println("没有这个概念类:::" + strClass);
			return;
		}
		AnnotationProperty pID = ontModel.getAnnotationProperty(urlBase + "id");
		if (null == pID) {
			System.out.println("没有id属性哦");
			return;
		}
		/******** 判断是否存在ID，若存在，便不再添加 *********/
		Iterator<?> iter = ontClass.listPropertyValues(pID);
		if (!iter.hasNext()) {
			Literal literal = ontModel.createLiteral(id, "en");
			ontClass.addLiteral(pID, literal);
		}
	}

	/**
	 * 批量添加ID
	 * 
	 * @param name
	 *            最顶层概念类的类名
	 */
	public void addIDs(String name)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + name);
		if (null == ontClass) {
			System.out.println("没有这个概念类哦");
			return;
		}
		List<String> classList = findClasses(ontClass);
		for (String strClass : classList) {
			String id = GUID.randomUUID().toString();
			/** java自带的GUID生成器 **/
			// String id = UUID.randomUUID().toString();
			addID(strClass, id);
		}
		write2file();
	}

	/**
	 * 给本体库各个概念词加入重要等级
	 * 
	 * @param filepath
	 *            保存重要等级的txt文件
	 */
	public void addImp(String filepath)
	{
		List<String> list = FileIO.readLines(filepath);

		for (String str : list) {
			String[] names = str.trim().split("\t");
			int len = names.length;
			if (len < 2 || "0".equals(names[1])) {
				continue;
			}
			String name = names[0].trim();
			String imp = names[1].trim();

			OntClass ontClass = getOntClass(name);
			if (null == ontClass) {
				System.out.println(name + "没有。。。");
				continue;
			}
			Literal starLiteral = ontModel.createTypedLiteral(Integer.parseInt(imp), "xsd:positiveInteger");
			AnnotationProperty starPro = ontModel.getAnnotationProperty(urlBase + "imp");
			ontClass.addLiteral(starPro, starLiteral);
			System.out.println("添加了" + name + ":" + imp);
		}
		write2file();
	}

}
