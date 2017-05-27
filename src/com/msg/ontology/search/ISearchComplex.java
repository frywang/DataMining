package com.msg.ontology.search;

import java.util.List;

import org.apache.jena.ontology.OntClass;

import com.msg.ontology.dict.TripleDict;

/**
 * 复杂查询，主要查询Restriction和Equivalent
 * @author MSG
 */
public interface ISearchComplex
{
	/**
	 * 查询Restriction
	 * @param name 概念的实际传入值
	 * @param cls 概念类对象
	 * @param prop 属性名
	 * @param value 属性值
	 * @return 对应的三元组
	 */
	public List<TripleDict> searchR(String name, OntClass cls, String prop, String value);

	/**
	 * 查询所有的subclassOf的属性和属性值
	 * @param cls 概念名
	 * @return 由所有属性和属性值组成的字符串
	 */
	public List<TripleDict> searchRs(String cls);
	
	/**
	 * 查询equivalent
	 * @param cls 概念类名
	 * @param prop 属性名
	 * @return 对应的查询值
	 */
	public TripleDict searchE(String cls, String prop);
	
	/**
	 * 查询所有的EquivalentClass的属性和属性值
	 * @param cls 概念名
	 * @return 由所有属性和属性值组成的字符串
	 */
	public String searchEs(String cls);

}
