package com.msg.ontology.search.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import com.msg.ontology.Ontology;
import com.msg.ontology.dict.OwlDict;

/**
 * 基本Model类，实现了基本功能
 * @author MSG
 */
public class BasicModel
{
	protected OntModel ontModel;
	protected String uriBase;

	public BasicModel(Ontology os)
	{
		this.ontModel = os.getOntModel();
		this.uriBase = ontModel.getNsPrefixURI("");
	}

	public OntModel getOntModel()
	{
		return ontModel;
	}

	public String getUriBase()
	{
		return uriBase;
	}

	/**
	 * 获得概念父类下的所有子类
	 * @param ontClass 概念父类
	 * @return 返回概念类对象列表
	 */
	public List<OntClass> getSubClasses(OntClass ontClass)
	{
		List<OntClass> clsList = new ArrayList<OntClass>();
		clsList.add(ontClass);
		Iterator<OntClass> subsIter = ontClass.listSubClasses(true);
		while (subsIter.hasNext()) {
			OntClass subclass = subsIter.next();
			clsList.addAll(getSubClasses(subclass));
		}
		return clsList;
	}

	/**
	 * 获得概念父类下的所有子类
	 * @param i 所在的层级
	 * @param ontClass 概念父类
	 * @return 返回所在层级和概念URI组成的字符串列表
	 */
	public List<String> getSubClasses(int i, OntClass ontClass)
	{
		List<String> classes = new ArrayList<String>();
		classes.add(i + " " + ontClass.getURI());
		if (ontClass.hasSubClass()) {
			i++;
			Iterator<OntClass> subclasses = ontClass.listSubClasses(true);
			while (subclasses.hasNext()) {
				OntClass subclass = subclasses.next();
				classes.addAll(getSubClasses(i, subclass));
			}
		}
		return classes;
	}

	/**
	 * 获得概念父类下的所有子类
	 * @param sb 存放概念路径
	 * @param i 所在的层级
	 * @param ontClass 概念父类
	 * @return 返回概念所在路径和深度和概念URI组成的字符串列表
	 */
	public List<String> getSubClasses(StringBuilder sb, int i, OntClass ontClass)
	{
		List<String> classList = new ArrayList<String>();
		if (0 == i) {
			sb.append(ontClass.getLocalName());
		} else {
			sb.append(">>" + ontClass.getLocalName());
		}
		classList.add(sb.toString() + " " + i + " " + ontClass.getURI());
		if (ontClass.hasSubClass()) {
			i++;
			Iterator<OntClass> subclasses = ontClass.listSubClasses(true);
			while (subclasses.hasNext()) {
				OntClass subclass = subclasses.next();
				classList.addAll(getSubClasses(sb, i, subclass));
			}
			if (1 == i) {
				sb.delete(0, sb.length());
			} else {
				sb.delete(sb.lastIndexOf(">>"), sb.length());
			}
		}
		return classList;
	}

	/**
	 * 获得概念子类下的所有父类
	 * @param ontClass 概念子类
	 * @return 概念父类列表(不包含Restriction)
	 */
	public List<OntClass> getSuperClasses(OntClass ontClass)
	{
		List<OntClass> classes = new ArrayList<OntClass>();
		if (!ontClass.isRestriction()) {
			classes.add(ontClass);
		}
		Iterator<OntClass> superclasses = ontClass.listSuperClasses(true);
		while (superclasses.hasNext()) {
			OntClass superclass = superclasses.next();
			classes.addAll(getSuperClasses(superclass));
		}
		return classes;
	}

