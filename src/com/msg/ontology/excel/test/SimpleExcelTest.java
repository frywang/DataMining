package com.msg.ontology.excel.test;

import com.msg.ontology.OntologySimple;
import com.msg.ontology.excel.impl.ExcelSimple;
import com.msg.ontology.insert.impl.InsertComplex;

public class SimpleExcelTest
{

	public static void main(String[] args)
	{
		String filepath = "animal.xlsx";
		String owlpath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		OntologySimple ont = new OntologySimple(owlpath);
		ExcelSimple se = new ExcelSimple(filepath);
		InsertComplex ci = new InsertComplex(ont, se);
		// se.getPropFromExcel();
		//ci.insertBasicProps();
		// se.getBehavePropFromExcel();
		ci.insertBehaveProps();
		ont.write2file();
		se.closeWorkbook();
		ont.closeModel();

	}

}
