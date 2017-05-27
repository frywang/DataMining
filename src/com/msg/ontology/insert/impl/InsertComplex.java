package com.msg.ontology.insert.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.XSD;

import com.msg.ontology.BasicModel;
import com.msg.ontology.OntologySimple;
import com.msg.ontology.dict.OwlDict;
import com.msg.ontology.excel.impl.ExcelSimple;
import com.msg.ontology.insert.IInsertComplex;

public class InsertComplex extends BasicModel implements IInsertComplex
{
	private ExcelSimple se;

	public InsertComplex(OntologySimple so, ExcelSimple se)
	{
		super(so);
		this.se = se;
	}

	@Override
	public int insertSyn(OntClass ontClass, String[] syns)
	{
		int iCount = 0;
		for (String syn : syns) {
			ontClass.addLabel(syn, "zh");
			iCount++;
		}
		return iCount;
	}

	@Override
	public int insertNick(OntClass ontClass, String[] nicks)
	{
		int iCount = 0;
		AnnotationProperty apNick = ontModel.getAnnotationProperty(uriBase + "nickname");
		for (String nick : nicks) {
			Literal literal = ontModel.createLiteral(nick, "zh");
			ontClass.addLiteral(apNick, literal);
			iCount++;
		}
		return iCount;
	}

	@Override
	public int insertAnnos()
	{
		int iCount = 0;
		List<OwlDict> dicts = se.getAnnoFromExcel();
		for (OwlDict dict : dicts) {
			String name = dict.getName();
			String syns = dict.getSynname();
			String nicks = dict.getNickname();
			if (null != name) {
				OntClass ontClass = ontModel.getOntClass(uriBase + name);
				if (null != ontClass) {
					if (null != syns) {
						String[] synSplit = syns.split(" ");
						insertSyn(ontClass, synSplit);
					}

					if (null != nicks) {
						String[] nickSplit = nicks.split(" ");
						insertNick(ontClass, nickSplit);
					}
					iCount++;
				}
			}
		}
		return iCount;
	}

	@Override
	public int insertBasicProps()
	{
		Map<String, String> props = se.getBasicPropFromExcel();

		Set<String> sets = props.keySet();
		int iCount = 0;
		for (String set : sets) {
			String[] splits = props.get(set).split("#");
			ObjectProperty ontProp = null;
			if ("ObjectProperty".equals(splits[0])) {
				ontProp = ontModel.getObjectProperty(uriBase + set);
				if (null == ontProp) {
					ontProp = ontModel.createObjectProperty(uriBase + set);
					ontProp.addLabel(set, "zh");
				}
			}

			if (!"null".equals(splits[1])) {
				ObjectProperty superProp = ontModel.getObjectProperty(uriBase + splits[1]);
				if (null == superProp) {
					superProp = ontModel.createObjectProperty(uriBase + splits[1]);
				}
				ontProp.addSuperProperty(superProp);
			}

			// ontModel.createDatatypeProperty("");

			if (!"null".equals(splits[2])) {
				Resource domain = null;
				if ("Thing".equals(splits[2])) {
					if (OWL.Thing.isResource()) {
						domain = OWL.Thing;
					}
				} else {
					domain = ontModel.getOntClass(uriBase + splits[2]);
				}
				if (null != domain) {
					ontProp.addDomain(domain);
				} else {
					System.out.println(splits[2] + "不存在！");
				}
			}

			if (!"null".equals(splits[3])) {
				Resource range = null;
				if ("Thing".equals(splits[3])) {
					if (OWL.Thing.isResource()) {
						range = OWL.Thing;
						ontProp.addRange(range);
						iCount++;
					}
				} else if (splits[3].contains("，")) {
					String[] ranges = splits[3].split("，");
					for (String rang : ranges) {
						range = ontModel.getOntClass(uriBase + rang);
						if (null == rang) {
							System.out.println(rang + "不存在");
						}
						ontProp.addRange(range);
						iCount++;
					}
				} else {
					range = ontModel.getOntClass(uriBase + splits[3]);
					if (null == range) {
						System.out.println("没有" + splits[3]);
						continue;
					}
					ontProp.addRange(range);
					iCount++;
				}
			}

		}
		return iCount;
	}

	public int insertBehaveProps()
	{
		Map<String, String> props = se.getBehavePropFromExcel();

		Set<String> sets = props.keySet();
		int iCount = 0;
		for (String set : sets) {
			String[] splits = props.get(set).split("#");
			if ("DatatypeProperty".equals(splits[0])) {
				DatatypeProperty dp = ontModel.getDatatypeProperty(uriBase + set);
				if (null == dp) {
					dp = ontModel.createDatatypeProperty(uriBase + set);
				}

				String str = splits[2];
				if ("boolean".equals(str)) {
					dp.addRange(XSD.xboolean);
				} else if ("positiveInteger".equals(str)) {
					dp.addRange(XSD.positiveInteger);
				} else if ("String".equals(str)) {
					dp.addRange(XSD.xstring);
				}

				String sup = splits[1];
				if (!"null".equals(sup)) {
					DatatypeProperty supdp = ontModel.getDatatypeProperty(uriBase + sup);
					if (null == supdp) {
						supdp = ontModel.createDatatypeProperty(uriBase + sup);
					}
					dp.addSuperProperty(supdp);
				}
			}
		}
		return iCount;
	}
	
	public int insertPropValue(String name)
	{
		Map<String, String> props = se.getBehaveValueFromExcel();
		OntClass ontClass = ontModel.getOntClass(uriBase + name);
		if(null == ontClass)
		{
			System.out.println("_______________________________________________________");
			return -1;
		}
		Set<String> sets = props.keySet();
		int iCount = 0;
		for (String set : sets) {
			String[] values  = props.get(set).split("#");
			if(values.length != 2)
			{
				continue;
			}
			DatatypeProperty prop = ontModel.getDatatypeProperty(uriBase + set);
			Literal literal = null;
			if(!"null".equals(values[1]))
			{
				if(values[0].equals("boolean")){
					if("true".equals(values[1])){
						literal = ontModel.createTypedLiteral(true);
					}else if ("false".equals(values[1])){
						literal = ontModel.createTypedLiteral(false);
					}
					
				}else if(values[0].equals("String")){
					literal = ontModel.createTypedLiteral(values[1]);
				}
				HasValueRestriction hvr = ontModel.createHasValueRestriction(null, prop, literal);
				//ontClass.setSuperClass(hvr);
				//ontClass.s
				hvr.setSubClass(ontClass);
			}
			
		}
		return iCount;
	}

}
