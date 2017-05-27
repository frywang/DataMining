package com.msg.ontology.delete.test;

import com.msg.ontology.OntologySimple;
import com.msg.ontology.delete.impl.DeleteSimple;

public class SimpleDeleteTest
{

	public static void main(String[] args)
	{
		String filepath = "qieyinChild.owl";
		OntologySimple so = new OntologySimple(filepath);
		DeleteSimple sd = new DeleteSimple(so);
		sd.deleteProps();
		so.write2file();
		so.closeModel();
	}

}
