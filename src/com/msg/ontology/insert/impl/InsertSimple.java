package com.msg.ontology.insert.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;

import com.msg.ontology.BasicModel;
import com.msg.ontology.OntologySimple;
import com.msg.ontology.insert.IInsertSimple;
import com.msg.ontology.util.GUID;

public class InsertSimple extends BasicModel implements IInsertSimple
{

	public InsertSimple(OntologySimple ont)
	{
		super(ont);
	}

	@Override
	public OntProperty insertProp(String prop, String strDomain, String strRange)
	{
		OntProperty ontProp = null;
		ontProp = ontModel.getOntProperty(uriBase + prop);
		if (null == ontProp) {
			ontProp = ontModel.createOntProperty(uriBase + prop);
			ontProp.addLabel(prop, "zh");
		} else {
			System.out.println("已经存在属性: " + prop + "，无需添加！");
		}
		Resource domain = null;
		if ("Thing".equals(strDomain)) {
			// ontDomain = ontModel.getOntClass(OWL.Thing.getURI());
			if (OWL.Thing.isResource()) {
				domain = OWL.Thing;
			}
		} else {
			domain = ontModel.getOntClass(uriBase + strDomain);
		}
		ontProp.addDomain(domain);
		OntClass range = null;
		if ("Thing".equals(strRange)) {
			range = ontModel.getOntClass(OWL.Thing.getURI());
		} else {
			range = ontModel.getOntClass(uriBase + strRange);
		}
		ontProp.addRange(range);
		return ontProp;
	}

	@Override
	public OntClass insertClass(String strClass)
	{
		String uri = uriBase + strClass;
		OntClass ontClass = ontModel.getOntClass(uri);
		if (null == ontClass) {
			ontClass = ontModel.createClass(uri);
			ontClass.addLabel(strClass, "zh");
		} else {
			System.out.println("概念类:" + strClass + " 已经存在，无需再次添加");
		}

		return ontClass;
	}

	@Override
	public int insertClasses(List<String> classes)
	{
		int iCount = 0;
		for (String cls : classes) {
			OntClass ontClass = insertClass(cls);
			if (null != ontClass) {
				iCount++;
			} else {
				System.out.println("概念类" + cls + "添加失败");
			}
		}
		return iCount;
	}

	@Override
	public Individual insertInstance(OntClass ontClass, String individual)
	{
		String uri = uriBase + individual;
		Individual instance = ontModel.getIndividual(uri);
		if (null == instance) {
			instance = ontClass.createIndividual(uri);
			instance.addLabel(individual, "zh");
		} else {
			System.out.println("实例： " + individual + " 已经存在，请查询后重新添加");
		}
		return instance;
	}

	@Override
	public boolean insertId(OntClass ontClass)
	{
		boolean flag = false;
		AnnotationProperty ap = ontModel.getAnnotationProperty(uriBase + "id");
		String id = GUID.randomUUID().toString();

		Literal i = ontModel.createLiteral(id, "en");
		RDFNode rn = ontClass.getPropertyValue(ap);
		if (null == rn) {
			ontClass.addLiteral(ap, i);
			flag = true;
		} else {
			System.out.println(ontClass.getLocalName() + "已经存在ID属性");
		}
		return flag;
	}

	@Override
	public int insertIDs(OntClass ontClass)
	{
		int iCount = 0;
		List<OntClass> subclasses = getSubClasses(ontClass);
		for (OntClass subclass : subclasses) {
			if (insertId(subclass)) {
				iCount++;
			}
		}
		return iCount;
	}

	@Override
	public boolean insertLabel(OntClass ontClass)
	{
		boolean flag = false;
		String strClass = ontClass.getLocalName();
		Iterator<?> iterLabel = ontClass.listLabels("zh");
		Literal literal = ontModel.createLiteral(strClass, "zh");
		if (!iterLabel.hasNext()) {
			ontClass.addLabel(literal);
			System.out.println(strClass + "插入Label成功");
			flag = true;
		} else {
			int iCount = 0;
			while (iterLabel.hasNext()) {
				String strLabel = ((Literal) iterLabel.next()).getLexicalForm();
				if (strLabel.equals(strClass)) {
					iCount++;
				}
			}
			if (0 == iCount) {
				ontClass.addLabel(literal);
				System.out.println(strClass + "插入Label成功");
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public int insertLabels(OntClass ontClass)
	{
		int iCount = 0;
		List<OntClass> subclasses = getSubClasses(ontClass);
		for (OntClass subclass : subclasses) {
			if (insertLabel(subclass)) {
				iCount++;
			}
		}
		return iCount;
	}

}
