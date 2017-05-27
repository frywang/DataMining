package com.msg.ontology.search.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.MaxCardinalityRestriction;
import org.apache.jena.ontology.MinCardinalityRestriction;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.msg.ontology.Ontology;
import com.msg.ontology.dict.TripleDict;
import com.msg.ontology.search.ISearchComplex;

public class SearchComplex extends BasicModel implements ISearchComplex
{
	public SearchComplex(Ontology ontology)
	{
		super(ontology);
	}
	
	@Override
	public List<TripleDict> searchR(String name, OntClass ontClass, String prop, String val)
	{
		List<TripleDict> triples = new ArrayList<TripleDict>();
 		TripleDict td = new TripleDict();
 		
 		//设置subject
 		if(null != name && !"".equals(name.trim()))
 		{
 			td.setSubject(name);
 		}
 		
 		//查找英文名，待完善
		if(prop.contains("英文名") || prop.contains("外文名"))
		{
			Iterator<?> enList = ontClass.listLabels("en");
			StringBuilder sb = new StringBuilder("");
			while(enList.hasNext())
			{
				String en = ((Literal) enList.next()).getString();
				sb.append(en + " ");
			}
			String names = sb.toString().trim();
			if(null != val && !"".equals(val.trim())){
				if (val.equals(names) || names.contains(val + " ") || names.contains(" " + val)) {
					td.setFlag(true);
				}else{
					td.setFlag(false);
				}
				td.setPredicate(prop);
				td.setObject(val);
				triples.add(td);
				return triples;
			}else{
				td.setPredicate(prop);
				td.setObject(sb.toString().trim());
				td.setFlag(true);
				triples.add(td);
				return triples;
			}
			
		}
		
		//查找同义词,待完善
		if(prop.contains("别称") || prop.contains("同义词"))
		{
			Iterator<?> enList = ontClass.listLabels("zh");
			StringBuilder sb = new StringBuilder("");
			while(enList.hasNext())
			{
				String syn = ((Literal) enList.next()).getString();
				sb.append(syn + " ");
			}
			td.setPredicate(prop);
			td.setObject(sb.toString().trim());
			td.setFlag(true);
			triples.add(td);
			return triples;
		}
		
		//若是属性无法识别，则直接输出comment
		Property ontProp = ontModel.getOntProperty(uriBase + prop);
		if(null == prop || "".equals(prop) || null == ontProp)
		{
			String comm = ontClass.getComment("zh");
			td.setObject(comm);
			td.setFlag(true);
			triples.add(td);
			return triples;
		}

		td.setPredicate(prop);

		//列出所有的Restriction
		Iterator<OntClass> classes = ontClass.listSuperClasses();
		
		String value = null;
		while (classes.hasNext()) {
			OntClass ontSu = classes.next();
			if (ontSu.isRestriction()) {
				Restriction r = ontSu.asRestriction();
				if (r.getOnProperty().getURI().equals(ontProp.getURI())) {
					value = getRValue(r);
					break;
				}
			}
		}
		
		if(null == val || "".equals(val.trim())){
			td.setObject(value);
			td.setFlag(true);
		}else{
			if(value.contains(val)){
				td.setObject(val);
				td.setFlag(true);
			}else{
				td.setObject(val);
				td.setFlag(false);
			}
		}
		
		triples.add(td);
		return triples;
	}
	
	/**
	 * 识别出同义词和昵称
	 * @param name
	 * @param prop
	 * @param val
	 * @return List<TripleDict> triple列表
	 */
	public List<TripleDict> searchRBySynAndNick(String name, String prop, String val)
	{
		List<OntClass> clsList = getNameBySynAndNick(name);
		List<TripleDict> triples = new ArrayList<TripleDict>();
		if(0 == clsList.size())
		{
			triples = getSubject(prop,val.trim());
			return triples;
		}
		for(OntClass cls: clsList)
		{
			triples.addAll(searchR(name,cls,prop,val));
		}
		return triples;
	}
	
	/**
	 * 根据属性和属性值确定概念
	 * @param prop 属性
	 * @param val 属性值
	 * @return 返回三元组的列表
	 */
	public List<TripleDict> getSubject(String prop, String val)
	{
		List<TripleDict> lists = new ArrayList<TripleDict>();
		OntProperty ontProp = ontModel.getOntProperty(uriBase + prop);
		if(null != ontProp)
		{
			Iterator<Restriction> rIter = ontProp.listReferringRestrictions();
			while(rIter.hasNext())
			{
				Restriction r = rIter.next();
				String rvalue = getRValue(r);
				if(rvalue.equals(val) || rvalue.contains(val + " ") || rvalue.contains(" " + val))
				{
					List<TripleDict> list = new ArrayList<TripleDict>();
					Iterator<OntClass> clsIter = r.listSubClasses(true);
					while(clsIter.hasNext())
					{
						TripleDict td = new TripleDict();
						td.setPredicate(prop);
						td.setObject(val);
						OntClass on = clsIter.next();
						if(on.isURIResource())
						{
							String subject = on.getLocalName();
							if(null != subject)
							{
								td.setSubject(subject);
								td.setFlag(true);
							}
						}
						list.add(td);
					}
					lists.addAll(list);
				}
			}
		}
		return lists;
	}

