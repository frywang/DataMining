package com.msg.ontology.insert;

import org.apache.jena.ontology.OntClass;

public interface IInsertComplex
{
	public int insertSyn(OntClass ontClass, String[] syns);

	public int insertNick(OntClass ontClass, String[] nicks);

	public int insertAnnos();

	public int insertBasicProps();

}
