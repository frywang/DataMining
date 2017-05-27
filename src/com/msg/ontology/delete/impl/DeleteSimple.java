package com.msg.ontology.delete.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;

import org.apache.jena.ontology.OntResource;

import com.msg.ontology.BasicModel;
import com.msg.ontology.OntologySimple;
import com.msg.ontology.delete.IDeleteSimple;

public class DeleteSimple extends BasicModel implements IDeleteSimple
{

	public DeleteSimple(OntologySimple ont)
	{
		super(ont);
	}

	@Override
	public int deleteProps()
	{
		//Iterator<?> ontProp = ontModel.listObjectProperties();
		Iterator<?> ontProp = ontModel.listDatatypeProperties();
		while (ontProp.hasNext()) {
			DatatypeProperty op = (DatatypeProperty) ontProp.next();
			Iterator<?> iter = op.listDomain();
			List<OntResource> res = new ArrayList<>();
			while(iter.hasNext()){
				OntResource ont = (OntResource)iter.next();
				res.add(ont);
			}
			for(OntResource ontr: res){
				op.removeDomain(ontr);
			}
		}
	
		return 0;
	}

}
