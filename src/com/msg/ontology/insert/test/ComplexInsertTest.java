package com.msg.ontology.insert.test;

import com.msg.ontology.OntologySimple;
import com.msg.ontology.excel.impl.ExcelSimple;
import com.msg.ontology.insert.impl.InsertComplex;

public class ComplexInsertTest
{
	public static void main(String[] args)
	{
		String filepath = "E:/Workspace/qieyin/Ontology/qieyinChild.owl";
		String excelpath = "dog.xlsx";
		OntologySimple so = new OntologySimple(filepath);
		ExcelSimple se = new ExcelSimple(excelpath);
		InsertComplex ci = new InsertComplex(so, se);

		//ci.insertAnnos();
		ci.insertPropValue("狗");
		se.closeWorkbook();
		so.write2file();
		so.closeModel();
	}

}
