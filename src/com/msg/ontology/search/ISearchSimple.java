package com.msg.ontology.search;

import java.util.List;

import com.msg.ontology.dict.OwlDict;

/**
 * 简单查询，主要根据概念名、同义词、昵称、父类、子类、imp查询
 * 
 * @author MSG
 */
public interface ISearchSimple
{
	/**
	 * 根据概念词、同义词或昵称来查询字典
	 * @param name 概念词、同义词或昵称
	 * @return 字典列表
	 */
	public List<OwlDict> searchByNames(String name);
	
	/**
	 * 根据子类查询
	 * @param childname 子类名
	 * @return 概念列表
	 */
	public List<OwlDict> searchByChild(String childname);

	/**
	 * 根据父类查询
	 * @param parentname 父类概念名
	 * @return 概念列表
	 */
	public List<OwlDict> searchByParent(String parentname);

	/**
	 * 根据imp(重要性)查询
	 * @param imp imp值
	 * @return 概念列表
	 */
	public List<OwlDict> searchByImp(String imp);
	
}