	/**
	 * 获得restriction的属性值
	 * @param r Restriction
	 * @return 返回Restriction对应的值
	 */
	private String getRValue(Restriction r)
	{
		String rValue = null;
		if (r.isSomeValuesFromRestriction() || r.isAllValuesFromRestriction()) {
			rValue = getValue(r);
		} else if (r.isHasValueRestriction()) {
			HasValueRestriction hvr = r.asHasValueRestriction();
			rValue = getHasValue(hvr);
		} else if (r.isMaxCardinalityRestriction()) {
			MaxCardinalityRestriction maxcr = r.asMaxCardinalityRestriction();
			int max = maxcr.getMaxCardinality();
			rValue = max + "";
		} else if (r.isMinCardinalityRestriction()) {
			MinCardinalityRestriction mincr = r.asMinCardinalityRestriction();
			int min = mincr.getMinCardinality();
			rValue = min + "";
		} else if (r.isCardinalityRestriction()) {
			CardinalityRestriction cr = r.asCardinalityRestriction();
			int c = cr.getCardinality();
			rValue = c + "";
		} else {
			System.out.println("无法识别的Restriction");
		}
		return rValue;
	}
	
	/**
	 * HasValueRestriction的值
	 * @param hvr HasValueRestriction对象
	 * @return HasValueRestriction的值
	 */
	private String getHasValue(HasValueRestriction hvr)
	{
		String hasvalue = null;
		RDFNode node = hvr.getHasValue();
		if (node.isLiteral()) {
			hasvalue = node.asLiteral().getLexicalForm();
		} else if (node.isResource()) {
			hasvalue = node.asResource().getLocalName();
		}
		return hasvalue;
	}
	
	/**
	 * 查找到相同的父类
	 * @param cls1 概念名
	 * @param cls2 概念名
	 * @return 返回相同的概念父类名
	 */
	public String getSameParent(String cls1, String cls2)
	{
		String parent = null;
		OntClass ont1 = ontModel.getOntClass(uriBase + cls1);
		OntClass ont2 = ontModel.getOntClass(uriBase + cls2);
		OntClass ontp = OntTools.getLCA(ontModel,ont1,ont2);
		parent = ontp.getLocalName();
		return parent;
	}
	
	/**
	 * 获得AllValuesFromRestriction的值
	 * @param afr AllValuesFromRestriction对象
	 * @return 值
	 */
	private String getValue(Restriction restriction)
	{
		String values = null;
		Resource resource = null;
		if (restriction.isAllValuesFromRestriction()) {
			resource = restriction.asAllValuesFromRestriction().getAllValuesFrom();
		}else if (restriction.isSomeValuesFromRestriction()) {
			resource = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
		}
		String value = resource.getLocalName();
		if (null != value) {
			values = value.trim();
		} else {
			OntClass ont = (OntClass) resource;
			values = getUIValue(ont);
		}
		return values;
	}

	/**
	 * 获得unionclass或者intersectionclass的值
	 * @param ont 
	 * @return
	 */
	public String getUIValue(OntClass ont)
	{
		Iterator<?> iter = null;
		if (ont.isUnionClass()) {
			iter = ont.asUnionClass().listOperands();
		}else if(ont.isIntersectionClass()){
			iter = ont.asIntersectionClass().listOperands();
		}
		StringBuilder sb = new StringBuilder("");
		while (iter.hasNext()) {
			sb.append(((OntClass) iter.next()).getLocalName() + " ");
		}
		return sb.toString().trim();
	}

	@Override
	public List<TripleDict> searchRs(String cls)
	{
		List<TripleDict> tdList = new ArrayList<TripleDict>();
		OntClass ontClass = ontModel.getOntClass(uriBase + cls);
		if (null != ontClass) {
			Iterator<OntClass> classes = ontClass.listSuperClasses();
			while (classes.hasNext()) 
			{
				OntClass ontSu = classes.next();
				if (ontSu.isRestriction()) 
				{
					TripleDict td = new TripleDict();
					td.setSubject(cls);
					Restriction r = ontSu.asRestriction();
					String prop = r.getOnProperty().getLocalName();
					td.setPredicate(prop);
					String value = getRValue(r);
					td.setObject(value);
					td.setFlag(true);
					tdList.add(td);
				}
			}
		}else{
			System.out.println("没有概念：" + cls);
		}
		return tdList;
	}

	@Override
	public TripleDict searchE(String cls, String prop)
	{
		return null;
	}

	@Override
	public String searchEs(String cls)
	{
		return null;
	}

}
