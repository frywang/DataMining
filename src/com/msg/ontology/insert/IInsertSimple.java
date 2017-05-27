package com.msg.ontology.insert;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

public interface IInsertSimple
{
	/**
	 * 插入单个属性
	 * @param prop 属性名
	 * @param strDomain 属性定义域
	 * @param strRange 属性值域
	 * @return 返回插入的属性对象
	 */
	public OntProperty insertProp(String prop, String strDomain, String strRange);

	/**
	 * 插入一个概念类
	 * @param strClass 概念名
	 * @return 返回插入的概念对象
	 */
	public OntClass insertClass(String strClass);

	/**
	 * 插入实例
	 * @param ontClass 要插入实例的概念对象
	 * @param instance 实例名
	 * @return 返回插入的实例对象
	 */
	public Individual insertInstance(OntClass ontClass, String instance);

	/**
	 * 插入随机生成的ID
	 * @param ontClass 要插入ID的概念对象
	 */
	public boolean insertId(OntClass ontClass);

	/**
	 * 插入与概念名相同的Label
	 * @param ontClass 概念对象
	 * @return true表明插入成功
	 */
	public boolean insertLabel(OntClass ontClass);

	/**
	 * 批量添加概念类
	 * @param classes 由概念名组成的列表
	 * @return 返回插入的概念数目
	 */
	public int insertClasses(List<String> classes);

	/**
	 * 批量插入ID
	 * @param ontClass 要插入ID的概念父类
	 * @return 返回插入的条数
	 */
	public int insertIDs(OntClass ontClass);

	/**
	 * 批量插入与概念名相同的Label
	 * @param ontClass 
	 * @return
	 */
	public int insertLabels(OntClass ontClass);
}
