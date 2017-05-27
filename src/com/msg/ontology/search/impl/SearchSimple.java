package com.msg.ontology.search.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import com.msg.ontology.Ontology;
import com.msg.ontology.dict.OwlDict;
import com.msg.ontology.search.ISearchSimple;

public class SearchSimple extends BasicModel implements ISearchSimple
{
	public SearchSimple(Ontology ont)
	{
		super(ont);
	}

	public OwlDict searchByName(String name)
	{
		OntClass ontClass = ontModel.getOntClass(uriBase + name);
		if (null == ontClass) {
			System.out.println("概念类" + name + "不存在！");
			return null;
		}
		OwlDict dict = getDict(ontClass);
		return dict;
	}

	@Override
	public List<OwlDict> searchByNames(String name)
	{
		List<OwlDict> dicts = getDictByAnno(RDFS.label, name);
		Property prop = ontModel.getOntProperty(uriBase + "nickname");
		if (null != prop) {
			dicts.addAll(getDictByAnno(prop, name));
		}
		return dicts;
	}

	@Override
	public List<OwlDict> searchByChild(String childname)
	{
		OntClass child = ontModel.getOntClass(uriBase + childname);
		if (null == child) {
			System.out.println("概念类" + childname + "不存在");
			return null;
		}
		List<OntClass> classes = getSuperClasses(child);
		List<OwlDict> dicts = new ArrayList<OwlDict>();
		for (OntClass cls : classes) {
			OwlDict dict = getDict(cls);
			dicts.add(dict);
		}
		return dicts;
	}

	@Override
	public List<OwlDict> searchByParent(String parentname)
	{
		OntClass parent = null;
		if("Thing".equals(parentname))
		{
			parent = ontModel.getOntClass(OWL.Thing.getURI());
		}else{
			parent = ontModel.getOntClass(uriBase + parentname);
		}
		if (null == parent) {
			System.out.println("概念类" + parentname + "不存在");
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		List<String> classes = getSubClasses(sb, 0, parent);
		List<OwlDict> dicts = new ArrayList<OwlDict>();

		for (String cls : classes) {
			String[] clses = cls.split(" ");
			String[] parents = clses[0].split(">>");
			int sizeOfParents = parents.length;
			OntClass ontClass = ontModel.getOntClass(clses[2]);
			OwlDict dict = getDict(ontClass);
			dict.setLevel(clses[1]);
			dict.setPath(clses[0]);
			if(1 == sizeOfParents)
			{
				dict.setParentname("Thing");
				dict.setParent_id(" ");
			}else{
				String parentName = parents[sizeOfParents - 2];
				dict.setParentname(parentName);
				dict.setParent_id(parentId(parentName));
			}
			dicts.add(dict);
		}

		return dicts;
	}

	@Override
	public List<OwlDict> searchByImp(String imp)
	{
		Property prop = ontModel.getAnnotationProperty(uriBase + "imp");
		if (null == prop) {
			System.out.println("没有imp这个属性");
			return null;
		}
		List<OwlDict> dicts = new ArrayList<OwlDict>();
		ResIterator res = ontModel.listSubjectsWithProperty(prop);
		while (res.hasNext()) {
			Resource resource = res.nextResource();
			OwlDict dict = getDict((OntClass) resource);
			if (dict.getImp().equals(imp)) {
				dicts.add(dict);
			}
		}

		return dicts;
	}

	
	/**
	 * 导出本体库中所有的概念词、同义词、昵称直接输出到文件
	 * @param ontClass 概念类对象
	 */
	public List<String> getThings(String strClass)
	{
		OntClass ontClass = null;
		if ("Thing".equals(strClass)) {
			ontClass = ontModel.getOntClass(OWL.Thing.getURI());
		} else {
			ontClass = ontModel.getOntClass(uriBase + strClass);
		}
		List<String> classes = getSubClasses(0, ontClass);
		List<String> lists = new ArrayList<String>();

		for (String strs : classes) {
			String star = strs.split(" ")[0];
			String str = strs.split(" ")[1];
			OntClass cls = ontModel.getOntClass(str);
			if (null == cls) {
				System.out.println(str);
				continue;
			}
			OwlDict dict = getDict(cls);
			String imp = dict.getImp();
			if ("".equals(imp.trim())) {
				imp = "0";
			}
			lists.add(dict.getName() + "\t" + star + "\t" + imp);
			String syns = dict.getSynname().trim();

			if (!"".equals(syns)) {
				if (syns.contains("_")) {
					String[] syn = syns.split("_");
					for (String s : syn) {
						if (!s.equals(dict.getName())) {
							lists.add(s.trim() + "\t" + star + "\t" + imp);
						}
					}
				} else {
					if (!syns.equals(dict.getName())) {
						lists.add(syns + "\t" + star + "\t" + imp);
					}
				}
			}

			String nicks = dict.getNickname().trim();
			if (!"".equals(nicks)) {
				if (nicks.contains("_")) {
					String[] nick = nicks.split("_");
					for (String s : nick) {
						lists.add(s.trim() + "\t" + star + "\t" + imp);
					}
				} else {
					lists.add(nicks + "\t" + star + "\t" + imp);
				}
			}
		}
		return lists;
	}
	
	public List<String> getAttrs()
	{
		List<String> attrList = new ArrayList<String>();
		Iterator<OntProperty> propIter = ontModel.listAllOntProperties();
		while(propIter.hasNext())
		{
			OntProperty ontProp = propIter.next();
			String attr = ontProp.getLocalName();
			attrList.add(attr);
		}
		return attrList;
	}

}
