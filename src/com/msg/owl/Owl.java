package com.msg.owl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.vocabulary.RDFS;

import com.msg.file.FileIO;

public class Owl
{

	protected String filePath;
	protected String urlBase;
	protected OntModel ontModel;

	public Owl(String filePath)
	{
		this.filePath = filePath;
		this.ontModel = modelConstruct();
		this.urlBase = ontModel.getNsPrefixURI("");
	}

	public String getFilePath()
	{
		return filePath;
	}

	public OntModel getOntModel()
	{
		return ontModel;
	}

	/**
	 * 从owl文件中读取一个OntModel对象
	 * 
	 * @return OntModel 读取到的OntModel对象
	 */
	private OntModel modelConstruct()
	{
		if (!filePath.endsWith(".owl") && !filePath.endsWith(".ttl")) {
			System.out.println("暂时不支持此格式：" + filePath);
			return null;
		}
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		FileInputStream fis = FileIO.openFileInputStream(filePath);

		if (filePath.endsWith(".owl")) {
			ontModel.read(fis, null, RDFLanguages.strLangRDFXML);
		} else if (filePath.endsWith(".ttl")) {
			ontModel.read(fis, null, RDFLanguages.strLangTurtle);
		}

		FileIO.closeFileInputStream(fis);
		return ontModel;
	}

	/**
	 * 根据概念名，获得OntClass对象
	 * 
	 * @param cls
	 *            概念名
	 * @return 获得的OntClass对象
	 */
	public OntClass getOntClass(String cls)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + cls.trim());
		return ontClass;
	}

	/**
	 * 获得Property
	 * 
	 * @param strProp
	 *            属性名
	 * @return 返回属性
	 */
	public Property getOntProperty(String strProp)
	{
		Property property = null;
		if ("synname".equals(strProp)) {
			property = RDFS.label;
		} else {
			property = ontModel.getOntProperty(urlBase + strProp);
		}
		return property;
	}

	/**
	 * 对打印出来的东西进行编排
	 * 
	 * @param iNum
	 *            层次标记数字
	 * @return String 是逗号的个数
	 */
	protected String number(int iNum)
	{
		StringBuilder numberTest = new StringBuilder("");
		for (int i = 0; i < iNum; i++) {
			numberTest.append(",");
		}
		return numberTest.toString();
	}

	/**
	 * 取得一个概念下边所有的子类并以层级结构显示
	 * 
	 * @param num
	 *            数字代表层级
	 * @param ontClass
	 *            最顶层的父类
	 * @return List<String> 返回一个List对象，其中包括了所有的类别
	 */
	protected List<String> findClasses(int num, OntClass ontClass)
	{

		List<String> listClass = new ArrayList<String>();

		/********* 获得概念类的名字 *********/
		String strClass = ontClass.getLocalName();
		// System.out.println(number(num) + strClass);
		listClass.add(number(num) + strClass);
		if (ontClass.hasSubClass()) {
			num++;
			Iterator<OntClass> iter = ontClass.listSubClasses();
			while (iter.hasNext()) {
				OntClass subClass = iter.next();
				listClass.addAll(findClasses(num, subClass));
			}
		}
		return listClass;
	}

	/**
	 * 查找所有类别及子类，直接输出
	 * 
	 * @param ontClass
	 *            最顶层的父类
	 * @return 存放所有子类的列表
	 */
	public List<String> findClasses(OntClass ontClass)
	{
		if (null == ontClass) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		// System.out.println(ontClass.getLocalName());
		list.add(ontClass.getLocalName());

		if (ontClass.hasSubClass()) {
			Iterator<OntClass> iter = ontClass.listSubClasses();
			while (iter.hasNext()) {
				OntClass subClass = iter.next();
				list.addAll(findClasses(subClass));
			}
		}
		return list;
	}

	/**
	 * 获得所有的Property
	 */
	public void getProperties()
	{
		Iterator<OntProperty> ps = ontModel.listAllOntProperties();
		while (ps.hasNext()) {
			OntProperty p = ps.next();
			System.out.println(p.getLocalName());
		}
	}

	/**
	 * 将本体写入文件中
	 */
	public void write2file()
	{
		FileOutputStream fos = FileIO.openFileOutputStream(filePath);

		if (null == fos) {
			System.out.println(filePath + "没有获得文件流");
			return;
		}

		if (filePath.endsWith(".owl")) {
			ontModel.write(fos, RDFLanguages.strLangRDFXML);
		} else if (filePath.endsWith(".ttl")) {
			ontModel.write(fos, RDFLanguages.strLangTurtle);
		}

		FileIO.closeFileOutputStream(fos);
		System.out.println("写入文件成功！");
	}

	/**
	 * 输出到控制台
	 */
	public void write2Console()
	{

		if (filePath.endsWith(".owl")) {
			ontModel.write(System.out, RDFLanguages.strLangRDFXML);
		} else if (filePath.endsWith(".ttl")) {
			ontModel.write(System.out, RDFLanguages.strLangTurtle);
		}
	}

	/**
	 * 关闭OntModel流
	 */
	public void closeModel()
	{
		ontModel.close();
	}

}