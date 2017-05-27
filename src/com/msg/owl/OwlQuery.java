package com.msg.owl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.AllValuesFromRestriction;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.MaxCardinalityRestriction;
import org.apache.jena.ontology.MinCardinalityRestriction;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

/**
 * Project Name: Data Author: MSG Time: 2016年9月7日 下午12:04:19
 */

public class OwlQuery extends Owl
{

	public OwlQuery(String filePath)
	{
		super(filePath);
	}

	/**
	 * 根据主语谓语找到宾语
	 * 
	 * @param strClass
	 *            概念词(主语)
	 * @param strProperty
	 *            属性(谓语)
	 * @return 返回宾语
	 */
	public List<String> findObject(String strClass, String strProperty)
	{

		String queryString = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ " PREFIX owl: <http://www.w3.org/2002/07/owl#> "
				+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ " PREFIX child: <http://qieyin/ontologies/child#> " + " SELECT DISTINCT ?restrictionValue "
				+ " WHERE " + " { " + " child:" + strClass + " rdfs:subClassOf ?restriction. "
				+ " ?restriction owl:onProperty child:" + strProperty + ". "
				+ " ?restriction ?restrictionPredicate ?restrictionValue. " + " ?restrictionValue rdf:type owl:Class. "
				+ " ?subject rdf:type owl:Class. " + " } ";
		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();

		List<String> list = new ArrayList<String>();

		while (results.hasNext()) {
			QuerySolution result = results.next();
			RDFNode r = result.get("restrictionValue");
			String ee = r.asResource().getLocalName();
			System.out.println(ee);
		}

		qe.close();
		return list;
	}

	public String searchObjectValue(String cls, String prop)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + cls);
		Property ontProp = getProp(prop);

		if (null == ontClass || null == ontProp) {
			System.out.println("没有<<" + cls + ">>概念和<<" + prop + "属性的搭配");
			return null;
		}
		String aValue = getAvalue(ontClass, ontProp);
		if (null != aValue && !"".equals(aValue)) {
			System.out.println(aValue);
		} else {
			Iterator<OntClass> iterCls = ontClass.listSuperClasses(false);
			while (iterCls.hasNext()) {
				OntClass ontSu = iterCls.next();
				if (ontSu.isRestriction()) {
					Restriction r = ontSu.asRestriction();
					if (r.getOnProperty().getURI().equals(ontProp.getURI())) {
						getRValue(r);
					}
				}
			}
		}
		return null; // ...................
	}

	public String searchDictByClassName(String cls)
	{
		OntClass ontClass = ontModel.getOntClass(urlBase + cls);
		if (null == ontClass) {
			System.out.println("没有<<" + cls + ">>这个类");
			return null;
		}
		OwlClassDict dict = new OwlClassDict();
		dict.setName(cls);

		Property synProp = getProp("synname");
		String synname = getAvalue(ontClass, synProp);
		dict.setSynname(synname);

		Property nickProp = getProp("nickname");
		String nickname = getAvalue(ontClass, nickProp);
		dict.setNickname(nickname);

		Property idProp = getProp("id");
		String id = getAvalue(ontClass, idProp);
		dict.setId(id);

		Property impProp = getProp("imp");
		String imp = getAvalue(ontClass, impProp);
		dict.setImp(imp);

		System.out.println(dict);

		return null;
	}

	private Property getProp(String prop)
	{
		Property ontProp = null;
		if ("synname".equals(prop)) {
			ontProp = RDFS.label;
		} else {
			ontProp = ontModel.getOntProperty(urlBase + prop);
		}
		return ontProp;
	}

	private String getAvalue(OntClass ontClass, Property ontProp)
	{
		Iterator<?> iter = ontClass.listPropertyValues(ontProp);
		StringBuilder sb = new StringBuilder("");
		while (iter.hasNext()) {
			RDFNode node = (RDFNode) iter.next();
			sb.append(node.asLiteral().getLexicalForm() + " ");
		}
		return sb.toString().trim();
	}

	private void getRValue(Restriction r)
	{
		if (r.isSomeValuesFromRestriction()) {
			SomeValuesFromRestriction sfr = r.asSomeValuesFromRestriction();
			Resource resource = sfr.getSomeValuesFrom();

			System.out.println(" some " + resource.getClass());
		} else if (r.isAllValuesFromRestriction()) {
			AllValuesFromRestriction afr = r.asAllValuesFromRestriction();
			Resource resource = afr.getAllValuesFrom();
			System.out.println(" only " + resource.getLocalName());
		} else if (r.isHasValueRestriction()) {
			HasValueRestriction hvr = r.asHasValueRestriction();
			RDFNode node = hvr.getHasValue();
			if (node.isLiteral()) {
				System.out.println(node.asLiteral().getLexicalForm());
			} else if (node.isResource()) {
				System.out.println(node.asResource().getLocalName());
			}
		} else if (r.isMaxCardinalityRestriction()) {
			MaxCardinalityRestriction maxcr = r.asMaxCardinalityRestriction();
			int max = maxcr.getMaxCardinality();
			System.out.println(" max " + max);
		} else if (r.isMinCardinalityRestriction()) {
			MinCardinalityRestriction mincr = r.asMinCardinalityRestriction();
			int min = mincr.getMinCardinality();
			System.out.println(" min " + min);
		} else if (r.isCardinalityRestriction()) {
			CardinalityRestriction cr = r.asCardinalityRestriction();
			int c = cr.getCardinality();
			System.out.println(" exactly " + c);
		} else {
			System.out.println("无法识别的Restriction");
		}
	}

}
