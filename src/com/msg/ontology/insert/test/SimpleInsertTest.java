package com.msg.ontology.insert.test;
/*
 * 添加知识库里到uri，添加uri,添加label,
 */
import org.apache.jena.ontology.OntClass;

import com.msg.ontology.OntologySimple;
import com.msg.ontology.insert.impl.InsertSimple;

public class SimpleInsertTest
{

	public static void main(String[] args)
	{
		String filepath = "/home/fry/Documents/svn/ai/trunk/ontology/qieyinChild.owl";
		OntologySimple ont = new OntologySimple(filepath);
		InsertSimple si = new InsertSimple(ont);
		// si.insertProp("TestProp", "Thing", "Test");
		OntClass ontClass = si.getOntModel().getOntClass(si.getUriBase() + "植物");
		int i = si.insertIDs(ontClass);
		si.insertLabels(ontClass);
		System.out.println(i);
		ont.write2file();
		ont.closeModel();
	}

}
