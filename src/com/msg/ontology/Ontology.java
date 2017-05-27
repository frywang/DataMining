package com.msg.ontology;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class Ontology implements IOntology
{
	private String filepath;
	private OntModel ontModel;

	public Ontology(String filepath, boolean flag)
	{
		this.filepath = filepath;
		this.ontModel = initOntModel(flag);
	}
	public Ontology(String filepath)
	{
		this.filepath = filepath;
		this.ontModel = initOntModel(true);
	}

	public OntModel getOntModel()
	{
		return ontModel;
	}

	@Override
	public OntModel initOntModel(boolean flag)
	{
		//目前只支持owl和ttl
		if (!filepath.endsWith(".owl") && !filepath.endsWith(".ttl")) {
			System.out.println("暂时不支持此格式：" + filepath);
			return null;
		}
		OntModelSpec spec = null;
		if(flag){
			//具备基本的推理功能，能够对概念或属性的传递进行推理
			spec = new OntModelSpec( OntModelSpec.OWL_MEM_TRANS_INF );
		}else{
			//不具备任何推理
			spec = new OntModelSpec( OntModelSpec.OWL_MEM );
		}
		OntModel ontModel = ModelFactory.createOntologyModel(spec);
		
		InputStream fis = null;
		try {
			fis = new FileInputStream(filepath);
			if (filepath.endsWith(".owl")) {
				ontModel.read(fis, null, "RDF/XML-ABBREV");
			} else if (filepath.endsWith(".ttl")) {
				ontModel.read(fis, null, "TURTLE");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ontModel;
	}

	@Override
	public void write2file()
	{
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			if (filepath.endsWith(".owl")) {
				ontModel.write(fos, "RDF/XML-ABBREV");
			} else if (filepath.endsWith(".ttl")) {
				ontModel.write(fos, "TURTLE");
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("写入文件成功！");
	}

	@Override
	public void closeModel()
	{
		if (!ontModel.isClosed()) {
			ontModel.close();
		}
	}

}