	/**
	 * 以BufferedWriter的形式写入文本中
	 * @param list Object列表
	 * @param filepath 写出的文件名
	 */
	public void write2buffer(List<?> list, String filepath)
	{
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"));
			for (Object obj : list) {
				bw.write(obj.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("写入文件" + filepath + "成功");
	}
	
	public List<OntClass> getNameBySynAndNick(String name)
	{
		List<OntClass> clsList = new ArrayList<OntClass>();
		ResIterator clsBySyn = ontModel.listSubjectsWithProperty(RDFS.label, name, "zh");
		while (clsBySyn.hasNext()) {
			Resource resource = clsBySyn.nextResource();
			OntClass ontClass = ontModel.getOntClass(resource.getURI());
			if(null == ontClass)
			{
				continue;
			}
			clsList.add(ontClass);
		}
		
		Property nickProp = ontModel.getOntProperty(uriBase + "nickname");
		
		ResIterator clsByNick = ontModel.listSubjectsWithProperty(nickProp, name, "zh");
		while(clsByNick.hasNext())
		{
			Resource resource = clsByNick.nextResource();
			OntClass ontClass = ontModel.getOntClass(resource.getURI());
			if(null == ontClass)
			{
				continue;
			}
			clsList.add(ontClass);
		}
		
		return clsList;
	}

	/**
	 * 根据annotationProperty获取字典列表
	 * @param prop annotation属性
	 * @param anno 属性值
	 * @return 字典列表
	 */
	protected List<OwlDict> getDictByAnno(Property prop, String value)
	{
		List<OwlDict> dicts = new ArrayList<OwlDict>();
		ResIterator res = ontModel.listSubjectsWithProperty(prop, value, "zh");
		while (res.hasNext()) {
			Resource resource = res.nextResource();
			OntClass ontClass = ontModel.getOntClass(resource.getURI());
			if(null == ontClass)
			{
				continue;
			}
			OwlDict dict = getDict(ontClass);
			dicts.add(dict);
		}
		return dicts;
	}

	/**
	 * 获得一个OwlDict对象
	 * @param ontClass 概念类对象
	 * @return OwlDict对象
	 */
	protected OwlDict getDict(OntClass ontClass)
	{
		OwlDict dict = new OwlDict();
		String name = ontClass.getLocalName();
		if (null == name || "".equals(name.trim())) {
			System.out.println("name为空，出错");
			return null;
		}
		dict.setName(name);
		String syn = getAnnoValues(ontClass, RDFS.label);
		
		dict.setSynname(syn);

		Property nickProp = ontModel.getOntProperty(uriBase + "nickname");
		String nick = getAnnoValues(ontClass, nickProp);
		dict.setNickname(nick);

		Property idProp = ontModel.getOntProperty(uriBase + "id");
		String id = getAnnoValues(ontClass, idProp);
		dict.setId(id);

		Property impProp = ontModel.getOntProperty(uriBase + "imp");
		String imp = getAnnoValues(ontClass, impProp);
		dict.setImp(imp);
		return dict;
	}

	/**
	 * 获得AnnotationProperty的值
	 * @param ontClass 概念类
	 * @param prop 属性
	 * @return 返回概念类和属性对应的值
	 */
	private String getAnnoValues(OntClass ontClass, Property prop)
	{
		Iterator<?> iterNick = null;
		if(prop.getURI().equals(RDFS.label.getURI()))
		{
			iterNick = ontClass.listLabels("zh");
		}else{
			iterNick = ontClass.listPropertyValues(prop);
		}
		StringBuilder sbProp = new StringBuilder("");
		int i = 0;
		while (iterNick.hasNext()) 
		{
			String nick = ((Literal) iterNick.next()).getString();
			if (i == 0) {
				sbProp.append(nick);
			} else {
				sbProp.append("_" + nick);
			}
			i++;
		}
		if ("".equals(sbProp.toString())) 
		{
			return " ";
		}
		return sbProp.toString();
	}
	
	public String parentId(String name)
	{
		AnnotationProperty annotationProperty = ontModel.getAnnotationProperty(uriBase + "id");
		OntClass ontClass = ontModel.getOntClass(uriBase + name);
		String value = null;
		if (null != ontClass) {
			Iterator<?> ids = ontClass.listPropertyValues(annotationProperty);
			while (ids.hasNext()) {
				Literal id = (Literal) ids.next();
				value = id.getLexicalForm();
				break;
			}
		}
		return value;
	}
	
}
